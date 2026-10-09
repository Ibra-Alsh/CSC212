package ProjectDS_P1;

public abstract class Person implements IPerson throws IllegalArgumentException {
	
	protected final int id;
	protected String name;
	protected String phoneNumber;
	protected LinkedList<IRide> rideHistory;
	
	public Person(int id,String name,String phoneNumber) {
		this.id=id;
		this.name=name;
		setPhoneNumber(phoneNumber);
		rideHistory=new LinkedList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
		if(phoneNumber==null||phoneNumber.length()!=10||!phoneNumber.matches("[0-9]+")) {
		throw new IllegalArgumentException("Phonne number must be exactly 10 digits (numric characters only)");
		}
		this.phoneNumber=phoneNumber;
	}

	public int getId() {
		return id;
	}
	
	public LinkedList<IRide> getRideHistory(){
		return rideHistory;
	}

	@Override
	public String toString() {
		return "Person: " + id + ", name: " + name + ", phoneNumber: " + phoneNumber ;
	}
	
	
	
	

}
