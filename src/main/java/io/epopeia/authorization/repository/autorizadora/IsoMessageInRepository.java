package io.epopeia.authorization.repository.autorizadora;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.authorization.domain.autorizadora.IsoMessageIn;

/**
 * Repositorio para tabela MESSAGE_ISO_IN da autorizadora.
 * 
 * @author Fernando Amaral
 */
public interface IsoMessageInRepository extends JpaRepository<IsoMessageIn, Long> {
}
