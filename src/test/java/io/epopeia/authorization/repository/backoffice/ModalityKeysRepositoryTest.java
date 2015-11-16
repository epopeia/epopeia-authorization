package io.epopeia.authorization.repository.backoffice;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Set;

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
import io.epopeia.authorization.builder.ModalityKeysBuilder;
import io.epopeia.authorization.domain.backoffice.ModalityKey;
import io.epopeia.authorization.faker.ModalityFaker;
import io.epopeia.authorization.faker.KeysFaker.EKeys;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.helper.DDLValidator;
import io.epopeia.authorization.helper.DMLValidator;
import io.epopeia.authorization.repository.backoffice.ModalityKeysRepository;

/**
 * Teste do repositorio de modalidadeschaves e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BackofficeH2Config.class })
@TransactionConfiguration(transactionManager = "txMgrBackoffice", defaultRollback = true)
@Transactional(value = "txMgrBackoffice", readOnly = false)
public class ModalityKeysRepositoryTest {

	@Autowired
	private ModalityKeysRepository modalidadeChaves;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DDLValidator ddlValidator;

	@Autowired
	private DMLValidator dmlValidator;

	private ModalityKeysBuilder builder;

	private static final String TB_MODALIDADESCHAVES = "MODALIDADESCHAVES";
	private static final String[] TB_MODALIDADESCHAVES_COLUMNS = new String[] { "codigomodalidade", "codigochave" };
	private static final String[] TB_MODALIDADESCHAVES_PKS = new String[] { "codigomodalidade", "codigochave" };
	private static final String[] TB_MODALIDADESCHAVES_FKS = new String[] { "modalidades.codigomodalidade", "chaves.codigochave" };

	private static final String TB_CHAVES = "CHAVES";
	private static final String[] TB_CHAVES_COLUMNS = new String[] { "codigochave", "codigotipochave", "chave", "ativa" };
	private static final String[] TB_CHAVES_PKS = new String[] { "codigochave" };
	private static final String[] TB_CHAVES_FKS = new String[] {};

	public ModalityKeysRepositoryTest() {
		this.builder = new ModalityKeysBuilder();
	}

	/**
	 * logic to verify the initial state before a transaction is started
	 */
	@BeforeTransaction
	public void asEntidadesDevemGerarTabelasIgualProducao() throws SQLException {
		ddlValidator.validateAll(TB_CHAVES, TB_CHAVES_COLUMNS, TB_CHAVES_PKS, TB_CHAVES_FKS);
		ddlValidator.validateAll(TB_MODALIDADESCHAVES, TB_MODALIDADESCHAVES_COLUMNS,
								TB_MODALIDADESCHAVES_PKS, TB_MODALIDADESCHAVES_FKS);
	}

	/**
	 * set up test data within the transaction
	 */
	@Before
	public void insereMassaDeDadosParaOsTestes() {
		// table should be empty at this moment
		final long count = modalidadeChaves.count();
		assertEquals("Base de dados nao esta vazia", 0, count);

		// create a modality with two keys
		Set<ModalityKey> mks = builder
				.modalities(EModality.ModalidadeUm, EModality.ModalidadeDois)
				.withKeys(EKeys.ChaveUm, EKeys.ChaveDois).create();

		// Manual flush is required to avoid false positive in test
		modalidadeChaves.save(mks);
		modalidadeChaves.flush();
		assertEquals("Quantidade de registros dps do insert: ", count + 4, modalidadeChaves.count());
	}

	/**
	 * logic which uses the test data and modifies database state within transaction
	 */
	@Test
	public void deveBuscarModalidadesEChavesEmUmaUnicaQuery() throws SQLException {
		// get current sql counter and clear history of querys
		long stmCounter = dmlValidator.getStatementCounter();
		dmlValidator.clearQueryHistory();

		// also clear any object references cached by insert in persistence context
		entityManager.clear();

		// This query must be executed in just one statement with joins between entities
		Set<ModalityKey> mks = modalidadeChaves.findByModalidade(ModalityFaker.getModality(EModality.ModalidadeUm));

		assertNotNull("ModalidadeChaves nao encontrado", mks);
		assertEquals("ModalidadesChaves nao foram carregadas", 2, mks.size());
		for(ModalityKey mk : mks) {
			assertNotNull("Modalidade nao foi carregada", mk.getModalidade().getCodigoModalidade());
			assertNotNull("Chave nao foi carregada", mk.getChave().getCodigoChave());
		}
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
		final long count = modalidadeChaves.count();
		assertEquals("Base de dados nao esta vazia depois do rollback", 0, count);
	}
}
