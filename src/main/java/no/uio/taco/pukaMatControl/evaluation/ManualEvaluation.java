package no.uio.taco.pukaMatControl.evaluation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ManualEvaluation {

	public static void main(String[] args) {
		
		
		postAnalysisEvaluation();
	}
	
	
	private static void postAnalysisEvaluation() {
		// read peaks
		int[] result = new int[0];
		int[] reference = new int[0];
		
		try {
			Path path = FileSystems.getDefault().getPath("matlabScripts", "data", "peaks.txt");
			List<String> peaksRes = new ArrayList<String>();
			peaksRes = Files.readAllLines(path, StandardCharsets.UTF_8);
			
			path = FileSystems.getDefault().getPath("matlabScripts", "data", "troughs.txt");
			List<String> troughsRes = new ArrayList<String>();
			troughsRes = Files.readAllLines(path, StandardCharsets.UTF_8);

			if (peaksRes.size() > 0 && troughsRes.size() > 0) {
				 
				int[] p = new int[peaksRes.size()];
				for (int i = 0; i < p.length; i++) {
					double d = Double.valueOf(peaksRes.get(i));
					p[i] = (int) d;
				}

				int[] t = new int[troughsRes.size()];
				for (int i = 0; i < p.length; i++) {
					double d = Double.valueOf(troughsRes.get(i));
					t[i] = (int) d; 
				}

				result = merge(p, t);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// read references
		try {
			Path path = FileSystems.getDefault().getPath("matlabScripts", "data", "pCleanRef.txt");
			List<String> peaksRef = new ArrayList<String>();
			peaksRef = Files.readAllLines(path, StandardCharsets.UTF_8);
			
			path = FileSystems.getDefault().getPath("matlabScripts", "data", "pCleanRef.txt");
			List<String> troughsRef = new ArrayList<String>();
			troughsRef = Files.readAllLines(path, StandardCharsets.UTF_8);

			if (peaksRef.size() > 0 && troughsRef.size() > 0) {

				int[] p = new int[peaksRef.size()];
				for (int i = 0; i < p.length; i++) {
					double d = Double.valueOf(peaksRef.get(i));
					p[i] = (int) d;
				}

				int[] t = new int[troughsRef.size()];
				for (int i = 0; i < p.length; i++) {
					double d = Double.valueOf(troughsRef.get(i));
					t[i] = (int) d; 
				}

				reference = merge(p, t);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (reference.length > 0 && result.length > 0) {
			EvaluationResult er = Evaluation.evaluateAnalysisResults(result, reference, 30000);
			System.out.println(er.toString());
		}
	}
	/**
	 * Merge two integer arrays and sort while doing so
	 * @param a
	 * @param b
	 * @return concatenated array
	 */
	private static int[] merge(int[] a, int[] b) {
	    int[] answer = new int[a.length + b.length];
	    int i = 0, j = 0, k = 0;
	    while (i < a.length && j < b.length)
	    {
	        if (a[i] < b[j])
	        {
	            answer[k] = a[i];
	            i++;
	        }
	        else
	        {
	            answer[k] = b[j];
	            j++;
	        }
	        k++;
	    }

	    while (i < a.length)
	    {
	        answer[k] = a[i];
	        i++;
	        k++;
	    }

	    while (j < b.length)
	    {
	        answer[k] = b[j];
	        j++;
	        k++;
	    }

	    return answer;
	}
}
