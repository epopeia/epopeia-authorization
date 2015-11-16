package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;

/**
 * Definicao da primary Key composta da entidade ChannelParameter
 * 
 * @author Fernando Amaral
 */
public class ChannelParameterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigoCanal;
	private Long codigoParametro;

	public ChannelParameterId() {

	}

	public ChannelParameterId(Long codigoCanal, Long codigoParametro) {
		super();
		this.codigoCanal = codigoCanal;
		this.codigoParametro = codigoParametro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCanal == null) ? 0 : codigoCanal.hashCode());
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
		ChannelParameterId other = (ChannelParameterId) obj;
		if (codigoCanal == null) {
			if (other.codigoCanal != null)
				return false;
		} else if (!codigoCanal.equals(other.codigoCanal))
			return false;
		if (codigoParametro == null) {
			if (other.codigoParametro != null)
				return false;
		} else if (!codigoParametro.equals(other.codigoParametro))
			return false;
		return true;
	}

}
