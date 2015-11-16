package io.epopeia.authorization.enums;

/**
 * Definicao das situacoes de processamento do framework. Algumas dessas
 * situacoes estao mapeadas na base do Backoffice Porem independente dela
 * podemos criar quantas situacoes forem necessarias visto que a transacao esta
 * sendo gravada via insereTransacao e na base da autorizadora nao possuir FK
 * para esses IDs.
 * 
 * Quando a insere transacao for migrada os codigos nao mapeados deverao ser
 * inseridos na tabela de situacoes do Backoffice.
 * 
 * @author Fernando Amaral
 */
public enum ECodes {
	AUTORIZADA,
	AUTORIZADA_PARCIALMENTE, /* Nao mapeado na SituacoesTransacoes */
	CANCELADA,
	LIQUIDADA,
	PENDENTE,
	NEGADA_NUMERO_CARTAO_INVALIDO,
	NEGADA_INSUFICIENCIA_DE_FUNDOS,
	NEGADA_TRANSACAO_INVALIDA,
	NEGADA_SENHA_INVALIDA,
	NEGADA_ERRO_DE_PROCESSAMENTO,
	NEGADA_VALOR_INVALIDO,
	NEGADA_FALHA_NA_CRIPTOGRAFIA,
	NEGADA_CARTAO_EXPIRADO,
	NEGADA_LIGUE_EMISSOR,
	NEGADA_TRANSACAO_REFERIDA,
	NEGADA_RETER_CARTAO,
	CANCELADA_SUSPEITA_FRAUDE,
	NEGADA_CARTAO_ROUBADO,
	NEGADA_CARTAO_PERDIDO,
	NEGADA_CARTAO_BLOQUEADO_CANCELADO,
	NEGADA_TRANSACAO_DUPLICADA,
	NEGADA_CODIGO_DE_SEGURANCA_INVALIDO,
	NEGADA_CONTA_EM_COBRANCA,
	NEGADA_CONTA_CANCELADA_BLOQUEADA,
	NEGADA_TRANSACAO_NAO_PERMITIDA,
	NEGADA_EXCESSO_DE_SAQUE,
	NEGADA_CODIGO_DE_SEGURANCA_2_INVALIDO,
	NEGADA_CODIGO_DE_SEGURANCA_CHIP_INVALIDO, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_NEGAR_TRANSACAO,
	NEGADA_GRUPO_RESTRICAO,
	NEGADA_GRUPO_RESTRICAO_CANAL,
	VLR_NAO_PERMIT,
	NEGADA_BLACKLIST_ESTABELECIMENTO,
	NEGADA_PIN_REQUERIDO,
	NEGADA_CVC2_REQUERIDO,
	NEGADA_CRIPTOGRAMA_INVALIDO, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_CHIP_REQUERIDO, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_FORMATO_INVALIDO, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_TRILHA_MAGNETICA_INVALIDA, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_FUNCAO_NAO_PERMITIDA, /* Nao mapeado na SituacoesTransacoes */
	TRANSACAO_ORIGINAL_NAO_ENCONTRADA, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_COMUNICACAO_HSM, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_VALIDACAO_CODIGO_SEGURANCA, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_PRODUTO_NAO_DEFINIDO, /* Nao mapeado na SituacoesTransacoes */
	NEGADA_ATRIBUTOS_TRANSACAO_INVALIDO /* Nao mapeado na SituacoesTransacoes */
}
