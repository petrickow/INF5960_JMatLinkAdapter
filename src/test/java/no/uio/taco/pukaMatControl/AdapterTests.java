package no.uio.taco.pukaMatControl;

import no.uio.taco.pukaMatControl.matControlAdapter.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
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
		
	@After
	public void tearDown() {
		engMatLab.engEvalString("clear");
	}
	
	@Test
	public void InstanciateAdapter() {
		
		assertNotNull("That the matlabconnection has been established and we have the proxy", engMatLab);
	}
	
	@Test
	public void CorrectReturnType() {
		engMatLab.engEvalString("variable = 1;");
		double adapter = engMatLab.engGetScalar("variable");
//		assertEquals(Double.TYPE, getType(adapter);

		//TODO get type
	}
}
