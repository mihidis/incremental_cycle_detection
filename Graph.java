package IncrementalCycleDetection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Graph {
	private int N;
	private Nodes nodes[];
	private TopSort topSort;
	private Subgraph subgraph[][];
	private ArrayList<Integer> Fa;
	private ArrayList<Integer> Fd;
	private ArrayList<Integer> Ba;
	private ArrayList<Integer> Bd;
	
	Graph(int N){
		this.N=N; 
		this.nodes = new Nodes[N];
		
		for(int i=0;i<N;i++) {
			nodes[i] = new Nodes(i); //innitializing nodes
		}		
		
		//this block is used so we can have a starting graph 
		/*
		int[][] edgesToAdd = randomEdges(); //in this var we get edges to add to the starting graph
		for(int i=0;i<edgesToAdd.length;i++) {
			nodes[edgesToAdd[i][0]].addEdge(edgesToAdd[i][1]);
			nodes[edgesToAdd[i][1]].addIncomingEdge(edgesToAdd[i][0]);
			updateAncestors(edgesToAdd[i][0],edgesToAdd[i][1]);
			updateDescentants(edgesToAdd[i][0],edgesToAdd[i][1]);
		}
		*/
		//
		topSort = new TopSort(this,nodes); //initializes top sort
		topSort.printTopSort();
		createSubgraphs(); //initializes subgraphs
	}
	
	int[][] randomEdges() { // gives random edges 
		int randomEdges = 3;//from here u can choose the number of starting edges 
		int[][] edgesToAdd = new int[randomEdges][2];
		Random rand = new Random();
		for(int i=0;i<randomEdges;i++) {
			int rand_int1 = rand.nextInt(N);
			int rand_int2 = rand.nextInt(N);
			
			//here we swap the edge so that the larger(smaller) node is the outgoing (incoming) edge
			//i use that so the starting top sort needs to be changed
			if(rand_int1>rand_int2) {
				edgesToAdd[i][0]=rand_int1;
				edgesToAdd[i][1]=rand_int2;
			}else if(rand_int1<rand_int2){
				edgesToAdd[i][1]=rand_int1;
				edgesToAdd[i][0]=rand_int2;
			}else {
				i--;
			}
		}
		/*
		for(int[] element:edgesToAdd) {
			System.out.println(element[0]+" "+element[1]);
		}
		*/
		return edgesToAdd;
	}
	
	int getN() {
		return N;
	}
	
	ArrayList<Integer> returnIndexEdges(int i) {//returns all the edges of a node
		return nodes[i].indexEdges();
	}
	
	void createSubgraphs(){ 
		subgraph = new Subgraph[N][N];
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				subgraph[i][j] = new Subgraph(i,j);
			}
		}
		for(int k=0;k<N;k++) {
			int i = nodes[k].getDescentant().size();
			int j = nodes[k].getAncestor().size();
			subgraph[i][j].addNode(nodes[k]);
		}
	}
	
	boolean addEdge(int[] newEdge) { 
		if(nodes[newEdge[0]].checkEdgeExists(newEdge[1])) {//checks if edge already excists 
			System.out.println("Edge already exists in graph! ");
		}else {
			
			//initializing Xup/Xdown for phase 2 
			ArrayList<Nodes> Xup = new ArrayList<Nodes>();
			ArrayList<Nodes> Xdown = new ArrayList<Nodes>();
			
			//adding the edges on the respective nodes
			nodes[newEdge[0]].addEdge(newEdge[1]);
			nodes[newEdge[1]].addIncomingEdge(newEdge[0]);
			
			//updates the subgraphs according to the changes made by the edge insertion
			updateSubgraphs(newEdge[0],newEdge[1],Xup,Xdown);
			
			
			Move_up(Xup);
			Move_down(Xdown);
			if(search(newEdge[0],newEdge[1])) {//search is a bool function that is true if it found a cycle (phase 3)
				System.out.println("Edge insertion creates cycle!");
				for(Nodes element: nodes) {
					System.out.println(element.getLabel());
				}
				return true;
			}else {
				System.out.println("Edge insertions doesnt create cycle!");
				
				update_forward(newEdge[1]);
				for(Nodes element: nodes) {
					System.out.println(element.getLabel());
				}
			}
		}
		return false;
	}
	
	void update_forward(int y){//phase 4 
		System.out.println("Forward update!");
		ArrayList<Integer> Q = new ArrayList<Integer>();
		Q=Fd;
		int temp = topSort.findMax(Q);
		Q.remove(Integer.valueOf(temp));
		while(Q.size()>0) {
			int temp_2 = topSort.findMax(Q);
			Q.remove(Integer.valueOf(temp_2));
			topSort.insert_before(temp_2, temp);
			temp=temp_2;
		}
		
		temp = y;
		Q=Bd;
		while(Q.size()>0) {
			int temp_2 = topSort.findMax(Q);
			Q.remove(Integer.valueOf(temp_2));
			topSort.insert_before(temp_2,temp);
			temp=temp_2;
		}
	}
	
	void updateSubgraphs(int x,int y,ArrayList<Nodes> Xup,ArrayList<Nodes> Xdown) {//phase 1
		
		ArrayList<Integer> alteredNodes = new ArrayList<Integer>();
		alteredNodes.addAll(updateAncestors(x,y));//gives the anc and desc list of the nodes the correct value 
		alteredNodes.addAll(updateDescentants(x,y));
		Set<Integer> set = new HashSet<>(alteredNodes);//i use the set because it erases duplicates fast 
		alteredNodes.clear();
		alteredNodes.addAll(set);
		
		//System.out.println(alteredNodes);
		for(int element: alteredNodes) {//for each node having changed anc/desc means the subgraph changes
			//System.out.println("nodes "+element+" desc are "+nodes[element].getDescentant());
			//System.out.println("nodes "+element+" asc are "+nodes[element].getAncestor());
			int i=nodes[element].getCurrSubgraph()[0];//i get the curr subgraph so i can delete the node
			int j=nodes[element].getCurrSubgraph()[1];
			//int temp1=i;
			//int temp2=j;
			subgraph[i][j].deleteNode(nodes[element]);
			int i_new=nodes[element].getAncestor().size()-1;//and i find the new subgraph to add the node
			int j_new=nodes[element].getDescentant().size()-1;
			//System.out.println(" "+element);
			subgraph[i_new][j_new].addNode(nodes[element]);		
			
			
			if(i<i_new || (i==i_new && j>j_new)) {//since i iterate through the nodes that changed subgraphs
				Xup.add(nodes[element]);		  //i sort the altered nodes into the respective set they need to be
			}else {								  // for phase 2
				Xdown.add(nodes[element]);
			}
			
		}
		for(int element: alteredNodes) {//since the subgraphs change i need to change the edges in each subgraph altered
			
			int i=nodes[element].getCurrSubgraph()[0];
			int j=nodes[element].getCurrSubgraph()[1];
			nodes[element].changeSubEdges(subgraph[i][j].getNodesToNum());
			
		}	
		
	}
	
	ArrayList updateAncestors(int x,int y) {//updating the ancestors of the node the edge is towards
											//this function also updates the ancestors of the descentants of y
		
		ArrayList<Integer> temp1 = new ArrayList<Integer>(nodes[x].getAncestor());
		temp1.removeAll(nodes[y].getAncestor());
		ArrayList<Integer> alteredNodes = new ArrayList<Integer>();
		int flag=0;
		for(int element: temp1) {
			nodes[y].addAncestor(element);
			flag=1;
		}
		if(flag==1) {
			alteredNodes.add(y);
		}
		
		
		for(int element: nodes[y].getDescentant()) {
			flag=0;
			for(int index: temp1) {
				if(nodes[element].addAncestor(index)) {
					flag=1;
				}
			}
			if (flag == 1){
				alteredNodes.add(element);
			}
		}
		//System.out.println(alteredNodes);
		return alteredNodes;
	}
	
	ArrayList updateDescentants(int x, int y) {//updating the descentants of the node the edge is coming out 
											   //this function also updates the descentants of the dancestors of x
		
		ArrayList<Integer> temp1 = new ArrayList<Integer>(nodes[y].getDescentant());
		temp1.removeAll(nodes[x].getDescentant());
		ArrayList<Integer> alteredNodes = new ArrayList<Integer>();
		
		int flag = 0;
		for(int element: temp1) {
			nodes[x].addDescentant(element);
			flag=1;
		}
		if(flag==1) {
			alteredNodes.add(x);
		}
		
		for(int element:nodes[x].getAncestor()) {
			flag=0;
			for(int index:temp1) {
				if(nodes[element].addDescentant(index)) {
					flag=1;
				}	
			}
			if (flag == 1){
				alteredNodes.add(element);
			}
		}

		return alteredNodes;
	}
	
	void Move_up(ArrayList<Nodes> Xup){//phase 2
		ArrayList<Nodes> Q = new ArrayList<Nodes>(Xup);
		for(Nodes element: Q) {
			System.out.print("Move Up: "+element.getCurr()+ " ");
		}
		System.out.println();
		topSort.moveUpChange(Q,subgraph);
	}
	
	void Move_down(ArrayList<Nodes> Xdown) {
		ArrayList<Nodes> Q = new ArrayList<Nodes>(Xdown);
		for(Nodes element: Q) {
			System.out.print("Move down: "+element.getCurr()+ " ");
		}
		System.out.println();
		topSort.moveDownChange(Q,subgraph);
	}
	
	
	boolean search(int x,int y) {//phase 3
		Fa= new ArrayList<Integer>();
		Fd= new ArrayList<Integer>();
		Ba= new ArrayList<Integer>();
		Bd= new ArrayList<Integer>();
		Fa.add(x);
		Ba.add(y);
		int flag=0;
		if(nodes[y].getCurrSubgraph()[0]!=nodes[x].getCurrSubgraph()[0] || nodes[y].getCurrSubgraph()[1]!=nodes[x].getCurrSubgraph()[1]) {
			flag=1;
		}
		
		if(flag==0) {
			int temp;
			while(Fa.size()>0 && Ba.size()>0) {
				temp = topSort.findMin(Fa);
				if(topSort.findMin(Bd)!=-1 && topSort.compare(x, topSort.findMin(Bd))) {
					
					return false;
				}else {
					if(explore_forward(x)==1) {
						return true;
					}
				}
				
				temp=topSort.findMax(Ba);
				if(Fd.size()>0) {
					if(!topSort.compare(temp, topSort.findMax(Fd))) {
						return false;
					}
				}else {
					if(explore_backward(y)==1) {
						return true;
					}
				}
			}
		}else {
			return false;
		}
		return false;
	}
	
	int explore_forward(int x) {
		int flag =0;
		Fa.remove(Integer.valueOf(x));
		Fd.add(x);
		for(int element:nodes[x].getSubEdges()) {
			if(Ba.contains(element) || Bd.contains(element)) {
				flag = 1;
				break;
			}else if(!Fa.contains(element) || !Fd.contains(element)) {
				Fa.add(element);
			}
		}
		return flag;
	}
	
	int explore_backward(int y) {
		int flag =0;
		Ba.remove(Integer.valueOf(y));
		Bd.add(y);
		for(int element:nodes[y].getIncSubEdges()) {
			if(Fa.contains(element) || Fd.contains(element)) {
				flag = 1;
				break;
			}else if(!Ba.contains(element) || !Bd.contains(element)) {
				Ba.add(element);
			}
		}
		return flag;
	}
	
	public int[] getTopSort() {
		return topSort.returnTopSortToArr();
	}
}
