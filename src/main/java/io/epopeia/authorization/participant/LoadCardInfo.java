package io.epopeia.authorization.participant;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.bo.CardBO;
import io.epopeia.authorization.bo.HsmBO;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;

/**
 * The aim of this participant is to load the card as primary
 * information to start any other process and/or validations etc
 * 
 * @author Fernando Amaral
 */
@Service
public class LoadCardInfo extends ChainHandler {

	@Autowired
	private HsmBO hsmBO;

	@Autowired
	private CardBO cardBO;

	@Override
	public int execute(Context context) throws Exception {
		// Load card informations from database
		loadCardInfo(imf(context));

		return PREPARED | NO_JOIN | READONLY;
	}

	public void loadCardInfo(FieldSet imf) throws Exception {
		// Encrypt pan for search
		String dek = hsmBO.getKey(imf.getValue(EFields.BIN), "00B");
		if (dek == null) {
			imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_PRODUTO_NAO_DEFINIDO);
			throw new Exception("Chave para o produto nao esta definida");
		}

		String panHex = null;
		try {
			panHex = hsmBO.encryptPan(imf.getValue(EFields.PAN), dek);
		} catch (Exception e) {
			imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_COMUNICACAO_HSM);
			throw e;
		}

		// Load card info and attach it to the imf
		FieldSet fsCard = cardBO.loadCardInfo(panHex);
		imf.add(fsCard);
	}
}
