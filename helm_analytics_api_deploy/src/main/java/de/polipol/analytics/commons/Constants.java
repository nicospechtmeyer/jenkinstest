package de.polipol.analytics.commons;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Commonly used constants.
 *
 * @author Joern Sprado
 */
public abstract class Constants {

	public static final String BASE_PACKAGE = "de.polipol.analytics";

	// Environment variables
	public static final String PLATFORM_ENCRYPTION_KEY = "PLATFORM_ENCRYPTION_KEY";
	public static final String DEV_MODE = "DEV_MODE";
	public static final String AUTH_ENABLED = "AUTH_ENABLED";
	public static final String CACHE_MOCK = "CACHE_MOCK";
	public static final String CORS_ENABLED = "CORS_ENABLED";
	public static final String AUTH_HOST = "AUTH_HOST";
	public static final String AUTH_CLIENT_ID = "AUTH_CLIENT_ID";
	public static final String AUTH_REALM = "AUTH_REALM";
	public static final String AUTH_SECRET = "AUTH_SECRET";
	public static final String AUTH_CALLBACK = "AUTH_CALLBACK";
	public static final String REDIS_HOST = "REDIS_HOST";
	public static final String REDIS_PORT = "REDIS_PORT";
	public static final String PUBLIC_FOLDER = "PUBLIC_FOLDER";
	public static final String PRIVATE_FOLDER = "PRIVATE_FOLDER";
	public static final String DICTIONARY_FOLDER = "DICTIONARY_FOLDER";
	public static final String DB_FILES_LOCATION = "DB_FILES_LOCATION";
	public static final String STANDALONE = "STANDALONE";

	// Kernel
	public static final String R_KERNEL_ID = "r";

	// Default settings
	public static final String DEFAULT_DB_FILES_LOCATION = "./data/db";
	public static final String DEFAULT_DB_BASE_SCAN_PACKAGE = Constants.BASE_PACKAGE + ".model.jsondb";
	public static final String DEFAULT_REDIS_HOST = "localhost";
	public static final String DEFAULT_REDIS_PORT = "6379";
	public static final String DEFAULT_PRIVATE_FOLDER = "/app/assets/private";
	public static final String DEFAULT_PUBLIC_FOLDER = "/app/assets/public";
	public static final String DEFAULT_DICTIONARY_FOLDER = "/app/assets/dictionary";

	// Content types
	public static final String APPLICATION_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String APPLICATION_IMAGE_BMP = "image/bmp";
	public static final String APPLICATION_IMAGE_JPEG = "image/jpeg";
	public static final String APPLICATION_IMAGE_PNG = "image/png";
	public static final String APPLICATION_IMAGE_SVG = "image/svg+xml";
	public static final String APPLICATION_IMAGE_TIFF = "image/tiff";
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String APPLICATION_XML = "application/xml";
	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String ATTACHMENT = "attachment";
	public static final String BASE64 = "base64";
	public static final String ACCEPT = "Accept";
	public static final String ENCODE = "encode";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";

	// Security
	public static final String SECURITY_CLIENT = "HeaderClient";
	public static final String SECURITY_AUTHORIZER = "custom";

	// CLI
	public static final String ARG_PORT = "port";
	public static final String ARG_PORT_DESC = "server port";
	public static final String ARG_PORT_SHORT = "p";

	// RESTful service
	public static final String LOCALHOST = "localhost";
	public static final int DEFAULT_PORT = 80;
	public static final String UPLOADED_FILE_KEY = "file";
	public static final String UPLOADED_FILES_KEY = "files";
	public static final String ENDPOINT_WEBSOCKET = "/events";
	public static final String ENDPOINT_INTERPRETERS = "/interpreters";
	public static final String ENDPOINT_NOTEBOOKS = "/notebooks";
	public static final String ENDPOINT_PARAGRAPHS = "/paragraphs";
	public static final String ENDPOINT_INFOS = "/ping";
	public static final String ENDPOINT_RESULTS = "/results";
	public static final String ENDPOINT_FILTERS = "/filters";
	public static final String ENDPOINT_EVALUATE = "/eval";
	public static final String ENDPOINT_EVALUATE_FILE = "/eval/file";
	public static final String ENDPOINT_LOGS = "/logs";
	public static final String ENDPOINT_JOBS = "/jobs";
	public static final String ENDPOINT_FILES = "/files";
	public static final String ENDPOINT_ENCRYPT = "/encrypt";
	public static final String ENDPOINT_FILE = "/file";
	public static final String ENDPOINT_TASKS = "/tasks";
	public static final String ENDPOINT_REDOC = "/redoc";
	public static final String ENDPOINT_SWAGGER_UI = "/api";
	public static final String ENDPOINT_SWAGGER_JSON = "/swagger-docs";

