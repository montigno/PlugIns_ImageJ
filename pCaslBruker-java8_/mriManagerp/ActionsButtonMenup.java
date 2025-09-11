package mriManagerp;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreeSelectionModel;

import brukerParavisionp.ImagePanelIRMp;
import brukerParavisionp.TableBrukerDatap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

import java.net.URL;
import java.util.Locale;



public class ActionsButtonMenup extends AbstractAction  {
	
	private static final long serialVersionUID = 1L;
	private PcaslBruker_ wind;
	private File FilestmpRep;
	private BufferedReader lectRep;
	private String lectBruker;
	private String lectBrukertmp="none";
	private String separator = File.separator;
	public boolean rawOn = false;
	private JDialog drep=null;
	private JTextField repBruker;
	private JButton buttonRepBruker;
	private String formatinTextPath;
	private String repselected = null;
	private Dimension dim;
		
	public static boolean dataIs = false;
				
	public ActionsButtonMenup(PcaslBruker_ wind,String command){
		super(command);
		this.wind=wind;
		lectBruker 	= new JFileChooser().getCurrentDirectory().toString();
		new UtilsSystemp();
		FilestmpRep = new File(UtilsSystemp.pathOfJar()+separator+"FilestmpRep.txt");
	}
	
//	public ActionsButtonMenu() {
//		
//	}

	@Override
	public void actionPerformed(ActionEvent evt) {
				
		dim = wind.getBoxImage().getPreferredSize();
	
		/*button Quit action */
		if (evt.getActionCommand().contentEquals("Quit")) {
			wind.dispose();
			wind.getFenTab().dispose();
			wind.getFenInfo().dispose();
			wind.getFenAbout().dispose();
			wind.getFenBug().dispose();
			
		}
		
		/* button Directory action */
		if (evt.getActionCommand().contentEquals("Bruker") || evt.getActionCommand().contentEquals("Open Bruker (PV5,PV6)"))  
				{	wind.getListSeq().getSelectionModel().setSelectionMode (TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
					rawOn=true; formatinTextPath="[Bruker]    ";}

	
		
		if (rawOn) {
			
			if (FilestmpRep.exists()){
				try {
					lectRep 	= new BufferedReader(new FileReader(FilestmpRep));
					lectBruker 	= lectRep.readLine();
					lectBruker	= lectBruker.substring(lectBruker.indexOf(" ")+1);
					if (lectRep!=null)
					lectRep.close();	
			}
			catch (Exception e2) {
				new GetStackTracep(e2);
				PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
				} 
			}
						
			FileView view =null;
			
			final JFileChooser rep = new JFileChooser();
			rep.setAcceptAllFileFilterUsed(false);
			rep.setApproveButtonText("Select this directory");
			rep.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			rep.setLocale(Locale.ENGLISH);
			rep.updateUI();
			rep.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
						 if(e.getClickCount() == 2) {
						 File file = rep.getSelectedFile();
						 if (file.isDirectory()) rep.approveSelection();
						 else {
							 rep.setCurrentDirectory(file);
							 rep.rescanCurrentDirectory();
						 }
					 }
				}
			});
		
						
			if (rawOn) {
				if (!lectBrukertmp.contentEquals("none")) lectBruker=lectBrukertmp;
				rep.setSelectedFile(new File(lectBruker));
				rep.setCurrentDirectory(new File(lectBruker));
				view = new BrukerFileView();
				rep.setDialogTitle("Choose Bruker Data");
				}
			
			if (wind.getIconCheck().getState())
					rep.setFileView(view);

			wind.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
//			if (!wind.getTextPath().getText().isEmpty()) 
//				lecttampon=wind.getTextPath().getText();
				

			switch (rep.showOpenDialog(wind)) {	
				
			case JFileChooser.APPROVE_OPTION :
				
				repselected=rep.getSelectedFile().getPath();
								
				if (!rep.getSelectedFile().isDirectory()) repselected=repselected.substring(0,repselected.lastIndexOf(separator));
				else if (repselected.substring(repselected.length()-1).contentEquals(separator)) repselected=repselected.substring(0,repselected.length()-1); 
												
				File tmpfile = new File (repselected);
					
				if (rawOn) {
					FilenameFilter filterSubject = new FilenameFilter() {
						public boolean accept(File dir, String name) {
							 if(name.contains("subject"))
						           return true;
						               return false;
						}
					};
					if (tmpfile.list(filterSubject).length!=0) 
						repselected=repselected.substring(0,repselected.lastIndexOf(separator));
						lectBrukertmp=repselected;
				}
										

										
			if (wind.getTextPath().getItemCount()!=0) {
						wind.getTextPath().insertItemAt(formatinTextPath+repselected,0);
						wind.getTextPath().setSelectedIndex(0);
			}
			else {
						wind.getTextPath().addItem(formatinTextPath+repselected);
			}
					
			rawOn=false;
			break;
			}
			
