package Utilies;

import java.util.Date;

public class common_utilies {
	
      public  static String generatebrandnewemail() {
    	
    	Date date = new Date();
    	String datestring = date.toString();
    	String datestringwithoutspace = datestring.replaceAll("\\s", "");
    	String datestringwithoutsapceaandcolons =datestringwithoutspace.replaceAll("\\:","" );
    	String BrandNewEmail = datestringwithoutsapceaandcolons+"@gmail.com";
    	return BrandNewEmail;
    }

}
