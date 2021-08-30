package de.polipol.analytics.commons.props;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import de.polipol.analytics.model.Interpreter;

public class InterpreterPropertiesReader extends AbstractPropertiesReader {

	private static final String DOT = ".";
	private static final String INTERPRETER = "interpreter";
	private static final String INTERPRETER_ORDERID = "app.orderId";
	private static final String INTERPRETER_TYPE = "app.type";
	private static final String INTERPRETER_VIEWTYPES = "app.viewTypes";
	private static final String INTERPRETER_TEXT = "app.text";
	private static final String INTERPRETER_INPUT_FORMATTER = "app.inputFormatter";
	private static final String INTERPRETER_ICON = "app.icon";

	private static final String INTERPRETER_OUTPUTTYPE = "outputType";
	private static final String KERNEL_ID = "kernelId";

	private static final String CONFIG = "config";
	public static final String PROPERTIES_FILENAME = "interpreter.properties";
	public static final Path PROPERTIES_FILE = Paths
			.get(System.getProperty("user.dir") + File.separator + CONFIG + File.separator + PROPERTIES_FILENAME);

	private List<Interpreter> interpreters;

	public InterpreterPropertiesReader() {
		super();
		this.interpreters = new ArrayList<>();
		this.file = PROPERTIES_FILE;
		init();
	}

	public InterpreterPropertiesReader(final Path file) {
		super();
		this.interpreters = new ArrayList<>();
		this.file = file;
		init();
	}

	@Override
	protected void evaluateProperties() {
		for (String interpreterName : getKernelNames()) {
			final String prefix = INTERPRETER + DOT + interpreterName + DOT;

			int orderId = 0;
			try {
				orderId = Integer.valueOf(this.properties.getProperty(prefix + INTERPRETER_ORDERID));
			} catch (Exception exception) {
			}
			final String text = this.properties.getProperty(prefix + INTERPRETER_TEXT);
			final String inputFormatter = this.properties.getProperty(prefix + INTERPRETER_INPUT_FORMATTER);
			final String type = this.properties.getProperty(prefix + INTERPRETER_TYPE);
			final List<String> viewTypes = Arrays
					.asList(StringUtils.split(properties.getProperty(prefix + INTERPRETER_VIEWTYPES), ","));
			final String icon = this.properties.getProperty(prefix + INTERPRETER_ICON);

			final String outputType = this.properties.getProperty(prefix + INTERPRETER_OUTPUTTYPE);
			final String kernelId = this.properties.getProperty(prefix + KERNEL_ID);

			if (CollectionUtils.isNotEmpty(viewTypes) && StringUtils.isNotEmpty(icon) && StringUtils.isNotEmpty(text)) {
				Interpreter interpreter = new Interpreter();
				interpreter.setId(interpreterName);
				interpreter.setOrderId(orderId);
				interpreter.setText(text);
				interpreter.setInputFormatter(inputFormatter);
				interpreter.setType(type);
				interpreter.setViewTypes(viewTypes);
				interpreter.setIcon(icon);
				if (StringUtils.isNotEmpty(kernelId)) {
					interpreter.setKernelId(kernelId);
				}
				if (StringUtils.isNotEmpty(outputType)) {
					interpreter.setOutputType(outputType);
				}
				interpreters.add(interpreter);
			}
		}
	}

	private List<String> getKernelNames() {
		List<String> interpreterNames = new ArrayList<String>();
		for (Object key : this.properties.keySet()) {
			String entry = (String) key;
			if (entry.startsWith(INTERPRETER) && entry.endsWith(INTERPRETER_TEXT)) {
				String interpreterName;
				interpreterName = entry.replace(INTERPRETER + DOT, "");
				interpreterName = interpreterName.replace(DOT + INTERPRETER_TEXT, "");
				interpreterNames.add(interpreterName);
			}
		}
		return interpreterNames;
	}

	public List<Interpreter> getKernels() {
		return this.interpreters;
	}
}