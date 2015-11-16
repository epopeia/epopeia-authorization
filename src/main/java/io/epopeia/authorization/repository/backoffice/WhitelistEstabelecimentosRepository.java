package io.epopeia.authorization.repository.backoffice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.WhitelistEstabelecimento;

/**
 * Repositorio para tabela WhitelistEstabelecimentos
 * 
 * @author Fernando Amaral
 */
public interface WhitelistEstabelecimentosRepository extends JpaRepository<WhitelistEstabelecimento, Long> {

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	WhitelistEstabelecimento findByCodigoAdquirenteAndCodigoEstabelecimento(String codigoAdquirente, String codigoEstabelecimento);
}
