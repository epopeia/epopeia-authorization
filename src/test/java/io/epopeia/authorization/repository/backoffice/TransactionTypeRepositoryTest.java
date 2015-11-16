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
import io.epopeia.authorization.builder.TransactionTypeBuilder;
import io.epopeia.authorization.domain.backoffice.TransactionType;
import io.epopeia.authorization.faker.TransactionTypeFaker.ETransactionType;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.TransactionTypeRepository;

/**
 * Teste do repositorio de tipos transacoes
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class TransactionTypeRepositoryTest {

	@Autowired
	private TransactionTypeRepository tiposTransacoes;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private TransactionTypeBuilder builder;

	private static final String TB_TIPOSTRANSACOES = "TIPOSTRANSACOES";
	private static final String[] TB_TIPOSTRANSACOES_COLUMNS = new String[] {
			"codigotipotransacao", "fatorsensibilizacao", "identificador",
			"liquidacaoimediata", "verificarcartaoativo",
			"verificarcontatitularativa", "verificarvalidadecartao" };
	private static final String[] TB_TIPOSTRANSACOES_PKS = new String[] { "codigotipotransacao" };
	private static final String[] TB_TIPOSTRANSACOES_FKS = new String[] {};

	public TransactionTypeRepositoryTest() {
		this.builder = new TransactionTypeBuilder();
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_TIPOSTRANSACOES,
				TB_TIPOSTRANSACOES_COLUMNS, TB_TIPOSTRANSACOES_PKS,
				TB_TIPOSTRANSACOES_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = tiposTransacoes.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// create a transaction type
		TransactionType tt = builder.transactionType(
				ETransactionType.COMPRA_CREDITO_A_VISTA).create();

		// Manual flush is required to avoid false positive in test
		tiposTransacoes.saveAndFlush(tt);
		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				tiposTransacoes.count());
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
		TransactionType tt = tiposTransacoes
				.findByIdentificador(ETransactionType.COMPRA_CREDITO_A_VISTA
						.name());

		assertNotNull("TipoTransacao nao encontrado", tt);
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
		final long count = tiposTransacoes.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
