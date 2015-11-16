package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Definicao da entidade TiposTransacoes
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="TiposTransacoes")
public class TransactionType {

	@Id
	@Column(name = "CodigoTipoTransacao")
	private Long codigoTipoTransacao;

	@Column(name = "Identificador")
	private String identificador;

	@Column(name = "VerificarContaTitularAtiva")
	private Character verificarContaTitularAtiva;

	@Column(name = "VerificarCartaoAtivo")
	private Character verificarCartaoAtivo;

	@Column(name = "VerificarValidadeCartao")
	private Character verificarValidadeCartao;

	@Column(name = "LiquidacaoImediata")
	private Character liquidacaoImediata;
	
	@Column(name = "FatorSensibilizacao")
	private Long fatorSensibilizacao;

	public Long getCodigoTipoTransacao() {
		return codigoTipoTransacao;
	}

	public void setCodigoTipoTransacao(Long codigoTipoTransacao) {
		this.codigoTipoTransacao = codigoTipoTransacao;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Character getVerificarContaTitularAtiva() {
		return verificarContaTitularAtiva;
	}

	public void setVerificarContaTitularAtiva(
			Character verificarContaTitularAtiva) {
		this.verificarContaTitularAtiva = verificarContaTitularAtiva;
	}

	public Character getVerificarCartaoAtivo() {
		return verificarCartaoAtivo;
	}

	public void setVerificarCartaoAtivo(Character verificarCartaoAtivo) {
		this.verificarCartaoAtivo = verificarCartaoAtivo;
	}

	public Character getVerificarValidadeCartao() {
		return verificarValidadeCartao;
	}

	public void setVerificarValidadeCartao(Character verificarValidadeCartao) {
		this.verificarValidadeCartao = verificarValidadeCartao;
	}

	public Long getFatorSensibilizacao() {
		return fatorSensibilizacao;
	}

	public void setFatorSensibilizacao(Long fatorSensibilizacao) {
		this.fatorSensibilizacao = fatorSensibilizacao;
	}

	public Character getLiquidacaoImediata() {
		return liquidacaoImediata;
	}

	public void setLiquidacaoImediata(Character liquidacaoImediata) {
		this.liquidacaoImediata = liquidacaoImediata;
	}
}
