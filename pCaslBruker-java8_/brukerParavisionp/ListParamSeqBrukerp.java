package brukerParavisionp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ListParamSeqBrukerp {

	private String chemAcqp,chemMethod;
	private String separator = File.separator;
	public static long totalTime;
	
	private String[][] acqpParams = { 
			{"##$ACQ_protocol_name", "","2"},
			{"##$PULPROG=","","2"},
			};
	
	private String[][] methodParams = {
			{"##$PVM_ScanTimeStr","","2"},
			};
	
	public ListParamSeqBrukerp (String chemSeq) {
		chemAcqp = chemSeq+separator+"acqp";
		chemMethod = chemSeq+separator+"method";
	}
	
	public String doListParamSeqRaw () throws IOException {
		String resul ="";
		for (int i=0;i<2;i++) {
	    	resul+=" - "+searchParam(acqpParams[i][0], chemAcqp, acqpParams[i][2]);
	    	resul=resul.replaceAll("<","");
	    	resul=resul.replaceAll(">","");	
	    }
		resul+=" - "+searchParam(methodParams[0][0], chemMethod, methodParams[0][2]);
		return resul;
	}
	
	public void totalTimeAcquisition() throws IOException  {
		String timetot;
		double hour=0,min=0,sec=0,msec=0,to=0;
		timetot=doListParamSeqRaw();
		if (!timetot.contains("not found")) {
			timetot=timetot.substring(timetot.indexOf("<")+1,timetot.indexOf(">"));
			if (timetot.matches(".*h.*s.*ms")) {
				hour=Long.parseLong(timetot.substring(0, timetot.indexOf("h")));
				min=Long.parseLong(timetot.substring(timetot.indexOf("h")+1, timetot.indexOf("m")));
				sec=Long.parseLong(timetot.substring(timetot.indexOf("m")+1, timetot.indexOf("s")));
				msec=Long.parseLong(timetot.substring(timetot.indexOf("s")+1, timetot.indexOf("ms")));
			}
			else if (timetot.matches(".*ms")) 
				//msec=Long.parseLong((timetot.substring(0, timetot.indexOf("ms"))).replace(".",","));
				msec=Double.parseDouble(timetot.substring(0, timetot.indexOf("ms")));
		to= 1000*(hour*3600+min*60+sec)+msec;
		}
		totalTime+=to;
 }
	
	
	private String searchParam (String paramToFind, String fichier, String order) throws IOException {
		BufferedReader lecteurAvecBuffer = null;
    	String ligne;
    	boolean find = false;
    	try {
    		lecteurAvecBuffer = new BufferedReader(new FileReader(fichier));
    		while ((ligne = lecteurAvecBuffer.readLine()) != null) {
	        	 if ( ligne.indexOf(paramToFind) != -1){
	        		if (order=="2") ligne = lecteurAvecBuffer.readLine();
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
