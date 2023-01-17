import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Graph {
	private int N;
	private Nodes nodes[];
	private TopSort topSort;
	private Subgraph subgraph[][];
	private ArrayList<Integer> Fa;
	private ArrayList<Integer> Fd;
	private ArrayList<Integer> Ba;
	private ArrayList<Integer> Bd;
	//private Edges edges = new Edges();
	
	Graph(int N){
		this.N=N;
		this.nodes = new Nodes[N];
		
		for(int i=0;i<N;i++) {
			nodes[i] = new Nodes(i);
		}
		topSort = new TopSort(this,nodes);
		topSort.printTopSort();
		createSubgraphs();
	}
	
	int getN() {
		return N;
	}
	
	ArrayList returnIndexEdges(int i) {
		return nodes[i].indexEdges();
	}
	
	void createSubgraphs(){
		subgraph = new Subgraph[N][N];
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				subgraph[i][j] = new Subgraph(i,j);
			}
			subgraph[0][0].addNode(nodes[i]);
		}
	}
	
	boolean addEdge(int[] newEdge) {
		if(nodes[newEdge[0]].checkEdgeExists(newEdge[1])) {
			System.out.println("Edge already exists in graph! ");
		}else {
			
			ArrayList<Nodes> Xup = new ArrayList<Nodes>();
			ArrayList<Nodes> Xdown = new ArrayList<Nodes>();
			
			//boolean family = nodes[newEdge[0]].checkRelation(newEdge[1]);
			nodes[newEdge[0]].addEdge(newEdge[1]);
			nodes[newEdge[1]].addIncomingEdge(newEdge[0]);
			/*
			if(!family) {
				updateSubgraphs(newEdge[0],newEdge[1],Xup,Xdown);
			}*/
			updateSubgraphs(newEdge[0],newEdge[1],Xup,Xdown);
			
			Move_up(Xup);
			Move_down(Xdown);
			//topSort = new TopSort(this, nodes);
			topSort.printTopSort();
			if(search(newEdge[0],newEdge[1])) {
				System.out.println("Edge insertion creates cycle!");
				for(Nodes element: nodes) {
					System.out.println(element.getLabel());
				}
				return true;
			}else {
				System.out.println("Edge insertions doesnt create cycle!");
				update_forward(newEdge[1]);
			}
			topSort.printTopSort();
			
			//System.out.println("nodes ancestors are : "+nodes[newEdge[0]].getAncestor());
		}
		return false;
	}
	
	void update_forward(int y){
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
	
	void updateSubgraphs(int x,int y,ArrayList<Nodes> Xup,ArrayList<Nodes> Xdown) {
		ArrayList<Integer> alteredNodes = new ArrayList<Integer>();
		alteredNodes.addAll(updateAncestors(x,y));
		alteredNodes.addAll(updateDescentants(x,y));
		Set<Integer> set = new HashSet<>(alteredNodes);
		alteredNodes.clear();
		alteredNodes.addAll(set);
		
		//System.out.println(alteredNodes);
		for(int element: alteredNodes) {
			//System.out.println("nodes "+element+" desc are "+nodes[element].getDescentant());
			//System.out.println("nodes "+element+" asc are "+nodes[element].getAncestor());
			int i=nodes[element].getCurrSubgraph()[0];
			int j=nodes[element].getCurrSubgraph()[1];
			//int temp1=i;
			//int temp2=j;
			subgraph[i][j].deleteNode(nodes[element]);
			int i_new=nodes[element].getAncestor().size()-1;
			int j_new=nodes[element].getDescentant().size()-1;
			//System.out.println(" "+element);
			subgraph[i_new][j_new].addNode(nodes[element]);		
			
			
			if(i<i_new || (i==i_new && j>j_new)) {
				Xup.add(nodes[element]);
			}else {
				Xdown.add(nodes[element]);
			}
			/*
			subgraph[i][j].printSubNodes();
			System.out.println("");
			subgraph[temp1][temp2].printSubNodes();
			System.out.println("\n");
			*/
		}
		for(int element: alteredNodes) {
			//nodes[element].changeSubEdges(alteredNodes);
			int i=nodes[element].getCurrSubgraph()[0];
			int j=nodes[element].getCurrSubgraph()[1];
			nodes[element].changeSubEdges(subgraph[i][j].getNodesToNum());
			nodes[element].printSubEdges();
		}	
		for(Nodes node: nodes) {
			node.printNodesInfo();
		}
	}
	
	ArrayList updateAncestors(int x,int y) {
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
	
	ArrayList updateDescentants(int x, int y) {
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
		//System.out.println(alteredNodes);
		return alteredNodes;
	}
	
	void Move_up(ArrayList<Nodes> Xup){
		ArrayList<Nodes> Q = new ArrayList<Nodes>(Xup);
		for(Nodes element: Q) {
			System.out.print("Move Up: "+element.getCurr()+ " ");
		}
		System.out.println();
		topSort.moveDownChange(Q);
	}
	
	void Move_down(ArrayList<Nodes> Xdown) {
		ArrayList<Nodes> Q = new ArrayList<Nodes>(Xdown);
		for(Nodes element: Q) {
			System.out.print("Move down: "+element.getCurr()+ " ");
		}
		System.out.println();
		topSort.moveUpChange(Q);
	}
	
	void printGraphInfo(){
		System.out.println("Graph has "+N+" nodes.");
		for(Nodes node: nodes) {
			System.out.println("Node "+node.getCurr()+" has "+node.getAncestor().size()+" ancestors.");
			System.out.println("Also has "+node.getDescentant().size()+" descnentants.");
			System.out.println("Belongs to Subgraph "+node.getCurrSubgraph());
		}
	}
	
	boolean search(int x,int y) {
		Fa= new ArrayList<Integer>();
		Fd= new ArrayList<Integer>();
		Ba= new ArrayList<Integer>();
		Bd= new ArrayList<Integer>();
		Fa.add(x);
		Ba.add(y);
		int flag=0;
		if(nodes[y].getCurrSubgraph()[0]!=nodes[x].getCurrSubgraph()[0] || nodes[y].getCurrSubgraph()[1]!=nodes[x].getCurrSubgraph()[1]) {
			System.out.println("Vx =Vx' comparison ");
			flag=1;
			
		}
		
		if(flag==0) {
			int temp;
			while(Fa.size()>0 && Ba.size()>0) {
				temp = topSort.findMin(Fa);
				if(topSort.findMin(Bd)!=-1 && topSort.compare(x, topSort.findMin(Bd))) {
					System.out.println("kx < ky comparison");
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
}
