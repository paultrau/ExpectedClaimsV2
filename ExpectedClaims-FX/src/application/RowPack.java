package application;

import java.io.File;



public class RowPack {
	boolean is10load;
	double load;
	double adjPremuim;
	int theMode;
	
	String plancode;
	Date premiumDate;
	
	String policyNum;
	
	String sign;
	
	boolean a1 = false;
	boolean a2 = false;
	boolean a3 = false;
	
	Agent agent1;
	Agent agent2;
	Agent agent3;

	
	double premiumAmt;
	
	double newPremium1;
	double newPremium2;
	double newPremium3;
	
	int countedID;
	
	
	public RowPack(String policyNum, String planCode, Date premiumDate, Agent agent1, Agent agent2, Agent agent3, double premiumAmt, String sign) {
		this.policyNum = policyNum;
		this.premiumDate = premiumDate;
		this.sign = sign;

		int count = 3;
		
		this.agent1 = agent1;
		if (agent1.getPolicyStartDate()==null) {
			a1=false;
		}
		else if (premiumDate.isAfter(agent1.getPolicyStartDate())) 
		{
			a1=true;
		}
		
		this.agent2 = agent2;
		if (agent2!=null) {
			if (agent2.getPolicyStartDate()==null) {
				a2=false;
			}
			else if (premiumDate.isAfter(agent2.getPolicyStartDate())) {a2=true;}
		}
		else {
			System.out.println("[!] Agent 2 Null");
			count--;
			}
		
		this.agent3 = agent3;
		if (agent3!=null) {
			if (agent3.getPolicyStartDate()==null) {
				a3=false;
			}
			else if (premiumDate.isAfter(agent3.getPolicyStartDate())) {a3=true;}
		}
		else {
			System.out.println("[!] Agent 3 Null");
			count--;
			}
		
		this.premiumAmt = premiumAmt;
		this.plancode = planCode;
		
		if (planCode.contains("S")) {
			is10load = true;
		}
		else {
			is10load = false;
		}
		
		this.setCount(count);
	}
	
	public void calculateShares() {
		//set load
		System.out.println("[!] Premium: "+ this.premiumAmt);
		this.adjPremuim = this.premiumAmt/100;
		System.out.println("[!] Adjusted Premium: "+ this.adjPremuim);
		
		if (this.is10load) {
			this.load = this.adjPremuim / 1.1;
			this.load = this.load / 10;
			System.out.println("[!] Load: "+ load);
			this.adjPremuim = this.adjPremuim - this.load;
		}
		else {
			this.load = 0;
		}
		
		if (this.sign.equals("-")){
			this.adjPremuim = this.adjPremuim * -1;
		}
		
		switch(this.theMode) {
		case 1:
			System.out.println("[1] Mode 1");
			if (a1)
				this.newPremium1 = this.adjPremuim * this.agent1.getShare()/(this.agent1.getShare());	
			if (a2)	
				this.newPremium2 = this.adjPremuim * this.agent2.getShare()/(this.agent2.getShare());	
			if (a3)	
				this.newPremium3 = this.adjPremuim * this.agent3.getShare()/(this.agent3.getShare());	
			break;
		case 2:
			System.out.println("[2] Mode 2");
			System.out.println("[+] Bools: "+ a1+a2+a3);
			if (a1 && a2) {
				this.newPremium1 = (this.agent1.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent2.getShare());
				this.newPremium2 = (this.agent2.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent2.getShare());
				}
			if (a1 && a3) {
				this.newPremium1 = (this.agent1.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent3.getShare());
				this.newPremium3 = (this.agent3.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent3.getShare());
				}
			if (a3 && a2) {
				this.newPremium3 = (this.agent3.getShare()*this.adjPremuim) / (this.agent3.getShare()+this.agent2.getShare());
				this.newPremium2 = (this.agent2.getShare()*this.adjPremuim) / (this.agent3.getShare()+this.agent2.getShare());
				}
			
			if (a1 && (!a2 || !a3)) {
				if (!a2 && !a3) {
					this.newPremium1 = this.adjPremuim * this.agent1.getShare()/(this.agent1.getShare());	
				}
				if (!a2 && a3) {
					this.newPremium1 = (this.agent1.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent3.getShare());
					this.newPremium3 = (this.agent3.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent3.getShare());
				}
				if (a2 && !a3) {
					this.newPremium1 = (this.agent1.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent2.getShare());
					this.newPremium2 = (this.agent2.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent2.getShare());
				}
			}
			
			if (!a1 && a2) {
				this.newPremium2 = this.adjPremuim * this.agent2.getShare()/(this.agent2.getShare());				
				}
			if (a3 && !a1) {
				this.newPremium3 = this.adjPremuim * this.agent3.getShare()/(this.agent3.getShare());	
			}
			break;
		case 3:			
			System.out.println("[3] Mode 3");
			System.out.println("[+] Bools: "+ a1+a2+a3);
			if (a1 && a2 && a3) {
				this.newPremium1 = this.agent1.getShare()*this.adjPremuim;
				this.newPremium2 = this.agent2.getShare()*this.adjPremuim;
				this.newPremium3 = this.agent3.getShare()*this.adjPremuim;
			}
			if (a1 && (!a2 || !a3)) {
				if (!a2 && !a3) {
					this.newPremium1 = this.adjPremuim * this.agent1.getShare()/(this.agent1.getShare());		
				}
				if (!a2 && a3) {
					this.newPremium1 = (this.agent1.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent3.getShare());
					this.newPremium3 = (this.agent3.getShare()*this.adjPremuim) / (this.agent1.getShare()+this.agent3.getShare());
				}
				if (a2 && !a3) {
					this.newPremium1 = (this.agent1.getShare()*this.adjPremuim) / ((this.agent1.getShare()+this.agent2.getShare()));
					this.newPremium2 = (this.agent2.getShare()*this.adjPremuim) / ((this.agent1.getShare()+this.agent2.getShare()));
				}
			}
			if (!a1 && (a2 || a3)) {
				if (!a2 && a3) {
					this.newPremium3 = this.adjPremuim * this.agent3.getShare()/(this.agent3.getShare());
				}
				if (a2 && !a3) {
					this.newPremium2 = this.adjPremuim * this.agent2.getShare()/(this.agent2.getShare());				
					}
			}
			if (!a1 && a2 && a3) {
				this.newPremium2 = (this.agent2.getShare()*this.adjPremuim) / (this.agent2.getShare()+this.agent3.getShare());
				this.newPremium3 = (this.agent3.getShare()*this.adjPremuim) / (this.agent2.getShare()+this.agent3.getShare());
			}
			break;
		default:
			System.out.println("[-] No agents.");
		}
		System.out.println("[!!!] Adjusted Premium minus Load: "+ this.adjPremuim);
		System.out.println("[+] Sum of Agent Premiums: "+ (this.newPremium1+this.newPremium2+this.newPremium3) );
		
	}

