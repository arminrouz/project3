package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
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
//    	IDictionary<URI, Double> zeros = new ArrayDictionary<URI, Double>();

//    	for (KVPair<URI, ISet<URI>> pair : graph) {
//    		zeros.put(pair.getKey(), 0.0);
//    	}
    	updated = initRank; //changed this 
    	
    	for (int i = 0; i < limit; i++) { //armin 
    		System.out.println(i);
    		IDictionary<URI, Double> intermediate = new ArrayDictionary<URI, Double>();;
    		for (KVPair<URI, ISet<URI>> pair : graph) {
        		intermediate.put(pair.getKey(), 0.0);
    	    }
    		for (KVPair<URI, ISet<URI>> pair : graph) {
    			
    			URI cur = pair.getKey();
//        		System.out.println(zeros.get(cur));

    			ISet<URI> theLinks = pair.getValue();
    			double value = updated.get(cur);
    			if (theLinks.isEmpty()) {
    				for (KVPair<URI, Double> pair2 : intermediate) {
    					intermediate.put(pair2.getKey(), pair2.getValue() + 
    							decay * (value / graph.size()));
    				} 
    			} else {
//    				double newRank = 0.0;
					for (URI theUri : theLinks) {
						if(theLinks.size() > 0) { // added this
							double oldRank = updated.get(cur);
							double newRank = intermediate.get(theUri) + (oldRank * decay / (double) theLinks.size());
							intermediate.put(theUri, newRank);
//							double oldVal = oldRank * decay / (double) graph.get(theUri).size();
//							newRank = newRank + oldVal;
						}
					}
					//newRank = newRank + (1 - decay) / graph.size();
//					intermediate.put(cur, intermediate.get(cur) + newRank);
    			}
    
    			//double newRank = initRank.get(link) / 
    			
    		}

    		for (KVPair<URI, Double> finalChecker : intermediate) {
    			Double newVal = finalChecker.getValue() + (1 - decay) / graph.size();
//    			if (Math.abs(updated.get(finalChecker.getKey()) - newVal) < epsilon) {
//    				counter++;
//    			}
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
//    		updated = intermediate;
    	}
    	
    	
//    	IDictionary<URI, Double> intermediate;
//    	for (int i = 0; i < limit; i++) {
//    		intermediate = zeros;
//    		for (KVPair<URI, ISet<URI>> pair : graph) {
//    			URI uri = pair.getKey();
//    			ISet<URI> linksTo = pair.getValue();
//    			if (linksTo.isEmpty()) {
//    				for(KVPair<URI, Double> pair2 : intermediate) {
//    					URI nextUri = pair2.getKey();
//    					Double oldVal = pair2.getValue();
    	
//    					intermediate.put(nextUri, oldVal + (decay * oldVal / (double) graph.size()));
//    				}
//    			}
//    			for (URI link : linksTo) {
//    				double val;
//    				if(i == 0) {
//    					val = initRank.get(link);
//    				} else {
//    					val = updated.get(link);
//    				}
//    				if(graph.get(link).size() > 0) {
//    					intermediate.put(uri, intermediate.get(uri) + (decay * val / (double) graph.get(link).size()));
//    				}
//    			}
//    			for (KVPair<URI, Double> pair3 : intermediate) {
//    				double oldValue = pair3.getValue();
//    				double newVal = oldValue + ((1 - decay) / (double) graph.size());
//    				intermediate.put(pair3.getKey(), newVal);
//    			}
//    		}
//    		boolean check = true;
//    		for (KVPair<URI, Double> finalPair : updated) {
//    			URI uri2 = finalPair.getKey();
//    			if(check == true && Math.abs(updated.get(uri2) - intermediate.get(uri2)) >= epsilon) {
//    				check = false;
//    			}
//    		}
//    		updated = intermediate;
//    		if (check == true) {
//    			return updated;
//    		}
//    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
//    	
//        for (int i = 0; i < limit; i++) {
//        	boolean check = true;
//    		IDictionary<URI, Double> intermediate = new ArrayDictionary<URI, Double>();
//    		
//    		int counter = 0;
//        	for (KVPair<URI, ISet<URI>> pair : graph) { // FOR EACH PAGE (VERTEX) IN THE GRAPH
//        		//int counter = 0;
//        		double newRank = 0;
//        		if(pair.getValue().isEmpty()) {
//        			for(KVPair<URI, Double> pair2 : finalRank) {
//        				URI uri = pair2.getKey();
//        				if(i == 0) {
//        					newRank = initRank.get(uri);
//        					
//        				} else {
//        					newRank = finalRank.get(uri);
//        				}
//        				//System.out.println(newRank);
//        				newRank +=  (decay * newRank) / (double) graph.size();
//    					//intermediate.put(uri, newRank + (decay * newRank) / (double) graph.size());
//
//        			}
//        		}
//        		for (URI uri : pair.getValue()) { // FOR EACH THING THAT LINKS TO IT
//    				double size = (double) graph.get(uri).size(); // NUMBER IT LINKS TO
//            		if(size != 0.0) { // IF NO OUTGOING LINKS
//            		
//	            		if(i == 0) { // IF FIRST ITERATION, USE VALUES IN INITIAL RANK DICT
//	            			newRank += decay * initRank.get(uri) / size;
//	            		} else { // ELSE USE LAST UPDATED VALUE
//	            			newRank += decay * finalRank.get(uri) / size;
//	            		}
//            		}
//    			}
//    			newRank += (1.0 - decay) / (double) graph.size(); // DECAY FACTOR
//        		if(/*check == true &&*/ Math.abs(finalRank.get(pair.getKey()) - newRank) >= epsilon) {
//        			//check = false;
//        			counter++;
//        		}
//        		
//        		//System.out.println(newRank);
//    
//        		intermediate.put(pair.getKey(), newRank);
//        	}
//        	finalRank = intermediate;
//        	if(/*check == true*/ counter == 0) {
//        		return finalRank;
//        	}
        	// ADD A CHECK FOR IF ONE OF THE WEBPAGES DOESNT HAVE OUTGOING LINKS, DIVIDE BY N
        	// INSTEAD OF NUMBER OF OUTGOING LINKS.
        	
        	// HERE ADD A CHECK FOR IF THE DIFFERENCE BETWEEN PREVIOUS RANK AND THE NEW RANK ARE 
        	// GREATER THAN EPSILON
        	
//        	int checker = 0;
//        	boolean check = true;
//        	for (KVPair<URI, ISet<URI>> pair : graph) {
//        		double size = pair.getValue().size();
//        		if (size == 0.0) {
//        			size = (double) graph.size();
//        		}
//        		double oldRank;
//        		double newRank;
//        		if (i == 0) {
//        			oldRank = initRank.get(pair.getKey());
//        		} else {
//        			oldRank = finalRank.get(pair.getKey());
//        		}
//        		newRank = oldRank + decay * (oldRank / size) + ((1.0 - decay) / (double) graph.size());
//        		if (i < 5) {
//            		System.out.println(newRank);
//
//        		}
//        		if (check == true && Math.abs(oldRank - newRank) > epsilon) {
////        			checker++;
//        			check = false;
//        		}
//        		finalRank.put(pair.getKey(), newRank);
//        		
//        	}
        	// Step 3: the convergence step should go here.
            // Return early if we've converged.
//        	if(check == true /*checker == 0*/) {
//        		return finalRank;
//        	} 
//            
//        }
//        return finalRank;
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
