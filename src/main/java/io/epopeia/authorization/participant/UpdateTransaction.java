package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandlerRequired;
import io.epopeia.authorization.bo.AutorizadoraBO;

/**
 * The aim of this participant is to update fields returned by
 * backoffice after processing procedure InsereTransacao.
 * 
 * @author Fernando Amaral
 */
@Service
public class UpdateTransaction extends ChainHandlerRequired {

	@Autowired
	private AutorizadoraBO autorizadoraBO;

	@Override
	public int execute(Context context) throws Exception {
		autorizadoraBO.updateTransacao(imf(context));
		return PREPARED | NO_JOIN | READONLY;
	}
}
