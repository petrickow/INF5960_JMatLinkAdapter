package no.uio.taco.pukaMatControl.matControlAdapter;

import java.awt.Image;
import matlabcontrol.extensions.MatlabTypeConverter;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.*;

public class JMatLinkAdapter implements IJMatLink {

	MatlabTypeConverter converter;
	MatlabProxyFactory factory;
	
	MatlabProxy proxy;
	MatlabProxy plainProxy; // this is used to keep a reference of the original proxy, in case we want to switch back to no debug mode...
	
	LoggingMatlabProxy logProxy;
	
	Boolean debug;
	
	/*** 
	 * Constructor does not take arguments, sets up a new instance
	 * of a matlabProxy if no other connection exists
	 */
	public JMatLinkAdapter() {
		MatlabProxyFactoryOptions options;
        options = new MatlabProxyFactoryOptions.Builder()
        		.setLogFile("matlabctrlLog.txt")
        		.setHidden(false) // just the command window
                .build();
        
		factory = new MatlabProxyFactory(options);
		
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
		if (proxy != null && proxy.isConnected()) {
			try {
				proxy.eval("clear;");
			} catch (MatlabInvocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				proxy = factory.getProxy();
				plainProxy = proxy; // keep a copy of the original proxy
				logProxy = new LoggingMatlabProxy(proxy); // for debug
				//helpers
				converter = new MatlabTypeConverter(proxy);
				setDebug(true);
				
			} catch (MatlabConnectionException e) {
				// TODO: Better error print
				e.printStackTrace();
				System.exit(1); // terminate execution
			}
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
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a variable from matlab workspace.
	 */
	public double engGetScalar(String arrayS) throws MatlabInvocationException  {
		// TODO, add evalSTring("isscalar(arrayS)");
		
		double ret = ((double[]) proxy.getVariable(arrayS))[0]; // MATLAB always thinks in arrays, a scalar is a double-array with one element 
		return (double) ret;
	}	
	
	/**
	 * Retrieves a matlab-typed array and converts it to a two dimensional Java array.
	 */
	public double[][] engGetArray(String arrayS) {
		try {
			MatlabNumericArray array = converter.getNumericArray(arrayS); // get numberic array
			double[][] javaArray = array.getRealArray2D(); // convert to java native type
			return javaArray;
			
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block: Debuginformation if we crash
			
			e.printStackTrace();
		}
		return null;
	}
	
	/* *********ENGPUTARRAY*********************************/
	/**
	 * Pro-guess, create an array with one element
	 */
	
	public void engPutArray(String arrayS, double valueD)  {
		try {
			proxy.setVariable(arrayS, valueD); 
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
	}

	public void engPutArray(String arrayS, double[] valueD) {
		try {
			// we expect [1][valD.length], but get [valD.len][1]
			proxy.setVariable(arrayS, valueD); // this has to be tested.
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
	}
	
	public void engPutArray(String arrayS, double[][] valueDD) {
		try {
			MatlabNumericArray mlArray = new MatlabNumericArray(valueDD, null); //Create matlab variable based on Java 2d array
			/* Matlab does not allow jagged arrays, convert to number array 
			 * to be sure that the array is ok with matlab 
			 */
			converter.setNumericArray(arrayS, mlArray);
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
	}	
	/* *****************************************************/
	
//*****************************************************************//
//	NOT IN USE IN PUKA - will not be prioritized
//*****************************************************************//	
	/**
	 * New instance with existing connection
	 * @param engMatLab - existing session
	 */
	public JMatLinkAdapter(MatlabProxy proxy) {
		this.proxy = proxy;
	}
	
	// Not used in puka, low priority
	public void engClose(long epI) {
		proxy.disconnect();
	}

	// Not used in puka, low priority
	public void engCloseAll() {

	}

	// Not used in puka, low priority
	public void engEvalString(long epI, String evalS) {

	}

	// Not used in puka, low priority
	public double[][] engGetArray(long epI, String arrayS) {
		
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
		// spin-wait, this has not been tested properly
		// no asynch calls to matlab
		while (proxy.isRunningInsideMatlab()) {
			try {
				wait(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			proxy.exit();
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void setDebug(boolean debugB) {
		if (debugB) {
			LoggingMatlabProxy.showInConsoleHandler();
			proxy = logProxy;
		}
		else
			proxy = plainProxy;
		debug = debugB;
	}
}
