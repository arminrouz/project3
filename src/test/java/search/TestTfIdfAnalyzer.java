package search;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.BaseTest;
import org.junit.Test;
import search.analyzers.TfIdfAnalyzer;
import search.models.Webpage;

import java.net.URI;

public class TestTfIdfAnalyzer extends BaseTest {
    // We say two floating point numbers are equal if they're within
    // this delta apart from each other.
    public static final double DELTA = 0.000001;

    private IList<String> strToIList(String input) {
        IList<String> output = new DoubleLinkedList<>();
        for (String word : input.split(" ")) {
            output.add(word);
        }
        return output;
    }

    private TfIdfAnalyzer makeExampleAnalyzer() {
        Webpage documentA = new Webpage(
                URI.create("http://example.com/fake-page-a.html"),
                new DoubleLinkedList<>(),
                strToIList("the mouse played with the cat"),
                "Document A title",
                "Document A blurb");

        Webpage documentB = new Webpage(
                URI.create("http://example.com/fake-page-b.html"),
                new DoubleLinkedList<>(),
                strToIList("the quick brown fox jumped over the lazy dog"),
                "Document B title",
                "Document B blurb");

        Webpage documentC = new Webpage(
                URI.create("http://example.com/fake-page-c.html"),
                new DoubleLinkedList<>(),
                strToIList("dog 1 and dog 2 ate the hot dog"),
                "Document C title",
                "Document C blurb");

        ISet<Webpage> documents = new ChainedHashSet<>();
        documents.add(documentA);
        documents.add(documentB);
        documents.add(documentC);

        return new TfIdfAnalyzer(documents);
    }

    private void compareVectors(IDictionary<String, Double> expected, IDictionary<String, Double> actual) {
        assertEquals("Document vectors do not have same size", expected.size(), actual.size());
        for (KVPair<String, Double> expectedPair : expected) {
            String key = expectedPair.getKey();
            double expectedWeight = expectedPair.getValue();
            double actualWeight = actual.get(key);

            assertEquals("Word '" + key + "' had differing weights", expectedWeight, actualWeight, DELTA);
        }
    }

    @Test(timeout=SECOND)
    public void testSpecExampleVectorCreation() {
        TfIdfAnalyzer analyzer = this.makeExampleAnalyzer();
        IDictionary<URI, IDictionary<String, Double>> vectors = analyzer.getDocumentTfIdfVectors();

        IDictionary<String, Double> documentAExpected = new ChainedHashDictionary<>();
        documentAExpected.put("the", 0.0);
        documentAExpected.put("mouse", 0.183102);
        documentAExpected.put("played", 0.183102);
        documentAExpected.put("with", 0.183102);
        documentAExpected.put("cat", 0.183102);

        compareVectors(
                documentAExpected,
                vectors.get(URI.create("http://example.com/fake-page-a.html")));


        IDictionary<String, Double> documentBExpected = new ChainedHashDictionary<>();
        documentBExpected.put("the", 0.0);
        documentBExpected.put("quick", 0.122068);
        documentBExpected.put("brown", 0.122068);
        documentBExpected.put("fox", 0.122068);
        documentBExpected.put("jumped", 0.122068);
        documentBExpected.put("over", 0.122068);
        documentBExpected.put("lazy", 0.122068);
        documentBExpected.put("dog", 0.045052);

        compareVectors(
                documentBExpected,
                vectors.get(URI.create("http://example.com/fake-page-b.html")));

        IDictionary<String, Double> documentCExpected = new ChainedHashDictionary<>();
        documentCExpected.put("dog", 0.135155);
        documentCExpected.put("1", 0.122068);
        documentCExpected.put("and", 0.122068);
        documentCExpected.put("2", 0.122068);
        documentCExpected.put("ate", 0.122068);
        documentCExpected.put("the", 0.0);
        documentCExpected.put("hot", 0.122068);

        compareVectors(
                documentCExpected,
                vectors.get(URI.create("http://example.com/fake-page-c.html")));
    }

