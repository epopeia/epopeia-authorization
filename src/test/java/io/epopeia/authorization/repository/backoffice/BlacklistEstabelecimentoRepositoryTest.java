package io.epopeia.authorization.repository.backoffice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.BackofficeH2Config;
import io.epopeia.authorization.builder.BlacklistBuilder;
import io.epopeia.authorization.domain.backoffice.BlacklistEstabelecimento;
import io.epopeia.authorization.faker.BlacklistFaker.EBlacklistFaker;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.BlacklistEstabelecimentoRepository;

/**
 * Teste do repositorio de blacklist e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class BlacklistEstabelecimentoRepositoryTest {

	@Autowired
	private BlacklistEstabelecimentoRepository blacklist;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private BlacklistBuilder builder;

	private static final String TB_BLACKLISTESTAB = "BLACKLISTESTABELECIMENTOS";
	private static final String[] TB_BLACKLISTESTAB_COLUMNS = new String[] {
			"codigoadquirente", "codigoestabelecimento" };
	private static final String[] TB_BLACKLISTESTAB_PKS = new String[] { "codigoadquirente",
			"codigoestabelecimento" };
	private static final String[] TB_BLACKLISTESTAB_FKS = new String[] {};

	public BlacklistEstabelecimentoRepositoryTest() {
		this.builder = new BlacklistBuilder();
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_BLACKLISTESTAB, TB_BLACKLISTESTAB_COLUMNS,
				TB_BLACKLISTESTAB_PKS, TB_BLACKLISTESTAB_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = blacklist.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// create a product with two modalities
		BlacklistEstabelecimento wl = builder
				.blacklistEstabelecimento(EBlacklistFaker.EstabelecimentoUmRede)
				.create();

		// Manual flush is required to avoid false positive in test
		blacklist.saveAndFlush(wl);
		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				blacklist.count());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarProdutosEModalidadesEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		BlacklistEstabelecimento bl = blacklist
				.findByCodigoAdquirenteAndCodigoEstabelecimento(
						EBlacklistFaker.EstabelecimentoUmRede.getCodigoAdquirente(), 
						EBlacklistFaker.EstabelecimentoUmRede.getCodigoEstabelecimento());

		assertNotNull("BlacklistEstabelecimento nao encontrado", bl);
		assertEquals("Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
	}

	/**
	 * execute "tear down" logic within the transaction
	 */
	@After
	public void removeMassaDeDadosComitadosNosTestes() {
		// default rollback is already activated
	}

	/**
	 * logic to verify the final state after transaction has rolled back
	 */
	@AfterTransaction
	public void baseDeveEstarVaziaAoFinalDoTeste() {
		final long count = blacklist.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
