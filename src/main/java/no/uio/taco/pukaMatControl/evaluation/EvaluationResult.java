package no.uio.taco.pukaMatControl.evaluation;

public class EvaluationResult {
	double precision;
	double recall;
	
	
	public String toString() {
		return "P: " +precision + " R: " + recall;
	}
}