	// Comments
	public static final String LOGGER_SEPARATOR = "---------------------------------------------------------------------------------------";

	// Cache
	public static final String CACHE_SEPARATOR = "::";

	// Swagger
	public static final String SWAGGER_ANNOTATION_SCANNING_FOLDER = BASE_PACKAGE + ".web";
	public static final String SWAGGER_FILE_TITLE = "Model-Service API";
	public static final String SWAGGER_MODEL_TITLE = "Model-Service API";
	public static final String SWAGGER_CRON_TITLE = "Cron-Service API";
	public static final String SWAGGER_DESC = EMPTY;
	public static final String SWAGGER_VERSION = "1.0";

	public static final String ID = "id";
	public static final String INFO = "Info";
	public static final String INTERPRETER = "Interpreter";
	public static final String NOTEBOOK = "Notebook";
	public static final String NOTEBOOK_ID = "notebookId";
	public static final String NOTEBOOK_ID_DESC = "Notebook ID";
	public static final String PARAGRAPH = "Paragraph";
	public static final String PARAGRAPH_ID = "paragraphId";
	public static final String PARAGRAPH_ID_DESC = "Paragraph ID";
	public static final String KERNEL_ID = "kernelId";
	public static final String FILTER = "Filter";
	public static final String FILTER_ID_DESC = "Filter ID";
	public static final String LOG = "Log";
	public static final String LOG_ID_DESC = "Log ID";
	public static final String JOB = "Job";
	public static final String JOB_ID_DESC = "Job ID";
	public static final String FILE = "File";
	public static final String FILE_ID_DESC = "File ID";
	public static final String FILENAME = "filename";
	public static final String FILENAME_DESC = "File name";
	public static final String TASK = "Task";
	public static final String ANALYTICS = "Anayltics";
	public static final String FIELD = "field";
	public static final String FIELD_DESC = "Attribute name";
	public static final String DIRECTORY = "directory";
	public static final String DIRECTORY_DESC = "Directory name";
	public static final String USER = "user";
//	public static final String STANDARD_USER = "user";
	public static final String ROLE = "role";
	public static final String EXPRESSION = "expression";
	public static final String FILE_EXTENSION = "extension";
	public static final String OUTPUT = "output";
	public static final String INPUT = "input";
	public static final String DEBUG = "debug";
	public static final String IS_PROCESSING_SUCCESSFUL = "isProcessingSuccessful";
	public static final String PROCESSED_AT = "processedAt";
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String NO_PATH_VARIABLE_IDENTIFIER = "NO_PATH_VARIABLE_";

//	public static final Path DEFAULT_CONFIG_DIRECTORY = Paths.get(System.getProperty("user.dir") + File.separator + "config");
//	public static final Path DEFAULT_TMP_DIRECTORY = Paths.get(System.getProperty("user.dir") + File.separator + "tmp");
//	public static final String DELIMITER = "&";

	public static final String TYPE = "type";
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_DOCUMENT = "document";
	public static final String TYPE_PLAIN = "plain";

	public static final String IMAGE_SIZE_EXTRAHEIGHT = "extraheight";
	public static final String IMAGE_SIZE_EXTRAWIDTH = "extrawidth";
	public static final String IMAGE_SIZE_HEIGHT = "height";
	public static final String IMAGE_SIZE_STANDARD = "standard";
	public static final String IMAGE_SIZE_WIDTH = "width";

	public static final String MIME_PNG_PREFIX = "data:image/png;base64,";
	public static final String MIME_PDF_PREFIX = "data:application/pdf;base64,";
	public static final String MIME_JSON_PREFIX = "data:application/json;base64,";

}
