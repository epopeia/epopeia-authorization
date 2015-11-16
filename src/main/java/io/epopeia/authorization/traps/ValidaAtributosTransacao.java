package io.epopeia.authorization.traps;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ETxnAttributes;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Message;

@Component
@Order(value=1)
@Qualifier("businessRules")
public class ValidaAtributosTransacao implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Message fsTxnAttr = imf.getComponent(ETxnAttributes.ATRIBUTOS_TRANSACAO);

		if(fsTxnAttr != null)
			return false;

		imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_ATRIBUTOS_TRANSACAO_INVALIDO);
		return true;
	}
}