    @Test(timeout=SECOND)
    public void testSpecExampleAgainstQuery() {
        TfIdfAnalyzer analyzer = this.makeExampleAnalyzer();

        IList<String> query = new DoubleLinkedList<>();
        query.add("the");
        query.add("1");
        query.add("cat");

        assertEquals(
                0.353553,
                analyzer.computeRelevance(query, URI.create("http://example.com/fake-page-a.html")),
                DELTA);
        assertEquals(
                0.0,
                analyzer.computeRelevance(query, URI.create("http://example.com/fake-page-b.html")),
                DELTA);
        assertEquals(
                0.283389,
                analyzer.computeRelevance(query, URI.create("http://example.com/fake-page-c.html")),
                DELTA);
    }
//    
//    private IDictionary<String, Double> computeTfScores(IList<String> words) {
//        IDictionary<String, Double> tfScores = new ArrayDictionary<String, Double>();
//        for(String word : words) {
//        	tfScores.put(word, countWord(word, words) / (double) words.size());
//        }
//        return tfScores;
//    }
//    private Double countWord(String target, IList<String> words) {
//    	Double counter = 0.0;
//    	for(String word : words) {
//    		if(word.equalsIgnoreCase(target)) {
//    			counter++;
//    		}
//    	}
//    	return counter;
//    }
//    
//    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
//    	int numDocs = pages.size();
//    	IDictionary<String, Double> idfScores = new ChainedHashDictionary<String, Double>();
//    	for(Webpage page : pages) {
//    		ISet<String> uniqueWords = new ChainedHashSet<String>();
//    		for(String word : page.getWords()) {
//    			uniqueWords.add(word);
//    		}
//    		for (String word : uniqueWords) {
//    			
//	    		
//    			if(!idfScores.containsKey(word)) {
//    				idfScores.put(word,  0.0);
//    			}
//    			double oldScore = idfScores.get(word);
//    			idfScores.put(word, oldScore + 1.0);
//	    		
//    		}
//    	}
//
//    	for(KVPair<String, Double> pair : idfScores) {
//    		idfScores.put(pair.getKey(), Math.log((double) numDocs / pair.getValue()));
//    	}
//    	
//    	return idfScores;
//    }
//    
//    @Test(timeout=SECOND) 
//    public void testComputeTfScores() {
//    	IDictionary<String, Double> testDict = new ChainedHashDictionary<String, Double>();
//    	IList<String> testList = new DoubleLinkedList<String>();
//    	testList.add("Peter");
//    	testList.add("is");
//    	testList.add("cool");
//    	testDict.put("Peter", 1.0 / 3.0);
//    	testDict.put("is", 1.0 / 3.0);
//    	testDict.put("cool", 1.0 / 3.0);
//    	compareVectors(testDict, computeTfScores(testList));
//    }
//    
//    @Test(timeout=SECOND) 
//    public void testComputeIdfScores() {
//    	ISet<Webpage> testSet = new ChainedHashSet<Webpage>();
//    	Webpage documentA = new Webpage(
//                URI.create("http://example.com/fake-page-a.html"),
//                new DoubleLinkedList<>(),
//                strToIList("the mouse played with the cat"),
//                "Document A title",
//                "Document A blurb");
//    	testSet.add(documentA);
//    	IDictionary<String, Double> testDict = new ChainedHashDictionary<String, Double>();
//    	testDict.put("the", Math.log(1.0 / 1.0));
//    	testDict.put("mouse", Math.log(1.0 / 1.0));
//    	testDict.put("played", Math.log(1.0 / 1.0));
//    	testDict.put("with", Math.log(1.0 / 1.0));
//    	testDict.put("the", Math.log(1.0 / 1.0));
//    	testDict.put("cat", Math.log(1.0 / 1.0));
//    	compareVectors(testDict, computeIdfScores(testSet));
//    }
    
}
