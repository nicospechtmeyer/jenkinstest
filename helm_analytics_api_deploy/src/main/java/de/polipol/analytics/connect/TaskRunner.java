package de.polipol.analytics.connect;

//import static de.polipol.analytics.connect.r.RKeywords.NO_PATH_VARIABLE_IDENTIFIER;
//import static de.polipol.analytics.connect.r.RKeywords.PARAGRAPH_REFERENCE;
//import static de.polipol.analytics.output.OutputGenerator.OutputType.PRETTYJSON;
//import static de.polipol.analytics.file.FileExtension.JSON;
//import static de.polipol.analytics.file.FileExtension.LOG;
//import static de.polipol.analytics.file.FileExtension.PDF;
//import static de.polipol.analytics.file.FileExtension.PNG;
//import static de.polipol.analytics.file.FileExtension.RNW;
//import static de.polipol.analytics.file.FileExtension.TXT;
//import static de.polipol.analytics.file.FileExtension.map;
//import static de.polipol.analytics.web.Server.DELIMITER;
//import static de.polipol.analytics.web.Server.EXPRESSION;
//import static de.polipol.analytics.web.Server.FILENAME;
//import static de.polipol.analytics.web.Server.ID;
//import static de.polipol.analytics.web.Server.IS_PROCESSING_SUCCESSFUL;
//import static de.polipol.analytics.web.Server.POOL_ID;
//import static de.polipol.analytics.web.Server.PROCESSED_AT;
//import static de.polipol.analytics.web.Server.USER;
//import static de.polipol.analytics.web.Server.MIME_PDF_PREFIX;
//import static de.polipol.analytics.web.Server.MIME_PNG_PREFIX;
//import static de.polipol.analytics.web.Server.OUTPUT;
//import static de.polipol.analytics.web.Server.PARAGRAPH_ID;
//import static de.polipol.analytics.web.Server.DATE_FORMAT;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.text.SimpleDateFormat;
//import java.nio.file.Files;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.github.mustachejava.DefaultMustacheFactory;
//import com.github.mustachejava.Mustache;
//import com.github.mustachejava.MustacheFactory;
//import com.google.common.collect.Lists;
//import com.google.gson.Gson;
//import com.google.gson.stream.JsonReader;
//
//import de.polipol.analytics.service.data.DataService;
//import de.polipol.analytics.service.file.FileService;
//import de.polipol.analytics.service.r.RService;
//import de.polipol.analytics.file.FileExtension;
//import de.polipol.analytics.web.util.WebUtils;

