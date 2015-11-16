package io.epopeia.authorization.repository.backoffice;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.epopeia.authorization.domain.backoffice.Product;

/**
 * Repositorio para tabela Produtos e seus relacionamentos
 * 
 * @author Fernando Amaral
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	@EntityGraph(value = "productWithModality", type = EntityGraphType.LOAD)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	Product findByBin(String bin);
}
