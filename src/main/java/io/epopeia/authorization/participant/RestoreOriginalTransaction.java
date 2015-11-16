package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.AutorizadoraBO;

/**
 * The aim of this participant is load the transaction from autorizadora
 * table transacoes into the IMF fields that represents the original
 * informations.
 * 
 * @author Fernando Amaral
 */
@Service
public class RestoreOriginalTransaction extends ChainHandler {

	@Autowired
	private AutorizadoraBO autorizadoraBO;

	@Override
	public int execute(Context context) throws Exception {
		if(autorizadoraBO.getTransacaoOrigem(imf(context)) == null)
			return ABORTED;

		return PREPARED | NO_JOIN | READONLY;
	}
}
