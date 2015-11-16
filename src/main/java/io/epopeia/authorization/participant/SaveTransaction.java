package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.AutorizadoraBO;


/**
 * The aim of this participant is save the transaction in
 * database Autorizadora table Transacoes after parse process.
 * 
 * @author Fernando Amaral
 */
@Service
public class SaveTransaction extends ChainHandler {

	@Autowired
	private AutorizadoraBO autorizadoraBO;

	@Override
	public int execute(Context context) throws Exception {
		autorizadoraBO.criaTransacao(imf(context));

		return PREPARED | NO_JOIN | READONLY;
	}
}
