package io.epopeia.authorization.repository.backoffice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.BlacklistEstabelecimento;

/**
 * Repositorio para tabela BlacklistEstabelecimento
 * 
 * @author Fernando Amaral
 */
public interface BlacklistEstabelecimentoRepository extends JpaRepository<BlacklistEstabelecimento, Long> {

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	BlacklistEstabelecimento findByCodigoAdquirenteAndCodigoEstabelecimento(String codigoAdquirente, String codigoEstabelecimento);
}
