package brukerParavisionp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mriManagerp.GetStackTracep;
import mriManagerp.ListSequencep;
import mriManagerp.PcaslBruker_;


public class ListParamBrukerp {
	
	private String chemAcqp,chemMethod,chemVisupars;
	private String separator = File.separator;
	private String[][] list1,list2,list3;
		
	private String[][] visuparsParams1 = {	
			{"##$VisuCoreDim=" , "Dimensions (2D/3D)" , "1"},				//list1[0][x]
			{"##$VisuAcqEchoTime" , "Echo Time" , "2"},						//list1[1][x]
			{"##$VisuAcqRepetitionTime" , "Repetition Time" , "2"},			//list1[2][x]
			{"##$VisuAcqInversionTime" , "Inversion Time" , "2"},			//list1[3][x]
			{"##$VisuAcqNumberOfAverages=" , "Number of Average" , "1"},	//list1[4][x]
			{"##$VisuCoreWordType=" , "Data type", "1"},					//list1[5][x]
			{"##$VisuCoreSize" , "Width" , "2"},							//list1[6][x]
			{"##$VisuCoreSize" , "Height" , "2"},							//list1[7][x]
			{"##$VisuCoreFrameCount=" , "Depth" , "1"},						//list1[8][x]
			{"##$VisuCoreByteOrder=", "Byte Order", "1"},					//list1[9][x]
			{"##$VisuCoreExtent=","FOV","2"}									//list1[10][x]
			};
	private String[][] visuparsParams2 =	{
			{"##$VisuCreatorVersion=" , "Paravision version","2"},			//list2[0][x]
			{"##$VisuStation" , "Spectro","2"},								//list2[1][x]
			{"##$VisuAcqDate" , "Acquisition date","2"}						//list2[2][x]
			};
	private String[][] visuparsParams3 =	{
			{"##$VisuAcqFlipAngle=" , "Flip angle","1"},					//list3[1][x]
			};
	private String[][] acqpParams1 = { 
			{"##$NSLICES", "number of slice","1"},								//list1[11][x]
			{"##$NECHOES=", "number of echo","1"},								//list1[12][x]
			{"##$ACQ_slice_thick=", "slice thickness","1"},						//list1[13][x]
			{"##$ACQ_slice_sepn=", "slice separation","2"},						//list1[14][x]
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
			{"##$PVM_NRepetitions=" , "number of repetition","1"},			//list1[15][x]
			{"##$PVM_SpatResol", "ResolX (mm)","2"},						//list1[16][x]
			{"##$PVM_SpatResol", "ResolY (mm)","2"},						//list1[17][x]
			{"##$PVM_SliceThick=", "ResolZ (mm)","1"},						//list1[18][x]
			{"##$PVM_SPackArrSliceOrient=","slice orientation","2"},		//list1[19][x]
			{"##$PVM_SPackArrSliceOrient=","number of orientation","1"},	//list1[20][x]
			{"##$PVM_SPackArrReadOrient=","readout","2"},					//list1[21][x]
			{"##$PCASL_PhiSwList=", "PCASL", "2"},							//list1[22][x]
			};
	private String[][] methodParams3 = {
			{"##$PVM_RefAttCh1=","PVM_RefAttCh1","1",						//list3[5][x]
			 "##$PVM_RefPowCh1=","PVM_PVM_RefPowCh1","1"},					//list3[6][x]
			};

	
	public ListParamBrukerp (String chemSeq,String file) {
		
		if (chemSeq.isEmpty() && !file.isEmpty()){
		chemSeq = file.substring(0, file.indexOf("pdata"));
		String chemData = chemSeq.substring(0,chemSeq.lastIndexOf(separator));
		chemData = chemData.substring(0,chemData.lastIndexOf(separator));
		}
		chemVisupars = file+separator+"visu_pars";
		chemAcqp = chemSeq+separator+"acqp";
		chemMethod = chemSeq+separator+"method";
	}
	
	
	public String[][] listParamB1 () {
				
		list1 = new String[visuparsParams1.length+acqpParams1.length+methodParams1.length][2];
		
		/**********************************************************/
		for (int i=0;i<visuparsParams1.length;i++) {
			list1[i][0]=visuparsParams1[i][1];
			try {
				list1[i][1]=searchParam(visuparsParams1[i][0], chemVisupars, visuparsParams1[i][2]);
			} catch (Exception e) {
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
			}
			list1[i][1]=list1[i][1].replaceAll("<","");
			list1[i][1]=list1[i][1].replaceAll(">","");
		}
		try {
			list1[2][1]=tabScaling(visuparsParams1[2][0], chemVisupars);
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
		try {
			list1[1][1]=tabScaling(visuparsParams1[1][0], chemVisupars);
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
		try {
			list1[3][1]=tabScaling(visuparsParams1[3][0], chemVisupars);
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
		
		
		if (list1[0][1].contentEquals("1"))  list1[7][1]="";
		else {
		list1[6][1]=list1[6][1].substring(0,list1[6][1].indexOf(" "));
		list1[7][1]=list1[7][1].substring(list1[7][1].indexOf(" ")+1);
		if (list1[0][1].contentEquals("3")) {
			list1[8][1]=list1[7][1].substring(list1[7][1].indexOf(" ")+1);
			list1[7][1]=list1[7][1].substring(0,list1[7][1].indexOf(" "));
		}
		else 
		list1[7][1]=list1[7][1].substring(list1[7][1].indexOf(" ")+1);
		}
		
		/**********************************************************/
		for (int i=0;i<acqpParams1.length;i++) {
			list1[i+visuparsParams1.length][0]=acqpParams1[i][1];
			try {
				list1[i+visuparsParams1.length][1]=searchParam(acqpParams1[i][0], chemAcqp, acqpParams1[i][2]);
			} catch (Exception e) {
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
			}
			list1[i+visuparsParams1.length][1]=list1[i+visuparsParams1.length][1].replaceAll("<","");
			list1[i+visuparsParams1.length][1]=list1[i+visuparsParams1.length][1].replaceAll(">","");
		}
		
		/**********************************************************/
		for (int i=0;i<methodParams1.length;i++) {
			list1[i+visuparsParams1.length+acqpParams1.length][0]=methodParams1[i][1];
			try {
				list1[i+visuparsParams1.length+acqpParams1.length][1]=searchParam(methodParams1[i][0], chemMethod, methodParams1[i][2]);
			} catch (Exception e) {
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
			}
			list1[i+visuparsParams1.length+acqpParams1.length][1]=list1[i+visuparsParams1.length+acqpParams1.length][1].replaceAll("<","");
			list1[i+visuparsParams1.length+acqpParams1.length][1]=list1[i+visuparsParams1.length+acqpParams1.length][1].replaceAll(">","");
		}
		if (!list1[16][1].equalsIgnoreCase("null")) {
			
		try {
			list1[16][1]=list1[16][1].substring(0,list1[16][1].indexOf(" "));
			list1[17][1]=list1[17][1].substring(list1[17][1].indexOf(" ")+1);
		} catch (Exception e) {
			list1[17][1]="";
		}
		try {
			if ((searchParam("##$PVM_SpatResol=( ",chemMethod,"1")).contains("( 3 )")) {
			list1[18][1]=list1[17][1].substring(list1[17][1].indexOf(" ")+1);
			list1[17][1]=list1[17][1].substring(0,list1[17][1].indexOf(" "));
			}
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
		
		if (list1[21][1].contains("H_F")) {
			String tmp = list1[16][1];
			list1[16][1]=list1[17][1];
			list1[17][1]=tmp;
		}
		}
		list1[20][1]=list1[20][1].replaceAll("\\( ","");
		list1[20][1]=list1[20][1].replaceAll(" \\)","");
		
		try {
			if (searchParam("##$PCASL_LabelPhSw=",chemMethod,"1").contains("On")) 
				list1[22][1]=tabScaling(methodParams1[7][0], chemMethod);
			else
				list1[22][1]=tabScaling("##$PCASL_PhiSwListC=",chemMethod);
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
		
		return list1;
	}
		
	public String[][] listParamB2() {
		list2 = new String[visuparsParams2.length+acqpParams2.length][2];
		
		/**********************************************************/
		list2[0][0] = visuparsParams2[0][1];
		try {
			list2[0][1] = searchParam(visuparsParams2[0][0], chemVisupars, visuparsParams2[0][2]);
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
		}
		list2[0][1]=list2[0][1].replaceAll("<","");
		list2[0][1]=list2[0][1].replaceAll(">","");
		if (list2[0][1].substring(0, 1).contentEquals("6")) visuparsParams2[2][2]="1";
		for (int i=1;i<visuparsParams2.length;i++) {
			list2[i][0]=visuparsParams2[i][1];
			try {
				list2[i][1]=searchParam(visuparsParams2[i][0], chemVisupars, visuparsParams2[i][2]);
			} catch (Exception e) {
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
			}
			list2[i][1]=list2[i][1].replaceAll("<","");
			list2[i][1]=list2[i][1].replaceAll(">","");
		}
		
		/**********************************************************/
		for (int i=0;i<acqpParams2.length;i++) {
			list2[i+visuparsParams2.length][0]=acqpParams2[i][1];
			try {
				list2[i+visuparsParams2.length][1]=searchParam(acqpParams2[i][0], chemAcqp, acqpParams2[i][2]);
			} catch (Exception e) {
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
			}
			list2[i+visuparsParams2.length][1]=list2[i+visuparsParams2.length][1].replaceAll("<","");
			list2[i+visuparsParams2.length][1]=list2[i+visuparsParams2.length][1].replaceAll(">","");
		}
			
		return list2;
	}

	public String[][] listParamB3() {
		list3 = new String[acqpParams3.length+methodParams3.length][2];
		
		/**********************************************************/
		for (int i=0;i<acqpParams3.length;i++) {
			list3[i][0]=acqpParams3[i][1];
			list3[i][1]=searchParam(acqpParams3[i][0], chemAcqp, acqpParams3[i][2]);
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
			list3[1][1]=searchParam(acqpParams3[1][3], chemAcqp, acqpParams3[1][5]);
			list3[1][1]=list3[1][1].replaceAll("<","");
			list3[1][1]=list3[1][1].replaceAll(">","");
			list3[2][0]=acqpParams3[2][4];
			list3[2][1]=searchParam(acqpParams3[2][3], chemAcqp, acqpParams3[2][5]);
			list3[2][1]=list3[2][1].replaceAll("<","");
			list3[2][1]=list3[2][1].replaceAll(">","");
			if (!list3[1][1].contentEquals("not found")) {
				list3[1][1]=list3[1][1].substring(0,list3[1][1].indexOf(" "));
				list3[2][1]=list3[2][1].substring(list3[2][1].indexOf(" ")+1);
				try {
					list3[2][1]=list3[2][1].substring(0,list3[2][1].indexOf(" "));
				} catch (Exception e) {
					list3[2][1]="";
				}
				}
		}

		/**********************************************************/
		for (int i=0;i<methodParams3.length;i++) {
			list3[i+acqpParams3.length][0]=methodParams3[i][1];
			list3[i+acqpParams3.length][1]=searchParam(methodParams3[i][0], chemMethod, methodParams3[i][2]);
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll("<","");
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll(">","");
		}

		if (list3[4][1].contentEquals("not found")) 
			for (int i=0;i<methodParams3.length;i++) {
				list3[i+acqpParams3.length][0]=methodParams3[i][4];
				list3[i+acqpParams3.length][1]=searchParam(methodParams3[i][3], chemMethod, methodParams3[i][5]);
				list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll("<","");
				list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll(">","");
			}
		
		/**********************************************************/
		for (int i=0;i<visuparsParams3.length;i++) {
			list3[i+acqpParams3.length][0]=visuparsParams3[i][1];
			list3[i+acqpParams3.length][1]=searchParam(visuparsParams3[i][0], chemVisupars, visuparsParams3[i][2]);
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll("<","");
			list3[i+acqpParams3.length][1]=list3[i+acqpParams3.length][1].replaceAll(">","");
		}
		
		return list3;
	}
		
	private String searchParam (String paramToFind, String fichier, String order)  {
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
    	catch(Exception exc) {
    		return "not found";
    	}
    	 
    	 return "not found";
	}
	
	private String tabScaling (String paramToFind, String fichier) throws NumberFormatException, IOException {
		String ligne;
		String resul="";
		
		int inc = 0;
		BufferedReader lecteurAvecBuffer = new BufferedReader(new FileReader(fichier));
		
		while ((ligne = lecteurAvecBuffer.readLine()) != null) {
			if ( ligne.indexOf(paramToFind) != -1){
				inc=Integer.parseInt(ligne.substring(ligne.indexOf("(")+2, ligne.indexOf(" )")));
				int i=0;
				while (i<inc) {
					ligne=lecteurAvecBuffer.readLine();
					for (int j=0;j<ligne.split(" ").length;j++) {
						resul+=ligne.split(" ")[j]+" ";
					}
					i+=ligne.split(" ").length;
				}
				break;
			}
			}
		lecteurAvecBuffer.close();
		try {
			resul=resul.substring(0, resul.length()-1);
		} catch (Exception e) {
//			new getStackTrace(e);
//			MRIFileManager_.getBugText().setText(MRIFileManager_.getBugText().getText()+"\n----------------\n"+getStackTrace.getMessage());	
			resul="not found";
		}
		return resul;
	}
	
	public void fillBruker (String seqSelec) {
		
		String path = ListSequencep.hm.get(seqSelec)[0];
		String[] hmValue = new String[15];
		
		hmValue[0] =  path; // path of file
		hmValue[1] 	= list1[5][1];	// data type
		hmValue[2] 	= list1[6][1];  // width
		hmValue[3] 	= list1[7][1];	// height
		hmValue[4] 	= list1[8][1];	// depth
		hmValue[5] 	= list1[9][1];	// byteOrder
		hmValue[6] 	= list1[11][1];	// number of Slice
		hmValue[7] 	= list1[15][1];	// number of Repet
		hmValue[8] 	= list1[16][1];	// resolX
		hmValue[9] 	= list1[17][1];	// resolY
		hmValue[10] = list1[18][1];	// resolZ
		hmValue[11] = list1[19][1];	// sliceOrient
		hmValue[12] = list1[0][1];	// dimension
		hmValue[13] = list1[20][1];	// nOrient
		hmValue[14]	= list3[4][1];	// bandwith
		//for (String str:hmValue) System.out.println(str);
		ListSequencep.hm.put(seqSelec,hmValue);
	}
	
}
