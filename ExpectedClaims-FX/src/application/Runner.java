package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		//get folderpath from user
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("[?] Enter filepath: ");
	    String folderPath = myObj.nextLine();  // Read user input
	    System.out.println("[+] Filepath is: " + folderPath);  // Output user input
	    File[] allFiles = new File(folderPath).listFiles(); 
	    System.out.println("[+] Files loaded!");  // Output user input
	    Parser theParser = new Parser(allFiles);
	   
	    ArrayList<RowPack> theData = new ArrayList<RowPack>();
	    
		System.out.println("[...] Initializing Row Data...");
	    
		theData = theParser.initialize();
		
	    System.out.println("[+] Initialized Row Data!");
		int count = 2;
		for (RowPack row: theData) {
			System.out.println("");
			System.out.println("[+] Displaing Row: " +count++);
			row.calculateShares();
			//System.out.println(row.countedID);
			System.out.println("Final Premiums (Agent 1-3): "+row.newPremium1 +", "+row.newPremium2 +", "+row.newPremium3 +", ");
			System.out.println("Counted ID: " + row.countedID);
		}
		
		System.out.println("[...] Writing files...");
		ExportTool.writeFile(theData, theParser);
		System.out.println("[+] Files exported!");
		//create final sheets
		
		/*for (Agent agent: theParser.agents) {
			System.out.println("["+agent+"]");
			System.out.println("["+agent.getID()+"] Count: " + agent.getCount());
		}*/
	}	
}
