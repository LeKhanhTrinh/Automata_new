package Processor;


import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelProcessor {

	/**
	 * @param args
	 */
	File file;
	WritableWorkbook workbook;
	WritableSheet sheet;

	public ExcelProcessor(String namefile) {
		// TODO Auto-generated constructor stub
		try {
			file = new File(namefile + ".xls");
			workbook = Workbook.createWorkbook(file);
			sheet = workbook.createSheet("Transition", 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean addCellInString(int row, int column, String value) {
		try {
			Label label = new Label(row, column, value);
			sheet.addCell(label);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addCellInNumber(int row, int column, double value) {
		try {
			Number number = new Number(row, column, value);
			sheet.addCell(number);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean close() {
		try {
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}
