import java.util.*;

public class TopSort {
	private int head;
	private int tail;
	private Nodes[] nodes;
	Stack stack;
	
	TopSort(Graph graph, Nodes[] nodes){
		this.nodes = nodes;
		stack = new Stack();
		boolean visited[] = new boolean[graph.getN()];
		
		for(int i=0;i<graph.getN();i++) {
			visited[i]=false;
		}
		for(int i=0;i<graph.getN();i++) {
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
	
	void makeNodes() {
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
	
	void giveLabels() {
		double index = 1.0;
		int temp = head;
		while(true) {
			if(nodes[temp].getNext()==-1) {
				nodes[temp].setLabel(index);
				break;
			}else {
				nodes[temp].setLabel(index);
				index += 1.0;
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
	
	int getHead() {
		return head;
	}
	
	int getTail() {
		return tail;
	}
	/*
	boolean compare(int x, int y) {
		int temp=head;
		while(true) {
			if(temp==x) {
				return true;
			}else if(temp==y) {
				return false;
			}else {
				temp = nodes[temp].getNext();
			}
		}
	}*/
	
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
	
	void insert_before(int x,int y) {
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
		if(nodes[x].getPrev()!=-1) {
			label_conflicts(x,y,1);
		}else {
			nodes[y].setLabel(nodes[x].getLabel()-0.0000001);
		}
		nodes[y].setNext(x);
		if(nodes[x].getPrev()!=-1) {
			nodes[nodes[x].getPrev()].setNext(y);
			nodes[y].setPrev(nodes[x].getPrev());
		}else {
			nodes[y].setPrev(-1);
			head=y;
		}
		nodes[x].setPrev(y);
	}
	
	void insert_after(int x,int y) {
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
		if(nodes[x].getNext()!=-1) {
			label_conflicts(x,y,2);
		}else {
			nodes[y].setLabel(nodes[x].getLabel()+0.0000001);
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
	}
	
	void label_conflicts(int x,int y,int order){
		if(order==1) {
			while(true) {
				if(nodes[nodes[x].getPrev()].getLabel()==nodes[x].getLabel()-0.0000001) {
					label_conflicts(nodes[x].getPrev(),nodes[x].getPrev(),1);
				}else {
					nodes[y].setLabel(nodes[x].getLabel()-0.0000001);
				}
			}
		}else {
			while(true) {
				if(nodes[nodes[x].getNext()].getLabel()==nodes[x].getLabel()+0.0000001) {
					label_conflicts(nodes[x].getNext(),nodes[x].getPrev(),2);
				}else {
					nodes[y].setLabel(nodes[x].getLabel()+0.0000001);
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
	
	void moveUpChange(ArrayList<Nodes> Q) {
		int temp = getTail();
		while(!Q.isEmpty()) {
			  
			if(Q.contains(nodes[temp])){
				if(temp!=head) {
					Q.remove(nodes[temp]);
					int cur = nodes[temp].getPrev();
					insert_before(head,temp);
					temp=cur;
				}else {
					Q.remove(nodes[temp]);
				}
			}else {
				temp = nodes[temp].getPrev();
			}
		}
		
	}
	
	void moveDownChange(ArrayList<Nodes> Q) {
		int temp = getHead();
		while(!Q.isEmpty()) {
			if(Q.contains(nodes[temp])){
				
				if(temp!=tail) {
					Q.remove(nodes[temp]);	
					int cur = nodes[temp].getNext();
					insert_after(tail,temp);
					temp = cur;
				}else {
					Q.remove(nodes[temp]);	
				}
			}else {
				temp = nodes[temp].getNext();
			}
		}
	}
}
