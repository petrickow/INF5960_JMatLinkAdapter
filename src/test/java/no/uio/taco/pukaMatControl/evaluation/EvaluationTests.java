package no.uio.taco.pukaMatControl.evaluation;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class EvaluationTests {
//					[FP, TP, TP, TP, FP , TP ] 
	static int[] result = {10, 30, 50, 70, 100, 110};
//					  [FN, TP, TP, TP, FN, TP,  FN ]
	static int[] reference = {15, 30, 50, 70, 90, 100, 120};
	static int countTN = 150;
	
	
	@BeforeClass
	public static void setup() {
		System.out.println("Create test-data set "); 
		Evaluation.evaluateAnalysisResults(result, reference, countTN);
	}
		
	/**
	 */
	@Test
	public void CalculatePrecision() {
		
	}	
	
	@Test
	public void CalculateRecall() {
		
	}	
}
