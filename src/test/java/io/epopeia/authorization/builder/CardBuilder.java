package io.epopeia.authorization.builder;

import io.epopeia.authorization.domain.backoffice.AccountSituation;
import io.epopeia.authorization.domain.backoffice.Card;
import io.epopeia.authorization.domain.backoffice.CardSituation;
import io.epopeia.authorization.domain.backoffice.Channel;
import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.domain.backoffice.TitularAccount;
import io.epopeia.authorization.faker.AccountFaker;
import io.epopeia.authorization.faker.AccountSituationFaker;
import io.epopeia.authorization.faker.CardFaker;
import io.epopeia.authorization.faker.CardSituationFaker;
import io.epopeia.authorization.faker.ChannelFaker;
import io.epopeia.authorization.faker.ModalityFaker;
import io.epopeia.authorization.faker.ProductFaker;
import io.epopeia.authorization.faker.AccountFaker.EAccounts;
import io.epopeia.authorization.faker.AccountSituationFaker.EAccountSituations;
import io.epopeia.authorization.faker.CardFaker.ECards;
import io.epopeia.authorization.faker.CardSituationFaker.ECardSituations;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ProductFaker.EProduct;

/**
 * Builder de objetos Card e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
public class CardBuilder {

	private Card cartao;

	public CardBuilder() {
		this.cartao = CardFaker.getCard();
	}

	public CardBuilder cartao(ECards cf) {
		this.cartao = CardFaker.getCard(cf);
		return this;
	}

	public CardBuilder withCardSituation(ECardSituations csf) {
		CardSituation situacaoCartao = CardSituationFaker.getCardSituation(csf);
		cartao.setSituacao(situacaoCartao);
		cartao.setStatus(csf.getStatus());
		return this;
	}

	public CardBuilder withChannel(EChannels chf) {
		Channel canal = ChannelFaker.getCardChannels(chf);
		canal.setCodigoCartao(this.cartao.getCodigoCartao());
		this.cartao.setCanal(canal);
		return this;
	}

	public CardBuilder withAccount(EAccounts ctf) {
		TitularAccount conta = AccountFaker.getAccount(ctf);
		cartao.setContaTitular(conta);
		return this;
	}

	public CardBuilder withAccountSituation(EAccountSituations ctsf) {
		AccountSituation situacaoConta = AccountSituationFaker.getCardSituation(ctsf);
		cartao.getContaTitular().setSituacao(situacaoConta);
		cartao.getContaTitular().setStatus(ctsf.getStatus());
		return this;
	}

	public CardBuilder withModality(EModality mf) {
		Modality modalidade = ModalityFaker.getModality(mf);
		cartao.getContaTitular().setModalidade(modalidade);
		return this;
	}

	public CardBuilder withProduct(EProduct pf) {
		Product produto = ProductFaker.getProduct(pf);
		cartao.getContaTitular().getModalidade().setProduto(produto);
		return this;
	}

	public Card create() {
		return this.cartao;
	}
}
