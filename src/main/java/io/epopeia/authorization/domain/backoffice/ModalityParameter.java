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

import io.epopeia.authorization.api.AuthorizationParameter;

/**
 * Definicao da entidade ModalidadesParametros
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "ModalidadesParametros")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "modalityParameterWithParameter",
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
@IdClass(ModalityParameterId.class)
public class ModalityParameter implements AuthorizationParameter {

	@Id
	@JoinColumn(name = "CodigoModalidade")
	private Long codigoModalidade;

	@Id
	@JoinColumn(name = "CodigoParametro")
	private Long codigoParametro;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoModalidade", insertable = false, updatable = false)
	private Modality modalidade;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoParametro", insertable = false, updatable = false)
	private Parameter parametro;

	@Column(name = "Valor")
	private String valor;

	public Long getCodigoModalidade() {
		return codigoModalidade;
	}

	public void setCodigoModalidade(Long codigoModalidade) {
		this.codigoModalidade = codigoModalidade;
	}

	public Long getCodigoParametro() {
		return codigoParametro;
	}

	public void setCodigoParametro(Long codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	public Modality getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modality modalidade) {
		this.modalidade = modalidade;
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

	@Override
	public Calendar getDataExpiracao() {
		return null;
	}

}
