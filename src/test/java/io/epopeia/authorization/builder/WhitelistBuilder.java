package io.epopeia.authorization.builder;

import io.epopeia.authorization.domain.backoffice.WhitelistEstabelecimento;
import io.epopeia.authorization.faker.WhitelistFaker;
import io.epopeia.authorization.faker.WhitelistFaker.EWhitelistFaker;

/**
 * Builder de objetos WhitelistEstabelecimento
 * 
 * @author Fernando Amaral
 */
public class WhitelistBuilder {

	private WhitelistEstabelecimento whitelist;

	public WhitelistBuilder() {
		this.whitelist = WhitelistFaker.getWhitelistEstabelecimento();
	}

	public WhitelistBuilder whitelistEstabelecimento(EWhitelistFaker eWhitelistFaker) {
		this.whitelist = WhitelistFaker.getWhitelistEstabelecimento(eWhitelistFaker);
		return this;
	}

	public WhitelistBuilder withCvc2EmptyPermission() {
		this.whitelist.setCvc2ausente('S');
		return this;
	}

	public WhitelistEstabelecimento create() {
		return whitelist;
	}
}
