package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;

/**
 * Definicao da primary Key composta da entidade ProductParameter
 * 
 * @author Fernando Amaral
 */
public class ProductParameterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigoProduto;
	private Long codigoParametro;

	public ProductParameterId() {

	}

	public ProductParameterId(Long codigoProduto, Long codigoParametro) {
		super();
		this.codigoProduto = codigoProduto;
		this.codigoParametro = codigoParametro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoParametro == null) ? 0 : codigoParametro.hashCode());
		result = prime * result + ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
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
		ProductParameterId other = (ProductParameterId) obj;
		if (codigoParametro == null) {
			if (other.codigoParametro != null)
				return false;
		} else if (!codigoParametro.equals(other.codigoParametro))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		return true;
	}

}
