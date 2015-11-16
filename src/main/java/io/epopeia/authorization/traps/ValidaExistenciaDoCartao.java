package io.epopeia.authorization.traps;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Message;

@Component
@Order(value=2)
@Qualifier("businessRules")
public class ValidaExistenciaDoCartao implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Long cardId = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;

		if(cardId != null)
			return false;

		imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_NUMERO_CARTAO_INVALIDO);
		return true;
	}
}
