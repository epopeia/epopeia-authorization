package io.epopeia.authorization.enums;

public enum EFuncaoCartao {
	CREDITO		(1),
	DEBITO		(2),
	MULTIPLO	(3);

	private int idFuncaoCartao;

	private EFuncaoCartao(int idFuncaoCartao) {
		this.idFuncaoCartao=idFuncaoCartao;
	}

	public int getFuncaoCartao(){
		return this.idFuncaoCartao;
	}

	public static EFuncaoCartao getFuncaoCartao(int idFuncaoCartao){
		for (EFuncaoCartao funcao : EFuncaoCartao.values()) {
			if(funcao.getFuncaoCartao() == idFuncaoCartao){
				return funcao;
			}
		}		
		return null;
	}
}
