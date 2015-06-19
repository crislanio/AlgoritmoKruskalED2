package br.ufc.ED2.kruskal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Graph {

	public Integer noOfNodes;
	public Integer noOfEdges;
	public Map<Integer, Node> nodesHashMap;
	public List<Node> nodesList;
	public List<Edge> edgesList;

	public List<Edge> mstTreeEdgesList;

	public Long matchingSize;

	public List<Edge> zeroGraphEdges;
	public List<Node> zeroGraphNodes;

	public List<Edge> blossomInsideEdges;

	public Graph() {
		nodesList = new LinkedList<Node>();
		edgesList = new LinkedList<Edge>();
		nodesHashMap = new HashMap<>();
		noOfNodes = 0;
		noOfEdges = 0;
		mstTreeEdgesList = new LinkedList<Edge>();
		matchingSize = 0L;
		zeroGraphEdges = new LinkedList<Edge>();
		zeroGraphNodes = new LinkedList<Node>();
		blossomInsideEdges = new LinkedList<Edge>();
	}

	/**
	 * add the nodes and edge to the graph
	 * @param node1
	 * @param node2
	 * @param weight
	 */
	public void addNodeAndEdge(Node node1, Node node2, Integer weight) {

		if(nodesHashMap.containsKey(node1.id)) {
			node1 = nodesHashMap.get(node1.id);
		} else {
			nodesList.add(node1);
			nodesHashMap.put(node1.id, node1);
		}

		if(nodesHashMap.containsKey(node2.id)) {
			node2 = nodesHashMap.get(node2.id);
		} else {
			nodesList.add(node2);
			nodesHashMap.put(node2.id, node2);
		}

		Edge edge = new Edge(node1, node2, weight);
		edgesList.add(edge);		// add the edge to the list
		node1.addAdjNode(edge);		// add the adjacency list matrix for node1
		node2.addAdjNode(edge);		// add the adjacency list matrix for node2
	}

	/**
	 * Provide a feasible labeling initially
	 */
	public void FeasibleLabelling() {
		for(Node node : nodesList)
			node.L = 0.0;
		double max;
		for(int i=1;i<=2;i++) {
			for(Node node : nodesList) {
				max = 0;
				for(Edge edge : node.adjListEdges) {
					if(max <= ((double) edge.weight - edge.getOtherEnd(node).L))
						max = (double) edge.weight - edge.getOtherEnd(node).L;
				}

				node.L = max;
			}
		}
		PrintClass.PrintLabelling(this);
	}

	public void ComputeZeroGraph(int countNo) {	// has to be computed on originalA and originalB in each node

		/**
		 * 1) Keep the zero graph edges between nodes inside the same blossom for blossom expansion
		 * 2) Clear ZGNodes and ZGEdges list
		 * 3) Recompute ZG again for edges between nodes with blossom parent as itself
		 */
		for(Edge edge : zeroGraphEdges)		// Add edges between nodes inside the blossom to the permanent list
			if(sameBlossom(edge) && !blossomInsideEdges.contains(edge))	// Inefficient I guess
				blossomInsideEdges.add(edge);

		zeroGraphEdges.clear();
		zeroGraphNodes.clear();
		for(Edge edge : edgesList) {
			if(!sameBlossom(edge) && (edge.originalA.L + edge.originalB.L) == (double) edge.weight )
			{
				zeroGraphEdges.add(edge);

				edge.originalA.zeroAdjEdges.add(edge);	// update zeroAdjEdges list in each end of the node
				edge.originalB.zeroAdjEdges.add(edge);

				if(!edge.a.equals(edge.originalA))	// update zeroAdjEdges list in blossom node also
					edge.a.zeroAdjEdges.add(edge);
				if(!edge.b.equals(edge.originalB))
					edge.b.zeroAdjEdges.add(edge);

				if(!zeroGraphNodes.contains(edge.a))	// add blossom nodes in the zeroGraphNodes
					zeroGraphNodes.add(edge.a);
				if(!zeroGraphNodes.contains(edge.b))
					zeroGraphNodes.add(edge.b);
			}
		}

		PrintClass.PrintZeroGraph(this);
	}

	/**
	 * if 2 nodes are in the same blossom
	 * @param edge
	 * @return
	 */
	private boolean sameBlossom(Edge edge) {
		if(edge.a.blossomParent.equals(edge.b.blossomParent))
			return true;
		return false;
	}

	/**
	 * Get the free node
	 * @return
	 */
	private Node getFreeNode() {
		// First return the free node in the zeroGraphNodes
		List<Node> freeNodeList = new LinkedList<Node>();
		for(Node node : zeroGraphNodes) 				// blossom nodes are also added to zeroGraphNodes
			if(node.mate == null && node.blossomParent.equals(node) && node.L > 0)					// nodes inside the blossom should not be included
				return node;
		for(Node node : nodesList)
			if(node.mate == null && node.blossomParent.equals(node) && node.L > 0)						// nodes inside the blossom should not be included; inefficient
				return node;
		return null;
	}

	/**
	 * Greedy approach for maximal matching
	 * @param countNo
	 */
	private void MaximalMatching(int countNo) {

		/**
		 * 1) Mating need not be done for nodes inside the blossom
		 * 2) Initialize |M| = 0;
		 * 3) ZeroGraphEdges never contain edges inside the same blossom. Still we are checking again.
		 */
		matchingSize = 0L;
		for(Node node : nodesList)
			node.mate = null;
		for(Edge edge : zeroGraphEdges) {
			if(!sameBlossom(edge) && (edge.a.mate == null) && (edge.b.mate == null)) {		// mating is not done on nodes that are inside the blossom
				edge.a.mate = edge.b;
				edge.b.mate = edge.a;
				++matchingSize;
			}
		}

		PrintClass.PrintMaximalMatching(this);

	}

	private void ReduceLabels(Node freeNode, double minSlack) {
		/**
		 * 1) If real outer node, -Delta
		 * 2) If blossom outer node, +2*Delta and all real nodes inside the blossom by -Delta
		 * 3) If real inner node, +Delta
		 * 4) If blossom inner node, -2*Delta and all real nodes inside the blossom by +Delta
		 */
		/*System.out.println("\n\n\n---------- Updating Labels (before) ------------");
		while(node1 != null) {
			System.out.println(node1);
			node1 = node1.parentAugPath;
		}*/

		Queue<Node> q = new LinkedList<Node>();
		Queue<Node> qTree = new LinkedList<Node>();
		q.add(freeNode);
		qTree.add(freeNode);
		while(!q.isEmpty()) {
			Node u = q.poll();
			for(Node node : u.childAugPath) {
				q.add(node);
				qTree.add(node);
			}
		}

		while(!qTree.isEmpty()) {
			Node node = qTree.poll();
			if(node.outerNode && node.blossomChilds.size() == 0)	// Point 1
				node.L -= minSlack;
			else if(node.outerNode && node.blossomChilds.size() > 0){
				node.L += 2*minSlack;
				for(Node nodeChild : node.blossomChilds)
					if(nodeChild.blossomChilds.size() == 0)
						nodeChild.L -= minSlack;
			}
			else if(!node.outerNode && node.blossomChilds.size() == 0)
				node.L += minSlack;
			else if(!node.outerNode && node.blossomChilds.size() > 0) {
				node.L -= 2*minSlack;
				for(Node nodeChild : node.blossomChilds)
					if(nodeChild.blossomChilds.size() == 0)
						nodeChild.L += minSlack;
			}
		}
		/*System.out.println("\n\n\n---------- Updating Labels (after) ------------");
		node1 = lastNodeInHT;
		while(node1 != null) {
			System.out.println(node1);
			node1 = node1.parentAugPath;
		}*/
		PrintClass.PrintLabelling(this);
	}

	private double CalculateMinSlacks(Node freeNode) {
		/**
		 * 1) Edge between outer node and node outside the tree
		 * 2) Real Edge between 2 outer nodes in the same tree
		 * 3) Labels of real outer nodes (if blossom is outer node, then consider all the real nodes inside the blossom)
		 * 4) Half the labels of inner blossoms
		 */

		Node v, head1 = freeNode;
		double minSlack = Double.MAX_VALUE, slack = 0.0;

		Queue<Node> q = new LinkedList<Node>();
		Queue<Node> qTree = new LinkedList<Node>();
		q.add(freeNode);
		while(!q.isEmpty()) {
			Node u = q.poll();
			u.seen = false;
			for(Node node : u.childAugPath) {
				q.add(node);
				qTree.add(node);
			}
		}

		q.clear();
		q.add(freeNode);
		while(!q.isEmpty()) {
			Node u = q.poll();
			for(Node tempNode: u.childAugPath)			// add all the childs
				q.add(tempNode);
			if(u.outerNode) {
				if(u.isRealOuterNode() && u.L <= minSlack)						// Point 3
				{
					System.out.println("Slack 3: " + u.L);
					minSlack = u.L;
				}

				for(Edge edge : u.adjListEdges) {
					v = edge.getOtherEnd(u);
					if(v.seen)
						continue;
					if(!isNodeInTree(qTree,v)) {	// Point 1: edge to node outside the tree; Inefficient
						slack = (edge.originalA.L+edge.originalB.L-(double)edge.weight);
						System.out.println("Slack1 = " + slack + " for Edge : " + edge);
						if(slack <= minSlack)
							minSlack = slack;
					}
					else if(v.outerNode && isNodeInTree(qTree,v) && !edge.isZeroEdge()) {	//Point 2;
						slack = (edge.originalA.L + edge.originalB.L - (double) edge.weight)/2;
						System.out.println("Slack2 = " + slack + " for Edge : " + edge);
						if(slack <= minSlack)
							minSlack = slack;
					}
				}
			}
			else {					// Point 4 : if inner node and it is blossom node, then
				if(!u.isRealOuterNode() && u.L <= minSlack)
					minSlack = u.L;
			}
		}

		if(minSlack < 0)
			System.exit(1);
		return minSlack;
	}

	private boolean isNodeInTree(Queue<Node> qTree,Node v) {
		if(qTree.contains(v))
			return true;
		return false;
	}

	/**
	 * Process the augmenting path to increase the matching
	 * @param v
	 */
	private void ProcessAugPath(Node v) {

		Node p,x,nmx,y;
		p = v.parentAugPath;
		x = p.parentAugPath;
		p.mate = v;
		v.mate = p;

		while(x != null) {
			nmx = x.parentAugPath;
			y = nmx.parentAugPath;
			x.mate = nmx;
			nmx.mate = x;
			x = y;
		}
		++matchingSize;

	}


	public boolean MaximumMatching(int countNo) {			// Returns true if matching found

		/**
		 * 1) Find Maximal Matching
		 * 2) Find a free node
		 * 3) If no free node found, then exit the algorithm saying that perfect matching is found
		 * 4) Then construct Aug path.
		 * 5) If alternating path is found, then increase the |M| and return
		 * 6) If Hungarian Tree is found, then find the slack, change labels and return
		 * 		Note: |M| will not be increased in (6)
		 * 7) Then compute zero graph and run this function again
		 */
		MaximalMatching(countNo);
		Node freeNode = getFreeNode();
		if(freeNode == null)					// Perfect matching found
			return true;
		System.out.println("\n\nFree Node : "+ freeNode);

		/*
		 * Construct Augmenting Path
		 */

		Queue<Node> q = new LinkedList<Node>();
		for(Node node : nodesList) {
			node.seen = false;
			node.parentAugPath = null;
			node.childAugPath.clear();
		}
		freeNode.seen = true;
		freeNode.outerNode = true;							// inefficient
		q.add(freeNode);

		Node u = null,v,x;
		boolean flag = false;

		while(!q.isEmpty()) {
			u = q.poll();
			for(Edge edge : u.adjListEdges) {
				if(edge.isZeroEdge()) {					// if that edge in zero graph
					v = edge.getOtherEnd(u);
					if(!v.isRealOuterNode() && v.L == 0) {			// if blossom node as inner node and if its label is 0, then expand it
						System.out.println("\n\n\n Expanding inner Blossom with label = 0 for node " + v);
						ExpandBlossom(v);
						MaximumMatching(countNo);
					}
					if(!v.seen) {
						v.seen = true;
						v.parentAugPath = u;
						u.childAugPath.add(v);
						v.outerNode = false;
						if(v.mate == null) {				// free inner node is found. increase |M| by 1
							ProcessAugPath(v);
							if(getFreeNode() == null)		// when matching increases by 1, there is a chance of getting perfect matching
								return true;
							flag = true;
							break;
						}
						else {								// grow the tree
							x = v.mate;
							x.seen = true;
							x.parentAugPath = v;
							v.childAugPath.add(x);
							x.outerNode=true;
							q.add(x);
						}
					}
					else {
						//						if(v.outerNode && !isInSameTree(u,v)) {	// u and v are in different tree and AugPath found
						//							/*
						//							 *  v is an outernode in different tree
						//							 *  The reverse the parent pointers in the other tree
						//							 *  Aug Path is from Path from u to its root + edge(u,v) + Path from v'root to v
						//							 */
						//							System.out.println(" @@@@@@@ LCA in the same tree @@@@@@@");
						//							Node parentV = getParent(v);
						//							reverseParentPointers(v).parent = u;
						//							ProcessAugPath(parentV);
						//							if(getFirstFreeNode().size() == 0)		// when matching increases by 1, there is a chance of getting perfect matching
						//								return true;
						//
						//							try {
						//								Thread.sleep(4000);
						//							} catch (InterruptedException e) {
						//								e.printStackTrace();
						//							}
						//							flag = true;
						//							break;
						//						}
						if(v.outerNode) {						// Blossom detected
							System.out.println("Blossom detected for edge " + edge);
							Node newBlossomNode = BlossomDetectedFunction(u,v);
							q.remove(v);						// remove v as it will already be added in the queue
							u = newBlossomNode;
						}
					}

				}	// end of if
			} // end of for
			if(flag)
				break;
		}	// end of while

		//		PrintClass.PrintAugmentingPath(u);
		PrintClass.PrintChildAugPath(getParent(u));

		if(!flag && u.outerNode) { 						// if u is outer node, then Hungarian Tree is formed
			/**
			 * 1) Calculate All Slacks and fin Min of it
			 * 2) Update labels
			 */

			double minSlack = CalculateMinSlacks(getParent(u));
			System.out.println("\n\nMinSlack = "+ minSlack);
			ReduceLabels(getParent(u), minSlack);
		}

		return false;
	}

	/**
	 * Reverse the parent pointers to handle the augmenting path between 2 different trees
	 * @param v
	 * @return
	 */
	private Node reverseParentPointers(Node v) {
		if(v.parentAugPath != null)
			reverseParentPointers(v.parentAugPath).parentAugPath = v;
		return v;
	}

	private Node getParent(Node v) {
		while(v.parentAugPath != null)
			v = v.parentAugPath;
		return v;
	}

	private boolean isInSameTree(Node u, Node v) {

		System.out.println("Is in same tree :" + u + v);
		List<Node> tempList1 = new LinkedList<Node>();
		while(u != null) {
			tempList1.add(u);
			u =u.parentAugPath;
		}
		while(v!=null){
			if(tempList1.contains(v))
				return true;
			v=v.parentAugPath;
		}
		return false;
	}

	private Node BlossomDetectedFunction(Node u, Node v) {

		/**
		 * 1) Find LCA of u and v by storing all the nodes in the path from u to LCA and from v to LCA in a tempList
		 * 2) This list has to be used later
		 * 3) Construct the blossom node and update all the edges
		 */

		Node LCA = null, newBlossomNode;
		List<Node> blossomNodesList = FindLCA(u,v);
		LCA = blossomNodesList.remove(0);
		System.out.println("\n\nBlossom Details: ");
		System.out.println("Blossom Nodes List = " + blossomNodesList);
		System.out.println("LCA = " + LCA);
		newBlossomNode = ConstructBlossomNode(blossomNodesList, LCA);
		return newBlossomNode;
	}

	private Node ConstructBlossomNode(List<Node> blossomNodesList, Node LCA) {
		++noOfNodes;
		Node newBlossomNode = new Node(noOfNodes);
		nodesList.add(newBlossomNode);
		nodesHashMap.put(newBlossomNode.id, newBlossomNode);
		zeroGraphNodes.add(newBlossomNode);

		newBlossomNode.mate = LCA.mate;					// always I guess
		if(LCA.mate != null)					// if blossom is first node in the tree
			LCA.mate.mate = newBlossomNode;
		newBlossomNode.parentAugPath = LCA.parentAugPath;
		if(LCA.parentAugPath != null) {
			LCA.parentAugPath.childAugPath.add(newBlossomNode);
			LCA.parentAugPath.childAugPath.remove(LCA);
		}
		newBlossomNode.outerNode = true;					// when a blossom is created, it is always an outer node
		for(Node node : blossomNodesList) {
			newBlossomNode.blossomChilds.add(node);
			node.seen = false;
			node.mate = null;
			node.blossomParent = newBlossomNode;
			// update edges
			for(Edge edge : node.adjListEdges) {
				if(!blossomNodesList.contains(edge.getOtherEnd(node))) { 		// edges from that node to outside the blossom
					newBlossomNode.adjListEdges.add(edge);
					edge.UpdateEdge(newBlossomNode, node);						// update the edge
					if(edge.originalA.L + edge.originalB.L == (double) edge.weight)		// if that edge is zero graph edge
						newBlossomNode.zeroAdjEdges.add(edge);
				}
			}
		}

		PrintClass.PrintCompleteNodeDetails(newBlossomNode);
		return newBlossomNode;
	}

	private List<Node> FindLCA(Node u, Node v) {

		//		PrintClass.PrintAugmentingPath(u);
		//		PrintClass.PrintAugmentingPath(v);
		List<Node> blossomNodesList = new LinkedList<Node>();		// Use hasing instead; inefficient
		Node LCA = null;
		while(u != null) {
			blossomNodesList.add(u);
			u = u.parentAugPath;
		}
		while(v != null) {
			if(blossomNodesList.contains(v)) {
				LCA = v;
				break;
			}
			blossomNodesList.add(v);
			v = v.parentAugPath;
		}
		Node tempLCA = LCA.parentAugPath;
		while(tempLCA != null) {
			blossomNodesList.remove(tempLCA);
			tempLCA = tempLCA.parentAugPath;
		}

		blossomNodesList.add(0, LCA);		// add the LCA as first of the list and remove it later
		return blossomNodesList;
	}

	public Node getFirstBlossomNodeInTopMostLevel() {

		if(nodesHashMap.get(noOfNodes).blossomChilds.size() == 0)		// no further blossoms found
			return null;
		if(!nodesHashMap.get(noOfNodes).blossomParent.equals(nodesHashMap.get(noOfNodes)))
			System.out.println("\n\n%%%%%%%%%%%%%%%%%% getFirstBlossomNodeInTopMostLevel : SHOULD NOT BE EXECUTED %%%%%%%%%%%%%%%%%%%%%%");
		return nodesHashMap.get(noOfNodes);
	}

	public void ExpandBlossom(Node blossomNodeToExpand) {


		// Cross Check
		for(Node node : blossomNodeToExpand.blossomChilds)
			if(node.mate != null || node.seen)
				System.out.println("\n\n%%%%%%%%%%%%%%%%%% ExpandBlossom : SHOULD NOT BE EXECUTED %%%%%%%%%%%%%%%%%%%%%%");

		/**
		 * 1) First find the node inside the blossom which is mating with the outside node
		 * 2) There cannot be 2 such nodes
		 */

		Node blossomNodeMatingOutside = getBlossomNodeMatingOutside(blossomNodeToExpand);
		System.out.println("Blossom node mating outside is " + blossomNodeMatingOutside);

		blossomNodeMatingOutside.mate = blossomNodeToExpand.mate;
		blossomNodeToExpand.mate.mate = blossomNodeMatingOutside;


		Node current = blossomNodeMatingOutside, next=null, otherEndNode;
		while(current != null) {
			current.seen = true;
			next=null;
			for(Edge edge : current.adjListEdges) {
				otherEndNode = edge.getOtherEnd(current);
				if(!otherEndNode.blossomParent.equals(current.blossomParent))	// node outside blossom
					edge.UpdateOriginalValue();
				else if(!otherEndNode.seen && next == null)				// get the next blossom node in the linear order
					next = otherEndNode;
				else
					continue;
			}
			if(current.mate == null && next != null)
				MutualMate(current, next);
			current.blossomParent = current;		//remove it from the blossom
			current = next;
		}
		blossomNodeToExpand.blossomChilds.clear();

		nodesHashMap.remove(noOfNodes);
		nodesList.remove(blossomNodeToExpand);
		--noOfNodes;

		PrintClass.PrintMaximalMatching(this);

	}

	private void MutualMate(Node a, Node b) {
		a.mate = b;
		b.mate = a;
	}

	/**
	 * Get the blossom node that is mating outside the blossom
	 * @param blossomNodeToExpand
	 * @return
	 */
	private Node getBlossomNodeMatingOutside(Node blossomNodeToExpand) {

		PrintClass.PrintCompleteNodeDetails(blossomNodeToExpand);
		for(Edge edge : blossomNodeToExpand.adjListEdges) {
			if(edge.getOtherEnd(blossomNodeToExpand).equals(blossomNodeToExpand.mate))
				return edge.getOriginalNode(blossomNodeToExpand);
		}

		System.out.println("\n\n%%%%%%%%%%%%%%%%%% getBlossomNodeMatingOutside : SHOULD NOT BE EXECUTED %%%%%%%%%%%%%%%%%%%%%%");
		return null;
	}
	/**
	 * Get the maximum perfect matching value which is the output
	 * @return
	 */
	public Long GetMaximumWeightPerfectMatchingValue() {

		Long maxValue = 0L;
		Edge edge = null;
		for(Node node : nodesList)
			node.seen = false;
		for(Node node : nodesList) {
			if(node.seen)
				continue;
			edge = node.getEdgeForGivenNode(node.mate);
			if(edge == null)
				System.out.println("\n\n%%%%%%%%%%%%%%%%%% GetMaximumWeightPerfectMatchingValue : SHOULD NOT BE EXECUTED %%%%%%%%%%%%%%%%%%%%%%");

			maxValue += edge.weight;
			node.seen = true;
			node.mate.seen = true;
		}

		return maxValue;
	}

	/**
	 * Kruskal algorithm for MST
	 */
	public void Kruskal() {

		Node ru, rv;
		for(Node node: nodesList)
			MakeSet(node);
		for(Edge edge : edgesList) {					// edges will already be in non-decreasing order
			ru = Find(edge.originalA);
			rv = Find(edge.originalB);
			if(!ru.equals(rv)) {
				mstTreeEdgesList.add(edge);
				edge.originalA.mstEdge.add(edge);
				edge.originalB.mstEdge.add(edge);
				Union(ru,rv);
			}
		}
	}
	private void Union(Node ru, Node rv) {
		if(ru.rank > rv.rank)
			rv.mstParent = ru;
		else if(rv.rank > ru.rank)
			ru.mstParent = rv;
		else {
			rv.mstParent = ru;
			++ru.rank;
		}
	}
	private Node Find(Node node) {
		if(node.mstParent.equals(node))
			return node;
		return Find(node.mstParent);
	}
	private void MakeSet(Node node) {
		node.mstParent = node;
	}

	/**
	 * Find the odd degree nodes
	 * @return
	 */
	public List<Node> OddDegreeNodes() {

		List<Node> oddDegreeNodes = new LinkedList<Node>();
		for(Node node : nodesList)
			if(node.mstEdge.size() % 2 == 1)
				oddDegreeNodes.add(node);
		return oddDegreeNodes;
	}

	/**
	 * Implement Floyd Warshall Algorithm to dins all pairs shortest paths
	 * @param oddDegreeNodes
	 * @return
	 */
	public Integer[][] AllPairsShortestPathsUsingFloydWarshallAlgorithm(List<Node> oddDegreeNodes) {

		Integer[][] shortestPathMatrix = new Integer[noOfNodes][noOfNodes];

		for(int i=0;i<shortestPathMatrix.length;i++)
			for(int j=i;j<shortestPathMatrix.length;j++) {
				shortestPathMatrix[i][j] = 0;
				shortestPathMatrix[j][i] = 0;
			}

		for(Edge edge : edgesList) {
			shortestPathMatrix[edge.originalA.id-1][edge.originalB.id-1] = edge.weight;
			shortestPathMatrix[edge.originalB.id-1][edge.originalA.id-1] = edge.weight;
		}

		for(int k=0;k<noOfNodes;k++)
			for(int i=0;i<shortestPathMatrix.length;i++)
				for(int j=i;j<shortestPathMatrix.length;j++) {
					if(shortestPathMatrix[i][j] > shortestPathMatrix[i][k] + shortestPathMatrix[k][j]) {
						shortestPathMatrix[i][j] = shortestPathMatrix[i][k] + shortestPathMatrix[k][j];
						shortestPathMatrix[j][i] = shortestPathMatrix[i][k] + shortestPathMatrix[k][j];
					}
				}

		System.out.println("\n\nAll Pairs Shortest Path: ");
		for(int i=0;i<noOfNodes;i++) {
			for(int j=0;j<noOfNodes;j++)
				System.out.print(shortestPathMatrix[i][j] + "  ");
			System.out.println();
		}
		return shortestPathMatrix;
	}

}
