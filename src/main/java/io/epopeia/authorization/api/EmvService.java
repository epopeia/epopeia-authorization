package io.epopeia.authorization.api;

import io.epopeia.authorization.model.FieldSet;

/**
 * Interface a ser implementada por todos os gateways que utilizam servicos de
 * validacoes EMV externos
 * 
 * @author Fernando Amaral
 */
public interface EmvService {
	String getCryptogramCommand(final FieldSet imf) throws Exception;

	boolean validateResponseFromEmvExtService(final String retFromEmvExtService, FieldSet imf) throws Exception;
}
