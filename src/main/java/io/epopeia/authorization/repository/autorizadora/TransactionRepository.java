package io.epopeia.authorization.repository.autorizadora;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.epopeia.authorization.domain.autorizadora.Transaction;

/**
 * Repositorio para tabela TRANSACOES da autorizadora. Aqui tb sao definidos 
 * os metodos de busca de transacoes para situacoes de cancelamentos etc
 * 
 * @author Fernando Amaral
 */
@Repository(value="autorizadoraTransactionRepository")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Transaction findByCardNumberAndDateHourGMTAndAmountAndStanAndType(String cardNumber, Date dateHourGMT, BigDecimal amount, String stan, String type);
	Transaction findByDateAndAuthorizationReferenceNumber(Date date, String authorizationReferenceNumber);
}
