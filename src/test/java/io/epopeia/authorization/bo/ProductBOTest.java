package io.epopeia.authorization.bo;

import static org.junit.Assert.assertEquals;
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
import io.epopeia.authorization.bo.ProductBO;
import io.epopeia.authorization.builder.ProductBuilder;
import io.epopeia.authorization.faker.ProductFaker.EProduct;
import io.epopeia.authorization.repository.backoffice.ProductRepository;
import io.epopeia.authorization.spring.CacheConfig;

/**
 * Teste do objeto de negocios ProductBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class, CacheConfig.class })
public class ProductBOTest {

	@Autowired
	private ProductRepository produtosMock;

	@Autowired
	private ProductBO productBO;

	@Autowired
	private CacheManager ehCacheMgr;

	private ProductBuilder builder;

	public ProductBOTest() {
		this.builder = new ProductBuilder();
	}

	@Before
	public void setUp() {
		Mockito.reset(produtosMock);
		for (String cacheName : ehCacheMgr.getCacheNames()) {
			ehCacheMgr.getCache(cacheName).clear();
		}
	}

	@Test
	public void deveBuscarECachearIdDoProduto() {
		String binToFind = EProduct.BinMastercard.getBin();
		Long expectedProductId = EProduct.BinMastercard.getCodigoProduto();

		// specify mock behave when method called
		when(produtosMock.findByBin(binToFind)).thenReturn(
				builder.product(EProduct.BinMastercard).create());

		Long id = null;
		id = productBO.getProductId(binToFind);
		assertEquals(expectedProductId, id);

		Cache cache = ehCacheMgr.getCache("bins");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(expectedProductId, value);
		}
	}

	@Test
	public void deveBuscarECachearIdDaBandeira() {
		String binToFind = EProduct.BinElo.getBin();
		Long expectedBrandId = new Long(EProduct.BinElo.getBandeira()
				.getCodigoBandeira());

		// specify mock behave when method called
		when(produtosMock.findByBin(binToFind)).thenReturn(
				builder.product(EProduct.BinElo).create());

		Long id = null;
		id = productBO.getBrandId(binToFind);
		assertEquals(expectedBrandId, id);

		Cache cache = ehCacheMgr.getCache("bandeiras");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(expectedBrandId, value);
		}
	}
}
