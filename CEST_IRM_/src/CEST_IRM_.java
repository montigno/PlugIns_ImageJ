import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;

import javax.swing.JOptionPane;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.gui.Roi;
import ij.plugin.PlugIn;

public class CEST_IRM_ implements PlugIn {

	private JSONParser parser = new JSONParser();
	private Object obj;
	Plot p, update;
	static PlotWindow pw;
	ImagePlus imp;
	float range[] = new float[3];

	@Override
	public void run(String arg) {
		if (WindowManager.getImageTitles().length == 0) {
			IJ.showMessage("No images");
			return;
		}
		new CEST_IRM_().main();
	}

	public void main() {

		imp = WindowManager.getCurrentImage();
		
		String current_json = IJ.getDirectory("image") + imp.getTitle();
		current_json = current_json.replace(".nii", ".json");
		String tmp = "-10.0 10.0 0.1";
		
		if (new File(current_json).exists()) {
			try {
				obj = parser.parse(new FileReader(current_json));
				JSONObject object = (JSONObject) obj;
				JSONObject under_object = (JSONObject) object.get("SatTransFreqRanges");
				tmp = under_object.get("value").toString();
				tmp = tmp.replace("[", "");
				tmp = tmp.replace("]", "");
				tmp = tmp.replace(",", " ");
				
			} catch (Exception e) {
				tmp = getFreqRange();
			}
//			IJ.log(current_json);
			
		}
		else
			tmp = getFreqRange();
		
		for (int i=0; i < 3; i++)
			range[i] = Float.parseFloat(tmp.split(" +")[i]);
		
		p = new Plot("CEST-IRM", "Chemical Shift (ppm)", "");
//		p.setLimits(-5, 5, 0, 110);
		pw = p.show();

		imp.getCanvas().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				doAsymetry(imp, imp.getRoi());
			}
		});
	}

	public void doAsymetry(ImagePlus imp, Roi roi_user) {
		int size_z = imp.getNFrames();
		double[] data_x = new double[size_z - 1];
		double[] signal_z = new double[size_z - 1];
		double[] asym = new double[size_z / 2];

		imp.setRoi(roi_user);
		imp.setSlice(1);
//		double S0 = imp.getRoi().getStatistics().mean;
		for (int i = 0; i < size_z - 1; i++) {
			imp.setSlice(i + 2);
			data_x[i] = (float) range[0] + i * range[2];
			signal_z[i] = imp.getRoi().getStatistics().mean;
		}

		for (int i = 0; i < asym.length; i++) {
//			IJ.log(String.valueOf(i));
			asym[i] = 100 * (signal_z[i] - signal_z[size_z - 2 - i]) / signal_z[i];
//			asym[i] = 100 * (signal_z[i] - signal_z[size_z - 2 - i]) / S0;
		}

		update = new Plot("CEST-IRM", "Chemical Shift (ppm)", "");
		update.setColor(Color.blue);
		update.addLabel(0.01, 0.20, "Z-Spectrum(100*M/Mo)");
		update.addPoints(data_x, signal_z, Plot.LINE);
		update.setColor(Color.red);
		update.addLabel(0.01, 0.28, "CESTasym(%)");
		update.addPoints(data_x, asym, Plot.LINE);
		update.setLimitsToFit(true);

		pw.drawPlot(update);

	}
	
	public String getFreqRange() {
		return JOptionPane.showInputDialog(null, "Enter the frequency Range (min max step)", "-10.0 10.0 0.1");
	}
}