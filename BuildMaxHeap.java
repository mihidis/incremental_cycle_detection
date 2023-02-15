
public class BuildMaxHeap {
	private int size;
   
    void heapify(Nodes arr[], int N, int i)
    {
        int largest = i; 
        int l=2*i+1; 
        int r=2*i+2; 
  
        if(l<N && arr[l].getLabel()>arr[largest].getLabel()) {
            largest = l;
        }
       
        if(r<N && arr[r].getLabel()>arr[largest].getLabel()) {
            largest = r;
        }
      
        if(largest != i) {
            Nodes swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, N, largest);
        }
    }
  
   
    void buildHeap(Nodes arr[], int N)
    {
    	this.size = N;
        int startIdx = (N / 2) - 1;
        for(int i = startIdx; i >= 0; i--) {
            heapify(arr, N, i);
        }
    }

    void printHeap(Nodes arr[], int N)
    {
        System.out.println( "Array representation of Heap is:");
  
        for(int i = 0; i < N; ++i)
            System.out.print(arr[i].getCurr() + " ");
  
        System.out.println();
    }
    
    int parent(int pos) {
    	return ((pos-1)/2);
    }
    
    int leftChild(int pos) {
    	return (2*pos)+1;
    }
    
    int rightChild(int pos) {
    	return (2*pos)+2;
    }
    
    boolean hasRightChild(int pos) {
    	if((2*pos)+2>=size) {
    		return false;
    	}
    	return true;
    }
    
    boolean hasLeftChild(int pos) {
    	if((2*pos)+1>=size) {
    		return false;
    	}
    	return true;
    }
    
    boolean isLeaf(int pos,int size) {
    	if(pos>(size/2) && pos<=size) {
    		return true;
    	}
    	return false;
    }
    
    void swap(int fpos,int spos,Nodes[] Q) {
    	Nodes temp = Q[fpos];
    	Q[fpos] = Q[spos];
    	Q[spos] = temp;
    }
    
    public int extractMax(Nodes[] Q) {
    	int toReturn = Q[0].getCurr();
    	if(size >1) {
   
	    	Q[0]=Q[size-1];
	    	size--;
	    	re_heapify(0,Q);
    	}
	    return toReturn;
    }
    
    void re_heapify(int pos,Nodes[] Q) {
    	if(!isLeaf(pos,size)) {   		
    		if(hasRightChild(pos)){
	    		if(Q[pos].getLabel()<Q[leftChild(pos)].getLabel() || Q[pos].getLabel()<Q[rightChild(pos)].getLabel()){
	    			if(Q[leftChild(pos)].getLabel()>Q[rightChild(pos)].getLabel()) {
	    				swap(pos,leftChild(pos),Q);
	    				re_heapify(leftChild(pos),Q);
	    			}else {
	    				swap(pos,rightChild(pos),Q);
	    				re_heapify(rightChild(pos),Q);
	    			}
	    		}
    		}else if(hasLeftChild(pos)) {
    			if(Q[leftChild(pos)].getLabel()>Q[pos].getLabel()) {
    				swap(pos,leftChild(pos),Q);
    			}
    		}
    	}
    }
}
