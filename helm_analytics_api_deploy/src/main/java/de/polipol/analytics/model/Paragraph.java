package de.polipol.analytics.model;

public interface Paragraph extends Element, Comparable<Paragraph> {

	@Override
	int compareTo(Paragraph paragraph);

	String getExpression();

	String getFields();

	int getImageHeight();

	String getImageSize();

	int getImageWidth();

	String getInput();

	String getInterpreter();

	String getKernelId();

	String getNotebookId();

	int getOrderId();

	String getOutputType();

	int getResolution();

	int getSpan();

	String getTitle();

	String getViewTypes();

	boolean hasDocumentOutput();

	boolean hasImageOutput();

	boolean hasTextOutput();

	boolean isActive();

	boolean isHide();

	void setActive(boolean active);

	void setExpression(String expression);

	void setFields(String fields);

	void setHide(boolean hide);

	void setImageHeight(int imageHeight);

	void setImageSize(String imageSize);

	void setImageWidth(int imageWidth);

	void setInput(String input);

	void setInterpreter(String interpreter);

	void setKernelId(String kernelId);

	void setNotebookId(String notebookId);

	void setOrderId(int orderId);

	void setOutputType(String outputOutputType);

	void setSpan(int span);

	void setTitle(String title);

	void setViewTypes(String viewTypes);
}