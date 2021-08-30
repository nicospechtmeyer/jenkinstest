//package de.polipol.analytics.data.semantic;
//
//import static de.polipol.analytics.commons.TestConstants.TEST_KERNEL_ID;
//import static de.polipol.analytics.commons.TestConstants.TEST_RESOURCES_FOLDER;
//import static de.polipol.analytics.connect.data.DataType.CONTAINER;
//import static de.polipol.analytics.connect.data.semantic.Label.NATIVESTRING_IDENTIFIER;
//import static de.polipol.analytics.commons.TestConstants.COLUMN1;
//import static de.polipol.analytics.commons.TestConstants.COLUMN2;
//import static de.polipol.analytics.commons.TestConstants.CONTAINER1;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.notNullValue;
//import static org.hamcrest.Matchers.contains;
//import static org.hamcrest.Matchers.containsInAnyOrder;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import de.polipol.analytics.connect.data.semantic.ColumnLabelKey;
//import de.polipol.analytics.connect.data.semantic.ContainerLabelKey;
//import de.polipol.analytics.connect.data.semantic.DefaultColumnLabelKey;
//import de.polipol.analytics.connect.data.semantic.DefaultContainerLabelKey;
//import de.polipol.analytics.connect.data.semantic.DefaultDictionary;
//import de.polipol.analytics.connect.data.semantic.DefaultGroundedLabel;
//import de.polipol.analytics.connect.data.semantic.DefaultLabel;
//import de.polipol.analytics.connect.data.semantic.Dictionary;
//import de.polipol.analytics.connect.data.semantic.GroundedLabel;
//import de.polipol.analytics.connect.data.semantic.Label;
//import de.polipol.analytics.exception.InvalidLabelException;
//import de.polipol.analytics.exception.InvalidReasonerException;
//import de.polipol.analytics.exception.NoLabelExistsException;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public final class DictionaryTest {
//
//	private Label columnLabel1;
//	private Label columnLabel2;
//	private Label containerLabel1;
//	private Dictionary dictionary;
//	private Dictionary dictionaryFromFile;
//	private GroundedLabel groundedColumnLabel1;
//	private GroundedLabel groundedContainerLabel1;
//	private Label notRegisteredContainerLabel;
//
//	@Test
//	public void existsDictionary() {
//		assertThat(dictionary, is(notNullValue()));
//	}
//
//	@Test
//	public void existsDictionaryFromFile() {
//		assertThat(dictionaryFromFile, is(notNullValue()));
//	}
//
//	@Test
//	public void getContainerLabel() throws InvalidLabelException, NoLabelExistsException {
//		assertThat(dictionary.resolveContainerLabels(containerLabel1), contains(groundedContainerLabel1));
//	}
//
//	@Before
//	public void init() throws InvalidReasonerException, InvalidLabelException, IllegalArgumentException,
//			InvocationTargetException, NoSuchMethodException, SecurityException {
//		containerLabel1 = new DefaultLabel("containerLabel1");
//		notRegisteredContainerLabel = new DefaultLabel("notRegisteredContainerLabel");
//		columnLabel1 = new DefaultLabel("columnLabel1");
//		columnLabel2 = new DefaultLabel("columnLabel2");
//
//		groundedContainerLabel1 = new DefaultGroundedLabel("containerLabel1", CONTAINER1);
//		groundedColumnLabel1 = new DefaultGroundedLabel(columnLabel1, COLUMN1);
//		Map<ContainerLabelKey, GroundedLabel> containerMap = new HashMap<ContainerLabelKey, GroundedLabel>();
//		containerMap.put(new DefaultContainerLabelKey(CONTAINER1), groundedContainerLabel1);
//		Map<ColumnLabelKey, GroundedLabel> columnsMap = new HashMap<ColumnLabelKey, GroundedLabel>();
//		columnsMap.put(new DefaultColumnLabelKey(COLUMN1), groundedColumnLabel1);
//		columnsMap.put(
//				new DefaultColumnLabelKey(NATIVESTRING_IDENTIFIER + containerLabel1.getAnnotationString(), COLUMN2),
//				groundedColumnLabel1);
//		dictionary = new DefaultDictionary(containerMap, columnsMap);
//		dictionaryFromFile = new DefaultDictionary(TEST_KERNEL_ID, TEST_RESOURCES_FOLDER);
//	}
//
//	@Test
//	public void resolveColumnWithoutLabelDefinition() throws InvalidLabelException, NoLabelExistsException {
//		Label nativeColumnLabel = new DefaultLabel(NATIVESTRING_IDENTIFIER + COLUMN1);
//		GroundedLabel groundedNativeColumnLabel = new DefaultGroundedLabel(COLUMN1, COLUMN1);
//		List<GroundedLabel> resolvedColumnLabels = dictionary.resolveColumnLabels(groundedContainerLabel1,
//				nativeColumnLabel);
//		assertThat(resolvedColumnLabels, contains(groundedNativeColumnLabel));
//	}
//
//	@Test
//	public void resolveColumnWithoutLabelDefinition1() throws InvalidLabelException, NoLabelExistsException {
//		Label nativeColumnLabel = new DefaultLabel(NATIVESTRING_IDENTIFIER + COLUMN1);
//		GroundedLabel groundedNativeColumnLabel = new DefaultGroundedLabel(COLUMN1, COLUMN1);
//		List<Label> columnLabels = new ArrayList<Label>();
//		columnLabels.add(nativeColumnLabel);
//		columnLabels.add(columnLabel1);
//		List<GroundedLabel> resolvedColumnLabels = dictionary.resolveColumnLabels(groundedContainerLabel1,
//				columnLabels);
//		assertThat(resolvedColumnLabels, containsInAnyOrder(groundedColumnLabel1, groundedNativeColumnLabel));
//	}
//
//	@Test
//	public void resolveContainerWithoutLabelDefinition() throws InvalidLabelException, NoLabelExistsException {
//		Label nativeContainerLabel = new DefaultLabel(NATIVESTRING_IDENTIFIER + CONTAINER1);
//		GroundedLabel groundedNativeContainerLabel = new DefaultGroundedLabel(CONTAINER1, CONTAINER1, CONTAINER);
//		List<GroundedLabel> resolvedContainerLabels = dictionary.resolveContainerLabels(nativeContainerLabel);
//		assertThat(resolvedContainerLabels, contains(groundedNativeContainerLabel));
//	}
//
//	public void resolveNonExistingColumnLabel() throws InvalidLabelException, NoLabelExistsException {
//		List<GroundedLabel> resolvedColumnLabels = dictionary.resolveColumnLabels(groundedContainerLabel1,
//				columnLabel2);
//		assertThat(resolvedColumnLabels.size(), is(0));
//	}
//
//	@Test(expected = NoLabelExistsException.class)
//	public void resolveNonExistingContainerLabel() throws InvalidLabelException, NoLabelExistsException {
//		dictionary.resolveContainerLabels(notRegisteredContainerLabel);
//	}
//
//	@Test
//	public void resolveSpecificColumnNameByLabels() throws InvalidLabelException, NoLabelExistsException {
//		assertThat(dictionary.resolveColumnLabels(groundedContainerLabel1, columnLabel1),
//				contains(groundedColumnLabel1));
//	}
//
//	@Test
//	public void resolveSpecificColumnNameByLabelsWithoutContainerLabel()
//			throws InvalidLabelException, NoLabelExistsException {
//		List<GroundedLabel> resolvedColumnLabels = dictionary.resolveColumnLabels(groundedContainerLabel1,
//				columnLabel1);
//		assertThat(resolvedColumnLabels, contains(groundedColumnLabel1));
//	}
//}
