package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.AllowMerchant;

/**
 * Faker de objetos AllowMerchant
 * 
 * @author Fernando Amaral
 */
public class AllowMerchantFaker {

	public enum EAllowMerchant {
		FarmaciasComMcc5122(1L, 94L, "5122", null, null),
		FarmaciasComMcc5912(2L, 94L, "5912", null, null),
		ValeCulturaComMcc5733EPOSRede(3L, 139L, "5733", "6207", null),
		MinhaCasaMelhorComPOSCielo(4L, 18L, null, "6142", "1000046556"), //sem filial
		MinhaCasaMelhorComPOSRede(5L, 18L, null, "6207", "10019979"),
		LojaUmMagazineLuizaComPOSCielo(6L, 39L, null, "12088", "1050051405"), //sem filial
		LojaDoisMagazineLuizaComPOSCielo(7L, 39L, null, "12088", "10489776650001"), //com filial
		LojaTresMagazineLuizaComPOSRede(8L, 39L, null, "6207", "56388306");

		private Long codigoRestricao;
		private Long codigoGrupoRestricao;
		private String mcc;
		private String codigoAdquirente;
		private String codigoEstabelecimento;

		private EAllowMerchant(Long codigoRestricao, Long codigoGrupoRestricao,
				String mcc, String codigoAdquirente, String codigoEstabelecimento) {
			this.codigoRestricao = codigoRestricao;
			this.codigoGrupoRestricao = codigoGrupoRestricao;
			this.mcc = mcc;
			this.codigoAdquirente = codigoAdquirente;
			this.codigoEstabelecimento = codigoEstabelecimento;
		}

		public Long getCodigoGrupoRestricao() {
			return codigoGrupoRestricao;
		}

		public String getMcc() {
			return mcc;
		}

		public String getCodigoAdquirente() {
			return codigoAdquirente;
		}

		public String getCodigoEstabelecimento() {
			return codigoEstabelecimento;
		}
	}

	static public AllowMerchant getAllowMerchant() {
		return getAllowMerchant(EAllowMerchant.MinhaCasaMelhorComPOSCielo);
	}

	static public AllowMerchant getAllowMerchant(EAllowMerchant amf) {
		AllowMerchant am = new AllowMerchant();
		am.setCodigoRestricao(amf.codigoRestricao);
		am.setCodigoGrupoRestricao(amf.codigoGrupoRestricao);
		am.setMcc(amf.mcc);
		am.setCodigoAdquirente(amf.codigoAdquirente);
		am.setCodigoEstabelecimento(amf.codigoEstabelecimento);
		return am;
	}
}
