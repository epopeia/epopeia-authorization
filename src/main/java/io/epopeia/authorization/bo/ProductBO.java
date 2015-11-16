package io.epopeia.authorization.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.repository.backoffice.ProductRepository;

/**
 * Objeto de negocio que define os BINs e bandeiras do sistema.
 * 
 * A nomenclatura utilizada na base do backoffice define BINs como sendo
 * produtos quando na verdade os produtos sao definidos pelas modalidades.
 * 
 * As responsabilidades desse servico sao:
 * 
 * - Consulta de produtos id por bin
 * - Consulta de bandeira id por bin
 * 
 * @author Fernando Amaral
 */
@Service
public class ProductBO {

	private ProductRepository produtos;

	@Autowired
	public ProductBO(ProductRepository produtos) {
		this.produtos = produtos;
	}

	@Cacheable(value = "bins", condition = "(#bin?:'').length() == 6", unless = "#result == null")
	public Long getProductId(final String bin) {
		if (bin == null || bin.length() != 6)
			return null;
		Product p = produtos.findByBin(bin);
		return p != null ? p.getCodigoProduto() : null;
	}

	@Cacheable(value = "bandeiras", condition = "(#bin?:'').length() == 6", unless = "#result == null")
	public Long getBrandId(final String bin) {
		if (bin == null || bin.length() != 6)
			return null;
		Product p = produtos.findByBin(bin);
		return p != null ? p.getCodigoBandeira() : null;
	}

}
