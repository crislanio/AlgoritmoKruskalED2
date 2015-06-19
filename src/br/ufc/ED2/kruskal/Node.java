package br.ufc.ED2.kruskal;

/**
 * 
 * @author Maaniccka Sentil Manickam Poonkundran
 * Node class which stores the ID, visited or not, adjListEdges(list of edges from this node to other nodes)
 */
import java.util.LinkedList;
import java.util.List;

public class Node {

	public Integer id;
	public boolean visited;
	public List<Edge> adjListEdges;

	public Node mstParent;
	public Integer rank;
	public List<Edge> mstEdge;

	public double L;
	public Node mate;
	public boolean seen;
	public boolean outerNode;
	public Node parentAugPath;
	public List<Node> childAugPath;
	public List<Edge> zeroAdjEdges;

	public Node blossomParent;
	public List<Node> blossomChilds;


	/**
	 * Default Constructor
	 */
	public Node() {
		adjListEdges = new LinkedList<>();
	}

	/**
	 * Parameterized constructor
	 * @param id
	 */
	public Node(Integer id) {
		this.id = id;
		this.visited = false;
		adjListEdges = new LinkedList<>();
		mstParent = null;
		rank = 0;
		mstEdge = new LinkedList<Edge>();
		L = 0;
		mate = null;
		seen = false;
		outerNode = false;
		parentAugPath = null;
		childAugPath = new LinkedList<Node>();
		zeroAdjEdges = new LinkedList<Edge>();
		blossomParent = this;
		blossomChilds = new LinkedList<Node>();
	}

	/**
	 * add the edge
	 * @param adj
	 */
	public void addAdjNode(Edge adj) {
		this.adjListEdges.add(adj);
	}

	@Override
	public String toString() {				// Print the labels instead of id
		return "Node: " + L + " (id:"+id+")";
	}

	public boolean isRealOuterNode() {
		if(blossomChilds.size() == 0)
			return true;
		return false;
	}

	public Edge getEdgeForGivenNode(Node mate) {
		for(Edge edge : adjListEdges)
		{
			if(edge.getOtherEnd(this).equals(mate))
				return edge;
		}

		return null;
	}

}