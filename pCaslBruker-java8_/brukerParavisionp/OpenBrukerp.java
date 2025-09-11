package brukerParavisionp;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import ij.*;
import ij.gui.Plot;
import ij.gui.StackWindow;
import ij.io.FileInfo;
import ij.io.FileOpener;
import ij.measure.Calibration;
import ij.plugin.HyperStackConverter;
import ij.process.ImageProcessor;
import mriManagerp.GetStackTracep;
import mriManagerp.PcaslBruker_;

public class OpenBrukerp extends Thread{
	
	private String chemin2dseq;
	private String cheminVisupars;
	private PcaslBruker_ wind;
	private String sampleOrscale;
	private String sliceorient;
	private String seqSel;
	private ImagePlus imgplus;
	private int nImage,nSlices = 0,nFrames = 0;
	//private String order="default";
	private String order="xyczt";
	public FileInfo finfo = new FileInfo();
	private int i,j;
	String[] lp = null;
	int dimension;
	double	resolX,resolY,resolZ;
	int precision=4;
	private String[] listParam;
	private String separator = File.separator;
	private String[][] listStackParam = new String[4][2];
	private String[][] listElements = new String[2][2];
	private String ch,chb,ch1,ch2;
	private boolean all;
	private Plot profPlot;
			
	public OpenBrukerp (PcaslBruker_ wind,String chemin2dseq,String sampleOrscale,boolean all,String[] listParam,String seqSel) throws NumberFormatException, IOException {
						
		this.seqSel=seqSel;
		this.chemin2dseq=chemin2dseq;
		this.listParam=listParam;
		this.wind=wind;
		this.sampleOrscale=sampleOrscale;
		this.all=all;
		chemin2dseq= chemin2dseq.substring(0,chemin2dseq.lastIndexOf(separator))+separator;
		cheminVisupars = chemin2dseq+"visu_pars";
		finfo.fileFormat = FileInfo.RAW;
		
		if (!all) run();

	}
	
