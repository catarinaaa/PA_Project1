package ist.meic.pa.FunctionalProfilerExtended;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/*
* Class that keeps two maps that count reads and writes
*/
public class Log {

	private TreeMap<String, Integer> countReads = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> countWrites = new TreeMap<String, Integer>();
	private TreeMap<String, TreeMap<String, Integer>> countOutsideClassRead = new TreeMap<String, TreeMap<String, Integer>>();
	private TreeMap<String, TreeMap<String, Integer>> countOutsideClassWrite = new TreeMap<String, TreeMap<String, Integer>>();

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
	* Increment number of reads made outside the class in which the field is declared
	*/
	public void addReadOutsideFieldClass(String fieldname, String classname){
		if(countOutsideClassRead.containsKey(classname)){
			if (countOutsideClassRead.get(classname).containsKey(fieldname))
				countOutsideClassRead.get(classname).put(fieldname, countOutsideClassRead.get(classname).get(fieldname) + 1);
			else
				countOutsideClassRead.get(classname).put(fieldname, 1);
		}
		else
			countOutsideClassRead.put(classname, new TreeMap<String, Integer>());
	}

	/*
	* Increment number of writes made outside the class in which the field is declared
	*/
	public void addWriteOutsideFieldClass(String fieldname, String classname){
		if(countOutsideClassWrite.containsKey(classname)){
			if (countOutsideClassWrite.get(classname).containsKey(fieldname))
				countOutsideClassWrite.get(classname).put(fieldname, countOutsideClassWrite.get(classname).get(fieldname) + 1);
			else
				countOutsideClassWrite.get(classname).put(fieldname, 1);
		}
		else
			countOutsideClassWrite.put(classname, new TreeMap<String, Integer>());
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
		if (countOutsideClassRead.size() == 0){
			System.out.println("Class reads outside its class: 0");
		}
		if (countOutsideClassWrite.size() == 0){
			System.out.println("Class writes outside its class: 0");
		}
		for(Map.Entry<String, TreeMap<String, Integer>> el : countOutsideClassRead.entrySet()){
			if (countOutsideClassRead.get(el.getKey()).size() == 0){
				System.out.println("Class " + el.getKey() + ": no fields read outside class");
			}
			else
				System.out.println("Class " + el.getKey() + ":");
			for (Map.Entry<String, Integer> fieldEl : countOutsideClassRead.get(el.getKey()).entrySet())
				System.out.println("\t" + fieldEl.getKey() + "-> reads outside its class: " + fieldEl.getValue());
		}
		for(Map.Entry<String, TreeMap<String, Integer>> el : countOutsideClassWrite.entrySet()){
			if (countOutsideClassWrite.get(el.getKey()).size() == 0){
				System.out.println("Class " + el.getKey() + ": no fields written outside class");
			}
			else
				System.out.println("Class " + el.getKey() + ":");
			for (Map.Entry<String, Integer> fieldEl : countOutsideClassWrite.get(el.getKey()).entrySet())
				System.out.println("\t" + fieldEl.getKey() + "-> writes outside its class: " + fieldEl.getValue());
		}
	}
}
