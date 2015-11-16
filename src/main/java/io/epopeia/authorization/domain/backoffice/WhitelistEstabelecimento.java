package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * Definicao da entidade WhitelistEstabelecimentos
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "WhitelistEstabelecimentos")
@IdClass(WhitelistEstabelecimentoId.class)
public class WhitelistEstabelecimento {

	@Id
	@JoinColumn(name = "CodigoAdquirente")
	private String codigoAdquirente;

	@Id
	@JoinColumn(name = "CodigoEstabelecimento")
	private String codigoEstabelecimento;

	@Column(name = "CVC2ausente")
	private Character cvc2ausente;

	@Column(name = "Estabelecimento")
	private String estabelecimento;

	public String getCodigoAdquirente() {
		return codigoAdquirente;
	}

	public void setCodigoAdquirente(String codigoAdquirente) {
		this.codigoAdquirente = codigoAdquirente;
	}

	public String getCodigoEstabelecimento() {
		return codigoEstabelecimento;
	}

	public void setCodigoEstabelecimento(String codigoEstabelecimento) {
		this.codigoEstabelecimento = codigoEstabelecimento;
	}

	public Character getCvc2ausente() {
		return cvc2ausente;
	}

	public void setCvc2ausente(Character cvc2ausente) {
		this.cvc2ausente = cvc2ausente;
	}

	public String getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

}
