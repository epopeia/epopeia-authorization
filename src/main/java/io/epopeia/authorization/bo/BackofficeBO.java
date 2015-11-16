package io.epopeia.authorization.bo;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.ProceduresRepository;

/**
 * Objeto de negocio que age como a ponte entre os participantes do jpos e os
 * componentes do framework especificamente no contexto da base do backoffice,
 * onde todos as informacoes de cartoes, contas, parametros, modalidades,
 * restricoes etc sao encontrados bem como as procedures para obter essas
 * informacoes e gravar a transacao no banco.
 * 
 * Esse objeto de negocio sera dividido em diversos objetos menores quando
 * migrarmos o conteudo dessas procedures para o framework.
 * 
 * @author Fernando Amaral
 */
@Service
public class BackofficeBO {

	private ProceduresRepository procedures;

	@Autowired
	public BackofficeBO(ProceduresRepository procedures) {
		this.procedures = procedures;
	}

	public void insereTransacaoBackoffice(FieldSet imf) {
		procedures.insereTransacao(imf);
	}

	public void sensibilizaCancelamento(FieldSet imf) {
		procedures.cancelaTransacao(imf);
	}

	public BigDecimal getSaldoCartao(Long cardId) {
		BigDecimal saldo = BigDecimal.ZERO;
		saldo = procedures.getSaldoCartao(cardId);
		saldo = saldo.setScale(2);
		return saldo;
	}

	public void verificaConsultaSaldoInsereTarifa(FieldSet imf) {
		procedures.verificaConsultaSaldoInsereTarifa(imf);
	}

}
