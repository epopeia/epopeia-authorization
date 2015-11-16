package io.epopeia.authorization.domain.backoffice;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Definicao da entidade Transacoes e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="Transacoes")
public class Transaction {

	@Id
	@Column(name = "CodigoTransacao")
	private Long codigoTransacao;

	@Column(name = "CodigoCartao")
	private Long codigoCartao;

	@Column(name = "CodigoTipoTransacao")
	private Long codigoTipoTransacao;

	@Column(name = "DataAutorizacao")
	private Calendar dataAutorizacao;

	@Column(name = "DataCadastro")
	private Calendar dataCadastro;

	@Column(name = "Situacao")
	private Character situacao;

	@Column(name = "CodigoSituacao")
	private Long codigoSituacao;

	@Column(name = "Valor")
	private BigDecimal valor;

	@Column(name = "Parcelas")
	private Long parcelas;

	@Column(name = "CodigoMoeda")
	private Long codigoMoeda;

	@Column(name = "DataLiquidacao")
	private Calendar dataLiquidacao;

	@Column(name = "CodigoTransacaoCancelamento")
	private Long codigoTransacaoCancelamento;

	@Column(name = "AutorizadorDE22")
	private String autorizadorDE22;

	@Column(name = "AutorizadorDE26")
	private String autorizadorDE26;

	@Column(name = "AutorizadorDE48_80")
	private String autorizadorDE48_80;

	@Column(name = "AutorizadorDE61")
	private String autorizadorDE61;

	@Column(name = "CodigoAdquirente")
	private Long codigoAdquirente;

	@Column(name = "Estabelecimento")
	private String estabelecimento;

	@Column(name = "CodigoEstabelecimento")
	private String codigoEstabelecimento;

	@Column(name = "CategoriaEstabelecimento")
	private String categoriaEstabelecimento;

	@Column(name = "ValorSensibilizado")
	private BigDecimal valorSensibilizado;

	@Column(name = "CodigoTransacaoOrigem")
	private Long codigoTransacaoOrigem;

	@Column(name = "CpfCnpj")
	private String cpfCnpj;

	public Long getCodigoTransacao() {
		return codigoTransacao;
	}

	public void setCodigoTransacao(Long codigoTransacao) {
		this.codigoTransacao = codigoTransacao;
	}

	public Long getCodigoCartao() {
		return codigoCartao;
	}

	public void setCodigoCartao(Long codigoCartao) {
		this.codigoCartao = codigoCartao;
	}

	public Long getCodigoTipoTransacao() {
		return codigoTipoTransacao;
	}

	public void setCodigoTipoTransacao(Long codigoTipoTransacao) {
		this.codigoTipoTransacao = codigoTipoTransacao;
	}

	public Calendar getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(Calendar dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public Calendar getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Calendar dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Character getSituacao() {
		return situacao;
	}

	public void setSituacao(Character situacao) {
		this.situacao = situacao;
	}

	public Long getCodigoSituacao() {
		return codigoSituacao;
	}

	public void setCodigoSituacao(Long codigoSituacao) {
		this.codigoSituacao = codigoSituacao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getParcelas() {
		return parcelas;
	}

	public void setParcelas(Long parcelas) {
		this.parcelas = parcelas;
	}

	public Long getCodigoMoeda() {
		return codigoMoeda;
	}

	public void setCodigoMoeda(Long codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	public Calendar getDataLiquidacao() {
		return dataLiquidacao;
	}

	public void setDataLiquidacao(Calendar dataLiquidacao) {
		this.dataLiquidacao = dataLiquidacao;
	}

	public Long getCodigoTransacaoCancelamento() {
		return codigoTransacaoCancelamento;
	}

	public void setCodigoTransacaoCancelamento(Long codigoTransacaoCancelamento) {
		this.codigoTransacaoCancelamento = codigoTransacaoCancelamento;
	}

	public String getAutorizadorDE22() {
		return autorizadorDE22;
	}

	public void setAutorizadorDE22(String autorizadorDE22) {
		this.autorizadorDE22 = autorizadorDE22;
	}

	public String getAutorizadorDE26() {
		return autorizadorDE26;
	}

	public void setAutorizadorDE26(String autorizadorDE26) {
		this.autorizadorDE26 = autorizadorDE26;
	}

	public String getAutorizadorDE48_80() {
		return autorizadorDE48_80;
	}

	public void setAutorizadorDE48_80(String autorizadorDE48_80) {
		this.autorizadorDE48_80 = autorizadorDE48_80;
	}

	public String getAutorizadorDE61() {
		return autorizadorDE61;
	}

	public void setAutorizadorDE61(String autorizadorDE61) {
		this.autorizadorDE61 = autorizadorDE61;
	}

	public Long getCodigoAdquirente() {
		return codigoAdquirente;
	}

	public void setCodigoAdquirente(Long codigoAdquirente) {
		this.codigoAdquirente = codigoAdquirente;
	}

	public String getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public String getCodigoEstabelecimento() {
		return codigoEstabelecimento;
	}

	public void setCodigoEstabelecimento(String codigoEstabelecimento) {
		this.codigoEstabelecimento = codigoEstabelecimento;
	}

	public String getCategoriaEstabelecimento() {
		return categoriaEstabelecimento;
	}

	public void setCategoriaEstabelecimento(String categoriaEstabelecimento) {
		this.categoriaEstabelecimento = categoriaEstabelecimento;
	}

	public BigDecimal getValorSensibilizado() {
		return valorSensibilizado;
	}

	public void setValorSensibilizado(BigDecimal valorSensibilizado) {
		this.valorSensibilizado = valorSensibilizado;
	}

	public Long getCodigoTransacaoOrigem() {
		return codigoTransacaoOrigem;
	}

	public void setCodigoTransacaoOrigem(Long codigoTransacaoOrigem) {
		this.codigoTransacaoOrigem = codigoTransacaoOrigem;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
}
