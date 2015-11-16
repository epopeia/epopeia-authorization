package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;

/**
 * Definicao da primary Key composta da entidade TitularAccountParameter
 * 
 * @author Fernando Amaral
 */
public class TitularAccountParameterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigoContaTitular;
	private Long codigoParametro;

	public TitularAccountParameterId() {

	}

	public TitularAccountParameterId(Long codigoContaTitular, Long codigoParametro) {
		super();
		this.codigoContaTitular = codigoContaTitular;
		this.codigoParametro = codigoParametro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoContaTitular == null) ? 0 : codigoContaTitular.hashCode());
		result = prime * result
				+ ((codigoParametro == null) ? 0 : codigoParametro.hashCode());
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
		TitularAccountParameterId other = (TitularAccountParameterId) obj;
		if (codigoContaTitular == null) {
			if (other.codigoContaTitular != null)
				return false;
		} else if (!codigoContaTitular.equals(other.codigoContaTitular))
			return false;
		if (codigoParametro == null) {
			if (other.codigoParametro != null)
				return false;
		} else if (!codigoParametro.equals(other.codigoParametro))
			return false;
		return true;
	}

}
