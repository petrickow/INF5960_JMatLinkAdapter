package no.uio.taco.pukaMatControl.matControlTests;

import no.uio.taco.pukaMatControl.matControlAdapter.*;


public class AdapterTesting {
	static JMatLinkAdapter engMatLab;
	
	public static void main(String[] args) {
		engMatLab = new JMatLinkAdapter();  //initiate connection
		engMatLab.engOpen();
		
		
		
		
		
	}
}
