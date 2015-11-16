package io.epopeia.authorization.enums;

/**
 * Definicao dos parametros que a mensagem possui carregados do banco.
 * Parametros sao como os labels. Se estiverem presentes na mensagem
 * entende-se como as permissoes que a mensagem possuira durante o 
 * processamento de autorizacao.
 * 
 * @author Fernando Amaral
 */
public enum EParams {
	PARAMETERS_INFO
	, VALIDAR_CHIP
	, PERMITE_DEBITO
	, VALOR_MINIMO_TRANSACAO
	, VALOR_MAXIMO_TRANSACAO
}
