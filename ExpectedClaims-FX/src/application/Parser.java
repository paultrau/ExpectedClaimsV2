package application;

import org.apache.commons.collections4.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.examples.xssf.eventusermodel.XLSX2CSV;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

public class Parser {
	
	private File[] fileList;
	private ArrayList<XSSFWorkbook> workbookList = new ArrayList<XSSFWorkbook>();
	private XSSFSheet dateConversion;
	private XSSFSheet plancodeSheet;
	private XSSFSheet dataSheet;
	public ArrayList<Integer> agentIDs = new ArrayList<Integer>();
	public ArrayList<Agent> agents = new ArrayList<Agent>();
	
	//begin new reading stuff ************
	/*
	public void processOneSheet(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        // To look up the Sheet Name / Sheet Order / rID,
        //  you need to process the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId2");
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }
    
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        XMLReader parser = XMLHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }
    
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                System.out.print(attributes.getValue("r") + " - ");
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = sst.getItemAt(idx).getString();
                nextIsString = false;
            }
            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v")) {
                System.out.println(lastContents);
            }
        }
        public void characters(char[] ch, int start, int length) {
            lastContents += new String(ch, start, length);
        }
    }
	
	// end new reading stuff ****************
	*/
	
	public Parser(File[] theList) {
		this.fileList = theList;
		System.out.println("[...] Loading files as XLSX...");
		//load as workable XLSXs
		try {
				System.out.println("[...] "+ fileList[0].getName()+" loading...");
				OPCPackage fis = OPCPackage.open(fileList[0].getAbsolutePath()); 
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				workbookList.add(wb);
				System.out.println("[+] "+ fileList[0].getName()+" loaded successfully.");
				
				System.out.println("[...] "+ fileList[1].getName()+" loading...");
				fis = OPCPackage.open(fileList[1].getAbsolutePath()); 
				wb = new XSSFWorkbook(fis);
				workbookList.add(wb);
				System.out.println("[+] "+ fileList[1].getName()+" loaded successfully.");
				
				System.out.println("[...] "+ fileList[2].getName()+" loading...");
				fis = OPCPackage.open(fileList[1].getAbsolutePath()); 
				wb = new XSSFWorkbook(fis);
				workbookList.add(wb);
				System.out.println("[+] "+ fileList[2].getName()+" loaded successfully.");
		}
		catch(Exception e) {
			System.out.println("[-] XLSX Initialization Unsuccessfully.");
			e.printStackTrace();
			System.exit(0);
		}
		
		int minColumns = 600000;
		
		/*
		try (OPCPackage p = OPCPackage.open(fileList[2].getPath(), PackageAccess.READ)) {
            XLSX2CSV xlsx2csv = new XLSX2CSV(p, System.out, minColumns);
            xlsx2csv.process();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			
		
		dateConversion = workbookList.get(0).getSheetAt(0); 
		plancodeSheet = workbookList.get(1).getSheetAt(0);
		//dataSheet = workbookList.get(2).getSheetAt(0);
		
		System.out.println("[+] XLSX Initialization Successful.");
		//end constructor
		}
	
	public ArrayList<RowPack> initialize() {
		int count = 1;
		ArrayList<RowPack> myList = new ArrayList<RowPack>();
		
		//indexes for column identification
		int pDi=0;
		int pi=0;
		int pri=0;
		int a1i=0;
		int a2i=0;
		int a3i=0;
		int si =0;
		
		for (Row theRow: dataSheet) {
			try {
			if(theRow.getCell(1).toString() != null) {
				//System.out.println(theRow.getCell(1).toString());
				if (count==1) {
					for (Cell cell: theRow) {
						cell.setCellType(CellType.STRING);
						switch(cell.getStringCellValue()) {
						case "PolicyDate":
							pDi = cell.getColumnIndex();
							break;
						case "Plan":
							pi = cell.getColumnIndex();
							break;
						case "ReinPrem1":
							pri = cell.getColumnIndex();
							break;
						case "1st Agent ID":
							a1i = cell.getColumnIndex();
							break;
						case "2nd Agent ID":
							a2i = cell.getColumnIndex();
							break;
						case "3rd Agent ID":
							a3i = cell.getColumnIndex();
							break;
						case "ReinPrem Sign1":
							si = cell.getColumnIndex();
						default:
							//System.out.println("[...] Locating necessary columns...");
						}
					}
					count++;
					System.out.println("[+] Columns Located...");
					System.out.println(pDi +","+ pi +","+ pri +","+ a1i +","+ a2i +","+ a3i);
				}
				else {
				
				System.out.println("[...] Processing Row: "+count);
				
				System.out.println("[...] Procsessing Agents");
				
				Agent agent1;
				if (theRow.getCell(a1i)!=null) {
					agent1 = this.agentFactory(a1i, theRow);
					if (!(agentIDs.contains(agent1.getID()))){
						agentIDs.add(agent1.getID());
						agents.add(agent1);						
					}
					else {
						int index = this.findAgentIndex(agents, agent1);
						agents.get(index).addCount();
					}
				}
				else {agent1=null;}
				
				Agent agent2;
				if (theRow.getCell(a2i)!=null) {
					agent2 = this.agentFactory(a2i, theRow);
					if (!(agentIDs.contains(agent2.getID()))) {
						agentIDs.add(agent2.getID());
						agents.add(agent2);
					}	
					else {
						int index = this.findAgentIndex(agents, agent2);
						agents.get(index).addCount();
					}
				}
				else{agent2 = null;}
				
				
				Agent agent3;
				if (theRow.getCell(a3i)!=null) {					
					//if (theRow.getCell(a3i).getNumericCellValue()!=0 && theRow.getCell(a3i+1).getNumericCellValue()!=0) {
						agent3 = this.agentFactory(a3i, theRow);
						if (!(agentIDs.contains(agent3.getID()))) {
							agentIDs.add(agent3.getID());
							agents.add(agent3);
						}	
						else {
							int index = this.findAgentIndex(agents, agent3);
							agents.get(index).addCount();
						}
				}
				else{agent3 = null;}
				
				System.out.println("[!] Agents: "+ agent1 +", "+agent2+", "+ agent3);
				System.out.println("[+] Processed Agents");
				
				theRow.getCell(1).setCellType(CellType.STRING);
				String policyNum = theRow.getCell(1).getStringCellValue();
				
				//theRow.getCell(31).setCellType(CellType.STRING);
				
				theRow.getCell(pi).setCellType(CellType.STRING);
				String planCode = this.convertPlanCode(theRow.getCell(pi).getStringCellValue());
				//String planCode = theRow.getCell(13).getStringCellValue();
				
				System.out.println("[+] Plan Code extracted: "+planCode);
				
				theRow.getCell(pDi).setCellType(CellType.NUMERIC);
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String temp = sdf.format(theRow.getCell(pDi).getDateCellValue());
				Date premiumDate = new Date(temp);
				//System.out.println("[+] Premium Date extracted: "+premiumDate);
				
				double premiumAmt = theRow.getCell(pri).getNumericCellValue();
	
				theRow.getCell(si).setCellType(CellType.STRING);
				String sign = theRow.getCell(si).getStringCellValue();
				
				RowPack thePack = new RowPack(policyNum,planCode,premiumDate,agent1,agent2,agent3,premiumAmt,sign);
				System.out.println(thePack);
				
				myList.add(thePack);
				count++;
					}//end else
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("[!!!] Exception Detected");
			}
		}
		return myList;
	}//endfunction
	
	public Agent agentFactory(int column, Row theRow) {
		
		Agent agent1 = new Agent((int)theRow.getCell(column).getNumericCellValue(),
				theRow.getCell (column+1).getNumericCellValue()/100,
				this.getAgentDate((int)theRow.getCell(column).getNumericCellValue()));
		
		//System.out.println("[!] Agent Created with: ID: "+agent1.getID()+" DATE: "+agent1.getPolicyStartDate()+" SHARE: "+agent1.getShare());
		return agent1;
	
	}
	
	public String convertPlanCode(String old) {
		String returnThis = null;
		for (Row theRow: plancodeSheet) {
				String here = theRow.getCell(0).getStringCellValue();
				if (here.equals(old)) {
					String temp = theRow.getCell(1).getStringCellValue();
					returnThis = temp;
				}
			}
		return returnThis;
	}
	
	@SuppressWarnings("deprecation")
	public Date getAgentDate(int date) {
		Date returnThis = null;
		String theString = Integer.toString(date);
		for (Row theRow: dateConversion) {
			theRow.getCell(1).setCellType(CellType.STRING);
				String num = theRow.getCell(1).getStringCellValue();
				if (num.equals(theString)){
					theRow.getCell(3).setCellType(CellType.NUMERIC);
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String temp = sdf.format(theRow.getCell(3).getDateCellValue());
					returnThis = new Date(temp);
				}
			}
		return returnThis;
	}
	
	public void addToAgent(Agent agent1, double amount) {
		int index = this.findAgentIndex(agents, agent1);
		System.out.println("[!!!!!!] Adding "+amount+" to "+agents.get(index).getTotal() +" for Agent ID: "+agent1.getID() );
		agents.get(index).addTotal(amount);
	}
	
	public double getTotal(int id) {
		int index = this.findAgentIndex2(agents, id);
		return agents.get(index).getTotal();
	}
	
	public int findAgentIndex2(ArrayList<Agent> list, int id) {
		int index = -1;
		for (int x=0; x < list.size(); x++) {
			Agent agent = list.get(x);
			if (agent.getID()==id) {
				index = x;
				break;
			}
		}
		return index;
	}
	
	public int findAgentIndex(ArrayList<Agent> list, Agent theAgent) {
		int index = -1;
		for (int x=0; x < list.size(); x++) {
			Agent agent = list.get(x);
			if (agent.getID()==(theAgent.getID())) {
				index = x;
				break;
			}
		}
		return index;
	}
		
}
