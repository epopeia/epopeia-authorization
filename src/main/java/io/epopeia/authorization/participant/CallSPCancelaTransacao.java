package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.BackofficeBO;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;

/**
 * The aim of this participant is call the procedure CancelaTransacao
 * in Backoffice database.
 * 
 * @author Fernando Amaral
 */
@Service
public class CallSPCancelaTransacao extends ChainHandler {

	@Autowired
	private BackofficeBO backoffice;

	@Override
	public int execute(Context context) throws Exception {
		Long origNSU = imf(context).getValueAsLong(EFields.ORIG_BACKOFFICE_NSU);
		Long origAutorizadoraNSU = imf(context).getValueAsLong(EFields.ORIG_AUTORIZADORA_NSU);
		Enum<?> situacaoOriginal = imf(context).getEnum(EFields.ORIG_STATUS_TRANSACAO, ECodes.class);

		// Somente cancelamos uma transacao original de venda que exista na base da 
		if(origAutorizadoraNSU != null && origNSU != null) {
			if(situacaoOriginal.equals(ECodes.AUTORIZADA) || situacaoOriginal.equals(ECodes.AUTORIZADA_PARCIALMENTE))
				// Somente estorna se a transacao esta aprovada na base
				backoffice.sensibilizaCancelamento(imf(context));
			else
				// Se a transacao original ja esta negada seta o motivo da negacao original na transacao corrente
				imf(context).setValue(EFields.STATUS_TRANSACAO, situacaoOriginal);
		}

		return PREPARED | NO_JOIN | READONLY;
	}
}
