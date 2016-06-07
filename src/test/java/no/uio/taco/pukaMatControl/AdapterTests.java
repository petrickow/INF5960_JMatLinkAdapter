package no.uio.taco.pukaMatControl;

import no.uio.taco.pukaMatControl.matControlAdapter.*;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import matlabcontrol.MatlabInvocationException;

public class AdapterTests {

	private static JMatLinkAdapter engMatLab;
	
	
	@BeforeClass
	public static void setup() {
		System.out.println("Please wait while matlab is instansiated ... "); 
		engMatLab = new JMatLinkAdapter();  //initiate connection
		engMatLab.engOpen();
		engMatLab.setDebug(false);
	}
		
	@AfterClass
	public static void tearDown() {
		engMatLab.engEvalString("clear");
		engMatLab.kill();
		System.out.println("Done and done"); // change to logger?
	}
	
	/**
	 * This test makes sure the adapter has been instanciated
	 */
	@Test
	public void InstanciateAdapter() {
		assertNotNull("That the matlabconnection has been established and we have the proxy", engMatLab);
	}
	
	/**
	 * Test engEvalString assigned variable in matlab engine, and that the double retrieved with engGetScalar 
	 * equals the variable set.  
	 */
	@Test
	public void SettingAndGettingVariables() {
		double dbl = 1.0;
		engMatLab.engEvalString("variable = "+ dbl + ";");
		double res;
		try {
			res = engMatLab.engGetScalar("variable");
			assertEquals("The value of engGetScalar is the same as provided", dbl, res, 0.0);
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Testing that we handle errors in some sense
	 */
	@Test
	public void TryingToGetNonexistingScalar() {
		MatlabInvocationException error = null;
		double res;
		try {
			res = engMatLab.engGetScalar("nonExistent");
		} catch (MatlabInvocationException e) {
			error = e;
		}
		assertNotNull(error);
		
	}
	
	
	/**
	 * Test that engEvalString is able to call matlab standard functions. This test uses floor() from mathworks
	 * standard math library to test on a double value
	 */
	@Test
	public void StandardMatlabFunctionIsCalled() {
		double dbl = 3.3;
		engMatLab.engEvalString("variable = " + dbl + ";");
		engMatLab.engEvalString("result = floor(variable)");
		double res;
		try {
			res = engMatLab.engGetScalar("result");
			assertNotEquals(dbl, res);
		} catch (MatlabInvocationException e) {
			
		}
	}
	
	/**
	 * Set a java.lang.double using engPutArray. Retrieve the variable set using engGetScalar
	 * and engGetArray 
	 */
	@Test
	public void SettingAndGettingSimpleArray() {
		double dbl = 3.0;
		engMatLab.engPutArray("array", dbl);
		
		double res;
		try {
			res = engMatLab.engGetScalar("array");
			assertEquals("Scalar set with engPutArray is retrievable with engGetScalar", dbl, res, 0.0);
			
			double[][] resArr = engMatLab.engGetArray("array");
			assertEquals("The same result is retrieved with engGetArray", dbl, resArr[0][0], 0.0);
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * Create and set a multidimensional double array (double[][]) using engPutArray
	 * and verify that engGetArray returns the same double[][].
	 */
	@Test
	public void SettingAndGettingOneDimensionalArray() {
		double[] array = {1.0, 2.2, 3.3, 4.4, 5.5};
		engMatLab.engPutArray("array", array); // put array creates a cell array
		double[][] res = engMatLab.engGetArray("array"); //Change put array to convert into matlab type
		assertTrue(res.length == 1); 

		double[] first = res[0]; // convert to 1 dimensional array for ease of comparison.
		assertArrayEquals(array, first, 0.0); // we want the array to be identical when it is returned.
	}
	
	/**
	 * Create and set a multidimensional double array (double[][]) using engPutArray
	 * and verify that engGetArray returns the same double[][].
	 */
	@Test
	public void SettingAndGettingMultiDimensionalArray() {
		double[][] array = {{1.1, 2.2, 3.3, 4.4, 5.5}, {5.1, 4.2, 3.3, 2.4, 1.5}};
		engMatLab.engPutArray("array", array); // put array creates a cell array
		double[][] res = engMatLab.engGetArray("array"); // Change put array to convert into matlab type

		assertArrayEquals(array, res); // we want the array to be identical when it is returned.
	}
	
	/**
	 * Do matrix mathematics and validate the answer to verify that the conversion of
	 * 2d java matrix to matlab type. 
	 */
	@Test
	public void OperationsOnMultiDimensionalArraysAreCorrect() {
		double[][] array = {{2.0}, {1.0}};
		
		engMatLab.engPutArray("array", array);
		engMatLab.engEvalString("array = array*2");
		
		double[][] res = engMatLab.engGetArray("array");
				
		assertTrue(array[0][0]*2 == res[0][0]);
		assertTrue(array[1][0]*2 == res[1][0]);
	}

	/**
	 * TODO: This has to be confirmed manually at the moment... ... ...
	 */
	@Test
	public void DisableAndEnableLogger() {
		engMatLab.setDebug(true);
		engMatLab.engEvalString("printed = 1.0"); // find a way to capture stdout? or check console...
		engMatLab.setDebug(false);
		engMatLab.engEvalString("not_printed = 1.0");
		//assertTrue(true);
	}
	
	@Test
	public void VerifyMATLABTypes() {
		double[][] arrDD = {{1.1, 2.2, 3.3, 4.4, 5.5}, {5.1, 4.2, 3.3, 2.4, 1.5}};
		double[] arrD = {1.0, 2.2, 3.3, 4.4, 5.5};
		double D = 3.3;
		double ans = 0.0;
		try {
			engMatLab.engPutArray("d", D);
			engMatLab.engEvalString("isscalar(d)");
			engMatLab.engEvalString("ans = double(ans)"); // has to convert to double to get the right type back 
			ans = engMatLab.engGetScalar("ans");
			
			assertTrue(ans == 1);
			
			engMatLab.engPutArray("d", arrD);
			engMatLab.engEvalString("isnumeric(d)");
			engMatLab.engEvalString("ans = double(ans)");
			ans = engMatLab.engGetScalar("ans");
			assertTrue(ans == 1);
			
			engMatLab.engPutArray("d", arrDD);
			engMatLab.engEvalString("isnumeric(d)");
			engMatLab.engEvalString("ans = double(ans)");
			ans = engMatLab.engGetScalar("ans");
			assertTrue(ans == 1);
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
		
		engMatLab.engEvalString("whos");
	}
	
	@Test
	public void VerifyIndexingOfArrays() {
		double[][] arrDD = {{1.1, 2.2, 3.3, 4.4, 5.5}, {5.1, 4.2, 3.3, 2.4, 1.5}};
		double ans = 0.0;
		try {
			engMatLab.engPutArray("arr", arrDD);
			engMatLab.engEvalString("first = arr(1,1)");
			ans = engMatLab.engGetScalar("first");
			assertTrue(ans == arrDD[0][0]);
			
			int length = arrDD.length;
			engMatLab.engEvalString("last = arr("+length+","+length+");");
			ans = engMatLab.engGetScalar("last");
			assertTrue(ans == arrDD[length-1][length-1]);
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
		
		
	}
}
