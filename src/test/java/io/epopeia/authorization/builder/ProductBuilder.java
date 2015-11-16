package io.epopeia.authorization.builder;

import java.util.HashSet;
import java.util.Set;

import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.faker.ModalityFaker;
import io.epopeia.authorization.faker.ProductFaker;
import io.epopeia.authorization.faker.ModalityFaker.EModality;
import io.epopeia.authorization.faker.ProductFaker.EProduct;

/**
 * Builder de objetos Product e suas entidades envolvidas
 * 
 * @author Fernando Amaral
 */
public class ProductBuilder {

	private Product produto;

	public ProductBuilder() {
		this.produto = ProductFaker.getProduct();
	}

	public ProductBuilder product(EProduct eProduct) {
		this.produto = ProductFaker.getProduct(eProduct);
		return this;
	}

	public ProductBuilder withModalities(EModality... modalities) {
		Set<Modality> modalidiesOfProduct = new HashSet<Modality>();
		for (EModality modality : modalities) {
			Modality m = ModalityFaker.getModality(modality);
			m.setProduto(this.produto);
			modalidiesOfProduct.add(m);
		}
		produto.setModalidades(modalidiesOfProduct);
		return this;
	}

	public Product create() {
		return produto;
	}
}
