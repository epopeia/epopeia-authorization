package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.AccountSituation;

/**
 * Faker de objetos AccountSituation
 * 
 * @author Fernando Amaral
 */
public class AccountSituationFaker {

	public enum EAccountSituations {
		ATIVA_SITUACAO_INICIAL(1L,'A'),
		BLOQUEADA_SITUACAO_INICIAL(2L,'B'),
		CANCELADA_A_PEDIDO_DO_CLIENTE(3L,'C'),
		CANCELADA_POR_INATIVIDADE(4L,'C'),
		BLOQUEADA_CONTA_EM_COBRANCA(5L,'B'),
		CANCELADA_CONTA_EM_COBRANCA(6L,'C'),
		CANCELADO_CONTA_EM_COBRANCA(7L,'C');

		private Long codigoSituacao;
		private Character status;

		private EAccountSituations(Long codigoSituacao, Character status) {
			this.codigoSituacao = codigoSituacao;
			this.status = status;
		}

		public Character getStatus() {
			return status;
		}
	}

	static public AccountSituation getCardSituation() {
		return getCardSituation(EAccountSituations.ATIVA_SITUACAO_INICIAL);
	}

	static public AccountSituation getCardSituation(EAccountSituations ctsf) {
		AccountSituation cts = new AccountSituation();
		cts.setCodigoSituacao(ctsf.codigoSituacao);
		cts.setIdentificador(ctsf.name());
		return cts;
	}
}
