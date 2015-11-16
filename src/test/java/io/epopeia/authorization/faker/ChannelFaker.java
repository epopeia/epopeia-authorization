package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.Channel;

/**
 * Faker de objetos CardChannels
 * 
 * @author Fernando Amaral
 */
public class ChannelFaker {

	public enum EChannels {
		CanalUm(1L),
		CanalDois(2L);

		private Long codigoCanal;

		private EChannels(Long codigoCanal) {
			this.codigoCanal = codigoCanal;
		}

		public Long getCodigoCanal() {
			return codigoCanal;
		}
	}

	static public Channel getCardChannels() {
		return getCardChannels(EChannels.CanalUm);
	}

	static public Channel getCardChannels(EChannels cf) {
		Channel c = new Channel();
		c.setCodigoCanal(cf.codigoCanal);
		return c;
	}
}
