package io.epopeia.authorization.enums;

/**
 * Definicao dos meios de entrada do PAN segundo o padrao ISO8583.
 * 
 * @author Fernando Amaral
 */
public enum EPanEntryMode {
	  MANUAL("01")
	, MAGNETIC_NO_TRACK("02")
	, MAGNETIC_FULL_TRACK("90")
	, FALLBACK_NO_TRACK("79")
	, FALLBACKC_FULL_TRACK("80")
	, CHIP("05")
	, ECOMMERCE("81")
	;

	private EPanEntryMode(String panEntryModeCode) {
		this.panEntryModeCode = panEntryModeCode;
	}

	private String panEntryModeCode;

	public String getPanEntryModeCode() {
		return panEntryModeCode;
	}

	public static EPanEntryMode getByCode(String code) {
		for (EPanEntryMode entryMode : EPanEntryMode.values()) {
			if (entryMode.getPanEntryModeCode().equals(code)) {
				return entryMode;
			}
		}
		return null;
	}
}
