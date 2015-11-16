package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.AllowChannel;
import io.epopeia.authorization.faker.AllowMerchantCnpjFaker.EAllowMerchantCnpj;
import io.epopeia.authorization.faker.AllowMerchantFaker.EAllowMerchant;
import io.epopeia.authorization.faker.ChannelFaker.EChannels;

/**
 * Faker de objetos AllowChannel
 * 
 * @author Fernando Amaral
 */
public class AllowChannelFaker {

	public enum EAllowChannel {
		CanalUmGrupoFarmaciasComMcc5122(1L, EChannels.CanalUm,
				EAllowMerchant.FarmaciasComMcc5122, null),
		CanalUmGrupoMinhaCasaMelhorComPOSCieloECnpj(2L, EChannels.CanalUm,
				EAllowMerchant.MinhaCasaMelhorComPOSCielo, 
				EAllowMerchantCnpj.MinhaCasaMelhor),
		CanalUmGrupoValeCulturaComMcc5733EPOSRede(3L, EChannels.CanalUm,
				EAllowMerchant.ValeCulturaComMcc5733EPOSRede, null);

		private Long codigoRestricaoCanal;
		private EChannels canal;
		private EAllowMerchant restricaoEstabelecimento;
		private EAllowMerchantCnpj restricaoCnpj;

		private EAllowChannel(Long codigoRestricaoCanal, 
				EChannels canal, EAllowMerchant restricaoEstabelecimento,
				EAllowMerchantCnpj restricaoCnpj) {
			this.codigoRestricaoCanal = codigoRestricaoCanal;
			this.canal = canal;
			this.restricaoEstabelecimento = restricaoEstabelecimento;
			this.restricaoCnpj = restricaoCnpj;
		}

		public EAllowMerchant getEAllowMerchant() {
			return restricaoEstabelecimento;
		}

		public EAllowMerchantCnpj getRestricaoCnpj() {
			return restricaoCnpj;
		}

		public EChannels getCanal() {
			return canal;
		}
	}

	static public AllowChannel getAllowChannel() {
		return getAllowChannel(EAllowChannel.CanalUmGrupoFarmaciasComMcc5122);
	}

	static public AllowChannel getAllowChannel(EAllowChannel amf) {
		AllowChannel am = new AllowChannel();
		am.setCodigoRestricaoCanal(amf.codigoRestricaoCanal);
		am.setCodigoCanal(amf.canal.getCodigoCanal());
		if (amf.restricaoEstabelecimento != null)
			am.setCodigoGrupoRestricao(amf.restricaoEstabelecimento.getCodigoGrupoRestricao());
		else if (amf.restricaoCnpj != null)
			am.setCodigoGrupoRestricao(amf.restricaoCnpj.getCodigoGrupoRestricao());
		am.setValidaAutorizacao(1);
		return am;
	}
}
