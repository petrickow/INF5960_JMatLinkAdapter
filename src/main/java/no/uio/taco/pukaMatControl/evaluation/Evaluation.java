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
		
		List<State> typeList = createEmptyTypeList(countTN);
		
		setResultToFP(typeList, result); // set all result indexes to FP
		updateWithReference(typeList, reference); // set 
	
		double TPcount = 0, FPcount = 0, TNcount = 0, FNcount = 0, unknown = 0;
		
		for (State e : typeList) {
			switch (e) {
			case TP: TPcount++; break;
			case FP: FPcount++; break;
			case TN: TNcount++; break;
			case FN: FNcount++; break;
			default: unknown++; break; // debug
			}
		}
		EvaluationResult e = new EvaluationResult();
		System.out.println("Recall: TP(" + TPcount +") / (TP(" + TPcount + ") + FN(" + FNcount + ")) = " + TPcount / (TPcount + FNcount));

		// how many relevant items are selected 
		e.recall = TPcount / (TPcount + FNcount);
		
		//How many selected items are relevant (according to reference)
		e.precision = TPcount / (TPcount + FPcount);
		System.out.println("Recall: TP(" + TPcount +") / (TP(" + TPcount +") + FP(" + FPcount + ")) = " + TPcount / (TPcount + FPcount));
		
		System.out.println(e.precision + " " + e.recall);
		return e;
	}

	/**
	 * All indexes found in the analysis RESULT are initially set to FALSE POSITIVE
	 * @param typeMap
	 * @param resultIndexes
	 */
	private static void setResultToFP(List<State> typeList, int[] resultIndexes) {
		for (int i = 0; i < resultIndexes.length; i++) {
			typeList.set(resultIndexes[i], State.FP);
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
				case FP: // All indexes that exist in RESULT set to FP - if the index exist in REFERENCES it is a TP
					typeList.set(referenceIndexes[i], State.TP);
					break;
				case TN: // Not found in result -> ergo a false negative since it exist in reference
					typeList.set(referenceIndexes[i], State.FN);
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
			typeList.add(State.TN);
		}
		return typeList;
	}
	
	
	
}
