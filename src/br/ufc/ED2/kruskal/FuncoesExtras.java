package br.ufc.ED2.kruskal;

import java.util.List;

public class FuncoesExtras {

	public Grafo root;
	public No rootNode;

	/**
	 * Default Constructor
	 */
	public FuncoesExtras() {
		root = new Grafo();
		rootNode = null;
	}

	/**
	 * Parse the first line of the input
	 * @param line
	 */
	public void parseFirstLineInput(String line) {
		String splitStr[] = line.split("\\s+");
		root.noDosNodos = Integer.parseInt(splitStr[0]);
		root.noDasArestas = Integer.parseInt(splitStr[1]);
	}

	/**
	 * Parse the remaining lines of the input
	 * @param str
	 */
	public void addToGraph(String str) {
		String splitStr[] = str.trim().split("\\s+");

		No a, b;
		Integer id1 = Integer.parseInt(splitStr[0]);
		Integer id2 = Integer.parseInt(splitStr[1]);
		if(root.nodosHashMap.containsKey(id1)) {
			a = root.nodosHashMap.get(id1);
		} else {
			a = new No(id1);
		}
		if(root.nodosHashMap.containsKey(id2)) {
			b = root.nodosHashMap.get(id2);
		} else {
			b = new No(id2);
		}

		if(rootNode == null) {
			rootNode = a;
		}

		root.addNodeAndEdge(a,b, Integer.parseInt(splitStr[2]));		// add Node and Edge
	}


	public void ExecutionFunction() {


		/**
		 * 1) Kruskal algorithm to find the MST
		 * 2) Find nodes with odd degree
		 * 3) Find all pairs shortest paths
		 */


		root.Kruskal();						// mstTree contains the edges of the MST Tree
		System.out.println("\n\nArestas na MST Tree by Kruskal Algorithm :");
		for(Aresta edge : root.mstArvArestasList)
			System.out.println("(" + edge.a.id + "," + edge.b.id + ") = " + edge.peso);
		List<No> oddDegreeNodes = root.OddDegreeNodes();
		System.out.println("\n\nOdd degree nodes are :");
		for(No node : oddDegreeNodes)
			System.out.println("Node : " + node.id);
	}
}
