

public class RiderList implements IRiderList{
	
	private LinkedList<IRider> riders;
	
	public RiderList() {
		riders=new LinkedList<>();
	}
	
	
	public boolean add(IRider rider) {
		
		if(rider==null||findById(rider.getId())!=null||findByEmail(rider.getEmail())!=null) {
			return false;
		}
		
		if(riders.empty()) {
			riders.insert(rider);
		return true;
		}
		
		riders.findfirst();
		
		while(true) {
			IRider current=riders.retrieve();
			
			
			 if(current.compareTo(rider)>0){
				riders.update(rider);
				riders.insert(current);
				return true;
			}
			 
			 if(riders.last()) {
				 riders.insert(rider);
				 return true;
			 }
			
			riders.findnext();
		}
		
	}
	
	
	
	public IRider findById(int riderId) {
		if(riders.empty()) return null;
		
		riders.findfirst();
		while(true) {
			if(riders.retrieve().getId()==riderId) {
				return riders.retrieve();
			}
			if(riders.last()) {
				return null;
			}
			riders.findnext();
		}
		
		
	}
	
	public LinkedList<IRider> findByName(String fullName){
		LinkedList<IRider> temp=new LinkedList<>();
		if(riders.empty())
			return temp;
		
		riders.findfirst();
		
		
		while(true) {
			if(riders.retrieve().getName().equalsIgnoreCase(fullName)) {
				temp.insert(riders.retrieve());
			}
			if(riders.last()) {
				break;
			}
			riders.findnext();
		}
		return temp;
		
	}
	
	public IRider findByEmail(String email) {
		
if(riders.empty()) return null;
		
		riders.findfirst();
		while(true) {
			if(riders.retrieve().getEmail().equals(email)) {
				return riders.retrieve();
			}
			if(riders.last()) {
				return null;
			}
			riders.findnext();
		}
		
	}
	
	
	public  LinkedList<IRider> findByHomeCity(String homeCity){
		
		LinkedList<IRider> temp=new LinkedList<>();
		if(riders.empty())
			return temp;
		
		riders.findfirst();
		
		
		while(true) {
			if(riders.retrieve().getHomeCity().equalsIgnoreCase(homeCity)) {
				temp.insert(riders.retrieve());
			}
			if(riders.last()) {
				break;
			}
			riders.findnext();
		}
		return temp;
	}
	
	
	
	public LinkedList<IRider> getAll(){
		LinkedList<IRider> temp=new LinkedList<>();
		if(riders.empty())
			return temp;
		
		riders.findfirst();
		
		
		while(true) {
			
				temp.insert(riders.retrieve());
			
			if(riders.last()) {
				break;
			}
			riders.findnext();
		}
		return temp;
		
	}
	
	
	
	public boolean removeById(int riderId) {
		if(riders.empty()||findById(riderId)==null) {
			return false;
		}
		
		else {
			riders.remove();
			return true;
		}
	}
	
	public  boolean removeByEmail(String email) {
		if(riders.empty()||findByEmail(email)==null) {
			return false;
		}
		
		else {
			riders.remove();
			return true;
		}
	}
	
	
	public int removeByName(String fullName) {
		
		  if (riders.empty()) {
	            return 0;
	        }

	        int count = 0;
	        riders.findfirst();

	        while (true) {

	            IRider current = riders.retrieve();

	            if (current.getName().equalsIgnoreCase(fullName)) {
	                riders.remove();
	                count++;
	                if (riders.empty()) {
	                    break;
	                }
	                continue;
	            }
	            if (riders.last()) {
	                break;
	            }

	            riders.findnext();
	        }

	        return count;
	}
		

	
	
	
	public int removeByHomeCity(String homeCity) {
		
		 if (riders.empty()) {
	            return 0;
	        }

	        int count = 0;
	        riders.findfirst();

	        while (true) {

	            IRider current = riders.retrieve();

	            if (current.getHomeCity().equalsIgnoreCase(homeCity)) {
	                riders.remove();
	                count++;
	                if (riders.empty()) {
	                    break;
	                }
	                continue;
	            }
	            if (riders.last()) {
	                break;
	            }

	            riders.findnext();
	        }

	        return count;
	}
	
	
	
	
	public int size() {
		int count=0;
		
		if(riders.empty())
			return count;
		
		riders.findfirst();
		
		
		while(true) {
			
			count++;
			
			if(riders.last()) {
				break;
			}
			riders.findnext();
		}
		return count;
		
	}
	
	
	
	

}
