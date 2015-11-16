package io.epopeia.authorization.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.epopeia.authorization.BusinessObjectConfig;
import io.epopeia.authorization.bo.TransactionTypeBO;
import io.epopeia.authorization.builder.TransactionTypeBuilder;
import io.epopeia.authorization.domain.backoffice.TransactionType;
import io.epopeia.authorization.enums.ETxnAttributes;
import io.epopeia.authorization.faker.TransactionTypeFaker.ETransactionType;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.TransactionTypeRepository;
import io.epopeia.authorization.spring.CacheConfig;

/**
 * Teste do objeto de negocios TransactionTypeBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class, CacheConfig.class })
public class TransactionTypeBOTest {

	@Autowired
	private TransactionTypeRepository tiposTransacoesMock;

	@Autowired
	private TransactionTypeBO transactionTypeBO;

	@Autowired
	private CacheManager ehCacheMgr;

	private TransactionTypeBuilder builder;

	public TransactionTypeBOTest() {
		this.builder = new TransactionTypeBuilder();
	}

	@Before
	public void setUp() {
		Mockito.reset(tiposTransacoesMock);
		for (String cacheName : ehCacheMgr.getCacheNames()) {
			ehCacheMgr.getCache(cacheName).clear();
		}
	}

	@Test
	public void deveBuscarECachearAtributosDosTiposDeTransacao() {
		String transactionTypeToFind = ETransactionType.COMPRA_CREDITO_A_VISTA.getIdentificador().name();

		// create a transaction type
		TransactionType tt = builder.transactionType(
				ETransactionType.COMPRA_CREDITO_A_VISTA).create();

		// specify mock behave when method called
		when(tiposTransacoesMock.findByIdentificador(transactionTypeToFind)).thenReturn(tt);

		FieldSet attributes = null;
		attributes = transactionTypeBO.getTransactionAtributes(transactionTypeToFind);
		assertNotNull(attributes);
		assertEquals(attributes.getValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO), "S");
		assertEquals(attributes.getValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA), "S");
		assertEquals(attributes.getValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO), "S");

		Cache cache = ehCacheMgr.getCache("tiposTransacoes");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object cachedKey : ehcache.getKeys()) {
			Element element = ehcache.get(cachedKey);
			Object value = element.getObjectValue();
			assertEquals(attributes, value);
		}
	}
}
