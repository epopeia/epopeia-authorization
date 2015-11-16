package io.epopeia.authorization.repository.backoffice;

import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.ModalityKey;

/**
 * Repositorio para tabela ModalidadeChaves e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface ModalityKeysRepository extends JpaRepository<ModalityKey, Long> {

	@EntityGraph(value = "modalityKeysWithModalityAndKeys", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Set<ModalityKey> findByModalidade(Modality modality);
}
