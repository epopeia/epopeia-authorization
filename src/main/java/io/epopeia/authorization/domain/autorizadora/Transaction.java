package io.epopeia.authorization.domain.autorizadora;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Definicao da entidade Transacoes e os planos de execucao das querys com
 * outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "TRANSACOES")
public class Transaction {

	@Id
	@Column(name = "CODIGO_TRANSACAO")
	private Long id;

	@Column(name = "TIPO_TRANSACAO", length = 40, nullable = false)
	private String type;

	@Column(name = "CODIGO_CARTAO")
	private Long cardId;

	@Column(name = "CODIGO_PROCESSAMENTO", length = 8, nullable = false)
	private String pcode;

	@Column(name = "CODIGO_RESPOSTA", length = 2, nullable = false)
	private String respCode;

	@Column(name = "DATA", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	/* Embora armazene o valor GMT na base a coluna se chama data_hora */
	@Column(name = "DATA_HORA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateHourGMT;

	/* Embora armazene o valor local na base a coluna se chama data_hora_gmt */
	@Column(name = "DATA_HORA_GMT", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateHour;

	@Column(name = "IDENTIFICACAO_AUTORIZACAO_BACKOFFICE", length = 6, nullable = false)
	private String authorizationReferenceNumber;

	@Column(name = "IDENTIFICACAO_AUTORIZACAO_TERMINAL", length = 6)
	private String stan;

	@Column(name = "CODIGO_MOEDA", length = 3, nullable = false)
	private String currencyCode;

	@Column(name = "NUMERO_CARTAO", length = 19)
	private String cardNumber;

	@Column(name = "PARCELAS")
	private Integer installment;

	@Column(name = "SITUACAO", length = 60, nullable = false)
	private String status;

	@Column(name = "VALOR", nullable = false)
	private BigDecimal amount;

	@Column(name = "CODIGO_TRANSACAO_ORIGEM", nullable = true)
	private Long idTransactionOrigin;

	/*
	 * Esse id pode estar relacionado com a tabela de transacoes ou
	 * transacoesCancelamento no backoffice
	 */
	@Column(name = "CODIGO_TRANS_BACKOFFICE", nullable = true)
	private Long idTransactionBackoffice;

	@Column(name = "TAXA")
	private BigDecimal fee;

	@Column(name = "NSU_CAIXA", nullable = true)
	private Long nsuCaixa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateHourGMT() {
		return dateHourGMT;
	}

	public void setDateHourGMT(Date dateHourGMT) {
		this.dateHourGMT = dateHourGMT;
	}

	public Date getDateHour() {
		return dateHour;
	}

	public void setDateHour(Date dateHour) {
		this.dateHour = dateHour;
	}

	public String getAuthorizationReferenceNumber() {
		return authorizationReferenceNumber;
	}

	public void setAuthorizationReferenceNumber(
			String authorizationReferenceNumber) {
		this.authorizationReferenceNumber = authorizationReferenceNumber;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Integer getInstallment() {
		return installment;
	}

	public void setInstallment(Integer installment) {
		this.installment = installment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getIdTransactionOrigin() {
		return idTransactionOrigin;
	}

	public void setIdTransactionOrigin(Long idTransactionOrigin) {
		this.idTransactionOrigin = idTransactionOrigin;
	}

	public Long getIdTransactionBackoffice() {
		return idTransactionBackoffice;
	}

	public void setIdTransactionBackoffice(Long idTransactionBackoffice) {
		this.idTransactionBackoffice = idTransactionBackoffice;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Long getNsuCaixa() {
		return nsuCaixa;
	}

	public void setNsuCaixa(Long nsuCaixa) {
		this.nsuCaixa = nsuCaixa;
	}
}
