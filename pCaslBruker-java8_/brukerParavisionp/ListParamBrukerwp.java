package brukerParavisionp;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import mriManagerp.GetStackTracep;
import mriManagerp.PcaslBruker_;

public class ListParamBrukerwp {

	private String chemAcqp,chemMethod,chemVisupars ;
	private String separator = File.separator;
	private String textOfAcqp,textOfMethod,textOfVisupars;
	//private int version=1;
	
	
	private String[][] visuparsParams1 = {	
			{"##$VisuAcqSize" , "Acquisition size","2"},					//list1[0][x]
			{"##$VisuCoreDim=" , "Dimensions (2D/3D)" , "1"},				//list1[1][x]
			{"##$VisuAcqEchoTime" , "Echo Time" , "2"},						//list1[2][x]
			{"##$VisuAcqRepetitionTime" , "Repetition Time" , "2"},			//list1[3][x]
			{"##$VisuAcqNumberOfAverages=" , "Number of Average" , "1"},	//list1[4][x]
			{"##$VisuCoreWordType=" , "Data type", "1"},					//list1[5][x]
			{"##$VisuCoreSize" , "Width" , "2"},							//list1[6][x]
			{"##$VisuCoreSize" , "Height" , "2"},							//list1[7][x]
			{"##$VisuCoreFrameCount=" , "Depth" , "1"}						//list1[8][x]
			};
	private String[][] visuparsParams2 =	{
			{"##$VisuCreatorVersion=" , "Paravision version","2"},			//list2[0][x]
			{"##$VisuStation" , "Spectro","2"},								//list2[1][x]
			{"##$VisuAcqDate" , "Acquisition date","2"}						//list2[2][x]
			};
	private  String[][] visuparsParams3 =	{
			{"##$VisuAcqFlipAngle=" , "Flip angle","1"},					//list3[1][x]
			};
	private String[][] acqpParams1 = { 
			{"##$NSLICES=", "number of slice","1"},							//list1[9][x]
			{"##$NECHOES=", "number of echo","1"},							//list1[10][x]
			{"##$ACQ_slice_thick=", "slice thickness","1"},					//list1[11][x]
			{"##$ACQ_slice_sepn=", "slice separation","2"},					//list1[12][x]
			{"##$ACQ_fov", "FOV","2"},										//list1[13][x]
			};
	private String[][] acqpParams2 = { 				
			{"##$ACQ_operation_mode", "coil","2"},							//list2[3][x]
			{"##$ACQ_operator=","session","2"},								//list2[4][x]
			};
	private String[][] acqpParams3 = { 
			{"##$BF1=","Frequency (MHz)","1"},								//list3[0][x]
			{"##$SP=","Tx0 attenuator","2","##$P=","Tx0 attenuator","2"},   //list3[1][x]
			{"##$SP=","Tx1 attenuator","2","##$P=","Tx0 attenuator","2"},	//list3[2][x]
			{"##$RG=","Receiver gain","1"},									//list3[3][x]
			{"##$SW_h=","BandWith","1"},									//list3[4][x]
			};
	private String[][] methodParams1 = {
			{"##$PVM_NRepetitions=" , "number of repetition","1"},			//list1[14][x]
			{"##$PVM_SpatResol", "ResolX (mm)","2"},						//list1[15][x]
			{"##$PVM_SpatResol", "ResolY (mm)","2"},						//list1[16][x]
			{"##$PVM_SliceThick=", "ResolZ (mm)","1"},						//list1[17][x]
			{"##$PVM_SPackArrSliceOrient=","slice orientation","2"},		//list1[18][x]
			{"##$PVM_SPackArrSliceOrient=","number of orientation","1"}		//list1[19][x]
			};
	private String[][] methodParams3 = {
			{"##$PVM_RefAttCh1=","PVM_RefAttCh1","1",						//list3[4][x]
			 "##$PVM_RefPowCh1=","PVM_PVM_RefPowCh1","1"},					//list3[5][x]
			};

	
	public ListParamBrukerwp (String chemSeq,String file) {
		//this.chemSubject=chemSubject;
			
		if (chemSeq.isEmpty() && !file.isEmpty()){
		chemSeq = file.substring(0, file.indexOf("pdata"));
		String chemData = chemSeq.substring(0,chemSeq.lastIndexOf(separator));
		chemData = chemData.substring(0,chemData.lastIndexOf(separator));
		}
		
		chemVisupars = file+separator+"visu_pars";
		chemAcqp = chemSeq+separator+"acqp";
		chemMethod = chemSeq+separator+"method";
		
		textOfVisupars= extract(chemVisupars);
		textOfAcqp=extract(chemAcqp);
		textOfMethod=extract(chemMethod);

		}
	
