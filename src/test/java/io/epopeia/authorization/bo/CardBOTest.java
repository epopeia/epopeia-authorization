package io.epopeia.authorization.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.epopeia.authorization.BusinessObjectConfig;
import io.epopeia.authorization.bo.CardBO;
import io.epopeia.authorization.builder.CardBuilder;
import io.epopeia.authorization.domain.backoffice.Card;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.faker.AccountFaker.EAccounts;
import io.epopeia.authorization.faker.AccountSituationFaker.EAccountSituations;
import io.epopeia.authorization.faker.CardFaker.ECards;
import io.epopeia.authorization.faker.CardSituationFaker.ECardSituations;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ProductFaker.EProduct;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.CardRepository;
import io.epopeia.authorization.spring.CacheConfig;

/**
 * Teste do objeto de negocios ProductBO
 * 
 * @author Fernando Amaral
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BusinessObjectConfig.class, CacheConfig.class })
public class CardBOTest {

	@Autowired
	private CardRepository cartoesMock;

	@Autowired
	private CardBO cardBO;

	private CardBuilder builder;

	public CardBOTest() {
		this.builder = new CardBuilder();
	}

	@Before
	public void setUp() {
		Mockito.reset(cartoesMock);
	}

	@Test
	public void deveBuscarCartaoERetornarFieldSetPreenchido() {
		String cardToFind = ECards.CartaoUm.getNumero();
		Long expectedCardId = ECards.CartaoUm.getCodigoCartao();

		// create a card
		Card c = builder
				.cartao(ECards.CartaoUm)
				.withCardSituation(ECardSituations.ATIVO_SITUACAO_INICIAL)
				.withAccount(EAccounts.ContaUm)
				.withAccountSituation(EAccountSituations.ATIVA_SITUACAO_INICIAL)
				.withModality(EModality.ModalidadeUm)
				.withProduct(EProduct.BinMastercard)
				.withChannel(EChannels.CanalUm).create();

		// specify mock behave when method called
		when(cartoesMock.findByNumero(cardToFind)).thenReturn(c);

		FieldSet fsCard = null;
		fsCard = cardBO.loadCardInfo(cardToFind);
		assertNotNull(fsCard);
		assertEquals(expectedCardId, fsCard.getValueAsLong(ECardInfo.CARD_ID));
	}
}
