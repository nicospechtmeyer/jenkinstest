package de.polipol.analytics.connect.r;

import static de.polipol.analytics.commons.Constants.IMAGE_SIZE_EXTRAHEIGHT;
import static de.polipol.analytics.commons.Constants.IMAGE_SIZE_EXTRAWIDTH;
import static de.polipol.analytics.commons.Constants.IMAGE_SIZE_HEIGHT;
import static de.polipol.analytics.commons.Constants.IMAGE_SIZE_STANDARD;
import static de.polipol.analytics.commons.Constants.IMAGE_SIZE_WIDTH;
import static de.polipol.analytics.commons.Constants.OUTPUT;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.cache.Cache;
import de.polipol.analytics.connect.AnalyticsService;
import de.polipol.analytics.file.FileExtension;
import de.polipol.analytics.model.Paragraph;
import de.polipol.analytics.task.TaskService;

public final class RTaskService implements TaskService {

	protected Cache cache;
	protected AnalyticsService analyticsService;

	public RTaskService(Cache cache) {
		analyticsService = new RService();
	}

	@Override
	public byte[] evaluateParagraph(Paragraph paragraph, Map<String, String> variables, String role) {
		byte[] byteOutput = new byte[] {};
		try {
			FileExtension fileExtension = TaskService.getFileExtension(paragraph);
			if (paragraph.hasTextOutput()) {
				Map<String, String> post = new HashMap<>();
				byteOutput = analyticsService.getText(fileExtension, paragraph.getExpression(), OUTPUT, variables,
						post);
			} else if (paragraph.hasImageOutput()) {
				byteOutput = analyticsService.getImage(fileExtension, paragraph.getExpression(), variables,
						this.getImageWidth(paragraph.getImageSize()), this.getImageHeight(paragraph.getImageSize()),
						paragraph.getResolution());
			}
		} catch (Exception exception) {
			byteOutput = exception.toString().getBytes();
		}
		return byteOutput;
	}

	private int getImageHeight(String imageSize) {
		if (StringUtils.isNotEmpty(imageSize)) {
			if (imageSize.equals(IMAGE_SIZE_STANDARD)) {
				return 1500;
			}
			if (imageSize.equals(IMAGE_SIZE_WIDTH)) {
				return 1500;
			}
			if (imageSize.equals(IMAGE_SIZE_EXTRAWIDTH)) {
				return 1500;
			}
			if (imageSize.equals(IMAGE_SIZE_HEIGHT)) {
				return 2500;
			}
			if (imageSize.equals(IMAGE_SIZE_EXTRAHEIGHT)) {
				return 3500;
			}
		}
		return 2000;
	}

	private int getImageWidth(String imageSize) {
		if (StringUtils.isNotEmpty(imageSize)) {
			if (imageSize.equals(IMAGE_SIZE_STANDARD)) {
				return 2000;
			}
			if (imageSize.equals(IMAGE_SIZE_WIDTH)) {
				return 3000;
			}
			if (imageSize.equals(IMAGE_SIZE_EXTRAWIDTH)) {
				return 4000;
			}
			if (imageSize.equals(IMAGE_SIZE_HEIGHT)) {
				return 2000;
			}
			if (imageSize.equals(IMAGE_SIZE_EXTRAHEIGHT)) {
				return 2000;
			}
		}
		return 2000;
	}
}
