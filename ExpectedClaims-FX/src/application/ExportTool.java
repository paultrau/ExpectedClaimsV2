package application;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//Policy Date	Agent 1 ID	Agent 1 Share	Agent 1 Amt	Agent 2 ID	Agent 2 ID	Agent 2 Amt	Agent 3 ID	Agent 3 ID	Agent 3 Amt	Counted ID


public class ExportTool {
	static List<String> titles = Arrays.asList("Policy No.","Plan Code",
			"Policy Date","Premium Amount",  "Surivorship Load", "Adjusted Premium", "Validation",
			"Agent1 ID","Agent1 Share","Agent1 Amt", "Agent 1 Eligable?",
			"Agent2 ID","Agent2 Share","Agent2 Amt", "Agent 2 Eligable?",
			"Agent3 ID","Agent3 Share","Agent3 Amt", "Agent 3 Eligable?", "Counted ID");

	static List<String> titles2 = Arrays.asList("Agent ID", "Agent Count","Final Cumulative Amount");
	
	
	public static void writeFile(ArrayList<RowPack> data,Parser theParser) {
		ArrayList<Integer> ids = theParser.agentIDs;
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		ArrayList<Integer> agentIDs = new ArrayList<Integer>();
		XSSFSheet sheet1 = workbook.createSheet("Expected Claims Report");
		XSSFSheet sheet2 = workbook.createSheet("Agent Summary");
		int rowCount = 0;
		
		double total=0;
		
		for (RowPack pack : data) {
			
			int columnCount = 0;
			if (rowCount == 0) {
				//title row
				Row row = sheet1.createRow(rowCount++);
				for (String word: titles) {
					Cell cell = row.createCell(columnCount++);
					cell.setCellValue(word);
				}
			}
			else {
				Row row = sheet1.createRow(rowCount++);
				
				System.out.println("["+pack.policyNum+"] Policy Number Processing...");
				
				cellStringFactory(columnCount++,pack.policyNum,row);
				cellStringFactory(columnCount++,pack.plancode,row);
				cellStringFactory(columnCount++,pack.premiumDate.toString(),row);
				cellDoubleFactory(columnCount++,pack.premiumAmt,row);
				cellDoubleFactory(columnCount++,pack.load,row);
				cellDoubleFactory(columnCount++,pack.adjPremuim,row);
				if (pack.sign.equals("+")) {
					cellDoubleFactory(columnCount++,pack.premiumAmt-(100*(pack.adjPremuim+pack.load)),row);
				}
				if (pack.sign.equals("-")) {
					cellDoubleFactory(columnCount++,pack.premiumAmt+(100*(pack.adjPremuim+pack.load)),row);
				}	
				cellIntFactory(columnCount++,pack.agent1.getID(),row);
				cellDoubleFactory(columnCount++,pack.agent1.getShare()*100,row);
				cellDoubleFactory(columnCount++,pack.newPremium1,row);
				cellStringFactory(columnCount++,Boolean.toString(pack.a1),row);
				theParser.addToAgent(pack.agent1, pack.newPremium1);
				total = total + pack.newPremium1;
				//agentIDs.add(pack.agent1.getID());
				
				if (pack.agent2!=null) {
				cellIntFactory(columnCount++,pack.agent2.getID(),row);
				cellDoubleFactory(columnCount++,pack.agent2.getShare()*100,row);	
				cellDoubleFactory(columnCount++,pack.newPremium2,row);
				cellStringFactory(columnCount++,Boolean.toString(pack.a2),row);
				theParser.addToAgent(pack.agent2, pack.newPremium2);
				total = total + pack.newPremium2;
				//agentIDs.add(pack.agent2.getID());
				}
				else
					columnCount = columnCount + 4;
				if (pack.agent3!=null) {

				cellIntFactory(columnCount++,pack.agent3.getID(),row);
				cellDoubleFactory(columnCount++,pack.agent3.getShare()*100,row);
				cellDoubleFactory(columnCount++,pack.newPremium3,row);
				cellStringFactory(columnCount++,Boolean.toString(pack.a3),row);
				theParser.addToAgent(pack.agent3, pack.newPremium3);
				total = total + pack.newPremium3;
				//agentIDs.add(pack.agent3.getID());
				}
				else
					columnCount = columnCount + 4;
				
				cellIntFactory(columnCount++,pack.countedID,row);
				agentIDs.add(pack.countedID);
				System.out.println("[+] Row Processed!");
			}
		}
		Row row2 = sheet1.createRow(++rowCount);
		int columnCount2 = 0;
		cellStringFactory(columnCount2++,"Cumlative Total =>",row2);
		cellDoubleFactory(columnCount2++,total,row2);
		//end sheet 1
		
		
		
		rowCount = 0;
		
		ArrayList<FinalPack> finalPacks = writeFrequencies(agentIDs);
		if (rowCount == 0) {
			int columnCount = 0;
			//title row
			Row row = sheet2.createRow(rowCount++);
			for (String word: titles2) {
				Cell cell = row.createCell(columnCount++);
				cell.setCellValue(word);
			}
		}
		total=0;
		double total2 =0 ;
		for (FinalPack pack: finalPacks) {
				int columnCount = 0;
				Row row = sheet2.createRow(rowCount++);
				cellIntFactory(columnCount++,pack.id,row);
				cellIntFactory(columnCount++,pack.count,row);
				cellDoubleFactory(columnCount++,theParser.getTotal(pack.id),row);
				total = total + pack.count;
				total2 = total2 + theParser.getTotal(pack.id);
			}
		
		row2 = sheet2.createRow(++rowCount);
		columnCount2 = 0;
		cellStringFactory(columnCount2++,"Total Count =>",row2);
		cellDoubleFactory(columnCount2++,total,row2);
		
		cellStringFactory(columnCount2++,"Cumlative Total =>",row2);
		cellDoubleFactory(columnCount2++,total2,row2);
		
		try (FileOutputStream outputStream = new FileOutputStream("Expected_Claims_Amount_Output.xlsx"))
		{
			workbook.write(outputStream);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("[-] Error writing file");
		}
		
		
	}
	
	public static ArrayList<FinalPack> writeFrequencies(ArrayList<Integer> list)
    {
		ArrayList<FinalPack> output = new ArrayList<FinalPack>();
        // hashmap to store the frequency of element
        Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
  
        for (Integer i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }
  
        // displaying the occurrence of elements in the arraylist
        for (Map.Entry<Integer, Integer> val : hm.entrySet()) {
        	System.out.println("[+] ID: " +val.getKey() + ", occured "+val.getValue()+" times.");
        	output.add(new FinalPack(val.getKey(),val.getValue()));
        }
        return output;
    }
	
	public static void cellStringFactory(int col,String value,Row row) {
		Cell cell = row.createCell(col);
		cell.setCellValue(value);
	}
	public static void cellDoubleFactory(int col,Double value,Row row) {
		Cell cell = row.createCell(col);
		cell.setCellValue(value);
	}
	public static void cellIntFactory(int col,int value,Row row) {
		Cell cell = row.createCell(col);
		cell.setCellValue(value);
	}
	

	
}
