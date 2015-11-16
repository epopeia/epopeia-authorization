package io.epopeia.authorization.repository.backoffice;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.api.AuthorizationParameter;
import io.epopeia.authorization.domain.backoffice.ChannelParameter;

/**
 * Repositorio para tabela CanaisParametros e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface ChannelParameterRepository extends JpaRepository<ChannelParameter, Long> {

	@EntityGraph(value = "channelParameterWithParameter", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public List<AuthorizationParameter> findByCodigoCanal(Long codigoCanal);
}
