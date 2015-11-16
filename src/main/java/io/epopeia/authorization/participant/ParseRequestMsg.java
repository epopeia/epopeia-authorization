package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.api.RequestParser;

/**
 * The aim of this participant is delegate to gateway parse
 * the message from brand into the IMF container. Also the
 * labels are assigned here.
 * 
 * @author Fernando Amaral
 */
@Service
public class ParseRequestMsg extends ChainHandler {

	@Autowired(required=false)
	private RequestParser requestParser;

	@Override
	public int execute(Context context) throws Exception {
		requestParser.parseRequestMsg(request(context), imf(context));
		requestParser.assignMessageLabels(request(context), imf(context));
		
		return PREPARED | NO_JOIN | READONLY;
	}
}
