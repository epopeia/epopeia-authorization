package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Definicao da entidade RestrictionCnpj e os planos de execucao das querys com
 * outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "RestricoesCnpj")
@IdClass(AllowMerchantCnpjId.class)
public class AllowMerchantCnpj {

	@Id
	@Column(name = "CodigoGrupoRestricao")
	private Long codigoGrupoRestricao;

	@Id
	@Column(name = "cnpj")
	private String cnpj;

	public Long getCodigoGrupoRestricao() {
		return codigoGrupoRestricao;
	}

	public void setCodigoGrupoRestricao(Long codigoGrupoRestricao) {
		this.codigoGrupoRestricao = codigoGrupoRestricao;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
}
