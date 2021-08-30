package de.polipol.analytics.task;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Map;

import de.polipol.analytics.file.FileExtension;
import de.polipol.analytics.model.Paragraph;

public interface TaskService {

	static FileExtension getFileExtension(Paragraph paragraph) {
		if (paragraph.hasTextOutput()) {
			return FileExtension.JSON;
		} else if (paragraph.hasImageOutput()) {
			return FileExtension.PNG;
		} else if (paragraph.hasDocumentOutput()) {
			return FileExtension.PDF;
		}
		return FileExtension.TXT;
	}

	static String getVariables() {
		return EMPTY;
	}

	byte[] evaluateParagraph(Paragraph paragraph, Map<String, String> variables, String role);
}