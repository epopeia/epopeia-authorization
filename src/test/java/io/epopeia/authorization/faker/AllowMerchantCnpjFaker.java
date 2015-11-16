package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.AllowMerchantCnpj;

/**
 * Faker de objetos AllowMerchantCnpj
 * 
 * @author Fernando Amaral
 */
public class AllowMerchantCnpjFaker {

	public enum EAllowMerchantCnpj {
		MinhaCasaMelhor(18L, "12345678901234");

		private Long codigoGrupoRestricao;
		private String cnpj;

		private EAllowMerchantCnpj(Long codigoGrupoRestricao, String cnpj) {
			this.codigoGrupoRestricao = codigoGrupoRestricao;
			this.cnpj = cnpj;
		}

		public Long getCodigoGrupoRestricao() {
			return codigoGrupoRestricao;
		}

		public String getCnpj() {
			return cnpj;
		}
	}

	static public AllowMerchantCnpj getAllowMerchantCnpj() {
		return getAllowMerchantCnpj(EAllowMerchantCnpj.MinhaCasaMelhor);
	}

	static public AllowMerchantCnpj getAllowMerchantCnpj(EAllowMerchantCnpj amf) {
		AllowMerchantCnpj am = new AllowMerchantCnpj();
		am.setCodigoGrupoRestricao(amf.codigoGrupoRestricao);
		am.setCnpj(amf.cnpj);
		return am;
	}
}
