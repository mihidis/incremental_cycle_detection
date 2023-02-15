package IncrementalCycleDetection;
import java.util.*;

public class Edges {
	private ArrayList<Integer> edges = new ArrayList<>(); 
	
	
	//class that saves the edges of every node
	Edges(){
		
	}
	
	void addEdge_2(int towardsEdge) {
		edges.add(towardsEdge);
	}
	
	boolean checkExists(int towardsEdge) {
		if(edges.contains(towardsEdge)) {
			return true;
		}else {
			return false;
		}
	}
	
	ArrayList<Integer> getEdges() {
		return edges;
	}
}
