package datastructures.concrete;

import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Arrays;

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
    private int height; //keeps track of heap height for array resize.
    private int size; 
    int fullSize; 

    // Feel free to add more fields and constants.

    public ArrayHeap() {
    	height = 1; 
    	heap = makeArrayOfT(5);
    	size = 0;
    	fullSize = 5; 
    }
    /*!
     * !
     * !
     * !
     * WE NEED TO IMPLEMENT FLOYDS METHOD, BUILDING A HEAP TAKES TOO LONG!!!!
     * 
     *
     */

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
    
    public void buildHeap(IList<T> input) {
    	
    	for(int i = 0; i < input.size(); i++) {
    		if(size + 1 > fullSize) {
        		resize();
        	}
    		heap[i] = input.get(i);
    		size++;
    	}
    	size = heap.length;
    	
    	for(int i = size / 2 - 1; i >= 0; i--) {
    		percolateDown(i);
    	}
    }
    
    private void resize() {
    	height++;
    	T[] newHeap = makeArrayOfT(size + (int) Math.pow(4, height));
    	fullSize = size + (int) Math.pow(4, height);
    	for (int i = 0; i < size; i++) {
    		newHeap[i] = heap[i];
    	}
    	heap = newHeap;
    	System.out.println("heap size: " + fullSize);
    }

    @Override
    public T removeMin() {
    	T temp = heap[0];
    	T lastVal = heap[size - 1];
    	heap[0] = lastVal; 
    	//heap[size - 1] = temp;
    	percolateDown(0);
    	size--;
        //percolate down after switching last with first
    	return temp;
    }
    
    private void percolateUp(int index) {
    	if(heap[index].compareTo(heap[(index - 1) / 4]) < 0) {
    		T temp = heap[index];
    		heap[index] = heap[(index - 1) / 4];
    		heap[(index - 1) / 4] = temp;
    		percolateUp((index - 1) / 4);
    	}
    }
    private void percolateDown(int index) {

    	int smallest = index;
    	int child;
    	for(int i = 1; i < 5; i++) {
    		child = 4 * index + i;
    		if(child < size && heap[child].compareTo(heap[smallest]) < 0) {
    			smallest = child;
    		}
    	}
    	if(smallest != index) {
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
    	if(size + 1 > fullSize) {
    		resize();
    	}
    	heap[size] = item;
    	size++; 

    	if(size > 1) {
    		percolateUp(size - 1);
    	}
    }
    
//    public void changeTop(T item) {
//    	heap[0] = item;
//    	percolateDown(0);
//    }

    @Override
    public int size() {
    	return size;
    }
}
