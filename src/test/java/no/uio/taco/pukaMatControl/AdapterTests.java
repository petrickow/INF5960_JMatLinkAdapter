package no.uio.taco.pukaMatControl;

import no.uio.taco.pukaMatControl.matControlAdapter.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.*;


public class AdapterTests {

	private static JMatLinkAdapter engMatLab;
	
	
	@BeforeClass
	public static void setup() {
		System.out.println("Please wait while matlab is instansiated ... ");
		engMatLab = new JMatLinkAdapter();  //initiate connection
		engMatLab.engOpen();
		
	}
		
	@AfterClass
	public static void tearDown() {
		engMatLab.engEvalString("clear");
		engMatLab.kill();
		System.out.println("Done and done"); // change to logger?
	}
	
	
	
	@Test
	public void InstanciateAdapter() {
		assertNotNull("That the matlabconnection has been established and we have the proxy", engMatLab);
	}
	
	@Test
	public void SettingAndGettingVariables() {
		double dec = 1.0;
		engMatLab.engEvalString("variable = "+ dec + ";");
		double adapter = engMatLab.engGetScalar("variable");
		assertEquals("The value of engGetScalar is the same as provided", dec, adapter, 0.0);
		
	}
	
	@Test
	public void StandardMatlabFunctionIsCalled() {
		double dec = 3.3;
		engMatLab.engEvalString("variable = " + dec + ";");
		engMatLab.engEvalString("result = floor(variable)");
		double res = engMatLab.engGetScalar("result");
		
		assertNotEquals(dec, res);
	}
	
	@Test
	public void SettingAndGettingArrays() {
		
		double[][] array = {{1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0}};
		
		// put array creates a cell array
		engMatLab.engPutArray("array", array);
		
		//Change put array to convert into matlab type
		double[][] res = engMatLab.engGetArray("array");

		

		assertArrayEquals(array, res); // we want the array to be identical when it is returned.
		
	}
	
	/**
	 * Do some simple math to the array to make sure matlab reads the array correctly
	 */
	@Test
	public void OperationsOnArraysAreCorrect() {
		double[][] array = {{1.0}, {1.0}};
		engMatLab.engPutArray("array", array);
		
		engMatLab.engEvalString("array*2");
		
		double[][] res = engMatLab.engGetArray("array");
		
		assertTrue(array[0][0]*2 == res[0][0]);
		assertTrue(array[1][0]*2 == res[1][0]);
	}
}
