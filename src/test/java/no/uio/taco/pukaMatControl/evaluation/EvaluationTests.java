package no.uio.taco.pukaMatControl.evaluation;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class EvaluationTests {
// cheat-sheet for results:									[FP, TP, TP, TP, FP , TP ] 
	final int[][] result = 	{{15, 30, 50, 70, 90, 100, 120},{10, 30, 50, 70, 100, 110}};
//					  		[FN, TP, TP, TP, FN, TP,  FN ]
	final int[] reference =	{15, 30, 50, 70, 90, 100, 120};
	final int countTN = 150;
	static int i;
	EvaluationResult evaluationResult; 
	
	@BeforeClass
	public static void  setup() {
		System.out.println("Create test-data set."); 
		i = 0;
	}
	
	@Before
	public void beforeEach() {
		evaluationResult = Evaluation.evaluateAnalysisResults(result[i++], reference, countTN);
	}
	/**
	 */
	@Test
	public void IdenticalArrayesGives100() {
		Assert.assertNotNull(evaluationResult);
		assertTrue(evaluationResult.precision == 1);
		assertTrue(evaluationResult.recall == 1);
	}	
	
	@Test
	public void IsLowerThanOneWhenThereAreDifferences() {
		Assert.assertNotNull(evaluationResult);
		assertTrue(evaluationResult.precision < 1);
		assertTrue(evaluationResult.recall < 1);
	}	
}
