package mriManagerp;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;

import brukerParavisionp.ChangeSubjectp;
import brukerParavisionp.ImagePanelIRMp;
import brukerParavisionp.TableBrukerDatap;

public class ActionSelectionDatap implements MouseListener, KeyListener {

	private String dataSelected="";
	private PcaslBruker_ wind;
	private String separator = File.separator;
		
		
	public ActionSelectionDatap(PcaslBruker_ wind) {
		this.wind=wind;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (SwingUtilities.isLeftMouseButton(arg0)) {
			wind.getListSeq().invalidate();	 //to avoid bug
			goSelectionData();
		}
		
		try{
		if (wind.getTabData().getValueAt(0,0).toString().contains("BrukerLogo") ) {	
				if (SwingUtilities.isRightMouseButton(arg0) && !dataSelected.isEmpty() 
						&& !wind.getTabData().getValueAt(wind.getTabData().getSelectedRow(),0).toString().contains("DicomLogo")) {
					JPopupMenu popupMenuData = new JPopupMenu();
			
					JMenuItem patientMenuItem = new JMenuItem("change Patient name");
					patientMenuItem.addActionListener(actionListener);
					popupMenuData.add(patientMenuItem);
		    
					popupMenuData.addSeparator();
		    
					JMenuItem studyMenuItem = new JMenuItem("change Study name");
					studyMenuItem.addActionListener(actionListener);
					popupMenuData.add(studyMenuItem);   
		    
					popupMenuData.show(arg0.getComponent(), arg0.getX(), arg0.getY());
				}
		}
		}
		catch(Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}	

	@Override
	public void keyPressed(KeyEvent arg1) {
	}	

	@Override
	public void keyReleased(KeyEvent arg1) {
		
		if (arg1.getKeyCode() == KeyEvent.VK_DOWN || arg1.getKeyCode() == KeyEvent.VK_UP )
			goSelectionData();
	}

	@Override
	public void keyTyped(KeyEvent arg1) {
		
	}

	private void goSelectionData()  {
			
			wind.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
			try {
				if (wind.getTabData().getValueAt(0, 0)!="no Bruker data in this directory" 	&&
					wind.getTabData().getValueAt(0, 0)!="no Dicom data in this directory" 	&&
					wind.getTabData().getValueAt(0, 0)!="no Par/Rec data in this directory" &&
					wind.getTabData().getValueAt(0, 0)!="no NifTI data in this directory" 	&&
					wind.getTabData().getValueAt(0, 0)!="no Matlab data in this directory" 	&& 
					wind.getTabData().getValueAt(0, 0)!="no Analyze data in this directory" && 
					!wind.getTabData().getValueAt(0, 0).toString().isEmpty()) {
					wind.getTextPath2dseq().setText(null);
					
					dataSelected=wind.getTextPath().getSelectedItem().toString();
					dataSelected=dataSelected.substring(12);
					dataSelected+=separator+wind.getTabData().getValueAt(wind.getTabData().getSelectedRow(), 1);
										
					if (!dataSelected.isEmpty())
							new ListSequencep(wind, dataSelected);
				}
			} catch (Exception e){
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
			}
			
			if (wind.getListSeq().getRowCount()==0){
				
				Dimension dim = wind.getBoxImage().getPreferredSize();
				ImagePanelIRMp panelImage;
				try {
					//TabParam model0 = new TabParam("", 0,"","");
					Object[][] data = new Object[1][2];
					data[0][0] = " ";
					data[0][1] = " ";
					
					TabParamp model0 = new TabParamp(data);
					TableRowSorter<TabParamp> sorter0 = new TableRowSorter<TabParamp>(model0);
					wind.getTabParam1().setModel(model0);
					wind.getTabParam1().setRowSorter(sorter0);
					wind.getTabParam2().setModel(model0);
					wind.getTabParam2().setRowSorter(sorter0);
					wind.getTabParam3().setModel(model0);
					wind.getTabParam3().setRowSorter(sorter0);
					panelImage = new ImagePanelIRMp(null);
					panelImage.setPreferredSize(dim);
					wind.getBoxImage().removeAll();
					wind.getBoxImage().add(panelImage);
	
				} catch (Exception e) {
					new GetStackTracep(e);
					PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
				}
			}
			wind.setCursor(null);
	}
	
	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			
			String cheminSubject;
			cheminSubject=dataSelected+separator+"subject";
			String defautName = null;
			String newName = null;
			int rowSelected = wind.getTabData().getSelectedRow();
			boolean isChanged = true;
			
			
			if (actionEvent.getActionCommand().contentEquals("change Patient name")) {
				defautName=""+wind.getTabData().getValueAt(wind.getTabData().getSelectedRow(), 2);
				newName = (String)JOptionPane.showInputDialog(wind, "new Patient name",dataSelected,JOptionPane.QUESTION_MESSAGE,null,null,defautName);
				if (newName==null && defautName!=null) {
					isChanged = false;
					newName=defautName;
				}
			};
			
			if (actionEvent.getActionCommand().contentEquals("change Study name")) {
				defautName=""+wind.getTabData().getValueAt(wind.getTabData().getSelectedRow(), 3);
				newName = (String)JOptionPane.showInputDialog(wind, "new Study name",dataSelected,JOptionPane.QUESTION_MESSAGE,null,null,defautName);
				if (newName==null && defautName!=null) {
					isChanged = false;
					newName=defautName;
				}
			};
			
			wind.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			if (isChanged) {
				
			try {
				new ChangeSubjectp(cheminSubject, newName,actionEvent.getActionCommand());
			} 	catch (IOException e) {
				new GetStackTracep(e);
				PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
				
				}

			String nomfichier = wind.getTextPath().getSelectedItem().toString();
			nomfichier=nomfichier.substring(12);
								
			TableBrukerDatap model;
			TableRowSorter<TableBrukerDatap> sorter;
			
			wind.getBottom().removeAllChildren();
			wind.getListSeq().updateUI();
													
			try {
				model = new TableBrukerDatap(nomfichier);
				sorter = new TableRowSorter<TableBrukerDatap>(model);
				wind.getTabData().setModel(model);
				wind.getTabData().setRowSorter(sorter);
				wind.getTabData().setEnabled(true);
				wind.getTabData().getRowSorter().toggleSortOrder(4);
									
			} catch (IOException e1) {
				new GetStackTracep(e1);
				PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
			}
			
			wind.getTabData().setRowSelectionInterval(rowSelected, rowSelected);
			goSelectionData();
			}
	
			wind.setCursor(null);
		}
	};


}

