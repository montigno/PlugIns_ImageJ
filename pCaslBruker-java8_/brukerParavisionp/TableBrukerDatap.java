package brukerParavisionp;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class TableBrukerDatap extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Format","Data Bruker","Patient","Study","Date"};
	private Object[][] data;
	private int j;
	//private subject paramSubject;
	//private listParamBruker paramSubject;
	private String[] paramSubject;
	private String repertoire;
	private String separator = File.separator;
	private boolean rootDisk = false;
	private String[] listSousRep;
	
	public TableBrukerDatap(String repertoire) throws IOException {

		this.repertoire=repertoire;

		URL imgURL = getClass().getResource("/BrukerLogo32p.jpg");
		ImageIcon BrukerIcon = new ImageIcon(imgURL);
		
		listSousRep = listesousrep();
		
		if (listSousRep.length!=0) {

		data = new Object[listSousRep.length][5];
		j=0;
		data[0][0]="no Bruker subject file found";
		data[0][1]="";
		data[0][2]="";
		data[0][3]="";
		data[0][4]="";

		 for (int i=0; i<data.length;i++) {
			if (isBruker(repertoire+separator+listSousRep[i])) {
				paramSubject = new ListBrukerDatap(repertoire+separator+listSousRep[i]+separator+"subject").listParamDataBruker();;
				data[j][0]=BrukerIcon;
				data[j][1]=listSousRep[i];
				data[j][2]=paramSubject[0];
				data[j][3]=paramSubject[1];
				data[j][4]=paramSubject[2];
				
				j++;
			}
		}
		
		 if (j==0) j++;
		}
		
		else {
		data = new Object[1][5];
		data[0][0]="no Bruker subject file found";
		data[0][1]="";
		data[0][2]="";
		data[0][3]="";
		data[0][4]="";
		j=1;
		}
	}

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
       	return j;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class<? extends Object> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    
    public String[] listesousrep () {
		
		if (!repertoire.contains(separator)) {
			repertoire+=separator;
			rootDisk=true;
		}
		
		File listeSousRepertoire = new File(repertoire);
		
		String[] listrep = listeSousRepertoire.list();
		
		if (rootDisk){
			repertoire=repertoire.substring(0,repertoire.length()-1);
			rootDisk=false;
		}
		
		return listrep;
	}
    
    public boolean isBruker (String repertoire1) {
	    File sousrep = new File(repertoire1);
	    
	    if (sousrep.isDirectory()) 
	    //if (searchSubject("subject", sousrep) && searchSubject("AdjStatePerStudy", sousrep)) return true;
	    	if (searchSubject("subject", sousrep)) return true;
	    	else return false;
	    return false;
	 }
    
    public boolean searchSubject(String fileToFind, File searchIn) {
        String[] listOfFiles = searchIn.list();
         boolean find = false;
        int i = 0;
        try {
        	while (i < listOfFiles.length && !find) {
     	
           	if (listOfFiles[i].contentEquals(fileToFind)) {
           		File fich = new File(searchIn.toString()+separator+listOfFiles[i]);
           		if (fich.length()!=0) find=true;
           	}
           	i++;
        }
        } catch (Exception e) {
        	find=false;
        }
        return find;
 }
}