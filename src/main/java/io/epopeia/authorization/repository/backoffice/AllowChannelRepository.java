package io.epopeia.authorization.repository.backoffice;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.AllowChannel;

/**
 * Repositorio para tabela RestricoesCanais e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface AllowChannelRepository extends JpaRepository<AllowChannel, Long> {

	@Query("select count(r) from AllowChannel r where r.codigoCanal = ?1 and r.validaAutorizacao = 1 ")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Long countByCodigoCanal(Long codigoCanal);

	@Query("select count(r) from AllowMerchant r where r.codigoGrupoRestricao in "
			+ "(select rc.codigoGrupoRestricao from AllowChannel rc "
			+ "where rc.codigoCanal = ?1 and rc.validaAutorizacao = 1) and "
			+ "(((r.codigoAdquirente = ?3 or r.codigoAdquirente is null) and r.mcc = ?2) or "
			+ "(r.codigoAdquirente = ?3 and r.codigoEstabelecimento in (?4)))")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Long countByCodigoCanalAndMccAndCodigoAdquirenteAndCodigoEstabelecimento(
			Long codigoCanal, String mcc, String codigoAdquirente, Collection<String> codigoEstabelecimento);

	@Query("select count(r) from AllowMerchantCnpj r where r.codigoGrupoRestricao in "
			+ "(select rc.codigoGrupoRestricao from AllowChannel rc "
			+ "where rc.codigoCanal = ?1 and rc.validaAutorizacao = 1) and r.cnpj = ?2")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Long countByCodigoCanalAndCnpj(Long codigoCanal, String cnpj);
}
