package no.uio.taco.pukaMatControl.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains calculation methods for metrics used
 * in the thesis. 
 * @author Cato Danielsen
 */
public class Evaluation {

	private enum State{
		TN, FP, TP, FN;
	}

	/**
	 * Corpus: all the potential results: all indexes in a signal
	 * Retrieved: the results from the analysis
	 * Relevant: reference result
	 * 
	 * @param result
	 * @param reference
	 * @param countTN - corpus of potential results
	 */
	public static EvaluationResult evaluateAnalysisResults(int[] result, int[] reference, int countTN) {
		
		/* Specific for precision and recall*/
		List<State> typeList = createEmptyTypeList(countTN);
		
		 
		setResultToFP(typeList, result); // set all result indexes to FP
		updateWithReference(typeList, reference); // set references to either TP or FN 
	
		double TPcount = 0, FPcount = 0, TNcount = 0, FNcount = 0, unknown = 0;
		int c = 0;
		for (State type : typeList) {
			switch (type) {
			
			case TP: TPcount++; break;
			case FP: FPcount++; break;
			case TN: TNcount++; break;
			case FN: FNcount++; break;
			default: unknown++; break; // debug
			
			
			}
			c++;
		}
		EvaluationResult evalResult = new EvaluationResult();


		// how many relevant items are selected 
		evalResult.recall = TPcount / (TPcount + FNcount);
		
		//How many selected items are relevant (according to reference)
		evalResult.precision = TPcount / (TPcount + FPcount);
		
		System.out.println("Precision: TP(" + TPcount +") / (TP(" + TPcount +") + FP(" + FPcount + ")) = " + evalResult.precision);
		System.out.println("Recall: TP(" + TPcount +") / (TP(" + TPcount + ") + FN(" + FNcount + ")) = " + evalResult.recall);
		
		return evalResult;
	}

	/**
	 * All indexes found in the analysis RESULT are initially set to FALSE POSITIVE
	 * @param typeMap
	 * @param resultIndexes
	 */
	private static void setResultToFP(List<State> typeList, int[] resultIndexes) {
		System.out.println(resultIndexes[0] + " <-----------------");
		System.out.println(typeList.get(0) + " <-----------------");
		for (int i = 0; i < resultIndexes.length; i++) {
			if (resultIndexes[i] == 0)
			{
				
				System.out.println(resultIndexes.length + " at: " + i + "  whoot?");
			}
			
			if (i > 0  && resultIndexes[i] == resultIndexes[i -1]) {
				continue;
			}
			typeList.set(resultIndexes[i], State.FP);
		}
		int count = 0;
		System.out.println(typeList.get(0) + " <-----------------");

	}

	/**
	 * We then look through the REFERECE analysis marking the corresponding indexes set to
	 * FALSE POSITIVE to TRUE POSITIVE, as they are a part of the reference set.
	 * When we find indexes set to TRUE NEGATIVE, these are changed to FALSE NEGATIVE, as they
	 * are a part of the REFERECE, but not found in the RESULT.
	 * @param typeList
	 * @param referenceIndexes
	 */
	private static void updateWithReference(List< State> typeList, int[] referenceIndexes) {
		
		for (int i = 0; i < referenceIndexes.length; i++) {
			
			if (i > 0 && referenceIndexes[i] == referenceIndexes[i - 1]) { // when there is no pause, set it once
				System.out.println("Single point, no pause detected");
				continue; 
			}

			switch (typeList.get(referenceIndexes[i])) {
				case FP: // All indexes that exist in RESULT set to FP - if the index exist in REFERENCES it is a TP
					System.out.println("What?! " + i + " " + referenceIndexes[i] + " " + typeList.get(referenceIndexes[i]));
					typeList.set(referenceIndexes[i], State.TP);
					break;
				case TN: // Not found in result -> ergo a false negative since it exist in reference
					typeList.set(referenceIndexes[i], State.FN);
					break;
				default:
					System.out.println("REF:\tError when updating index: " + referenceIndexes[i] + "\nState found in list + " + typeList.get(referenceIndexes[i]));
					break;
			}
		}
		
	}

	
	/**
	 * Create a map with keys 1 to 'countTN' with the State TRUE NEGATIVE.
	 * This is the corpus of indexes that can be marked by REFERENCE or RESULT
	 * @param countTN - signal length
	 * @return
	 */
	private static List<State> createEmptyTypeList(int countTN) {
		List<State> typeList = new ArrayList<State>(countTN);
		for (int i = 0; i < countTN; i++) {
			typeList.add(State.TN);
			
		}

		return typeList;
	}
	
	
	
}
