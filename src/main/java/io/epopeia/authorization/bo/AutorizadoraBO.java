package io.epopeia.authorization.bo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.jpos.core.ConfigurationException;
import org.jpos.core.SimpleConfiguration;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.util.LogEvent;
import org.jpos.util.ProtectedLogListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.domain.autorizadora.ErrorLog;
import io.epopeia.authorization.domain.autorizadora.IsoMessageIn;
import io.epopeia.authorization.domain.autorizadora.IsoMessageOut;
import io.epopeia.authorization.domain.autorizadora.Transaction;
import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Message;
import io.epopeia.authorization.repository.autorizadora.ErrorLogRepository;
import io.epopeia.authorization.repository.autorizadora.IsoMessageInRepository;
import io.epopeia.authorization.repository.autorizadora.IsoMessageOutRepository;
import io.epopeia.authorization.repository.autorizadora.TransactionRepository;
import io.epopeia.jpos.JPOSContext;

/**
 * Objeto de negocio que age como a ponte entre os participantes do jpos e os
 * componentes do framework especificamente no contexto da base da autorizadora,
 * onde sao gravadas as mensagem iso de entrada e saida e o log da transacao
 * parseada. Alem disso a gravacao de erros fatais e exceptions geradas sao
 * gravados na tabela de erro.
 * 
 * @author Fernando Amaral
 */
@Service
public class AutorizadoraBO {

	private static ProtectedLogListener protectedLog = new ProtectedLogListener();
	private static SimpleConfiguration conf = new SimpleConfiguration();

	private IsoMessageInRepository isoMessageInRepo;
	private IsoMessageOutRepository isoMessageOutRepo;
	private TransactionRepository transacaoRepo;
	private ErrorLogRepository errorLogRepo;

	@Autowired
	public AutorizadoraBO(IsoMessageInRepository isoMessageInRepo,
			IsoMessageOutRepository isoMessageOutRepo,
			TransactionRepository transacaoRepo, ErrorLogRepository errorLogRepo) {
		this.isoMessageInRepo = isoMessageInRepo;
		this.isoMessageOutRepo = isoMessageOutRepo;
		this.transacaoRepo = transacaoRepo;
		this.errorLogRepo = errorLogRepo;
	}

	@Value("${log.protectisofields:}")
	private String protectedIsoFields;

	@Value("${log.wipeisofields:}")
	private String wipeIsoFields;

	public String getNewApprovalCode(FieldSet imf) {

		Date gmt_date_time = imf.getValueAsDate(EFields.GMT_DATE_TIME,
				"MMDDHHmmss");

		Random random = new Random();
		Integer rndNumber = random.nextInt(999999) + 1;
		String rndString = "000000".substring(0, 6 - rndNumber.toString()
				.length())
				+ rndNumber.toString();

		Transaction transacao = null;
		transacao = transacaoRepo.findByDateAndAuthorizationReferenceNumber(
				gmt_date_time, rndString);
		if (transacao != null) {
			rndString = getNewApprovalCode(imf);
		}

		return rndString;
	}

	public void saveModelMessageOut(ISOMsg isoMessageOut, Long codigoTransacao)
			throws ISOException {
		Calendar c = Calendar.getInstance();
		IsoMessageOut modelMessageout = new IsoMessageOut();
		modelMessageout.setId(codigoTransacao);
		modelMessageout.setDate(c);
		modelMessageout.setDatetime(c);
		modelMessageout.setMessageType(isoMessageOut.getMTI());
		modelMessageout.setMessageXML(isotoXML(isoMessageOut));
		isoMessageOutRepo.save(modelMessageout);
	}

	public Long saveModelMessageIn(ISOMsg isoMsgEntrada) throws ISOException {
		Calendar c = Calendar.getInstance();
		IsoMessageIn modelMessageIn = new IsoMessageIn();
		modelMessageIn.setDate(c);
		modelMessageIn.setDatetime(c);
		modelMessageIn.setMessageType(isoMsgEntrada.getMTI());
		modelMessageIn.setMessageXML(isotoXML(isoMsgEntrada));
		isoMessageInRepo.save(modelMessageIn);
		return modelMessageIn.getId();
	}

