package no.uio.taco.pukaMatControl.matControlAdapter;

import java.awt.*;

public interface IJMatLink {

	
	public void engClose();
	public void engClose(long epI);
	public void engCloseAll(); 

	public void engEvalString(long epI, String evalS);
	public void engEvalString(String evalS);
	
	public double[][] engGetArray(long epI, String arrayS);
	public double[][] engGetArray(String arrayS);
	public String engGetCharArray(long epI, String arrayS);
	public String engGetCharArray(String arrayS);
	
	/* Not used in puka... low priority*/
	public Image engGetFigure(int figure, int dx, int dy); 
	public Image engGetFigure(long epI, int figure, int dx, int dy);
	
	public String engGetOutputBuffer();
	public String engGetOutputBuffer(long epI);
	
	public double engGetScalar(long epI, String arrayS);
	public double engGetScalar(String arrayS); //?

	public boolean engGetVisible(long epI); // visibility-status of matlab window
	
	public void engOpen();
	public void engOpen(String startCmds);
	public long engOpenSingleUse();
	public long engOpenSingleUse(String startCmds);
	
	public int engOutputBuffer();
	public int engOutputBuffer(long epI);
	public int engOutputBuffer(long epI, int buflenI);
	
	public void engPutArray(long epI, String arrayS, double valueD);
	public void engPutArray(long epI, String arrayS, double[] valueD);
	public void engPutArray(long epI, String arrayS, double[][] valueDD);
	
	public void engPutArray(String arrayS, double valueD);
	public void engPutArray(String arrayS, double[] valueD);
	public void engPutArray(String arrayS, double[][] valueDD);

	public void engPutArray(String arrayS, int valueI);
	
	public void engSetVisible(long epI, boolean visB);
	
	public String getVersion();
	public void kill();
	public void setDebug(boolean debugB);
	
}
/*

	void	engClose() 
Close the connection to matlab.
	void	engClose(long epI) 
Close a specified connection to an instance of matlab.
	void	engCloseAll() 
Close all connections to matlab E.g.
	void	engEvalString(long epI, java.lang.String evalS) 
Evaluate an expression in a specified workspace.
	void	engEvalString(java.lang.String evalS) 
Evaluate an expression in matlab's workspace.
	double[][]	engGetArray(long epI, java.lang.String arrayS) 
Get an array from a specified instance/workspace of matlab.
	double[][]	engGetArray(java.lang.String arrayS) 
Get an array from matlab's workspace.
	java.lang.String[]	engGetCharArray(long epI, java.lang.String arrayS) 
Get an 'char' array (string) from matlab's workspace.
	java.lang.String[]	engGetCharArray(java.lang.String arrayS) 
Get an 'char' array (string) from matlab's workspace.
	java.awt.Image	engGetFigure(int figure, int dx, int dy) 
Return image of figure from Matlab
	java.awt.Image	engGetFigure(long epI, int figure, int dx, int dy) 
Return image of figure from Matlab
	java.lang.String	engGetOutputBuffer() 
Return the outputs of previous commands from a specified instance/ workspace form matlab.
	java.lang.String	engGetOutputBuffer(long epI) 
Return the ouputs of previous commands in matlab's workspace.
	double	engGetScalar(long epI, java.lang.String arrayS) 
Get a scalar value from a specified workspace.
	double	engGetScalar(java.lang.String arrayS) 
Get a scalar value from matlab's workspace.
	boolean	engGetVisible(long epI) 
return the visibility status of the Matlab window
	void	engOpen() 
Open engine.
	void	engOpen(java.lang.String startCmdS) 
Open engine.
	long	engOpenSingleUse() 
engOpenSingleUse ***************************** Open engine for single use.
	long	engOpenSingleUse(java.lang.String startCmdS) 
Open engine for single use.
	int	engOutputBuffer() 
Return the outputs of previous commands from matlab's workspace.
	int	engOutputBuffer(long epI) 
Return the outputs of previous commands from a specified instance/ workspace form matlab.
	int	engOutputBuffer(long epI, int buflenI) 
Return the ouputs of previous commands in matlab's workspace.
	void	engPutArray(long epI, java.lang.String arrayS, double valueD) 
Put an array into a specified instance/workspace of matlab.
	void	engPutArray(long epI, java.lang.String arrayS, double[] valuesD) 
Put an array (1 dimensional) into a specified instance/workspace of matlab.
	void	engPutArray(long epI, java.lang.String arrayS, double[][] valuesDD) 
Put an array (2 dimensional) into a specified instance/workspace of matlab.
	void	engPutArray(java.lang.String arrayS, double valueD) 
Put an array into matlab's workspace.
	void	engPutArray(java.lang.String arrayS, double[] valuesD) 
Put an array (1 dimensional) into a specified instance/workspace of matlab.
	void	engPutArray(java.lang.String arrayS, double[][] valuesDD) 
Put an array (2 dimensional) into matlab's workspace.
	void	engPutArray(java.lang.String arrayS, int valueI) 
Put an array into a specified workspace.
	void	engSetVisible(long epI, boolean visB) 
Set the visibility of the Matlab window
	java.lang.String	getVersion() 
Returns the current version of JMatLink E.g.
	void	kill() 
obsolete method
	void	setDebug(boolean debugB) 
Switch on or disable debug information printed to standard output.

*/