import java.util.*;


public class main {
	public static void main(String[] args) {
		int N = 10;
		Graph  graph = new Graph(N);
		
		Scanner scan = new Scanner(System.in);
		String temp[];
		
		while(true) {
			
			System.out.println("Give new edge! ");
			try{
				temp = scan.nextLine().split(" ");
			
				int[] newEdge = {Integer.parseInt(temp[0]),Integer.parseInt(temp[1])};
				if(isbetween(newEdge,N)) {
					if(graph.addEdge(newEdge)) {
						break;
					}
					checkTopSort(graph);
				}else {
					System.out.println("Vertex given out of bounds!");
				}
			}
			catch(Exception e){
				System.out.println("Wrong input given");
				System.out.println("You can try again \n \n ");
			}
		}
		
	}
	
	public static boolean isbetween(int[] newEdge, int N) {
		if (newEdge[0]>=0 && newEdge[0]<N && newEdge[1]>=0 && newEdge[1]<N) {
			return  true;
		}
		return false;
	}
	
	public static void checkTopSort(Graph graph) {
		boolean flag = true;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int element:graph.getTopSort()) {
			if(!flag) {
				break;
			}
			list.add(element);
			for(int edge:graph.returnIndexEdges(element)) {
				if(list.contains(edge)) {
					flag=false;
					break;
				}
			}
		}
		if(flag) {
			System.out.println("This topological sorting is correct!\n");
		}else {
			System.out.println("This topological sort is incorect!\n");
		}
		
	}
}
