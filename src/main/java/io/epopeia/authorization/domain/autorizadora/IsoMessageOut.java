package io.epopeia.authorization.domain.autorizadora;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Definicao da entidade MessageIsoOut e os planos de execucao das querys com
 * outras entidades relacionadas
 * 
 * @author Fernando Amaral
 */
@Entity
@Table(name = "MESSAGE_ISO_OUT")
public class IsoMessageOut {

	@Id
	@Column(name = "ID_MESSAGE_ISO_IN")
	private Long id;

	@Column(name = "MESSAGE_TYPE", length = 4)
	private String messageType;

	@Column(name = "DATE")
	@Temporal(TemporalType.DATE)
	private Calendar date;

	@Column(name = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar datetime;

	@Column(name = "MESSAGE_XML", length = 8000)
	private String messageXML;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Calendar getDatetime() {
		return datetime;
	}

	public void setDatetime(Calendar datetime) {
		this.datetime = datetime;
	}

	public String getMessageXML() {
		return messageXML;
	}

	public void setMessageXML(String messageXML) {
		this.messageXML = messageXML;
	}
}
