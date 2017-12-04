package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import java.util.NoSuchElementException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int sizeUsed; 
    int fullSize; 

    // Feel free to add more fields and constants.

    public ArrayHeap() {
    	heap = makeArrayOfT(5);
    	sizeUsed = 0;
    	fullSize = 5; 
    }


    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }
    

    private void resize() {
    	T[] newHeap = makeArrayOfT(sizeUsed * 2);
    	fullSize = newHeap.length;
    	for (int i = 0; i < sizeUsed; i++) {
    		newHeap[i] = heap[i];
    	}
    	heap = newHeap;
    }

    @Override
    public T removeMin() {
    	if(sizeUsed == 0) {
    		throw new NoSuchElementException();
    	}
    	T temp = heap[0];
    	T lastVal = heap[sizeUsed - 1];
    	heap[0] = lastVal; 
    	heap[sizeUsed - 1] = null;
    	sizeUsed--;

    	percolateDown(0);
    	return temp;
    }
    
    private void percolateUp(int index) {
    	if (heap[index].compareTo(heap[(index - 1) / 4]) < 0) {
    		T temp = heap[index];
    		heap[index] = heap[(index - 1) / 4];
    		heap[(index - 1) / 4] = temp;
    		percolateUp((index - 1) / 4);
    	}
    }
    
    private void percolateDown(int index) {
    	int smallest = index;
    	int child;
    	for (int i = 1; i < 5; i++) {
    		child = (4 * index) + i;
    		if (child < sizeUsed && heap[child].compareTo(heap[smallest]) < 0) {
    			smallest = child;
    		}
    	}
    	if (smallest != index) {
    		T temp = heap[index];
    		heap[index] = heap[smallest];
    		heap[smallest] = temp;
    		percolateDown(smallest);
    	}
    }

    @Override
    public T peekMin() {
    	return heap[0];
    }

    @Override
    public void insert(T item) {
    	if(item == null) {
    		throw new IllegalArgumentException();
    	}
    	if (sizeUsed + 1 > fullSize) {
    		resize();
    	}
    	heap[sizeUsed] = item;
    	sizeUsed++; 

    	if (sizeUsed > 1) {
    		percolateUp(sizeUsed - 1);
    	}
    }
    
    @Override
    public int size() {
    	return sizeUsed;
    }
}