	public void run() {
	
		//for (String str:listParam) System.out.println(str);
		dimension 		= 	Integer.parseInt(listParam[12]);
		
		if (dimension>1) {
		boolean stack = false;
		String title;
		String datType	=	listParam[1];  
		sliceorient 	= 	listParam[11];
		int width 		= 	Integer.parseInt(listParam[2]);
		int height 		= 	Integer.parseInt(listParam[3]);
		int depth		= 	Integer.parseInt(listParam[4]);
		String ByteOrder= 	listParam[5];
		int slice 	= 		Integer.parseInt(listParam[6]);
		int repet	= 1;
		try { 
			repet = Integer.parseInt(listParam[7]);
		}
		catch (Exception e) {
			new GetStackTracep(e);
    		PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		};
		resolX = Float.parseFloat(listParam[8]);
		resolY = Float.parseFloat(listParam[9]);
		resolZ = Float.parseFloat(listParam[10]);
		int		nOrientation = Integer.parseInt(listParam[13]);
		
		
		String nomData,nomStudy;
		int selectRow = wind.getTabData().getSelectedRow();
		
		nomData = (String) wind.getTabData().getValueAt(selectRow, 1);
		nomStudy = (String) wind.getTabData().getValueAt(selectRow, 3);

		title=nomStudy+" - "+nomData+" - "+seqSel;
		
		if (dimension==2) 
			nImage = depth;
		else nImage = depth * repet;
		
		try {
			listStackParam = new StackOrderImagep(cheminVisupars).listStack();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("**********************************");
//		System.out.println("VisuCoreFrameCount ="+listStackParam[0][1]);
//		System.out.println("VisuFGOrderDescDim ="+listStackParam[1][1]);
//		System.out.println("VisuFGOrderDesc ="+listStackParam[2][1]);
//		System.out.println("VisuGroupDepVals ="+listStackParam[3][1]);
		
		//----------- create image--------------------------------------------------------
		finfo.fileFormat=FileInfo.RAW;
		finfo.fileName = chemin2dseq;
		if (datType.contains("_16BIT_SGN_INT")) finfo.fileType = FileInfo.GRAY16_SIGNED;
		else 
			if (datType.contains("_32BIT_SGN_INT")) finfo.fileType = FileInfo.GRAY32_INT;
			else finfo.fileType = FileInfo.GRAY8;
		finfo.offset = 0;
		finfo.gapBetweenImages = 0;
		if (ByteOrder.contentEquals("littleEndian")) finfo.intelByteOrder = true;
		else finfo.intelByteOrder = false;
		finfo.unit="mm";
		finfo.valueUnit="mm";
		finfo.nImages=nImage;
		finfo.width=width;
		finfo.height=height;
		finfo.pixelWidth=resolX;
		finfo.pixelHeight=resolY;
		finfo.pixelDepth=resolZ;
		finfo.frameInterval=1;
					
		FileOpener fo = new FileOpener(finfo);
		
		imgplus = fo.open(all);
		imgplus.setTitle("2dseq");
		imgplus=convertToGray32(imgplus);
		
		//----------- Normalization image--------------------------------------------------------
		NormalizationImageIRMp scal = new NormalizationImageIRMp(chemin2dseq);
		
		try {
			if (scal.factorNormalize().length!=1 && scal.offsetNormalize().length!=1) {
				for (int i=0;i<imgplus.getStackSize();i++) {
					imgplus.setSlice(i+1);
					imgplus.getProcessor().multiply(scal.factorNormalize()[i]);
					imgplus.getProcessor().add(scal.offsetNormalize()[i]);
				}
			}
		 
		
		if (scal.factorNormalize().length!=1 && scal.offsetNormalize().length==1) {
			
			for (int i=0;i<imgplus.getStackSize();i++) {
				imgplus.setSlice(i+1);
				if (scal.offsetNormalize()[0]!=0) imgplus.getProcessor().add(scal.offsetNormalize()[0]);
				imgplus.getProcessor().multiply(scal.factorNormalize()[i]);
			}
		}
		
		if (scal.factorNormalize().length==1 && scal.offsetNormalize().length!=1) {
			
			for (int i=0;i<imgplus.getStackSize();i++) {
				imgplus.setSlice(i+1);
				if (scal.factorNormalize()[0]!=1) imgplus.getProcessor().multiply(scal.factorNormalize()[0]);
				imgplus.getProcessor().add(scal.offsetNormalize()[i]);
			}
		}
		
		if (scal.factorNormalize().length==1 && scal.offsetNormalize().length==1) {
		
			for (int i=0;i<imgplus.getStackSize();i++) {
				imgplus.setSlice(i+1);
				if (scal.factorNormalize()[0]!=1) imgplus.getProcessor().multiply(scal.factorNormalize()[0]);
				if (scal.offsetNormalize()[0]!=0) imgplus.getProcessor().add(scal.offsetNormalize()[0]);
			}
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//----------- arrange image--------------------------------------------------------
		nSlices=slice;
		nFrames=1;
	 
		
		try {
			listElements=new ListElementFramep(cheminVisupars).listElement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			System.out.println("VisuCoreDataUnits = "+listElements[0][1]);
//			System.out.println("VisuFGElemComment = "+listElements[1][1]);
				
	
		if (dimension ==2) {
			if (Integer.parseInt(listStackParam[1][1])==2){
				stack=true;
				ch = listStackParam[2][1];
				
				if (ch.contains("FG_SLICE")) {
						if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_SLICE")) {
						nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						//order="default";
						order="xyczt";
					}
					else nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
				ch=ch.substring(ch.lastIndexOf("("));
					if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_SLICE")) {
						nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						order="xyctz";	
					}
					else nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
				}
				
				else if (ch.contains("FG_ECHO")) {
					if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_ECHO")) {
						nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						//order="default";
						order="xyczt";
					}
					else nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
					ch=ch.substring(ch.lastIndexOf("("));
					if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_ECHO")) {
						nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						order="xyctz";	
					}
					else nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
					}
				
				else if (ch.contains("FG_ISA")) {
					if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_ISA")) {
						nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						order="xyctz";
					}
					else nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						ch=ch.substring(ch.lastIndexOf("("));
					if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_ISA")) {
						nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						//order="default";
						order="xyczt";
					}
					else nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
					}
				
				else if (ch.contains("FG_CYCLE")) {
					if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_CYCLE")) {
						nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						order="xyczt";
					}
					else nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
					ch=ch.substring(ch.lastIndexOf("("));
					if (ch.substring(ch.indexOf("<")+1,ch.indexOf(">")).contains("FG_CYCLE")) {
						nSlices=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
						order="xyctz";
					}
					else nFrames=Integer.parseInt(ch.substring(ch.indexOf("(")+1,ch.indexOf(",")));
					
				}
			}
			else {
				nSlices=Integer.parseInt(listStackParam[0][1]);
				nFrames=1;
			}
			ch = listStackParam[2][1];
			chb = ch;
		}
		ch1=listElements[0][1];
		ch2=listElements[1][1];
		/*System.out.println("ch = "+ch);
		System.out.println("ch1 = "+ch1);
		System.out.println("ch2 = "+ch2);*/
		
		
		if (ch!=null && ch1!=null && ch2!=null && all) {
			if (ch.contains("FG_ISA")) {
			ch=ch.substring(ch.indexOf(">")+1);
			ch=ch.substring(ch.indexOf("<")+1, ch.indexOf(">"));
			IJ.log(title+"\n");
			IJ.log(ch+" :\n");
							
				for (int i=0;i<Integer.parseInt(chb.substring(chb.indexOf("(")+1,chb.indexOf(",")));i++) {
				IJ.log("t:"+(i+1)+"/"+chb.substring(chb.indexOf("(")+1,chb.indexOf(","))+" - "+ch2.substring(ch2.indexOf("<")+1, ch2.indexOf(">"))+" "+"["+ch1.substring(ch1.indexOf("<")+1, ch1.indexOf(">"))+"] \n");
				ch1=ch1.substring(ch1.indexOf(">")+1);
				ch2=ch2.substring(ch2.indexOf(">")+1);
				}
				IJ.log("\n");
			}
		}
		
		if (dimension==3) {	
				nSlices=depth;
				nFrames=repet;
				//order="default";
				order="xyczt";
				if (nFrames!=1) stack=true;
		}			
		
		//new HyperStackConverter();
		imgplus = HyperStackConverter.toHyperStack(imgplus, 1, nSlices, nFrames, order, "grayscale");
		imgplus.resetDisplayRange();
		
		
		
		if (stack && all) {
				EventQueue.invokeLater(new Runnable() {
				public void run() {
					new StackWindow(imgplus);				
				}
			});
		}	
		
		
