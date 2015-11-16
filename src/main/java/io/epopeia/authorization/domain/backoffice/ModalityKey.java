package io.epopeia.authorization.domain.backoffice;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;

/**
 * Definicao da entidade ModalidadeChaves e os planos de execucao
 * das querys com outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name="ModalidadesChaves")
@NamedEntityGraphs({
	@NamedEntityGraph(
        name = "modalityKeysWithModalityAndKeys",
        attributeNodes = {
        	@NamedAttributeNode(value = "modalidade", subgraph = "modalityKeysGraph")
            , @NamedAttributeNode(value = "chave", subgraph = "modalityKeysGraph")
        }
    )
})
@IdClass(ModalityKeyId.class)
public class ModalityKey {

	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CodigoModalidade")
	private Modality modalidade;

	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CodigoChave")
	private Key chave;

	public Modality getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modality modalidade) {
		this.modalidade = modalidade;
	}

	public Key getChave() {
		return chave;
	}

	public void setChave(Key chave) {
		this.chave = chave;
	}

}
