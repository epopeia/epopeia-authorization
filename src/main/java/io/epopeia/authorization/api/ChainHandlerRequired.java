package io.epopeia.authorization.api;

import java.io.Serializable;

import org.jpos.transaction.AbortParticipant;

/**
 * Wrapper to AbortParticipant interface of jpos that
 * add a method execute with the throws Exception for we be
 * able to handle errors in the ChainOfResponsibility layer.
 *
 * @author Fernando Amaral
 */
public abstract class ChainHandlerRequired extends ChainHandler implements AbortParticipant {

	@Override
	public int prepareForAbort(long id, Serializable context) {
		return prepare(id, context);
	}
}
