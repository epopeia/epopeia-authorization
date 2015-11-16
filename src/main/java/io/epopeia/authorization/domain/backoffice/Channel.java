package io.epopeia.authorization.domain.backoffice;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Definicao da entidade Channel
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "Canais")
public class Channel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@JoinColumn(name = "CodigoCartao")
	private Long codigoCartao;

	@Column(name = "CodigoCanal")
	private Long codigoCanal;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoCartao", insertable = false, updatable = false)
	private Card cartao;

	// overwrite default behavior of jpa to join a different column instead the primary key CodigoCartao
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoCanal", referencedColumnName = "CodigoCanal", insertable = false, updatable = false)
	private Set<ChannelParameter> canaisParametros;

	public Long getCodigoCartao() {
		return codigoCartao;
	}

	public void setCodigoCartao(Long codigoCartao) {
		this.codigoCartao = codigoCartao;
	}

	public Long getCodigoCanal() {
		return codigoCanal;
	}

	public void setCodigoCanal(Long codigoCanal) {
		this.codigoCanal = codigoCanal;
	}

	public Set<ChannelParameter> getCanaisParametros() {
		return canaisParametros;
	}

	public void setCanaisParametros(Set<ChannelParameter> canaisParametros) {
		this.canaisParametros = canaisParametros;
	}

	public Card getCartao() {
		return cartao;
	}

	public void setCartao(Card cartao) {
		this.cartao = cartao;
	}
}
