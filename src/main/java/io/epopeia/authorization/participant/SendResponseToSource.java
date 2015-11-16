package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandlerRequired;

/**
 * The aim of this participant is response to the remote host that
 * ask for a request of authorization.
 * 
 * @author Fernando Amaral
 */
@Service
public class SendResponseToSource extends ChainHandlerRequired {

	@Override
	public int execute(Context context) throws Exception {
		//We must be sure that a response message was successfully created and there is a source to send.
		if(response(context) != null && source(context) != null) {
			source(context).send(response(context));
		}

		return PREPARED | NO_JOIN | READONLY;
	}
}
