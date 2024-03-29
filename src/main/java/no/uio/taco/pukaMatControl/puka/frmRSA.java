package no.uio.taco.pukaMatControl.puka;

/*
 * frmRSA.java
 *
 * Created on October 31, 2002, 1:54 PM
 */

/**
 *
 * @author  Joset A. Etzel
 */
import java.util.*; import javax.swing.table.*;

import matlabcontrol.MatlabInvocationException;

import javax.swing.*; import java.lang.Math; 
import java.math.BigDecimal; import java.io.*; import java.text.NumberFormat; import java.util.Locale;

/** GUI and code to calculate respiration, heart rate, and RSA measures.
 */
public class frmRSA extends javax.swing.JInternalFrame {
  double[][] dblGaps; double[][] dblPeakLocation; double[] dblNew; double[][] dblPeakType;
  double[] dblRWaves; int[] intNewType; int intFirstIndex = 0; double[] dblResp; 
	private static HeartMeasures rmHeartData;
  	
	/** Creates new form frmRSA */
    public frmRSA() { 
			initComponents(); 
					
			frmLoadData.engMatLab.engEvalString("ECG = data1(:, " + frmPreferences.getColEKG() + ");");
			frmLoadData.engMatLab.engEvalString("plot(ECG);");  //show the EKG signal so can check it
			
			rmHeartData = new HeartMeasures();  //get object ready to store 
			rmHeartData.BlankOutValues();  //set to zero, just in case
			rmHeartData.setClipID(frmLoadData.getClipName());  //get name of the clip from frmLoadData
		}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  private void initComponents() {//GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    jTabbedPane1 = new javax.swing.JTabbedPane();
    fraOne = new javax.swing.JPanel();
    cmdPrepare = new javax.swing.JButton();
    txtComment = new javax.swing.JTextArea();
    cmdInvalid = new javax.swing.JButton();
    lblComment = new javax.swing.JLabel();
    fraTwo = new javax.swing.JPanel();
    cmdHRV = new javax.swing.JButton();
    fraThree = new javax.swing.JPanel();
    cmdRSA = new javax.swing.JButton();
    txtResults = new javax.swing.JTextArea();
    fraFour = new javax.swing.JPanel();
    txtRSA = new javax.swing.JTextArea();
    cmdCancel = new javax.swing.JButton();

    getContentPane().setLayout(new java.awt.GridBagLayout());

    setClosable(true);
    setIconifiable(true);
    setMaximizable(true);
    setResizable(true);
    setTitle("RSA");
    jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
    jTabbedPane1.setPreferredSize(new java.awt.Dimension(450, 350));
    fraOne.setLayout(new java.awt.GridBagLayout());

    fraOne.setPreferredSize(new java.awt.Dimension(40, 300));
    cmdPrepare.setMnemonic('P');
    cmdPrepare.setText("Prepare EKG");
    cmdPrepare.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdPrepareActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
    fraOne.add(cmdPrepare, gridBagConstraints);

    txtComment.setRows(10);
    txtComment.setPreferredSize(new java.awt.Dimension(350, 170));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    fraOne.add(txtComment, gridBagConstraints);

    cmdInvalid.setMnemonic('I');
    cmdInvalid.setText("Invalid Signal");
    cmdInvalid.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdInvalidActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(10, 80, 0, 0);
    fraOne.add(cmdInvalid, gridBagConstraints);

    lblComment.setText("Comment");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    fraOne.add(lblComment, gridBagConstraints);

    jTabbedPane1.addTab("Step 1", fraOne);

    cmdHRV.setMnemonic('H');
    cmdHRV.setText("Calculate HRV");
    cmdHRV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdHRVActionPerformed(evt);
      }
    });

    fraTwo.add(cmdHRV);

    jTabbedPane1.addTab("Step 2", fraTwo);

    fraThree.setLayout(new java.awt.GridBagLayout());

    cmdRSA.setText("3. RSA calculation");
    cmdRSA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
			cmdRSAActionPerformed(evt);
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(10, 2, 0, 0);
    fraThree.add(cmdRSA, gridBagConstraints);

    txtResults.setPreferredSize(new java.awt.Dimension(320, 150));
    fraThree.add(txtResults, new java.awt.GridBagConstraints());

    jTabbedPane1.addTab("Step 3", fraThree);

    txtRSA.setLineWrap(true);
    txtRSA.setWrapStyleWord(true);
    txtRSA.setPreferredSize(new java.awt.Dimension(320, 200));
    fraFour.add(txtRSA);

    jTabbedPane1.addTab("Step 4", fraFour);

    getContentPane().add(jTabbedPane1, new java.awt.GridBagConstraints());

    cmdCancel.setMnemonic('C');
    cmdCancel.setText("Close");
    cmdCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdCancelActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
    getContentPane().add(cmdCancel, gridBagConstraints);

    pack();
  }//GEN-END:initComponents

	private void cmdInvalidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInvalidActionPerformed
		//sets the respiration object to have isValid = false, but still can have a comment
		int intResponse = 0;
		
		//double check that they really want it to be invalid
		intResponse = JOptionPane.showConfirmDialog(null, "Are you sure that you want to mark this signal as invalid?", "Confirm Invalid", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (intResponse == JOptionPane.NO_OPTION) { return; }
		
		rmHeartData.setIsValid(false);  //set to invalid
		rmHeartData.setComment(txtComment.getText());  //assign comment, if any
		
		try { this.setClosed(true); }  //close the form
		catch(java.beans.PropertyVetoException e) { e.printStackTrace(); }
	}//GEN-LAST:event_cmdInvalidActionPerformed

	private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
		//just close the form
		try { this.setClosed(true); } 
		catch(java.beans.PropertyVetoException e) { e.printStackTrace(); }
	}//GEN-LAST:event_cmdCancelActionPerformed

	private void cmdPrepareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrepareActionPerformed
		int intTemp = 0;

		rmHeartData.setIsValid(true);  //set to valid
		rmHeartData.setComment(txtComment.getText());  //assign comment, if any
		
		intTemp = ReWriteECG(frmPreferences.getColEKG());  //make a temp file of the ecg column from the data
		System.out.println("result from ReWriteECG(): " + intTemp);
		intTemp = RunConvertECG(); //converts ecg.txt into wfdb format
		System.out.println("result from RunConvertECG(): " + intTemp);		

		intTemp = RunECGPUWAVE(); //run ecgpuwave (marks ECG waveforms)
		System.out.println("result from RunECGPUWAVE(): " + intTemp);		
		intTemp = RunRDANN();    //read the annotation created by ecgpuwave into an external file
		System.out.println("result from RunRDANN(): " + intTemp);
		intTemp = ReadRDANNOutput();  //read the beats from the external file created by RunRDANN() into an array
		System.out.println("result from ReadRDANNOutput(): " + intTemp);
		intTemp = RunANN2RR();  //create a RR interval series
		System.out.println("result from RunANN2RR(): " + intTemp);
		intTemp = RunIHR();  //create a instantaneous heart rate series
		System.out.println("result from RunIHR(): " + intTemp);
		
		jTabbedPane1.setSelectedIndex(1);  //set next panel on top
	}//GEN-LAST:event_cmdPrepareActionPerformed

	private int RunConvertECG() {
	  //convert the file from raw text to wfdb format 
		//puts the headers into SunOne's working directory!
		String strCmd = ""; int intExit = 0;

		strCmd = frmPreferences.strConvertECGPath + "convertECG.exe";
		System.out.println("strCmd in RunConvertECG: " + strCmd);
		try{            
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe" ; cmd[1] = "/C" ;
			cmd[2] = strCmd;  //put my command into the end so executed as if it was at a c prompt*/
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);
			
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR"); //capture error messages      
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");  //capture output messages
      errorGobbler.start(); outputGobbler.start();  //start the readers
			OutputStreamWriter jcStream = new OutputStreamWriter(proc.getOutputStream(), "US-ASCII");
   
			jcStream.write("temp" + "\n"); jcStream.flush(); //record name
			jcStream.write(frmPreferences.strSampleFreq + "\n"); jcStream.flush(); //sampling frequency, Hz per signal
			jcStream.write(frmPreferences.strLength + "\n"); jcStream.flush(); //length of record, H:M:S
			jcStream.write(frmPreferences.strWFDBDataPath + "\n"); jcStream.flush(); //directory for signal files; 30 char max
			jcStream.write(frmPreferences.getInstallPath() + "ecg.txt\n"); jcStream.flush(); //directory for input files
			jcStream.write("temporary file" + "\n"); jcStream.flush(); //signal description
			jcStream.write(frmPreferences.strSignalUnits + "\n"); jcStream.flush(); //signal units
			jcStream.write(frmPreferences.strSignalGain + "\n"); jcStream.flush();  //signal gain (adu/mV)
			jcStream.write(frmPreferences.strADCResolution + "\n"); jcStream.flush();  //ADC resolution in bits
			jcStream.write(frmPreferences.strADCZero + "\n"); jcStream.flush();  //ADC zero level (adu)

      intExit = proc.waitFor();  //get processes' exit value to check for errors
      System.out.println("ExitValue: " + intExit);  //if intExit <> 0 then there was an error
		} catch (Throwable t) { t.printStackTrace(); if (intExit == 0){ intExit = 1; } }
		return intExit;
	}
	
	private int RunECGPUWAVE() {
		//sends the record ecgTmp created by ConvertECG to ecgpuwave for waveform analysis
		String strCmd = ""; int intExit = 0; 
		
		strCmd = frmPreferences.getInstallPath() + "ecgpuwave -r temp -a atr";
		System.out.println("strCmd in RunECGPUWAVE: " + strCmd);
		
		try{            
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe" ; cmd[1] = "/C" ;
			cmd[2] = strCmd;  //put my command into the end so executed as if it was at a c prompt
			Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(cmd);
            
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR"); //capture error messages      
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");  //capture output messages

      errorGobbler.start(); outputGobbler.start();  //start the readers
      intExit = proc.waitFor();  //get processes' exit value to check for errors
      System.out.println("ExitValue: " + intExit);  //if intExit <> 0 then there was an error
		} catch (Throwable t) { t.printStackTrace(); if( intExit == 0 ){ intExit = 1; } }
		return intExit;
	}
	
	private int RunRDANN() {
	  //read the annotation created by ecgpuwave into an external file
		/* code adapted from:
		http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
		When Runtime.exec() won't: Navigate yourself around pitfalls related to the Runtime.exec() method
		@author Michael Daconta */
		String strCmd = ""; int intExit = 0;
		
		strCmd = frmPreferences.strWFDBPath + "rdann -a atr -r " + frmPreferences.getInstallPath() + "temp >" + frmPreferences.getInstallPath() + "ecgOut.txt";
		try{            
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe" ; cmd[1] = "/C" ;
			cmd[2] = strCmd;  //put my command into the end so executed as if it was at a c prompt
						
			Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(cmd);
            
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR"); //capture error messages      
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");  //capture output messages

      errorGobbler.start(); outputGobbler.start();  //start the readers
      intExit = proc.waitFor();  //get processes' exit value to check for errors
      System.out.println("ExitValue: " + intExit);  //if intExit <> 0 then there was an error
		} catch (Throwable t) { t.printStackTrace(); if( intExit == 0 ){ intExit = 1; } }
		
		return intExit;
	}
	
	private int RunANN2RR() {
		//use ann2rr from the wfdb code to create an interval series.  
		//the -i s8 option has the output in seconds with 8 decimal places - NOT sample units
		String strCmd = ""; int intExit = 0; int intStopTime = 0;
		
		strCmd = frmPreferences.strWFDBPath + "ann2rr -r " + frmPreferences.getInstallPath() + 
		 "temp -a atr -i s8 -f s" + frmLoadData.getStartTime() + " -t s" + frmLoadData.getStopTime() + 
		 " > " + frmPreferences.getInstallPath() + "rrOut.txt";
		System.out.println("strCmd in RunANN2RR: " + strCmd);	
		try{            
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe" ; cmd[1] = "/C" ;
			cmd[2] = strCmd;  //put my command into the end so executed as if it was at a c prompt
			Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(cmd);         
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR"); //capture error messages      
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");  //capture output messages
      errorGobbler.start(); outputGobbler.start();  //start the readers
      intExit = proc.waitFor();  //get processes' exit value to check for errors
      System.out.println("ExitValue: " + intExit);  //if intExit <> 0 then there was an error
		} catch (Throwable t) { t.printStackTrace(); if( intExit == 0 ){ intExit = 1; } }
		
		return intExit;
	}
	
		private int RunIHR() {
		//use ihr from the wfdb code to create a instantaneous heart rate series
		String strCmd = ""; int intExit = 0; int intStopTime = 0;
		
		strCmd = frmPreferences.strWFDBPath + "ihr -r " + frmPreferences.getInstallPath() + 
		 "temp -a atr -f s" + frmLoadData.getStartTime() + " -t s" + frmLoadData.getStopTime() + 
		 " > " + frmPreferences.getInstallPath() + "temp.ihr";
		System.out.println("strCmd in IHR: " + strCmd);	
		try{            
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe" ; cmd[1] = "/C" ;
			cmd[2] = strCmd;  //put my command into the end so executed as if it was at a c prompt
			Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(cmd);         
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR"); //capture error messages      
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");  //capture output messages
      errorGobbler.start(); outputGobbler.start();  //start the readers
      intExit = proc.waitFor();  //get processes' exit value to check for errors
      System.out.println("ExitValue: " + intExit);  //if intExit <> 0 then there was an error
		} catch (Throwable t) { t.printStackTrace(); if( intExit == 0 ){ intExit = 1; } }
		
		return intExit;
	}
	
	private int ReadRDANNOutput() {
		//read the beats from the external file created by RunRDANN() into an array
		int intTemp = 0; File fileRDANN; String strPath = ""; StringTokenizer joTokenizer; String strInput = "";
		String strTemp = ""; String strData = ""; ArrayList jcBeats = new ArrayList(); double[] dblBeats;
		Integer IntBeat; int intC = 0; Integer IntData; 

		strPath = frmPreferences.getInstallPath() + "ecgOut.txt";
		fileRDANN = new File(strPath);  //make the File object on the file created by rdann in RunRDANN()
		if (fileRDANN.isFile() == false) { JOptionPane.showMessageDialog(null, "The file is not valid.", "File Validation Error", JOptionPane.ERROR_MESSAGE); return -1; }
		
		try {  
      FileReader joFReader = new FileReader(fileRDANN.getPath());  //reader to connect to the file
      BufferedReader joBReader = new BufferedReader(joFReader);  //another reader

			while( (strInput = joBReader.readLine()) != null ) {
				joTokenizer = new StringTokenizer(strInput);  //read strInput w/ tab, space, & return delimiters
				strTemp = joTokenizer.nextToken();  //don't care about the first column - times
				strData = joTokenizer.nextToken();  //this column is the data pts at the marked events
				strTemp = joTokenizer.nextToken();	//this column is the data flags; don't care about rest of columns.
				if (strTemp.equals("N") == true) {  //a R peak, so save
					IntData = new Integer(strData);	  //only copy beats during clip time to matlab
					if ((IntData.intValue() >= frmLoadData.getStartTime()) & (IntData.intValue() <= (frmLoadData.getTotalTime() + frmLoadData.getStartTime()))) 
						{ jcBeats.add(IntData); }
				}  
			}
			joFReader.close(); joBReader.close();  //get rid of readers
			
			dblBeats = new double[jcBeats.size()];  //put beats into the intBeats array instead of the list
			for(intC = 0;intC < jcBeats.size();intC++) { IntBeat = (Integer)jcBeats.get(intC); dblBeats[intC] = IntBeat.intValue(); }

		frmLoadData.engMatLab.engPutArray("RR", dblBeats);  //send clean array to matlab
		frmLoadData.engMatLab.engEvalString("cd ('" + frmPreferences.getInstallPath() + "\\matlabScripts')");  //switch just in case
		frmLoadData.engMatLab.engEvalString("plotECGwithR");  //plot ECG signal with R peaks marked
		rmHeartData.setPeakList(jcBeats);  //save list of R peaks in data object
		
		}catch (Exception e) { JOptionPane.showMessageDialog(null, "The file is not valid.", "Serious Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); intTemp = -1; }
	return intTemp;
	}
	
	private int ReWriteECG(int intECGCol) {
		//parse the ECG column out of the raw data file and create an external file for reading into wfdb format
		File fileRaw; int intTemp = 0; StringTokenizer jcTokenizer; String strInput = ""; int intCCols = 0;
		File fileOut; String strToken = "";
		
		fileRaw = new File(frmLoadData.getDataFilePath());
		if (fileRaw.isFile() == false) { JOptionPane.showMessageDialog(null, "The raw data file is not valid.", "Serious Error", JOptionPane.ERROR_MESSAGE); return -1; }
		fileOut = new File(frmPreferences.getInstallPath() + "ecg.txt");
		
		try {  
      FileReader jcFReader = new FileReader(fileRaw.getPath());  //reader to connect to the file
      BufferedReader jcBReader = new BufferedReader(jcFReader);  //another reader
			FileWriter jcOut = new FileWriter(fileOut);  //writer to create the ECG column temp file

			while( (strInput = jcBReader.readLine()) != null ) {
				jcTokenizer = new StringTokenizer(strInput);  //read strInput w/ tab, space, & return delimiters
				intCCols = 1;  //reset column counter
				while( jcTokenizer.hasMoreTokens() == true ) {  //iterate over the line looking for the ECG column
					strToken = jcTokenizer.nextToken();
					if( intCCols == intECGCol ) { jcOut.write(strToken + "\n"); }
					intCCols++;
				}
				strInput = "";
			}
			jcFReader.close(); jcBReader.close(); jcOut.close();  //get rid of readers
			
    }catch (Exception e) { JOptionPane.showMessageDialog(null, "The file is not valid.", "Serious Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); intTemp = -1; }

	return intTemp;
	}

	private void cmdHRVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHRVActionPerformed
			String strInstallPath = "";
			
			strInstallPath = frmPreferences.getInstallPath();
			frmLoadData.engMatLab.engEvalString("load " + strInstallPath + "rrOut.txt" + ";");
			frmLoadData.engMatLab.engEvalString("rrIntervals = rrOut';");
			frmLoadData.engMatLab.engEvalString("load " + strInstallPath + "temp.ihr" + ";");
			frmLoadData.engMatLab.engEvalString("ihr = temp(:,2)';");
			
			try {
				CalculateHRV();
			} catch (MatlabInvocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  //call sub to display HRV statistics
			
			jTabbedPane1.setSelectedIndex(2);  //set next panel on top
	}//GEN-LAST:event_cmdHRVActionPerformed
		
		 private void CalculateHRV() throws MatlabInvocationException {
      //sub does calculations in matlab on the rrIntervals array (R peaks) to find basic HRV statistics during the stimulus
			//RR array in matlab only has the peaks that happened during the clip time
      double dblTemp = 0; BigDecimal jcBigDec;
					
			NumberFormat jcNumberFormat = NumberFormat.getInstance(Locale.US);  //set up NumberFormat for the USA
			jcNumberFormat.setMaximumFractionDigits(4);  //only two digits after the decimal shown
		
      frmLoadData.engMatLab.engEvalString("length(rrIntervals);");  //# of R peaks during song
      dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			rmHeartData.setNumRPeaks((int)dblTemp);	
      txtResults.append("total number of R peaks: " + dblTemp + "\n");
			
      frmLoadData.engMatLab.engEvalString("min(rrIntervals);");  //shortest beat
      dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setShortestBeat(jcBigDec);	
			txtResults.append("shortest beat (sec): " + jcNumberFormat.format(dblTemp) + "\n");
			
      frmLoadData.engMatLab.engEvalString("max(rrIntervals);");  //longest beat
      dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setLongestBeat(jcBigDec);		
			txtResults.append("longest beat (sec): " + jcNumberFormat.format(dblTemp) + "\n");
			
      frmLoadData.engMatLab.engEvalString("mean(rrIntervals);");  //mean RR interval
      dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setRRMean(jcBigDec);		
      txtResults.append("RR interval mean (sec): " + jcNumberFormat.format(dblTemp) + "\n");

      frmLoadData.engMatLab.engEvalString("std(rrIntervals);");  //RR standard deviation
      dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
      jcBigDec = new BigDecimal(dblTemp); rmHeartData.setRRStdDev(jcBigDec);
      txtResults.append("RR interval std dev (sec): " + jcNumberFormat.format(dblTemp) + "\n");
			
      frmLoadData.engMatLab.engEvalString("mean(ihr);");  //mean heart rate
      dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setHeartRateMean(jcBigDec);		
      txtResults.append("mean instantaneous heart rate (beats/min): " + jcNumberFormat.format(dblTemp) + "\n");
			
      frmLoadData.engMatLab.engEvalString("std(ihr);");  // heart rate std dev
      dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setHeartRateStdDev(jcBigDec);		
      txtResults.append("IHR std dev (beats/min): " + jcNumberFormat.format(dblTemp) + "\n");
    }
    
    private double FindCR(double dblTimePt) {
      // searches through dblRWaves and returns the index of the largest time pt in dblRWaves that's smaller 
      // than dblTimePt.  Called by cmdRSAActionPerformed.
      int intC = 0;
      
      while (dblRWaves[intC] < (dblTimePt)) { intC++; }
      return (intC - 1);  //subtract 1 to get the index of the largest time pt SMALLER than dblTimePt
    }
    
    private void cmdRSAActionPerformed(java.awt.event.ActionEvent evt) throws MatlabInvocationException {//GEN-FIRST:event_cmdRSAActionPerformed
			// use matlab to calculate the RSA and show the result in puka
			double dblTemp = 0; BigDecimal jcBigDec; RespMeasures rmData; double[][] dblRSA; 
			int intC = 0; ArrayList jcTempList = new ArrayList();

			//check if the respiration data is available - have to have it before RSA calculations
			rmData = frmRespiration.getRespDataObject();  //get the object from frmRSA
			if(rmData == null) { JOptionPane.showMessageDialog(null, "You must analyze the respiration signal first.", "Respiration not found", JOptionPane.ERROR_MESSAGE); return; }
			
			frmLoadData.engMatLab.engEvalString("[RSA, saveDiffRwin, saveDiffNum, firstR, lastR] = calculateRSA(troughs, RR, ECG, y);");  //generates RSA variable in matlab
			NumberFormat jcNumberFormat = NumberFormat.getInstance(Locale.US);  //set up NumberFormat for the USA
			jcNumberFormat.setMaximumFractionDigits(4);  //only two digits after the decimal shown
		
			frmLoadData.engMatLab.engEvalString("mean(RSA);");  //mean RSA calculation
			dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			txtRSA.append("mean RSA: " + jcNumberFormat.format(dblTemp) + "\n");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setRSAMean(jcBigDec);
			
			frmLoadData.engMatLab.engEvalString("std(RSA);");  //std deviation RSA calculation
			dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			txtRSA.append("RSA standard deviation: " + jcNumberFormat.format(dblTemp) + "\n");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setRSAStdDev(jcBigDec);

			frmLoadData.engMatLab.engEvalString("min(RSA);");  //smallest RSA 
			dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setRSAMin(jcBigDec);
			txtRSA.append("minimum RSA: " + jcNumberFormat.format(dblTemp) + "\n");

			frmLoadData.engMatLab.engEvalString("max(RSA);");  //largest RSA 
			dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			jcBigDec = new BigDecimal(dblTemp); rmHeartData.setRSAMax(jcBigDec);
			txtRSA.append("maximum RSA: " + jcNumberFormat.format(dblTemp) + "\n");

			frmLoadData.engMatLab.engEvalString("length(RSA);");  //# RSA should = # breaths
			dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
			rmHeartData.setRSACount((int)dblTemp);
			txtRSA.append("RSA count: " + jcNumberFormat.format(dblTemp) + "\n\nbreath differences: ");
			
			dblRSA = frmLoadData.engMatLab.engGetArray("RSA");  //RSA for each breath
			for (intC = 0; intC < dblRSA[0].length; intC++) {
				txtRSA.append(jcNumberFormat.format(dblRSA[0][intC]) + " ");
				jcTempList.add(new Integer((int)dblRSA[0][intC]));
			}
			rmHeartData.setRSAList(jcTempList);
			
			frmLoadData.engMatLab.engEvalString("plotRSA(RR, y, saveDiffRwin, saveDiffNum, firstR, lastR, troughs);");  //shows resp with R peaks
			jTabbedPane1.setSelectedIndex(3);  //set next panel on top
    }//GEN-LAST:event_cmdRSAActionPerformed
    
  		
		/** returns rmHeartData filled with derived heart rate-related data
		 * for the clip.  Called after CalculateResp().
		 * @return HeartMeasures object filled with derived heart rate variability and
		 * respiratory sinus arrythymia data.
		 */		
		public static HeartMeasures getHeartDataObject() { return rmHeartData;	}
		

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cmdCancel;
  private javax.swing.JButton cmdHRV;
  private javax.swing.JButton cmdInvalid;
  private javax.swing.JButton cmdPrepare;
  private javax.swing.JButton cmdRSA;
  private javax.swing.JPanel fraFour;
  private javax.swing.JPanel fraOne;
  private javax.swing.JPanel fraThree;
  private javax.swing.JPanel fraTwo;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JLabel lblComment;
  private javax.swing.JTextArea txtComment;
  private javax.swing.JTextArea txtRSA;
  private javax.swing.JTextArea txtResults;
  // End of variables declaration//GEN-END:variables

}
