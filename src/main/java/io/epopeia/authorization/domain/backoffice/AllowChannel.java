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
@Table(name = "RestricoesCanais")
public class AllowChannel {

	@Id
	@Column(name = "CodigoRestricaoCanal")
	private Long codigoRestricaoCanal;

	@Column(name = "CodigoCanal")
	private Long codigoCanal;

	@Column(name = "CodigoGrupoRestricao")
	private Long codigoGrupoRestricao;

	@Column(name = "ValidaAutorizacao")
	private Integer validaAutorizacao;

	public Long getCodigoRestricaoCanal() {
		return codigoRestricaoCanal;
	}

	public void setCodigoRestricaoCanal(Long codigoRestricaoCanal) {
		this.codigoRestricaoCanal = codigoRestricaoCanal;
	}

	public Long getCodigoCanal() {
		return codigoCanal;
	}

	public void setCodigoCanal(Long codigoCanal) {
		this.codigoCanal = codigoCanal;
	}

	public Long getCodigoGrupoRestricao() {
		return codigoGrupoRestricao;
	}

	public void setCodigoGrupoRestricao(Long codigoGrupoRestricao) {
		this.codigoGrupoRestricao = codigoGrupoRestricao;
	}

	public Integer getValidaAutorizacao() {
		return validaAutorizacao;
	}

	public void setValidaAutorizacao(Integer validaAutorizacao) {
		this.validaAutorizacao = validaAutorizacao;
	}

}