	public String[][] listParam1 () throws IOException {
		String[][] list1 = new String[visuparsParams1.length+acqpParams1.length+methodParams1.length][2];
		
		/**********************************************************/
		for (int i=0;i<visuparsParams1.length;i++) {
			list1[i][0]=visuparsParams1[i][1];
			list1[i][1]=searchParam(visuparsParams1[i][0], textOfVisupars, visuparsParams1[i][2]);
			list1[i][1]=list1[i][1].replaceAll("<","");
			list1[i][1]=list1[i][1].replaceAll(">","");
		}
		if (list1[1][1].contentEquals("1"))  list1[7][1]="";
		else {
		list1[6][1]=list1[6][1].substring(0,list1[6][1].indexOf(" "));
		list1[7][1]=list1[7][1].substring(list1[7][1].indexOf(" ")+1);
		if (list1[1][1].contentEquals("3")) {
			list1[8][1]=list1[7][1].substring(list1[7][1].indexOf(" ")+1);
			list1[7][1]=list1[7][1].substring(0,list1[7][1].indexOf(" "));
		}
		else 
		list1[7][1]=list1[7][1].substring(list1[7][1].indexOf(" ")+1);
		}
		
		/**********************************************************/
		for (int i=0;i<acqpParams1.length;i++) {
			list1[i+visuparsParams1.length][0]=acqpParams1[i][1];
			list1[i+visuparsParams1.length][1]=searchParam(acqpParams1[i][0], textOfAcqp, acqpParams1[i][2]);
			list1[i+visuparsParams1.length][1]=list1[i+visuparsParams1.length][1].replaceAll("<","");
			list1[i+visuparsParams1.length][1]=list1[i+visuparsParams1.length][1].replaceAll(">","");
		}
		
		/**********************************************************/
		for (int i=0;i<methodParams1.length;i++) {
			list1[i+visuparsParams1.length+acqpParams1.length][0]=methodParams1[i][1];
			list1[i+visuparsParams1.length+acqpParams1.length][1]=searchParam(methodParams1[i][0], textOfMethod, methodParams1[i][2]);
			list1[i+visuparsParams1.length+acqpParams1.length][1]=list1[i+visuparsParams1.length+acqpParams1.length][1].replaceAll("<","");
			list1[i+visuparsParams1.length+acqpParams1.length][1]=list1[i+visuparsParams1.length+acqpParams1.length][1].replaceAll(">","");
		}
		if (!list1[15][1].equalsIgnoreCase("null")) {
			
		try {
			list1[15][1]=list1[15][1].substring(0,list1[15][1].indexOf(" "));
			list1[16][1]=list1[16][1].substring(list1[16][1].indexOf(" ")+1);
		} catch (Exception e) {
			list1[16][1]="";
		}
		if ((searchParam("##$PVM_SpatResol=( ",textOfMethod,"1")).contains("( 3 )")) {
		list1[17][1]=list1[16][1].substring(list1[16][1].indexOf(" ")+1);
		list1[16][1]=list1[16][1].substring(0,list1[16][1].indexOf(" "));
		}	
		}
		list1[19][1]=list1[19][1].replaceAll("\\( ","");
		list1[19][1]=list1[19][1].replaceAll(" \\)","");
		return list1;
	}
	
	
	public String[][] listParam2() throws IOException {
		String[][] list2 = new String[visuparsParams2.length+acqpParams2.length][2];
		
		/**********************************************************/
		list2[0][0] = visuparsParams2[0][1];
		list2[0][1] = searchParam(visuparsParams2[0][0], textOfVisupars, visuparsParams2[0][2]);
		list2[0][1]=list2[0][1].replaceAll("<","");
		list2[0][1]=list2[0][1].replaceAll(">","");
		if (list2[0][1].substring(0, 1).contentEquals("6")) visuparsParams2[2][2]="1";
		for (int i=1;i<visuparsParams2.length;i++) {
			list2[i][0]=visuparsParams2[i][1];
			list2[i][1]=searchParam(visuparsParams2[i][0], textOfVisupars, visuparsParams2[i][2]);
			list2[i][1]=list2[i][1].replaceAll("<","");
			list2[i][1]=list2[i][1].replaceAll(">","");
		}
		
		/**********************************************************/
		for (int i=0;i<acqpParams2.length;i++) {
			list2[i+visuparsParams2.length][0]=acqpParams2[i][1];
			list2[i+visuparsParams2.length][1]=searchParam(acqpParams2[i][0], textOfAcqp, acqpParams2[i][2]);
			list2[i+visuparsParams2.length][1]=list2[i+visuparsParams2.length][1].replaceAll("<","");
			list2[i+visuparsParams2.length][1]=list2[i+visuparsParams2.length][1].replaceAll(">","");
		}
			
		return list2;
	}

