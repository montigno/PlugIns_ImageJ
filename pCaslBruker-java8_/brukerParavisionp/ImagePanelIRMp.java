package brukerParavisionp;

import ij.ImagePlus;
import ij.gui.ProfilePlot;
import ij.io.FileInfo;
import ij.io.FileOpener;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class ImagePanelIRMp extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image img;
	private int widthScreen,heightScreen;
	private ProfilePlot profPlot;
	private int w,h;
	private ImagePlus imp;
	
	public ImagePanelIRMp(Image img) {
		
		if (img == null) {
			URL imgURL = getClass().getResource("/WhiteScreenp.jpg");
			img=new ImageIcon(imgURL).getImage();
		}
		this.img=img;
	}
	
	public ImagePanelIRMp(String cheminImg, int w,int h,int n,String type) {
				
		this.h=h;
		this.w=w;
		Float f= new Float(n);
		int mult=0;
		int intValue= f.intValue();
		FileInfo fi = new FileInfo();
		
		if (type.contains("_16BIT_SGN_INT")) {
			fi.fileType= FileInfo.GRAY16_SIGNED;
			mult=2;
		}
		else 
				if (type.contains("_32BIT_SGN_INT")) {
							fi.fileType= FileInfo.GRAY32_INT;
							mult=4;}
					else {fi.fileType= FileInfo.GRAY8;mult=1;
						}
			
		  fi.fileName = cheminImg;
		  fi.width = w;
		  fi.height = h;
		  fi.offset = w*h*intValue*mult;
		  fi.nImages = 1;
		  fi.intelByteOrder = true;
		  FileOpener fo = new FileOpener(fi);
		  imp = fo.open(false);
		  //profPlot.createWindow();
		  if (imp!=null) {
			  img= imp.getImage();
		  }
					
	}

	public void paintComponent (Graphics g) {
		g.drawImage(img, 0, 0, 200,200,null);
		widthScreen=1280;
		heightScreen=1024;
		if (h!=1)
		g.drawImage(img, 0, 0, widthScreen/6,heightScreen/5,null);
		else {
			imp.setRoi(0, 0, w, 1);
			profPlot = new ProfilePlot(imp);
			g.drawImage(profPlot.getPlot().getImagePlus().getImage(),0, 0, widthScreen/6,heightScreen/5,null);
		}
	}
}
