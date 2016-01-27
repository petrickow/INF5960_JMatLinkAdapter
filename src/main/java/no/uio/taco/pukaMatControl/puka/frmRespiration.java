package no.uio.taco.pukaMatControl.puka;

/*
 * frmRespiration.java
 *
 * Created on May 27, 2003, 12:07 PM
 */

/**
 *
 * @author  Joset A. Etzel
 */

import javax.swing.table.*; import java.math.BigDecimal; import javax.swing.JOptionPane;
import java.text.NumberFormat; import java.util.Locale; import java.util.ArrayList;

public class frmRespiration extends javax.swing.JInternalFrame {
	private double[][] dblP; private double[][] dblT; 
	private double[][] dblPlabels; private double[][] dblTlabels;
	private static RespMeasures rmData;
	
	/** Creates new form frmRespiration */
	public frmRespiration() {
		initComponents();
		pack();
		frmLoadData.engMatLab.engEvalString("cd ('" + frmPreferences.getInstallPath() + "\\matlabScripts')"); 
		//y holds the respiration signal in matlab, from the start to the stop time ONLY
		frmLoadData.engMatLab.engEvalString("y = data1(:, " + frmPreferences.getColRespiration() + ");");
		
		int startTime = frmLoadData.getStartTime(); int stopTime = frmLoadData.getStopTime();
		frmLoadData.engMatLab.engEvalString("y = y(" + frmLoadData.getStartTime() + "," + frmLoadData.getStopTime() + ");");
		frmLoadData.engMatLab.engEvalString("plot(y, 'm');");  //show the respiration signal so can check it

		//initialize the RespMeasures object so can store the information
		rmData = new RespMeasures();  //get object ready to store 
		rmData.BlankOutValues();  //set everything to zero, just in case
		rmData.setClipID(frmLoadData.getClipName());  //get name of the clip from frmLoadData
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
  private void initComponents() {//GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    buttonGroup1 = new javax.swing.ButtonGroup();
    buttonGroup2 = new javax.swing.ButtonGroup();
    cmdCancel = new javax.swing.JButton();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    fraOne = new javax.swing.JPanel();
    cmdStart = new javax.swing.JButton();
    cmdInvalid = new javax.swing.JButton();
    txtComment = new javax.swing.JTextArea();
    lblComment = new javax.swing.JLabel();
    fraTwo = new javax.swing.JPanel();
    txtLabel = new javax.swing.JTextArea();
    txtNewNum = new javax.swing.JTextField();
    cmdRecalculate = new javax.swing.JButton();
    cmdContinue = new javax.swing.JButton();
    fraThree = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tblTroughs = new javax.swing.JTable();
    cmdApply = new javax.swing.JButton();
    cmdPauses = new javax.swing.JButton();
    rdoShowAll = new javax.swing.JRadioButton();
    rdoShowOnlyValid = new javax.swing.JRadioButton();
    chkPeaks = new javax.swing.JCheckBox();
    chkTroughs = new javax.swing.JCheckBox();
    rdoShowOnlyInvalid = new javax.swing.JRadioButton();
    jScrollPane11 = new javax.swing.JScrollPane();
    tblPeaks = new javax.swing.JTable();
    fraFour = new javax.swing.JPanel();
    cmdNext = new javax.swing.JButton();
    rdoUsePauses1 = new javax.swing.JRadioButton();
    rdoNoPauses1 = new javax.swing.JRadioButton();
    cmdRecenter = new javax.swing.JButton();
    fraFive = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    tblResults = new javax.swing.JTable();

    getContentPane().setLayout(new java.awt.GridBagLayout());

    setClosable(true);
    setIconifiable(true);
    setMaximizable(true);
    setResizable(true);
    setTitle("Respiration Analysis");
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
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
    getContentPane().add(cmdCancel, gridBagConstraints);

    jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
    jTabbedPane1.setPreferredSize(new java.awt.Dimension(350, 350));
    fraOne.setLayout(new java.awt.GridBagLayout());

    cmdStart.setMnemonic('S');
    cmdStart.setText("Start Analysis");
    cmdStart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdStartActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
    fraOne.add(cmdStart, gridBagConstraints);

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
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
    fraOne.add(cmdInvalid, gridBagConstraints);

    txtComment.setLineWrap(true);
    txtComment.setRows(10);
    txtComment.setWrapStyleWord(true);
    txtComment.setPreferredSize(new java.awt.Dimension(300, 170));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 10, 0);
    fraOne.add(txtComment, gridBagConstraints);

    lblComment.setText("Comment:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    fraOne.add(lblComment, gridBagConstraints);

    jTabbedPane1.addTab("Step 1", fraOne);

    fraTwo.setLayout(new java.awt.GridBagLayout());

    txtLabel.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
    txtLabel.setEditable(false);
    txtLabel.setLineWrap(true);
    txtLabel.setText("If too many incorrect peaks have been marked, enter a larger number and click Recalculate.  If too few peaks have been marked, enter a smaller number.  Otherwise, click Continue.");
    txtLabel.setWrapStyleWord(true);
    txtLabel.setPreferredSize(new java.awt.Dimension(200, 100));
    fraTwo.add(txtLabel, new java.awt.GridBagConstraints());

    txtNewNum.setText(".1");
    txtNewNum.setMinimumSize(new java.awt.Dimension(10, 20));
    txtNewNum.setPreferredSize(new java.awt.Dimension(40, 20));
    txtNewNum.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtNewNumKeyReleased(evt);
      }
    });

    fraTwo.add(txtNewNum, new java.awt.GridBagConstraints());

    cmdRecalculate.setMnemonic('R');
    cmdRecalculate.setText("Recalculate");
    cmdRecalculate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdRecalculateActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
    fraTwo.add(cmdRecalculate, gridBagConstraints);

    cmdContinue.setMnemonic('C');
    cmdContinue.setText("Continue");
    cmdContinue.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdContinueActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
    fraTwo.add(cmdContinue, gridBagConstraints);

    jTabbedPane1.addTab("Step 2", fraTwo);

    fraThree.setLayout(new java.awt.GridBagLayout());

    jScrollPane1.setPreferredSize(new java.awt.Dimension(140, 190));
    tblTroughs.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "name", "valid?"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.Boolean.class
      };
      boolean[] canEdit = new boolean [] {
        false, true
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tblTroughs.setMaximumSize(null);
    tblTroughs.setMinimumSize(null);
    tblTroughs.setPreferredSize(null);
    jScrollPane1.setViewportView(tblTroughs);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    fraThree.add(jScrollPane1, gridBagConstraints);

    cmdApply.setMnemonic('A');
    cmdApply.setText("Apply Changes");
    cmdApply.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdApplyActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    fraThree.add(cmdApply, gridBagConstraints);

    cmdPauses.setText("Next");
    cmdPauses.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdPausesActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 3;
    fraThree.add(cmdPauses, gridBagConstraints);

    rdoShowAll.setSelected(true);
    rdoShowAll.setText("Show All");
    buttonGroup1.add(rdoShowAll);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
    fraThree.add(rdoShowAll, gridBagConstraints);

    rdoShowOnlyValid.setText("Show Valid Only");
    buttonGroup1.add(rdoShowOnlyValid);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
    fraThree.add(rdoShowOnlyValid, gridBagConstraints);

    chkPeaks.setSelected(true);
    chkPeaks.setText("Show Peaks");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
    fraThree.add(chkPeaks, gridBagConstraints);

    chkTroughs.setSelected(true);
    chkTroughs.setText("Show Troughs");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
    fraThree.add(chkTroughs, gridBagConstraints);

    rdoShowOnlyInvalid.setText("Show Invalid Only");
    buttonGroup1.add(rdoShowOnlyInvalid);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
    fraThree.add(rdoShowOnlyInvalid, gridBagConstraints);

    jScrollPane11.setPreferredSize(new java.awt.Dimension(140, 190));
    tblPeaks.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "name", "valid?"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.Boolean.class
      };
      boolean[] canEdit = new boolean [] {
        false, true
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tblPeaks.setMaximumSize(null);
    tblPeaks.setMinimumSize(null);
    tblPeaks.setPreferredSize(null);
    jScrollPane11.setViewportView(tblPeaks);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    fraThree.add(jScrollPane11, gridBagConstraints);

    jTabbedPane1.addTab("Step 3", fraThree);

    fraFour.setLayout(new java.awt.GridBagLayout());

    cmdNext.setMnemonic('N');
    cmdNext.setText("Next");
    cmdNext.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdNextActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new java.awt.Insets(25, 0, 0, 0);
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
    fraFour.add(cmdNext, gridBagConstraints);

    rdoUsePauses1.setSelected(true);
    rdoUsePauses1.setText("Use pauses in calculations");
    buttonGroup2.add(rdoUsePauses1);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(20, 0, 2, 0);
    fraFour.add(rdoUsePauses1, gridBagConstraints);

    rdoNoPauses1.setText("no pauses in calculations");
    buttonGroup2.add(rdoNoPauses1);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    fraFour.add(rdoNoPauses1, gridBagConstraints);

    cmdRecenter.setMnemonic('R');
    cmdRecenter.setText("Recenter Markers");
    cmdRecenter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cmdRecenterActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
    fraFour.add(cmdRecenter, gridBagConstraints);

    jTabbedPane1.addTab("Step 4", fraFour);

    fraFive.setLayout(new java.awt.GridBagLayout());

    jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 275));
    tblResults.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {"Ttotal - mean", "seconds", ""},
        {"Ttotal - std dev", "seconds", null},
        {"Ti - mean", "seconds", null},
        {"Ti - std dev", "seconds", null},
        {"Te - mean", "seconds", null},
        {"Te - std dev", "seconds", null},
        {"Pi - mean", "seconds", null},
        {"Pi - std dev", "seconds", null},
        {"Pe - mean", "seconds", null},
        {"Pe - std dev", "seconds", null},
        {"Insp Duty Time", null, null},
        {"RR - mean", "cycles/minute", null},
        {"RR - std dev", "cycles/minute", null},
        {"num breaths", null, null},
        {"shortest breath", "seconds", null},
        {"longest breath", "seconds", null}
      },
      new String [] {
        "measure", "units", "value"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean [] {
        false, false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    jScrollPane2.setViewportView(tblResults);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    fraFive.add(jScrollPane2, gridBagConstraints);

    jTabbedPane1.addTab("Step 5", fraFive);

    getContentPane().add(jTabbedPane1, new java.awt.GridBagConstraints());

  }//GEN-END:initComponents

	private void cmdRecenterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRecenterActionPerformed
		//calls adjustFlatPT.m, which moves the peak/trough markers to the center of each pause
		//then call the routines to recreate the needed arrays and redraw the graph
		
		frmLoadData.engMatLab.engEvalString("[validPeaks, validTroughs] = adjustFlatPT(Qd, validPeaks, validTroughs, newP, newT);");  
		//frmLoadData.engMatLab.engEvalString("[validPeaks, validTroughs] = makeValidArrays(P,T,peakLabels, troughLabels);");
		frmLoadData.engMatLab.engEvalString("plotPauses(Qd, validPeaks, validTroughs, th, newP, newT);");	
	}//GEN-LAST:event_cmdRecenterActionPerformed

	private void txtNewNumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNewNumKeyReleased
		//pressing enter in txtNewNum is the same as clicking cmdRecalculate
		if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {	cmdRecalculate.doClick();	} 
	}//GEN-LAST:event_txtNewNumKeyReleased

	private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
		//control the computation of breathing statistics
		ArrayList jcTempList = new ArrayList(); double[][] dblTemp; int intTemp = 0; int intC = 0;
		double[][] dblTroughs; double[][] dblNewP; double[][] dblNewT;
		
		//need the peaks and troughs array regardless of using pauses or not
		frmLoadData.engMatLab.engEvalString("[peaks,troughs] = generatePT(P,T,peakLabels, troughLabels);");
		//Ttotal is calculated off of the troughs array - pauses don't matter
		frmLoadData.engMatLab.engEvalString("[avgTtot,stdTtot] = calculateTtotal(troughs);");
		
		//call the matlab scripts to do the calculations either with or without pauses
		if (rdoUsePauses1.isSelected() == true) {  //calculations include pauses
			frmLoadData.engMatLab.engEvalString("[avgPI,stdPI,avgPE,stdPE] = calculatePauses(newP,newT);"); 
			frmLoadData.engMatLab.engEvalString("[avgTI,stdTI,avgTE,stdTE] = calculateInsExp(newP,newT);");
		} else {  //pauses ignored; all assumed to be zero
			frmLoadData.engMatLab.engEvalString("avgPI = 0;");  //set all of these variables to zero
			frmLoadData.engMatLab.engEvalString("stdPI = 0;");	//so that CalculateResp() can retrieve
			frmLoadData.engMatLab.engEvalString("avgPE = 0;");	//the values to show in the table
			frmLoadData.engMatLab.engEvalString("stdPE = 0;");
			frmLoadData.engMatLab.engEvalString("[avgTI,stdTI,avgTE,stdTE] = calculateInsExpNoPauses(peaks,troughs);");			
		}
				
		CalculateResp();  //shows results in the table and sets in rmData
		jTabbedPane1.setSelectedIndex(4);  //set next panel on top
		
		//save the peaks and troughs arrays to rmData - these are the locations (undecimated)
		//of the valid peaks and troughs.  this will also save any manual pause alterations
		dblTemp = frmLoadData.engMatLab.engGetArray("peaks");
		for (intC = 0; intC < dblTemp[0].length; intC++) { jcTempList.add(new Integer((int)dblTemp[0][intC])); }
		rmData.setPeakList(jcTempList); 
		
		jcTempList = new ArrayList();
		dblTroughs = frmLoadData.engMatLab.engGetArray("troughs");
		for (intC = 0; intC < dblTroughs[0].length; intC++) { jcTempList.add(new Integer((int)dblTroughs[0][intC])); }
		rmData.setTroughList(jcTempList); 

		jcTempList = new ArrayList();
		dblNewP = frmLoadData.engMatLab.engGetArray("newP");
		for (intC = 0; intC < dblNewP[0].length; intC++) { jcTempList.add(new Integer((int)dblNewP[0][intC])); }
		rmData.setPeakPauseList(jcTempList); 
		
		jcTempList= new ArrayList();
		dblNewT = frmLoadData.engMatLab.engGetArray("newT");
		for (intC = 0; intC < dblNewT[0].length; intC++) { jcTempList.add(new Integer((int)dblNewT[0][intC])); }
		rmData.setTroughPauseList(jcTempList); 
	}//GEN-LAST:event_cmdNextActionPerformed

	private void cmdContinueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdContinueActionPerformed
		//continue the analysis code, starting where cmdStart left off
		frmLoadData.engMatLab.engEvalString("[peakLabels,troughLabels] = classifyPeaks(Qd,P,T,th);");		
		
		//get the peaks/troughs and labels back into the table 
		dblP = frmLoadData.engMatLab.engGetArray("P");  
		dblT = frmLoadData.engMatLab.engGetArray("T"); 		
		dblPlabels = frmLoadData.engMatLab.engGetArray("peakLabels");  
		dblTlabels = frmLoadData.engMatLab.engGetArray("troughLabels");  
		FillPeaksTable(0); FillTroughsTable(0);  //fill both with all peaks/troughs
		jTabbedPane1.setSelectedIndex(2);  //set next panel on top
	}//GEN-LAST:event_cmdContinueActionPerformed

	private void cmdRecalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRecalculateActionPerformed
		//reruns newPT if needed, changing the constant used find the threshold
		//used if some peaks were missed or too many peaks were detected.
		double dblValue = 0; String strText = "";
		
		strText = txtNewNum.getText();  //get and check the number entered for the correct range
		if (strText.equals("")) { JOptionPane.showMessageDialog(null, "You must enter a new number between 0 and 1.", "Error", JOptionPane.ERROR_MESSAGE); return; }
		dblValue = (new Double(strText)).doubleValue();
		if (0 >= dblValue | dblValue > 1) { JOptionPane.showMessageDialog(null, "You must enter a new number between 0 and 1.", "Error", JOptionPane.ERROR_MESSAGE); return; }
		
		frmLoadData.engMatLab.engEvalString("[P,T,th,Qd] = newPT(y, " + dblValue + ", onsetTime, endTime)");  //run newPT with the new number
	}//GEN-LAST:event_cmdRecalculateActionPerformed

	private void cmdInvalidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInvalidActionPerformed
		//sets the respiration object to have isValid = false, but still can have a comment
		int intResponse = 0;
		
		//double check that they really want it to be invalid
		intResponse = JOptionPane.showConfirmDialog(null, "Are you sure that you want to mark this signal as invalid?", "Confirm Invalid", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (intResponse == JOptionPane.NO_OPTION) { return; }
		
		rmData.setIsValid(false);  //set to invalid
		rmData.setComment(txtComment.getText());  //assign comment, if any
		
		try { this.setClosed(true); }  //close the form
		catch(java.beans.PropertyVetoException e) { e.printStackTrace(); }
	}//GEN-LAST:event_cmdInvalidActionPerformed

	private void cmdPausesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPausesActionPerformed
		//runs checkPT to calculate and show the pauses. 
		int intC = 0; int intNum = 0; 
		
		DoApply();  //call cmdApply first
		frmLoadData.engMatLab.engEvalString("[validPeaks, validTroughs] = makeValidArrays(P,T,peakLabels, troughLabels);");
		frmLoadData.engMatLab.engEvalString("[newP] = markPeakPauses(Qd, validPeaks, validTroughs, th);");
		frmLoadData.engMatLab.engEvalString("[newT] = markTroughPauses(Qd, validPeaks, validTroughs, th);");
		frmLoadData.engMatLab.engEvalString("plotPauses(Qd, validPeaks, validTroughs, th, newP, newT);");		

		jTabbedPane1.setSelectedIndex(3);  //set next panel on top
	}//GEN-LAST:event_cmdPausesActionPerformed

	private void CalculateResp() {
		//sub does calculations in matlab on the troughs array - locations where done breathing out but not
    //yet started breathing in - to find basic statistics on the respiration during the stimulus
    double dblTemp = 0; BigDecimal jcBigDec; double dblTi = 0; double dblTtot = 0; int intSampling = 0;
		
		NumberFormat jcNumberFormat = NumberFormat.getInstance(Locale.US);  //set up NumberFormat for the USA
		jcNumberFormat.setMaximumFractionDigits(4);  //only two digits after the decimal shown
		intSampling = frmPreferences.getSamplingFreq();
		
    frmLoadData.engMatLab.engEvalString("length(troughs);");  //# of breaths
    dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 13, 2);
		rmData.setNumBreaths((int)dblTemp);
		
    frmLoadData.engMatLab.engEvalString("min(diff(troughs));");  //shortest breath
    dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 14, 2);		
		jcBigDec = new BigDecimal(dblTemp); rmData.setShortestBreath(jcBigDec);
		
		frmLoadData.engMatLab.engEvalString("max(diff(troughs));");  //longest breath
    dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 15, 2);
		jcBigDec = new BigDecimal(dblTemp); rmData.setLongestBreath(jcBigDec);
      
		frmLoadData.engMatLab.engEvalString("mean(diff(troughs));");  //average breath length
    dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
    dblTemp = 60000 / dblTemp;  
		tblResults.setValueAt(jcNumberFormat.format(dblTemp), 11, 2);
		jcBigDec = new BigDecimal(dblTemp); rmData.setRespRateMean(jcBigDec);
			
    frmLoadData.engMatLab.engEvalString("std(diff(troughs));");  //standard deviation of breath length
    dblTemp = frmLoadData.engMatLab.engGetScalar("ans");
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 12, 2);
		jcBigDec = new BigDecimal(dblTemp); rmData.setRespRateStdDev(jcBigDec);
		
		//post-inspiratory & expiratory pause calculations
    dblTemp = frmLoadData.engMatLab.engGetScalar("avgPI");
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 6, 2);
		jcBigDec = new BigDecimal(dblTemp); rmData.setPostInspPauseMean(jcBigDec);
    
		dblTemp = frmLoadData.engMatLab.engGetScalar("stdPI");	
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 7, 2);			
		jcBigDec = new BigDecimal(dblTemp); rmData.setPostInspPauseStdDev(jcBigDec);	    
		
		dblTemp = frmLoadData.engMatLab.engGetScalar("avgPE");		
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 8, 2);
		jcBigDec = new BigDecimal(dblTemp); rmData.setPostExpPauseMean(jcBigDec);			
		
		dblTemp = frmLoadData.engMatLab.engGetScalar("stdPE");	
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 9, 2);
		jcBigDec = new BigDecimal(dblTemp); rmData.setPostExpPauseStdDev(jcBigDec);
		
		//total cycle time calculations
    dblTemp = frmLoadData.engMatLab.engGetScalar("avgTtot");
		dblTtot = dblTemp;  //save Ttotal in variable to calculate indpiration duty cycle later
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 0, 2);
		jcBigDec = new BigDecimal(dblTemp); rmData.setTotalBreathMean(jcBigDec);				
		
    dblTemp = frmLoadData.engMatLab.engGetScalar("stdTtot");
		dblTemp = dblTemp/intSampling;
    tblResults.setValueAt(jcNumberFormat.format(dblTemp), 1, 2);		
		jcBigDec = new BigDecimal(dblTemp); rmData.setTotalBreathStdDev(jcBigDec);
		
		//inspiration and expiration time calculations
      dblTemp = frmLoadData.engMatLab.engGetScalar("avgTI");
			dblTi = dblTemp;  //save inspiration time for inspiratory duty cycle calculation
			dblTemp = dblTemp/intSampling;
      tblResults.setValueAt(jcNumberFormat.format(dblTemp), 2, 2);
			jcBigDec = new BigDecimal(dblTemp); rmData.setInspTimeMean(jcBigDec);
			
			dblTemp = frmLoadData.engMatLab.engGetScalar("stdTI");
			dblTemp = dblTemp/intSampling;
      tblResults.setValueAt(jcNumberFormat.format(dblTemp), 3, 2);
			jcBigDec = new BigDecimal(dblTemp); rmData.setInspTimeStdDev(jcBigDec);
      
			dblTemp = frmLoadData.engMatLab.engGetScalar("avgTE");
			dblTemp = dblTemp/intSampling;
      tblResults.setValueAt(jcNumberFormat.format(dblTemp), 4, 2);
			jcBigDec = new BigDecimal(dblTemp); rmData.setExpTimeMean(jcBigDec);
      
			dblTemp = frmLoadData.engMatLab.engGetScalar("stdTE");
			dblTemp = dblTemp/intSampling;
      tblResults.setValueAt(jcNumberFormat.format(dblTemp), 5, 2);
			jcBigDec = new BigDecimal(dblTemp); rmData.setExpTimeStdDev(jcBigDec);
			
			//inspriratory duty cycle 
			dblTemp = dblTi/dblTtot;	
      tblResults.setValueAt(jcNumberFormat.format(dblTemp), 10, 2);
			jcBigDec = new BigDecimal(dblTemp); rmData.setInspDutyTimeMean(jcBigDec);	
    }
	
	private void cmdApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApplyActionPerformed
		DoApply();  //have this button's actions in a separate method so can call from cmdPauses also
	}//GEN-LAST:event_cmdApplyActionPerformed

	private void DoApply() {
		//go through the table, changing the numbers in dblTlabels & dblPlabels to match the check marks
		//then send the new labels to matlab and regraph the respiration signal
		int intC = 0; Boolean bolTemp; double dblWhat = 0; double dblWhich = 0; String strLabel = "";
		int intNumber = 0;
		
		//read the tables into dblTlabels & dblPlabels
		for (intC = 0; intC < tblPeaks.getRowCount(); intC++) {  //read all of the rows
			strLabel = (String)tblPeaks.getValueAt(intC, 0);  //of the form p# or t#		
			intNumber = new Integer(strLabel.substring(1)).intValue();  //number is after the initial p or t
			intNumber = intNumber - 1;  //the numbers are 1-based but the array is 0-based
			bolTemp = (Boolean)tblPeaks.getValueAt(intC, 1);  
			if (bolTemp.booleanValue() == true) {	dblPlabels[0][intNumber] = 1; }  //set peak to valid
			else { dblPlabels[0][intNumber] = 2; }  //set peak to invalid
		}
		for (intC = 0; intC < tblTroughs.getRowCount(); intC++) {  //read all of the rows
			strLabel = (String)tblTroughs.getValueAt(intC, 0);  //of the form p# or t#		
			intNumber = new Integer(strLabel.substring(1)).intValue();  //number is after the initial p or t
			intNumber = intNumber - 1;  //the numbers are 1-based but the array is 0-based
			bolTemp = (Boolean)tblTroughs.getValueAt(intC, 1);  
			if (bolTemp.booleanValue() == true) { dblTlabels[0][intNumber] = 1; } //set trough to valid
			else { dblTlabels[0][intNumber] = 2; } //set trough to invalid
		}
		
		//set dblWhat: 0 -> show all; 1 -> show only valid; 2 -> show only invalid, 3 -> show only questionable
		if (rdoShowAll.isSelected() == true) { dblWhat = 0; }
		else if (rdoShowOnlyValid.isSelected() == true) { dblWhat = 1; }
		else if (rdoShowOnlyInvalid.isSelected() == true) { dblWhat = 2; }

		//set dblWhich.  indicates whether to show just peaks (0), just troughs (1), or both (2)
		if (chkPeaks.isSelected() == true & chkTroughs.isSelected() == true) { 
			dblWhich = 2; 
			FillPeaksTable((int)dblWhat); FillTroughsTable((int)dblWhat);		}
		else if (chkPeaks.isSelected() == true & chkTroughs.isSelected() == false) { dblWhich = 0; FillPeaksTable((int)dblWhat); }
		else if (chkPeaks.isSelected() == false & chkTroughs.isSelected() == true) { dblWhich = 1; FillTroughsTable((int)dblWhat); }
		else if (chkPeaks.isSelected() == false & chkTroughs.isSelected() == false) { JOptionPane.showMessageDialog(null, "You must selected to view either peaks, troughs, or both.", "Error", JOptionPane.ERROR_MESSAGE); return; }
		
		//send the new arrays to matlab and replot
    frmLoadData.engMatLab.engPutArray("peakLabels", dblPlabels);  
    frmLoadData.engMatLab.engPutArray("troughLabels", dblTlabels); 		
		frmLoadData.engMatLab.engPutArray("Which", dblWhich); frmLoadData.engMatLab.engPutArray("What", dblWhat);
		frmLoadData.engMatLab.engEvalString("redrawResp(Qd,P,T,th,peakLabels,troughLabels,What, Which);");
}

	private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
		//just close the form
		try { this.setClosed(true); } 
		catch(java.beans.PropertyVetoException e) { e.printStackTrace(); }
	}//GEN-LAST:event_cmdCancelActionPerformed

	private void cmdStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdStartActionPerformed
		//run the respiration analysis scripts
	
		rmData.setIsValid(true);  //flag object as valid
		rmData.setComment(txtComment.getText());  //assign comment, if any
		
		//run newPT.m & classifyPeaks.m, which mark the peaks and valleys
		frmLoadData.engMatLab.engEvalString("[P,T,th,Qd] = newPT(y, .1, onsetTime, endTime)");  //run the matlab script

		jTabbedPane1.setSelectedIndex(1);  //set next panel on top
	}//GEN-LAST:event_cmdStartActionPerformed
	
	private void FillPeaksTable(int intWhat) {
		//sub fills up tblPeaks with the names and properties of the peaks currently in matlab
		//intWhat: 0 -> show all; 1 -> show only valid; 2 -> show only invalid, 3 -> show only questionable
		int intC = 0;	Object objVec[] = {"", new Boolean(false)}; Boolean BolTemp; int intRow = 0;
		
		DefaultTableModel jcTableModel = (DefaultTableModel)tblPeaks.getModel();  //get the table model for the table so can alter it
    if (jcTableModel.getRowCount() > 0) { jcTableModel.setRowCount(0); }  //get rid of previous rows, in case
		
		//peak labels: 1 = valid, 2 = invalid
		intRow = -1;
		for (intC = 0; intC < dblPlabels[0].length; intC++) {  
			System.out.println("peak " + intC);
			if (dblPlabels[0][intC] == 1) {	BolTemp = new Boolean(true); }  //good peak
			else { BolTemp = new Boolean(false); } //bad or questionable peak

			if (dblPlabels[0][intC] == 1 & (intWhat == 0 | intWhat == 1)) { 
				jcTableModel.addRow(objVec);  intRow++;  //increment row counter
				jcTableModel.setValueAt("p" + (intC + 1), intRow, 0);  
				jcTableModel.setValueAt(BolTemp, intRow, 1); }
			else if (dblPlabels[0][intC] == 2 & (intWhat == 0 | intWhat == 2)) {
				jcTableModel.addRow(objVec);  intRow++;  //increment row counter
				jcTableModel.setValueAt("p" + (intC + 1), intRow, 0);  
				jcTableModel.setValueAt(BolTemp, intRow, 1); }
			else if (dblPlabels[0][intC] == 3 & (intWhat == 0 | intWhat == 3)) {
				jcTableModel.addRow(objVec);  intRow++;  //increment row counter
				jcTableModel.setValueAt("p" + (intC + 1), intRow, 0);  
				jcTableModel.setValueAt(BolTemp, intRow, 1); }				
		}
	}
	
	private void FillTroughsTable(int intWhat) {
		//sub fills up tblTroughs with the names and properties of the troughs currently in matlab
		//intWhat: 0 -> show all; 1 -> show only valid; 2 -> show only invalid, 3 -> show only questionable
		int intC = 0;	Object objVec[] = {"", new Boolean(false)}; Boolean BolTemp; int intRow = 0;
		
		DefaultTableModel jcTableModel = (DefaultTableModel)tblTroughs.getModel();  //get the table model for the table so can alter it
    if (jcTableModel.getRowCount() > 0) { jcTableModel.setRowCount(0); }  //get rid of previous rows, in case

		//trough labels: 1 = valid, 2 = invalid, 3 = question
		intRow = -1;
		for (intC = 0; intC < dblTlabels[0].length; intC++) {  //add all of the troughs
			if (dblTlabels[0][intC] == 1) {	BolTemp = new Boolean(true); }  //good trough
			else { BolTemp = new Boolean(false); } //bad or questionable trough
				
			if (dblTlabels[0][intC] == 1 & (intWhat == 0 | intWhat == 1)) { 
				jcTableModel.addRow(objVec);  intRow++;  //increment row counter
				jcTableModel.setValueAt("t" + (intC + 1), intRow, 0); 
				jcTableModel.setValueAt(BolTemp, intRow, 1); }
			else if (dblTlabels[0][intC] == 2 & (intWhat == 0 | intWhat == 2)) {
				jcTableModel.addRow(objVec);  intRow++;  //increment row counter
				jcTableModel.setValueAt("t" + (intC + 1), intRow, 0);  
				jcTableModel.setValueAt(BolTemp, intRow, 1); }
			else if (dblTlabels[0][intC] == 3 & (intWhat == 0 | intWhat == 3)) {
				jcTableModel.addRow(objVec);  intRow++;  //increment row counter
				jcTableModel.setValueAt("t" + (intC + 1), intRow, 0); 
				jcTableModel.setValueAt(BolTemp, intRow, 1); }	
		}
	}
		
			/** returns rmData filled with derived respiration data
		 * for the clip.  Called after CalculateResp().
		 * @return RespMeasures object filled with derived respiration data.
		 */		
		public static RespMeasures getRespDataObject() { return rmData;	}
	
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.ButtonGroup buttonGroup1;
  private javax.swing.ButtonGroup buttonGroup2;
  private javax.swing.JCheckBox chkPeaks;
  private javax.swing.JCheckBox chkTroughs;
  private javax.swing.JButton cmdApply;
  private javax.swing.JButton cmdCancel;
  private javax.swing.JButton cmdContinue;
  private javax.swing.JButton cmdInvalid;
  private javax.swing.JButton cmdNext;
  private javax.swing.JButton cmdPauses;
  private javax.swing.JButton cmdRecalculate;
  private javax.swing.JButton cmdRecenter;
  private javax.swing.JButton cmdStart;
  private javax.swing.JPanel fraFive;
  private javax.swing.JPanel fraFour;
  private javax.swing.JPanel fraOne;
  private javax.swing.JPanel fraThree;
  private javax.swing.JPanel fraTwo;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane11;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JLabel lblComment;
  private javax.swing.JRadioButton rdoNoPauses1;
  private javax.swing.JRadioButton rdoShowAll;
  private javax.swing.JRadioButton rdoShowOnlyInvalid;
  private javax.swing.JRadioButton rdoShowOnlyValid;
  private javax.swing.JRadioButton rdoUsePauses1;
  private javax.swing.JTable tblPeaks;
  private javax.swing.JTable tblResults;
  private javax.swing.JTable tblTroughs;
  private javax.swing.JTextArea txtComment;
  private javax.swing.JTextArea txtLabel;
  private javax.swing.JTextField txtNewNum;
  // End of variables declaration//GEN-END:variables
	
}
