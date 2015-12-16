package no.uio.taco.pukaMatControl;

import no.uio.taco.pukaMatControl.matControlAdapter.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class AdapterTests {

	@Test
	public void InstanciateAdapter() {
		
		JMatLinkAdapter engMatLab = new JMatLinkAdapter();  //initiate connection
		engMatLab.engOpen();
		System.out.println("whoohoo");
		assertNull(engMatLab);
		
	}
}
