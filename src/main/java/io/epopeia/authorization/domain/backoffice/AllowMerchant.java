package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Definicao da entidade Restricoes e os planos de execucao das querys com
 * outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "Restricoes")
public class AllowMerchant {

	@Id
	@Column(name = "Restricao")
	private Long codigoRestricao;

	@Column(name = "CodigoGrupoRestricao")
	private Long codigoGrupoRestricao;

	@Column(name = "mcc")
	private String mcc;

	@Column(name = "CodigoAdquirente")
	private String codigoAdquirente;

	@Column(name = "CodigoEstabelecimento")
	private String codigoEstabelecimento;

	public Long getCodigoRestricao() {
		return codigoRestricao;
	}

	public void setCodigoRestricao(Long codigoRestricao) {
		this.codigoRestricao = codigoRestricao;
	}

	public Long getCodigoGrupoRestricao() {
		return codigoGrupoRestricao;
	}

	public void setCodigoGrupoRestricao(Long codigoGrupoRestricao) {
		this.codigoGrupoRestricao = codigoGrupoRestricao;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

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

}
