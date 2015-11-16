package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.AutorizadoraBO;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.jpos.JPOSContext;

/**
 * The aim of this participant is save all iso 8583 messages
 * that were received in the channel.
 * 
 * @author Fernando Amaral
 */
@Service
public class SaveMessageIsoIn extends ChainHandler {

	@Autowired
	private AutorizadoraBO autorizadoraBO;

	@Override
	public int execute(Context context) throws Exception {
		/* We're assuming this participant will always be the first one */
		JPOSContext.registerTimeMillisFirstParticipant(context);

		Long autorizadoraNSU = autorizadoraBO.saveModelMessageIn(request(context));
		imf(context).setValue(EFields.AUTORIZADORA_NSU, autorizadoraNSU.toString());

		return PREPARED | NO_JOIN | READONLY;
	}
}
