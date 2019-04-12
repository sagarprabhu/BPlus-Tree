import java.util.ArrayList;
import java.util.List;

/**class for a key-value dictionary pair
 * 
 * @author sagar prabhu
 *
 */
public class Pair {
	
	int key;
	
	// List of values associated with the key
	List<String> values;
	
	// For creating internal nodes
	public Pair(int key) {
		this.key = key;
		this.values = new ArrayList<String>();
	}
	
	// For creating external nodes
	public Pair(int key, String value) {
		this.key = key;
		
		if(this.values == null) 
			this.values = new ArrayList<String>();
		
		this.values.add(value);	
	}
	
	/**
	 * getter method
	 * @return values associated with the key
	 */
	public List<String> getValues() {
		return this.values;
	}
	
	/**
	 * setter method
	 * @param List of values 
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	public String toString() {
		return "(" + key +"," + values + ")";
	}
}
