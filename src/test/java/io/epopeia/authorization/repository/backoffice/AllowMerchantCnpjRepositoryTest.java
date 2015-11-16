package io.epopeia.authorization.repository.backoffice;

import static org.junit.Assert.assertEquals;

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
import io.epopeia.authorization.faker.AllowMerchantCnpjFaker;
import io.epopeia.authorization.faker.AllowMerchantCnpjFaker.EAllowMerchantCnpj;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.AllowMerchantCnpjRepository;

/**
 * Teste do repositorio de restricoes cnpj e suas entidades
 * envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class AllowMerchantCnpjRepositoryTest {

	@Autowired
	private AllowMerchantCnpjRepository restricoesCnpj;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private static final String TB_RESTRICOESCNPJ = "RESTRICOESCNPJ";
	private static final String[] TB_RESTRICOESCNPJ_COLUMNS = new String[] {
			"codigogruporestricao", "cnpj" };
	private static final String[] TB_RESTRICOESCNPJ_PKS = new String[] {
			"codigogruporestricao", "cnpj" };
	private static final String[] TB_RESTRICOESCNPJ_FKS = new String[] {};

	public AllowMerchantCnpjRepositoryTest() {
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_RESTRICOESCNPJ, TB_RESTRICOESCNPJ_COLUMNS,
				TB_RESTRICOESCNPJ_PKS, TB_RESTRICOESCNPJ_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = restricoesCnpj.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// Manual flush is required to avoid false positive in test
		for (EAllowMerchantCnpj am : EAllowMerchantCnpj.values()) {
			restricoesCnpj.saveAndFlush(AllowMerchantCnpjFaker
					.getAllowMerchantCnpj(am));
		}
		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				restricoesCnpj.count());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarPermissoesPraUmCnpjDeUmGrupoEspecificoEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesCnpj
				.countByCodigoGrupoRestricaoAndCnpj(
						EAllowMerchantCnpj.MinhaCasaMelhor.getCodigoGrupoRestricao(),
						EAllowMerchantCnpj.MinhaCasaMelhor.getCnpj());

		assertEquals("Permissoes nao econtradas", 1, countAllows.longValue());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarEmUmaUnicaQueryENaoEncontrarPermissoesCnpjDeUmGrupoRestricaoNaoCadastrado()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesCnpj
				.countByCodigoGrupoRestricaoAndCnpj(
						0L,
						EAllowMerchantCnpj.MinhaCasaMelhor.getCnpj());

		assertEquals("Permissoes nao econtradas", 0, countAllows.longValue());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarEmUmaUnicaQueryENaoEncontrarPermissoesDeUmGrupoRestricaoComCnpjNaoCadastrado()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesCnpj
				.countByCodigoGrupoRestricaoAndCnpj(
						EAllowMerchantCnpj.MinhaCasaMelhor.getCodigoGrupoRestricao(),
						"99999999999999");

		assertEquals("Permissoes nao econtradas", 0, countAllows.longValue());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
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
		final long count = restricoesCnpj.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
