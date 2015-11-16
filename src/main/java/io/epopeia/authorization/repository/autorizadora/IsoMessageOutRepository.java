package io.epopeia.authorization.repository.autorizadora;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.authorization.domain.autorizadora.IsoMessageOut;

/**
 * Repositorio para tabela MESSAGE_ISO_OUT da autorizadora.
 * 
 * @author Fernando Amaral
 */
public interface IsoMessageOutRepository extends JpaRepository<IsoMessageOut, Long> {
}
