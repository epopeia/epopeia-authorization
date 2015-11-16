package io.epopeia.jpos;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;

import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;

/**
 * Implementacao de um listener do jpos utilizando filas Space
 * para enviar para o componente Q2 - TransactionManager processar.
 * Neste momento sao inseridos no contexto a mensagem de requisicao
 * uma nova referencia do imf e a porta de origem.
 * 
 * @author Fernando Amaral
 */
public class ISORequestListenerImpl implements ISORequestListener {
	@SuppressWarnings("unchecked")
	Space<String, Context> sp = SpaceFactory.getSpace("tspace:default");

	public static final String TXNMGR_QUEUE = "txnmgrQueue";

	public boolean process(ISOSource source, ISOMsg request) {		
		//Wrap the source and iso in a context
		Context ctx = new Context();

		JPOSContext.putRequestMessageFormat(ctx, request);
		JPOSContext.putIsoSource(ctx, source);
		JPOSContext.registerTimeMillisListenerIn(ctx);

		//Also add a new reference of internal message format
		JPOSContext.putInternalMessageFormat(ctx, new FieldSet(EFields.IMF));

		//Put it in the txn manager for processing
		sp.out(TXNMGR_QUEUE, ctx);

		//Release the Listener
		return true;
	}
}
