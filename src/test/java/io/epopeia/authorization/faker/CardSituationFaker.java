package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.CardSituation;

/**
 * Faker de objetos CardSituation
 * 
 * @author Fernando Amaral
 */
public class CardSituationFaker {

	public enum ECardSituations {
		ATIVO_SITUACAO_INICIAL(1L,'A'),
		BLOQUEADO_SITUACAO_INICIAL(2L,'B'),
		CANCELADO_A_PEDIDO_DO_PORTADOR(3L,'C'),
		CANCELADO_POR_INATIVIDADE(4L,'C'),
		BLOQUEADO_A_PEDIDO_DO_PORTADOR(5L,'B'),
		ATIVO_A_PEDIDO_DO_PORTADOR(6L,'A'),
		CANCELADO_FALHA_PROCESSAMENTO(7L,'C'),
		CANCELADO_NOVO_CARTAO_CRIADO(8L,'C'),
		BLOQUEADO_SUSPEITA_DE_FRAUDE(9L,'B'),
		BLOQUEADO_CARTAO_PERDIDO(10L,'B'),
		BLOQUEADO_CARTAO_ROUBADO(11L,'B'),
		CANCELADO_CARTAO_PERDIDO(12L,'C'),
		CANCELADO_CARTAO_ROUBADO(13L,'C'),
		BLOQUEADO_TENTATIVAS_EXCEDIDAS(14L,'B'),
		CANCELADO_CONSTATACAO_DE_FRAUDE(15L,'C'),
		CANCELADO_FRAUDE_INTERNA(16L,'C'),
		CANCELADO_CARTAO_CLONADO(17L,'C'),
		BLOQUEADO_RETER_CARTAO(18L,'B'),
		BLOQUEADO_EXCESSO_DE_SAQUE(19L,'B'),
		ATIVO_INVESTIGACAO_FRAUDE_CONCLUIDA(20L,'A'),
		BLOQUEADO_POR_INATIVIDADE(21L,'B'),
		CANCELADO_CARTAO_EXPIRADO(22L,'C'),
		TRATAVADO_SITUACAO_INICIAL(23L,'T'),
		CONTRATO_CANCELADO(24L,'C'),
		CONTRATO_INANDIMPLENTE(25L,'B'),
		CANCELADO_A_PEDIDO_DO_GESTOR_DO_PROGRAMA(26L,'C'),
		BLOQUEADO_CONTRATO_VENCIDO(27L,'B'),
		BLOQUEADO_CORRECAO_FINANCEIRA(28L,'B');

		private Long codigoSituacao;
		private Character status;

		private ECardSituations(Long codigoSituacao, Character status) {
			this.codigoSituacao = codigoSituacao;
			this.status = status;
		}

		public Character getStatus() {
			return status;
		}
	}

	static public CardSituation getCardSituation() {
		return getCardSituation(ECardSituations.ATIVO_SITUACAO_INICIAL);
	}

	static public CardSituation getCardSituation(ECardSituations csf) {
		CardSituation cs = new CardSituation();
		cs.setCodigoSituacao(csf.codigoSituacao);
		cs.setIdentificador(csf.name());
		return cs;
	}
}
