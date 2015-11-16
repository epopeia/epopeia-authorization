package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.beans.TrapLoader;

/**
 * The aim of this participant is validate the integrity of the message
 * like a valid date, a valid number in amount, a valid pan length etc
 * These traps should be designed for general transaction types and
 * should not rely to database or external entities like HSM.
 * 
 * @author Fernando Amaral
 */
@Service
public class BasicValidations extends ChainHandler {

	@Autowired
	private TrapLoader trapLoader;

	@Override
	public int execute(Context context) throws Exception {
		if(trapLoader.validateAll("basicValidations", imf(context)))
			return ABORTED;

		return PREPARED | NO_JOIN | READONLY;
	}
}
