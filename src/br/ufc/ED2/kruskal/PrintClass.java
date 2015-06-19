package br.ufc.ED2.kruskal;

import java.util.LinkedList;
import java.util.Queue;

public class PrintClass {

	public static void PrintGraph(Graph graph) {
		System.out.println("\n\n\n------------- Printing the graph --------------\n");
		System.out.println(graph.nodesList);
		System.out.println(graph.edgesList);
		for(Node node : graph.nodesList)
			System.out.println("For node : " + node + " >> " + node.adjListEdges);
	}

	public static void PrintLabelling(Graph graph) {

		System.out.println("\n\n\n------------ Printing Labels -----------\n");
		for(int i=1;i<=graph.noOfNodes;i++)
			System.out.println(graph.nodesHashMap.get(i).id + " >> label = " + graph.nodesHashMap.get(i).L);
	}

	public static void PrintZeroGraph(Graph graph) {

		System.out.println("\n\n\n------------ Printing Zero Graph-----------\n");
		System.out.println(graph.zeroGraphNodes);
		for(Edge edge : graph.zeroGraphEdges)
			System.out.println(edge);
	}

	public static void PrintFreeNodes(Graph graph) {
		// First return the free node in the hungarian tree
		System.out.println("\n\n\n------------ Printing Free Nodes in ZG-----------\n");
		for(Node node : graph.zeroGraphNodes) 				// blossom nodes are also added to zeroGraphNodes
			if(node.mate == null && node.blossomParent.equals(node))					// nodes inside the blossom should not be included
				System.out.println("ZG Free Node = " + node + " >> " + node.mate);
		for(Node node : graph.nodesList)
			if(node.mate == null && node.blossomParent.equals(node))						// nodes inside the blossom should not be included
				System.out.println("Free Node = " + node + " >> " + node.mate);
	}

	public static void PrintMaximalMatching(Graph graph) {

		System.out.println("\n\n\n------------ Printing Maximal Matching Nodes -----------\n");
		for(Node node : graph.nodesList)
			if(node.mate != null) {
				System.out.println("("+node.id + "," + node.mate.id+")");
				node.seen = true;
				node.mate.seen = true;
			}
		for(Node node : graph.nodesList)
			node.seen = false;
	}

	public static void PrintAugmentingPath(Node u) {

		System.out.println("\n\n\n------------ Printing Augmenting Path -----------\n");
		while(u != null) {
			System.out.println(u + " and Parent: " + u.parentAugPath + " and Outer Node = " + u.outerNode);
			u = u.parentAugPath;
		}

	}

	public static void PrintCompleteNodeDetails(Node blossomNode) {

		System.out.println("\n\n\n------------ Printing Complete Node details-----------\n");
		System.out.println("Id : " + blossomNode.id);
		System.out.println("Adj List Edges : " + blossomNode.adjListEdges);
		System.out.println("Label : " + blossomNode.L);
		System.out.println("Mate : " + blossomNode.mate);
		System.out.println("OuterNode : " + blossomNode.outerNode);
		System.out.println("Parent : " + blossomNode.parentAugPath);
		System.out.println("ZeroAdjEdges : " + blossomNode.zeroAdjEdges);
		System.out.println("Blossom Parent : " + blossomNode.blossomParent);
		System.out.println("Blossom Child Nodes : " + blossomNode.blossomChilds);

	}

	public static void PrintChildAugPath(Node freeNode) {

		System.out.println("\n\n\n------------ Printing ChildAug Path details-----------\n");
		Queue<Node> q = new LinkedList<Node>();
		q.add(freeNode);
		while(!q.isEmpty()) {
			Node u = q.poll();
			for(Node node : u.childAugPath) {
				System.out.println(node + " has parent as " + u);
				q.add(node);
			}

		}
	}

}
