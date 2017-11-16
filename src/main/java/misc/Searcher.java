package misc;

import java.util.NoSuchElementException;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.exceptions.NotYetImplementedException;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        
    	IList<T> topK = new DoubleLinkedList<T>();
    	if (input.isEmpty()) {
    		throw new NoSuchElementException();
    	}
    	
    	if (k == 0) {
    		return topK;
    	}
    	
    	if (k < 0 || k > input.size()) {
    		throw new IllegalArgumentException();
    	}
    	
    	ArrayHeap<T> heap = new ArrayHeap<T>();
		int counter = 0; 
		for (T value : input) {
			if (counter < k) {
				heap.insert(value);
			} else {
				if (value.compareTo(heap.peekMin()) > 0) {
	    			heap.removeMin();
	    			heap.insert(value);
	    		}
			}
			counter++;
		}
  
    	for (int i = 0; i < k; i++) {
    		topK.add(heap.removeMin());
    	}
    	return topK;
    }
}
