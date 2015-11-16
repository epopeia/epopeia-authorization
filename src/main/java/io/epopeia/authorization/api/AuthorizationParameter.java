package io.epopeia.authorization.api;

import java.util.Calendar;

import io.epopeia.authorization.domain.backoffice.Parameter;

public interface AuthorizationParameter {

	String getValor();

	Long getCodigoParametro();

	Parameter getParametro();

	Calendar getDataExpiracao();
}
