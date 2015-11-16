package io.epopeia.authorization.api;

import java.io.Serializable;

/**
 * Interface a ser implementada pelos gateways para informar
 * ao framework o tipo de transacao que esta sendo tratado.
 * Dessa forma o grupo de participantes apropriado para o
 * processamento da mensagem sera selecionado.
 * 
 * @author Fernando Amaral
 */
public interface MessageClassifier<E extends Enum<?>> {
	E classify(Serializable ctx) throws Exception;
}
