package no.uio.taco.pukaMatControl.matControlAdapter;

import java.awt.Image;

import matlabcontrol.*;

public class JMatLinkAdapter implements IJMatLink {

	MatlabProxyFactory factory;
	MatlabProxy engMatLab;
	
	
	/*** 
	 * Constructor does not take arguments, sets up a new instance
	 * of a matlabProxy if no other connection exists
	 */
	public JMatLinkAdapter() {
		
		if (engMatLab == null) {
			
		}
		else {
			
		}
	}
	
	/**
	 * New instance with existing connection
	 * @param engMatLab - existing session
	 */
	public JMatLinkAdapter(MatlabProxy engMatLab) {
		this.engMatLab = engMatLab; 
	}


	public void engClose() {
		// TODO Auto-generated method stub
		
	}


	public void engClose(long epI) {
		// TODO Auto-generated method stub
		
	}


	public void engCloseAll() {
		// TODO Auto-generated method stub
		
	}


	public void engEvalString(long epI, String evalS) {
		// TODO Auto-generated method stub
		
	}


	public void engEvalString(String evalS) {
		// TODO Auto-generated method stub
		
	}


	public void engEvalString() {
		// TODO Auto-generated method stub
		
	}


	public double[][] engGetArray(long epI, String arrayS) {
		// TODO Auto-generated method stub
		return null;
	}


	public double[][] engGetArray(String arrayS) {
		// TODO Auto-generated method stub
		return null;
	}


	public String engGetCharArray(long epI, String arrayS) {
		// TODO Auto-generated method stub
		return null;
	}


	public String engGetCharArray(String arrayS) {
		// TODO Auto-generated method stub
		return null;
	}


	public Image engGetFigure(int figure, int dx, int dy) {
		// TODO Auto-generated method stub
		return null;
	}


	public Image engGetFigure(long epI, int figure, int dx, int dy) {
		// TODO Auto-generated method stub
		return null;
	}


	public String engGetOutputBuffer() {
		// TODO Auto-generated method stub
		return null;
	}


	public String engGetOutputBuffer(long epI) {
		// TODO Auto-generated method stub
		return null;
	}


	public double engGetScalar(long epI, String arrayS) {
		// TODO Auto-generated method stub
		return 0;
	}


	public double engGetScalar(String arrayS) {
		// TODO Auto-generated method stub
		return 0;
	}


	public boolean engGetVisible(long epI) {
		// TODO Auto-generated method stub
		return false;
	}


	public void engOpen() {
		// TODO Auto-generated method stub
		
	}


	public void engOpen(String startCmds) {
		// TODO Auto-generated method stub
		
	}


	public long engOpenSingleUse() {
		// TODO Auto-generated method stub
		return 0;
	}


	public long engOpenSingleUse(String startCmds) {
		// TODO Auto-generated method stub
		return 0;
	}


	public int engOutputBuffer() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int engOutputBuffer(long epI) {
		// TODO Auto-generated method stub
		return 0;
	}


	public int engOutputBuffer(long epI, int buflenI) {
		// TODO Auto-generated method stub
		return 0;
	}


	public void engPutArray(long epI, String arrayS, double valueD) {
		// TODO Auto-generated method stub
		
	}


	public void engPutArray(long epI, String arrayS, double[] valueD) {
		// TODO Auto-generated method stub
		
	}


	public void engPutArray(long epI, String arrayS, double[][] valueDD) {
		// TODO Auto-generated method stub
		
	}


	public void engPutArray(String arrayS, double valueD) {
		// TODO Auto-generated method stub
		
	}


	public void engPutArray(String arrayS, double[] valueD) {
		// TODO Auto-generated method stub
		
	}


	public void engPutArray(String arrayS, double[][] valueDD) {
		// TODO Auto-generated method stub
		
	}


	public void engPutArray(String arrayS, int valueI) {
		// TODO Auto-generated method stub
		
	}


	public void engSetVisible(long epI, boolean visB) {
		// TODO Auto-generated method stub
		
	}


	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}


	public void kill() {
		// TODO Auto-generated method stub
		
	}


	public void setDebug(boolean debugB) {
		// TODO Auto-generated method stub
		
	}

	

}
