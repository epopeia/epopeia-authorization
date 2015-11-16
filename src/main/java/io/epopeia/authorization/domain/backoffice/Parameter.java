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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Definicao da entidade Parametros
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "Parametros")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "parameterWithSystemParameter",
        attributeNodes = {
            @NamedAttributeNode("parametroSistema")
        }
    )
  , @NamedEntityGraph(
        name = "parameterWithProductParameter",
        attributeNodes = {
            @NamedAttributeNode("parametrosProduto")
        }
    )
  , @NamedEntityGraph(
        name = "parameterWithModalityParameter",
        attributeNodes = {
            @NamedAttributeNode("parametrosModalidade")
        }
    )
  , @NamedEntityGraph(
        name = "parameterWithChannelParameter",
        attributeNodes = {
            @NamedAttributeNode("parametrosCanais")
        }
    )
  , @NamedEntityGraph(
        name = "parameterWithAccountParameter",
        attributeNodes = {
            @NamedAttributeNode("parametrosContaTitular")
        }
    )
})
public class Parameter {

	@Id
	@Column(name = "CodigoParametro")
	private Long codigoParametro;

	@Column(name = "Identificador")
	private String identificador;

	@OneToOne(mappedBy = "parametro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private SystemParameter parametroSistema;

	@OneToMany(mappedBy = "parametro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ProductParameter> parametrosProduto;

	@OneToMany(mappedBy = "parametro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ModalityParameter> parametrosModalidade;

	@OneToMany(mappedBy = "parametro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<TitularAccountParameter> parametrosContaTitular;

	@OneToMany(mappedBy = "parametro", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ChannelParameter> parametrosCanais;

	public Long getCodigoParametro() {
		return codigoParametro;
	}

	public void setCodigoParametro(Long codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public SystemParameter getParametroSistema() {
		return parametroSistema;
	}

	public void setParametroSistema(SystemParameter parametroSistema) {
		this.parametroSistema = parametroSistema;
	}

	public Set<ProductParameter> getParametrosProduto() {
		return parametrosProduto;
	}

	public void setParametrosProduto(Set<ProductParameter> parametrosProduto) {
		this.parametrosProduto = parametrosProduto;
	}

	public Set<ModalityParameter> getParametrosModalidade() {
		return parametrosModalidade;
	}

	public void setParametrosModalidade(Set<ModalityParameter> parametrosModalidade) {
		this.parametrosModalidade = parametrosModalidade;
	}

	public Set<TitularAccountParameter> getParametrosContaTitular() {
		return parametrosContaTitular;
	}

	public void setParametrosContaTitular(
			Set<TitularAccountParameter> parametrosContaTitular) {
		this.parametrosContaTitular = parametrosContaTitular;
	}

	public Set<ChannelParameter> getParametrosCanais() {
		return parametrosCanais;
	}

	public void setParametrosCanais(Set<ChannelParameter> parametrosCanais) {
		this.parametrosCanais = parametrosCanais;
	}

}
