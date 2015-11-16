package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.Modality;
import io.epopeia.authorization.enums.EFuncaoCartao;

/**
 * Faker de objetos Modality
 * 
 * @author Fernando Amaral
 */
public class ModalityFaker {

	public enum EModality {
		ModalidadeUm(1L, EFuncaoCartao.CREDITO),
		ModalidadeDois(2L, EFuncaoCartao.DEBITO),
		ModalidadeTres(1L, EFuncaoCartao.MULTIPLO),
		ModalidadeQuatro(2L, EFuncaoCartao.MULTIPLO);

		private Long codigoModalidade;
		private EFuncaoCartao funcaoCartao;

		private EModality(Long codigoModalidade, EFuncaoCartao funcaoCartao) {
			this.codigoModalidade = codigoModalidade;
			this.funcaoCartao = funcaoCartao;
		}

		public Long getCodigoModalidade() {
			return codigoModalidade;
		}

		public EFuncaoCartao getFuncaoCartao() {
			return funcaoCartao;
		}
	}

	static public Modality getModality() {
		return getModality(EModality.ModalidadeUm);
	}

	static public Modality getModality(EModality mf) {
		Modality m = new Modality();
		m.setCodigoModalidade(mf.codigoModalidade);
		m.setCodigoFuncaoCartao(new Long(mf.funcaoCartao.getFuncaoCartao()));
		return m;
	}
}
