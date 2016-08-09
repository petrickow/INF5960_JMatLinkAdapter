package no.uio.taco.pukaMatControl.evaluation;

public class EvaluationResult {
	double precision;
	double recall;
	
	
	public String toString() {
		return "Precision: " +precision + "\nRecall: " + recall;
	}
}
