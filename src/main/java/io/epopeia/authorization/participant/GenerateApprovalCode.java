package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.AutorizadoraBO;
import io.epopeia.authorization.enums.EFields;

/**
 * The aim of this participant is generate a new approval code
 * for each transaction. The approval code is generate looking
 * for transactions at Autorizadora database and it does not
 * repeat in the same day.
 * 
 * @author Fernando Amaral
 */
@Service
public class GenerateApprovalCode extends ChainHandler {

	@Autowired
	private AutorizadoraBO autorizadoraBO;

	@Override
	public int execute(Context context) throws Exception {
		String approvalCode = autorizadoraBO.getNewApprovalCode(imf(context));
		imf(context).setValue(EFields.APPROVAL_CODE, approvalCode);

		return PREPARED | NO_JOIN | READONLY;
	}
}
