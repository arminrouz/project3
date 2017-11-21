package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
	public static final Timer TIMER = new Timer();
    private IDictionary<String, Double> idfScores;
    private IDictionary<URI, Double> normDocVector;
    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normDocVector = new ChainedHashDictionary<URI, Double>();
        for(KVPair<URI, IDictionary<String, Double>> pair : documentTfIdfVectors) {
        	normDocVector.put(pair.getKey(), norm(pair.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * This method should return a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
    	System.out.println("start compute IDF");
    	TIMER.start();
    	int numDocs = pages.size();
    	IDictionary<String, Double> idfScores = new ChainedHashDictionary<String, Double>();
    	for(Webpage page : pages) {
    		ISet<String> uniqueWords = new ChainedHashSet<String>();
    		for(String word : page.getWords()) {
    			uniqueWords.add(word);
    		}
    		for (String word : uniqueWords) {
    			
	    		
    			if(!idfScores.containsKey(word)) {
    				idfScores.put(word,  0.0);
    			}
    			double oldScore = idfScores.get(word);
    			idfScores.put(word, oldScore + 1.0);
	    		
    		}
    	}

    	for(KVPair<String, Double> pair : idfScores) {
    		idfScores.put(pair.getKey(), Math.log((double) numDocs / pair.getValue()));
    	}
    	System.out.println("compute IDF time = " + TIMER.stop());

    	return idfScores;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<String, Double>();
        for(String word : words) {
        	tfScores.put(word, countWord(word, words) / (double) words.size());
        }
        return tfScores;
    }

    private Double countWord(String target, IList<String> words) {
    	Double counter = 0.0;
    	for(String word : words) {
    		if(word.equalsIgnoreCase(target)) {
    			counter++;
    		}
    	}
    	return counter;
    }
    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
    	IDictionary<URI, IDictionary<String, Double>> finalVector = new ChainedHashDictionary<URI, IDictionary<String, Double>>();
    	for (Webpage page : pages) {
    		IList<String> list = page.getWords();
    		IDictionary<String, Double> scores = computeTfScores(list);
    		for(KVPair<String, Double> pair : scores) {
    			String key = pair.getKey();
        		scores.put(key, pair.getValue() * idfScores.get(key));
        	}
    		finalVector.put(page.getUri(), scores);
    		
    	}
        return finalVector;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    
    private Double norm(IDictionary<String, Double> vector) {
    	double output = 0.0;
    	for(KVPair<String, Double> pair : vector) {
    		double val = pair.getValue();
    		output += val * val;
    	}
    	return Math.sqrt(output);
    }
    
    public Double computeRelevance(IList<String> query, URI pageUri) {
    	System.out.println("start compute relevance");
    	TIMER.start();
    	IDictionary<String, Double> docVector = documentTfIdfVectors.get(pageUri);
    	IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();
    	IDictionary<String, Double> queryTfScores = computeTfScores(query);
    	
    	for(KVPair<String, Double> pair : queryTfScores) {
			String key = pair.getKey();
    		queryVector.put(key, pair.getValue() * idfScores.get(key));
    	}
    	
    	double numerator = 0.0;
		for(KVPair<String, Double> pair : queryVector) {
			String key = pair.getKey();
			if(docVector.containsKey(key)) {
				numerator += (double)queryVector.get(key) * (double)docVector.get(key);
			}
		}
    	double denominator = normDocVector.get(pageUri) * norm(queryVector);
    	System.out.println("compute relevance time = " + TIMER.stop());
    	if(denominator != 0.0) {
    		
    		return (double) numerator / denominator;	
    	} else {
    		return 0.0;
    	}
    	
        // TODO: Replace this with actual, working code.

        // TODO: The pseudocode we gave you is not very efficient. When implementing,
        // this smethod, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
    }
    
    public static class Timer {
    	private long time;
    	
    	public void start() {
    		time = System.currentTimeMillis();
    	}
    	
    	public long stop() {
    		return System.currentTimeMillis() - time;
    	}
    }
}
