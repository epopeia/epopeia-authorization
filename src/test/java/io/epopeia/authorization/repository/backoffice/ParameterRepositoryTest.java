package io.epopeia.authorization.repository.backoffice;

import static org.junit.Assert.*;

import java.sql.SQLException;
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
import io.epopeia.authorization.api.AuthorizationParameter;
import io.epopeia.authorization.builder.ParameterBuilder;
import io.epopeia.authorization.domain.backoffice.Parameter;
import io.epopeia.authorization.enums.EParams;
import io.epopeia.authorization.faker.AccountFaker.EAccounts;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ParameterFaker.EParamsFaker;
import io.epopeia.authorization.faker.ProductFaker.EProduct;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.AccountParameterRepository;
import io.epopeia.authorization.repository.backoffice.ChannelParameterRepository;
import io.epopeia.authorization.repository.backoffice.ModalityParameterRepository;
import io.epopeia.authorization.repository.backoffice.ParameterRepository;
import io.epopeia.authorization.repository.backoffice.ProductParameterRepository;

/**
 * Teste do repositorio de produtos e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class ParameterRepositoryTest {

	@Autowired
	private ParameterRepository parametros;

	@Autowired
	private ProductParameterRepository parametrosProdutos;

	@Autowired
	private ModalityParameterRepository parametrosModalidades;

	@Autowired
	private ChannelParameterRepository parametrosCanais;

	@Autowired
	private AccountParameterRepository parametrosContas;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private ParameterBuilder builder;

	private static final String TB_PARAMETROS = "PARAMETROS";
	private static final String[] TB_PARAMETROS_COLUMNS = new String[] {
			"codigoparametro", "identificador" };
	private static final String[] TB_PARAMETROS_PKS = new String[] { "codigoparametro" };
	private static final String[] TB_PARAMETROS_FKS = new String[] {};

	private static final String TB_PARAMETROSSISTEMAS = "PARAMETROSSISTEMA";
	private static final String[] TB_PARAMETROSSISTEMAS_COLUMNS = new String[] {
			"codigoparametro", "valor" };
	private static final String[] TB_PARAMETROSSISTEMAS_PKS = new String[] { "codigoparametro" };
	private static final String[] TB_PARAMETROSSISTEMAS_FKS = new String[] { "parametros.codigoparametro" };

	private static final String TB_PRODUTOSPARAMETROS = "PRODUTOSPARAMETROS";
	private static final String[] TB_PRODUTOSPARAMETROS_COLUMNS = new String[] {
			"codigoproduto", "codigoparametro", "valor" };
	private static final String[] TB_PRODUTOSPARAMETROS_PKS = new String[] {
			"codigoproduto", "codigoparametro" };
	private static final String[] TB_PRODUTOSPARAMETROS_FKS = new String[] {
			"parametros.codigoparametro", "produtos.codigoproduto" };

	private static final String TB_MODALIDADESPARAMETROS = "MODALIDADESPARAMETROS";
	private static final String[] TB_MODALIDADESPARAMETROS_COLUMNS = new String[] {
			"codigomodalidade", "codigoparametro", "valor" };
	private static final String[] TB_MODALIDADESPARAMETROS_PKS = new String[] {
			"codigomodalidade", "codigoparametro" };
	private static final String[] TB_MODALIDADESPARAMETROS_FKS = new String[] {
			"parametros.codigoparametro", "modalidades.codigomodalidade" };

	private static final String TB_CONTASTITULARESPARAMETROS = "CONTASTITULARESPARAMETROS";
	private static final String[] TB_CONTASTITULARESPARAMETROS_COLUMNS = new String[] {
			"codigocontatitular", "codigoparametro", "valor", "dataexpiracao" };
	private static final String[] TB_CONTASTITULARESPARAMETROS_PKS = new String[] {
			"codigocontatitular", "codigoparametro" };
	private static final String[] TB_CONTASTITULARESPARAMETROS_FKS = new String[] {
			"parametros.codigoparametro", "contastitulares.codigocontatitular" };

	private static final String TB_CANAISPARAMETROS = "CANAISPARAMETROS";
	private static final String[] TB_CANAISPARAMETROS_COLUMNS = new String[] {
			"codigocanal", "codigoparametro", "valor", "dataexpiracao" };
	private static final String[] TB_CANAISPARAMETROS_PKS = new String[] {
			"codigocanal", "codigoparametro" };
	private static final String[] TB_CANAISPARAMETROS_FKS = new String[] {
			"parametros.codigoparametro" };

	public ParameterRepositoryTest() {
		this.builder = new ParameterBuilder();
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_PARAMETROS, TB_PARAMETROS_COLUMNS,
				TB_PARAMETROS_PKS, TB_PARAMETROS_FKS);
		ddlValidator.validateAll(TB_PARAMETROSSISTEMAS,
				TB_PARAMETROSSISTEMAS_COLUMNS, TB_PARAMETROSSISTEMAS_PKS,
				TB_PARAMETROSSISTEMAS_FKS);
		ddlValidator.validateAll(TB_PRODUTOSPARAMETROS,
				TB_PRODUTOSPARAMETROS_COLUMNS, TB_PRODUTOSPARAMETROS_PKS,
				TB_PRODUTOSPARAMETROS_FKS);
		ddlValidator.validateAll(TB_MODALIDADESPARAMETROS,
				TB_MODALIDADESPARAMETROS_COLUMNS, TB_MODALIDADESPARAMETROS_PKS,
				TB_MODALIDADESPARAMETROS_FKS);
		ddlValidator.validateAll(TB_CONTASTITULARESPARAMETROS,
				TB_CONTASTITULARESPARAMETROS_COLUMNS,
				TB_CONTASTITULARESPARAMETROS_PKS,
				TB_CONTASTITULARESPARAMETROS_FKS);
		ddlValidator.validateAll(TB_CANAISPARAMETROS,
				TB_CANAISPARAMETROS_COLUMNS, TB_CANAISPARAMETROS_PKS,
				TB_CANAISPARAMETROS_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = parametros.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// create a parameter with a system parameter
		Parameter p = builder.parameter(EParamsFaker.VALIDAR_CHIP)
				.withSystemValue("valor sistema")
				.withChannelValue(EChannels.CanalUm, "valor canal")
				.withAccountValue(EAccounts.ContaUm, "valor conta")
				.withModalityValue(EModality.ModalidadeUm, "valor modalidade")
				.withProductValue(EProduct.BinMastercard, "valor produto")
				.create();

		// Manual flush is required to avoid false positive in test
		parametros.saveAndFlush(p);

		/*
		 * Para analisar pelo Web server tool do H2
		 * TestTransaction.flagForCommit(); TestTransaction.end();
		 * TestTransaction.start();
		 */

		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				parametros.count());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarParametrosEParametrosSistemaEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Parameter p = parametros.findByIdentificador(EParams.VALIDAR_CHIP
				.name());

		assertNotNull("Parametro nao encontrado", p);
		assertEquals("ParametroSistema nao foi carregado", "valor sistema", p
				.getParametroSistema().getValor());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarParametrosEParametrosProdutoEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		List<AuthorizationParameter> lpp = parametrosProdutos
				.findByCodigoProduto(EProduct.BinMastercard.getCodigoProduto());
		AuthorizationParameter kp = lpp.get(0);

		assertNotNull("Parametro nao encontrado", lpp);
		assertEquals("Esperava 1 parametro carregado", 1L, lpp.size());
		assertEquals("ParametroProduto nao foi carregado", "valor produto",
				kp.getValor());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarParametrosEParametrosModalidadeEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		List<AuthorizationParameter> lpp = parametrosModalidades
				.findByCodigoModalidade(EModality.ModalidadeUm.getCodigoModalidade());
		AuthorizationParameter kp = lpp.get(0);

		assertNotNull("Parametro nao encontrado", lpp);
		assertEquals("Esperava 1 parametro carregado", 1L, lpp.size());
		assertEquals("ParametroModalide nao foi carregado", "valor modalidade",
				kp.getValor());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarParametrosEParametrosCanaisEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		List<AuthorizationParameter> lpp = parametrosCanais
				.findByCodigoCanal(EChannels.CanalUm.getCodigoCanal());
		AuthorizationParameter kp = lpp.get(0);

		assertNotNull("Parametro nao encontrado", lpp);
		assertEquals("Esperava 1 parametro carregado", 1L, lpp.size());
		assertEquals("ParametroCanal nao foi carregado", "valor canal",
				kp.getValor());
		assertEquals(
				"Mais de uma query foi chamada: " + dmlValidator.getLastQuery(),
				stmCounter + 1, dmlValidator.getStatementCounter());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarParametrosEParametrosContasEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		List<AuthorizationParameter> lpp = parametrosContas
				.findByCodigoContaTitular(EAccounts.ContaUm.getCodigoContaTitular());
		AuthorizationParameter kp = lpp.get(0);

		assertNotNull("Parametro nao encontrado", lpp);
		assertEquals("Esperava 1 parametro carregado", 1L, lpp.size());
		assertEquals("ParametroConta nao foi carregado", "valor conta",
				kp.getValor());
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
		final long count = parametros.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
