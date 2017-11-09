package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import java.util.*; 
import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testExtendedSize() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	Random rn = new Random();
    	for (int i = 0; i < 750; i++) {
    		heap.insert(rn.nextInt(2049) + 1);
    	}
    	assertEquals(750, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testMultipleRemoveSize() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	Random rn = new Random();
    	for (int i = 0; i < 850; i++) {
    		heap.insert(rn.nextInt(2049) + 1);
    	}
    	
    	for (int i = 0; i < 10; i++) {
    		heap.removeMin();
    	}
    	assertEquals(840, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testMinPeek() {
    	Random rn = new Random();
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(-1);
    	for (int i = 0; i < 500; i++) {
    		heap.insert(rn.nextInt(2049) + 1);
    	}
    	assertEquals(heap.peekMin(), -1);
    }
    
    @Test(timeout=SECOND)
    public void testMinReturn() {
    	Random rn = new Random();
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(-2);
    	heap.insert(-1);
    	for (int i = 0; i < 500; i++) {
    		heap.insert(rn.nextInt(2049) + 1);
    	}
    	int returnedMin = heap.removeMin();
    	assertEquals(returnedMin, -2);
    	assertEquals(heap.peekMin(), -1); 
    			
    }
    
    @Test(timeout=SECOND)
    public void testInsertBasic() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	heap.insert(1);
    	assertEquals(heap.peekMin(), 1);
    }
    
    @Test(timeout=SECOND)
    public void testInsertWithChildren() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	for(int i = 1; i<= 20; i++) {
    		heap.insert(i);
    	}
    	for(int i = 1; i <= 20; i++) {
    		assertEquals(heap.removeMin(), i);
    	}
    	assertTrue(heap.isEmpty());
    }
}
