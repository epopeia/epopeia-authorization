package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.Parameter;
import io.epopeia.authorization.enums.EParams;

/**
 * Faker de objetos Parameter
 * 
 * @author Fernando Amaral
 */
public class ParameterFaker {

	public enum EParamsFaker {
		DUMMY(0L, EParams.PARAMETERS_INFO),
		VALIDAR_CHIP(1L, EParams.VALIDAR_CHIP),
		PERMITE_DEBITO(2L, EParams.PERMITE_DEBITO),
		VALOR_MINIMO_TRANSACAO(3L, EParams.VALOR_MINIMO_TRANSACAO),
		VALOR_MAXIMO_TRANSACAO(4L, EParams.VALOR_MAXIMO_TRANSACAO);

		private Long codigoParametro;
		private EParams identificador;

		private EParamsFaker(Long codigoParametro, EParams identificador) {
			this.codigoParametro = codigoParametro;
			this.identificador = identificador;
		}

		public Long getCodigoParametro() {
			return codigoParametro;
		}

		public EParams getIdentificador() {
			return identificador;
		}

	}

	static public Parameter getParameter() {
		return getParameter(EParamsFaker.DUMMY);
	}

	static public Parameter getParameter(EParamsFaker pf) {
		Parameter p = new Parameter();
		p.setCodigoParametro(pf.codigoParametro);
		p.setIdentificador(pf.identificador.name());
		return p;
	}

}
