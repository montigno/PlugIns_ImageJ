import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import ij.plugin.PlugIn;
import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.WindowManager;

public class AtlasIrmage_ extends JFrame implements PlugIn, ImageListener {

	private static final long serialVersionUID = 1L;

	static JList<String> listImageToWand;
	static List<String> list_img;
	static JComboBox<String> listLabelFile;
	static String[] list_all_img;
	JComboBox<String> listLabelText;
	ImagePlus imp;
	String version="1.0";
	static HashMap<String, String> dict_label = new HashMap<String, String>();

	@Override
	public void run(String arg) {
		if (WindowManager.getImageTitles().length == 0) {
			IJ.showMessage("No images");
			return;
		}
		ImagePlus.addImageListener(this);
		new AtlasIrmage_().main();
	}

	public void main() {

		JPanel panelHead = new JPanel();
		panelHead.setLayout(null);
		panelHead.setBorder(BorderFactory.createLineBorder(Color.gray));

		JLabel label0 = new JLabel("Labels Text");
		label0.setBounds(10, 10, 100, 25);
		String path_label_txt = IJ.getDirectory("plugins") + "Atlas_Irmage" + File.separator + "_AtlasText"
				+ File.separator;
		File builderDirectory = new File(path_label_txt);
		File[] builderFiles = builderDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});

		String[] list_txt_label = new String[builderFiles.length];
		for (int i = 0; i < builderFiles.length; i++)
			list_txt_label[i] = builderFiles[i].getName();

		listLabelText = new JComboBox<>();
		listLabelText.setBounds(10, 30, 800, 25);
		listLabelText.setEnabled(true);
		listLabelText.setEditable(false);
		listLabelText.setModel(new DefaultComboBoxModel<String>(list_txt_label));
		change_dict();
		listLabelText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				change_dict();

			}
		});
//		dict_labels(IJ.getDirectory("plugins") + "Atlas_Irmage" + File.separator
//				+ listLabelText.getSelectedItem().toString());

		JLabel label1 = new JLabel("Labels Image");
		label1.setBounds(10, 60, 100, 25);
		list_all_img = WindowManager.getImageTitles();
		AtlasIrmage_.list_img = Arrays.asList(list_all_img);
		listLabelFile = new JComboBox<>();
		listLabelFile.setBounds(10, 80, 800, 25);
		listLabelFile.setEnabled(true);
		listLabelFile.setEditable(false);
		listLabelFile.setModel(new DefaultComboBoxModel<String>(list_all_img));