	public void criaTransacao(FieldSet imf) {

		Transaction transacao = new Transaction();
		Date gmt_date_time = imf.getValueAsDate(EFields.GMT_DATE_TIME,
				"MMDDHHmmss");
		Long nsu = imf.getValueAsLong(EFields.AUTORIZADORA_NSU);
		String type = imf.getValue(EFields.TIPO_TRANSACAO_AUTORIZADORA);
		String pan = imf.getValue(EFields.PAN_TRUNCATED);
		String pcode = imf.getValue(EFields.PCODE);
		BigDecimal amount = imf.getValueAsBigDecimal(EFields.AMOUNT_ACQUIRER,
				100, 2, true);
		String stan = imf.getValue(EFields.STAN);
		String currency = imf.getValue(EFields.CURRENCY_CODE_ACQUIRER);
		BigDecimal fee = imf.getValueAsBigDecimal(EFields.FEE, 100, 2, true);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

		transacao.setId(nsu);
		transacao.setType(type);
		transacao.setPcode(pcode);
		transacao.setAmount(amount == null ? BigDecimal.ZERO : amount);
		transacao.setDate(gmt_date_time);
		transacao.setDateHour(calendar.getTime());
		transacao.setDateHourGMT(gmt_date_time);
		transacao.setCurrencyCode(currency);
		transacao.setAuthorizationReferenceNumber("");
		transacao.setRespCode("XX");
		transacao.setStatus("PROCESSANDO");

		if (!stan.isEmpty())
			transacao.setStan(stan);

		if (!pan.isEmpty())
			transacao.setCardNumber(pan);

		if (fee != null)
			transacao.setFee(fee);

		transacaoRepo.save(transacao);
	}

	public void updateTransacao(FieldSet imf) {
		Transaction transacao = null;
		Long autorizadoraNSUOrig = imf
				.getValueAsLong(EFields.ORIG_AUTORIZADORA_NSU);
		Long autorizadoraNSU = imf.getValueAsLong(EFields.AUTORIZADORA_NSU);
		Long backofficeNSU = imf.getValueAsLong(EFields.BACKOFFICE_NSU);
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Long cardId = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;
		String approvalCode = imf.getValue(EFields.APPROVAL_CODE);
		String respCode = imf.getValue(EFields.RESP_CODE);
		Enum<?> situacao = imf.getEnum(EFields.STATUS_TRANSACAO, ECodes.class);

		if (autorizadoraNSU != null) {

			transacao = transacaoRepo.findOne(autorizadoraNSU);

			if (transacao != null) {

				if (!respCode.isEmpty())
					transacao.setRespCode(respCode);

				if (!situacao.equals(Message.EmptyEnum.EMPTY_ENUM))
					transacao.setStatus(situacao.name());

				if (!approvalCode.isEmpty())
					transacao.setAuthorizationReferenceNumber(approvalCode);

				if (cardId != null)
					transacao.setCardId(cardId);

				if (backofficeNSU != null)
					transacao.setIdTransactionBackoffice(backofficeNSU);

				if (autorizadoraNSUOrig != null)
					transacao.setIdTransactionOrigin(autorizadoraNSUOrig);

				/**
				 * TODO: Quando uma transacao foi autorizada parcialmente
				 * estamos alterando o valor que realmente foi aprovado e
				 * atualizando na base de transacoes da autorizadora para ficar
				 * igual a base de transacoes do backoffice. O problema eh que
				 * nao existe garantias que esse update sera feito com sucesso e
				 * soh estamos fazendo aqui pq o estorno busca a transacao
				 * original primeiramente na base da autorizadora e dps no
				 * backoffice. Caso algum erro ocorra havera uma divergencia
				 * entre o valor gravado na tabela de transacoes do backoffice
				 * e da autorizadora.
				 * 
				 * A solucao para esse problema eh de longo prazo. Primeiro
				 * temos que matar a insereTransacao para ter total controle dos
				 * valores que sao inseridos na tabela de transacoes do
				 * backoffice. Dps, precisamos ajustar o fluxo de estorno para
				 * buscar a transacao original somente na base do backoffice.
				 * O ultimo passo eh parar de utilizar a base de transacoes da
				 * autorizadora, unificando as colunas na tabela de transacoes
				 * do backoffice somente, visto que eh no backoffice onde as
				 * regras de constraints etc serao feitas.
				 */
				if (situacao.equals(ECodes.AUTORIZADA_PARCIALMENTE)) {
					BigDecimal partialAmount = imf.getValueAsBigDecimal(
							EFields.AMOUNT_AUTHORIZATION, 100, 2, true);
					if (partialAmount != null)
						transacao.setAmount(partialAmount);
				}

				transacaoRepo.save(transacao);
			}
		}
	}

