package mriManagerp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import brukerParavisionp.ImagePanelIRMp;
import ij.IJ;
import ij.plugin.PlugIn;


public class PcaslBruker_ extends JFrame implements PlugIn {

	private static final long serialVersionUID = 7780120094880598555L;
	private JFrame fenInfo;
	private JFrame fenTab;
	private JFrame fenBug;
	
	private JComboBox<String> textPath;

	private JSplitPane splitPane;
	private JTable dataList;
	private JTree seqList;
	
	private JTextArea textPath2dseq;
	
	private JTextArea textInfo;
	private static JTextArea textBug;
	
	private JTabbedPane tabbedPane;
	private JTable tabParam1,tabParam2,tabParam3;
	
	private Box boxImage;
	private Box boxSlid;
	private JSlider slidImage;
	
	private JDialog dinfo;
	private DefaultMutableTreeNode bottom;
	private ImagePanelIRMp previewImage;
	public static int widthScreen,heightScreen;
	
	private JCheckBoxMenuItem showicon;
	
	private JMenuItem exportNifti,exportAnalyze;
	
	public static String OS = System.getProperty("os.name").toLowerCase();
	
	public void run(String arg) {

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){

				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

				} catch (Exception e) {
					new GetStackTracep(e);
					PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
				}
				widthScreen=1280;
				heightScreen=1024;
				init();
				BrukerFilesWindows();
				buildTab();
				buildInfo();
				buildAbout();
				buildBug();
				}
		});
	}
	
	private void init() {

	}
	
	private void BrukerFilesWindows () {
		
		setTitle("MRI Files Manager (IRMaGe)"); 
		setSize(widthScreen/2,heightScreen*3/4); 
		setResizable(false);
		IJ.getInstance().setLocation(0,0);
		setLocation(0, IJ.getInstance().getY()+IJ.getInstance().getHeight()+10);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		
		URL imgURLB = getClass().getResource("/BrukerLogo32p.jpg");
		ImageIcon iconBruker = new ImageIcon(imgURLB);
				
		JMenuBar menu_bar1 = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu openf = new JMenu("Open");
		JMenu exportf = new JMenu("export");
		JMenu menu1 = new JMenu("Information");
		JMenu option = new JMenu("Option");
		JMenu pref = new JMenu("Preference");
					
		JMenuItem openBruker = new JMenuItem(new ActionsButtonMenup(this,"Open Bruker (PV5,PV6)"));
		openBruker.setIcon(iconBruker);
		
		JMenuItem help = new JMenuItem(new ActionsButtonMenup(this,"Help"));
		JMenuItem about = new JMenuItem(new ActionsButtonMenup(this,"About.."));
		JMenuItem bug = new JMenuItem(new ActionsButtonMenup(this,"Error window"));
		JMenuItem info = new JMenuItem(new ActionsButtonMenup(this,"Detailed file window"));
		JMenuItem repwork = new JMenuItem(new ActionsButtonMenup(this,"Current working directory"));
										
		showicon = new JCheckBoxMenuItem("show icons in the file chooser dialog");
		showicon.setSelected(false);
				
		menu1.add(help);
		menu1.add(about);
		openf.add(openBruker);
	
		file.add(openf);
		file.addSeparator();
		file.add(exportf);
		pref.add(showicon);
		option.add(bug);
		option.add(info);
		option.add(repwork);
		menu_bar1.add(file);
		menu_bar1.add(menu1);
		menu_bar1.add(pref);
		menu_bar1.add(option);
		
				
		//setMenuProject();

								
		this.setJMenuBar(menu_bar1);
		
		JPanel panel = new JPanel(); 
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.GRAY);

		JButton boutonBruker = new JButton(new ActionsButtonMenup(this,"Bruker"));
		JButton boutonQuit = new JButton(new ActionsButtonMenup(this,"Quit"));
		
		boutonBruker.setIcon(iconBruker);
		
		panel.add(boutonBruker);
		panel.add(boutonQuit);
				
		textPath = new JComboBox<String>();
		textPath.addActionListener(new ActionsButtonMenup(this, "comboboxChanged"));
		textPath.setPreferredSize(new Dimension(widthScreen/2-20, heightScreen/40));
		textPath.setForeground(Color.BLUE);
		textPath.setEnabled(true);
		textPath.setEditable(false);
		panel.add(textPath);
		
		Object [][] data = new Object[20][5];
		String[] columnNames = {"Format","Data Bruker","Patient","Study","Date"};
		dataList = new JTable(data, columnNames);
		dataList.setFillsViewportHeight(true);
		dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataList.getTableHeader().setReorderingAllowed(false);
		dataList.setEnabled(false);
		dataList.addMouseListener(new ActionSelectionDatap(this));
		dataList.addKeyListener((KeyListener) new ActionSelectionDatap(this));
			
		bottom = new DefaultMutableTreeNode();
		bottom.add(new DefaultMutableTreeNode());
		seqList = new JTree(bottom);
		seqList.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		//seqList.getSelectionModel().setSelectionMode (TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		seqList.setRootVisible(false);
		seqList.setExpandsSelectedPaths(true);
		seqList.addTreeSelectionListener(new ActionSelectionSeqp(this));
		seqList.addMouseListener(new ActionSelectionSeqp(this));
		seqList.addKeyListener(new ActionSelectionSeqp(this));
		
		JScrollPane treeView1 = new JScrollPane(dataList);
		JScrollPane treeView2 = new JScrollPane(seqList);
		treeView1.setMinimumSize(new Dimension(widthScreen/2-20, heightScreen/4));
		treeView2.setMinimumSize(new Dimension(widthScreen/2-20, heightScreen/4));
        
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,treeView1,treeView2);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerSize(8);
		splitPane.setPreferredSize(new Dimension(widthScreen/2-20, heightScreen/2));
		splitPane.setOneTouchExpandable(true);
        panel.add(splitPane);
        
        textPath2dseq = new JTextArea();
        textPath2dseq.setEditable(false);
		JScrollPane scroll2dseq = new JScrollPane(textPath2dseq);
		scroll2dseq.setPreferredSize(new Dimension(this.getWidth()-20, heightScreen/15));
		panel.add(scroll2dseq);
		
		setContentPane(panel);
			
		setVisible(true);
}
	
	private void buildTab() {
		fenTab = new JFrame();
		fenTab.setTitle("Parameters & Preview"); 
		fenTab.setLocation(new Point(this.getX()+this.getWidth()+10, this.getY()));
		fenTab.setSize(widthScreen/4,this.getHeight());
		fenTab.setResizable(false);
		fenTab.setLayout(new BoxLayout(fenTab, BoxLayout.Y_AXIS));
		Box box1 = new Box(BoxLayout.X_AXIS);
		Box box2 = new Box(BoxLayout.X_AXIS);
		boxImage = new Box(BoxLayout.X_AXIS);
		boxSlid = new Box(BoxLayout.X_AXIS);
						
		JPanel panel2 = new JPanel();
		
		tabParam1 = new JTable();
		tabParam2 = new JTable();
		tabParam3 = new JTable();
		tabParam1.setPreferredSize(new Dimension(widthScreen/4-30, this.getHeight()/2));
		tabParam2.setPreferredSize(new Dimension(widthScreen/4-30, this.getHeight()/2));
		tabParam3.setPreferredSize(new Dimension(widthScreen/4-30, this.getHeight()/2));
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Sequence", tabParam1);
		tabbedPane.addTab("Equipment", tabParam2);
		tabbedPane.addTab("TX/RX", tabParam3);
		
//		previewImage = new ImagePanelIRM(null);
//		previewImage.setPreferredSize(new Dimension(widthScreen/6,heightScreen/5));
									
		JSeparator sep=new JSeparator(SwingConstants.HORIZONTAL);
		sep.setPreferredSize(new Dimension(widthScreen/4-30, heightScreen/50));
		
		slidImage = new JSlider();
		slidImage.setPreferredSize(new Dimension(220,30));
		slidImage.setEnabled(false);
		
		
		box1.add(tabbedPane);
		box2.add(sep);
//		boxImage.add(previewImage);
		resetBoxImage();
		boxSlid.add(slidImage);
				
		panel2.add(box1);
		panel2.add(box2);
		panel2.add(boxImage);
		panel2.add(boxSlid);
		
		fenTab.setContentPane(panel2);
		fenTab.setVisible(true);
	}

	private void buildInfo() {
		fenInfo = new JFrame();
		fenInfo.setTitle("Detailed file window");
		//fenInfo.setLocation(new Point(this.getX(), this.getY()+this.getHeight()));
		fenInfo.setLocationRelativeTo(dataList);
		fenInfo.setResizable(false);
		fenInfo.setSize(widthScreen/2,heightScreen/7);
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout());
		panel3.setBackground(Color.GRAY);
		textInfo = new JTextArea();
		textInfo.setEditable(false);
		JScrollPane scrollPaneArea = new JScrollPane(textInfo);
		scrollPaneArea.setPreferredSize(new Dimension(this.getWidth()-20, heightScreen/10));
		panel3.add(scrollPaneArea);
		fenInfo.setContentPane(panel3);
		//fenInfo.setVisible(true);
	}
		
	private void buildAbout() {
		
		dinfo = new JDialog();
		dinfo.setTitle("About this PlugIn");
		//dinfo.setLayout(new BoxLayout(dinfo, BoxLayout.Y_AXIS));
		
		JPanel panelAbout = new JPanel();
		
		Box boxIma = new Box(BoxLayout.Y_AXIS);
		Box boxTxt = new Box(BoxLayout.Y_AXIS);
		
		JTextArea labelTxt = new JTextArea();
		labelTxt.setPreferredSize(new Dimension(widthScreen/6-18,heightScreen/12));
		labelTxt.setEditable(false);
		labelTxt.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		labelTxt.setText(	"Version 1.3 (2015/07/02) \n" +
							"Developped by Olivier Montigon\n" +
	    					"UMS IRMaGe\n" +
	    					"Unite IRM 3T Recherche\n" +
	    					"38043 Grenoble Cedex 9");
	    
	    URL imgURL = getClass().getResource("/LogoIRMaGep.jpg");
	    ImagePanelIRMp imageAbout = new ImagePanelIRMp(new ImageIcon(imgURL).getImage());
	    imageAbout.setPreferredSize(new Dimension(widthScreen/6-5,heightScreen/5));
	    boxTxt.add(labelTxt, BorderLayout.PAGE_START);
	    boxIma.add(imageAbout, BorderLayout.LINE_END);
	    
	    panelAbout.add(boxTxt, BorderLayout.CENTER);
	    panelAbout.add(boxIma, BorderLayout.CENTER);
	    
	    dinfo.setContentPane(panelAbout);
	    dinfo.setSize(new Dimension((int) (imageAbout.getPreferredSize().getWidth()),(int) (labelTxt.getPreferredSize().getHeight()+imageAbout.getPreferredSize().getHeight()+30)));
	    dinfo.setResizable(false);
	    dinfo.setAlwaysOnTop(true);
	    dinfo.setLocationRelativeTo(this);
	    dinfo.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	   	}
	
	private void buildBug() {
		
		fenBug = new JFrame();
		fenBug.setTitle("Error window");
		//fenBug.setLocation(new Point(this.getX(), this.getY()+this.getHeight()));
		fenBug.setLocationRelativeTo(dataList);
		fenBug.setResizable(true);
		fenBug.setSize(widthScreen/2,heightScreen/5);
		
		JPanel panel4 = new JPanel();
		
		panel4.setLayout(new FlowLayout());
		panel4.setBackground(Color.GRAY);
		textBug = new JTextArea();
		textBug.setEditable(false);
		
		
		JScrollPane scrollPaneArea = new JScrollPane(textBug);
//		scrollPaneArea.setPreferredSize(new Dimension(this.getWidth()/10, heightScreen/10));
		panel4.setLayout(new BorderLayout());
		panel4.add(scrollPaneArea, BorderLayout.CENTER);
		fenBug.setContentPane(panel4);
		//fenBug.pack();

	}
	
	/********* accesseurs**************/
	
	public JComboBox<String> getTextPath() {
		return textPath;
	}
	
	public JTable getTabData() {
		return dataList;
	}
		
	public JTree getListSeq() {
		return seqList;
	}
	
	public DefaultMutableTreeNode getBottom() {
		return bottom;
	}
	
	public JTextArea getTextPath2dseq(){
		return textPath2dseq;
	}
	
	public JFrame getFenTab() {
		return fenTab;
	}
	
	public JTable getTabParam1() {
		return tabParam1;
	}
	
	public JTable getTabParam2() {
		return tabParam2;
	}
	
	public JTable getTabParam3() {
		return tabParam3;
	}
	
	public Box getBoxImage() {
		return boxImage;
	}
		
	public JFrame getFenInfo() {
		return fenInfo;
	}
	
	public JDialog getFenAbout() {
		return dinfo;
	}
	
	public JTextArea getInfoText() {
		return textInfo;
	}
	
	public JFrame getFenBug() {
		return fenBug;
	}
	
	public static JTextArea getBugText() {
		return textBug;
	}
	
	public JSlider getSlidImage() {
		return slidImage;
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	public JCheckBoxMenuItem getIconCheck() {
		return showicon;
	}
	
	public JMenuItem getexportNiftiItem() {
		return exportNifti;
	}
	
	public JMenuItem getexportAnalyzeItem() {
		return exportAnalyze;
	}
	
	
	/********* mutateurs **************/
	
//	public void setPreviewImage(ImagePanelIRM previewImage) {
//		this.previewImage=previewImage;
//	}
	
	
	/******** reset *****************/
	
	public void resetBoxImage() {
		
		URL imgURL = getClass().getResource("/WhiteScreenp.jpg");
		Image img=new ImageIcon(imgURL).getImage();
		previewImage = new ImagePanelIRMp(img);
		previewImage.setPreferredSize(new Dimension(widthScreen/6,heightScreen/5));
		this.getBoxImage().removeAll();
		this.getBoxImage().add(previewImage);
		getBoxImage().updateUI();
	}
	
	public void resetTabParam() {
		TabParamp model0;
		try {
			Object[][] data = new Object[1][2];
			data[0][0] = " ";
			data[0][1] = " ";
			//model0 = new TabParam("", 0,"","");
			model0 = new TabParamp(data);
			TableRowSorter<TabParamp> sorter0 = new TableRowSorter<TabParamp>(model0);
			this.getTabParam1().setModel(model0);
			this.getTabParam1().setRowSorter(sorter0);
			this.getTabParam2().setModel(model0);
			this.getTabParam2().setRowSorter(sorter0);
			this.getTabParam3().setModel(model0);
			this.getTabParam3().setRowSorter(sorter0);
			
		} catch (Exception e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
		}
	}
	
}