			rep.setSelectedFile(null);
			
		}
			
			wind.setCursor(null);

			if (evt.getActionCommand().contentEquals("comboBoxChanged")) {
								
					wind.getBottom().removeAllChildren();
					wind.getListSeq().updateUI();
					
					repselected=wind.getTextPath().getSelectedItem().toString();
									
					formatinTextPath=repselected.substring(repselected.indexOf("[")+1, repselected.indexOf("]"));
					repselected=repselected.substring(12);
																			
					if (formatinTextPath.contentEquals("Bruker")) rawOn=true;
					
					
					wind.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					
					try {
														
					if (rawOn) {
						TableBrukerDatap model = new TableBrukerDatap(repselected);
						TableRowSorter<TableBrukerDatap> sorter = new TableRowSorter<TableBrukerDatap>(model);
						wind.getTabData().setModel(model);
						wind.getTabData().setRowSorter(sorter);
						wind.getTabData().setEnabled(true);
						wind.getTabData().getRowSorter().toggleSortOrder(4);
					}
										
					} catch (Exception e1) {
						//wind.getInfoText().setText(String.valueOf(e1));
						new GetStackTracep(e1);
						PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
						
					}
					
//					if (wind.getTextPath().getItemCount()!=0) {
//						wind.getTextPath().insertItemAt(formatinTextPath+repselected,0);
//						wind.getTextPath().setSelectedIndex(0);
//					}
//					else {
//						wind.getTextPath().addItem(formatinTextPath+repselected);
//					}
					wind.getTextPath2dseq().setText("");
					wind.getInfoText().setText("");
					//wind.setCursor(null);
					wind.resetTabParam();
										
					ImagePanelIRMp panelImage;
					panelImage = new ImagePanelIRMp(null);
					panelImage.setPreferredSize(dim);
					wind.getBoxImage().removeAll();
					wind.getBoxImage().add(panelImage);
					wind.getFenTab().validate();
					wind.getSlidImage().setEnabled(false);
					
					wind.setCursor(null);

					rawOn=false;
			}

			
		/* menu Help clicked */
		if (evt.getActionCommand().contentEquals("Help")) {
			URL res = getClass().getResource("/Help.png");
			ImageIcon iconhelp = new ImageIcon(res);
			
			JFrame dialogHelp = new JFrame("Help");
			dialogHelp.setSize(new Dimension(800,1000));
			
			JLabel imgLabel = new JLabel("",iconhelp,JLabel.CENTER);
			
			JPanel imgPanel = new JPanel(new BorderLayout());
			imgPanel.setMinimumSize(new Dimension(800,1000));
			imgPanel.add(imgLabel,BorderLayout.CENTER);
						
			JScrollPane scroll = new JScrollPane(imgPanel);
			scroll.setPreferredSize(new Dimension(1200,1200));
			//scroll.add(imgPanel);
			
			//dialogHelp.add(imgLabel);
			dialogHelp.add(scroll);
			dialogHelp.setVisible(true);
		}
				
		/* menu About clicked */
		if (evt.getActionCommand().contentEquals("About..")) {
			wind.getFenAbout().setVisible(true);
		}
				
		
		/* menu Bug report clicked */
		if (evt.getActionCommand().contentEquals("Error window")) {
			wind.getFenBug().setVisible(true);
		}
		
		
		/* menu Show Info clicked */
		if (evt.getActionCommand().contentEquals("Detailed file window")) {
			wind.getFenInfo().setVisible(true);
		}
		
		/* menu Current working directory clicked */
		if (evt.getActionCommand().contentEquals("Current working directory")) {
	
			if (FilestmpRep.exists()){
			try {
				lectRep = new BufferedReader(new FileReader(FilestmpRep));
				lectBruker = lectRep.readLine();
				lectBruker=lectBruker.substring(lectBruker.indexOf(" ")+1);
				lectRep.close();	
				}
				catch (Exception e2) {
					new GetStackTracep(e2);
					PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
				} 
			}
		int widthScreen,heightScreen;
		/*Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		widthScreen=(int) screen.getWidth();
		heightScreen=(int) screen.getHeight();*/
		widthScreen=1280;
		heightScreen=1024;
		
		drep = new JDialog();
		drep.setLayout(new GridLayout(8,1));
		
		JPanel panelBruker = new JPanel();
		panelBruker.setLayout(new BoxLayout(panelBruker,BoxLayout.LINE_AXIS));
		URL imgURLB = getClass().getResource("/BrukerLogo32p.jpg");
		ImageIcon iconBruker = new ImageIcon(imgURLB);
		JLabel labelBruker = new JLabel("Bruker directory :          ",iconBruker,JLabel.CENTER);
		repBruker = new JTextField();
		repBruker.setEditable(false);
		repBruker.setPreferredSize(new Dimension(widthScreen/3, heightScreen/35));
		repBruker.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		repBruker.setText(lectBruker);
					
		JScrollPane jspBruker = new JScrollPane(repBruker);
		jspBruker.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		buttonRepBruker = new JButton("Change Dir.");
		buttonRepBruker.addActionListener(actionChooseRep);

		panelBruker.add(labelBruker);
		panelBruker.add(jspBruker);
		panelBruker.add(Box.createHorizontalStrut(5));
		panelBruker.add(buttonRepBruker);
		panelBruker.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		

				
		JPanel panelOk = new JPanel();
		JButton repOk = new JButton("Ok");
		repOk.addActionListener(actionOkRep);
		panelOk.add(repOk);
		
		drep.add(panelBruker);
		drep.add(panelOk);
		/*Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int widthScreen,heightScreen;
		widthScreen=(int) screen.getWidth();
		heightScreen=(int) screen.getHeight();*/
		//drep.setSize(widthScreen/2, heightScreen/10);
		drep.setResizable(true);
	    drep.setAlwaysOnTop(true);
	    drep.setLocationRelativeTo(wind.getJMenuBar());
		drep.setVisible(true);
		drep.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		drep.pack();
		drep.pack();
		}
	}
	
	ActionListener actionChooseRep = new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
								
				
				JFileChooser repch = new JFileChooser();
				String nomrep = null;
				
				repch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				repch.setApproveButtonText("Select this directory");
				repch.setLocation(drep.getX(), drep.getY()+drep.getHeight());
				repch.setLocale(Locale.ENGLISH);
				repch.updateUI();
				
				
				if (FilestmpRep.exists()){
					try {
						lectRep = new BufferedReader(new FileReader(FilestmpRep));
						lectBruker = lectRep.readLine();
						lectBruker=lectBruker.substring(lectBruker.indexOf(" ")+1);
						lectRep.close();	
						
						if (e.getSource()==buttonRepBruker && lectBruker!=null){
							repch.setCurrentDirectory(new File(lectBruker));
							repch.setDialogTitle("Bruker directory ?");
						}
					}
						catch (Exception e2) {
							new GetStackTracep(e2);
							PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());

						} 
					}

				if (repch.showOpenDialog(drep) == JFileChooser.APPROVE_OPTION) {	
					nomrep=repch.getSelectedFile().getPath();
					if (e.getSource()==buttonRepBruker) 	repBruker.setText(nomrep);
				}
			}
		};
		
	ActionListener actionOkRep = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String repCh = null;
								
				repCh="[Bruker] "+repBruker.getText()+"\r\n[DicomBr] ";
				
				if (FilestmpRep.exists())
				FilestmpRep.delete();
			
			try {
				FilestmpRep.createNewFile();
				FileWriter printRep = new FileWriter(FilestmpRep);
				printRep.write(repCh);
				printRep.close();
			} catch (Exception e1) {
				new GetStackTracep(e1);
				PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
			}
			drep.dispose();
				
				
			}
		};
}

class BrukerFileView extends FileView {
		
	public Icon getIcon(File file) {
				
		URL imgURL = getClass().getResource("/BrukerLogo32p.jpg");
		ImageIcon BrukerIcon = new ImageIcon(imgURL);
		
		File f = new File(file.getPath());
		
		if (f.isDirectory()) {
			if (searchSubject("subject", file)) return BrukerIcon;
		}
		
		return null;
	}
	
	public Boolean isTraversable(File file) {
		if (file.isDirectory()) 
			if (searchSubject("subject", file)) return false;
		return true;
	}
	
	public boolean searchSubject (String fileToFind, File searchIn) {
        String[] listOfFiles = searchIn.list();
        boolean find = false;
        int i = 0;
        
        //System.out.println(i < listOfFiles.length);
        
        try {
        while (i < listOfFiles.length && !find) {
        	if (listOfFiles[i].contentEquals(fileToFind)) {
        		find=true;
        		break;
        	}
           	i++;
        }
        }
        catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());	
        }
        return find;
	}
}







