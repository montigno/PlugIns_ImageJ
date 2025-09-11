package mriManagerp;

import ij.ImagePlus;
import parametricCalculsp.Pcaslp;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;

import brukerParavisionp.ImagePanelIRMp;
import brukerParavisionp.ListParamBrukerp;
import brukerParavisionp.OpenBrukerp;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

public class ActionSelectionSeqp implements TreeSelectionListener, MouseListener, KeyListener {

	private PcaslBruker_ wind;
	protected JTree treeSelection;
	private String separator = File.separator;
	private String to="";
	private static String format="";
	private String width, height,depth,slice,echo,typeData,offsetFile,byteorder;
	private static String chemReco;
	protected static String rep,plage;
	protected static HashMap<String, String[]> hv;

	private ImagePanelIRMp panelImageB;

	private Dimension dim;

	public static ImagePlus imgMat;
	
	public static HashMap<String, ArrayList<Object[]>> hmapOfFileA ;
	//private ArrayList<Object[]> listImA = new ArrayList<Object[]>();
	/*listImA contient pour chaque image : 	0 - path of DIRFILE file
	 * 										1 - echo number
	 * 										2 - image number
	 * 										3 - slice number
	 * 										4 - Repetition Time
	 * 										5 - Echo Time
	 * 										6 - slice location
	 * 										7 - image type
	 * 										8 - temporal position
	 * 						  
	 */

	public ActionSelectionSeqp(PcaslBruker_ wind) {
		this.wind=wind;
	}

	public void valueChanged(TreeSelectionEvent arg0) {
				
		if (arg0.getPath().getPathCount()==1) return;
		
		wind.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		if (wind.getTabData().getValueAt(wind.getTabData().getSelectedRow(),0).toString().contains("BrukerLogo")) {
			this.caseOfSeqRaw(arg0);
			format="Bruker";
		}
		
		wind.setCursor(null);
	}

	public void caseOfSeqRaw(TreeSelectionEvent argRaw) {
		String seqSelected;
		treeSelection = (JTree) argRaw.getSource();
		if (treeSelection.getAnchorSelectionPath().getPathCount()==2) 
			seqSelected = treeSelection.getAnchorSelectionPath().getPath()[1].toString();
		else seqSelected = treeSelection.getAnchorSelectionPath().getPath()[2].toString()+" - "+treeSelection.getAnchorSelectionPath().getPath()[1].toString();
		
		//new ListSequence();
		hv = ListSequencep.getListMap();
		try {
			chemReco =hv.get(seqSelected)[0];
		} catch (Exception e) {
			to = wind.getTextPath2dseq().getText();
			if (!to.isEmpty()){
			to=to.substring(0, to.indexOf(">")+1);
			wind.getTextPath2dseq().setText(to);
			}
			wind.resetTabParam();
			wind.resetBoxImage();
			wind.getSlidImage().setEnabled(false);
			return;
		}
			
		dim = wind.getBoxImage().getPreferredSize();
		wind.resetBoxImage();
		wind.getSlidImage().setEnabled(false);
		if (wind.getSlidImage().getChangeListeners().length!=0)
			wind.getSlidImage().removeChangeListener(wind.getSlidImage().getChangeListeners()[0]);
		
		to = wind.getTextPath2dseq().getText();
		if (!to.isEmpty()){
			to=to.substring(0, to.indexOf(">")+1);
			wind.getTextPath2dseq().setText(to+"\n"+chemReco);
		}
		else
			wind.getTextPath2dseq().setText(chemReco);
			
			ListParamBrukerp listparamR = new ListParamBrukerp("",chemReco.substring(0,chemReco.indexOf("2dseq")));
		
			try {
			
			TabParamp model1 = new TabParamp(listparamR.listParamB1());
			TableRowSorter<TabParamp> sorter1 = new TableRowSorter<TabParamp>(model1);
			wind.getTabParam1().setModel(model1);
			wind.getTabParam1().setRowSorter(sorter1);
						
			TabParamp model2 = new TabParamp(listparamR.listParamB2());
			TableRowSorter<TabParamp> sorter2 = new TableRowSorter<TabParamp>(model2);
			wind.getTabParam2().setModel(model2);
			wind.getTabParam2().setRowSorter(sorter2);
			
			TabParamp model3 = new TabParamp(listparamR.listParamB3());
			TableRowSorter<TabParamp> sorter3 = new TableRowSorter<TabParamp>(model3);
			wind.getTabParam3().setModel(model3);
			wind.getTabParam3().setRowSorter(sorter3);
			
			listparamR.fillBruker(seqSelected);
					
			width =(String) wind.getTabParam1().getValueAt(6,1);
			height=(String) wind.getTabParam1().getValueAt(7,1);
			depth =(String) wind.getTabParam1().getValueAt(8,1);
			typeData=(String) wind.getTabParam1().getValueAt(5,1);
						
												
			if (!((String) wind.getTabParam1().getValueAt(0,1)).equals("1")) {
				panelImageB = new ImagePanelIRMp(chemReco, Integer.parseInt(width),Integer.parseInt(height),Integer.parseInt(depth)/2,typeData);
				panelImageB.setPreferredSize(dim);
				wind.getBoxImage().removeAll();
				wind.getBoxImage().add(panelImageB);
				
				wind.getSlidImage().setMinimum(0);
				wind.getSlidImage().setMaximum(Integer.parseInt(depth)-1);
				wind.getSlidImage().setValue(Integer.parseInt(depth)/2);
				
				wind.getSlidImage().addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						JSlider sl = (JSlider) e.getSource();
						if (sl.getValueIsAdjusting()) {
							panelImageB = new ImagePanelIRMp(chemReco, Integer.parseInt(width),Integer.parseInt(height),sl.getValue(),typeData);
							panelImageB.setPreferredSize(dim);
							wind.getBoxImage().removeAll();
							wind.getBoxImage().add(panelImageB);
							wind.getTabbedPane().updateUI();
						}
					}
				});
				wind.getSlidImage().setEnabled(true);
			}
			
			else {
				panelImageB = new ImagePanelIRMp(chemReco, Integer.parseInt(width),1,0,typeData);
				panelImageB.setPreferredSize(dim);
				wind.getBoxImage().removeAll();
				wind.getBoxImage().add(panelImageB);
			}
			} catch (Exception e) {
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
				}
		
		
