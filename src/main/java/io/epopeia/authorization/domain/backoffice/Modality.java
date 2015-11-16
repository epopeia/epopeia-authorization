package io.epopeia.authorization.domain.backoffice;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Definicao da entidade Modalidades e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="Modalidades")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "modalityWithProduct",
        attributeNodes = {
            @NamedAttributeNode("produto")
        }
    )
})
public class Modality {

	@Id
	@Column(name="CodigoModalidade")
	private Long codigoModalidade;

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="CodigoProduto")
	private Product produto;

	@Column(name="CodigoFuncaoCartao")
	private Long codigoFuncaoCartao;

	@OneToMany(mappedBy="modalidade", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ModalityParameter> modalidadesParametros;

	public Long getCodigoModalidade() {
		return codigoModalidade;
	}

	public void setCodigoModalidade(Long codigoModalidade) {
		this.codigoModalidade = codigoModalidade;
	}

	public Product getProduto() {
		return produto;
	}

	public void setProduto(Product produto) {
		this.produto = produto;
	}

	public Long getCodigoFuncaoCartao() {
		return codigoFuncaoCartao;
	}

	public void setCodigoFuncaoCartao(Long codigoFuncaoCartao) {
		this.codigoFuncaoCartao = codigoFuncaoCartao;
	}

	public Set<ModalityParameter> getModalidadesParametros() {
		return modalidadesParametros;
	}

	public void setModalidadesParametros(
			Set<ModalityParameter> modalidadesParametros) {
		this.modalidadesParametros = modalidadesParametros;
	}

}
