package de.polipol.analytics.model.jsondb;

import static de.polipol.analytics.model.jsondb.JsonDBConstants.CONTAINER_PARAGRAPHS;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import de.polipol.analytics.model.DefaultParagraph;
import de.polipol.analytics.model.Paragraph;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = CONTAINER_PARAGRAPHS, schemaVersion = "1.0")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonDBParagraph extends DefaultParagraph {

	@Id
	private String id;

	public JsonDBParagraph() {
		this.id = UUID.randomUUID().toString();
		init();
	}

	public JsonDBParagraph(Paragraph paragraph) {
		if (StringUtils.isEmpty(paragraph.getId())) {
			this.id = UUID.randomUUID().toString();
		} else {
			this.id = paragraph.getId();
		}
		this.createdAt = paragraph.getCreatedAt();
		this.creator = paragraph.getCreator();
		this.title = paragraph.getTitle();
		this.notebookId = paragraph.getNotebookId();
		this.kernelId = paragraph.getKernelId();
		this.outputType = paragraph.getOutputType();
		this.expression = paragraph.getExpression();
		this.hide = paragraph.isHide();
		this.active = paragraph.isActive();
		this.input = paragraph.getInput();
		this.span = paragraph.getSpan();
		this.orderId = paragraph.getOrderId();
		this.fields = paragraph.getFields();
		this.viewTypes = paragraph.getViewTypes();
		this.interpreter = paragraph.getInterpreter();
		this.imageSize = paragraph.getImageSize();
		this.imageWidth = paragraph.getImageWidth();
		this.imageHeight = paragraph.getImageHeight();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}
