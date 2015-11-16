package io.epopeia.authorization.enums;

public enum EBandeiras {
	MASTERCARD(1),
	VISA(2),
	ELO(3);

	private int idBandeira;

	private EBandeiras(int idBandeira) {
		this.idBandeira = idBandeira;
	}

	public int getCodigoBandeira() {
		return this.idBandeira;
	}

	public static EBandeiras getBandeira(int idBandeira) {
		for (EBandeiras bandeira : EBandeiras.values()) {
			if (bandeira.getCodigoBandeira() == idBandeira) {
				return bandeira;
			}
		}
		return null;
	}
}
