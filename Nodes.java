import java.util.*;
public class Nodes {
	private int prev;
	private int next;
	private int curr;
	private int node;
	private double label;
	private int[] currSubgraph = new int[2];
	private int[] lastSubgraph = {0,0};
	private Edges edges = new Edges();
	private Edges incEdges = new Edges();
	private ArrayList<Integer> ancestors = new ArrayList<Integer>();
	private ArrayList<Integer> descentants = new ArrayList<Integer>();
	private Edges subEdges;
	private Edges incSubEdges;
	
	Nodes(int node){
		this.node = node;
		this.curr = node;
		ancestors.add(node);
		descentants.add(node);
	}

	void printNodesInfo() {
		System.out.println("Nodes "+curr+" size is :"+ ancestors.size()+" "+descentants.size());
		System.out.println("Nodes "+curr+" outgoing edges are : "+edges.getEdges());
		System.out.println("Nodes "+curr+" incoming edges are : "+incEdges.getEdges());
	}
	
	boolean checkEdgeExists(int towardsEdge) {
		if( edges.checkExists(towardsEdge)) {
			return true;
		}else {
			return false;
		}
	}
	
	void addEdge(int towardsEdge) {
		edges.addEdge_2(towardsEdge);
	}
	
	void addIncomingEdge(int incEdge) {
		incEdges.addEdge_2(incEdge);
	}
	
	ArrayList<Integer> indexEdges() {
		return edges.getEdges();
	}

	boolean checkRelation(int y) {
		if(descentants.contains(y)) {
			return true;
		}else if(ancestors.contains(y)){
			return true;
		}else {
			return false;
		}
	}
	
	boolean addDescentant(int u) {
		
		if(!descentants.contains(u)) {
			descentants.add(u);
			return true;
		}
		return false;
	}
	
	boolean addAncestor(int u) {
		if(!ancestors.contains(u)) {
			ancestors.add(u);
			return true;
		}
		return false;
	}
	
	void changeSubEdges(ArrayList<Integer> subNodes) {
		subEdges= new Edges();
		for(int element:subNodes) {
			if(edges.getEdges().contains(element)) {
				subEdges.addEdge_2(element);
			}
		}
		incSubEdges = new Edges();
		for(int element:subNodes) {
			if(incEdges.getEdges().contains(element)) {
				incSubEdges.addEdge_2(element);
			}
		}
	}
	
	void printSubEdges() {
		System.out.println("Nodes "+curr+" incoming SubEdges are: "+incSubEdges.getEdges());
		System.out.println("Nodes "+curr+" outgoing SubEdges are: "+subEdges.getEdges());
	}
	
	
	//GETTERS---SETTERS
	public int getNumNode() {
		return node;
	}
	
	public int getPrev() {
		return prev;
	}

	public void setPrev(int prev) {
		this.prev = prev;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getCurr() {
		return curr;
	}
	
	public double getLabel() {
		return label;
	}
	
	public void setLabel(double label) {
		this.label=label;
	}
	
	public ArrayList<Integer> getAncestor(){
		return ancestors;
	}
	
	public ArrayList<Integer> getDescentant(){
		return descentants;
	}
	
	public int[] getCurrSubgraph() {
		return currSubgraph;
	}
	
	public void setCurrSubgraph(int i,int j) {
		currSubgraph[0]=i;
		currSubgraph[1]=j;
	}
	
	public int[] getLastSubgraph() {
		return lastSubgraph;
	}
	
	public void setLastSubgraph(int i,int j) {
		lastSubgraph[0]=1;
		lastSubgraph[1]=j;
	}
	
	public ArrayList<Integer> getSubEdges(){
		return subEdges.getEdges();
	}
	
	public ArrayList<Integer> getIncSubEdges(){
		return incSubEdges.getEdges();
	}
}
