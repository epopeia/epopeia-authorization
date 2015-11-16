package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Definicao da entidade SituacoesContasTitulares
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "SituacoesContasTitulares")
public class AccountSituation {

	@Id
	@Column(name = "CodigoSituacao")
	private Long codigoSituacao;

	@Column(name = "Identificador")
	private String identificador;

	public Long getCodigoSituacao() {
		return codigoSituacao;
	}

	public void setCodigoSituacao(Long codigoSituacao) {
		this.codigoSituacao = codigoSituacao;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
}
