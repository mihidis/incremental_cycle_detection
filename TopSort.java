package IncrementalCycleDetection;
import java.util.*;
//in this class theres the data structure for O(1) insert_after, insert_before, delete, compare
//also stores the topological sorting of the nodes
public class TopSort {
	private int head;
	private int tail;
	private Nodes[] nodes;
	Stack stack;
	
	TopSort(Graph graph, Nodes[] nodes){//initializing the top sort 
		this.nodes = nodes;
		stack = new Stack();
		boolean visited[] = new boolean[graph.getN()];
		
		for(int i=0;i<graph.getN();i++) {
			visited[i]=false;
		}
		for(int i=graph.getN()-1;i>=0;i--) {
			if(visited[i]==false) {
				topologicalSortUtil(graph,i,visited,stack);
			}
		}
		makeNodes();
		giveLabels(); 
	}
	
	void topologicalSortUtil(Graph graph,int v,boolean visited[],Stack stack) {
		visited[v]=true;
		Integer i;

		Iterator<Integer> it = graph.returnIndexEdges(v).iterator();
		
		
		while(it.hasNext()) {
			i=it.next();
			if(!visited[i]) {
				topologicalSortUtil(graph,i,visited,stack);
			}
		}
		stack.push(v);
		
	}
	
	void makeNodes() {// this function gets the stack and pops each element into its place storing the next and previous of every node
					  //also stores the tail and head of the topological sort
		head = (int) stack.pop();
		nodes[head].setPrev(-1);
		int tempPrev = head;
		int temp;
		while(stack.empty()==false) {
			temp = (int) stack.pop();
			nodes[tempPrev].setNext(temp);
			nodes[temp].setPrev(tempPrev);
			tempPrev=temp;
		}
		nodes[tempPrev].setNext(-1);
		tail=tempPrev;
	}
	
	void giveLabels() {//labels are needed for the compare in constant time. its a value to compare the nodes
		int index = 10000;
		int temp = head;
		while(true) {
			if(nodes[temp].getNext()==-1) {
				nodes[temp].setLabel(index);
				break;
			}else {
				nodes[temp].setLabel(index);
				index += 10000;
				temp = nodes[temp].getNext();  
			}
		}
	}
	
	void printTopSort() {
		System.out.println("Current topological sort: ");
		int temp=head;
		while(true) {
			System.out.print(nodes[temp].getCurr() + " ");
			if(nodes[temp].getNext()==-1) {
				break;
			}else {
				temp = nodes[temp].getNext();
			}
		}
		System.out.println("");
	}
	
	int[] returnTopSortToArr() {// returns an array with the topological sorting of the nodes
		int temp=head;
		int [] Array = new int[nodes.length];
		for(int i =0;i<nodes.length;i++) {
			Array[i]=temp;
			temp = nodes[temp].getNext();
		}
		return Array;
	}
	
	int getHead() {
		return head;
	}
	
	int getTail() {
		return tail;
	}
	
	boolean compare(int x,int y) {
		if(nodes[x].getLabel()>nodes[y].getLabel()) {
			return true;
		}else {
			return false;
		}
	}
	
	void delete(int x) {
		int tempAsc = nodes[x].getPrev();
		int tempDes = nodes[x].getNext();
		
		nodes[tempAsc].setNext(tempDes);
		nodes[tempDes].setPrev(tempAsc);
	}
	
	void insert_before(int x,int y) {//in this function i insert y before x
		System.out.println("In insert before are: "+ x+" "+y);
		if(nodes[y].getPrev()==-1) {	//firstly i release y from his neighbors
			nodes[nodes[y].getNext()].setPrev(-1);
			head=nodes[y].getNext();
		}else if(nodes[y].getNext()==-1) {
			nodes[nodes[y].getPrev()].setNext(-1);
			tail= nodes[y].getPrev();
		}else {
			nodes[nodes[y].getPrev()].setNext(nodes[y].getNext());
			nodes[nodes[y].getNext()].setPrev(nodes[y].getPrev());
		}
		
		nodes[y].setNext(x);	//set all the neighbors to appropriate linking 
		if(nodes[x].getPrev()!=-1) {
			nodes[nodes[x].getPrev()].setNext(y);
			nodes[y].setPrev(nodes[x].getPrev());
		}else {
			nodes[y].setPrev(-1);	
			head=y;
		}
		nodes[x].setPrev(y);
		label_conflicts(x,y,1); //deal with nodes that want to take the same label
	}
	
	void insert_after(int x,int y) {//in this function i insert y after x 
		System.out.println("In insert after are: "+ x+" "+y);	//same things that happen in insert_before
		if(nodes[y].getPrev()==-1) {
			nodes[nodes[y].getNext()].setPrev(-1);
			head=nodes[y].getNext();
		}else if(nodes[y].getNext()==-1) {
			nodes[nodes[y].getPrev()].setNext(-1);
			tail= nodes[y].getPrev();
		}else {
			nodes[nodes[y].getPrev()].setNext(nodes[y].getNext());
			nodes[nodes[y].getNext()].setPrev(nodes[y].getPrev());
		}

		nodes[y].setPrev(x);
		if(nodes[x].getNext()==-1) {
			nodes[y].setNext(-1);
			tail=y;
		}else {
			nodes[y].setNext(nodes[x].getNext());
			nodes[nodes[x].getNext()].setPrev(y);
		}
		nodes[x].setNext(y);
		label_conflicts(x,y,2);
	}
	
