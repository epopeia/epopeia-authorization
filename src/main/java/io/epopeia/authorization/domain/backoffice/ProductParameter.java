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
 * Definicao da entidade ProdutosParametros
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "ProdutosParametros")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "productParameterWithParameter",
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
@IdClass(ProductParameterId.class)
public class ProductParameter implements AuthorizationParameter {

	@Id
	@JoinColumn(name = "CodigoProduto")
	private Long codigoProduto;

	@Id
	@JoinColumn(name = "CodigoParametro")
	private Long codigoParametro;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoProduto", insertable = false, updatable = false)
	private Product produto;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CodigoParametro", insertable = false, updatable = false)
	private Parameter parametro;

	@Column(name = "Valor")
	private String valor;

	public Long getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Long codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Long getCodigoParametro() {
		return codigoParametro;
	}

	public void setCodigoParametro(Long codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	public Product getProduto() {
		return produto;
	}

	public void setProduto(Product produto) {
		this.produto = produto;
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
