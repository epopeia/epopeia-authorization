package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Definicao da entidade Chaves e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="Chaves")
public class Key {

	@Id
	@Column(name="CodigoChave")
	private Long codigoChave;

	@Column(name="CodigoTipoChave")
	private String codigoTipoChave;

	@Column(name="Chave")
	private String chave;

	@Column(name="Ativa")
	private Character ativa;

	public Long getCodigoChave() {
		return codigoChave;
	}

	public void setCodigoChave(Long codigoChave) {
		this.codigoChave = codigoChave;
	}

	public String getCodigoTipoChave() {
		return codigoTipoChave;
	}

	public void setCodigoTipoChave(String codigoTipoChave) {
		this.codigoTipoChave = codigoTipoChave;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Character getAtiva() {
		return ativa;
	}

	public void setAtiva(Character ativa) {
		this.ativa = ativa;
	}
}
