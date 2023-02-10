import java.util.*;


public class main {
	public static void main(String[] args) {
		int N = 5;
		Graph  graph = new Graph(N);
		
		Scanner scan = new Scanner(System.in);
		String temp[];
		
		while(true) {
			System.out.println("Give new edge! ");
			
			temp = scan.nextLine().split(" ");
			
			int[] newEdge = {Integer.parseInt(temp[0]),Integer.parseInt(temp[1])};
			if(isbetween(newEdge,N)) {
				if(graph.addEdge(newEdge)) {
					break;
				}
			}else {
				System.out.println("Vertex given out of bounds!");
			}
		}
		
	}
	
	public static boolean isbetween(int[] newEdge, int N) {
		if (newEdge[0]>=0 && newEdge[0]<N && newEdge[1]>=0 && newEdge[1]<N) {
			return  true;
		}
		return false;
	}
}
