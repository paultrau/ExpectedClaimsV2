package application;

public class FinalPack {
	int id;
	int count;
	double total = 0;
	
	public FinalPack(int theid, int thecount, double total) {
		this.id=theid;
		this.count=thecount;
		
	}

	public FinalPack(Integer key, Integer value) {
		this.id=key;
		this.count=value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
