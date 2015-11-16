package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;

/**
 * Definicao da primary Key composta da entidade WhitelistEstabelecimentos
 * 
 * @author Fernando Amaral
 */
public class WhitelistEstabelecimentoId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigoAdquirente;
	private String codigoEstabelecimento;

	public WhitelistEstabelecimentoId() {

	}

	public WhitelistEstabelecimentoId(String codigoAdquirente, String codigoEstabelecimento) {
		this.codigoAdquirente = codigoAdquirente;
		this.codigoEstabelecimento = codigoEstabelecimento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoAdquirente == null) ? 0 : codigoAdquirente.hashCode());
		result = prime
				* result
				+ ((codigoEstabelecimento == null) ? 0 : codigoEstabelecimento
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
		WhitelistEstabelecimentoId other = (WhitelistEstabelecimentoId) obj;
		if (codigoAdquirente == null) {
			if (other.codigoAdquirente != null)
				return false;
		} else if (!codigoAdquirente.equals(other.codigoAdquirente))
			return false;
		if (codigoEstabelecimento == null) {
			if (other.codigoEstabelecimento != null)
				return false;
		} else if (!codigoEstabelecimento.equals(other.codigoEstabelecimento))
			return false;
		return true;
	}
	
}
