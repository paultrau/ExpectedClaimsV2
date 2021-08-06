package application;

public class Agent {

	private int ID;
	private double share;
	private Date policyStartDate;
	private boolean isEligable;
	private double finalShare;
	private int counter;
	private double finalTotal=0;
	
	
	public Agent(int id, double share, Date theDate) {
		this.ID = id;
		this.share = share;
		this.policyStartDate = theDate;
		this.counter = 1;
		this.setEligable();
	}
	
	public void addTotal(double amount) {
		this.finalTotal = this.finalTotal + amount;
	}
	
	public double getTotal() {
		return finalTotal;
	}
	
	public String toString() {
		return "[Agent "+this.ID+"] Date: " +this.policyStartDate;
	}
	
	public void addCount() {
		this.counter++;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}


	public double getShare() {
		return share;
	}


	public void setShare(double share) {
		this.share = share;
	}


	public Date getPolicyStartDate() {
		return policyStartDate;
	}


	public void setPolicyStartDate(Date policyDate) {
		this.policyStartDate = policyDate;
	}

	public boolean isEligable(Date date) {
		boolean is = this.policyStartDate.isAfter(date);
		return is;
	}


	public void setEligable() {
		if (this.policyStartDate == null) {
			this.isEligable=false;
		}
		}

	public int getCount() {
		// TODO Auto-generated method stub
		return this.counter;
	}
}
