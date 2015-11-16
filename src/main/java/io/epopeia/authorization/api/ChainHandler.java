package io.epopeia.authorization.api;

import java.io.Serializable;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import io.epopeia.authorization.bo.AutorizadoraBO;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.spring.SpringContext;
import io.epopeia.jpos.JPOSContext;

/**
 * Wrapper to TransactionParticipant interface of jpos that
 * add a method execute with the throws Exception for we be
 * able to handle errors in the ChainOfResponsibility layer.
 *
 * @author Fernando Amaral
 */
public abstract class ChainHandler implements TransactionParticipant {

	public abstract int execute(Context context) throws Exception;

	@Override
	public int prepare(long id, Serializable context) {
		try {
			return execute((Context)context);
		} catch (Exception e) {
			SpringContext.getSpringContainer().getBean(AutorizadoraBO.class)
				.logError(this.getClass().getName(), e, context);
		}
		return ABORTED;
	}

	@Override
	public void commit(long id, Serializable context) {
	}

	@Override
	public void abort(long id, Serializable context) {
	}

	public final FieldSet imf(Context context) {
		return JPOSContext.getInternalMessageFormat(context);
	}

	public final ISOSource source(Context context) {
		return JPOSContext.getIsoSource(context);
	}

	public final ISOMsg request(Context context) {
		return JPOSContext.getRequestMessageFormat(context);
	}

	public final ISOMsg response(Context context) {
		return JPOSContext.getResponseMessageFormat(context);
	}
}