	public void setCount(int mode) {
		this.theMode = mode;
		switch(mode) {
			case 1:
				if (a1)
					this.countedID = agent1.getID();
				if (a2)	
					this.countedID = agent2.getID();
				if (a3)	
					this.countedID = agent3.getID();
				break;
			case 2:
				if (a3 && a2) {
					if (agent3.getPolicyStartDate().isAfter(this.agent2.getPolicyStartDate()))
						this.countedID = agent2.getID();
					else
						this.countedID = agent3.getID();}
				if (a1 && a3) {
					if (agent3.getPolicyStartDate().isAfter(this.agent1.getPolicyStartDate()))
						this.countedID = agent1.getID();
					else
						this.countedID = agent3.getID();}
				if (a1 && a2) {
					if (agent2.getPolicyStartDate().isAfter(this.agent1.getPolicyStartDate()))
						this.countedID = agent1.getID();
					else
						this.countedID = agent2.getID();
					}
				if (a1 && !a2) {
					this.countedID = agent1.getID();
					}
				if (!a1 && a2) {
					this.countedID = agent2.getID();
					}
				break;
			case 3:
				if (a1 && a2 && a3) {
					if (agent2.getPolicyStartDate().isAfter(agent3.getPolicyStartDate())) {
						if (agent1.getPolicyStartDate().isAfter(agent3.getPolicyStartDate())) {
							this.countedID = agent3.getID();
						}
					}
					if (agent1.getPolicyStartDate().isAfter(agent2.getPolicyStartDate())) {
						if (agent3.getPolicyStartDate().isAfter(agent2.getPolicyStartDate())) {
							this.countedID = agent2.getID();
						}
					}
					if (agent2.getPolicyStartDate().isAfter(agent1.getPolicyStartDate())) {
						if (agent3.getPolicyStartDate().isAfter(agent1.getPolicyStartDate())) {
							this.countedID = agent1.getID();
						}
					}}
				if (a1 && a2 && !a3) {
					if (agent1.getPolicyStartDate().isAfter(this.agent2.getPolicyStartDate()))
						this.countedID = agent2.getID();
					else
						this.countedID = agent1.getID();
					}
				if (!a1 && a2 && !a3) {
					this.countedID = agent2.getID();
				}
				if (!a1 && !a2 && a3) {
					this.countedID = agent3.getID();
				}
				if (!a1 && !a2 && !a3) {
					this.countedID=0;
				}
				if (!a1 && a2 && a3) {
					if (agent3.getPolicyStartDate().isAfter(this.agent2.getPolicyStartDate()))
						this.countedID = agent2.getID();
					else
						this.countedID = agent3.getID();
					
				}
				if (a1 && !a2 && !a3) {
					this.countedID = agent1.getID();
					
				}
				if (a1 && !a2 && a3) {
					if (agent1.getPolicyStartDate().isAfter(this.agent3.getPolicyStartDate()))
						this.countedID = agent3.getID();
					else
						this.countedID = agent1.getID();
				}
				break;
			default:
				System.out.println("[-] No agents.");
			}//end switch 
		}//end function
	
	
	public String toString() {
		//return ("Date:" +this.premiumDate);
		return ("Row Object with... Plan Code: "+this.plancode+" Premium Date: "+premiumDate+ " Premium Amount: "+this.premiumAmt+" E: "+a1+a2+a3);
	}
}
