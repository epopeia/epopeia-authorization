package io.epopeia.authorization.traps;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.bo.HsmBO;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Labels;
import io.epopeia.authorization.model.Message;

@Component
@Order(value = 6)
@Qualifier("businessRules")
public class ValidaCodigosDeSeguranca implements AuthorizationTrap {

	@Override
	public boolean validate(FieldSet imf) throws Exception {
		Labels tags = imf.getLabels(EFields.TAGS);

		if (cardIssuedWithChip(imf) && // para cartoes emitidos com chip
			tags.contains(ELabels.CHIP) && // capturados com chip no term
			tags.contains(ELabels.CHIP_DATA)) { // com DE 55 presente na msg
			String respIcvv = imf.getValue(EFields.RESP_ICVV); // validamos o icvv
			if (!respIcvv.equals(HsmBO.SECURITY_CODE_OK)) {
				imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CODIGO_DE_SEGURANCA_CHIP_INVALIDO);
				return true;
			}
		}

		if (tags.contains(ELabels.MAGNETIC) && // para transacoes capturadas como magneticas
			(tags.contains(ELabels.TRACK1_PRESENT) || // com DE 45 presente na msg
			 tags.contains(ELabels.TRACK2_PRESENT))) { // ou DE 35 presente na msg
			String respCvc = imf.getValue(EFields.RESP_CVC); // validamos o cvc
			if (!respCvc.equals(HsmBO.SECURITY_CODE_OK)) {
				imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CODIGO_DE_SEGURANCA_INVALIDO);
				return true;
			}
		}

		if (tags.contains(ELabels.CVC2)) { // para todas as transacoes com coleta de cvc2
			String respCvc2 = imf.getValue(EFields.RESP_CVC2); // validamos o cvc2
			if (!respCvc2.equals(HsmBO.SECURITY_CODE_OK)) {
				imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_CODIGO_DE_SEGURANCA_2_INVALIDO);
				return true;
			}
		}

		return false;
	}

	private boolean cardIssuedWithChip(final FieldSet imf) {
		Message validarChip = FieldSet.navigate(imf, "card_info.parameters_info.validar_chip");
		return validarChip != null && validarChip.getValue().equals("S") ? true : false;
	}

}
