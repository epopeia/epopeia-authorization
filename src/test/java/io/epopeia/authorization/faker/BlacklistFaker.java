package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.BlacklistEstabelecimento;

/**
 * Faker de objetos BlacklistEstabelecimento
 * 
 * @author Fernando Amaral
 */
public class BlacklistFaker {

	public enum EBlacklistFaker {
		EstabelecimentoUmRede("006207", "006445438"),
		EstabelecimentoDoisCielo("012088", "010397731060001"),
		EstabelecimentoTresCielo("012088", "010470079210001"),
		EstabelecimentoQuatroRede("006207", "033266506");

		private String codigoAdquirente;
		private String codigoEstabelecimento;

		private EBlacklistFaker(String codigoAdquirente,
				String codigoEstabelecimento) {
			this.codigoAdquirente = codigoAdquirente;
			this.codigoEstabelecimento = codigoEstabelecimento;
		}

		public String getCodigoAdquirente() {
			return codigoAdquirente;
		}

		public String getCodigoEstabelecimento() {
			return codigoEstabelecimento;
		}
	}

	static public BlacklistEstabelecimento getBlacklistEstabelecimento() {
		return getBlacklistEstabelecimento(EBlacklistFaker.EstabelecimentoUmRede);
	}

	static public BlacklistEstabelecimento getBlacklistEstabelecimento(EBlacklistFaker blf) {
		BlacklistEstabelecimento bl = new BlacklistEstabelecimento();
		bl.setCodigoAdquirente(blf.codigoAdquirente);
		bl.setCodigoEstabelecimento(blf.codigoEstabelecimento);
		return bl;
	}

}
