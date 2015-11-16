package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.Product;
import io.epopeia.authorization.enums.EBandeiras;

/**
 * Faker de objetos Product
 * 
 * @author Fernando Amaral
 */
public class ProductFaker {

	public enum EProduct {
		BinMastercard(1L, "111111", EBandeiras.MASTERCARD),
		BinElo(2L, "222222", EBandeiras.ELO);

		private Long codigoProduto;
		private String bin;
		private EBandeiras bandeira;

		private EProduct(Long codigoProduto, String bin, EBandeiras bandeira) {
			this.codigoProduto = codigoProduto;
			this.bin = bin;
			this.bandeira = bandeira;
		}

		public Long getCodigoProduto() {
			return codigoProduto;
		}

		public String getBin() {
			return bin;
		}

		public EBandeiras getBandeira() {
			return bandeira;
		}
	}

	static public Product getProduct() {
		return getProduct(EProduct.BinMastercard);
	}

	static public Product getProduct(EProduct pf) {
		Product p = new Product();
		p.setCodigoProduto(pf.codigoProduto);
		p.setBin(pf.bin);
		p.setCodigoBandeira(new Long(pf.bandeira.getCodigoBandeira()));
		return p;
	}
}
