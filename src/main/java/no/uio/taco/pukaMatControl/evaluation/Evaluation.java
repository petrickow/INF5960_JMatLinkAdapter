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
class Evaluation {

	public enum State{
		TN, FP, TP, FN;
	}

	/**
	 * TODO: what shall we call this with?
	 */
	public static void evaluate() {
		
	}
	
	/**
	 * Corpus: all the potential results: all indexes in a signal
	 * Retrieved: the results from the analysis
	 * Relevant: reference result
	 */
	public static void calculateRecall(int[] result, int[] reference, int size) {
		
	}
	

	
	
	
	/**
	 * 
	 * @param result
	 * @param reference
	 * @param countTN
	 */
	public static void calculatePrecision(int[] result, int[] reference, int countTN) {
		
		List<State> typeList = createEmptyTypeList(countTN);
		
		setResultToFP(typeList, result); // set all result indexes to FP
		updateWithReference(typeList, reference); // set 
		
//		double precision = 
	}




	/**
	 * All indexes found in the analysis RESULT are initially set to FALSE POSITIVE
	 * @param typeMap
	 * @param resultIndexes
	 */
	private static void setResultToFP(List<State> typeList, int[] resultIndexes) {
		for (int i = 0; i < resultIndexes.length; i++) {
			typeList.add(resultIndexes[i], State.FP);
			
//			if (typeMap.replace(resultIndexes[i], State.FP) == null) { /*TODO: Handle error*/ System.out.println("RESULT:\tError when setting FP in map with key: " + i); }
		}
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
			switch (typeList.get(referenceIndexes[i])) {
				case FP: // All indexes that exist in RESULT set to FP
					typeList.add(referenceIndexes[i], State.FP);
					break;
				case TN: // Not found in result -> ergo a false negative since it exist in reference
					typeList.add(referenceIndexes[i], State.FN);
					break;
				default:
					System.out.println("REF:\tError when updating index: " + i + "\nState found in list + " + typeList.get(referenceIndexes[i]));
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
			typeList.add(i, State.TN);
		}
		return typeList;
	}
	
	
	
}
