package io.epopeia.authorization.api;

import io.epopeia.authorization.model.FieldSet;

/**
 * Interface a ser implementada por todas as travas do sistema
 * incluindo basicValidations, authValidations, businessRules etc
 * O Spring eh responsavel por carregar listas de travas de acordo
 * com o qualificador dela. As travas sao carregadas pelo bean
 * TrapLoader.
 * 
 * @author Fernando Amaral
 */
public interface AuthorizationTrap {
	boolean validate(FieldSet imf) throws Exception;
}
