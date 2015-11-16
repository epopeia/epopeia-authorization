package io.epopeia.authorization.bo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Set;

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
import io.epopeia.authorization.bo.HsmBO;
import io.epopeia.authorization.builder.ModalityKeysBuilder;
import io.epopeia.authorization.builder.ProductBuilder;
import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.ModalityKey;
import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.faker.KeysFaker.EKeys;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ProductFaker.EProduct;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.ModalityKeysRepository;
import io.epopeia.authorization.repository.backoffice.ProceduresRepository;
import io.epopeia.authorization.repository.backoffice.ProductRepository;
import io.epopeia.authorization.spring.CacheConfig;

/**
 * Teste do objeto de negocios ProductBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class, CacheConfig.class })
public class HsmBOTest {

	@Autowired
	private ProductRepository produtosMock;

	@Autowired
	private ProceduresRepository proceduresMock;

	@Autowired
	private ModalityKeysRepository modalidadeChavesMock;

	@Autowired
	private HsmBO hsmBO;

	@Autowired
	private CacheManager ehCacheMgr;

	private ProductBuilder builderProduct;
	private ModalityKeysBuilder builderModalityKey;

	public HsmBOTest() {
		this.builderProduct = new ProductBuilder();
		this.builderModalityKey = new ModalityKeysBuilder();
	}

	@Before
	public void setUp() {
		Mockito.reset(produtosMock, modalidadeChavesMock, proceduresMock);
		for (String cacheName : ehCacheMgr.getCacheNames()) {
			ehCacheMgr.getCache(cacheName).clear();
		}
	}

	@Test
	public void devePreencherFieldSetComResultadoDeValidacaoDoCVC2OK() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		when(proceduresMock.cvv2(imf)).thenReturn("000");
		hsmBO.validateSecurityCodes(imf);
		assertEquals(HsmBO.SECURITY_CODE_OK, imf.getValue(EFields.RESP_CVC));
		assertEquals(HsmBO.SECURITY_CODE_OK, imf.getValue(EFields.RESP_CVC2));
		assertEquals(HsmBO.SECURITY_CODE_OK, imf.getValue(EFields.RESP_ICVV));
	}

	@Test
	public void devePreencherFieldSetComResultadoDeValidacaoDoCVC2NOK() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		when(proceduresMock.cvv2(imf)).thenReturn("111");
		hsmBO.validateSecurityCodes(imf);
		assertEquals(HsmBO.SECURITY_CODE_NOK, imf.getValue(EFields.RESP_CVC));
		assertEquals(HsmBO.SECURITY_CODE_NOK, imf.getValue(EFields.RESP_CVC2));
		assertEquals(HsmBO.SECURITY_CODE_NOK, imf.getValue(EFields.RESP_ICVV));
	}

	@Test
	public void devePreencherFieldSetComResultadoDeValidacaoDoCVC2FAIL() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		when(proceduresMock.cvv2(imf)).thenReturn("222");
		hsmBO.validateSecurityCodes(imf);
		assertEquals(HsmBO.SECURITY_CODE_FAIL, imf.getValue(EFields.RESP_CVC));
		assertEquals(HsmBO.SECURITY_CODE_FAIL, imf.getValue(EFields.RESP_CVC2));
		assertEquals(HsmBO.SECURITY_CODE_FAIL, imf.getValue(EFields.RESP_ICVV));
	}

	@Test(expected = Exception.class)
	public void devePreencherFieldSetComResultadoDeValidacaoDoCVC2NORETURN() throws Exception {
		FieldSet imf = new FieldSet(EFields.IMF);
		when(proceduresMock.cvv2(imf)).thenReturn(null);
		hsmBO.validateSecurityCodes(imf);
	}

	@Test
	public void deveBuscarECachearChaveDeEncriptacao() {
		String binToFind = EProduct.BinMastercard.getBin();
		String expectedKey = EKeys.Chave00B.getChave();

		// create a product with two modalities
		Product p = builderProduct
			.product(EProduct.BinMastercard)
			.withModalities(EModality.ModalidadeUm, EModality.ModalidadeDois).create();
		Modality modalityToFind = p.getModalidades().iterator().next();

		// create the same modalities with two keys
		Set<ModalityKey> mks = builderModalityKey
			.modalities(EModality.ModalidadeUm, EModality.ModalidadeDois)
			.withKeys(EKeys.ChaveUm, EKeys.Chave00B).create();

		// specify mock behave when method called
		when(produtosMock.findByBin(binToFind)).thenReturn(p);
		when(modalidadeChavesMock.findByModalidade(modalityToFind)).thenReturn(
				mks);

		String key = null;
		key = hsmBO.getKey(binToFind, "00B");
		assertEquals(expectedKey, key);

		Cache cache = ehCacheMgr.getCache("chaves");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object cachedKey : ehcache.getKeys()) {
			Element element = ehcache.get(cachedKey);
			Object value = element.getObjectValue();
			assertEquals(expectedKey, value);
		}
	}
}
