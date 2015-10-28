package no.uio.taco.pukaMatControl.puka;

/*
 * GeneralCode.java
 *
 * Created on January 6, 2003, 10:45 AM
 */

/**
 *
 * @author  Joset A. Etzel
 */
public class GeneralCode {
	
	/** Creates a new instance of GeneralCode */
	public GeneralCode() {
	}
	
	static String AddEscapeChars(String strSQL) {
		//adds another \ in front of all \, ', and " so everything saved ok into the database
    int intC = 0;
        
    StringBuffer strbNew = new StringBuffer(strSQL);
    for (intC = 0; intC < strSQL.length(); intC++) {   //parse string, looking for dangerous characters
			if (strbNew.charAt(intC) == '\\' | strbNew.charAt(intC) == '\"' | strbNew.charAt(intC) == '\'') {
				strbNew.insert(intC,'\\'); intC++; }   //insert escape char, increment counter to pass \
    }
    String strNew = new String(strbNew);  //convert character array back to string
    return strNew; 
	}
	
}
