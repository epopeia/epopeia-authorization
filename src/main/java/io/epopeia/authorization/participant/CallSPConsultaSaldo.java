package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.BackofficeBO;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.Message;

/**
 * The aim of this participant is call the procedure InsereTarifa
 * in Backoffice database for balanceInquiry transactions.
 * 
 * @author Fernando Amaral
 */
@Service
public class CallSPConsultaSaldo extends ChainHandler {

	@Autowired
	private BackofficeBO backoffice;

	@Override
	public int execute(Context context) throws Exception {
		ECodes situacao = null;
		Message fsCard = imf(context).getComponent(ECardInfo.CARD_INFO);
		Long cardId = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;
		if(cardId != null) {			
			//Call stored procedure VerificaConsultaSaldoInsereTarifa
			backoffice.verificaConsultaSaldoInsereTarifa(imf(context));
			
			//We always answer approved for balance inquiry
			situacao = ECodes.AUTORIZADA;
		} else {
			//If we can't identify the card just denied it.
			situacao = ECodes.NEGADA_NUMERO_CARTAO_INVALIDO;
		}
	
		imf(context).setValue(EFields.STATUS_TRANSACAO, situacao);

		return PREPARED | NO_JOIN | READONLY;
	}
}
