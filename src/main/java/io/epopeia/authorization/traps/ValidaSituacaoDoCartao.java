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
@Order(value = 3)
@Qualifier("businessRules")
public class ValidaSituacaoDoCartao implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Message fsTxnAttr = imf.getComponent(ETxnAttributes.ATRIBUTOS_TRANSACAO);

		if (fsCard != null && fsCard.getValue(ECardInfo.CARD_STATUS).equals("A") == false) {
			if (fsTxnAttr != null && fsTxnAttr.getValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO).equals("N") == false) {
				String situacaoCartao = fsCard.getValue(ECardInfo.CARD_SITUATION);

				if (situacaoCartao.equals("BLOQUEADO_CARTAO_PERDIDO") || situacaoCartao.equals("CANCELADO_CARTAO_PERDIDO"))
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CARTAO_PERDIDO);
				else if (situacaoCartao.equals("BLOQUEADO_CARTAO_ROUBADO") || situacaoCartao.equals("CANCELADO_CARTAO_ROUBADO"))
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CARTAO_ROUBADO);
				else if (situacaoCartao.equals("BLOQUEADO_RETER_CARTAO"))
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_RETER_CARTAO);
				else if (situacaoCartao.equals("BLOQUEADO_EXCESSO_DE_SAQUE"))
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_EXCESSO_DE_SAQUE);
				else
					imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CARTAO_BLOQUEADO_CANCELADO);

				return true;
			}
		}

		return false;
	}
}
