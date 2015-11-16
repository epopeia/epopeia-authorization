package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.TitularAccount;

/**
 * Faker de objetos TitularAccount
 * 
 * @author Fernando Amaral
 */
public class AccountFaker {

	public enum EAccounts {
		ContaUm(1L),
		ContaDois(2L);

		private Long codigoContaTitular;

		private EAccounts(Long codigoContaTitular) {
			this.codigoContaTitular = codigoContaTitular;
		}

		public Long getCodigoContaTitular() {
			return codigoContaTitular;
		}
	}

	static public TitularAccount getAccount() {
		return getAccount(EAccounts.ContaUm);
	}

	static public TitularAccount getAccount(EAccounts ctf) {
		TitularAccount ct = new TitularAccount();
		ct.setCodigoContaTitular(ctf.codigoContaTitular);
		return ct;
	}
}
