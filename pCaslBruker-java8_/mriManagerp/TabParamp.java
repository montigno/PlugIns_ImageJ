package mriManagerp;

import javax.swing.table.AbstractTableModel;

public class TabParamp extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Name parameter","Value parameter"};
	private Object[][] data;

	public TabParamp (Object [][] data) {
		
		this.data = data;
	}
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	
	public String getColumnName(int col) {
	        return columnNames[col];
	    }
	@Override
	public Object getValueAt(int row, int col) {
		 return data[row][col];
	}
	
    public Class<? extends Object> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

}
