package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;

/**
 * Definicao da primary RestrictionCnpjId composta da entidade RestrictionCnpj
 * 
 * @author Fernando Amaral
 */
public class AllowMerchantCnpjId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigoGrupoRestricao;
	private String cnpj;

	public AllowMerchantCnpjId() {
	}

	public AllowMerchantCnpjId(Long codigoGrupoRestricao, String cnpj) {
		this.codigoGrupoRestricao = codigoGrupoRestricao;
		this.cnpj = cnpj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime
				* result
				+ ((codigoGrupoRestricao == null) ? 0 : codigoGrupoRestricao
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllowMerchantCnpjId other = (AllowMerchantCnpjId) obj;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
		if (codigoGrupoRestricao == null) {
			if (other.codigoGrupoRestricao != null)
				return false;
		} else if (!codigoGrupoRestricao.equals(other.codigoGrupoRestricao))
			return false;
		return true;
	}

}
