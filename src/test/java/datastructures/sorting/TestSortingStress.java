package datastructures.sorting;

import misc.BaseTest;
import org.junit.Test;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import static org.junit.Assert.assertTrue;
import java.util.*; 

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
	protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
		return new ArrayHeap<>();
	}
    @Test(timeout=10*SECOND)
    public void testInsertTiming() {
    	Random rand = new Random(); 
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100000; i++) {
        	heap.insert(rand.nextInt(1000));
        }
        
        for (int i = 0; i < 100000; i++) {
        	assertEquals(heap.peekMin(), 0);
        }
    }
    
    @Test(timeout=15*SECOND)
    public void testRemoveMinTiming() {
    	Random rand = new Random();
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	for (int i = 0; i < 100000; i++) {
    		heap.insert(rand.nextInt(1000));
    	}
    	
    	for (int i = 0; i < 100000; i++) {
    		heap.removeMin();
    	}
    }
}
