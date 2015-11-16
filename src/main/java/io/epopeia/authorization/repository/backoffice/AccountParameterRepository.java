package io.epopeia.authorization.repository.backoffice;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.api.AuthorizationParameter;
import io.epopeia.authorization.domain.backoffice.TitularAccountParameter;

/**
 * Repositorio para tabela ContasTitularesParametros e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface AccountParameterRepository extends JpaRepository<TitularAccountParameter, Long> {

	@EntityGraph(value = "accountParameterWithParameter", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public List<AuthorizationParameter> findByCodigoContaTitular(Long codigoContaTitular);
}
