package io.epopeia.authorization.domain.backoffice;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Definicao da entidade Contas e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="ContasTitulares")
public class TitularAccount {

	@Id
	@Column(name="CodigoContaTitular")
	private Long codigoContaTitular;

	@Column(name="Situacao")
	private Character status;

	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="CodigoModalidade")
	private Modality modalidade;

	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="CodigoSituacao")
	private AccountSituation situacao;

	@OneToMany(mappedBy="contaTitular", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<TitularAccountParameter> contasTitularesParametros;

	public Long getCodigoContaTitular() {
		return codigoContaTitular;
	}

	public void setCodigoContaTitular(Long codigoContaTitular) {
		this.codigoContaTitular = codigoContaTitular;
	}

	public Modality getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modality modalidade) {
		this.modalidade = modalidade;
	}

	public AccountSituation getSituacao() {
		return situacao;
	}

	public void setSituacao(AccountSituation situacao) {
		this.situacao = situacao;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Set<TitularAccountParameter> getContasTitularesParametros() {
		return contasTitularesParametros;
	}

	public void setContasTitularesParametros(
			Set<TitularAccountParameter> contasTitularesParametros) {
		this.contasTitularesParametros = contasTitularesParametros;
	}
}
