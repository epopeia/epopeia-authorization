package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.beans.TrapLoader;

/**
 * The aim of this participant is validate the authorization messages
 * like credit and debit transactions as well the products releated
 * with then like Agro, Construcard, GiftCards etc
 * These traps should be designed looking for the fields presents
 * in the own message and should not rely to database or
 * external entities like HSM.
 * 
 * @author Fernando Amaral
 */
@Service
public class AuthValidations extends ChainHandler {

	@Autowired
	private TrapLoader trapLoader;

	@Override
	public int execute(Context context) throws Exception {
		if(trapLoader.validateAll("authValidations", imf(context)))
			return ABORTED;

		return PREPARED | NO_JOIN | READONLY;
	}
}
