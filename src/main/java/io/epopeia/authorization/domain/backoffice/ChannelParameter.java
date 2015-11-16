package io.epopeia.authorization.domain.backoffice;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.epopeia.authorization.api.AuthorizationParameter;

/**
 * Definicao da entidade CanaisParametros
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "CanaisParametros")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "channelParameterWithParameter",
            attributeNodes = {
            @NamedAttributeNode(value = "parametro", subgraph = "systemParameterGraph")
        }
      , subgraphs = {
            @NamedSubgraph(
                name = "systemParameterGraph",
                attributeNodes = {
                    @NamedAttributeNode("parametroSistema")
                }
            )
        }
    )
})
@IdClass(ChannelParameterId.class)
public class ChannelParameter implements AuthorizationParameter {

	@Id
	@JoinColumn(name = "CodigoCanal")
	private Long codigoCanal;

	@Id
	@JoinColumn(name = "CodigoParametro")
	private Long codigoParametro;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoParametro", insertable = false, updatable = false)
	private Parameter parametro;

	@Column(name = "Valor")
	private String valor;

	@Column(name = "DataExpiracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataExpiracao;

	public Long getCodigoParametro() {
		return codigoParametro;
	}

	public void setCodigoParametro(Long codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	public Parameter getParametro() {
		return parametro;
	}

	public void setParametro(Parameter parametro) {
		this.parametro = parametro;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Calendar getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(Calendar dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public Long getCodigoCanal() {
		return codigoCanal;
	}

	public void setCodigoCanal(Long codigoCanal) {
		this.codigoCanal = codigoCanal;
	}

}
