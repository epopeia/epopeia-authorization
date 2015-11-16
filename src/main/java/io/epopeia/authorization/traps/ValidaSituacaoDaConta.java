package io.epopeia.authorization.traps;

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
@Order(value = 4)
@Qualifier("businessRules")
public class ValidaSituacaoDaConta implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Message fsTxnAttr = imf.getComponent(ETxnAttributes.ATRIBUTOS_TRANSACAO);

		if (fsCard != null && fsCard.getValue(ECardInfo.ACCOUNT_STATUS).equals("A") == false) {
			if (fsTxnAttr != null && fsTxnAttr.getValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA).equals("N") == false) {
				String situacaoConta = fsCard.getValue(ECardInfo.ACCOUNT_SITUATION);

				if (situacaoConta.equals("BLOQUEADA_CONTA_EM_COBRANCA") ||
					situacaoConta.equals("CANCELADA_CONTA_EM_COBRANCA") ||
					situacaoConta.equals("CANCELADO_CONTA_EM_COBRANCA"))
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CONTA_EM_COBRANCA);
				else
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CONTA_CANCELADA_BLOQUEADA);

				return true;
			}
		}

		return false;
	}
}
