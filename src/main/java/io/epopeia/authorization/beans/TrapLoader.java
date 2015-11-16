package io.epopeia.authorization.beans;

import java.util.List;

import org.jpos.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.epopeia.authorization.api.AuthorizationTrap;
import io.epopeia.authorization.model.FieldSet;

/**
 * Componente responsavel por carregar as travas em grupos de listas
 * de acordo com o qualificador da trava. Novos qualificadores podem
 * ser criados futuramente e serem chamados nos participantes.
 * Os participantes nao chamam as travas diretamente. Eles chamam esse
 * bean que itera todas as travas da lista especificada.
 * 
 * @author Fernando Amaral
 */
@Component
public class TrapLoader {
	private static final Logger logger = LoggerFactory.getLogger(TrapLoader.class);

	@Autowired(required=false)
	@Qualifier("basicValidations")
	private List<AuthorizationTrap> basicValidations;

	@Autowired(required=false)
	@Qualifier("authValidations")
	private List<AuthorizationTrap> authValidations;
	
	@Autowired(required=false)
	@Qualifier("businessRules")
	private List<AuthorizationTrap> businessRules;

	public boolean validateAll(final String group, FieldSet imf) throws Exception {
		List<AuthorizationTrap> trapsToValidate = null;

		// filter the traps by qualifier
		if(group.equals("basicValidations"))
			trapsToValidate = basicValidations;
		else if(group.equals("authValidations"))
			trapsToValidate = authValidations;
		else if(group.equals("businessRules"))
			trapsToValidate = businessRules;

		// Validate the authorization traps if any
		if(trapsToValidate != null && trapsToValidate.size() > 0) {
			for(AuthorizationTrap trap : trapsToValidate) {
				if(trap.validate(imf)) {
					String info = "Trapped when validating " + trap.getClass().getCanonicalName();
					Log.getLog("Q2", "TM").info(info);
					logger.info(info);
					return true;
				}
			}
		}

		return false;
	}
}
