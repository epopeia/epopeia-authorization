package io.epopeia.authorization.repository.autorizadora;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.authorization.domain.autorizadora.ErrorLog;

/**
 * Repositorio para tabela ERROR_LOG da autorizadora.
 * 
 * @author Fernando Amaral
 */
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
