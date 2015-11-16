package io.epopeia.authorization.bo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.AllowChannelRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantCnpjRepository;
import io.epopeia.authorization.repository.backoffice.AllowMerchantRepository;

/**
 * Objeto de negocio que valida as regras do grupo de restricao que um cartao
 * esta inserido. As regras variam de permitir mccs, estabelecimentos, cnpj
 * especificos e restricoes globais de um canal.
 * 
 * @author Fernando Amaral
 */
@Service
public class RestrictionBO {

	private AllowMerchantRepository restricoes;
	private AllowMerchantCnpjRepository restricoesCnpj;
	private AllowChannelRepository restricoesCanais;

	@Autowired
	public RestrictionBO(AllowMerchantRepository restrictionRepository,
						 AllowMerchantCnpjRepository restrictionCnpjRepository,
						 AllowChannelRepository restrictionChannelRepository) {
		this.restricoes = restrictionRepository;
		this.restricoesCnpj = restrictionCnpjRepository;
		this.restricoesCanais = restrictionChannelRepository;
	}

	public void check(FieldSet imf) {
		FieldSet cardInfo = (FieldSet) imf.getComponent(ECardInfo.CARD_INFO);
		if (cardInfo != null) {
			Long codigoGrupoRestricao = cardInfo.getValueAsLong(ECardInfo.GROUP_RESTRICTION_ID);

			if (codigoGrupoRestricao != null) {
				// Busca por grupo restricao do cartao
				if (allowByMerchant(codigoGrupoRestricao, imf)) {
					imf.getLabels(EFields.TAGS).add(ELabels.ALLOW_MERCHANT);
				}
			} else {
				Long codigoCanal = cardInfo.getValueAsLong(ECardInfo.CARD_CHANNEL_ID);
				if (existsGroupRestrictionsForChannel(codigoCanal)) {
					imf.getLabels(EFields.TAGS).add(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL);
					// Busca por grupos restricoes do canal do cartao
					if (allowByChannel(codigoCanal, imf)) {
						imf.getLabels(EFields.TAGS).add(ELabels.ALLOW_CHANNEL);
					}
				}
			}
		}
	}

	public boolean existsGroupRestrictionsForChannel(Long codigoCanal) {
		Long gruposRestricoesDoCanal = new Long(0);
		if (codigoCanal != null) {
			gruposRestricoesDoCanal = restricoesCanais.countByCodigoCanal(codigoCanal);
			if (gruposRestricoesDoCanal.longValue() > 0)
				return true;
		}
		return false;
	}

	public boolean allowByChannel(Long codigoCanal, final FieldSet imf) {
		Long countEstabelecimento = new Long(0);
		Long countCnpj = new Long(0);
		String mcc = imf.getValue(EFields.MCC);
		String cnpj = imf.getValue(EFields.CPF_CNPJ);
		String codigoAdquirenteSemZeros = imf.getValue(EFields.ACQUIRER_INSTITUTION).replaceFirst("^0+(?!$)", "");
		String codigoEstabelecimentoSemZeros = imf.getValue(EFields.MERCHANT_ID).replaceFirst("^0+(?!$)", "");

		// Busca excecoes para esse grupo por mcc adquirente estabelecimento
		countEstabelecimento = restricoesCanais
			.countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
					codigoCanal, mcc, codigoAdquirenteSemZeros,
					getPossibleMerchantIds(codigoAdquirenteSemZeros, codigoEstabelecimentoSemZeros));

		if (countEstabelecimento != null && countEstabelecimento.longValue() > 0)
			return true;

		// Se nao existir tenta encontrar uma excecao pelo cnpj
		if (cnpj != null && cnpj.length() > 0) {
			countCnpj = restricoesCanais.countByCodigoCanalAndCnpj(codigoCanal, cnpj);

			if (countCnpj != null && countCnpj.longValue() > 0)
				return true;
		}

		return false;
	}

	public boolean allowByMerchant(Long codigoGrupoRestricao, final FieldSet imf) {
		Long count = new Long(0);
		Long countCnpj = new Long(0);
		String mcc = imf.getValue(EFields.MCC);
		String cnpj = imf.getValue(EFields.CPF_CNPJ);
		String codigoAdquirenteSemZeros = imf.getValue(EFields.ACQUIRER_INSTITUTION).replaceFirst("^0+(?!$)", "");
		String codigoEstabelecimentoSemZeros = imf.getValue(EFields.MERCHANT_ID).replaceFirst("^0+(?!$)", "");

		// Busca excecoes para esse grupo por mcc adquirente estabelecimento
		count = restricoes
			.countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
					codigoGrupoRestricao, mcc, codigoAdquirenteSemZeros,
					getPossibleMerchantIds(codigoAdquirenteSemZeros, codigoEstabelecimentoSemZeros));

		if (count != null && count.longValue() > 0)
			return true;

		// Se nao existir tenta encontrar uma excecao pelo cnpj
		if (cnpj != null && cnpj.length() > 0) {
			countCnpj = restricoesCnpj.countByCodigoGrupoRestricaoAndCnpj(codigoGrupoRestricao, cnpj);

			if (countCnpj != null && countCnpj.longValue() > 0)
				return true;
		}

		return false;
	}

	public Collection<String> getPossibleMerchantIds(String acquirerId, String merchantId) {
		Set<String> idsEstabelecimentos = new HashSet<String>();

		idsEstabelecimentos.add(merchantId);

		// Retirando o codigo de filial dos quatro digitos finais quando CIELO
		if (acquirerId.equals("12088") || acquirerId.equals("6142")) {
			idsEstabelecimentos.add(merchantId.substring(0, merchantId.length() - 4));
		}

		return idsEstabelecimentos;
	}
}
