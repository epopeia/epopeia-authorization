package io.epopeia.authorization.builder;

import java.util.HashSet;
import java.util.Set;

import io.epopeia.authorization.domain.backoffice.Key;
import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.ModalityKey;
import io.epopeia.authorization.faker.KeysFaker;
import io.epopeia.authorization.faker.ModalityFaker;
import io.epopeia.authorization.faker.KeysFaker.EKeys;
import io.epopeia.authorization.faker.ModalityFaker.EModality;

/**
 * Builder de objetos ModalidadesChaves e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
public class ModalityKeysBuilder {

	private Set<Modality> modalidades;
	private Set<Key> chaves;

	public ModalityKeysBuilder() {
	}

	public ModalityKeysBuilder modalities(EModality... modalities) {
		this.modalidades = new HashSet<Modality>();
		for (EModality modality : modalities) {
			Modality m = ModalityFaker.getModality(modality);
			this.modalidades.add(m);
		}
		return this;
	}

	public ModalityKeysBuilder withKeys(EKeys... keys) {
		this.chaves = new HashSet<Key>();
		for (EKeys key : keys) {
			Key k = KeysFaker.getKey(key);
			this.chaves.add(k);
		}
		return this;
	}

	public Set<ModalityKey> create() {
		Set<ModalityKey> modalidadesChaves = new HashSet<ModalityKey>();
		for(Modality modalidade : this.modalidades) {
			for(Key chave : this.chaves) {
				ModalityKey mk = new ModalityKey();
				mk.setModalidade(modalidade);
				mk.setChave(chave);
				modalidadesChaves.add(mk);
			}
		}
		return modalidadesChaves;
	}
}
