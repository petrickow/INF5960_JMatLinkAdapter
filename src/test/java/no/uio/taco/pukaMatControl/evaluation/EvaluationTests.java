package no.uio.taco.pukaMatControl.evaluation;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class EvaluationTests {
// cheatsheet for results:									[FP, TP, TP, TP, FP , TP ] 
	final int[][] result = 	{{15, 30, 50, 70, 90, 100, 120},{10, 30, 50, 70, 100, 110}};
//					  		[FN, TP, TP, TP, FN, TP,  FN ]
	final int[] reference =	{15, 30, 50, 70, 90, 100, 120};
	final int countTN = 150;
	static int i;
	EvaluationResult e; 
	
	@BeforeClass
	public static void  setup() {
		System.out.println("Create test-data set "); 
		i = 0;
	}
	
	@Before
	public void beforeEach() {
		e = Evaluation.evaluateAnalysisResults(result[i++], reference, countTN);
	}
	/**
	 */
	@Test
	public void IdenticalArrayesGives100() {
		Assert.assertNotNull(e);
		assertTrue(e.precision == 1);
		assertTrue(e.recall == 1);
	}	
	
	@Test
	public void IsLowerThanOneWhenThereAreDifferences() {
		Assert.assertNotNull(e);
		assertTrue(e.precision < 1);
		assertTrue(e.recall < 1);
	}	
}
