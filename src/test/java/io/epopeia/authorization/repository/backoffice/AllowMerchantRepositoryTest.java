package io.epopeia.authorization.repository.backoffice;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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
import io.epopeia.authorization.faker.AllowMerchantFaker;
import io.epopeia.authorization.faker.AllowMerchantFaker.EAllowMerchant;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.AllowMerchantRepository;

/**
 * Teste do repositorio de restricoes estabelecimentos e suas entidades
 * envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class AllowMerchantRepositoryTest {

	@Autowired
	private AllowMerchantRepository restricoesEstabelecimento;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private static final String TB_RESTRICOES = "RESTRICOES";
	private static final String[] TB_RESTRICOES_COLUMNS = new String[] {
			"restricao", "codigogruporestricao", "mcc", "codigoadquirente",
			"codigoestabelecimento" };
	private static final String[] TB_RESTRICOES_PKS = new String[] { "restricao" };
	private static final String[] TB_RESTRICOES_FKS = new String[] {};

	public AllowMerchantRepositoryTest() {
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_RESTRICOES, TB_RESTRICOES_COLUMNS,
				TB_RESTRICOES_PKS, TB_RESTRICOES_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = restricoesEstabelecimento.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// Manual flush is required to avoid false positive in test
		for (EAllowMerchant am : EAllowMerchant.values()) {
			restricoesEstabelecimento.saveAndFlush(AllowMerchantFaker
					.getAllowMerchant(am));
		}
		assertEquals("Quantidade de registros dps do insert: ", count + 8,
				restricoesEstabelecimento.count());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarPermissoesPraUmMCCDeQualquerAdquirenteEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesEstabelecimento
				.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EAllowMerchant.FarmaciasComMcc5122
								.getCodigoGrupoRestricao(),
						EAllowMerchant.FarmaciasComMcc5122.getMcc(), null, null);

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
	public void deveBuscarEmUmaUnicaQueryENaoEncontrarPermissoesPraUmMCCDeQualquerAdquirenteNaoCadastrado()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesEstabelecimento
				.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EAllowMerchant.FarmaciasComMcc5122
								.getCodigoGrupoRestricao(), "9999", // mcc
																	// qualquer
																	// nao
																	// cadastrado
						null, null);

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
	public void deveBuscarPermissoesPraUmMCCDeUmAdquirenteEspecificoEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesEstabelecimento
				.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EAllowMerchant.ValeCulturaComMcc5733EPOSRede
								.getCodigoGrupoRestricao(),
						EAllowMerchant.ValeCulturaComMcc5733EPOSRede.getMcc(),
						EAllowMerchant.ValeCulturaComMcc5733EPOSRede
								.getCodigoAdquirente(), null);

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
	public void deveBuscarEmUmaUnicaQueryENaoEncontrarPermissoesPraUmMCCDeUmAdquirenteEspecificoNaoCadastrado()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesEstabelecimento
				.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EAllowMerchant.ValeCulturaComMcc5733EPOSRede
								.getCodigoGrupoRestricao(),
						EAllowMerchant.ValeCulturaComMcc5733EPOSRede.getMcc(),
						"9999", // adquirente qualquer nao cadastrado para esse
								// mcc
						null);

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
	public void deveBuscarPermissoesPraUmEstabelecimentoEspecificoEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		List<String> estabelecimentos = new LinkedList<String>();
		estabelecimentos.add(EAllowMerchant.MinhaCasaMelhorComPOSCielo
				.getCodigoEstabelecimento());
		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesEstabelecimento
				.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EAllowMerchant.MinhaCasaMelhorComPOSCielo
								.getCodigoGrupoRestricao(), null,
						EAllowMerchant.MinhaCasaMelhorComPOSCielo
								.getCodigoAdquirente(), estabelecimentos);

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
	public void deveBuscarEmUmaUnicaQueryENaoEncontrarPermissoesPraUmEstabelecimentoEspecificoNaoCadastrado()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		List<String> estabelecimentos = new LinkedList<String>();
		estabelecimentos.add("9999"); //estabelecimento qualquer nao cadastrado
		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesEstabelecimento
				.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EAllowMerchant.MinhaCasaMelhorComPOSCielo
								.getCodigoGrupoRestricao(), null,
						EAllowMerchant.MinhaCasaMelhorComPOSCielo
								.getCodigoAdquirente(), estabelecimentos);

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
		final long count = restricoesEstabelecimento.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
