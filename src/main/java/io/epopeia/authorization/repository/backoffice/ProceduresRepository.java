package io.epopeia.authorization.repository.backoffice;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.enums.ECardInfo;
import io.epopeia.authorization.enums.ECodes;
import io.epopeia.authorization.enums.EFields;
import io.epopeia.authorization.enums.ETxnAttributes;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.model.Message;

/**
 * Repositorio temporario para armazenar as procedures
 * enquanto ainda nao portamos as regras para o framework
 * 
 * @author Fernando Amaral
 */
@Repository
@Transactional(value="txMgrBackoffice")
public class ProceduresRepository {

	public static final Integer INTONE = new Integer(1);
	public static final Integer INTZERO = new Integer(0);
	public static final Long LONGONE = new Long(1);
	public static final Long LONGZERO = new Long(0);
	public static final String EMPTYSTRING = "";

	private EntityManager entityManager;

	public ProceduresRepository() {
	}

	@PersistenceContext(unitName="backoffice")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public BigDecimal getSaldoCartao(Long idCartao) {
		BigDecimal saldo = BigDecimal.ZERO;
		Query query = entityManager
			.createNativeQuery("SELECT COALESCE(dbo.RetornaSaldoCartao(:CodigoCartao,'PRE_PAGO'),0)");
		query.setParameter("CodigoCartao", idCartao);
		saldo = (BigDecimal) query.getSingleResult();
		saldo = saldo.setScale(2);
		return saldo;
	}