//		else {
//				wind.resetTabParam();
//				wind.resetBoxImage();
//				wind.getSlidImage().setEnabled(false);
//		}
		

}
	

	
	public void mouseClicked(MouseEvent e) {
		if	(e.getClickCount()==2) {

			if (format.contains("Bruker")) 
				openBruker (wind.getListSeq().getSelectionPaths()[0]);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		
		boolean en = false;
		
		if (wind.getListSeq().getSelectionCount()==1) en=true;
		
		if (e.getButton()== MouseEvent.BUTTON3) {
						
			JPopupMenu popupMenu = null;
			
			if (format.contains("Bruker") && !wind.getListSeq().getSelectionPath().getLastPathComponent().toString().contains("MultiReco")) {
			popupMenu = new JPopupMenu();

		    JMenuItem sampleMenuItem = new JMenuItem("Open image(s) Ctrl+O");
		    sampleMenuItem.addActionListener(actionPop);
		    popupMenu.add(sampleMenuItem);
		    
		    popupMenu.addSeparator();

		    JMenuItem scaleXYMenuItem = new JMenuItem("Open image by scale X-Y");
		    scaleXYMenuItem.addActionListener(actionPop);
		    scaleXYMenuItem.setEnabled(en);
		    popupMenu.add(scaleXYMenuItem);
		    
		    JMenuItem scaleYXMenuItem = new JMenuItem("Open image by scale Y-X");
		    scaleYXMenuItem.addActionListener(actionPop);
		    scaleYXMenuItem.setEnabled(en);
		    popupMenu.add(scaleYXMenuItem);
		    
		    popupMenu.addSeparator();
		    
		    JMenuItem openMethod = new JMenuItem("see 'method' file ");
		    openMethod.addActionListener(actionPop);
		    openMethod.setEnabled(en);
		    popupMenu.add(openMethod);
		    
		    JMenuItem openAcqp = new JMenuItem("see 'acqp' file ");
		    openAcqp.addActionListener(actionPop);
		    openAcqp.setEnabled(en);
		    popupMenu.add(openAcqp);
		    
		    JMenuItem openVisuPars = new JMenuItem("see 'visu_pars' file ");
		    openVisuPars.addActionListener(actionPop);
		    openVisuPars.setEnabled(en);
		    popupMenu.add(openVisuPars);
		    
		    JMenuItem openReco = new JMenuItem("see 'reco' file ");
		    openReco.addActionListener(actionPop);
		    openReco.setEnabled(en);
		    popupMenu.add(openReco);
		    
		    popupMenu.addSeparator();
		    
		    JMenu mriprocess = new JMenu("MRI processor");
    
		    JMenuItem pcasl = new JMenuItem(new Pcaslp(wind,"pcasl","Bruker"));
		    	    
		    mriprocess.add(pcasl);
		    //mriprocess.setEnabled(en);
		    popupMenu.add(mriprocess);
		    
		    try {

		    if (wind.getTabParam1().getValueAt(22,1).toString().split(" ").length < 3) pcasl.setEnabled(false);
		    } catch (Exception e1) {
		    	new GetStackTracep(e1);
				PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
		    }
		    
		    popupMenu.addSeparator();
		   	    
		    popupMenu.show(e.getComponent(), e.getX(), e.getY());
		    
			}
		}
	}
	
	
	ActionListener actionPop = new ActionListener() {

		public void actionPerformed(ActionEvent actionEvent) {
						
			if (format.contains("Bruker")) 
				if (actionEvent.getActionCommand().contains("Open")) {
					for (int i=0;i<wind.getListSeq().getSelectionPaths().length;i++) {
							openBruker (wind.getListSeq().getSelectionPaths()[i]);
						}
				}
				
				if (actionEvent.getActionCommand().contains("method")) {
					showParamFileBruker(chemReco,"method");
				}
				
				if (actionEvent.getActionCommand().contains("acqp")) {
					showParamFileBruker(chemReco,"acqp");
				}
				
				if (actionEvent.getActionCommand().contains("visu_pars")) {
					showParamFileBruker(chemReco,"visu_pars");
				}
				
				if (actionEvent.getActionCommand().contains("reco")) {
					showParamFileBruker(chemReco,"reco");
				}
		}
	};
	
	private void openBruker(TreePath seqS) {
		String seqSel;
		
		if (seqS.getPathCount()==2) 
			 seqSel = seqS.getPathComponent(1).toString();
		else seqSel = seqS.getPathComponent(2).toString()+" - "+seqS.getPathComponent(1).toString();
		
		try{
		if (ListSequencep.hm.get(seqSel).length==1) {
			ListParamBrukerp listparamR = new ListParamBrukerp("",ListSequencep.hm.get(seqSel)[0].substring(0,ListSequencep.hm.get(seqSel)[0].indexOf("2dseq")));
			listparamR.listParamB1();
			listparamR.listParamB3();
			listparamR.fillBruker(seqSel);
		}
		new OpenBrukerp(wind,ListSequencep.hm.get(seqSel)[0],"Open by sample",true,ListSequencep.hm.get(seqSel),seqSel).start();
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
			return;
		}
	}
	
	private void showParamFileBruker (String chemReco, String keyword) {
		
		String pathFile=null;
		String tm="";
		boolean fileOk=false;
		
		chemReco=chemReco.substring(0,chemReco.lastIndexOf(separator)+1);
		
		if (keyword.contentEquals("visu_pars")) {
			pathFile = chemReco+"visu_pars";
		}
		
		if (keyword.contentEquals("reco")) {
			pathFile = chemReco+"reco";
		}
		
		if (keyword.contentEquals("acqp")) {
			pathFile = chemReco.substring(0, chemReco.indexOf("pdata"))+"acqp";
			
		}
		
		if (keyword.contentEquals("method")) {
			pathFile = chemReco.substring(0, chemReco.indexOf("pdata"))+"method";
		}
		
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(pathFile));
			StringWriter out = new StringWriter();
			int b;
			while ((b=in.read()) != -1)
	           out.write(b);
			out.flush();
			out.close();
			in.close();
			tm=out.toString();
			fileOk=true;
			
		}
	    catch (IOException e) {
	    	new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
	    }
	
	if (fileOk) {
		
		//JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame frame = new JFrame(pathFile);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   // frame.setSize(100, 200);
	    
	    JPanel panel = new JPanel();
	    panel.setLayout(new FlowLayout());
	    
	    JTextArea textAreal = new JTextArea(tm);
	    textAreal.setEditable(false);
	    
	    JScrollPane scrollPane = new JScrollPane(textAreal);
	    scrollPane.setPreferredSize(new Dimension(600, 500));
	    
	    panel.setLayout(new BorderLayout());
	    panel.add(scrollPane, BorderLayout.CENTER);
	   		 
	    frame.getContentPane().add(panel);
	    frame.pack();
	    frame.setVisible(true);
	}
	}

	

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && e.getKeyCode()== KeyEvent.VK_O) {
			
			wind.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			if (format.contains("Bruker")) 
					for (int i=0;i<wind.getListSeq().getSelectionPaths().length;i++) {
							openBruker (wind.getListSeq().getSelectionPaths()[i]);
			}
			wind.setCursor(null);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}



