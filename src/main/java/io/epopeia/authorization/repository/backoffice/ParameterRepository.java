package io.epopeia.authorization.repository.backoffice;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.Parameter;

/**
 * Repositorio para tabela Parametros e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

	@Override
	@EntityGraph(value = "parameterWithSystemParameter", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Parameter findOne(Long id);

	@EntityGraph(value = "parameterWithSystemParameter", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Parameter findByIdentificador(String identificador);

	@Override
	@EntityGraph(value = "parameterWithSystemParameter", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public List<Parameter> findAll();
}