public final class TaskRunner {

////	private static final Logger logger = LoggerFactory.getLogger(TaskRunner.class);
//
//    static final String IMAGE_SIZE_EXTRAHEIGHT = "extraheight";
//    static final String IMAGE_SIZE_EXTRAWIDTH = "extrawidth";
//    static final String IMAGE_SIZE_HEIGHT = "height";
//    static final String IMAGE_SIZE_STANDARD = "standard";
//    static final String IMAGE_SIZE_WIDTH = "width";
//
//    private RService rService;
//    private DataService dataService;
//    private FileService fileService;
//    private ModelService modelService;
//
//    public TaskRunner(RService rService, DataService dataService, FileService fileService, ModelService modelService) {
//        this.rService = rService;
//        this.dataService = dataService;
//        this.fileService = fileService;
//        this.modelService = modelService;
//    }
//
//    public static Map<String, String> getPathVariables(final Map<String, String> variables) {
//        Map<String, String> filters = new HashMap<>();
//        for (Map.Entry<String, String> variable : variables.entrySet()) {
//            String key = variable.getKey();
//            String value = variable.getValue();
//            if (!StringUtils.startsWith(key, NO_PATH_VARIABLE_IDENTIFIER)) {
//                filters.put(key, value);
//            }
//        }
//        return filters;
//    }
//
//    public static Map<String, String> getEvalVariables(final Map<String, String> variables) {
//        Map<String, String> filters = new HashMap<>();
//        for (Map.Entry<String, String> variable : variables.entrySet()) {
//            String key = variable.getKey();
//            String value = variable.getValue();
//            if (StringUtils.startsWith(key, NO_PATH_VARIABLE_IDENTIFIER)) {
//                filters.put(StringUtils.substringAfter(key, NO_PATH_VARIABLE_IDENTIFIER), value);
//            } else {
//                filters.put(key, value);
//            }
//        }
//        return filters;
//    }
//
//    private static String getFilterPathString(String notebookId, String role, Map<String, String> variables,
//                                              String separator) {
//        String filterPathString = separator + role + separator + notebookId;
//        List<String> sortedKeys = Lists.newArrayList(variables.keySet());
//        Collections.sort(sortedKeys);
//        for (String key : sortedKeys) {
//            filterPathString = filterPathString + separator + variables.get(key);
//        }
//        return filterPathString;
//    }
//
//    public static String getIdentifier(String fileExtension, String notebookId, String paragraphId, String role,
//                                       Map<String, String> variables) {
//        return fileExtension + "__" + paragraphId + getFilterPathString(notebookId, role, variables, "__");
//    }
//
//    public static Path getFilePath(Path directoryPath, String paragraphId, FileExtension fileExtension) {
//        Path filePath = Paths
//                .get(directoryPath + File.separator + paragraphId + "." + fileExtension.toString().toLowerCase());
//        return filePath;
//    }
//
//    public void evaluate(String notebookId, Map<String, String> variables, String role)
//            throws JsonParseException, JsonMappingException, IOException {
//        List<Paragraph> paragraphs = modelService.getParagraphs(notebookId);
//        Collections.sort(paragraphs);
//
//        Map<String, String> evalVariables = getEvalVariables(variables);
//        Map<String, String> pathVariables = getPathVariables(variables);
//
//        for (Paragraph paragraph : paragraphs) {
//            if (!paragraph.isActive() && (paragraph.isRExpression() || paragraph.isDataExpression())) {
//
//                Path directoryPath = getDirectoryPath(paragraph.getNotebookId(), role, pathVariables);
//                Path filePath = getFilePath(directoryPath, paragraph.getId(), getFileExtension(paragraph));
//
//                try {
//                    String result = WebUtils.getString(filePath);
//                    if (StringUtils.isNotEmpty(result)) {
//                        evalVariables.put(getParagraphIdentifier(paragraph), result);
//                    }
//                } catch (Exception exception) {
//                    evalVariables.put(getParagraphIdentifier(paragraph), "");
//                }
//            } else if (paragraph.isJsonExpression()) {
//                evalVariables.put(getParagraphIdentifier(paragraph), paragraph.getExpression());
//            }
//        }
//
//        for (Paragraph paragraph : paragraphs) {
//            if (paragraph.isActive()
//                    && (paragraph.isRExpression() || paragraph.isDataExpression() || paragraph.isImageExpression()
//                    || paragraph.isRnwPdfExpression() || paragraph.isRmdPdfExpression())) {
//                String output = evaluateParagraph(paragraph, evalVariables, pathVariables, role);
//                if (WebUtils.isJson(output)) {
//                    evalVariables.put(getParagraphIdentifier(paragraph), output);
//                }
//            }
//        }
//    }
//
//    public String evaluateParagraph(Paragraph paragraph, Map<String, String> evalVariables,
//                                    Map<String, String> pathVariables, String role) {
//        String output = EMPTY;
//        byte[] byteOutput = new byte[]{};
//        try {
//            if (paragraph.isRExpression()) {
//                Map<String, String> post = new HashMap<>();
//                output = rService.getResult(POOL_ID, JSON, paragraph.getExpression(), OUTPUT, evalVariables, post);
//                byteOutput = output.getBytes();
//            } else if (paragraph.isImageExpression()) {
//                Path filePath = rService.getImage(POOL_ID, PNG, paragraph.getExpression(), evalVariables,
//                        this.getImageWidth(paragraph.getImageSize()), this.getImageHeight(paragraph.getImageSize()),
//                        paragraph.getResolution());
//                output = MIME_PNG_PREFIX + WebUtils.getBase64String(filePath);
//                byteOutput = WebUtils.getByteArray(filePath);
//            } else if (paragraph.isDataExpression()) {
//                Gson gson = new Gson();
//                String substitutedExpression = this.getDataExpression(evalVariables, paragraph.getExpression());
//                JsonReader reader = new JsonReader(new StringReader(substitutedExpression));
//                DefaultDataExpression dataExpression = gson.fromJson(reader, DefaultDataExpression.class);
//                if (dataExpression.isNativeQuery()) {
//                    output = dataService.getResult(dataExpression.getPool(), dataExpression.getContainer(),
//                            dataExpression.getColumns(), dataExpression.getQuery(), dataExpression.getNumberOfRows(),
//                            PRETTYJSON);
//                } else {
//                    if (StringUtils.isNotEmpty(dataExpression.getQuery())) {
//                        List<String> selections = Arrays.asList(dataExpression.getQuery().split(DELIMITER));
//                        output = dataService.getResult(dataExpression.getPool(), dataExpression.getContainer(),
//                                dataExpression.getColumns(), selections, dataExpression.getNumberOfRows(), PRETTYJSON);
//                    } else {
//                        output = dataService.getResult(dataExpression.getPool(), dataExpression.getContainer(),
//                                dataExpression.getColumns(), dataExpression.getNumberOfRows(), PRETTYJSON);
//                    }
//                }
//                byteOutput = output.getBytes();
//            } else if (paragraph.isRnwPdfExpression()) {
//                Path filePath = rService.getDocument(POOL_ID, RNW, PDF, paragraph.getExpression(), evalVariables);
//                output = MIME_PDF_PREFIX + WebUtils.getBase64String(filePath);
//                byteOutput = WebUtils.getByteArray(filePath);
//            } else if (paragraph.isRmdPdfExpression()) {
//                Path filePath = rService.getDocument(POOL_ID, FileExtension.RMD, PDF, paragraph.getExpression(),
//                        evalVariables);
//                output = MIME_PDF_PREFIX + WebUtils.getBase64String(filePath);
//                byteOutput = WebUtils.getByteArray(filePath);
//            }
//            this.writeToFile(paragraph, role, pathVariables, byteOutput, true);
//        } catch (Exception exception) {
//            try {
//                this.writeToFile(paragraph, role, pathVariables, e.toString().getBytes(), false);
//            } catch (IOException e1) {
//            }
//            output = e.getMessage();
//        }
//        return output;
//    }
//
//    private int getImageHeight(String imageSize) {
//        if (StringUtils.isNotEmpty(imageSize)) {
//            if (imageSize.equals(IMAGE_SIZE_STANDARD)) {
//                return 1500;
//            }
//            if (imageSize.equals(IMAGE_SIZE_WIDTH)) {
//                return 1500;
//            }
//            if (imageSize.equals(IMAGE_SIZE_EXTRAWIDTH)) {
//                return 1500;
//            }
//            if (imageSize.equals(IMAGE_SIZE_HEIGHT)) {
//                return 2500;
//            }
//            if (imageSize.equals(IMAGE_SIZE_EXTRAHEIGHT)) {
//                return 3500;
//            }
//        }
//        return 2000;
//    }
//
//    private int getImageWidth(String imageSize) {
//        if (StringUtils.isNotEmpty(imageSize)) {
//            if (imageSize.equals(IMAGE_SIZE_STANDARD)) {
//                return 2000;
//            }
//            if (imageSize.equals(IMAGE_SIZE_WIDTH)) {
//                return 3000;
//            }
//            if (imageSize.equals(IMAGE_SIZE_EXTRAWIDTH)) {
//                return 4000;
//            }
//            if (imageSize.equals(IMAGE_SIZE_HEIGHT)) {
//                return 2000;
//            }
//            if (imageSize.equals(IMAGE_SIZE_EXTRAHEIGHT)) {
//                return 2000;
//            }
//        }
//        return 2000;
//    }
//
//    private Log createLog(String role, String notebookId, String paragraphId, String message,
//                          boolean isProcessingSuccessful) {
//        Log log = new DefaultLog();
//        log.setCreator(role);
//        log.setNotebookId(notebookId);
//        log.setParagraphId(paragraphId);
//        log.setMessage(message);
//        log.setProcessingSuccessful(isProcessingSuccessful);
//        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
//        log.setProcessedAt(formatter.format(new Date()));
//        return log;
//    }
//
//    private FileExtension getFileExtension(Paragraph paragraph) {
//        if (paragraph.isRExpression() || paragraph.isDataExpression()) {
//            return FileExtension.JSON;
//        } else if (paragraph.isImageExpression()) {
//            return FileExtension.PNG;
//        } else if (paragraph.isRmdPdfExpression() || paragraph.isRnwPdfExpression()) {
//            return FileExtension.PDF;
//        }
//        return FileExtension.TXT;
//    }
//
//    private void writeToFile(Paragraph paragraph, String role, Map<String, String> pathVariables, byte[] output,
//                             boolean isProcessingSuccessful) throws IOException {
//        Path directoryPath = getDirectoryPath(paragraph.getNotebookId(), role, pathVariables);
//        Path filePath = getFilePath(directoryPath, paragraph.getId(), getFileExtension(paragraph));
//        Path logFilePath = getFilePath(directoryPath, paragraph.getId(), LOG);
//        Files.createDirectories(directoryPath);
//        String jsonLog;
//        if (isProcessingSuccessful) {
//            Files.write(filePath, output, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
//            jsonLog = WebUtils.toJson(
//                    this.createLog(role, paragraph.getNotebookId(), paragraph.getId(), "", isProcessingSuccessful));
//        } else {
//            jsonLog = WebUtils.toJson(this.createLog(role, paragraph.getNotebookId(), paragraph.getId(),
//                    new String(output), isProcessingSuccessful));
//        }
//        Files.write(logFilePath, jsonLog.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
//    }
//
//    public String getModelElementToJson(String notebookId, String paragraphId, String role, String extension,
//                                        Map<String, String> variables) {
//        Path directory = getDirectoryPath(notebookId, role, variables);
//        Path file = getFilePath(directory, paragraphId, map(extension));
//
//        Map<String, Object> fileMap = new HashMap<String, Object>();
//        fileMap.put(ID, getIdentifier(extension, notebookId, paragraphId, role, variables));
//        fileMap.put(PARAGRAPH_ID, paragraphId);
//        fileMap.put(FILENAME, file.getFileName().toString());
//        if (map(extension).equals(JSON) || map(extension).equals(TXT)) {
//            try {
//                fileMap.put(OUTPUT, WebUtils.getString(file));
//            } catch (IOException exception) {
//                fileMap.put(OUTPUT, "");
//            }
//        } else if (map(extension).equals(PNG)) {
//            try {
//                fileMap.put(OUTPUT, MIME_PNG_PREFIX + WebUtils.getBase64String(file));
//            } catch (IOException exception) {
//                fileMap.put(OUTPUT, "");
//            }
//        } else if (map(extension).equals(PDF)) {
//            try {
//                fileMap.put(OUTPUT, MIME_PDF_PREFIX + WebUtils.getBase64String(file));
//            } catch (IOException exception) {
//                fileMap.put(OUTPUT, "");
//            }
//        } else if (map(extension).equals(LOG)) {
//            try {
//                Gson gson = new Gson();
//                JsonReader reader = new JsonReader(new FileReader(file.toString()));
//                Log log = gson.fromJson(reader, DefaultLog.class);
//                fileMap.put(USER, log.getCreator());
//                fileMap.put(OUTPUT, log.getMessage());
//                fileMap.put(IS_PROCESSING_SUCCESSFUL, log.isProcessingSuccessful());
//                fileMap.put(PROCESSED_AT, log.getProcessedAt());
//            } catch (Exception exception) {
//                fileMap.put(IS_PROCESSING_SUCCESSFUL, true);
//                fileMap.put(OUTPUT, "No processing logs exists");
//            }
//        }
//        return WebUtils.toJson(fileMap);
//    }
//
//    private String getParagraphIdentifier(Paragraph paragraph) {
//        return PARAGRAPH_REFERENCE + paragraph.getId().substring(0, 5);
//    }
//
//    public Path getDirectoryPath(String notebookId, String role, Map<String, String> variables) {
//        String filterPathString = getFilterPathString(notebookId, role, variables, File.separator.toString());
//        Path directoryPath = Paths.get(fileService.getPrivateFolder() + filterPathString);
//        return directoryPath;
//    }
//
//    private String getDataExpression(Map<String, String> scopes, String expression) throws IOException {
//        Writer writer = new StringWriter();
//        MustacheFactory mf = new DefaultMustacheFactory();
//        Mustache mustache = mf.compile(new StringReader(expression), EXPRESSION);
//        mustache.execute(writer, scopes);
//        writer.flush();
//        return writer.toString();
//    }
}
