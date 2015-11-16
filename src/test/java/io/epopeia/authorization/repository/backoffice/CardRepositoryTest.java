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
import io.epopeia.authorization.builder.CardBuilder;
import io.epopeia.authorization.domain.backoffice.Card;
import io.epopeia.authorization.faker.AccountFaker.EAccounts;
import io.epopeia.authorization.faker.AccountSituationFaker.EAccountSituations;
import io.epopeia.authorization.faker.CardFaker.ECards;
import io.epopeia.authorization.faker.CardSituationFaker.ECardSituations;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ProductFaker.EProduct;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.CardRepository;

/**
 * Teste do repositorio de cartoes e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class CardRepositoryTest {

	@Autowired
	private CardRepository cartoes;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private CardBuilder builder;

	private static final String TB_CARD = "CARTOES";
	private static final String[] TB_CARD_COLUMNS = new String[] {
			"codigocartao", "numero", "codigocontatitular", "datavalidade",
			"codigogruporestricao", "codigosituacao", "situacao" };
	private static final String[] TB_CARD_PKS = new String[] { "codigocartao" };
	private static final String[] TB_CARD_FKS = new String[] {
			"contastitulares.codigocontatitular",
			"situacoescartoes.codigosituacao" };

	private static final String TB_CARDSITUATION = "SITUACOESCARTOES";
	private static final String[] TB_CARDSITUATION_COLUMNS = new String[] {
			"codigosituacao", "identificador" };
	private static final String[] TB_CARDSITUATION_PKS = new String[] { "codigosituacao" };
	private static final String[] TB_CARDSITUATION_FKS = new String[] {};

	private static final String TB_ACCOUNT = "CONTASTITULARES";
	private static final String[] TB_ACCOUNT_COLUMNS = new String[] {
			"codigocontatitular", "codigomodalidade", "codigosituacao",
			"situacao" };
	private static final String[] TB_ACCOUNT_PKS = new String[] { "codigocontatitular" };
	private static final String[] TB_ACCOUNT_FKS = new String[] {
			"modalidades.codigomodalidade",
			"situacoescontastitulares.codigosituacao" };

	private static final String TB_ACCOUNTSITUATION = "SITUACOESCONTASTITULARES";
	private static final String[] TB_ACCOUNTSITUATION_COLUMNS = new String[] {
			"codigosituacao", "identificador" };
	private static final String[] TB_ACCOUNTSITUATION_PKS = new String[] { "codigosituacao" };
	private static final String[] TB_ACCOUNTSITUATION_FKS = new String[] {};

	private static final String TB_CARDCHANNELS = "CANAIS";
	private static final String[] TB_CARDCHANNELS_COLUMNS = new String[] {
			"codigocartao", "codigocanal" };
	private static final String[] TB_CARDCHANNELS_PKS = new String[] { "codigocartao" };
	private static final String[] TB_CARDCHANNELS_FKS = new String[] { "cartoes.codigocartao" };

	public CardRepositoryTest() {
		this.builder = new CardBuilder();
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_CARD, TB_CARD_COLUMNS, TB_CARD_PKS,
				TB_CARD_FKS);
		ddlValidator.validateAll(TB_CARDSITUATION, TB_CARDSITUATION_COLUMNS,
				TB_CARDSITUATION_PKS, TB_CARDSITUATION_FKS);
		ddlValidator.validateAll(TB_ACCOUNT, TB_ACCOUNT_COLUMNS,
				TB_ACCOUNT_PKS, TB_ACCOUNT_FKS);
		ddlValidator.validateAll(TB_ACCOUNTSITUATION,
				TB_ACCOUNTSITUATION_COLUMNS, TB_ACCOUNTSITUATION_PKS,
				TB_ACCOUNTSITUATION_FKS);
		ddlValidator.validateAll(TB_CARDCHANNELS, TB_CARDCHANNELS_COLUMNS,
				TB_CARDCHANNELS_PKS, TB_CARDCHANNELS_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = cartoes.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// create a card
		Card c = builder
				.cartao(ECards.CartaoUm)
				.withCardSituation(ECardSituations.ATIVO_SITUACAO_INICIAL)
				.withAccount(EAccounts.ContaUm)
				.withAccountSituation(EAccountSituations.ATIVA_SITUACAO_INICIAL)
				.withModality(EModality.ModalidadeUm)
				.withProduct(EProduct.BinMastercard)
				.withChannel(EChannels.CanalUm).create();

		// Manual flush is required to avoid false positive in test
		cartoes.saveAndFlush(c);
		assertEquals("Quantidade de registros dps do insert: ", count + 1,
				cartoes.count());
	}

	/**
	 * logic which uses the test data and modifies database state within
	 * transaction
	 */
	@Test
	public void deveBuscarModalidadesEChavesEmUmaUnicaQuery()
			throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence
		// context
		entityManager.clear();

		// This query must be executed in just one statement with joins between
		// entities
		Card c = cartoes.findByNumero(ECards.CartaoUm.getNumero());

		assertNotNull("Cartao nao encontrado", c);
		assertNotNull("Situacao do cartao nao carregado", c.getSituacao()
				.getIdentificador());
		assertNotNull("Canal nao carregado", c.getCanal().getCodigoCanal());
		assertNotNull("Conta nao carregada", c.getContaTitular()
				.getCodigoContaTitular());
		assertNotNull("Situacao da conta nao carregado", c.getContaTitular()
				.getSituacao().getIdentificador());
		assertNotNull("Modalidade nao carregada", c.getContaTitular()
				.getModalidade().getCodigoModalidade());
		assertNotNull("Produto nao carregado", c.getContaTitular()
				.getModalidade().getProduto().getCodigoProduto());
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
		final long count = cartoes.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0,
				count);
	}
}
