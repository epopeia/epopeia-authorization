package io.epopeia.authorization.traps;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.enums.ETipoTransacao;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Labels;

@Component
@Order(value = 8)
@Qualifier("businessRules")
public class ValidaGrupoRestricao implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Enum<?> tipoTransacao = imf.getEnum(EFields.TIPO_TRANSACAO_BACKOFFICE, ETipoTransacao.class);

		if (tipoTransacao.equals(ETipoTransacao.COMPRA_CREDITO_A_VISTA)) {

			FieldSet cardInfo = (FieldSet) imf.getComponent(ECardInfo.CARD_INFO);
			if (cardInfo != null) {
				Long codigoGrupoRestricao = cardInfo.getValueAsLong(ECardInfo.GROUP_RESTRICTION_ID);
				Long codigoCanal = cardInfo.getValueAsLong(ECardInfo.CARD_CHANNEL_ID);
				Labels tags = imf.getLabels(EFields.TAGS);

				// cartao esta num grupo restricao e nao possui permissao de passar
				if (codigoGrupoRestricao != null && !tags.contains(ELabels.ALLOW_MERCHANT)) {
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_GRUPO_RESTRICAO);
					return true;
				}

				// cartao esta num canal com grupos de restricao e nao possui permissao de passar
				if (codigoCanal != null && tags.contains(ELabels.GROUP_RESTRICTIONS_IN_CHANNEL) &&
					!tags.contains(ELabels.ALLOW_CHANNEL)) {
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_GRUPO_RESTRICAO_CANAL);
					return true;
				}
			}
		}

		return false;
	}

}
