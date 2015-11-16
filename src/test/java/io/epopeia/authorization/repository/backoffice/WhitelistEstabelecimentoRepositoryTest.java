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
import io.epopeia.authorization.builder.WhitelistBuilder;
import io.epopeia.authorization.domain.backoffice.WhitelistEstabelecimento;
import io.epopeia.authorization.faker.WhitelistFaker.EWhitelistFaker;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.WhitelistEstabelecimentosRepository;

/**
 * Teste do repositorio de whitelist e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class WhitelistEstabelecimentoRepositoryTest {

	@Autowired
	private WhitelistEstabelecimentosRepository whitelist;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private WhitelistBuilder builder;

	private static final String TB_WHITELISTESTAB = "WHITELISTESTABELECIMENTOS";
	private static final String[] TB_WHITELISTESTAB_COLUMNS = new String[] {
			"codigoadquirente", "codigoestabelecimento", "cvc2ausente", "estabelecimento" };
	private static final String[] TB_WHITELISTESTAB_PKS = new String[] { "codigoadquirente",
			"codigoestabelecimento" };
	private static final String[] TB_WHITELISTESTAB_FKS = new String[] {};

	public WhitelistEstabelecimentoRepositoryTest() {
		this.builder = new WhitelistBuilder();
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_WHITELISTESTAB, TB_WHITELISTESTAB_COLUMNS,
				TB_WHITELISTESTAB_PKS, TB_WHITELISTESTAB_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = whitelist.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// create a product with two modalities
		WhitelistEstabelecimento wl = builder
				.whitelistEstabelecimento(EWhitelistFaker.EstabelecimentoUmRede)
				.withCvc2EmptyPermission().create();

		// Manual flush is required to avoid false positive in test
		whitelist.saveAndFlush(wl);
		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				whitelist.count());
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
		WhitelistEstabelecimento wl = whitelist
				.findByCodigoAdquirenteAndCodigoEstabelecimento(
						EWhitelistFaker.EstabelecimentoUmRede.getCodigoAdquirente(), 
						EWhitelistFaker.EstabelecimentoUmRede.getCodigoEstabelecimento());

		assertNotNull("WhitelistEstabelecimento nao encontrado", wl);
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
		final long count = whitelist.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
