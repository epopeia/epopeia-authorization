package io.epopeia.authorization.repository.backoffice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.epopeia.authorization.domain.backoffice.Transaction;

/**
 * Repositorio para tabela Transacoes e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
@Repository(value="backofficeTransactionRepository")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
