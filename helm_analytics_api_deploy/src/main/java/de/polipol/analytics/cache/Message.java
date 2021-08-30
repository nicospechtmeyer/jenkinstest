package de.polipol.analytics.cache;

import static de.polipol.analytics.commons.Constants.DATE_FORMAT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {

	public String role;
	public String targetKey;
	public String paragraphId;
	public Map<String, String> variables;
	public String requestedAt;
	public boolean processed;

	public Message(String role, String targetKey, String paragraphId) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		this.role = role;
		this.requestedAt = formatter.format(new Date());
		this.targetKey = targetKey;
		this.paragraphId = paragraphId;
		this.variables = new HashMap<>();
		this.processed = false;
	}

	public Message(String role, String targetKey, String paragraphId, Map<String, String> variables) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		this.role = role;
		this.requestedAt = formatter.format(new Date());
		this.targetKey = targetKey;
		this.paragraphId = paragraphId;
		this.variables = variables;
		this.processed = false;
	}

	public String getParagraphId() {
		return paragraphId;
	}

	public String getRequestedAt() {
		return requestedAt;
	}

	public String getRole() {
		return role;
	}

	public String getTargetKey() {
		return targetKey;
	}

	public Map<String, String> getVariables() {
		return variables;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setParagraphId(String paragraphId) {
		this.paragraphId = paragraphId;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public void setRequestedAt(String requestedAt) {
		this.requestedAt = requestedAt;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
}
