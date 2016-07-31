package no.uio.taco.pukaMatControl.evaluation;

import java.util.HashMap;
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
		int countTP = 0;
		int countFP = 0;
		int countFN = 0;
		
		Map<Integer, State> typeMap = createEmptyTypeMap(countTN);
		
		
		setResultToFP(typeMap, result); // set all result indexes to FP
		updateWithReference(typeMap, reference); // set 
		
		
//		double precision = 
	}




	/**
	 * All indexes found in the analysis RESULT are initially set to FALSE POSITIVE
	 * @param typeMap
	 * @param resultIndexes
	 */
	private static void setResultToFP(Map<Integer, State> typeMap, int[] resultIndexes) {
		for (int i = 0; i < resultIndexes.length; i++) {
			typeMap.replace(resultIndexes[i], State.FP);
		}
		
	}

	/**
	 * We then look through the REFERECE analysis marking the corresponding indexes set to
	 * FALSE POSITIVE to TRUE POSITIVE, as they are a part of the reference set.
	 * When we find indexes set to TRUE NEGATIVE, these are changed to FALSE NEGATIVE, as they
	 * are a part of the REFERECE, but not found in the RESULT.
	 * @param typeMap
	 * @param referenceIndexes
	 */
	private static void updateWithReference(Map<Integer, State> typeMap, int[] referenceIndexes) {
		for (int i = 0; i < referenceIndexes.length; i++) {
			
			
			State s = typeMap.get(referenceIndexes[i]);
			
			switch (s) {
			case FP: // All indexes that exist in RESULT set to FP 
				typeMap.replace(referenceIndexes[i], State.TP);
				break;
			case TN: // Not found in result -> ergo a false negative since it exist in reference
				typeMap.replace(referenceIndexes[i], State.FN);
				break;
			default:
				// error!
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
	private static Map<Integer, State> createEmptyTypeMap(int countTN) {
		Map<Integer, State> typeMap = new HashMap<Integer, State>();
		for (int i = 1; i <= countTN; i++) {
			typeMap.put(new Integer(i), State.TN);
		}
		return typeMap;
	}
	
	
	
}
