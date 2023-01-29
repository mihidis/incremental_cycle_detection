import java.util.*;
public class Subgraph {

	private int i;
	private int j;
	int nextNode;
	int lastNode;
	private ArrayList<Nodes> nodes = new ArrayList<Nodes>();
	
	Subgraph(int i,int j){
		this.i=i;
		this.j=j;
	}
	
	void addNode(Nodes node) {
		nodes.add(node);
		node.setCurrSubgraph(i,j);
	}
	
	void deleteNode(Nodes node) {
		nodes.remove(node);
	}
	
	int getNumOfNodes() {
		return nodes.size();
	}
	
	void printSubNodes() {
		System.out.print("Subgraphs "+(i+1) +" "+(j+1)+" nodes are:");
		for(Nodes element:nodes) {
			System.out.print(element.getCurr()+" ");
		}
		System.out.println();
	}
	
	ArrayList<Integer> getNodesToNum(){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(Nodes element: nodes) {
			temp.add(element.getCurr());
		}
		return temp;
	}
	
	void setNextNode(int k) {
		this.nextNode= k;
	}
	
	void setLastNode(int k) {
		this.lastNode = k;
	}
	
	int getLastNode() {
		return lastNode;
	}
	
	int getNextNode() {
		return nextNode;
	}
	
}
