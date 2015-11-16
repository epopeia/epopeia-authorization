package io.epopeia.authorization.domain.autorizadora;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Definicao da entidade ErrorLog e os planos de execucao das querys com outras
 * entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "ERROR_LOG")
public class ErrorLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "DATA_HORA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateHour = Calendar.getInstance();

	@Column(name = "MESSAGE", length = 255)
	private String message;

	@Column(name = "STACKTRACE", length = 8000)
	private String stackTrace;

	@Lob
	@Column(name = "isoMessage")
	private byte[] isoMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getDateHour() {
		return dateHour;
	}

	public void setDateHour(Calendar dateHour) {
		this.dateHour = dateHour;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public byte[] getIsoMessage() {
		return isoMessage;
	}

	public void setIsoMessage(byte[] isoMessage) {
		this.isoMessage = isoMessage;
	}
}
