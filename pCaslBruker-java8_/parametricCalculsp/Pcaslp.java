package parametricCalculsp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import brukerParavisionp.OpenBrukerp;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import mriManagerp.GetStackTracep;
import mriManagerp.ListSequencep;
import mriManagerp.PcaslBruker_;

public class Pcaslp extends AbstractAction implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PcaslBruker_ wind;
	private double[] x,y;
	private double[] xoff,yoff;
	private ImagePlus imp;
	private String format;
	private ImageCanvas canvas;
	private ImageStack stack;
	private PlotWindow pwin; 
	private Plot plotCurve;
	private RoiManager Nroi;
	private Dimension dimWinPlot;
	private Color[] colors ={Color.BLUE,Color.RED,Color.ORANGE,Color.MAGENTA,Color.CYAN,Color.GREEN,Color.GRAY,Color.PINK,Color.YELLOW,Color.DARK_GRAY};

	public Pcaslp(PcaslBruker_ wind,String command, String format) {
		super(command);
		this.wind=wind;
		this.format=format;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		IJ.log("PCASL : ");
		IJ.log(" 	- make one or more ROI (rectangle, elliptical, polygone or point) "+"\n"
				+ " - add in Roi Manager, check the boxes 'Show All' and 'Labels'"+"\n"
				+ " - reselect the image window and click on space bar");

		TreePath seqS = wind.getListSeq().getSelectionPaths()[0];
		String seqSel;

		if (seqS.getPathCount()==2) 
			 seqSel = seqS.getPathComponent(1).toString();
		else seqSel = seqS.getPathComponent(2).toString()+" - "+seqS.getPathComponent(1).toString();

		if (format.contentEquals("Bruker")) {

		String tor = wind.getTextPath2dseq().getText();
		tor = tor.substring(tor.indexOf("\n")+1, tor.length());

		try {
			imp = new OpenBrukerp(wind,tor.toString(),"Open by sample",false,ListSequencep.hm.get(seqSel),seqSel).getimgplus();
			imp.show();
			imp = new ImagePlus();
			imp=IJ.getImage();

		} catch (Exception e1) {
			new GetStackTracep(e1);
			PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
		} 
		}

		ImageWindow win = imp.getWindow();
		win.addWindowListener(win);
		canvas = win.getCanvas();
//		canvas.addMouseListener(this);
//		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);

		//frames = imp.getNFrames(); 	//number of frames
		stack = imp.getStack();

		try {
			x=getTime(wind.getTabParam1().getValueAt(22, 1).toString());
		}
		catch (Exception e1) {
			JOptionPane.showMessageDialog(wind, "error echo time");
		}
		y = new double[x.length];
		yoff = new double[x.length-1];
		xoff = new double[x.length-1];

		plotCurve = new Plot("Plot", "Phase", "Relative perfusion");
		pwin=plotCurve.show();
		pwin.getCanvas().addKeyListener(this);

		Nroi = new RoiManager();
//		for (Window str:WindowManager.getAllNonImageWindows())
//		System.out.println(str);

	}

	private void doplotCurve() {

		double y1,y2,ymax = 0,xmax=0,ymin=0,xmin=0;
		String space="";

		IJ.log("\n********************************************");

		plotCurve = new Plot("Plot", "Phase", "Relative perfusion");

//		for (Double str:pplot.getPlot().getLimits())
//			System.out.println(str);
				
		try {
			dimWinPlot = WindowManager.getWindow("Plot").getBounds().getSize();
//			pplot = (PlotWindow) WindowManager.getWindow("Plot");
//			pplot.getPlot();
//			System.out.println(Plot.X_TICKS);
		}
		catch (Exception e) {
			pwin=null;
			
		}
				
		for (int s=0;s<Nroi.getCount();s++) {
		
		for (int i=1;i<=x.length;i++) {
			if (i>1) {
			int indexLabel = 1,indexControl=1;
			indexLabel = imp.getStackIndex(1, i, 1);
			indexControl = imp.getStackIndex(1, i, 2);
			ImageProcessor ipLabel = stack.getProcessor(indexLabel);
			ImageProcessor ipControl = stack.getProcessor(indexControl);
			y1= getYMeanRoi(ipLabel, Nroi.getRoi(s));
			y2= getYMeanRoi(ipControl, Nroi.getRoi(s));
			
			y[i-1]=100*(y2-y1)/y2;
			if (i==2) {
				ymax=y[1];
				ymin=y[1];
			}
			if (y[i-1]>ymax) {ymax=y[i-1];xmax=x[i-1];};
			if (y[i-1]<ymin) {ymin=y[i-1];xmin=x[i-1];};
			}
		}
		
		for (int a=0;a<xoff.length;a++) {
			xoff[a]=x[a+1];
			yoff[a]=y[a+1];
		}
		
		
		plotCurve.setColor(colors[s]);
		plotCurve.setLineWidth(1);
		plotCurve.addPoints(xoff, yoff,Plot.LINE);
		plotCurve.addPoints(xoff, yoff,Plot.X);
		plotCurve.addLabel(0, 0, space+"Plot"+(s+1));
		space=space+"            ";
		
		
		IJ.log("\nPlot"+(s+1)+" : ");
		IJ.log("Signal max ="+ymax+"\n"+"Signal min ="+ymin);
		IJ.log("Phase of max ="+xmax+"\n"+"Phase of min ="+xmin);
		
		}				
		
		if (pwin==null) {
			pwin=plotCurve.show();
		}
		else {
			pwin.drawPlot(plotCurve);
			pwin.setSize(dimWinPlot);
//			pwin.getPlot().setLimits(lim[0], lim[1], lim[2], lim[3]);
		}

		}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode()==KeyEvent.VK_SPACE) doplotCurve();
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private double[] getTime(String time) {
		String[] items = time.split(" ");
		
		double[] resul = new double[items.length];
		
		for (int i = 0; i < items.length; i++) {
		    try {
		        resul[i] = Double.parseDouble(items[i]);
		    } catch (NumberFormatException nfe) {};
		}
		return resul;
	}
	
	public float getYMeanRoi(ImageProcessor ips,Roi roiOfImage) {
		
		float yMean = 0;
		
		//if (roiOfImage!=null && !roiOfImage.isArea()) roiOfImage = null;
		ImageProcessor mask = roiOfImage!=null?roiOfImage.getMask():null;
		Rectangle r = roiOfImage!=null?roiOfImage.getBounds():new Rectangle(0,0,ips.getWidth(),ips.getHeight());
		int count = 0;
		try {
			for (int i=0;i<r.height;i++) 
				for (int j=0;j<r.width;j++) {
					if (mask==null||mask.getPixel(j,i)!=0) {
					count++;
					yMean += ips.getPixelValue(j+r.x, i+r.y);
					}
				}
		}
		catch (Exception e) {
		}
		return yMean/count;
	}
}
