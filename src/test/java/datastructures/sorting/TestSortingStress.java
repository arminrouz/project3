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
	
	
	@Test(timeout=30*SECOND)
	public void testSortStress() {
		Random rand = new Random();
		IList<Integer> test1 = new DoubleLinkedList<Integer>();
		List<Integer> test2 = new LinkedList<Integer>();
		for (int i = 0; i < 1000; i++) {
			int randNum = rand.nextInt(1000);
			test1.add(randNum);
			test2.add(randNum);
		}
		Collections.sort(test2);
		Searcher.topKSort(1000, test1);
		for (int i = 0; i < 1000; i++) {
			System.out.println("List1:" + test1.get(i) + "List2:" + test2.get(i));
			
			//assertEquals(test1.get(i), test2.get(i));
		}
		
	}
//	public void testSortTiming() {
//		Random rand = new Random();
//		IList<Integer> input = new DoubleLinkedList<Integer>();
//		List<Integer> inputList = new LinkedList<Integer>();
//		
//		//Integer nextRandom;
//		for(int i = 0; i < 100000; i++) {
//			int nextRandom = rand.nextInt(1000);
//			inputList.add(nextRandom);
//			input.add(nextRandom); //create input to sort
//		}
//		
//		IList<Integer> sortedInput = Searcher.topKSort(100000, input); //our sort method
//		
//		Collections.sort(inputList); //sorted LinkedList
//		for(int i = 0; i < 100000; i++) {
//			assertEquals(inputList.get(i), sortedInput.get(i));
//		}
//	}
	
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
