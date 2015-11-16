package io.epopeia.authorization.domain.backoffice;

import java.util.Calendar;

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
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Definicao da entidade Cartoes e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="Cartoes")
@NamedEntityGraphs({
	@NamedEntityGraph(
        name = "cardWithTitularAccountAndModalityAndProductAndChannelAndSituations",
        attributeNodes = {
            @NamedAttributeNode(value = "contaTitular", subgraph = "modalityAndSituationsGraph")
          , @NamedAttributeNode(value = "canal")
          , @NamedAttributeNode(value = "situacao")
        },
        subgraphs = {
            @NamedSubgraph(
                    name = "modalityAndSituationsGraph",
                    attributeNodes = {
                        @NamedAttributeNode(value = "modalidade", subgraph = "productGraph")
                      , @NamedAttributeNode(value = "situacao")
                    }
            )
            , @NamedSubgraph(
                    name = "productGraph",
                    attributeNodes = {
                        @NamedAttributeNode("produto")
                    }
            )
        }
    )
})
public class Card {

	@Id
	@Column(name="CodigoCartao")
	private Long codigoCartao;

	@Column(name="Numero", unique=true)
	private String numero;

	@Column(name="CodigoGrupoRestricao")
	private Long codigoGrupoRestricao;

	@Column(name="Situacao")
	private Character status;

	@Column(name="DataValidade")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataValidade;

	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="CodigoCartao", insertable=false, updatable=false)
	private Channel canal;

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="CodigoContaTitular")
	private TitularAccount contaTitular;

	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="CodigoSituacao")
	private CardSituation situacao;

	public Long getCodigoCartao() {
		return codigoCartao;
	}

	public void setCodigoCartao(Long codigoCartao) {
		this.codigoCartao = codigoCartao;
	}

	public TitularAccount getContaTitular() {
		return contaTitular;
	}

	public void setContaTitular(TitularAccount contaTitular) {
		this.contaTitular = contaTitular;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Channel getCanal() {
		return canal;
	}

	public void setCanal(Channel canal) {
		this.canal = canal;
	}

	public Long getCodigoGrupoRestricao() {
		return codigoGrupoRestricao;
	}

	public void setCodigoGrupoRestricao(Long codigoGrupoRestricao) {
		this.codigoGrupoRestricao = codigoGrupoRestricao;
	}

	public CardSituation getSituacao() {
		return situacao;
	}

	public void setSituacao(CardSituation situacao) {
		this.situacao = situacao;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Calendar getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Calendar dataValidade) {
		this.dataValidade = dataValidade;
	}

}
