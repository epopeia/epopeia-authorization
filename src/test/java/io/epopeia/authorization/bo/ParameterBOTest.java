package io.epopeia.authorization.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
import io.epopeia.authorization.api.AuthorizationParameter;
import io.epopeia.authorization.beans.ParameterSearch;
import io.epopeia.authorization.bo.ParameterBO;
import io.epopeia.authorization.builder.ParameterBuilder;
import io.epopeia.authorization.domain.backoffice.Parameter;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.faker.AccountFaker.EAccounts;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ParameterFaker.EParamsFaker;
import io.epopeia.authorization.faker.ProductFaker.EProduct;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.AccountParameterRepository;
import io.epopeia.authorization.repository.backoffice.ChannelParameterRepository;
import io.epopeia.authorization.repository.backoffice.ModalityParameterRepository;
import io.epopeia.authorization.repository.backoffice.ParameterRepository;
import io.epopeia.authorization.repository.backoffice.ProductParameterRepository;
import io.epopeia.authorization.spring.CacheConfig;

/**
 * Teste do objeto de negocios ParameterBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class, CacheConfig.class })
public class ParameterBOTest {

	@Autowired
	private ParameterRepository parametrosMock;

	@Autowired
	private ProductParameterRepository parametrosProdutosMock;

	@Autowired
	private ModalityParameterRepository parametrosModalidadesMock;

	@Autowired
	private ChannelParameterRepository parametrosCanaisMock;

	@Autowired
	private AccountParameterRepository parametrosContasMock;

	@Autowired
	private ParameterBO parameterBO;

	@Autowired
	private ParameterSearch parameterSearch;

	@Autowired
	private CacheManager ehCacheMgr;

	private ParameterBuilder builder;

	public ParameterBOTest() {
		this.builder = new ParameterBuilder();
	}

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		Mockito.reset(parametrosMock, parametrosProdutosMock,
				parametrosModalidadesMock, parametrosCanaisMock,
				parametrosContasMock);
		for (String cacheName : ehCacheMgr.getCacheNames()) {
			ehCacheMgr.getCache(cacheName).clear();
		}
	}

	@Test
	public void deveBuscarECachearValoresDeParametrosDeSistema() {
		String paramNameToFind = EParamsFaker.VALIDAR_CHIP.getIdentificador()
				.name();
		String expectedValue = "S";

		// specify mock behave when method called
		when(parametrosMock.findByIdentificador(paramNameToFind)).thenReturn(
				builder.parameter(EParamsFaker.VALIDAR_CHIP)
						.withSystemValue("S").create());

		String valorObtido = null;
		valorObtido = parameterBO.getSystemParameterValue(paramNameToFind);
		assertEquals(expectedValue, valorObtido);

		Cache cache = ehCacheMgr.getCache("parametrosSistemas");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(expectedValue, value);
		}
	}

	@Test
	public void deveBuscarECachearValoresDeParametrosDeProduto() {
		// create a list with 2 parameters for product 1
		Parameter pp1 = builder.parameter(EParamsFaker.PERMITE_DEBITO)
			.withProductValue(EProduct.BinMastercard, "N").create();
		Parameter pp2 = builder.parameter(EParamsFaker.VALIDAR_CHIP)
			.withProductValue(EProduct.BinMastercard, "S").create();
		List<AuthorizationParameter> lpp = new LinkedList<AuthorizationParameter>();
		lpp.addAll(pp1.getParametrosProduto());
		lpp.addAll(pp2.getParametrosProduto());

		// define all identifiers for product 1 as the expected result map
		Map<String, String> expectedMap = new LinkedHashMap<String, String>();
		expectedMap.put(EParamsFaker.PERMITE_DEBITO.getIdentificador().name(), "N");
		expectedMap.put(EParamsFaker.VALIDAR_CHIP.getIdentificador().name(), "S");

		// specify mock behave when method called
		when(parametrosProdutosMock.findByCodigoProduto(EProduct.BinMastercard
					.getCodigoProduto())).thenReturn(lpp);

		// execute the test
		Map<String, String> valoresObtidos = null;
		valoresObtidos = parameterBO.getAllProductParameters(
				EProduct.BinMastercard.getCodigoProduto());
		assertEquals(expectedMap, valoresObtidos);

		// assert that map was cached
		Cache cache = ehCacheMgr.getCache("parametrosProdutos");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(expectedMap, value);
		}
	}

	@Test
	public void deveBuscarECachearValoresDeParametrosDeModalidade() {
		// create a list with 2 parameters for modality 1
		Parameter pp1 = builder.parameter(EParamsFaker.PERMITE_DEBITO)
			.withModalityValue(EModality.ModalidadeUm, "N").create();
		Parameter pp2 = builder.parameter(EParamsFaker.VALIDAR_CHIP)
			.withModalityValue(EModality.ModalidadeUm, "S").create();
		List<AuthorizationParameter> lpp = new LinkedList<AuthorizationParameter>();
		lpp.addAll(pp1.getParametrosModalidade());
		lpp.addAll(pp2.getParametrosModalidade());

		// define all identifiers for modality 1 as the expected result map
		Map<String, String> expectedMap = new LinkedHashMap<String, String>();
		expectedMap.put(EParamsFaker.PERMITE_DEBITO.getIdentificador().name(), "N");
		expectedMap.put(EParamsFaker.VALIDAR_CHIP.getIdentificador().name(), "S");

		// specify mock behave when method called
		when(parametrosModalidadesMock.findByCodigoModalidade(
				EModality.ModalidadeUm.getCodigoModalidade())).thenReturn(lpp);

		// execute the test
		Map<String, String> valoresObtidos = null;
		valoresObtidos = parameterBO.getAllModalityParameters(
				EModality.ModalidadeUm.getCodigoModalidade());
		assertEquals(expectedMap, valoresObtidos);

		// assert that map was cached
		Cache cache = ehCacheMgr.getCache("parametrosModalidades");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(expectedMap, value);
		}
	}

	@Test
	public void deveBuscarECachearValoresDeParametrosDeCanais() {
		// create a list with 2 parameters for channel 1
		Parameter pp1 = builder.parameter(EParamsFaker.PERMITE_DEBITO)
			.withChannelValue(EChannels.CanalUm, "N").create();
		Parameter pp2 = builder.parameter(EParamsFaker.VALIDAR_CHIP)
			.withChannelValue(EChannels.CanalUm, "S").create();
		List<AuthorizationParameter> lpp = new LinkedList<AuthorizationParameter>();
		lpp.addAll(pp1.getParametrosCanais());
		lpp.addAll(pp2.getParametrosCanais());

		// add more one parameter with one month expired date thats should not influence
		Calendar exp = Calendar.getInstance();
		exp.add(Calendar.MONTH, -1);
		Parameter pp3 = builder.parameter(EParamsFaker.VALOR_MAXIMO_TRANSACAO)
			.withChannelValue(EChannels.CanalUm, "N", exp).create();
		lpp.addAll(pp3.getParametrosCanais());

		// define all identifiers for channel 1 as the expected result map
		Map<String, String> expectedMap = new LinkedHashMap<String, String>();
		expectedMap.put(EParamsFaker.PERMITE_DEBITO.getIdentificador().name(), "N");
		expectedMap.put(EParamsFaker.VALIDAR_CHIP.getIdentificador().name(), "S");

		// specify mock behave when method called
		when(parametrosCanaisMock.findByCodigoCanal(
				EChannels.CanalUm.getCodigoCanal())).thenReturn(lpp);

		// execute the test
		Map<String, String> valoresObtidos = null;
		valoresObtidos = parameterBO.getAllChannelParameters(
				EChannels.CanalUm.getCodigoCanal());
		assertEquals(expectedMap, valoresObtidos);

		// assert that map was cached
		Cache cache = ehCacheMgr.getCache("parametrosCanais");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(expectedMap, value);
		}
	}

	@Test
	public void deveBuscarECachearValoresDeParametrosDeContas() {
		// create a list with 2 parameters for channel 1
		Parameter pp1 = builder.parameter(EParamsFaker.PERMITE_DEBITO)
			.withAccountValue(EAccounts.ContaUm, "N").create();
		Parameter pp2 = builder.parameter(EParamsFaker.VALIDAR_CHIP)
			.withAccountValue(EAccounts.ContaUm, "S").create();
		List<AuthorizationParameter> lpp = new LinkedList<AuthorizationParameter>();
		lpp.addAll(pp1.getParametrosContaTitular());
		lpp.addAll(pp2.getParametrosContaTitular());

		// add more one parameter with one month expired date thats should not influence
		Calendar exp = Calendar.getInstance();
		exp.add(Calendar.MONTH, -1);
		Parameter pp3 = builder.parameter(EParamsFaker.VALOR_MAXIMO_TRANSACAO)
			.withAccountValue(EAccounts.ContaUm, "N", exp).create();
		lpp.addAll(pp3.getParametrosContaTitular());

		// define all identifiers for account 1 as the expected result map
		Map<String, String> expectedMap = new LinkedHashMap<String, String>();
		expectedMap.put(EParamsFaker.PERMITE_DEBITO.getIdentificador().name(), "N");
		expectedMap.put(EParamsFaker.VALIDAR_CHIP.getIdentificador().name(), "S");

		// specify mock behave when method called
		when(parametrosContasMock.findByCodigoContaTitular(
				EAccounts.ContaUm.getCodigoContaTitular())).thenReturn(lpp);

		// execute the test
		Map<String, String> valoresObtidos = null;
		valoresObtidos = parameterBO.getAllAccountParameters(
				EAccounts.ContaUm.getCodigoContaTitular());
		assertEquals(expectedMap, valoresObtidos);

		// assert that map was cached
		Cache cache = ehCacheMgr.getCache("parametrosContas");
		Ehcache ehcache = (Ehcache) cache.getNativeCache();
		assertEquals(1, ehcache.getKeys().size());
		for (Object key : ehcache.getKeys()) {
			Element element = ehcache.get(key);
			Object value = element.getObjectValue();
			assertEquals(expectedMap, value);
		}
	}

	@Test
	public void deveBuscarECacherTodosOsParametrosPorOrdemDePrioridade() {
		// set initial variables
		String pIdentificador = EParamsFaker.PERMITE_DEBITO.getIdentificador().name();
		Long accountId = EAccounts.ContaUm.getCodigoContaTitular();
		Long channelId = EChannels.CanalUm.getCodigoCanal();
		Long modalityId = EModality.ModalidadeUm.getCodigoModalidade();
		Long productId = EProduct.BinMastercard.getCodigoProduto();
		FieldSet imf = new FieldSet(EFields.IMF);
		FieldSet cardInfo = new FieldSet(ECardInfo.CARD_INFO);
		imf.add(cardInfo);
		cardInfo.setValue(ECardInfo.PRODUCT_ID, productId.toString());
		cardInfo.setValue(ECardInfo.MODALITY_ID, modalityId.toString());
		cardInfo.setValue(ECardInfo.CARD_CHANNEL_ID, channelId.toString());
		cardInfo.setValue(ECardInfo.ACCOUNT_ID, accountId.toString());
		String retValue = null;
		Cache cache = null;

		// create a different parameter for each entities
		Parameter pp = builder.parameter(EParamsFaker.PERMITE_DEBITO)
			.withProductValue(EProduct.BinMastercard, "teste fallback produto")
			.withModalityValue(EModality.ModalidadeUm, "teste fallback modalidade")
			.withChannelValue(EChannels.CanalUm, "teste fallback canal")
			.withAccountValue(EAccounts.ContaUm, "teste fallback conta")
			.withSystemValue("teste fallback sistema").create();

		// define return list from repositories
		List<AuthorizationParameter> lct = new LinkedList<AuthorizationParameter>();
		lct.addAll(pp.getParametrosContaTitular());
		List<AuthorizationParameter> lch = new LinkedList<AuthorizationParameter>();
		lch.addAll(pp.getParametrosCanais());
		List<AuthorizationParameter> lm = new LinkedList<AuthorizationParameter>();
		lm.addAll(pp.getParametrosModalidade());
		List<AuthorizationParameter> lp = new LinkedList<AuthorizationParameter>();
		lp.addAll(pp.getParametrosProduto());

		// first we expected nothing
		retValue = parameterSearch.getParameterValue(imf, pIdentificador);
		assertNull(retValue);

		// assert system was get first and cached
		when(parametrosMock.findByIdentificador(pIdentificador)).thenReturn(pp);
		retValue = parameterSearch.getParameterValue(imf, pIdentificador);
		assertEquals("Nao utilizou parametro sistema", "teste fallback sistema", retValue);
		cache = ehCacheMgr.getCache("parametrosSistemas");
		assertEquals(1, ((Ehcache) cache.getNativeCache()).getKeys().size());

		// assert product was get after and cached
		when(parametrosProdutosMock.findByCodigoProduto(productId)).thenReturn(lp);
		retValue = parameterSearch.getParameterValue(imf, pIdentificador);
		assertEquals("Nao utilizou parametro produto", "teste fallback produto", retValue);
		cache = ehCacheMgr.getCache("parametrosProdutos");
		assertEquals(1, ((Ehcache) cache.getNativeCache()).getKeys().size());

		// assert modality was get after and cached
		when(parametrosModalidadesMock.findByCodigoModalidade(modalityId)).thenReturn(lm);
		retValue = parameterSearch.getParameterValue(imf, pIdentificador);
		assertEquals("Nao utilizou parametro modalidade", "teste fallback modalidade", retValue);
		cache = ehCacheMgr.getCache("parametrosModalidades");
		assertEquals(1, ((Ehcache) cache.getNativeCache()).getKeys().size());

		// assert channel was get after and cached
		when(parametrosCanaisMock.findByCodigoCanal(channelId)).thenReturn(lch);
		retValue = parameterSearch.getParameterValue(imf, pIdentificador);
		assertEquals("Nao utilizou parametro canal", "teste fallback canal", retValue);
		cache = ehCacheMgr.getCache("parametrosCanais");
		assertEquals(1, ((Ehcache) cache.getNativeCache()).getKeys().size());

		// assert account was get after and cached
		when(parametrosContasMock.findByCodigoContaTitular(accountId)).thenReturn(lct);
		retValue = parameterSearch.getParameterValue(imf, pIdentificador);
		assertEquals("Nao utilizou parametro conta", "teste fallback conta", retValue);
		cache = ehCacheMgr.getCache("parametrosContas");
		assertEquals(1, ((Ehcache) cache.getNativeCache()).getKeys().size());
	}
}
