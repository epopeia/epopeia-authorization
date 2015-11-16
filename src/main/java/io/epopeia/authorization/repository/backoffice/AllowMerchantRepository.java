package io.epopeia.authorization.repository.backoffice;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.AllowMerchant;

/**
 * Repositorio para tabela Restricoes e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface AllowMerchantRepository extends JpaRepository<AllowMerchant, Long> {

	@Query("select count(r) from AllowMerchant r where "
			+ "(r.codigoGrupoRestricao = ?1 and (r.codigoAdquirente = ?3 or r.codigoAdquirente is null) and r.mcc = ?2) or "
			+ "(r.codigoGrupoRestricao = ?1 and r.codigoAdquirente = ?3 and r.codigoEstabelecimento in (?4))")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Long countByCodigoGrupoRestricaoAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
			Long codigoGrupoRestricao, String mcc, String codigoAdquirente,
			Collection<String> codigoEstabelecimento);
}
