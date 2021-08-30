package de.polipol.analytics.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.commons.OutputTypes;

public class DefaultParagraph extends AbstractElement implements Paragraph {

	// general attributes
	protected String title;
	protected String notebookId;
	protected int orderId;
	protected boolean active;
	protected String expression;
	protected String kernelId;
	protected String outputType;

	// image-specific attributes
	protected String input;
	protected String imageSize;
	protected int imageHeight;
	protected int imageWidth;

	// app-specific attributes
	protected boolean hide;
	protected String interpreter;
	protected String viewTypes;
	protected String fields;
	protected int span;

	public DefaultParagraph() {
		this.init();
	}

	@Override
	public int compareTo(final Paragraph paragraph) {
		if (this.getOrderId() < paragraph.getOrderId()) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public String getExpression() {
		return expression;
	}

	@Override
	public String getFields() {
		return fields;
	}

	@Override
	public int getImageHeight() {
		return imageHeight;
	}

	@Override
	public String getImageSize() {
		return imageSize;
	}

	@Override
	public int getImageWidth() {
		return imageWidth;
	}

	@Override
	public String getInput() {
		return input;
	}

	@Override
	public String getInterpreter() {
		return interpreter;
	}

	@Override
	public String getKernelId() {
		return kernelId;
	}

	@Override
	public String getNotebookId() {
		return notebookId;
	}

	@Override
	public int getOrderId() {
		return orderId;
	}

	@Override
	public String getOutputType() {
		return outputType;
	}

	@Override
	public int getResolution() {
		return 200;
	}

	@Override
	public int getSpan() {
		return span;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getViewTypes() {
		return viewTypes;
	}

	@Override
	public boolean hasDocumentOutput() {
		return StringUtils.equals(this.outputType, OutputTypes.DOCUMENT.toString()) ? true : false;
	}

	@Override
	public boolean hasImageOutput() {
		return StringUtils.equals(this.outputType, OutputTypes.IMAGE.toString()) ? true : false;
	}

	@Override
	public boolean hasTextOutput() {
		return StringUtils.equals(this.outputType, OutputTypes.JSON.toString()) ? true : false;
	}

	protected void init() {
		this.id = UUID.randomUUID().toString();
		this.fields = EMPTY;
		this.notebookId = EMPTY;
		this.creator = EMPTY;
		this.createdAt = EMPTY;
		this.title = EMPTY;
		this.expression = EMPTY;
		this.kernelId = EMPTY;
		this.outputType = EMPTY;
		this.imageSize = EMPTY;
		this.input = EMPTY;
		this.viewTypes = EMPTY;
		this.orderId = 0;
		this.hide = false;
		this.active = false;
		this.span = 12;
		this.imageHeight = 800;
		this.imageWidth = 1600;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isHide() {
		return hide;
	}

	@Override
	public void setActive(final boolean active) {
		this.active = active;
	}

	@Override
	public void setExpression(final String expression) {
		this.expression = expression;
	}

	@Override
	public void setFields(final String fields) {
		this.fields = fields;
	}

	@Override
	public void setHide(final boolean hide) {
		this.hide = hide;
	}

	@Override
	public void setImageHeight(final int imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Override
	public void setImageSize(final String imageSize) {
		this.imageSize = imageSize;
	}

	@Override
	public void setImageWidth(final int imageWidth) {
		this.imageWidth = imageWidth;
	}

	@Override
	public void setInput(final String input) {
		this.input = input;
	}

	@Override
	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	@Override
	public void setKernelId(final String kernelId) {
		this.kernelId = kernelId;
	}

	@Override
	public void setNotebookId(final String notebookId) {
		this.notebookId = notebookId;
	}

	@Override
	public void setOrderId(final int orderId) {
		this.orderId = orderId;
	}

	@Override
	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	@Override
	public void setSpan(final int span) {
		this.span = span;
	}

	@Override
	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public void setViewTypes(String viewTypes) {
		this.viewTypes = viewTypes;
	}
}