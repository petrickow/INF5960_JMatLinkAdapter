package no.uio.taco.pukaMatControl.puka;

/*
 * HeartMeasures.java
 *
 * Created on January 6, 2003, 3:36 PM
 */

/**
 *
 * @author  Joset A. Etzel
 */
import java.math.BigDecimal; import java.util.ArrayList;

public class HeartMeasures {
	
	private static int subID;
	private static int sessionID;
	private static String clipID = "";
	private static BigDecimal heartRateMean;
	private static BigDecimal heartRateStdDev;
  private static BigDecimal RSAMean;
	private static BigDecimal RSAStdDev;
  private static BigDecimal RRMean;
  private static BigDecimal RRStdDev;
	private static boolean bolIsValid = false;
	private static String strComment = "";
	private static ArrayList peakList = new ArrayList();  //list of Integers to store R peak times
	private static int intNumRPeaks = 0;
	private static BigDecimal ShortestBeat;
	private static BigDecimal LongestBeat;
	private static BigDecimal RSAMin;
	private static BigDecimal RSAMax;
	private static int intRSACount = 0;
	private static ArrayList RSAList = new ArrayList();  //list to store RSA for each breath
	
	/** Creates a new instance of HeartMeasures */
	public HeartMeasures() { 	}
	
	static void setComment(String strComment1) { strComment = strComment1; }
	static String getComment() { return strComment; }
	
	static void setIsValid(boolean bolValid) { bolIsValid = bolValid; }
	static boolean getIsValid() { return bolIsValid; }
	
	static void setSubID(int intSubID) { subID = intSubID; }
	static int getSubID() { return subID; }
	
	static void setClipID(String strClipID) { clipID = strClipID; }
	static String getClipID() { return clipID; }
	
	static void setSessionID(int intSessionID) { sessionID = intSessionID; }
	static int getSessionID() { return sessionID; }

	static void setHeartRateMean(java.math.BigDecimal bdHeartRateMean) { heartRateMean = bdHeartRateMean; }
	static java.math.BigDecimal getHeartRateMean() { return heartRateMean; }

	static void setHeartRateStdDev(java.math.BigDecimal bdHeartRateStdDev) { heartRateStdDev = bdHeartRateStdDev; }
	static java.math.BigDecimal getHeartRateStdDev() { return heartRateStdDev; }

	static void setRSAMean(java.math.BigDecimal bdRSAMean) { RSAMean = bdRSAMean; }
	static java.math.BigDecimal getRSAMean() { return RSAMean; }

	static void setRSAStdDev(java.math.BigDecimal bdRSAStdDev) { RSAStdDev = bdRSAStdDev; }
	static java.math.BigDecimal getRSAStdDev() { return RSAStdDev; }

	static void setRRMean(java.math.BigDecimal bdRRMean) { RRMean = bdRRMean; }
	static java.math.BigDecimal getRRMean() { return RRMean; }

	static void setRRStdDev(java.math.BigDecimal bdRRStdDev) { RRStdDev = bdRRStdDev; }
	static java.math.BigDecimal getRRStdDev() { return RRStdDev; }

	static void setPeakList(ArrayList peakList1) { peakList = peakList1; }
	static ArrayList getPeakList() { return peakList; }

	static void setNumRPeaks(int intNumRPeaks1) { intNumRPeaks = intNumRPeaks1; }
	static int getNumRPeaks() { return intNumRPeaks; }
	
	static void setShortestBeat(java.math.BigDecimal bdShortestBeat) { ShortestBeat = bdShortestBeat; }
	static java.math.BigDecimal getShortestBeat() { return ShortestBeat; }

	static void setLongestBeat(java.math.BigDecimal bdLongestBeat) { LongestBeat = bdLongestBeat; }
	static java.math.BigDecimal getLongestBeat() { return LongestBeat; }

	static void setRSAMin(java.math.BigDecimal bdRSAMin) { RSAMin = bdRSAMin; }
	static java.math.BigDecimal getRSAMin() { return RSAMin; }

	static void setRSAMax(java.math.BigDecimal bdRSAMax) { RSAMax = bdRSAMax; }
	static java.math.BigDecimal getRSAMax() { return RSAMax; }

	static void setRSACount(int intRSACount1) { intRSACount = intRSACount1; }
	static int getRSACount() { return intRSACount; }

	static void setRSAList(ArrayList RSAList1) { RSAList = RSAList1; }
	static ArrayList getRSAList() { return RSAList; }
	
	static void BlankOutValues() {
		//set all of the things to zero
	
		subID = 0; sessionID = 0; clipID = ""; heartRateMean = new BigDecimal(0);
		heartRateStdDev = new BigDecimal(0); RSAMean = new BigDecimal(0);
		RSAStdDev = new BigDecimal(0); RRMean = new BigDecimal(0); 
		RRStdDev = new BigDecimal(0); bolIsValid = false; strComment = "";
		peakList = new ArrayList(); intNumRPeaks = 0; ShortestBeat = new BigDecimal(0);
		LongestBeat = new BigDecimal(0); RSAMin = new BigDecimal(0);
		RSAMax = new BigDecimal(0); intRSACount = 0; RSAList = new ArrayList(); 
	}
}
