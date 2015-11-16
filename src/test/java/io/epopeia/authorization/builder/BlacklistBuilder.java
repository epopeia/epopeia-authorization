package io.epopeia.authorization.builder;

import io.epopeia.authorization.domain.backoffice.BlacklistEstabelecimento;
import io.epopeia.authorization.faker.BlacklistFaker;
import io.epopeia.authorization.faker.BlacklistFaker.EBlacklistFaker;

/**
 * Builder de objetos BlacklistEstabelecimento
 * 
 * @author Fernando Amaral
 */
public class BlacklistBuilder {

	private BlacklistEstabelecimento blacklist;

	public BlacklistBuilder() {
		this.blacklist = BlacklistFaker.getBlacklistEstabelecimento();
	}

	public BlacklistBuilder blacklistEstabelecimento(EBlacklistFaker eBlacklistFaker) {
		this.blacklist = BlacklistFaker.getBlacklistEstabelecimento(eBlacklistFaker);
		return this;
	}

	public BlacklistEstabelecimento create() {
		return blacklist;
	}
}
