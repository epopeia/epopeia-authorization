package io.epopeia.authorization.faker;

import io.epopeia.authorization.domain.backoffice.TransactionType;
import io.epopeia.authorization.enums.ETipoTransacao;

/**
 * Faker de objetos Product
 * 
 * @author Fernando Amaral
 */
public class TransactionTypeFaker {

	public enum ETransactionType {
		CARGA_PRE_PAGO(1L, ETipoTransacao.CARGA_PRE_PAGO, 'S', 'N', 'S', 'S', 1),
		COMPRA_CREDITO_A_VISTA(2L, ETipoTransacao.COMPRA_CREDITO_A_VISTA, 'S', 'S', 'S', 'N',-1),
		TARIFA_DE_SAQUE(3L, ETipoTransacao.TARIFA_DE_SAQUE, 'S','N', 'S', 'S', -1),
		TAXA_DE_SERVICO(4L, ETipoTransacao.TAXA_DE_SERVICO, 'S', 'N', 'N', 'S', -1),
		TAXA_DE_FRETE(5L, ETipoTransacao.TAXA_DE_FRETE, 'S', 'N', 'S', 'S', -1),
		TAXA_DE_REEMISSAO(6L, ETipoTransacao.TAXA_DE_REEMISSAO, 'S', 'N', 'N', 'S', -1), 
		TAXA_DE_REATIVACAO(7L, ETipoTransacao.TAXA_DE_REATIVACAO, 'S', 'N', 'N', 'S', -1), 
		COMPRA_DEBITO(8L, ETipoTransacao.COMPRA_DEBITO, 'S', 'S', 'S', 'N', -1), 
		COMPRA_CREDITO_PARCELADO_LOJISTA(9L, ETipoTransacao.COMPRA_CREDITO_PARCELADO_LOJISTA, 'S', 'S','S', 'N', -1),
		COMPRA_CREDITO_PARCELADO_EMISSOR(10L, ETipoTransacao.COMPRA_CREDITO_PARCELADO_EMISSOR, 'S', 'S', 'S','N', -1),
		TRANSFERENCIA_DE_SALDO_A_CREDITO(11L, ETipoTransacao.TRANSFERENCIA_DE_SALDO_A_CREDITO, 'N', 'N', 'N','S', 1),
		TRANSFERENCIA_DE_SALDO_A_DEBITO(12L, ETipoTransacao.TRANSFERENCIA_DE_SALDO_A_DEBITO, 'N', 'N', 'N','S', -1),
		SAQUE(13L, ETipoTransacao.SAQUE, 'S', 'S', 'S', 'N', -1),
		TARIFA_CONSULTA_SALDO(14L, ETipoTransacao.TARIFA_CONSULTA_SALDO, 'S', 'N', 'N', 'S', -1),
		ESTORNO(16L, ETipoTransacao.ESTORNO, 'S', 'N', 'N', 'S', 1),
		ESTORNO_CARGA(17L, ETipoTransacao.ESTORNO_CARGA, 'S', 'N', 'N', 'S', -1),
		CARGA_PEC(18L, ETipoTransacao.CARGA_PEC, 'S', 'S', 'S', 'N', 1),
		TARIFA_TRANSACAO_INTERNACIONAL(19L, ETipoTransacao.TARIFA_TRANSACAO_INTERNACIONAL, 'N', 'N','S', 'S', -1),
		TARIFA_EMISSAO(20L, ETipoTransacao.TARIFA_EMISSAO, 'S', 'N', 'N', 'S', -1),
		TARIFA_RECARGA_PEC(21L, ETipoTransacao.TARIFA_RECARGA_PEC, 'S', 'N', 'N', 'S', -1),
		RECEBIMENTO_REC(22L, ETipoTransacao.RECEBIMENTO_REC, 'S', 'S', 'S', 'N', -1),
		TARIFA_RECEBIMENTO_REC(23L, ETipoTransacao.TARIFA_RECEBIMENTO_REC, 'S', 'N', 'N', 'S',-1),
		COMPRA_AGRO_CUSTEIO(24L,ETipoTransacao.COMPRA_AGRO_CUSTEIO, 'S', 'S', 'S', 'N', -1),
		COMPRA_AGRO_INVESTIMENTO(25L, ETipoTransacao.COMPRA_AGRO_INVESTIMENTO, 'S', 'S', 'S','N', -1),
		ESTORNO_AGRO_CUSTEIO(26L,ETipoTransacao.ESTORNO_AGRO_CUSTEIO, 'S', 'N', 'N', 'S', 1),
		ESTORNO_AGRO_INVESTIMENTO(27L, ETipoTransacao.ESTORNO_AGRO_INVESTIMENTO, 'S', 'N', 'N','S', 1),
		CARGA_AGRO_CUSTEIO(28L,ETipoTransacao.CARGA_AGRO_CUSTEIO, 'S', 'N', 'S', 'S', 1),
		CARGA_AGRO_INVESTIMENTO(29L, ETipoTransacao.CARGA_AGRO_INVESTIMENTO, 'S', 'N', 'S','S', 1),
		ESTORNO_CARGA_AGRO_CUSTEIO(30L,ETipoTransacao.ESTORNO_CARGA_AGRO_CUSTEIO, 'S', 'N', 'N', 'S',1),
		ESTORNO_CARGA_AGRO_INVESTIMENTO(31L,ETipoTransacao.ESTORNO_CARGA_AGRO_INVESTIMENTO, 'S', 'N', 'N','S', 1),
		TAXA_BNDES(32L, ETipoTransacao.TAXA_BNDES, 'S', 'N','S', 'S', -1),
		DEVOLUCAO_SALDO_COMUM(33L,ETipoTransacao.DEVOLUCAO_SALDO_COMUM, 'S', 'N', 'N', 'S', -1);

		private long codigoTipoTransacao;
		private ETipoTransacao identificador;
		private char verificarContaTitularAtiva;
		private char verificarCartaoAtivo;
		private char verificarValidadeCartao;
		private char liquidacaoImediata;
		private int fatorSensibilizacao;

		private ETransactionType(long codigoTipoTransacao,
				ETipoTransacao identificador, char verificarContaTitularAtiva,
				char verificarCartaoAtivo, char verificarValidadeCartao,
				char liquidacaoImediata, int fatorSensibilizacao) {
			this.codigoTipoTransacao = codigoTipoTransacao;
			this.identificador = identificador;
			this.verificarContaTitularAtiva = verificarContaTitularAtiva;
			this.verificarCartaoAtivo = verificarCartaoAtivo;
			this.verificarValidadeCartao = verificarValidadeCartao;
			this.liquidacaoImediata = liquidacaoImediata;
			this.fatorSensibilizacao = fatorSensibilizacao;
		}

		public ETipoTransacao getIdentificador() {
			return identificador;
		}
	}

	static public TransactionType getTransactionType() {
		return getTransactionType(ETransactionType.COMPRA_CREDITO_A_VISTA);
	}

	static public TransactionType getTransactionType(ETransactionType ttf) {
		TransactionType tt = new TransactionType();
		tt.setCodigoTipoTransacao(ttf.codigoTipoTransacao);
		tt.setIdentificador(ttf.identificador.name());
		tt.setVerificarContaTitularAtiva(ttf.verificarContaTitularAtiva);
		tt.setVerificarCartaoAtivo(ttf.verificarCartaoAtivo);
		tt.setVerificarValidadeCartao(ttf.verificarValidadeCartao);
		tt.setLiquidacaoImediata(ttf.liquidacaoImediata);
		tt.setFatorSensibilizacao(new Long(ttf.fatorSensibilizacao));
		return tt;
	}
}
