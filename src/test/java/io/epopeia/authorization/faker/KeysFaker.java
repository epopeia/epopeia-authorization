package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.Key;

/**
 * Faker de objetos Keys
 * 
 * @author Fernando Amaral
 */
public class KeysFaker {

	public enum EKeys {
		ChaveUm(8L, "001", "UABE54714E5C0C2D1CE5145C4CA2E51CA", 'S'),
		ChaveDois(7L, "002", "UE4380A768C6EE1F93BB57B5FAF935B9F", 'S'),
		Chave00B(9L, "00B", "U845897A154C4C6A00F2D1F350AB2C7D9", 'S');

		private Long codigoChave;
		private String codigoTipoChave;
		private String chave;
		private Character ativa;

		private EKeys(Long codigoChave, String codigoTipoChave, String chave,
				Character ativa) {
			this.codigoChave = codigoChave;
			this.codigoTipoChave = codigoTipoChave;
			this.chave = chave;
			this.ativa = ativa;
		}

		public String getChave() {
			return chave;
		}
	}

	static public Key getKey() {
		return getKey(EKeys.ChaveUm);
	}

	static public Key getKey(EKeys kf) {
		Key k = new Key();
		k.setCodigoChave(kf.codigoChave);
		k.setCodigoTipoChave(kf.codigoTipoChave);
		k.setChave(kf.chave);
		k.setAtiva(kf.ativa);
		return k;
	}
}
