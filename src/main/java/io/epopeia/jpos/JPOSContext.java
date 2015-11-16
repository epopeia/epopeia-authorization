package io.epopeia.jpos;

import java.io.Serializable;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;

import io.epopeia.authorization.model.FieldSet;

/**
 * Classe responsavel por manipular o contexto do jpos que
 * sera trafegado pelos componentes do Q2 bem como a definicao
 * das chaves desse contexto.
 * 
 * @author Fernando Amaral
 */
public class JPOSContext {
	public static final String IMF = "IMF";
	public static final String REQUEST = "REQUEST";
	public static final String RESPONSE = "RESPONSE";
	public static final String ISOSOURCE = "ISOSOURCE";
	public static final String TS_LISTENER_IN = "TS_LISTENER_IN";
	public static final String TS_FIRST_PARTICIPANT_IN = "TS_FIRST_PARTICIPANT_IN";
	public static final String TS_LAST_PARTICIPANT_OUT = "TS_LAST_PARTICIPANT_OUT";
	public static final String TS_EMVSERVICE_SEND = "TS_EMVSERVICE_SEND";
	public static final String TS_EMVSERVICE_RECEIVE = "TS_EMVSERVICE_RECEIVE";

	public static ISOSource getIsoSource(final Serializable ctx) {
		return (ISOSource) ((Context)ctx).get(ISOSOURCE);
	}
	public static void putIsoSource(final Serializable ctx, final ISOSource source) {
		((Context)ctx).put(ISOSOURCE, source);
	}

	public static FieldSet getInternalMessageFormat(final Serializable ctx) {
		return (FieldSet) ((Context)ctx).get(IMF);
	}
	public static void putInternalMessageFormat(final Serializable ctx, final FieldSet imf) {
		((Context)ctx).put(IMF, imf);
	}

	public static ISOMsg getRequestMessageFormat(final Serializable ctx) {
		return (ISOMsg) ((Context)ctx).get(REQUEST);
	}
	public static void putRequestMessageFormat(final Serializable ctx, final ISOMsg request) {
		((Context)ctx).put(REQUEST, request);
	}

	public static ISOMsg getResponseMessageFormat(final Serializable ctx) {
		return (ISOMsg) ((Context)ctx).get(RESPONSE);
	}
	public static void putResponseMessageFormat(final Serializable ctx, final ISOMsg response) {
		((Context)ctx).put(RESPONSE, response);
	}

	public static void registerTimeMillisListenerIn(final Serializable ctx) {
		((Context)ctx).put(TS_LISTENER_IN, System.currentTimeMillis());
	}
	public static Long getTimeMillisListenerIn(final Serializable ctx) {
		return (Long) ((Context)ctx).get(TS_LISTENER_IN);
	}

	public static void registerTimeMillisFirstParticipant(final Serializable ctx) {
		((Context)ctx).put(TS_FIRST_PARTICIPANT_IN, System.currentTimeMillis());
	}
	public static Long getTimeMillisFirstParticipant(final Serializable ctx) {
		return (Long) ((Context)ctx).get(TS_FIRST_PARTICIPANT_IN);
	}

	public static void registerTimeMillisLastParticipant(final Serializable ctx) {
		((Context)ctx).put(TS_LAST_PARTICIPANT_OUT, System.currentTimeMillis());
	}
	public static Long getTimeMillisLastParticipant(final Serializable ctx) {
		return (Long) ((Context)ctx).get(TS_LAST_PARTICIPANT_OUT);
	}

	public static void registerTimeMillisEmvServiceSend(final Serializable ctx) {
		((Context)ctx).put(TS_EMVSERVICE_SEND, System.currentTimeMillis());
	}
	public static Long getTimeMillisEmvServiceSend(final Serializable ctx) {
		return (Long) ((Context)ctx).get(TS_EMVSERVICE_SEND);
	}

	public static void registerTimeMillisEmvServiceReceive(final Serializable ctx) {
		((Context)ctx).put(TS_EMVSERVICE_RECEIVE, System.currentTimeMillis());
	}
	public static Long getTimeMillisEmvServiceReceive(final Serializable ctx) {
		return (Long) ((Context)ctx).get(TS_EMVSERVICE_RECEIVE);
	}
}
