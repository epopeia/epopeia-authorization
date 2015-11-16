package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.WhitelistEstabelecimento;

/**
 * Faker de objetos WhitelistEstabelecimentos
 * 
 * @author Fernando Amaral
 */
public class WhitelistFaker {

	public enum EWhitelistFaker {
		EstabelecimentoUmRede("006207", "020260121", "MAGAZINE LUIZA COM     FRANCA        BRA"),
		EstabelecimentoDoisRede("006207", "047311410", "AMERICANAS COM         RIO DE JANEIR BRA"),
		EstabelecimentoTresRede("006207", "047311592", "SUBMARINO COM          RIO DE JANEIR BRA"),
		EstabelecimentoQuatroRede("006207", "047311614", "SHOPTIME.COM           RIO DE JANEIR BRA");

		private String codigoAdquirente;
		private String codigoEstabelecimento;
		private String estabelecimento;

		private EWhitelistFaker(String codigoAdquirente,
				String codigoEstabelecimento,
				String estabelecimento) {
			this.codigoAdquirente = codigoAdquirente;
			this.codigoEstabelecimento = codigoEstabelecimento;
			this.estabelecimento = estabelecimento;
		}

		public String getCodigoAdquirente() {
			return codigoAdquirente;
		}

		public String getCodigoEstabelecimento() {
			return codigoEstabelecimento;
		}

		public String getEstabelecimento() {
			return estabelecimento;
		}

	}

	static public WhitelistEstabelecimento getWhitelistEstabelecimento() {
		return getWhitelistEstabelecimento(EWhitelistFaker.EstabelecimentoUmRede);
	}

	static public WhitelistEstabelecimento getWhitelistEstabelecimento(EWhitelistFaker wlf) {
		WhitelistEstabelecimento wl = new WhitelistEstabelecimento();
		wl.setCodigoAdquirente(wlf.codigoAdquirente);
		wl.setCodigoEstabelecimento(wlf.codigoEstabelecimento);
		wl.setEstabelecimento(wlf.estabelecimento);
		return wl;
	}

}
