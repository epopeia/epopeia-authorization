package io.epopeia.authorization.participant;

import java.io.Serializable;
import java.util.Map;

import org.jpos.transaction.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.api.ChainHandler;
import io.epopeia.authorization.api.EmvService;
import io.epopeia.authorization.beans.ParameterSearch;
import io.epopeia.authorization.bo.BlacklistBO;
import io.epopeia.authorization.bo.ChipBO;
import io.epopeia.authorization.bo.HsmBO;
import io.epopeia.authorization.bo.RestrictionBO;
import io.epopeia.authorization.bo.TransactionTypeBO;
import io.epopeia.authorization.bo.WhitelistBO;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ELabels;
import io.epopeia.authorization.enums.EParams;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Labels;
import io.epopeia.authorization.model.Message;
import io.epopeia.jpos.JPOSContext;

/**
 * The aim of this participant is query remote entities as database, HSM, EMV
 * service or whatever externals information sources, looking for related
 * informations before to apply the business rules in the message. It should be
 * designed to load card, account, parameters, modalities and other related
 * infos as well validate the pan, pin, cryptogram etc
 * 
 * @author Fernando Amaral
 */
@Service
public class QueryRemoteEntities extends ChainHandler {

	@Autowired
	private ParameterSearch parametros;

	@Autowired
	private ChipBO chipBO;

	@Autowired
	private HsmBO hsmBO;

	@Autowired
	private WhitelistBO whitelistBO;

	@Autowired
	private BlacklistBO blacklistBO;

	@Autowired
	private RestrictionBO restrictionBO;

	@Autowired
	private TransactionTypeBO tipoTransacaoBO;

	@Autowired(required = false)
	private EmvService emvService;

	@Override
	public int execute(Context context) throws Exception {
		// Load related data from database
		loadTransactionAttributes(imf(context));
		loadParameters(imf(context));

		// validate security codes
		validateSecurityCodes(imf(context));

		// validate pin
		validatePinBlock(imf(context));

		// Validate the cryptogram (if has any in message) by external EMV service call
		if (transactionAcquiredAsChip(context) && !validateCryptogram(imf(context), context))
			return ABORTED;

		// check group restrictions
		checkGroupRestrictions(imf(context));
		checkBlackList(imf(context));

		return PREPARED | NO_JOIN | READONLY;
	}

	private void checkBlackList(FieldSet imf) {
		String codigoEstabelecimento = imf.getValue(EFields.MERCHANT_ID);
		String codigoAdquirente = imf.getValue(EFields.ACQUIRER_INSTITUTION);
		if (!codigoAdquirente.isEmpty() && !codigoEstabelecimento.isEmpty()) {
			boolean isInBlackList = blacklistBO.checkRestriction(codigoAdquirente, codigoEstabelecimento);
			if (isInBlackList == true) {
				Labels tags = imf.getLabels(EFields.TAGS);
				tags.add(ELabels.MERCHANT_IN_BLACKLIST);
			}
		}
	}

	private void checkGroupRestrictions(FieldSet imf) {
		restrictionBO.check(imf);
	}

	private void validatePinBlock(FieldSet imf) {
		String pinBlock = imf.getValue(EFields.PIN_BLOCK);
		if (pinBlock != null && !pinBlock.isEmpty()) {
			hsmBO.validatePinBlock(imf);
		}
	}

	private void loadWhiteListPermissions(FieldSet imf) {
		String codAdquirente = imf.getValue(EFields.ACQUIRER_INSTITUTION).trim();
		String codEstabelecimento = imf.getValue(EFields.MERCHANT_ID).trim();
		Labels labels = imf.getLabels(EFields.TAGS);

		// Assign a label field if does not exists
		if (labels == null) {
			labels = new Labels();
			imf.setLabels(EFields.TAGS, labels);
		}

		// Lookup permissions for this merchant
		if (codAdquirente != null && codAdquirente.length() > 0 && codEstabelecimento != null
				&& codEstabelecimento.length() > 0) {
			Map<String, String> permissions = whitelistBO.getPermissions(codAdquirente, codEstabelecimento);
			if (permissions != null) {
				// assign label for allow transactions without cvc2
				String allowEmptyCVC2 = permissions.get(ELabels.WHITELIST_ALLOW_EMPTY_CVC2.name());
				if (allowEmptyCVC2 != null && allowEmptyCVC2.equals("S"))
					labels.add(ELabels.WHITELIST_ALLOW_EMPTY_CVC2);
			}
		}
	}

	private void validateSecurityCodes(FieldSet imf) throws Exception {
		String cvc = imf.getValue(EFields.CVC);
		String cvc2 = imf.getValue(EFields.CVC2);
		String icvv = imf.getValue(EFields.ICVV);
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Long cardId = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;

		// only call BO if the fields are not empty, otherwise try
		// to load permissions in white list to skip cvc2 validation
		// when processing traps of business rules
		if (cardId != null && (!cvc.isEmpty() || !cvc2.isEmpty() || !icvv.isEmpty())) {
			try {
				hsmBO.validateSecurityCodes(imf);
			} catch (Exception e) {
				imf.setValue(EFields.STATUS_TRANSACAO, ECodes.NEGADA_VALIDACAO_CODIGO_SEGURANCA);
				throw e;
			}
		} else {
			loadWhiteListPermissions(imf);
		}
	}

	private void loadTransactionAttributes(FieldSet imf) {
		// Load transaction attributes and attach it to the imf
		String identificador = imf.getValue(EFields.TIPO_TRANSACAO_BACKOFFICE);
		FieldSet fsTxnAttr = tipoTransacaoBO.getTransactionAtributes(identificador);
		imf.add(fsTxnAttr);
	}

	private boolean transactionAcquiredAsChip(final Context context) {
		Labels tags = imf(context).getLabels(EFields.TAGS);
		if (tags.contains(ELabels.CHIP) && tags.contains(ELabels.CHIP_DATA))
			return true;
		return false;
	}

	private void loadParameters(FieldSet imf) {
		FieldSet params = new FieldSet(EParams.PARAMETERS_INFO);
		Message cardInfo = imf.getComponent(ECardInfo.CARD_INFO);

		/* Assign the reference in IMF */
		if (cardInfo != null) {
			cardInfo.add(params);
		}

		params.setValue(EParams.VALIDAR_CHIP, parametros.getParameterValue(imf, EParams.VALIDAR_CHIP.name(), "N"));
		params.setValue(EParams.PERMITE_DEBITO, parametros.getParameterValue(imf, EParams.PERMITE_DEBITO.name(), "N"));
	}

	private boolean validateCryptogram(FieldSet imf, Serializable ctx) throws Exception {
		try {
			String validaCriptograma = emvService.getCryptogramCommand(imf);

			JPOSContext.registerTimeMillisEmvServiceSend(ctx);
			String retFromEmvService = chipBO.emvExtServiceSendCommand(validaCriptograma);
			if (emvService.validateResponseFromEmvExtService(retFromEmvService, imf))
				return true;

			return false;
		} catch (Exception e) {
			throw new Exception("Erro durante validacao do criptograma pela servico EMV", e);
		} finally {
			JPOSContext.registerTimeMillisEmvServiceReceive(ctx);
		}
	}
}
