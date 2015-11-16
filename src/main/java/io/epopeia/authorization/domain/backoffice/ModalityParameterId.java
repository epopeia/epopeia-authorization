package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;

/**
 * Definicao da primary Key composta da entidade ModalityParameter
 * 
 * @author Fernando Amaral
 */
public class ModalityParameterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigoModalidade;
	private Long codigoParametro;

	public ModalityParameterId() {

	}

	public ModalityParameterId(Long codigoModalidade, Long codigoParametro) {
		super();
		this.codigoModalidade = codigoModalidade;
		this.codigoParametro = codigoParametro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoModalidade == null) ? 0 : codigoModalidade.hashCode());
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
		ModalityParameterId other = (ModalityParameterId) obj;
		if (codigoModalidade == null) {
			if (other.codigoModalidade != null)
				return false;
		} else if (!codigoModalidade.equals(other.codigoModalidade))
			return false;
		if (codigoParametro == null) {
			if (other.codigoParametro != null)
				return false;
		} else if (!codigoParametro.equals(other.codigoParametro))
			return false;
		return true;
	}

}