//		if (stack && !all) {
//			ImageStack ims = new ImageStack(width, height, imgplus.getNSlices());
//			ims=imgplus.getImageStack();	
//			imgplus.getImage();
//		}
		
		if (nOrientation==1 && all) {
		listPosition();
		//System.out.println("nSlices = "+nSlices+" : nFrames = "+nFrames);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
						for (j=1;j<nFrames+1;j++)
							for (i=1;i<nSlices+1;i++) {
							IJ.setSlice(i+(j-1)*nSlices);
							if (lp.length!=1)
								IJ.run("Set Label...","label=Pos:"+Float.valueOf(lp[i-1])+" mm");
							else IJ.run("Set Label...","label=Pos:"+Float.valueOf(lp[0])+" mm");
							}
						}
		});
		}
					
		if (sampleOrscale.contentEquals("Open by scale X-Y")) {
			float scale = (float)(resolX/resolY);
			if (scale!=1){
			if (scale>1) 
				IJ.run("Size...", "width="+(int) (scale*width)+" height="+height+" depth="+nSlices+" interpolation=None");
			else
				IJ.run("Size...", "width="+width+" height="+(int) (scale*height)+" depth="+nSlices+" interpolation=None");
			}
		}
		
		if (sampleOrscale.contentEquals("Open by scale Y-X")) {
			float scale = (float)(resolX/resolY);
			if (scale!=1){
			if (scale>1) 
				IJ.run("Size...", "width="+width+" height="+(int) (scale*height)+" depth="+nSlices+" interpolation=None");
			else
				IJ.run("Size...", "width="+(int) (scale*width)+" height="+height+" depth="+nSlices+" interpolation=None");
			}
		}
		
		if (all) {
		IJ.getImage().setTitle(title);
		IJ.run("Brightness/Contrast...");
		IJ.resetMinAndMax();
		}
	}
	
	if (dimension ==1) {
			
		FileInfo fi = new FileInfo();
		double 	bdw = Double.parseDouble(listParam[14]);
		int width 		= 	Integer.parseInt(listParam[2]);
		
		double resol = bdw / (double) width;
		String datType = listParam[1]; 
		
			if (datType.contains("_16BIT_SGN_INT")) {
				fi.fileType= FileInfo.GRAY16_SIGNED;
				
			}
			else 
					if (datType.contains("_32BIT_SGN_INT")) {
								fi.fileType= FileInfo.GRAY32_INT;
								}
						else {fi.fileType= FileInfo.GRAY8;
							}
				
			  fi.fileName	= chemin2dseq;
			  fi.width		= width;
			  fi.height		= 1;
			  fi.offset 	= 0;
			  fi.nImages 	= 1;
			  fi.intelByteOrder = true;
			  fi.unit		="Hz";
			  fi.valueUnit	="Hz";
			  fi.pixelWidth	= resol ;
			 	  
			  FileOpener fo = new FileOpener(fi);
			  ImagePlus imp = fo.open(false);
			  imp.setRoi(0, 0, width, 1);
			  
			  double X0[] = new double[width] ;
			  for (int i=0;i<width;i++) {
				  X0[i]=-(bdw/2)+resol*i;
			  }
			  
			  profPlot = new Plot(seqSel, "Frequency (Hz)", "Amplitude");
			  profPlot.setSize(600, 300);
			  profPlot.setColor(Color.blue);
			  profPlot.addPoints(X0, imp.getProcessor().getLine(0, 0, (double) width, 0), Plot.LINE);
			  profPlot.show();
		}
	}
	
	private ImagePlus convertToGray32(ImagePlus imgtmp) {
		
		int width =  imgtmp.getWidth();
	    int height = imgtmp.getHeight();
	    int nSlices = imgtmp.getStackSize();
		
		if (imgtmp.getType()!=ImagePlus.GRAY32) {
		ImageStack stack1 = imgtmp.getStack();
	    ImageStack stack2 = new ImageStack(width, height);
	    String label;
	      int inc = nSlices/20;
	      if (inc<1) inc = 1;
	      ImageProcessor ip1, ip2;
	      Calibration cal = imgtmp.getCalibration();
	      for(int i=1; i<=nSlices; i++) {
	          label = stack1.getSliceLabel(1);
	          ip1 = stack1.getProcessor(1);
	          ip1.setCalibrationTable(cal.getCTable());
	          ip2 = ip1.convertToFloat();
	          stack1.deleteSlice(1);
	          stack2.addSlice(label, ip2);
	          if ((i%inc)==0) {
	            IJ.showProgress((double)i/nSlices);
	            IJ.showStatus("Converting to 32-bits: "+i+separator+nSlices);
	          }
	        }
	        IJ.showProgress(1.0);
	        imgtmp.setStack(null, stack2);
	        imgtmp.setCalibration(imgtmp.getCalibration()); //update calibration
		}
	        return imgtmp;
	}
	
	private void listPosition () {
				
		if (sliceorient.equalsIgnoreCase("axial"))
		try {
			lp= new ListPositionFramep(cheminVisupars,1).listofPositionFrame()[2];
			if (dimension==3) {
				BigDecimal resol = new BigDecimal(String.valueOf(resolZ)) ;
				BigDecimal pos = new BigDecimal(lp[0]);
				lp = new String[nSlices];
				for (int i=0;i<nSlices;i++) {
					lp[i]=String.valueOf((resol.multiply(new BigDecimal(String.valueOf(i)))).add(pos));
					lp[i]=lp[i].substring(0,lp[i].indexOf(".")+3);
					if (lp[i].contains("E")) lp[i]="0";
				}
			}
			
		} catch (IOException e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
		else if (sliceorient.equals("coronal"))
		try {
			lp= new ListPositionFramep(cheminVisupars,-1).listofPositionFrame()[2];
			if (dimension==3) {
				BigDecimal resol = new BigDecimal(String.valueOf(resolZ)) ;
				resol=resol.multiply(new BigDecimal(String.valueOf("-1")));
				BigDecimal pos = new BigDecimal(lp[0]);
				lp = new String[nSlices];
				for (int i=0;i<nSlices;i++) {
					lp[nSlices-i-1]=String.valueOf((resol.multiply(new BigDecimal(String.valueOf(i)))).add(pos));
					lp[nSlices-i-1]=lp[nSlices-i-1].substring(0,lp[nSlices-i-1].indexOf(".")+3);
					if (lp[nSlices-i-1].contains("E")) lp[nSlices-i-1]="0";
				}
			}
		} catch (IOException e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
		else if (sliceorient.equals("sagittal"))
		try {
			lp= new ListPositionFramep(cheminVisupars,1).listofPositionFrame()[2];
			if (dimension==3) {
				 
				BigDecimal resol = new BigDecimal(String.valueOf(resolZ)) ;
				BigDecimal pos = new BigDecimal(lp[0]);
				lp = new String[nSlices];
				for (int i=0;i<nSlices;i++) {
					lp[i]=String.valueOf((resol.multiply(new BigDecimal(String.valueOf(i)))).add(pos));
					lp[i]=lp[i].substring(0,lp[i].indexOf(".")+3);
					if (lp[i].contains("E")) lp[i]="0";
				}
			}
		} catch (IOException e) {
			new GetStackTracep(e);
			PcaslBruker_.getBugText().setText(GetStackTracep.getMessage());
		}
	}

	public ImagePlus getimgplus () {
		return imgplus;
	}
	
	public int[] getSliceFrame() {
		int[] lst = {nSlices,nFrames};
		return lst;
	}
}


