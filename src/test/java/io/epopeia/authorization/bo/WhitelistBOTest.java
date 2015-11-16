package io.epopeia.authorization.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Map;

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
import io.epopeia.authorization.bo.WhitelistBO;
import io.epopeia.authorization.builder.WhitelistBuilder;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.faker.WhitelistFaker.EWhitelistFaker;
import io.epopeia.authorization.repository.backoffice.WhitelistEstabelecimentosRepository;
import io.epopeia.authorization.spring.CacheConfig;

/**
 * Teste do objeto de negocios WhitelistBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class, CacheConfig.class })
public class WhitelistBOTest {

	@Autowired
	private WhitelistEstabelecimentosRepository whitelistMock;

	@Autowired
	private WhitelistBO whitelistBO;

	@Autowired
	private CacheManager ehCacheMgr;

	private WhitelistBuilder builder;

	public WhitelistBOTest() {
		this.builder = new WhitelistBuilder();
	}

	@Before
	public void setUp() {
		Mockito.reset(whitelistMock);
		for (String cacheName : ehCacheMgr.getCacheNames()) {
			ehCacheMgr.getCache(cacheName).clear();
		}
	}

	@Test
	public void deveBuscarECachearMapaDePermissoesDaWhiteListDeEstabelecimentos() {
		String adquirenteToFind = EWhitelistFaker.EstabelecimentoUmRede.getCodigoAdquirente();
		String estabelecimentoToFind = EWhitelistFaker.EstabelecimentoUmRede.getCodigoEstabelecimento();

		// specify mock behave when method called
		when(whitelistMock.findByCodigoAdquirenteAndCodigoEstabelecimento(adquirenteToFind, estabelecimentoToFind))
			.thenReturn(builder.whitelistEstabelecimento(EWhitelistFaker.EstabelecimentoUmRede)
				.withCvc2EmptyPermission().create());

		Map<String, String> estabPermissions = null;
		estabPermissions = whitelistBO.getPermissions(adquirenteToFind, estabelecimentoToFind);
		assertNotNull(estabPermissions);
		assertEquals("Quantidade de permissoes nao confere", estabPermissions.size(), 1);
		assertEquals("Permissao para ausencia de cvc2 nao carregada", "S",
				estabPermissions.get(ELabels.WHITELIST_ALLOW_EMPTY_CVC2.name()));

		Cache cache = ehCacheMgr.getCache("whitelist");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(estabPermissions, value);
		}
	}

}
