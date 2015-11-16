package io.epopeia.authorization.beans;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.bo.ParameterBO;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Message;

/**
 * Componente responsavel por buscar os parametros de acordo com a priorizacao
 * das entidades utilizando como fallback os parametros de sistema.
 * 
 * @author Fernando Amaral
 */
@Component
public class ParameterSearch {

	@Autowired
	private ParameterBO parameterBO;

	public String getParameterValue(final FieldSet imf, String identificador, String defaultValue) {
		String ret = getParameterValue(imf, identificador);
		return ret != null ? ret : defaultValue;
	}

	public String getParameterValue(final FieldSet imf, String identificador) {
		Message cardInfo = imf.getComponent(ECardInfo.CARD_INFO);

		if (cardInfo != null) {
			Map<String, String> map = null;
			Long codigoContaTitular = cardInfo
					.getValueAsLong(ECardInfo.ACCOUNT_ID);
			Long codigoCanal = cardInfo
					.getValueAsLong(ECardInfo.CARD_CHANNEL_ID);
			Long codigoModalidade = cardInfo
					.getValueAsLong(ECardInfo.MODALITY_ID);
			Long codigoProduto = cardInfo.getValueAsLong(ECardInfo.PRODUCT_ID);

			for (int priority = 0; priority < 4; priority++) {

				// select a map of parameters by entity priority
				switch (priority) {
				case 0:
					map = codigoContaTitular == null ? null : parameterBO
							.getAllAccountParameters(codigoContaTitular);
					break;
				case 1:
					map = codigoCanal == null ? null : parameterBO
							.getAllChannelParameters(codigoCanal);
					break;
				case 2:
					map = codigoModalidade == null ? null : parameterBO
							.getAllModalityParameters(codigoModalidade);
					break;
				case 3:
					map = codigoProduto == null ? null : parameterBO
							.getAllProductParameters(codigoProduto);
					break;
				}

				// verify if identifier is in the map
				if (map != null) {
					String value = map.get(identificador);
					if (value != null) {
						return value;
					}
				}
			}
		}

		// try to return the system parameter as fall back
		return parameterBO.getSystemParameterValue(identificador);
	}
}
