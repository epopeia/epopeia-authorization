package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandlerRequired;
import io.epopeia.authorization.bo.AutorizadoraBO;
import io.epopeia.authorization.enums.EFields;

/**
 * The aim of this participant is save all iso 8583 messages
 * that were assembled and sent to the channel source.
 * 
 * @author Fernando Amaral
 */
@Service
public class SaveMessageIsoOut extends ChainHandlerRequired {

	@Autowired
	private AutorizadoraBO autorizadoraBO;

	@Override
	public int execute(Context context) throws Exception {
		Long codigoTransacao = null;

		//We must be sure that a imf message was successfully created.
		if(imf(context) != null) {
			codigoTransacao = imf(context).getValueAsLong(EFields.AUTORIZADORA_NSU);
		}

		//We must be sure that a response message was successfully created and
		//there is an ID that references a request message in MESSAGE_ISO_IN.
		if(response(context) != null && codigoTransacao != null) {
			autorizadoraBO.saveModelMessageOut(response(context), codigoTransacao);
		}

		return PREPARED | NO_JOIN | READONLY;
	}
}
