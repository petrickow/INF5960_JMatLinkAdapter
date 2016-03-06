package no.uio.taco.pukaMatControl.matControlAdapter;

import java.awt.Image;
import matlabcontrol.extensions.MatlabTypeConverter;
import matlabcontrol.*;
import matlabcontrol.extensions.MatlabNumericArray;

public class JMatLinkAdapter implements IJMatLink {

	MatlabProxyFactory factory;
	MatlabProxy proxy;
	
	
	/*** 
	 * Constructor does not take arguments, sets up a new instance
	 * of a matlabProxy if no other connection exists
	 */
	public JMatLinkAdapter() {
		MatlabProxyFactoryOptions options;
        options = new MatlabProxyFactoryOptions.Builder()
                .setHidden(true) // just the command window
                .build();
        
		factory = new MatlabProxyFactory(options);
	}
	
	/**
	 * New instance with existing connection
	 * @param engMatLab - existing session
	 */
	public JMatLinkAdapter(MatlabProxy proxy) {
		this.proxy = proxy; 
	}

	/**
	 * Disconnects and shuts down the matlab instance
	 */
	public void engClose() {
		proxy.disconnect(); // or proxy.exit()? 
	}

	/**
	 *  Uses MatlabProxyFactory created in constructor to get
	 *  an instance of a proxy to be used to communicate with matlab
	 *  @return Proxy object from MatlabProxyFactory 
	 */
	public void engOpen() {
		try {
			proxy = factory.getProxy();
		} catch (MatlabConnectionException e) {
			// TODO: Debug information
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	/**
	 * Passes instructions to MatLab using proxy.
	 * @param A string with the expression you wish to evaluate using matlab
	 */
	public void engEvalString(String evalS) {
		
		try {
			proxy.eval(evalS);
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block: Debuginformation if we crash
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a variable from matlab workspace.
	 */
	public double engGetScalar(String arrayS) {
		try {		
			double ret = ((double[]) proxy.getVariable(arrayS))[0];
			return (double) ret;
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block: Debuginformation if we crash
			e.printStackTrace();
		}
		return 0;
	}	
	
	/**
	 * 
	 */
	public double[][] engGetArray(String arrayS) {
		
		MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
		/*
		 * 
		 */
		try {
			MatlabNumericArray array = processor.getNumericArray(arrayS);
			double[][] javaArray = array.getRealArray2D();
			//Object o = proxy.getVariable(arrayS);
			//double[][] ret = ((double[]) proxy.getVariable(arrayS))[0];
			
			
			return javaArray;
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block: Debuginformation if we crash
			e.printStackTrace();
		}
		return null;
	}


	
	
	/* *********ENGPUTARRAY*********************************/
	public void engPutArray(String arrayS, double valueD) {
		
		try {
			proxy.setVariable(arrayS, valueD);
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
		
		
	}

	public void engPutArray(String arrayS, double[] valueD) {
		try {
			proxy.setVariable(arrayS, valueD);
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
		
	}

	public void engPutArray(String arrayS, double[][] valueDD) {
		try {
			proxy.setVariable(arrayS, valueDD);
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
	}	
	/* *****************************************************/
	
//*****************************************************************//
//	NOT IN USE - will not be prioritized
//*****************************************************************//	
	
	// Not used in puka, low priority
	public void engClose(long epI) {
		proxy.disconnect();
	}

	// Not used in puka, low priority
	public void engCloseAll() {
		// TODO Auto-generated method stub
		
	}

	// Not used in puka, low priority
	public void engEvalString(long epI, String evalS) {

	}

	// Not used in puka, low priority
	public double[][] engGetArray(long epI, String arrayS) {
		
		try {
			Object o = proxy.getVariable(arrayS);
			double[][] ret = (double[][]) o;
			return ret;
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block: Debuginformation if we crash
			e.printStackTrace();
		}
		return null;
	}

	// Not used in puka, low priority
	public String engGetCharArray(long epI, String arrayS) {
		return null;
	}

	// Not used in puka, low priority
	public String engGetCharArray(String arrayS) {
		return null;
	}

	// Not used in puka, low priority
	public Image engGetFigure(int figure, int dx, int dy) {
		return null;
	}

	// Not used in puka, low priority
	public Image engGetFigure(long epI, int figure, int dx, int dy) {
		return null;
	}

	// Not used in puka, low priority
	public String engGetOutputBuffer() {
		return null;
	}

	// Not used in puka, low priority
	public String engGetOutputBuffer(long epI) {
		return null;
	}

	// Not used in puka, low priority
	public double engGetScalar(long epI, String arrayS) {
		try {
			return ((double[]) proxy.getVariable(arrayS))[0];
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block: Debuginformation if we crash
			e.printStackTrace();
		}
		return 0;
	}

	// Not used in puka, low priority
	public boolean engGetVisible(long epI) {
		return false;
	}

	// Not used in puka, low priority
	public void engOpen(String startCmds) {
		
	}

	// Not used in puka, low priority
	public long engOpenSingleUse() {
		return 0;
	}

	// Not used in puka, low priority
	public long engOpenSingleUse(String startCmds) {
		return 0;
	}

	// Not used in puka, low priority
	public int engOutputBuffer() {
		return 0;
	}

	// Not used in puka, low priority
	public int engOutputBuffer(long epI) {
		return 0;
	}

	// Not used in puka, low priority
	public int engOutputBuffer(long epI, int buflenI) {
		return 0;
	}

	// Not used in puka, low priority
	public void engPutArray(long epI, String arrayS, double valueD) {
		
	}

	// Not used in puka, low priority
	public void engPutArray(long epI, String arrayS, double[] valueD) {
		
	}

	// Not used in puka, low priority
	public void engPutArray(long epI, String arrayS, double[][] valueDD) {
		
	}

	// Not used in puka, low priority
	public void engPutArray(String arrayS, int valueI) {
		
	}

	// Not used in puka, low priority
	public void engSetVisible(long epI, boolean visB) {

	}

	// Not used in puka, low priority
	public String getVersion() {
		return null;
	}

	public void kill() {
		/*From javadoc: 
		kill
		public void kill()
		obsolete method*/
	}


	public void setDebug(boolean debugB) {
		// TODO Auto-generated method stub
	}

	

}
