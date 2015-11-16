package io.epopeia.authorization.api;

import java.util.Currency;

import org.jpos.iso.ISOMsg;

import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;

/**
 * Interface a ser implementada pelos gateways para parsear as mensagens
 * externas das bandeiras do formato especifico para um formato comum
 * que sera utilizado pelo framework. Alem do parseamento tambem serao
 * atribuidos a mensagem labels para identificar naturezas de tipo de terminal,
 * capacidades de leitura, modalidades da captura etc
 * 
 * @author Fernando Amaral
 */
public abstract class RequestParser {

	public abstract void parseRequestMsg(final ISOMsg xmf, FieldSet imf) throws Exception;
	public abstract void assignMessageLabels(final ISOMsg xmf, FieldSet imf) throws Exception;

	protected void parseDefaultISO8583DataElements(final ISOMsg xmf, FieldSet imf) throws Exception {
		imf.setValue(EFields.MSG_TYPE, xmf.getMTI());

		if (xmf.hasField(2)) {
			String pan = xmf.getString(2);
			if(pan != null && pan.length() >= 16) {
				imf.setValue(EFields.PAN, pan, true);
				imf.setValue(EFields.BIN, pan.substring(0, 6));
				imf.setValue(EFields.PAN_TRUNCATED, pan.substring(0, 6) + "******" + pan.substring(12));
			}
		}

		imf.setValue(EFields.PCODE, xmf.getString(3));
		imf.setValue(EFields.AMOUNT_ACQUIRER, xmf.getString(4));
		imf.setValue(EFields.AMOUNT_SETTLEMENT, xmf.getString(5));
		imf.setValue(EFields.AMOUNT_CARDHOLDER, xmf.getString(6));

		if (xmf.hasField(7)) {
			String timestamp = xmf.getString(7);
			if(timestamp != null && timestamp.length() >= 10) {
				imf.setValue(EFields.GMT_DATE_TIME, timestamp);
				imf.setValue(EFields.GMT_DATE, timestamp.substring(0,4));
				imf.setValue(EFields.GMT_TIME, timestamp.substring(4));
			}
		}

		imf.setValue(EFields.STAN, xmf.getString(11));
		imf.setValue(EFields.LOCAL_TIME, xmf.getString(12));
		imf.setValue(EFields.LOCAL_DATE, xmf.getString(13));
		imf.setValue(EFields.EXPIRY_DATE, xmf.getString(14));
		imf.setValue(EFields.MCC, xmf.getString(18));
		imf.setValue(EFields.ACQUIRER_INSTITUTION, xmf.getString(32));
		imf.setValue(EFields.FORWARDING_INSTITUTION, xmf.getString(33));
		imf.setValue(EFields.ACQUIRER_REF_NUMBER, xmf.getString(37));
		imf.setValue(EFields.APPROVAL_CODE, xmf.getString(38));
		imf.setValue(EFields.RESP_CODE, xmf.getString(39));
		imf.setValue(EFields.TERMINAL_ID, xmf.getString(41));
		imf.setValue(EFields.MERCHANT_ID, xmf.getString(42));
		imf.setValue(EFields.CARD_ACCEPTOR_NAME_LOCAL, xmf.getString(43));
		imf.setValue(EFields.ADD_DATA_RESPONSE, xmf.getString(44));
		imf.setValue(EFields.PIN_BLOCK, xmf.getString(52), true);

		if(xmf.hasField(35)) {
			String trilha2 = xmf.getString(35);
			int startPosition = trilha2.startsWith("%") ? 1 : 0;
			int length = trilha2.length();

			imf.setValue(EFields.TRACK2_FULL, trilha2, true);

			if(length >= (16 + startPosition)) {
				String panTrilha = trilha2.substring(0 + startPosition, 16 + startPosition);
				imf.setValue(EFields.PAN, panTrilha, true);
				imf.setValue(EFields.BIN, panTrilha.substring(0, 6));
				imf.setValue(EFields.PAN_TRUNCATED, panTrilha.substring(0, 6) + "******" + panTrilha.substring(12));
				if(length >= (21 + startPosition)) {
					imf.setValue(EFields.EXPIRY_DATE, trilha2.substring(17 + startPosition, 21 + startPosition));
					if(length >= (24 + startPosition)) {
						imf.setValue(EFields.SERVICE_CODE, trilha2.substring(21 + startPosition, 24 + startPosition));
						if(length >= (32 + startPosition))
							imf.setValue(EFields.CVC, trilha2.substring(29 + startPosition, 32 + startPosition), true);
					}
				}
			}

		} else if(xmf.hasField(45)) {
			String trilha1 = xmf.getString(45);
			int startPosition = trilha1.startsWith("%") ? 1 : 0;
			int firstSeparador = trilha1.indexOf('^');
			int lastSeparador = trilha1.lastIndexOf('^');

			imf.setValue(EFields.TRACK1_FULL, trilha1, true);

			/* Parse do Pan mesmo sem considerar a presenca dos 2 separadores pois a trilha pode estar incompleta */
			if(firstSeparador >= (17 + startPosition)) {
				String panTrilha = trilha1.substring(1 + startPosition, firstSeparador);
				imf.setValue(EFields.PAN, panTrilha, true);
				imf.setValue(EFields.BIN, panTrilha.substring(0, 6));
				imf.setValue(EFields.PAN_TRUNCATED, panTrilha.substring(0, 6) + "******" + panTrilha.substring(12));
			}

			/* Garantir que haja 2 separadores */
			if(lastSeparador > firstSeparador) {
				/* Parse dos outros campos dps do separador */
				int length = trilha1.substring(lastSeparador).length();
				if(length >= 5) {
					imf.setValue(EFields.EXPIRY_DATE, trilha1.substring(lastSeparador + 1, lastSeparador + 5));
					if(length >= 8) {
						imf.setValue(EFields.SERVICE_CODE, trilha1.substring(lastSeparador + 5, lastSeparador + 8));
						if(length >= 18)
							imf.setValue(EFields.CVC, trilha1.substring(lastSeparador + 15, lastSeparador + 18), true);
					}
				}
			}
		}

		if(xmf.hasField(49)) {
			imf.setValue(EFields.CURRENCY_NUMBER_ACQUIRER, xmf.getString(49));
			imf.setValue(EFields.CURRENCY_CODE_ACQUIRER, getCurrencyByNumber(xmf.getString(49)));
		}

		if(xmf.hasField(50)) {
			imf.setValue(EFields.CURRENCY_NUMBER_SETTLEMENT, xmf.getString(50));
			imf.setValue(EFields.CURRENCY_CODE_SETTLEMENT, getCurrencyByNumber(xmf.getString(50)));
		}

		if(xmf.hasField(51)) {
			imf.setValue(EFields.CURRENCY_NUMBER_CARDHOLDER, xmf.getString(51));
			imf.setValue(EFields.CURRENCY_CODE_CARDHOLDER, getCurrencyByNumber(xmf.getString(51)));
		}
	}

	protected String getCurrencyByNumber(String currencyID) {
		Integer currencyNumber = new Integer(currencyID);
		for (Currency cur : Currency.getAvailableCurrencies()) {
			if (currencyNumber == cur.getNumericCode()) {
				return cur.getCurrencyCode();
			}
		}
		return null;
	}
}
