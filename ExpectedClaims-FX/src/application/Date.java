package application;

public class Date {
	
	private int year;
	private int month;
	private int day;
	
	public Date(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	public Date(String dateString) {
		this.month = Integer.valueOf(dateString.substring(0,2));
		this.day = Integer.valueOf(dateString.substring(3,5));
		this.year = Integer.valueOf(dateString.substring(6,10));
	}
	public String  toString() {
		return this.month+"/"+this.day+"/"+this.year;
	}

	public boolean isAfter(Date otherDate) {
		if (this.year > otherDate.year) {
			return true;
		}
		else if (this.year < otherDate.year) {
			return false;
		}
		if (this.month > otherDate.month) {
			return true;
		}
		else if (this.month < otherDate.month)
			return false;
		if (this.day >= otherDate.day)
			return true;
		else if (this.day < otherDate.day)
			return false;
		return false;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
}
