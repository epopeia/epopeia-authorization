package io.epopeia.authorization.repository.backoffice;

import static org.junit.Assert.*;

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
import io.epopeia.authorization.builder.ProductBuilder;
import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ProductFaker.EProduct;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.ProductRepository;

/**
 * Teste do repositorio de produtos e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository produtos;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private ProductBuilder builder;

	private static final String TB_PRODUTOS = "PRODUTOS";
	private static final String[] TB_PRODUTOS_COLUMNS = new String[] {
			"codigoproduto", "iin", "codigobandeira" };
	private static final String[] TB_PRODUTOS_PKS = new String[] { "codigoproduto" };
	private static final String[] TB_PRODUTOS_FKS = new String[] {};

	private static final String TB_MODALIDADES = "MODALIDADES";
	private static final String[] TB_MODALIDADES_COLUMNS = new String[] {
			"codigomodalidade", "codigoproduto", "codigofuncaocartao" };
	private static final String[] TB_MODALIDADES_PKS = new String[] { "codigomodalidade" };
	private static final String[] TB_MODALIDADES_FKS = new String[] { "produtos.codigoproduto" };

	public ProductRepositoryTest() {
		this.builder = new ProductBuilder();
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_PRODUTOS, TB_PRODUTOS_COLUMNS,
				TB_PRODUTOS_PKS, TB_PRODUTOS_FKS);
		ddlValidator.validateAll(TB_MODALIDADES, TB_MODALIDADES_COLUMNS,
				TB_MODALIDADES_PKS, TB_MODALIDADES_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = produtos.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// create a product with two modalities
		Product p1 = builder
				.product(EProduct.BinMastercard)
				.withModalities(EModality.ModalidadeUm,
						EModality.ModalidadeDois).create();

		// Manual flush is required to avoid false positive in test
		produtos.saveAndFlush(p1);
		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				produtos.count());
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
		Product p = produtos.findByBin(EProduct.BinMastercard.getBin());

		assertNotNull("Produto nao encontrado", p);
		assertEquals("Modalidades nao foram carregadas", 2, p.getModalidades()
				.size());
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
		final long count = produtos.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
