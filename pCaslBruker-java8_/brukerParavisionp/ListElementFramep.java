package brukerParavisionp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ListElementFramep {

	private String file ;
	
	private String[][] visuparsParams = {
			{"##$VisuCoreDataUnits","VisuCoreDataUnits= ","2"},
			{"##$VisuFGElemComment" , "VisuFGElemComment= ","2"},					
	};
	
	public ListElementFramep(String file) {
		this.file=file;
	}
	
	public String[][] listElement() throws IOException {
		String[][] listElem = new String[2][2];
		String text,line;
		String tampon = null;
				
		listElem[0][0]=visuparsParams[0][1];
		listElem[1][0]=visuparsParams[1][1];
		
		BufferedReader lecteurAvecBuffer = new BufferedReader(new FileReader(file));
		while ((line = lecteurAvecBuffer.readLine()) != null) {
			tampon=tampon+line;
		}
		lecteurAvecBuffer.close();
		
		if (tampon.contains(visuparsParams[0][0]) && tampon.contains(visuparsParams[1][0]))
		for (int i=0;i<2;i++){
			text=tampon;
			text=text.substring(text.indexOf(visuparsParams[i][0])+3);
			text=text.substring(text.indexOf("<"), text.indexOf("##"));
			if (text.contains("$$")) text=text.substring(0, text.indexOf("$$"));
			listElem[i][1]=text;
		}
		
		return listElem;
	
	}

}
