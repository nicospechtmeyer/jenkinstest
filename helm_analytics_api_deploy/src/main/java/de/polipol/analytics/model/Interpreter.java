package de.polipol.analytics.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

	String id;
	int orderId;
	String text;
	String inputFormatter;
	String type;
	List<String> viewTypes;
	String icon;
	String outputType;
	String kernelId;

	public Interpreter() {
		this.id = EMPTY;
		this.orderId = 0;
		this.type = EMPTY;
		this.text = EMPTY;
		this.inputFormatter = EMPTY;
		this.viewTypes = new ArrayList<>();
		this.outputType = EMPTY;
		this.kernelId = EMPTY;
		this.icon = EMPTY;
	}

	public String getIcon() {
		return icon;
	}

	public String getId() {
		return id;
	}

	public String getInputFormatter() {
		return inputFormatter;
	}

	public String getKernelId() {
		return kernelId;
	}

	public int getOrderId() {
		return orderId;
	}

	public String getOutputType() {
		return outputType;
	}

	public String getText() {
		return text;
	}

	public String getType() {
		return type;
	}

	public List<String> getViewTypes() {
		return viewTypes;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInputFormatter(String inputFormatter) {
		this.inputFormatter = inputFormatter;
	}

	public void setKernelId(String kernelId) {
		this.kernelId = kernelId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setViewTypes(List<String> viewTypes) {
		this.viewTypes = viewTypes;
	}
}