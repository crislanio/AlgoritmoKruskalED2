package com.utd.Project9Basic;

public class mxm135730OperationFunction {

	public mxm135730Graph root;
	public mxm135730Node rootNode;
	
	/**
	 * Default Constructor
	 */
	public mxm135730OperationFunction() {
		root = new mxm135730Graph();
		rootNode = null;
	}

	/**
	 * Parse the first line of the input
	 * @param line
	 */
	public void parseFirstLineInput(String line) {
		String splitStr[] = line.split("\\s+");
		root.noOfNodes = Integer.parseInt(splitStr[0]);
		return;
	}

	/**
	 * Parse the remaining lines of the input
	 * @param str
	 */
	public void addToGraph(String str) {
		String splitStr[] = str.trim().split("\\s+");

		mxm135730Node a, b;
		Integer id1 = Integer.parseInt(splitStr[0]);
		Integer id2 = Integer.parseInt(splitStr[1]);
		if(root.nodesHashMap.containsKey(id1))
			a = root.nodesHashMap.get(id1);
		else
			a = new mxm135730Node(id1);
		if(root.nodesHashMap.containsKey(id2))
			b = root.nodesHashMap.get(id2);
		else
			b = new mxm135730Node(id2);
		
		if(rootNode == null)
			rootNode = a;
		
		root.addNodeAndEdge(a,b, Integer.parseInt(splitStr[2]));		// add Node and Edge
	}

	/**
	 * Run the algorithm Maximum Cardinality Matching in Bipartite Graph
	 */
	public void MaximumMatchingInBipartiteGraph() {

		/*
		 * 1) Check if the graph is bipartite or not. If not, then return
		 * 2) First do "maximal matching"
		 * 3) Then do "maximum matching" 
		 */

		long start = System.currentTimeMillis();
		if(!root.isBipartite())	{	// if not bipartite, then return as not bipartite
			System.out.println("G is not bipartite");
			return;
		}
		
		root.maximalCardinalityMatching();
		root.maximumCardinalityMatching();
		long end = System.currentTimeMillis();
		
		System.out.println(root.matchingSize + "  " + (end-start));
		if(root.noOfNodes <= 100)
			root.PrintPairs();
		
	}

}
