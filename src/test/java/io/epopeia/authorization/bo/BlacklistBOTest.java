package io.epopeia.authorization.bo;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.epopeia.authorization.BusinessObjectConfig;
import io.epopeia.authorization.bo.BlacklistBO;
import io.epopeia.authorization.builder.BlacklistBuilder;
import io.epopeia.authorization.faker.BlacklistFaker.EBlacklistFaker;
import io.epopeia.authorization.repository.backoffice.BlacklistEstabelecimentoRepository;
import io.epopeia.authorization.spring.CacheConfig;

/**
 * Teste do objeto de negocios BlacklistBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class, CacheConfig.class })
public class BlacklistBOTest {

	@Autowired
	private BlacklistEstabelecimentoRepository blacklistMock;

	@Autowired
	private BlacklistBO blacklistBO;

	private BlacklistBuilder builder;

	public BlacklistBOTest() {
		this.builder = new BlacklistBuilder();
	}

	@Before
	public void setUp() {
		Mockito.reset(blacklistMock);
	}

	@Test
	public void deveBuscarEstabelecimentosEmBlackList() {
		String adquirenteToFind = EBlacklistFaker.EstabelecimentoUmRede.getCodigoAdquirente();
		String estabelecimentoToFind = EBlacklistFaker.EstabelecimentoUmRede.getCodigoEstabelecimento();

		// specify mock behave when method called
		when(blacklistMock.findByCodigoAdquirenteAndCodigoEstabelecimento(adquirenteToFind, estabelecimentoToFind))
			.thenReturn(builder.blacklistEstabelecimento(EBlacklistFaker.EstabelecimentoUmRede).create());

		boolean isInBlacklist = blacklistBO.checkRestriction(adquirenteToFind, estabelecimentoToFind);
		assertTrue(isInBlacklist);
	}

}
