package io.epopeia.authorization.repository.backoffice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.TransactionType;

/**
 * Repositorio para tabela TiposTransacoes
 * 
 * @author Fernando Amaral
 */
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	TransactionType findByIdentificador(String identificador);
}
