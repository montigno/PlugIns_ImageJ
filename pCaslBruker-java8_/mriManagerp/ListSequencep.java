package mriManagerp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import brukerParavisionp.ListParamSeqBrukerp;
import brukerParavisionp.Search2dseqp;


public class ListSequencep {
	
	public static String repertoryData;
	private PcaslBruker_ wind;
	private String to;
	public static HashMap<String, String[]> hm;
	private String[] hmValue;

			
	public ListSequencep() {
	}
	
	public ListSequencep(PcaslBruker_ wind,String repertory) {
		repertoryData=repertory;
		this.wind=wind;
						
		if (wind.getTabData().getValueAt(0,0).toString().contains("BrukerLogo")) {
				try {
					affichListSeqBruker();
				} catch (IOException e) {
					new GetStackTracep(e);
					PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
				}
		}

	}

	private void affichListSeqBruker() throws IOException {
			
		DefaultMutableTreeNode noeud = null,n=null;
		ListParamSeqBrukerp listSeqParam = null;
		List<String> list2dseq = new Search2dseqp(repertoryData).getList2dseq();
		String noSeq = null;
		hm = new HashMap<String, String[]>();
				
		ListParamSeqBrukerp.totalTime=0;
				
		wind.getBottom().removeAllChildren();
		wind.getListSeq().updateUI();
						
		if (!list2dseq.isEmpty()) {
		
		String noReco;
		wind.getInfoText().setText("");
	
		for (String str:list2dseq) {
			listSeqParam = new ListParamSeqBrukerp(str.substring(0,str.indexOf("pdata")-1));
			noSeq = str.substring(repertoryData.length()+1,str.indexOf("pdata")-1);
			wind.getInfoText().setText(wind.getInfoText().getText()+str+"\n");
			if (str.contains("*")) {
				noReco = str.toString();
				noReco = noReco.substring(noReco.indexOf("pdata")+6,noReco.indexOf("2dseq")-1);
				if (str.contains("**")){
					noeud = new DefaultMutableTreeNode(noSeq+listSeqParam.doListParamSeqRaw()+ " - (MultiReco)");
					wind.getBottom().add(noeud);
					wind.getListSeq().updateUI();
					n = new DefaultMutableTreeNode(noReco+" - Reco"+(noReco));
					noeud.add(n);
					wind.getListSeq().expandRow(wind.getListSeq().getRowCount()-1);
					//wind.getListSeq().updateUI();
					listSeqParam.totalTimeAcquisition();
				}
				else {
					n = new DefaultMutableTreeNode(noReco+" - Reco"+(noReco));
					noeud.add(n);
				}
				hmValue=new String[1];
				hmValue[0]=str.toString().replace("*","");
				hm.put(noReco+" - Reco"+(noReco)+" - "+n.getParent(),hmValue);
				}
			else {
				noeud = new DefaultMutableTreeNode(noSeq+listSeqParam.doListParamSeqRaw());
				wind.getBottom().add(noeud);
				hmValue=new String[1];
				hmValue[0]=str;
				hm.put(noSeq+listSeqParam.doListParamSeqRaw(),hmValue);
				wind.getListSeq().expandRow(wind.getListSeq().getRowCount()-1);
				wind.getListSeq().updateUI();
				listSeqParam.totalTimeAcquisition();
			}
		}
			
		wind.getListSeq().updateUI();
		wind.getListSeq().setSelectionRow(0);
		}
	
	long millis = ListParamSeqBrukerp.totalTime;
	long second = (millis / 1000) % 60;
	long minute = (millis / (1000 * 60)) % 60;
	long hour = (millis / (1000 * 60 * 60)) % 24;
	long day = (int) (millis / (1000 * 60 * 60 * 24));
	millis%=1000;
	String time ;
	if (day != 0) time = String.format("%02d day(s) + <%02dh%02dm%02ds%dms>", day ,hour, minute, second, millis);
	else time = String.format("<%02dh%02dm%02ds%dms>", hour, minute, second, millis);
	to=wind.getTextPath2dseq().getText();
	wind.getTextPath2dseq().setText("total acquisition time : "+time+"\n"+to);
	}
	
	public static HashMap<String, String[]> getListMap() {
		
//		for (String[] str:hm.values()) {
//			System.out.println("size = "+str.length);
//			for (String sts:str)
//				System.out.println(sts);
//		}		
//		
//			for (String stz:hm.keySet())
//				System.out.println(stz);
		
		return hm;
	}
}

