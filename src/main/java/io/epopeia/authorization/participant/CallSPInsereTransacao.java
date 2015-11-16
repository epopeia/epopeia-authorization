package io.epopeia.authorization.participant;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.BackofficeBO;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.enums.ETipoTransacao;
import io.epopeia.authorization.model.Message;

/**
 * The aim of this participant is call the procedure InsereTransacao
 * in Backoffice database. Also here we validate the partial authorization
 * rules until move the procedure to framework.
 * 
 * @author Fernando Amaral
 */
@Service
public class CallSPInsereTransacao extends ChainHandler {

	@Autowired
	private BackofficeBO backoffice;

	@Override
	public int execute(Context context) throws Exception {

		/**
		 * Regra para autorizações parciais. Projeto Cielo.
		 * Uma autorização que permite aprovação parcial de saldo contém
		 * a primeira posição do DE48.61 igual a 1. Zero ou nulo não permite.
		 * Consulto o saldo do cartão e se não for suficiente para o valor
		 * da compra, utilizo o saldo como valor da transação para zerar. 
		 */
		long acquirerCode = imf(context).getValueAsLong(EFields.ACQUIRER_INSTITUTION);
		boolean allow_partial_approval = imf(context).getLabels(EFields.TAGS).contains(ELabels.PARTIAL_AUTH_ALLOWED);
		Enum<?> tipoTransacao = imf(context).getEnum(EFields.TIPO_TRANSACAO_BACKOFFICE, ETipoTransacao.class);
		boolean processaAutorizacaoParcial = false;
		
		if( allow_partial_approval && tipoTransacao.equals(ETipoTransacao.COMPRA_CREDITO_A_VISTA) &&
			(acquirerCode == 12088 || acquirerCode == 12123 || acquirerCode == 6142) ) {

			BigDecimal minReference = new BigDecimal("0.01");
			BigDecimal valorAutorizacao = imf(context).getValueAsBigDecimal(EFields.AMOUNT_AUTHORIZATION, 100, 2, true);
			Message fsCard = imf(context).getComponent(ECardInfo.CARD_INFO);
			Long cardId = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;

			if(cardId != null) {
				BigDecimal balance = backoffice.getSaldoCartao(cardId);
				
				if(balance.compareTo(minReference) >= 0 && balance.compareTo(valorAutorizacao) < 0) {
					//Cartao tem saldo porem nao o suficiente para o valor total da transação.
					//Nesse cenario tentamos aprovar a transacao parcialmente.
					DecimalFormat zeropadded = new DecimalFormat("000000000000");
					imf(context).setValue(EFields.AMOUNT_AUTHORIZATION, zeropadded.format(balance.movePointRight(2)));
					processaAutorizacaoParcial = true;
				}
			}
		}

		//Call stored procedure InsereTransacao
		backoffice.insereTransacaoBackoffice(imf(context));

		/**
		 * Como a insere transacao so devolve aprovada remapeamos para parcial se a transacoes for parcial.
		 * Esse remapeamento sera removido quando matarmos a insere transacao e portarmos para ca.
		 */
		Enum<?> statusRet = imf(context).getEnum(EFields.STATUS_TRANSACAO, ECodes.class);
		if(processaAutorizacaoParcial == true && statusRet.equals(ECodes.AUTORIZADA))
			imf(context).setValue(EFields.STATUS_TRANSACAO, ECodes.AUTORIZADA_PARCIALMENTE);

		return PREPARED | NO_JOIN | READONLY;
	}
}
