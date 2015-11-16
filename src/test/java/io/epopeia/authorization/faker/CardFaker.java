package io.epopeia.authorization.faker;

import java.util.Calendar;

import io.epopeia.authorization.domain.backoffice.Card;

/**
 * Faker de objetos Card
 * 
 * @author Fernando Amaral
 */
public class CardFaker {

	public enum EGruposRestricoes {
		SEM_GRUPO(null),
		CAIXA_MCM(18L);

		private Long codigoGrupoRestricao;

		private EGruposRestricoes(Long codigoGrupoRestricao) {
			this.codigoGrupoRestricao = codigoGrupoRestricao;
		}

		public Long getCodigoGrupoRestricao() {
			return codigoGrupoRestricao;
		}
	}

	public enum ECards {
		CartaoUm(1L, "064E05EA967C1E37", Calendar.getInstance(), EGruposRestricoes.SEM_GRUPO),
		CartaoDois(2L, "2E3C1D523DEBA185", Calendar.getInstance(), EGruposRestricoes.CAIXA_MCM);

		private Long codigoCartao;
		private String numero;
		private Calendar dataValidade;
		private EGruposRestricoes grupoRestricao;

		private ECards(Long codigoCartao, String numero, Calendar dataValidade,
				EGruposRestricoes grupoRestricao) {
			this.codigoCartao = codigoCartao;
			this.numero = numero;
			this.dataValidade = dataValidade;
			this.grupoRestricao = grupoRestricao;
		}

		public String getNumero() {
			return numero;
		}

		public Long getCodigoCartao() {
			return codigoCartao;
		}
	}

	static public Card getCard() {
		return getCard(ECards.CartaoUm);
	}

	static public Card getCard(ECards cf) {
		Card c = new Card();
		c.setCodigoCartao(cf.codigoCartao);
		c.setNumero(cf.numero);
		c.setDataValidade(cf.dataValidade);
		c.setCodigoGrupoRestricao(cf.grupoRestricao.getCodigoGrupoRestricao());
		return c;
	}
}
