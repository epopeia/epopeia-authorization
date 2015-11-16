package io.epopeia.authorization.api;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import io.epopeia.authorization.model.FieldSet;

/**
 * Interface a ser implementada pelos gateways para montar as mensagens
 * externas das bandeiras do formato comum para o formato especifico.
 * Neste momento tb eh feito o mapping das situacoes de erro do framework
 * para os codigos de resposta de cada bandeira.
 * 
 * @author Fernando Amaral
 */
public abstract class ResponseBuilder {
	public abstract ISOMsg buildResponseMsg(final FieldSet imf, final ISOMsg request) throws Exception;
	public abstract String getResponseCode(final FieldSet imf, final ISOMsg request) throws Exception;

	public ISOMsg cloneForResponse(ISOMsg request, String respCode) throws ISOException {
		ISOMsg response = null;
		response = (ISOMsg) request.clone();
		response.setDirection(ISOMsg.OUTGOING);
		response.setResponseMTI();
		response.set(39, respCode);
		return response;
	}
}