	public Transaction getTransacaoOrigem(FieldSet imf) {

		Transaction transacaoOriginal = null;
		BigDecimal amount = imf.getValueAsBigDecimal(EFields.AMOUNT_ACQUIRER,
				100, 2, true);
		Date dateHourGMT = imf.getValueAsDate(EFields.ORIG_GMT_DATETIME,
				"MMDDHHmmss");
		String type = imf.getValue(EFields.ORIG_TIPO_TRANSACAO_AUTORIZADORA);
		String cardNumber = imf.getValue(EFields.PAN_TRUNCATED);
		String stan = imf.getValue(EFields.ORIG_STAN);

		transacaoOriginal = transacaoRepo
				.findByCardNumberAndDateHourGMTAndAmountAndStanAndType(
						cardNumber, dateHourGMT, amount, stan, type);

		if (transacaoOriginal != null) {

			// Restore some values from original transaction in the current
			// cancelation transaction
			String origRefNum = transacaoOriginal
					.getAuthorizationReferenceNumber();
			String origSituacao = transacaoOriginal.getStatus();
			Long origAutorizadoraID = transacaoOriginal.getId();
			Long origBackofficeID = transacaoOriginal
					.getIdTransactionBackoffice();
			Long origCardID = transacaoOriginal.getCardId();
			BigDecimal fee = transacaoOriginal.getFee();

			if (!origSituacao.isEmpty())
				imf.setValue(EFields.ORIG_STATUS_TRANSACAO, origSituacao);

			if (origAutorizadoraID != null)
				imf.setValue(EFields.ORIG_AUTORIZADORA_NSU,
						origAutorizadoraID.toString());

			if (origBackofficeID != null)
				imf.setValue(EFields.ORIG_BACKOFFICE_NSU,
						origBackofficeID.toString());

			if (origCardID != null) {
				Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
				if(fsCard != null) {
					fsCard.setValue(ECardInfo.CARD_ID, origCardID.toString());
				}
			}

			if (!origRefNum.isEmpty()
					&& imf.getValue(EFields.APPROVAL_CODE).isEmpty())
				imf.setValue(EFields.APPROVAL_CODE, origRefNum);

			if (fee != null) {
				fee = fee.setScale(2);
				imf.setValue(EFields.FEE, fee.multiply(new BigDecimal(100))
						.toString());
			} else
				imf.setValue(EFields.FEE, "");
		} else {
			imf.setValue(EFields.STATUS_TRANSACAO,
					ECodes.TRANSACAO_ORIGINAL_NAO_ENCONTRADA);
		}

		return transacaoOriginal;
	}

	public void logError(String message, Exception exception, Serializable ctx) {
		FieldSet imf = JPOSContext.getInternalMessageFormat(ctx);
		ISOMsg request = JPOSContext.getRequestMessageFormat(ctx);

		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			pw.close();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(baos);
			oout.writeObject(request);
			oout.close();

			StringBuilder stack = new StringBuilder();
			stack.append("Stacktrace: ");
			stack.append(sw.getBuffer().toString());

			if (imf != null) {
				stack.append("InternalMessageFormat: ");
				stack.append(imf.dump());
			}

			ErrorLog errorLog = new ErrorLog();
			errorLog.setMessage(message);
			errorLog.setStackTrace(stack.toString());
			errorLog.setIsoMessage(baos.toByteArray());

			errorLogRepo.save(errorLog);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String isotoXML(ISOMsg isoMsg) throws ConfigurationException {
		LogEvent logEvent = new LogEvent();
		logEvent.addMessage(isoMsg);

		conf.put("protect", protectedIsoFields);
		conf.put("wipe", wipeIsoFields);

		protectedLog.setConfiguration(conf);

		return protectedLog.log(logEvent).toString();
	}

}