//		listLabelFile.setUI(new myComboUI());

		JLabel label2 = new JLabel("List Image");
		label2.setBounds(10, 110, 100, 25);
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String item : list_all_img) {
			if (!item.contentEquals(listLabelFile.getSelectedItem().toString()))
				listModel.addElement(item);
		}
		listImageToWand = new JList<>(listModel);
		listImageToWand.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listImageToWand.setLayoutOrientation(JList.VERTICAL);
		listImageToWand.setVisibleRowCount(-1);
		listImageToWand.setSelectionInterval(0, listImageToWand.getModel().getSize() - 1);
		JScrollPane listScroller = new JScrollPane(listImageToWand);
		listScroller.setBounds(10, 130, 800, 150);

		JButton button_load = new JButton("Start");
		button_load.setBounds(10, 300, 80, 25);
		button_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				go_atlas();
			}
		});
		JButton button_stop = new JButton("Stop");
		button_stop.setBounds(120, 300, 80, 25);
		button_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stop_atlas();
			}
		});

		listLabelFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().contentEquals("comboBoxChanged")) {
					DefaultListModel<String> listModel = new DefaultListModel<String>();
//					list_img = AtlasIrmage_.list_img.toArray(new String[0]);
					for (String item : list_all_img) {
						if (!item.contentEquals(listLabelFile.getSelectedItem().toString()))
							listModel.addElement(item);
					}
					listImageToWand.setModel(listModel);
					listImageToWand.setSelectionInterval(0, listImageToWand.getModel().getSize() - 1);
				}
			}
		});

		panelHead.add(label0);
		panelHead.add(listLabelText);
		panelHead.add(label1);
		panelHead.add(listLabelFile);
		panelHead.add(label2);
		panelHead.add(listScroller);
		panelHead.add(button_load);
		panelHead.add(button_stop);

		getContentPane().add(panelHead);
		setSize(new Dimension(850, 380));
		setTitle("Atlas Irmage " + version);
		setVisible(true);
	}

	public void change_dict() {
		dict_labels(IJ.getDirectory("plugins") + "Atlas_Irmage" + File.separator + "_AtlasText" + File.separator
				+ listLabelText.getSelectedItem().toString());
	}

	public void dict_labels(String label_txt) {
		IJ.log(label_txt);
		AtlasIrmage_.dict_label.clear();
		try {
			Scanner scanner = new Scanner(new File(label_txt));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] line_arr = null;
				if (!line.contains("#")) {
					line_arr = line.split("\t", -1);
					AtlasIrmage_.dict_label.put(line_arr[0], line_arr[1]);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void stop_atlas() {
		listLabelText.setEnabled(true);
		listLabelFile.setEnabled(true);
		listImageToWand.setEnabled(true);
		try {
			imp.getCanvas().removeMouseListener(imp.getCanvas().getMouseListeners()[0]);
		} catch (Exception err) {
		}
	}

	public void go_atlas() {
		listLabelText.setEnabled(false);
		listLabelFile.setEnabled(false);
		listImageToWand.setEnabled(false);
		String lab_img = listLabelFile.getSelectedItem().toString();
		list_img = listImageToWand.getSelectedValuesList();
		String list_synchr = String.join(",", list_img);
		IJ.run("Synchronize Windows", list_synchr);

		imp = WindowManager.getImage(lab_img);
		imp.getCanvas().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int posX = imp.getCanvas().getCursorLoc().x;
				int posY = imp.getCanvas().getCursorLoc().y;
				int pixel_value = (int) imp.getProcessor().getValue(posX, posY);
				String px_value = String.valueOf(pixel_value);
				IJ.log(px_value + " : " + AtlasIrmage_.dict_label.get(px_value));
				IJ.doWand(posX, posY);
				IJ.setThreshold(pixel_value, pixel_value, "raw");
				IJ.run("Create Selection");
				doWandAll(posX, posY);
				IJ.selectWindow(lab_img);
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

		});
	}

	public void doWandAll(int posx, int posy) {
		for (String ef : list_img) {
			try {
				IJ.selectWindow(ef);
				IJ.run("Restore Selection");
			} catch (Exception err) {
			}
		}
	}

	public class myComboUI extends BasicComboBoxUI {
		protected ComboPopup createPopup() {
			BasicComboPopup popup = new BasicComboPopup(comboBox) {
				private static final long serialVersionUID = 1L;

				protected JScrollPane createScroller() {
					return new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
							ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				}
			};
			return popup;
		}
	}

	@Override
	public void imageOpened(ImagePlus imp) {
		DefaultListModel<String> deflist = (DefaultListModel<String>) AtlasIrmage_.listImageToWand.getModel();
		deflist.addElement(imp.getTitle());
		IJ.log(String.valueOf(deflist.size()));
		AtlasIrmage_.list_img.add(imp.getTitle());
		AtlasIrmage_.listImageToWand.removeAll();
		AtlasIrmage_.listImageToWand.setModel(deflist);
		AtlasIrmage_.listImageToWand.setSelectionInterval(0, deflist.getSize() - 1);
		AtlasIrmage_.listLabelFile.addItem(imp.getTitle());
		AtlasIrmage_.list_all_img = WindowManager.getImageTitles();
	}

	@Override
	public void imageClosed(ImagePlus imp) {
		DefaultListModel<String> deflist = (DefaultListModel<String>) AtlasIrmage_.listImageToWand.getModel();
		List<String> tmplistimg = new ArrayList<String>();
		for (String a : AtlasIrmage_.list_img) {
			if (a.contentEquals(imp.getTitle())) {
				tmplistimg.add(a);
				deflist.removeElement(a);
			}
		}
		AtlasIrmage_.list_img.removeAll(tmplistimg);
		AtlasIrmage_.listImageToWand.removeAll();
		AtlasIrmage_.listImageToWand.setModel(deflist);
		AtlasIrmage_.listImageToWand.setSelectionInterval(0, deflist.getSize() - 1);
		AtlasIrmage_.listLabelFile.removeItem(imp.getTitle());
		AtlasIrmage_.list_all_img = WindowManager.getImageTitles();
	}

	@Override
	public void imageUpdated(ImagePlus imp) {
		// TODO Auto-generated method stub
	}
}
