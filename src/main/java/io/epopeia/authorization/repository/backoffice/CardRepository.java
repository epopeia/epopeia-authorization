package io.epopeia.authorization.repository.backoffice;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.Card;

/**
 * Repositorio para tabela Cartoes e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface CardRepository extends JpaRepository<Card, Long> {

	@EntityGraph(value = "cardWithTitularAccountAndModalityAndProductAndChannelAndSituations", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Card findByNumero(String numeroCriptografado);
}
