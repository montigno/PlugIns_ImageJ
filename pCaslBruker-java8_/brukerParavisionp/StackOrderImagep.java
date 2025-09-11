package brukerParavisionp;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class StackOrderImagep {

	private String file ;
	
	
	private String[][] visuparsParams = {
			{"##$VisuCoreFrameCount","VisuCoreFrameCount= ","1"},
			{"##$VisuFGOrderDescDim" , "VisuFGOrderDescDim= ","1"},					
			{"##$VisuFGOrderDesc=" , "VisuFGOrderDesc= " , "2"},
			{"##$VisuGroupDepVals=","VisuGroupDepVals= ","2"}
	};
	
	
	public StackOrderImagep (String file) {
		this.file=file;
	}
	
	public String[][] listStack() throws IOException {
		String[][] listOrder = new String[4][2];
		String tampon;
		listOrder[0][0]=visuparsParams[0][1];
		listOrder[0][1]=searchParam(visuparsParams[0][0], file,visuparsParams[0][2]);
		listOrder[1][0]=visuparsParams[1][1];
		listOrder[1][1]=searchParam(visuparsParams[1][0], file,visuparsParams[1][2]);
		listOrder[2][0]=visuparsParams[2][1];
		tampon=searchParam(visuparsParams[2][0], file,visuparsParams[2][2]);
		if (tampon.contains("$"))tampon=tampon.substring(0, tampon.indexOf("$"));
		if (tampon.contains("#"))tampon=tampon.substring(0, tampon.indexOf("#"));
		listOrder[2][1]=tampon;
		listOrder[3][0]=visuparsParams[3][1];
		listOrder[3][1]=searchParam(visuparsParams[3][0], file,visuparsParams[3][2]);
		return listOrder;
	}
	
	
	public String searchParam (String paramToFind, String fichier, String order) throws IOException {
		BufferedReader lecteurAvecBuffer = null;
    	String ligne;
    	boolean find = false;
    	try {
    		lecteurAvecBuffer = new BufferedReader(new FileReader(fichier));
    		while ((ligne = lecteurAvecBuffer.readLine()) != null) {
	        	 if ( ligne.indexOf(paramToFind) != -1){
	        		if (order=="2") {
	        			ligne = lecteurAvecBuffer.readLine();
	        			ligne += lecteurAvecBuffer.readLine();
	        		}
	        		else ligne = ligne.substring(ligne.indexOf("=")+1);
	        		lecteurAvecBuffer.close();
	        		find=true;
	        		return ligne;
	        	 }
	        	 if (find) break;
	        	 
	    	 }  
	    	 lecteurAvecBuffer.close();
    	}
    	    catch(FileNotFoundException exc) {
    		return "not found";
    	    }
    	 
    	 return "not found";
	}
}