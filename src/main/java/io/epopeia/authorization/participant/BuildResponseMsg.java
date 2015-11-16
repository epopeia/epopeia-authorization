package io.epopeia.authorization.participant;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandlerRequired;
import io.epopeia.authorization.api.ResponseBuilder;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.jpos.JPOSContext;

/**
 * The aim of this participant is delegate to gateways the assembly
 * of iso response message that will be returned to Brands.
 * 
 * @author Fernando Amaral
 */
@Service
public class BuildResponseMsg extends ChainHandlerRequired {

	@Autowired(required=false)
	private ResponseBuilder responseBuilder;

	@Override
	public int execute(Context context) throws Exception {
		ISOMsg response = null;
		String respCode = null;

		try {
			//Get a specific brand's respCode before build the response message even
			//if getImf() returns null the concrete implementation must choice what
			//code to return. In must cases the gateways returns SYSTEM_ERROR.
			respCode = responseBuilder.getResponseCode(imf(context), request(context));

			// We must be sure that xmf message was converted in an imf message successfully
			if(imf(context) != null) {
				// Assign it in the instance of imf message and build the response.
				imf(context).setValue(EFields.RESP_CODE, respCode);
				response = responseBuilder.buildResponseMsg(imf(context), request(context));
			} else {
				// Otherwise we must build a response message based only in xmf fields
				response = responseBuilder.cloneForResponse(request(context), respCode);
			}

			JPOSContext.putResponseMessageFormat(context, response);
		} catch (Exception e) {
			try {
				// Even if we got an exception lets try to generate a response with system error code.
				response = responseBuilder.cloneForResponse(request(context), respCode);
				JPOSContext.putResponseMessageFormat(context, response);
			} catch (ISOException e1) {
				// Add exception to the current stack trace
				e.addSuppressed(e1);
				throw e;
			}
		}

		return PREPARED | NO_JOIN | READONLY;
	}
}
