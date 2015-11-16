package io.epopeia.authorization.domain.backoffice;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Definicao da entidade Produtos e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="Produtos")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "productWithModality",
        attributeNodes = {
            @NamedAttributeNode("modalidades")
        }
    )
})
public class Product {

	@Id
	@Column(name="CodigoProduto")
	private Long codigoProduto;

	@Column(name="iin")
	private String bin;

	@Column(name="CodigoBandeira")
	private Long codigoBandeira;

	@OneToMany(mappedBy="produto", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Modality> modalidades;

	@OneToMany(mappedBy="produto", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ProductParameter> produtosParametros;

	public Long getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Long codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public Long getCodigoBandeira() {
		return codigoBandeira;
	}

	public void setCodigoBandeira(Long codigoBandeira) {
		this.codigoBandeira = codigoBandeira;
	}

	public Set<Modality> getModalidades() {
		return modalidades;
	}

	public void setModalidades(Set<Modality> modalidades) {
		this.modalidades = modalidades;
	}

	public Set<ProductParameter> getProdutosParametros() {
		return produtosParametros;
	}

	public void setProdutosParametros(Set<ProductParameter> produtosParametros) {
		this.produtosParametros = produtosParametros;
	}

}
