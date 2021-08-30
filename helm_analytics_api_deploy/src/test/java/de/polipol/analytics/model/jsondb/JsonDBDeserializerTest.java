package de.polipol.analytics.model.jsondb;

import static de.polipol.analytics.commons.TestConstants.VIEW_TYPES;
import static de.polipol.analytics.commons.TestConstants.VIEW_TYPES_VALUE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.polipol.analytics.model.ModelUtils;
import de.polipol.analytics.model.Paragraph;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class JsonDBDeserializerTest {

//	@Test
//	public void deserializeNotebook() throws JsonMappingException, JsonProcessingException {
//		Notebook notebook1, notebook2;
//		notebook1 = new JsonDBNotebook();
//		notebook1.setTitle(TITLE_VALUE);
//		notebook2 = ModelUtils.getUpdatedElement(notebook1, JsonDBNotebook.class);
//		assertThat(notebook2.getId(), equalTo(notebook1.getId()));
//		assertThat(notebook2.getTitle(), equalTo(TITLE_VALUE));
//	}
//	
//	@Test
//	public void deserializeParagraph() throws JsonMappingException, JsonProcessingException {
//		Paragraph paragraph1, paragraph2;
//		paragraph1 = new JsonDBParagraph();
//		paragraph1.setTitle(TITLE_VALUE);
//		paragraph2 = ModelUtils.getUpdatedElement(new Gson().toJson(paragraph1), JsonDBParagraph.class);
//		assertThat(paragraph2.getId(), equalTo(paragraph1.getId()));
//		assertThat(paragraph2.getTitle(), equalTo(TITLE_VALUE));
//	}

	@Test
	public void deserializeParagraphViewType() throws JsonMappingException, JsonProcessingException {
		Paragraph paragraph1, paragraph2;
		paragraph1 = new JsonDBParagraph();
		paragraph2 = ModelUtils.getUpdatedElement(paragraph1, VIEW_TYPES, Arrays.toString(VIEW_TYPES_VALUE));
		assertThat(paragraph2.getId(), equalTo(paragraph1.getId()));
		assertThat(paragraph2.getViewTypes().split(",").length, equalTo(2));
	}
}
