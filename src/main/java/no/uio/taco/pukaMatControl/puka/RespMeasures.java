package no.uio.taco.pukaMatControl.puka;

/*
 * RespMeasures.java
 *
 * Created on January 1, 2003, 11:05 AM
 */

/**
 * a bunch of getters and setters storing the respiratory measure data for one subject & session
 * @author  Joset A. Etzel
 */
import java.math.BigDecimal; import java.util.ArrayList;

public class RespMeasures {
	
	private static java.math.BigDecimal totalBreathStdDev;
	private static java.math.BigDecimal totalBreathMean;	
	private static java.math.BigDecimal inspTimeMean;	
	private static java.math.BigDecimal inspTimeStdDev;
	private static java.math.BigDecimal expTimeMean;
	private static java.math.BigDecimal expTimeStdDev;
	private static java.math.BigDecimal postInspPauseMean;
	private static java.math.BigDecimal postInspPauseStdDev;
	private static java.math.BigDecimal postExpPauseMean;
	private static java.math.BigDecimal postExpPauseStdDev;
	private static java.math.BigDecimal inspDutyTimeMean;
	private static java.math.BigDecimal inspDutyTimeStdDev;
	private static java.math.BigDecimal respRateMean;
	private static java.math.BigDecimal respRateStdDev;
	private static int subID;
	private static int sessionID;
	private static String clipID = "";
	private static boolean bolIsValid = false;
	private static String strComment = "";
	private static ArrayList peakList = new ArrayList();  //list of Integers
	private static ArrayList troughList = new ArrayList();
	private static ArrayList peakPauseList = new ArrayList();
	private static ArrayList troughPauseList = new ArrayList();
	private static int intNumBreaths = 0;
	private static java.math.BigDecimal shortestBreath;
	private static java.math.BigDecimal longestBreath;	
	
	/** Creates a new instance of RespMeasures */
	public RespMeasures() { 	}
	
	static void setPeakList(ArrayList peakList1) { peakList = peakList1; }
	static ArrayList getPeakList() { return peakList; }
	
	static void setTroughList(ArrayList troughList1) { troughList = troughList1; }
	static ArrayList getTroughList() { return troughList; }
	
	static void setPeakPauseList(ArrayList peakPauseList1) { peakPauseList = peakPauseList1; }
	static ArrayList getPeakPauseList() { return peakPauseList; }
	
	static void setTroughPauseList(ArrayList troughPauseList1) { troughPauseList = troughPauseList1; }
	static ArrayList getTroughPauseList() { return troughPauseList; }
	
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
	
	static java.math.BigDecimal getTotalBreathMean() { return totalBreathMean; }
	static void setTotalBreathMean(BigDecimal bdTotalBreathMean) { totalBreathMean = bdTotalBreathMean; }
	
	static java.math.BigDecimal getTotalBreathStdDev() { return totalBreathStdDev; }
	static void setTotalBreathStdDev(BigDecimal bdTotalBreathStdDev) { totalBreathStdDev = bdTotalBreathStdDev; }
	
	static void setInspTimeMean(BigDecimal bdInspTimeMean) { inspTimeMean = bdInspTimeMean; }
	static java.math.BigDecimal getInspTimeMean() { return inspTimeMean; }
	
	static void setInspTimeStdDev(java.math.BigDecimal bdInspTimeStdDev) { inspTimeStdDev = bdInspTimeStdDev; }
	static java.math.BigDecimal getInspTimeStdDev() { return inspTimeStdDev; }
	
	static void setExpTimeMean(BigDecimal bdExpTimeMean) { expTimeMean = bdExpTimeMean; }
	static java.math.BigDecimal getExpTimeMean() { return expTimeMean; }
	
	static void setExpTimeStdDev(BigDecimal bdExpTimeStdDev) { expTimeStdDev = bdExpTimeStdDev; }
	static java.math.BigDecimal getExpTimeStdDev() { return expTimeStdDev; }
	
	static void setPostInspPauseMean(BigDecimal bdPass) { postInspPauseMean = bdPass; }
	static java.math.BigDecimal getPostInspPauseMean() { return postInspPauseMean; }
	
	static void setPostInspPauseStdDev(BigDecimal bdPostInspPauseStdDev) { postInspPauseStdDev = bdPostInspPauseStdDev; }
	static java.math.BigDecimal getPostInspPauseStdDev() { return postInspPauseStdDev; }
	
	static void setPostExpPauseMean(java.math.BigDecimal bdPostExpPauseMean) { postExpPauseMean = bdPostExpPauseMean; }
	static java.math.BigDecimal getPostExpPauseMean() { return postExpPauseMean; }
	
	static void setPostExpPauseStdDev(java.math.BigDecimal bdPostExpPauseStdDev) { postExpPauseStdDev = bdPostExpPauseStdDev; }
	static java.math.BigDecimal getPostExpPauseStdDev() { return postExpPauseStdDev; }
	
	static void setInspDutyTimeMean(BigDecimal bdPass) { inspDutyTimeMean = bdPass; }	
	static java.math.BigDecimal getInspDutyTimeMean() { return inspDutyTimeMean; }
	
	static void setInspDutyTimeStdDev(java.math.BigDecimal bdInspDutyTimeStdDev) { inspDutyTimeStdDev = bdInspDutyTimeStdDev; }
	static java.math.BigDecimal getInspDutyTimeStdDev() { return inspDutyTimeStdDev; }
	
	static void setRespRateMean(java.math.BigDecimal bdRespRateMean) { respRateMean = bdRespRateMean; }
	static java.math.BigDecimal getRespRateMean() { return respRateMean; }
	
	static void setRespRateStdDev(java.math.BigDecimal bdRespRateStdDev) { respRateStdDev = bdRespRateStdDev; }
	static java.math.BigDecimal getRespRateStdDev() { return respRateStdDev; }
	
	static void setNumBreaths(int intNumBreaths1) { intNumBreaths = intNumBreaths1; }
	static int getNumBreaths() { return intNumBreaths; }

	static void setShortestBreath(BigDecimal bdShortestBreath) { shortestBreath = bdShortestBreath; }	
	static java.math.BigDecimal getShortestBreath() { return shortestBreath; }

	static void setLongestBreath(BigDecimal bdLongestBreath) { longestBreath = bdLongestBreath; }	
	static java.math.BigDecimal getLongestBreath() { return longestBreath; }

	static void BlankOutValues() {
		//set all of the things to zero
		
		totalBreathStdDev = new BigDecimal(0); totalBreathMean = new BigDecimal(0);	
		inspTimeMean = new BigDecimal(0);	inspTimeStdDev = new BigDecimal(0); 
		expTimeMean = new BigDecimal(0); expTimeStdDev = new BigDecimal(0);
		postInspPauseMean = new BigDecimal(0); postInspPauseStdDev = new BigDecimal(0);
		postExpPauseMean = new BigDecimal(0); postExpPauseStdDev = new BigDecimal(0); 
		inspDutyTimeMean = new BigDecimal(0); inspDutyTimeStdDev = new BigDecimal(0); 
		respRateMean = new BigDecimal(0); respRateStdDev = new BigDecimal(0);
		subID = 0; sessionID = 0; clipID = ""; bolIsValid = false; strComment = "";
		peakList = new ArrayList(); troughList = new ArrayList();
		peakPauseList = new ArrayList(); troughPauseList = new ArrayList();
		intNumBreaths = 0; shortestBreath = new BigDecimal(0); longestBreath = new BigDecimal(0);	
	}
	
}