	void label_conflicts(int x,int y,int order){//in case a node wants to take a label that another node already has
												//i need to resolve this conflict
		System.out.println("In label conflicts "+x+" "+y+" "+order);
		if(x!=y) {
			if(order==1) {
				nodes[y].setLabel(nodes[nodes[y].getNext()].getLabel()-1);
				if(nodes[y].getPrev()!=-1) {
					if(nodes[nodes[y].getNext()].getLabel()-1==nodes[nodes[y].getPrev()].getLabel()){
						label_conflicts(x,nodes[y].getPrev(),1);
					}
				}
			}else {
				nodes[y].setLabel(nodes[nodes[y].getPrev()].getLabel()+1);
				if(nodes[y].getNext()!=-1) {
					if(nodes[nodes[y].getPrev()].getLabel()+1==nodes[nodes[y].getNext()].getLabel()) {
						label_conflicts(x,nodes[y].getNext(),2);
					}
				}	
			}
		}
	}
	
	
	int findMin(ArrayList<Integer> n) {
		int temp = head;
		while(temp!=-1) {
			if(n.contains(temp)) {
				return temp;
			}else if(nodes[temp].getNext()!=-1){
				temp = nodes[temp].getNext();
			}else {
				return -1;
			}
		}
		return -1;
	}
	
	int findMax(ArrayList<Integer> n) {
		int temp = tail;
		while(temp!=-1) {
			if(n.contains(temp)) {
				return temp;
			}else if(nodes[temp].getPrev()!=-1) {
				temp=nodes[temp].getPrev();
			}else {
				return -1;
			}
		}
		return -1;
	}
	
	void moveUpChange(ArrayList<Nodes> Q, Subgraph[][] g) {
		
		Nodes[] Qarr = new Nodes[Q.size()];
		for(int i=0;i<Qarr.length;i++) {
			Qarr[i]= Q.get(i);
		}
		BuildMaxHeap maxHeap = new BuildMaxHeap();
		maxHeap.buildHeap(Qarr,Qarr.length);
		//maxHeap.printHeap(Qarr, Qarr.length);
		
		while(!Q.isEmpty()) {
			int i = nodes[Qarr[0].getCurr()].getCurrSubgraph()[0];
			int j = nodes[Qarr[0].getCurr()].getCurrSubgraph()[1];
			int temp = getTail();
			while(true) {
				
				if(nodes[temp].getPrev()!=-1) {
					int i2 = nodes[temp].getCurrSubgraph()[0];
					int j2 = nodes[temp].getCurrSubgraph()[1];
					if(i2<i || (i2==i && j2>j)) {
						Q.remove(Qarr[0]);
						insert_after(temp,nodes[maxHeap.extractMax(Qarr)].getCurr());
						break;
					}else {
						temp= nodes[temp].getPrev();
					}
				}else {
					
					Q.remove(Qarr[0]);
					for(Nodes element:Q) {
						System.out.println(element.getCurr());
					}
					insert_before(temp,nodes[maxHeap.extractMax(Qarr)].getCurr());
					break;
				}
			}	
		}
		System.out.println("Top sort after one move up:");
		printTopSort();
	}
	
	void moveDownChange(ArrayList<Nodes> Q,Subgraph[][] g) {
		Nodes[] Qarr = new Nodes[Q.size()];
		for(int i=0;i<Qarr.length;i++) {
			Qarr[i]= Q.get(i);
		}
		BuildMinHeap minHeap = new BuildMinHeap();
		minHeap.buildHeap(Qarr,Qarr.length);
		//minHeap.printHeap(Qarr, Qarr.length);

		while(!Q.isEmpty()) {
			int i = nodes[Qarr[0].getCurr()].getCurrSubgraph()[0];
			int j = nodes[Qarr[0].getCurr()].getCurrSubgraph()[1];
			int temp = getHead();
			while(true) {
				if(nodes[temp].getNext()!=-1) {
					int i2 = nodes[temp].getCurrSubgraph()[0];
					int j2 = nodes[temp].getCurrSubgraph()[1];
					if(i<i2 || (i==i2 && j>j2)) {
						Q.remove(Qarr[0]);
						insert_before(temp,nodes[minHeap.extractMin(Qarr)].getCurr());
						break;
					}else {
						temp = nodes[temp].getNext();
					}
				}else {
					int i2 = nodes[temp].getCurrSubgraph()[0];
					int j2 = nodes[temp].getCurrSubgraph()[1];
					if(i<i2 || (i==i2 && j>j2)) {
						Q.remove(Qarr[0]);
						insert_before(temp,nodes[minHeap.extractMin(Qarr)].getCurr());
						break;
					}else {
						Q.remove(Qarr[0]);
						insert_after(temp,nodes[minHeap.extractMin(Qarr)].getCurr());
						break;
					}
				}
			}
		}
		
		System.out.println("Top sort after one move down:");
		printTopSort();
	}
}
