package io.epopeia.authorization.traps;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Message.EmptyEnum;

@Component
@Order(value = 7)
@Qualifier("businessRules")
public class ValidaPinBlockCriptografado implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Enum<?> pinBlockSituation = imf.getEnum(EFields.PIN_BLOCK_SITUATION, ECodes.class);

		if (!pinBlockSituation.equals(EmptyEnum.EMPTY_ENUM) && !pinBlockSituation.equals(ECodes.AUTORIZADA)) {
			imf.setValue(EFields.STATUS_TRANSACAO, pinBlockSituation);
			return true;
		}

		return false;
	}

}
