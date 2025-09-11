package brukerParavisionp;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import mriManagerp.GetStackTracep;
import mriManagerp.PcaslBruker_;


public class ListBrukerDatap {

	private int version=1;
	private String textOfSubject;
		
	private String[][] subjectParams = {	
			{"##$SUBJECT_name_string","Subject name : ","2" },
			{"##$SUBJECT_study_name","Subject study : ","2"},
			{"##$SUBJECT_date=", "Date : ","1"}
			};
	
	public ListBrukerDatap (String chemSubject) {
		textOfSubject= extract(chemSubject);
	}
	
	public String[] listParamDataBruker() throws IOException {
		String[] resul = new String[3];
				
		if (searchParam("##$SUBJECT_date=", textOfSubject, "1").indexOf("<")!=-1) {
			version=2;
		}
		else {
			subjectParams[2][2]="2";
		}
		
	    for (int i=0;i<subjectParams.length;i++) {
	    	resul[i]=searchParam(subjectParams[i][0],textOfSubject,subjectParams[i][2]);
	     	resul[i]=resul[i].replaceAll("<","");
	    	resul[i]=resul[i].replaceAll(">","");
	    }
	    if (resul[2]!="not found") {	    
	    Dateformatmodifp newdate= new Dateformatmodifp(resul[2],version);
	    resul[2]=newdate.newformdate();
	    }
	    return resul;
	}
	
private String extract (String file) {
		
		String tm="";
		
		try {BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
	       StringWriter out = new StringWriter();
	       int b;
	       while ((b=in.read()) != -1)
	           out.write(b);
	       out.flush();
	       out.close();
	       in.close();
	       tm=out.toString();
	}
	    catch (IOException e) {
	    	new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
	    }
		return tm;
	
	}
	
	private String searchParam(String paramToFind,String texte, String order)  {
		try{		
		texte=texte.substring(texte.indexOf(paramToFind));
		if (order.contains("1")) texte=texte.substring(paramToFind.length(),texte.indexOf("\n"));
		if (order.contains("2")) {
			texte=texte.substring(texte.indexOf("\n")+1);
			texte=texte.substring(0,texte.indexOf("\n"));
		}
		}
		catch (Exception e) {
		texte="not found";	
		}
		return texte;
	}
}