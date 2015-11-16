package io.epopeia.authorization.repository.backoffice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.AllowMerchantCnpj;

/**
 * Repositorio para tabela RestricoesCnpj e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface AllowMerchantCnpjRepository extends JpaRepository<AllowMerchantCnpj, Long> {

	@Query("select count(r) from AllowMerchantCnpj r where r.codigoGrupoRestricao = ?1 and r.cnpj = ?2")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Long countByCodigoGrupoRestricaoAndCnpj(Long codigoGrupoRestricao, String cnpj);
}
