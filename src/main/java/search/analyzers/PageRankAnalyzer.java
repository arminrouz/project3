package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
       //  page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> init = new ArrayDictionary<URI,ISet<URI>>();
        for (Webpage page : webpages) {
        	ISet<URI> uriSet = new ChainedHashSet<URI>();
        	for (URI uri : page.getLinks()) {
        		if (!uri.equals(page.getUri()) && !uriSet.contains(uri)) { //added this
        			for (Webpage page2 : webpages) {
        				if (page2.getUri().equals(uri)) {
                			uriSet.add(uri);
                			break;
        				}
        			}
        		}
        	}
        	init.put(page.getUri(), uriSet);
        }
        return init;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
    	IDictionary<URI, Double> initRank = new ArrayDictionary<URI, Double>();
    	for (KVPair<URI, ISet<URI>> pair : graph) {
    		initRank.put(pair.getKey(), 1.0 / (double) graph.size());
    	}
    	 // Step 2: The update step should go here
    	IDictionary<URI, Double> updated = new ArrayDictionary<URI, Double>();

    	updated = initRank; //changed this 
    	
    	for (int i = 0; i < limit; i++) { //armin 
    		System.out.println(i);
    		IDictionary<URI, Double> intermediate = new ArrayDictionary<URI, Double>();;
    		for (KVPair<URI, ISet<URI>> pair : graph) {
        		intermediate.put(pair.getKey(), 0.0);
    	    }
    		for (KVPair<URI, ISet<URI>> pair : graph) {
    			URI cur = pair.getKey();
    			ISet<URI> theLinks = pair.getValue();
    			double value = updated.get(cur);
    			if (theLinks.isEmpty()) {
    				for (KVPair<URI, Double> pair2 : intermediate) {
    					intermediate.put(pair2.getKey(), pair2.getValue() + 
    							decay * (value / graph.size()));
    				} 
    			} else {
					for (URI theUri : theLinks) {
						if(theLinks.size() > 0) { // added this
							double oldRank = updated.get(cur);
							double newRank = intermediate.get(theUri) + (oldRank * decay / (double) theLinks.size());
							intermediate.put(theUri, newRank);
						}
					}
    			}    			
    		}

    		for (KVPair<URI, Double> finalChecker : intermediate) {
    			Double newVal = finalChecker.getValue() + (1 - decay) / graph.size();
    			intermediate.put(finalChecker.getKey(), newVal);
    			
    		} //return intermediate, else keep going.
    		int counter = 0;
    		for(KVPair<URI, Double> pair : intermediate) {
    			URI uri = pair.getKey();
    			Double val = pair.getValue();
    			if (Math.abs(updated.get(uri) - val) < epsilon) {
    				counter++;
    			}
    			updated.put(uri, val);
    		}
    		if (counter == intermediate.size()) {
    			return intermediate;
    		}
    	}
    return updated;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        // TODO: Add working code here
        return pageRanks.get(pageUri);
    }
}
