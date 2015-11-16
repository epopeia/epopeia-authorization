package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.beans.TrapLoader;

/**
 * The aim of this participant is evaluate all business rules
 * according to all informations loaded in the IMF message.
 * Such validations could be the permissions for a specific
 * modality, rules of authorization for e-commerce, chip,
 * merchant, products etc
 * 
 * @author Fernando Amaral
 */
@Service
public class ApplyBusinessRules extends ChainHandler {

	@Autowired
	private TrapLoader trapLoader;

	@Override
	public int execute(Context context) throws Exception {
		// Validate the business rules if any
		if(trapLoader.validateAll("businessRules", imf(context)))
			return ABORTED;

		return PREPARED | NO_JOIN | READONLY;
	}
}
