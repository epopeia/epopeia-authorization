package io.epopeia.authorization.enums;

/**
 * Definicao dos rotulos a serem atribuidas na mensagem IMF.
 * Rotulos nao possuem valor. O proprio rotulo eh o valor.
 * 
 * @author Fernando Amaral
 */
public enum ELabels {
	AUTHORIZATION_REQUEST, //msgtypes 100 ou 200
	AUTHORIZATION_ADVICE, //msgtypes 120 ou 220
	REVERSAL_REQUEST, //msgtype 400
	REVERSAL_ADVICE, //msgtype 420
	MANUAL, //transacao digitada
	MAGNETIC, //transacao magnetica
	CHIP, //transacao chip
	ECOMMERCE, //transacao ecommerce
	MOBILE, //transacao mobile
	PIN_ENTRY_CAP, //terminal possui capacidade para ler pin (nao significa que o pin foi coletado)
	NO_PIN_ENTRY_CAP, //terminal nao possui capacidade para ler pin.
	PIN, //pin coletado e enviado
	NO_PIN, //pin nao coletado e/ou enviado
	CVC2, //cvc2 coletado e enviado
	NO_CVC2, //cvc2 nao coletado e/ou enviado
	CEP, //cep de onde o terminal esta localizado
	NO_CEP, //sem cep de onde o terminal esta localizado
	CNPJ, //cnpj do estabelecimento
	NO_CNPJ, //sem cnpj do estabelecimento
	CHIP_DATA, //dados do chip presente
	NO_CHIP_DATA, //dados do chip nao enviado
	TRACK1_PRESENT, //trilha 1 presente
	TRACK2_PRESENT, //trilha 2 presente
	NO_TRACK_DATA, //nenhuma trilha enviada
	ATTENDED_TERMINAL, //POS, PDVs alugados por adquirentes para lojistas
	UNATTENDED_TERMINAL, //PDAs, celulares, pc residencial ou qualquer outro terminal do usuario
	TERMINAL_MOBILE, //algumas bandeiras como elo tem um codigo proprio para terminais mobile
	NO_TERMINAL_USED, //URA, voz
	MERCHANT_TERMINAL_ON_LOCAL, //POS, PDV de adquirente na loja com operador do estabelecimento
	MERCHANT_TERMINAL_OFF_LOCAL, //POS, PDV de adquirente fora da loja com operador do estabelecimento
	REMOTE_TERMINAL, //PDAs, celulares, pc residencial ou qualquer outro terminal do usuario
	CARDHOLDER_PRESENT, //portador presente na loja
	CARDHOLDER_NOT_PRESENT, //portador nao presente (cartao de terceiros)
	CARDHOLDER_NOT_PRESENT_VOICE, //portador nao presente (pedido por telefone/URA)
	CARDHOLDER_NOT_PRESENT_ELECTRONIC, //portador nao presente (pedido eletronico por PDAs, celulares, pc residencial)
	CARD_PRESENT, //cartao presente no momento da compra
	CARD_NOT_PRESENT, //cartao nao presente no momento da compra (Ex: transacoes ecommerce digitadas num pc)
	TERMINAL_ONLY_MANUAL,
	TERMINAL_ONLY_MAGNETIC,
	TERMINAL_ONLY_CHIP,
	TERMINAL_MANUAL_MAGNETIC,
	TERMINAL_MAGNETIC_CHIP,
	TERMINAL_MANUAL_MAGNETIC_CHIP,
	TERMINAL_CONTACTLESS,
	PRE_AUTH, //transacao de pre autorizacao (Ex. reservas de hoteis)
	INTERNATIONAL_AUTH, //indica que uma transacao eh internacional
	PARTIAL_AUTH_ALLOWED,
	ALLOW_MERCHANT, //indica que exite uma excecao para o grupo restricao que o cartao esta cadastrado
	ALLOW_CHANNEL, //indica que exite uma excecao para o grupo restricao do canal do cartao
	GROUP_RESTRICTIONS_IN_CHANNEL, //indica que existem grupos restricao nesse canal
	MERCHANT_IN_BLACKLIST,
	WHITELIST_ALLOW_EMPTY_CVC2
}
