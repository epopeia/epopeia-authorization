package io.epopeia.authorization.participant;

import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandlerRequired;
import io.epopeia.jpos.JPOSContext;

/**
 * Participante que apenas joga numa fila de processamento uma
 * copia da mensagem para ser transformada em json.
 * 
 * @author Fernando Amaral
 */
@Service
public class LogStash extends ChainHandlerRequired {

	@SuppressWarnings("unchecked")
	Space<String, Context> sp = SpaceFactory.getSpace("tspace:default");

	public static final String LOGSTASH_QUEUE = "logstashQueue";

	@Override
	public int execute(Context context) throws Exception {	

		JPOSContext.registerTimeMillisLastParticipant(context);

		//Put it in the logstash queue for processing
		sp.out(LOGSTASH_QUEUE, context);

		return PREPARED | NO_JOIN | READONLY;
	}
}