	/**
	 *
	 * Operações
	 *  "01": Retorno de codigos de seguranca para embossing
	 *  "02": Validação de informações para transacoes
	 *
	 * Retornos
	 *   "???": {CVC1_result}{CVC2_result}{iCVV_result}
	 *   Result "0": Ok
	 *   Result "1": Erro de validação
	 *   Result "2": Erro interno no uso do HSM
	 *
	 */
	public String cvv2(FieldSet imf) {

		/* Parametros da proc */
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Long codigoCartao = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;
		String operacao = "02";
		String cvc1Recebido = imf.getValue(EFields.CVC);
		String cvc2Recebido = imf.getValue(EFields.CVC2);
		String iCvvRecebido = imf.getValue(EFields.ICVV);
		String retorno = null;

		StoredProcedureQuery storedProcedure = null;
		storedProcedure = entityManager.createStoredProcedureQuery("CVV2");

		storedProcedure.registerStoredProcedureParameter("CodigoCartao", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("Operacao", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("CVC1Recebido", String.class, ParameterMode.INOUT);
		storedProcedure.registerStoredProcedureParameter("CVC2Recebido", String.class, ParameterMode.INOUT);
		storedProcedure.registerStoredProcedureParameter("iCVVRecebido", String.class, ParameterMode.INOUT);
		storedProcedure.registerStoredProcedureParameter("Retorno", String.class, ParameterMode.OUT);

		storedProcedure.setParameter("CodigoCartao", codigoCartao == null ? LONGZERO : codigoCartao);
		storedProcedure.setParameter("Operacao", operacao);
		storedProcedure.setParameter("CVC1Recebido", cvc1Recebido);
		storedProcedure.setParameter("CVC2Recebido", cvc2Recebido);
		storedProcedure.setParameter("iCVVRecebido", iCvvRecebido);

		storedProcedure.execute();

		retorno = (String)storedProcedure.getOutputParameterValue("Retorno");
		return retorno;
	}

	public void verificaPINBlockCriptografado(FieldSet imf) {

		/* Parametros da proc */
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Long codigoCartao = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;
		String pinBlock = imf.getValue(EFields.PIN_BLOCK);

		/* Retornos da proc */
		Integer codigoSituacaoTransacao = null;
		String situacaoTransacao = null;
		String identificadorSituacaoTransacao = null;

		StoredProcedureQuery storedProcedure = null;
		storedProcedure = entityManager.createStoredProcedureQuery("VerificaPINBlockCrptografado");

		storedProcedure.registerStoredProcedureParameter("CodigoCartao", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("PINBlockCriptografadoInformado", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("PINBlockValido", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SituacaoTransacao", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("IdentificadorSituacaoTransacao", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("CodigoSituacaoTransacao", Integer.class, ParameterMode.OUT);

		storedProcedure.setParameter("CodigoCartao", codigoCartao == null ? LONGZERO : codigoCartao);
		storedProcedure.setParameter("PINBlockCriptografadoInformado", pinBlock);

		storedProcedure.execute();

		situacaoTransacao = (String)storedProcedure.getOutputParameterValue("SituacaoTransacao");
		identificadorSituacaoTransacao = (String)storedProcedure.getOutputParameterValue("IdentificadorSituacaoTransacao");
		codigoSituacaoTransacao = (Integer)storedProcedure.getOutputParameterValue("CodigoSituacaoTransacao");

		if (identificadorSituacaoTransacao != null && !identificadorSituacaoTransacao.isEmpty() &&
			situacaoTransacao != null && !situacaoTransacao.isEmpty() &&
			codigoSituacaoTransacao != null)
		    imf.setValue(EFields.PIN_BLOCK_SITUATION, identificadorSituacaoTransacao);
		else
			imf.setValue(EFields.PIN_BLOCK_SITUATION, ECodes.NEGADA_ERRO_DE_PROCESSAMENTO);
	}

	public void cancelaTransacao(FieldSet imf) {
		Long codigoTransacaoRetorno = null;
		String identificadorSituacaoTransacao = null;

		Long codigoTransacaoOriginal = imf.getValueAsLong(EFields.ORIG_BACKOFFICE_NSU);

		StoredProcedureQuery storedProcedure = null;

		storedProcedure = entityManager.createStoredProcedureQuery("CancelaTransacao");

		storedProcedure.registerStoredProcedureParameter("CodigoTransacaoParaCancelamento", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("IdentificadorSituacaoTransacaoParaCancelamento", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("DataAutorizacao", Date.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("CodigoTransacaoCancelamento", Long.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SituacaoTransacaoCancelamento", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("IdentificadorSituacaoTransacaoCancelamento", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("MotivoCancelamento", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("UsuarioCancelamento", String.class, ParameterMode.IN);

		storedProcedure.setParameter("CodigoTransacaoParaCancelamento", codigoTransacaoOriginal);
		storedProcedure.setParameter("IdentificadorSituacaoTransacaoParaCancelamento", ECodes.CANCELADA.name());
		storedProcedure.setParameter("DataAutorizacao", new java.sql.Timestamp((new Date()).getTime()));
		storedProcedure.setParameter("MotivoCancelamento", "");
		storedProcedure.setParameter("UsuarioCancelamento", "");

		storedProcedure.execute();

		codigoTransacaoRetorno = (Long)storedProcedure.getOutputParameterValue("CodigoTransacaoCancelamento");
		identificadorSituacaoTransacao = (String)storedProcedure.getOutputParameterValue("IdentificadorSituacaoTransacaoCancelamento");

		if(codigoTransacaoRetorno != null)
			imf.setValue(EFields.BACKOFFICE_NSU, codigoTransacaoRetorno.toString());

	    imf.setValue(EFields.STATUS_TRANSACAO, identificadorSituacaoTransacao);
	}

	public void insereTransacao(FieldSet imf) {
		StoredProcedureQuery storedProcedure1 = null;
		StoredProcedureQuery storedProcedure2 = null;

		/* Retornos da proc */
		Long codigoTransacao = null;
		Integer codigoMoeda = null;
		Integer codigoSituacaoTransacao = null;
		String situacaoTransacao = null;
		String identificadorSituacaoTransacao = null;

		/* Parametros da proc */
		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Long codigoCartao = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;
		String tipoTransacaoBackoffice = imf.getValue(EFields.TIPO_TRANSACAO_BACKOFFICE);
		BigDecimal valorAutorizacao = imf.getValueAsBigDecimal(EFields.AMOUNT_AUTHORIZATION, 100, 2, true);
		Integer codigoMoedaAutorizacao = imf.getValueAsInteger(EFields.CURRENCY_NUMBER_AUTHORIZATION);
		BigDecimal valorOriginalTransacao = imf.getValueAsBigDecimal(EFields.AMOUNT_ACQUIRER, 100, 2, true);
		BigDecimal taxaConversao = imf.getValueAsBigDecimal(EFields.CONVERSION_RATE);
		Integer codigoMoedaOriginalTransacao = imf.getValueAsInteger(EFields.CURRENCY_NUMBER_ACQUIRER);
		String cardAcceptorNameLocal = imf.getValue(EFields.CARD_ACCEPTOR_NAME_LOCAL);
		String codAdquirente = imf.getValue(EFields.ACQUIRER_INSTITUTION);
		String pan = imf.getValue(EFields.PAN);
		String cvc = imf.getValue(EFields.CVC);
		String cvc2 = imf.getValue(EFields.CVC2);
		String icvv = imf.getValue(EFields.ICVV);
		String mcc = imf.getValue(EFields.MCC);
		String de22 = imf.getValue(EFields.POS_ENTRY_CODE);
		String de26 = EMPTYSTRING; //campo especifico da master. Nao deveria estar na tabela de transacoes
		String pinBlock = imf.getValue(EFields.PIN_BLOCK);
		String merchantId = imf.getValue(EFields.MERCHANT_ID);
		String de48_80 = EMPTYSTRING; //campo especifico da master. Nao deveria estar na tabela de transacoes
		String de61 = EMPTYSTRING; //campo especifico da master. Nao deveria estar na tabela de transacoes
		String cpfCnpj = imf.getValue(EFields.CPF_CNPJ);
		Long codigoGrupoRestricao = fsCard != null ? fsCard.getValueAsLong(ECardInfo.GROUP_RESTRICTION_ID) : null;
		Integer codigoBandeira = fsCard != null ? fsCard.getValueAsInteger(ECardInfo.BRAND_ID) : null;
		Long codigoCanal = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_CHANNEL_ID) : null;
		Message fsTxnAttr = imf.getComponent(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		Integer codigoTipoTransacao = fsTxnAttr != null ? fsTxnAttr.getValueAsInteger(ETxnAttributes.TIPO_TRANSACAO_ID) : null;

		storedProcedure1 = entityManager.createStoredProcedureQuery("InsereTransacaoLite3");

		storedProcedure1.registerStoredProcedureParameter("IdentificadorTipoTransacao", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoCartao", Long.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("NumeroCartaoAberto", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("DataAutorizacao", Date.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CVV_CVCInformado", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CVV2_CVC2Informado", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("iCVV_CVCChipInformado", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("SenhaCartaoInformada", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("PINBlockCriptografadoInformado", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("ValorTransacao", BigDecimal.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoMoeda", Integer.class, ParameterMode.INOUT);
		storedProcedure1.registerStoredProcedureParameter("ValorOriginalTransacao", BigDecimal.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoMoedaOriginal", Integer.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("TaxaConversaoMoeda", BigDecimal.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("Parcelas", Integer.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("TaxaTransacao", BigDecimal.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("AutorizadorDE22", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("AutorizadorDE26", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("AutorizadorDE48_80", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("AutorizadorDE61", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoAdquirente", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("Estabelecimento", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoEstabelecimento", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CategoriaEstabelecimento", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CpfCnpj", String.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoGrupoRestricao", Long.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoBandeira", Integer.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoCanal", Long.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoTipoTransacao", Integer.class, ParameterMode.IN);
		storedProcedure1.registerStoredProcedureParameter("CodigoSituacaoTransacao", Integer.class, ParameterMode.OUT);
		storedProcedure1.registerStoredProcedureParameter("CodigoTransacao", Long.class, ParameterMode.OUT);
		storedProcedure1.registerStoredProcedureParameter("SituacaoTransacao", String.class, ParameterMode.OUT);
		storedProcedure1.registerStoredProcedureParameter("IdentificadorSituacaoTransacao", String.class, ParameterMode.OUT);

		storedProcedure1.setParameter("IdentificadorTipoTransacao", tipoTransacaoBackoffice);
		storedProcedure1.setParameter("CodigoCartao", codigoCartao == null ? LONGZERO : codigoCartao);
		storedProcedure1.setParameter("NumeroCartaoAberto", pan);
		storedProcedure1.setParameter("ValorTransacao", valorAutorizacao == null ? BigDecimal.ZERO : valorAutorizacao);
		storedProcedure1.setParameter("CodigoMoeda", codigoMoedaAutorizacao == null ? INTZERO : codigoMoedaAutorizacao);
		storedProcedure1.setParameter("ValorOriginalTransacao", valorOriginalTransacao == null ? BigDecimal.ZERO : valorOriginalTransacao);
		storedProcedure1.setParameter("CodigoMoedaOriginal", codigoMoedaOriginalTransacao == null ? INTZERO : codigoMoedaOriginalTransacao);
		storedProcedure1.setParameter("TaxaConversaoMoeda", taxaConversao == null ? BigDecimal.ZERO : taxaConversao);
		storedProcedure1.setParameter("Parcelas", INTONE);
		storedProcedure1.setParameter("TaxaTransacao", BigDecimal.ZERO); /* nao usado pela proc */
		storedProcedure1.setParameter("DataAutorizacao", new java.sql.Timestamp((new Date()).getTime()));
		storedProcedure1.setParameter("CVV_CVCInformado", cvc);
		storedProcedure1.setParameter("CVV2_CVC2Informado", cvc2);
		storedProcedure1.setParameter("iCVV_CVCChipInformado", icvv);
		storedProcedure1.setParameter("SenhaCartaoInformada", ""); /* nao usado pela proc */
		storedProcedure1.setParameter("PINBlockCriptografadoInformado", pinBlock);
		storedProcedure1.setParameter("CodigoAdquirente", codAdquirente);
		storedProcedure1.setParameter("Estabelecimento", cardAcceptorNameLocal);
		storedProcedure1.setParameter("CodigoEstabelecimento", merchantId);
		storedProcedure1.setParameter("CategoriaEstabelecimento", mcc);
		storedProcedure1.setParameter("AutorizadorDE22", de22);
		storedProcedure1.setParameter("AutorizadorDE26", de26);
		storedProcedure1.setParameter("AutorizadorDE48_80", de48_80);
		storedProcedure1.setParameter("AutorizadorDE61", de61);
		storedProcedure1.setParameter("CpfCnpj", cpfCnpj);
		storedProcedure1.setParameter("CodigoGrupoRestricao", codigoGrupoRestricao == null ? LONGZERO : codigoGrupoRestricao);
		storedProcedure1.setParameter("CodigoBandeira", codigoBandeira == null ? INTZERO : codigoBandeira);
		storedProcedure1.setParameter("CodigoCanal", codigoCanal == null ? LONGZERO : codigoCanal);
		storedProcedure1.setParameter("CodigoTipoTransacao", codigoTipoTransacao == null ? INTZERO : codigoTipoTransacao);
		storedProcedure1.execute();

		codigoMoeda = (Integer)storedProcedure1.getOutputParameterValue("CodigoMoeda");
		codigoSituacaoTransacao = (Integer)storedProcedure1.getOutputParameterValue("CodigoSituacaoTransacao");
		codigoTransacao = (Long)storedProcedure1.getOutputParameterValue("CodigoTransacao");
		situacaoTransacao = (String)storedProcedure1.getOutputParameterValue("SituacaoTransacao");
		identificadorSituacaoTransacao = (String)storedProcedure1.getOutputParameterValue("IdentificadorSituacaoTransacao");

		storedProcedure2 = entityManager.createStoredProcedureQuery("InsereTransacao_Parte2");

		storedProcedure2.registerStoredProcedureParameter("IdentificadorTipoTransacao", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CodigoCartao", Long.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("NumeroCartaoAberto", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("DataAutorizacao", Date.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CVV_CVCInformado", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CVV2_CVC2Informado", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("iCVV_CVCChipInformado", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("SenhaCartaoInformada", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("PINBlockCriptografadoInformado", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("ValorTransacao", BigDecimal.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CodigoMoeda", Integer.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("ValorOriginalTransacao", BigDecimal.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CodigoMoedaOriginal", Integer.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("TaxaConversaoMoeda", BigDecimal.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("Parcelas", Integer.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("TaxaTransacao", BigDecimal.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("AutorizadorDE22", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("AutorizadorDE26", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("AutorizadorDE48_80", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("AutorizadorDE61", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CodigoAdquirente", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("Estabelecimento", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CodigoEstabelecimento", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CategoriaEstabelecimento", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CpfCnpj", String.class, ParameterMode.IN);
		storedProcedure2.registerStoredProcedureParameter("CodigoSituacaoTransacao", Integer.class, ParameterMode.INOUT);
		storedProcedure2.registerStoredProcedureParameter("CodigoTransacao", Long.class, ParameterMode.INOUT);
		storedProcedure2.registerStoredProcedureParameter("SituacaoTransacao", String.class, ParameterMode.INOUT);
		storedProcedure2.registerStoredProcedureParameter("IdentificadorSituacaoTransacao", String.class, ParameterMode.INOUT);

		storedProcedure2.setParameter("IdentificadorTipoTransacao", tipoTransacaoBackoffice);
		storedProcedure2.setParameter("CodigoCartao", codigoCartao == null ? LONGZERO : codigoCartao);
		storedProcedure2.setParameter("NumeroCartaoAberto", pan);
		storedProcedure2.setParameter("ValorTransacao", valorAutorizacao == null ? BigDecimal.ZERO : valorAutorizacao);
		storedProcedure2.setParameter("CodigoMoeda", codigoMoeda == null ? INTZERO : codigoMoeda);
		storedProcedure2.setParameter("ValorOriginalTransacao", valorOriginalTransacao == null ? BigDecimal.ZERO : valorOriginalTransacao);
		storedProcedure2.setParameter("CodigoMoedaOriginal", codigoMoedaOriginalTransacao == null ? INTZERO : codigoMoedaOriginalTransacao);
		storedProcedure2.setParameter("TaxaConversaoMoeda", taxaConversao == null ? BigDecimal.ZERO : taxaConversao);
		storedProcedure2.setParameter("Parcelas", INTONE);
		storedProcedure2.setParameter("TaxaTransacao", BigDecimal.ZERO); /* nao usado pela proc */
		storedProcedure2.setParameter("DataAutorizacao", new java.sql.Timestamp((new Date()).getTime()));
		storedProcedure2.setParameter("CVV_CVCInformado", cvc);
		storedProcedure2.setParameter("CVV2_CVC2Informado", cvc2);
		storedProcedure2.setParameter("iCVV_CVCChipInformado", icvv);
		storedProcedure2.setParameter("SenhaCartaoInformada", ""); /* nao usado pela proc */
		storedProcedure2.setParameter("PINBlockCriptografadoInformado", pinBlock);
		storedProcedure2.setParameter("CodigoAdquirente", codAdquirente);
		storedProcedure2.setParameter("Estabelecimento", cardAcceptorNameLocal);
		storedProcedure2.setParameter("CodigoEstabelecimento", merchantId);
		storedProcedure2.setParameter("CategoriaEstabelecimento", mcc);
		storedProcedure2.setParameter("AutorizadorDE22", de22);
		storedProcedure2.setParameter("AutorizadorDE26", de26);
		storedProcedure2.setParameter("AutorizadorDE48_80", de48_80);
		storedProcedure2.setParameter("AutorizadorDE61", de61);
		storedProcedure2.setParameter("CpfCnpj", cpfCnpj);
		storedProcedure2.setParameter("CodigoSituacaoTransacao", codigoSituacaoTransacao);
		storedProcedure2.setParameter("CodigoTransacao", codigoTransacao);
		storedProcedure2.setParameter("SituacaoTransacao", situacaoTransacao);
		storedProcedure2.setParameter("IdentificadorSituacaoTransacao", identificadorSituacaoTransacao);			

		storedProcedure2.execute();

		codigoTransacao = (Long)storedProcedure2.getOutputParameterValue("CodigoTransacao");
		identificadorSituacaoTransacao = (String)storedProcedure2.getOutputParameterValue("IdentificadorSituacaoTransacao");

		if(codigoTransacao != null)
			imf.setValue(EFields.BACKOFFICE_NSU, codigoTransacao.toString());

	    imf.setValue(EFields.STATUS_TRANSACAO, identificadorSituacaoTransacao);
	}

	public void verificaConsultaSaldoInsereTarifa(FieldSet imf) {

		StoredProcedureQuery storedProcedure = null;

		/* Retornos da proc */
		BigDecimal saldoCartao = null;
		Long codigoTransacao = null;
		String situacaoTransacao = null;
		String identificadorSituacaoTransacao = null;

		Message fsCard = imf.getComponent(ECardInfo.CARD_INFO);
		Long codigoCartao = fsCard != null ? fsCard.getValueAsLong(ECardInfo.CARD_ID) : null;
		String cvc = imf.getValue(EFields.CVC);
		String cvc2 = imf.getValue(EFields.CVC2);
		String icvv = imf.getValue(EFields.ICVV);
		String pinBlock = imf.getValue(EFields.PIN_BLOCK);
		
		storedProcedure = entityManager.createStoredProcedureQuery("VerificaConsultaSaldoInsereTarifa");
						
		storedProcedure.registerStoredProcedureParameter("CodigoCartao", Long.class, ParameterMode.IN);			
		storedProcedure.registerStoredProcedureParameter("CVV_CVCInformado", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("CVV2_CVC2Informado", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("iCVV_CVCChipInformado", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("PINBlockCriptografadoInformado", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("SaldoCartao", BigDecimal.class, ParameterMode.INOUT);			
		storedProcedure.registerStoredProcedureParameter("CodigoTransacao", Long.class, ParameterMode.INOUT);
		storedProcedure.registerStoredProcedureParameter("SituacaoTransacao", String.class, ParameterMode.INOUT);
		storedProcedure.registerStoredProcedureParameter("IdentificadorSituacaoTransacao", String.class, ParameterMode.INOUT);
		
		storedProcedure.setParameter("CodigoCartao", codigoCartao == null ? LONGZERO : codigoCartao);	
		storedProcedure.setParameter("CVV_CVCInformado", cvc);
		storedProcedure.setParameter("CVV2_CVC2Informado", cvc2);
		storedProcedure.setParameter("iCVV_CVCChipInformado", icvv);
		storedProcedure.setParameter("PINBlockCriptografadoInformado", pinBlock);		
		storedProcedure.setParameter("SaldoCartao", saldoCartao);										
		storedProcedure.setParameter("CodigoTransacao", codigoTransacao);
		storedProcedure.setParameter("SituacaoTransacao", situacaoTransacao);
		storedProcedure.setParameter("IdentificadorSituacaoTransacao", identificadorSituacaoTransacao);	
		
		storedProcedure.execute();

		saldoCartao = (BigDecimal)storedProcedure.getOutputParameterValue("SaldoCartao");
		codigoTransacao = (Long)storedProcedure.getOutputParameterValue("CodigoTransacao");
		identificadorSituacaoTransacao = (String)storedProcedure.getOutputParameterValue("IdentificadorSituacaoTransacao");

		if(saldoCartao != null) {
			saldoCartao = saldoCartao.setScale(2);
			DecimalFormat zeropadded = new DecimalFormat("000000000000");
			if(fsCard != null) {
				fsCard.setValue(ECardInfo.CARD_BALANCE, zeropadded.format(saldoCartao.movePointRight(2)));
			}
		}

		if(codigoTransacao != null)
			imf.setValue(EFields.BACKOFFICE_NSU, codigoTransacao.toString());

	    imf.setValue(EFields.STATUS_TRANSACAO, identificadorSituacaoTransacao);
	}

}
