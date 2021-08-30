//package de.polipol.analytics.data.semantic;
//
//import static de.polipol.analytics.commons.TestConstants.FORMAT_PATTERN;
//import static de.polipol.analytics.connect.data.semantic.Label.ELEMENTSTRING_DELIMITER;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
//import static org.hamcrest.CoreMatchers.notNullValue;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import java.lang.reflect.InvocationTargetException;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import de.polipol.analytics.connect.data.DataType;
//import de.polipol.analytics.connect.data.semantic.DefaultLabel;
//import de.polipol.analytics.connect.data.semantic.Label;
//import de.polipol.analytics.connect.data.semantic.Reasoner;
//import de.polipol.analytics.connect.data.semantic.ReasonerFactory;
//import de.polipol.analytics.exception.InvalidLabelException;
//import de.polipol.analytics.exception.InvalidReasonerException;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public final class LabelTest {
//
//	private Label label, equivalentLabel, labelWithSameMeaningAndSameContextAndDifferentValue,
//			labelWithSameMeaningAndDifferentContextAndSameValue, labelWithDifferentMeaningAndSameContextAndSameValue,
//			labelWithSameMeaningAndSameContextAndSameValueWithMultipleContext;
//	private Reasoner reasoner;
//
//	@Test
//	public void doesNotSubsumeLabelWithDifferentMeaningAndSameContextAndSameValue() {
//		assertThat(reasoner.isSubsumedBy(label, labelWithDifferentMeaningAndSameContextAndSameValue), not(true));
//	}
//
//	@Test
//	public void doesNotSubsumeLabelWithSameMeaningAndDifferentContextAndSameValue() {
//		assertThat(reasoner.isSubsumedBy(label, labelWithSameMeaningAndDifferentContextAndSameValue), not(true));
//	}
//
//	@Test
//	public void doesNotSubsumeLabelWithSameMeaningAndSameContextAndDifferentValue() {
//		assertThat(reasoner.isSubsumedBy(label, labelWithSameMeaningAndSameContextAndDifferentValue), not(true));
//	}
//
//	@Test
//	public void doesNotSubsumeLabelWithSameMeaningAndSameContextAndSameValueWithMultipleContext() {
//		assertThat(reasoner.isSubsumedBy(label, labelWithSameMeaningAndSameContextAndSameValueWithMultipleContext),
//				not(true));
//	}
//
//	@Before
//	public void init() throws InvalidReasonerException, InvalidLabelException, IllegalArgumentException,
//			InvocationTargetException, NoSuchMethodException, SecurityException {
//		reasoner = ReasonerFactory.createDefaultReasoner();
//		assertThat(reasoner, is(notNullValue()));
//		label = new DefaultLabel("meaning" + Label.ANNOTATION_DELIMITER + "context" + Label.CONTEXT_DELIMITER + "value"
//				+ ELEMENTSTRING_DELIMITER + DataType.NUMBER + ELEMENTSTRING_DELIMITER + FORMAT_PATTERN);
//		equivalentLabel = new DefaultLabel(
//				"meaning" + Label.ANNOTATION_DELIMITER + "context" + Label.CONTEXT_DELIMITER + "value");
//		labelWithSameMeaningAndSameContextAndDifferentValue = new DefaultLabel(
//				"meaning" + Label.ANNOTATION_DELIMITER + "context" + Label.CONTEXT_DELIMITER + "value1");
//		labelWithSameMeaningAndDifferentContextAndSameValue = new DefaultLabel(
//				"meaning" + Label.ANNOTATION_DELIMITER + "context1" + Label.CONTEXT_DELIMITER + "value");
//		labelWithDifferentMeaningAndSameContextAndSameValue = new DefaultLabel(
//				"meaning1" + Label.ANNOTATION_DELIMITER + "context" + Label.CONTEXT_DELIMITER + "value");
//		labelWithSameMeaningAndSameContextAndSameValueWithMultipleContext = new DefaultLabel(
//				"meaning" + Label.ANNOTATION_DELIMITER + "context" + Label.CONTEXT_DELIMITER + "value"
//						+ Label.ANNOTATION_DELIMITER + "context1" + Label.CONTEXT_DELIMITER + "value");
//	}
//
//	@Test
//	public void isEquivalentToLabelWithSameMeaningAndSameContextAndSameValue() {
//		assertThat(reasoner.isEquivalent(label, equivalentLabel), is(true));
//	}
//
//	@Test
//	public void isNotEquivalentToLabelWithDifferentMeaningAndSameContextAndSameValue() {
//		assertThat(reasoner.isEquivalent(label, labelWithDifferentMeaningAndSameContextAndSameValue), not(true));
//	}
//
//	@Test
//	public void isNotEquivalentToLabelWithSameMeaningAndDifferentContextAndSameValue() {
//		assertThat(reasoner.isEquivalent(label, labelWithSameMeaningAndDifferentContextAndSameValue), not(true));
//	}
//
//	@Test
//	public void isNotEquivalentToLabelWithSameMeaningAndSameContextAndDifferentValue() {
//		assertThat(reasoner.isEquivalent(label, labelWithSameMeaningAndSameContextAndDifferentValue), not(true));
//	}
//
//	@Test
//	public void isNotEquivalentToLabelWithSameMeaningAndSameContextAndSameValueWithMultipleContext() {
//		assertThat(reasoner.isEquivalent(label, labelWithSameMeaningAndSameContextAndSameValueWithMultipleContext),
//				not(true));
//	}
//
//	@Test
//	public void isNotSubsumedByLabelWithDifferentMeaningAndSameContextAndSameValue() {
//		assertThat(reasoner.isSubsumedBy(labelWithDifferentMeaningAndSameContextAndSameValue, label), not(true));
//	}
//
//	@Test
//	public void isNotSubsumedByLabelWithSameMeaningAndDifferentContextAndSameValue() {
//		assertThat(reasoner.isSubsumedBy(labelWithSameMeaningAndDifferentContextAndSameValue, label), not(true));
//	}
//
//	@Test
//	public void isNotSubsumedByLabelWithSameMeaningAndSameContextAndDifferentValue() {
//		assertThat(reasoner.isSubsumedBy(labelWithSameMeaningAndSameContextAndDifferentValue, label), not(true));
//	}
//
//	@Test
//	public void isSubsumedByLabelWithSameMeaningAndSameContextAndSameValue() {
//		assertThat(reasoner.isSubsumedBy(equivalentLabel, label), is(true));
//	}
//
//	@Test
//	public void isSubsumedByLabelWithSameMeaningAndSameContextAndSameValueWithMultipleContext() {
//		assertThat(reasoner.isSubsumedBy(labelWithSameMeaningAndSameContextAndSameValueWithMultipleContext, label),
//				is(true));
//	}
//
//	@Test
//	public void subsumesLabelWithSameMeaningAndSameContextAndSameValue() {
//		assertThat(reasoner.isSubsumedBy(label, equivalentLabel), is(true));
//	}
//}
