package io.epopeia.authorization.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.epopeia.authorization.domain.backoffice.TransactionType;
import io.epopeia.authorization.enums.ETxnAttributes;
import io.epopeia.authorization.model.FieldSet;
import io.epopeia.authorization.repository.backoffice.TransactionTypeRepository;

/**
 * Objeto de negocio que define os tipos de transacoes
 * suportados pelos sistema
 * 
 * @author Fernando Amaral
 */
@Service
public class TransactionTypeBO {

	private TransactionTypeRepository tiposTransacoes;

	@Autowired
	public TransactionTypeBO(TransactionTypeRepository tiposTransacoes) {
		this.tiposTransacoes = tiposTransacoes;
	}

	@Cacheable(value = "tiposTransacoes", unless = "#result == null")
	public FieldSet getTransactionAtributes(String tipoTransacao) {
		if (tipoTransacao == null)
			return null;
		TransactionType tt = tiposTransacoes.findByIdentificador(tipoTransacao);
		if (tt == null)
			return null;
		FieldSet attr = new FieldSet(ETxnAttributes.ATRIBUTOS_TRANSACAO);
		attr.setValue(ETxnAttributes.TIPO_TRANSACAO_ID, tt.getCodigoTipoTransacao().toString());
		attr.setValue(ETxnAttributes.IDENTIFICADOR, tt.getIdentificador());
		attr.setValue(ETxnAttributes.VERIFICAR_CARTAO_ATIVO, tt.getVerificarCartaoAtivo().toString());
		attr.setValue(ETxnAttributes.VERIFICAR_CONTA_ATIVA, tt.getVerificarContaTitularAtiva().toString());
		attr.setValue(ETxnAttributes.VERIFICAR_VALIDADE_CARTAO, tt.getVerificarValidadeCartao().toString());
		return attr;
	}
}
