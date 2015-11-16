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
import io.epopeia.authorization.faker.AllowChannelFaker;
import io.epopeia.authorization.faker.AllowMerchantCnpjFaker;
import io.epopeia.authorization.faker.AllowMerchantFaker;
import io.epopeia.authorization.faker.AllowChannelFaker.EAllowChannel;
import io.epopeia.authorization.faker.AllowMerchantCnpjFaker.EAllowMerchantCnpj;
import io.epopeia.authorization.faker.AllowMerchantFaker.EAllowMerchant;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.AllowChannelRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantCnpjRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantRepository;

/**
 * Teste do repositorio de restricoes e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class AllowChannelRepositoryTest {

	@Autowired
	private AllowChannelRepository restricoesCanais;

	@Autowired
	private AllowMerchantRepository restricoesEstabelecimento;

	@Autowired
	private AllowMerchantCnpjRepository restricoesCnpj;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private static final String TB_RESTRICOESCANAIS = "RESTRICOESCANAIS";
	private static final String[] TB_RESTRICOESCANAIS_COLUMNS = new String[] {
			"codigorestricaocanal", "codigocanal", "codigogruporestricao",
			"validaautorizacao" };
	private static final String[] TB_RESTRICOESCANAIS_PKS = new String[] {
			"codigorestricaocanal" };
	private static final String[] TB_RESTRICOESCANAIS_FKS = new String[] {};

	public AllowChannelRepositoryTest() {
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_RESTRICOESCANAIS, TB_RESTRICOESCANAIS_COLUMNS,
				TB_RESTRICOESCANAIS_PKS, TB_RESTRICOESCANAIS_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = restricoesCanais.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// Manual flush is required to avoid false positive in test
		for (EAllowChannel am : EAllowChannel.values()) {
			restricoesCanais.saveAndFlush(AllowChannelFaker.getAllowChannel(am));

			EAllowMerchant restricaoEstabelecimento =  am.getEAllowMerchant();
			if (restricaoEstabelecimento != null)
				restricoesEstabelecimento.saveAndFlush(AllowMerchantFaker
					.getAllowMerchant(restricaoEstabelecimento));

			EAllowMerchantCnpj restricaoCnpj = am.getRestricaoCnpj();
			if (restricaoCnpj != null)
				restricoesCnpj.saveAndFlush(AllowMerchantCnpjFaker
					.getAllowMerchantCnpj(restricaoCnpj));
		}
		assertEquals("Quantidade de registros dps do insert: ", count + 3,
				restricoesEstabelecimento.count());
		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				restricoesCnpj.count());
		assertEquals("Quantidade de registros dps do insert: ", count + 3,
				restricoesCanais.count());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarGruposDeRestricoesPorCanal()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesCanais
				.countByCodigoCanal(EChannels.CanalUm.getCodigoCanal());

		assertEquals("Permissoes nao econtradas", 3, countAllows.longValue());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EChannels.CanalUm.getCodigoCanal(),
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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EChannels.CanalUm.getCodigoCanal(),
						"9999", // mcc qualquer nao cadastrado
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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EChannels.CanalUm.getCodigoCanal(),
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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EChannels.CanalUm.getCodigoCanal(),
						EAllowMerchant.ValeCulturaComMcc5733EPOSRede.getMcc(),
						"9999", // adquirente qualquer nao cadastrado para esse mcc
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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EChannels.CanalUm.getCodigoCanal(), null,
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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
						EChannels.CanalUm.getCodigoCanal(), null,
						EAllowMerchant.MinhaCasaMelhorComPOSCielo
								.getCodigoAdquirente(), estabelecimentos);

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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndCnpj(
						EChannels.CanalUm.getCodigoCanal(),
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
	public void naoDeveEncontrarNenhumaRestricaoSeOCanalNaoEstiverCadastrado()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndCnpj(
						0L, null);

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
		Long countAllows = restricoesCanais
				.countByCodigoCanalAndCnpj(
						EChannels.CanalUm.getCodigoCanal(),
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
	}
}
