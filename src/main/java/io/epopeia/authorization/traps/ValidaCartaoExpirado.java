package io.epopeia.authorization.traps;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ETxnAttributes;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Message;

@Component
@Order(value = 5)
@Qualifier("businessRules")
public class ValidaCartaoExpirado implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Message fsTxnAttr = imf.getComponent(ETxnAttributes.ATRIBUTOS_TRANSACAO);

		if (fsCard != null && fsTxnAttr != null &&
			fsTxnAttr.getValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO).equals("N") == false) {
			Date dtExpDate = fsCard.getValueAsDate(ECardInfo.CARD_EXPIRY_DATE);
			Date dtAtual = Calendar.getInstance().getTime();

			if (dtExpDate != null && dtExpDate.before(dtAtual)) {
				imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CARTAO_EXPIRADO);
				return true;
			}
		}

		return false;
	}
}
