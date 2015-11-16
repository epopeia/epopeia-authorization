package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;

/**
 * Definicao da primary Key composta da entidade ModalidadeChaves
 * 
 * @author Fernando Amaral
 */
public class ModalityKeyId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long modalidade;
	private Long chave;

	public ModalityKeyId() {
		
	}

	public ModalityKeyId(Long modalidade, Long chave) {
		super();
		this.modalidade = modalidade;
		this.chave = chave;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chave == null) ? 0 : chave.hashCode());
		result = prime * result
				+ ((modalidade == null) ? 0 : modalidade.hashCode());
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
		ModalityKeyId other = (ModalityKeyId) obj;
		if (chave == null) {
			if (other.chave != null)
				return false;
		} else if (!chave.equals(other.chave))
			return false;
		if (modalidade == null) {
			if (other.modalidade != null)
				return false;
		} else if (!modalidade.equals(other.modalidade))
			return false;
		return true;
	}
}