	public String[][] listParam3() throws IOException {
		String[][] list3 = new String[acqpParams3.length+methodParams3.length][2];
		
		/**********************************************************/
		for (int i=0;i<acqpParams3.length;i++) {
			list3[i][0]=acqpParams3[i][1];
			list3[i][1]=searchParam(acqpParams3[i][0], textOfAcqp, acqpParams3[i][2]);
			list3[i][1]=list3[i][1].replaceAll("<","");
			list3[i][1]=list3[i][1].replaceAll(">","");
			}
		
		if (!list3[1][1].contentEquals("not found")) {
		list3[1][1]=list3[1][1].substring(0,list3[1][1].indexOf(" "));
		list3[2][1]=list3[2][1].substring(list3[2][1].indexOf(" ")+1);
		list3[2][1]=list3[2][1].substring(0,list3[2][1].indexOf(" "));
		}
		else {
			list3[1][0]=acqpParams3[1][4];
			list3[1][1]=searchParam(acqpParams3[1][3], textOfAcqp, acqpParams3[1][5]);
			list3[1][1]=list3[1][1].replaceAll("<","");
			list3[1][1]=list3[1][1].replaceAll(">","");
			list3[2][0]=acqpParams3[2][4];
			list3[2][1]=searchParam(acqpParams3[2][3], textOfAcqp, acqpParams3[2][5]);
			list3[2][1]=list3[2][1].replaceAll("<","");
			list3[2][1]=list3[2][1].replaceAll(">","");
			if (!list3[1][1].contentEquals("not found")) {
				list3[1][1]=list3[1][1].substring(0,list3[1][1].indexOf(" "));
				list3[2][1]=list3[2][1].substring(list3[2][1].indexOf(" ")+1);
				list3[2][1]=list3[2][1].substring(0,list3[2][1].indexOf(" "));
				}
		}
		
		/**********************************************************/
		for (int i=0;i<methodParams3.length;i++) {
			list3[i+acqpParams3.length][0]=methodParams3[i][1];
			list3[i+acqpParams3.length][1]=searchParam(methodParams3[i][0], textOfMethod, methodParams3[i][2]);
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll("<","");
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll(">","");
		}
		
		if (list3[4][1].contentEquals("not found")) 
			for (int i=0;i<methodParams3.length;i++) {
				list3[i+acqpParams3.length][0]=methodParams3[i][4];
				list3[i+acqpParams3.length][1]=searchParam(methodParams3[i][3], textOfMethod, methodParams3[i][5]);
				list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll("<","");
				list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll(">","");
			}
		
		/**********************************************************/
		for (int i=0;i<visuparsParams3.length;i++) {
			list3[i+acqpParams3.length][0]=visuparsParams3[i][1];
			list3[i+acqpParams3.length][1]=searchParam(visuparsParams3[i][0], textOfVisupars, visuparsParams3[i][2]);
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll("<","");
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll(">","");
		}
		
		return list3;
	}
	
	
	private String extract (String file) {
		
		String tm="";
		
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
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
