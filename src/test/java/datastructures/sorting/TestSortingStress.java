package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
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
	
	
	@Test(timeout=100*SECOND)
	public void testSortTiming() {
		Random rand = new Random();
		IList<Integer> input = new DoubleLinkedList<Integer>();
		List<Integer> inputList = new LinkedList<Integer>();
		
		long start1 = System.nanoTime();
		Integer nextRandom;
		for(int i = 0; i < 100000; i++) {
			nextRandom = rand.nextInt(1000);
			inputList.add(nextRandom);
			input.add(nextRandom); //create input to sort
		}
		long end1 = System. nanoTime();
		System.out.println("making lists = " + (end1 - start1) / 1000000);		
		
		long start2 = System.nanoTime();
		IList<Integer> sortedInput = Searcher.topKSort(100000, input); //our sort method
		long end2 = System.nanoTime();
		System.out.println("topKSort time = " + (end2 - start2) / 1000000);
		
		Collections.sort(inputList); //sorted LinkedList
		
		for(int i = 0; i < 100000; i++) {
			System.out.println(i);
			assertEquals(sortedInput.get(i), inputList.get(i));
		}
//		for(int i = 0; i < 100000; i++) {
//			sortedInputArray[i] = inputList.get(i); 
//			//copy sorted LinkedList into array so that we can use assertListMatches, 
//			//which takes an array and an IList
//		}
//		assertListMatches(sortedInputArray, sortedInput);
		//assert that newly made array matches the sorted input 
		//(which was sorted using our topK method)
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
