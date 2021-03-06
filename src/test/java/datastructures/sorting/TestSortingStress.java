package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
	protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
		return new ArrayHeap<>();
	}
	
	
	@Test(timeout=10*SECOND)
	public void testSortTiming() {
		Random rand = new Random();
		IList<Integer> input = new DoubleLinkedList<Integer>();
		List<Integer> inputList = new LinkedList<Integer>();
		
		int nextRandom;
		for (int i = 0; i < 1000000; i++) {
			nextRandom = rand.nextInt(1000);
			inputList.add(nextRandom);
			input.add(nextRandom); //create input to sort
		}
		
		IList<Integer> mySortedK = Searcher.topKSort(100000, input); //our sort method

		Collections.sort(inputList); //sorted LinkedList
		
		List<Integer> sortedKList = new LinkedList<Integer>();
		Iterator<Integer> listIter = inputList.iterator();
		for (int i = 0; i < 900000; i++) {
			listIter.next();
		}
		
		for (int i = 0; i < 100000; i++) {
			sortedKList.add(listIter.next());
		}
		Iterator<Integer> sortedListIter = sortedKList.iterator();
		Iterator<Integer> myListIter = mySortedK.iterator();
		
		for (int i = 0; i < 100000; i++) {
			assertEquals(sortedListIter.next(), myListIter.next());
		}
		
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
    
    protected <T> void assertListMatches(T[] expected, IList<T> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());

        for (int i = 0; i < expected.length; i++) {
            try {
                assertEquals("Item at index " + i + " does not match", expected[i], actual.get(i));
            } catch (Exception ex) {
                String errorMessage = String.format(
                        "Got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        expected[i]);
                throw new AssertionError(errorMessage, ex);
            }
        }
    }
}
