package de.polipol.analytics.connect.data.semantic;

import static de.polipol.analytics.connect.data.DataType.CONTAINER;
import static de.polipol.analytics.connect.data.semantic.Label.NATIVESTRING_IDENTIFIER;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.polipol.analytics.connect.data.DataType;
import de.polipol.analytics.exception.InvalidLabelException;
import de.polipol.analytics.exception.InvalidReasonerException;
import de.polipol.analytics.exception.NoLabelExistsException;

public final class DefaultDictionary implements Dictionary {
	
	static final String DEFAULT_REASONER_CLASS = "de.polipol.analytics.connect.data.semantic.SimpleReasoner";
	private final Logger logger = LoggerFactory.getLogger(DefaultDictionary.class);
	protected Map<ColumnLabelKey, GroundedLabel> columnMap;
	protected Map<ContainerLabelKey, GroundedLabel> containerMap;
	protected Reasoner reasoner;

	public DefaultDictionary(final Map<ContainerLabelKey, GroundedLabel> containerMap,
			final Map<ColumnLabelKey, GroundedLabel> columnMap) throws InvalidReasonerException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super();
		this.containerMap = containerMap;
		this.columnMap = columnMap;
		this.reasoner = ReasonerFactory.createDefaultReasoner();
	}

	public DefaultDictionary(final String poolId, final Path dictionaryDirectory)
			throws InvalidReasonerException, InvalidLabelException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super();
		initialize(dictionaryDirectory, poolId);
		this.reasoner = ReasonerFactory.createDefaultReasoner();
	}

	public DefaultDictionary(final String poolId, final Path dictionaryDirectory, final Reasoner reasoner)
			throws InvalidReasonerException, InvalidLabelException {
		super();
		initialize(dictionaryDirectory, poolId);
		this.reasoner = reasoner;
	}

	private void initialize(final Path dictionaryDirectory, final String poolId) throws InvalidLabelException {
		Map<String, String> properties = readProperties(dictionaryDirectory, poolId);
		this.containerMap = new HashMap<ContainerLabelKey, GroundedLabel>();
		this.columnMap = new HashMap<ColumnLabelKey, GroundedLabel>();
		for (String key : properties.keySet()) {
			String elementString = properties.get(key);
			if (StringUtils.startsWith(key, CONTAINER_IDENTIFIER)) {
				String containerLabelString = key.substring(CONTAINER_IDENTIFIER.length(), key.length());
				GroundedLabel label = new DefaultGroundedLabel(containerLabelString, elementString, DataType.CONTAINER);
				this.containerMap.put(new DefaultContainerLabelKey(containerLabelString), label);
			} else {
				if (StringUtils.contains(key, CONTAINER_IDENTIFIER)) {
					String[] splittedKey = StringUtils.split(key, CONTAINER_IDENTIFIER);
					String columnLabelString = splittedKey[0];
					String containerLabelString = splittedKey[1];
					GroundedLabel label = new DefaultGroundedLabel(columnLabelString, elementString);
					this.columnMap.put(new DefaultColumnLabelKey(containerLabelString, columnLabelString), label);
				} else {
					String columnLabelString = key;
					GroundedLabel label = new DefaultGroundedLabel(columnLabelString, elementString);
					this.columnMap.put(new DefaultColumnLabelKey(columnLabelString), label);
				}
			}
		}
	}

	private boolean matches(Label label1, Label label2) {
		return reasoner.isSubsumedBy(label1, label2);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> readProperties(final Path dictionaryDirectory, final String poolId) {
		Properties properties = new Properties();
		@SuppressWarnings("rawtypes")
		Map mappings = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		String filename = poolId + "." + MAPPING_FileExtension;
		Path file = Paths.get(dictionaryDirectory + File.separator + filename);
		try {
			Reader reader = new FileReader(file.toFile());
			properties.load(reader);
			mappings.putAll(properties);
		} catch (IOException exception) {
			if (exception instanceof FileNotFoundException) {
				logger.info("dictionary file " + filename + " could not be found.");
			} else {
				logger.info("error while reading dictionary file.");
			}
		}
		return mappings;
	}

	@Override
	public List<GroundedLabel> resolveColumnLabels(final GroundedLabel groundedContainerLabel, final Label columnLabel)
			throws InvalidLabelException, NoLabelExistsException {
		List<GroundedLabel> resolvedColumnLabels = new ArrayList<GroundedLabel>();
		if (columnLabel.isNativeLabel()) {
			String nativeIdentifier = StringUtils.substring(columnLabel.getAnnotationString(),
					NATIVESTRING_IDENTIFIER.length());
			GroundedLabel resolvedColumnLabel = new DefaultGroundedLabel(nativeIdentifier, nativeIdentifier);
			resolvedColumnLabels.add(resolvedColumnLabel);
		} else {
			// case 1: containerLabel and columLabel matches
			for (Entry<ColumnLabelKey, GroundedLabel> entry : columnMap.entrySet()) {
				if (matches(entry.getKey().getContainerLabel(), groundedContainerLabel)
						&& matches(entry.getValue(), columnLabel)) {
					resolvedColumnLabels.add(entry.getValue());
				}
			}
			// case 2: columLabel matches and no containerLabel given
			if (resolvedColumnLabels.isEmpty()) {
				for (Entry<ColumnLabelKey, GroundedLabel> entry : columnMap.entrySet()) {
					if (matches(entry.getValue(), columnLabel) && entry.getKey().hasContainerWildcard()) {
						resolvedColumnLabels.add(entry.getValue());
					}
				}
			}
		}
		return resolvedColumnLabels;
	}

	@Override
	public List<GroundedLabel> resolveColumnLabels(final GroundedLabel groundedContainerLabel,
			final List<Label> columnLabels) throws InvalidLabelException, NoLabelExistsException {
		List<GroundedLabel> resolvedLabels = new ArrayList<GroundedLabel>();
		for (Label columnLabel : columnLabels) {
			resolvedLabels.addAll(resolveColumnLabels(groundedContainerLabel, columnLabel));
		}
		return resolvedLabels;
	}

	@Override
	public List<GroundedLabel> resolveContainerLabels(final Label containerLabel)
			throws InvalidLabelException, NoLabelExistsException {
		List<GroundedLabel> resolvedContainerLabels = new ArrayList<GroundedLabel>();
		if (containerLabel.isNativeLabel()) {
			String nativeIdentifier = StringUtils.substring(containerLabel.getAnnotationString(),
					NATIVESTRING_IDENTIFIER.length());
			GroundedLabel resolvedLabel = new DefaultGroundedLabel(nativeIdentifier, nativeIdentifier, CONTAINER);
			resolvedContainerLabels.add(resolvedLabel);
		} else {
			for (Entry<ContainerLabelKey, GroundedLabel> entry : containerMap.entrySet()) {
				if (matches(entry.getValue(), containerLabel)) {
					resolvedContainerLabels.add(entry.getValue());
				}
			}
		}
		if (resolvedContainerLabels.isEmpty()) {
			throw new NoLabelExistsException();
		}
		return resolvedContainerLabels;
	}
}
