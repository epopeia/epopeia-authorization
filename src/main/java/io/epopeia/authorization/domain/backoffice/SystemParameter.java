package io.epopeia.authorization.domain.backoffice;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.epopeia.authorization.api.AuthorizationParameter;

/**
 * Definicao da entidade ParametrosSistemas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "ParametrosSistema")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "systemParameterWithParameter",
        attributeNodes = {
            @NamedAttributeNode("parametro")
        }
    )
})
public class SystemParameter implements AuthorizationParameter {

	@Id
	@JoinColumn(name = "CodigoParametro")
	private Long codigoParametro;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoParametro", insertable = false, updatable = false)
	private Parameter parametro;

	@Column(name = "Valor")
	private String valor;

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Parameter getParametro() {
		return parametro;
	}

	public void setParametro(Parameter parametro) {
		this.parametro = parametro;
	}

	public Long getCodigoParametro() {
		return codigoParametro;
	}

	public void setCodigoParametro(Long codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	@Override
	public Calendar getDataExpiracao() {
		return null;
	}

}
