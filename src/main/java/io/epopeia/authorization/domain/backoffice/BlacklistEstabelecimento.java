package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * Definicao da entidade BlacklistEstabelecimentos
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "BlacklistEstabelecimentos")
@IdClass(BlacklistEstabelecimentoId.class)
public class BlacklistEstabelecimento {

	@Id
	@JoinColumn(name = "CodigoAdquirente")
	private String codigoAdquirente;

	@Id
	@JoinColumn(name = "CodigoEstabelecimento")
	private String codigoEstabelecimento;

	public String getCodigoAdquirente() {
		return codigoAdquirente;
	}

	public void setCodigoAdquirente(String codigoAdquirente) {
		this.codigoAdquirente = codigoAdquirente;
	}

	public String getCodigoEstabelecimento() {
		return codigoEstabelecimento;
	}

	public void setCodigoEstabelecimento(String codigoEstabelecimento) {
		this.codigoEstabelecimento = codigoEstabelecimento;
	}

}
