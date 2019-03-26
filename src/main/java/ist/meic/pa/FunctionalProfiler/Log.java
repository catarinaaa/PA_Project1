package ist.meic.pa.FunctionalProfiler;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/*
* Class that keeps two maps that count reads and writes
*/
public class Log {

	private TreeMap<String, Integer> countReads = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> countWrites = new TreeMap<String, Integer>();

	private static Log instance = new Log();

	public static Log getInstance() {
		return instance;
	}

	/*
	* Add entry with class name to both maps
	*/
	public void addClass(String classname) {
		countReads.put(classname, 0);
		countWrites.put(classname, 0);
	}

	/*
	* Increment number of reads made in class with classname
	*/
	public void addRead(String classname) {
		System.out.println("->Read in " + classname +  " " + countReads.size());
		if(countReads.containsKey(classname)) {
			countReads.put(classname, countReads.get(classname)+1);
		} else {
			this.addClass(classname);
			this.addRead(classname);
		}
	}

	/*
	* Increment number of writes made in class with classname
	*/
	public void addWrite(String classname) {
		System.out.println("->Write in " + classname +  " " + countReads.size());
		if(countWrites.containsKey(classname)) {
			countWrites.put(classname, countWrites.get(classname)+1);
		} else {
			this.addClass(classname);
			this.addWrite(classname);
		}
	}

	/*
	* Sum all values of map
	*/
	public int countAll(Collection<Integer> values) {
		int total = 0;
		for(Integer el : values)
			total += el;
		return total;
	}

	/*
	* Print number of reads and writes for each class
	*/
	public void print() {
		System.out.println("Total reads: " + countAll(countReads.values()) +
			" Total writes: " + countAll(countWrites.values()));
		for(Map.Entry<String, Integer> el : countReads.entrySet()) {
			System.out.println("class " + el.getKey() + " -> reads: " + el.getValue() +
				" writes: " + countWrites.get(el.getKey()));
		}
	}
}
