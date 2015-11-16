package io.epopeia.authorization.traps;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Labels;

@Component
@Order(value = 9)
@Qualifier("businessRules")
public class ValidaBlacklistEstabelecimento implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Labels tags = imf.getLabels(EFields.TAGS);

		if (tags.contains(ELabels.MERCHANT_IN_BLACKLIST)) {
			imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_BLACKLIST_ESTABELECIMENTO);
			return true;
		}

		return false;
	}
}
