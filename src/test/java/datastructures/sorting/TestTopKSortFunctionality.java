package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * See spec for details on what kinds of tests this class should include.
 */
import java.util.*;
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyList() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	try {
    		IList<Integer> top = Searcher.topKSort(1, list);
    		fail("Expected NoSuchElementException");
    	} catch (NoSuchElementException ex){
    		
    	}
    }
    
    @Test(timeout=SECOND)
    public void testListWithSingleElement() {
    	Integer[] arr = {1};
    	IList<Integer> list = new DoubleLinkedList<>();
    	list.add(1);
    	IList<Integer> top = Searcher.topKSort(1, list);
    	assertListMatches(arr, top);
    }
    
    @Test(timeout=SECOND)
    public void testZeroK() {
    	Random rand = new Random();
    	IList<Integer> list = new DoubleLinkedList<>();
    	for (int i = 0; i < 1000; i++) {
    		int val = rand.nextInt(1000);
    		list.add(val);
    	}
    	IList<Integer> top = Searcher.topKSort(0, list);
    	assertEquals(0, top.size());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNegativeK() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	Random rand = new Random(); 
    	for (int i = 0; i < 1000; i++) {
    		int val = rand.nextInt(1000);
    		list.add(val);
    	}
    	IList<Integer> test = Searcher.topKSort(-1, list);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidK() {
    	IList<Integer> list = new DoubleLinkedList<>();
    	Random rand = new Random(); 
    	for (int i = 0; i < 1000; i++) {
    		int val = rand.nextInt(1000);
    		list.add(val);
    	}
    	IList<Integer> test = Searcher.topKSort(list.size() + 1, list);
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
