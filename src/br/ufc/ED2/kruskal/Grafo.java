package br.ufc.ED2.kruskal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Grafo {

	public Integer noDosNodos;
	public Integer noDasArestas;
	public Map<Integer, No> nodosHashMap;
	public List<No> nodosList;
	public List<Aresta> arestasList;

	public List<Aresta> mstArvArestasList;

	public Long matchingSize;

	public List<Aresta> zeroGrafoAres;
	public List<No> zeroGrafoNodos;

	public List<Aresta> destaqueDentroArestas;

	public Grafo() {
		nodosList = new LinkedList<No>();
		arestasList = new LinkedList<Aresta>();
		nodosHashMap = new HashMap<>();
		noDosNodos = 0;
		noDasArestas = 0;
		mstArvArestasList = new LinkedList<Aresta>();
		matchingSize = 0L;
		zeroGrafoAres = new LinkedList<Aresta>();
		zeroGrafoNodos = new LinkedList<No>();
		destaqueDentroArestas = new LinkedList<Aresta>();
	}

	/**
	 * add os nodos e arestas no grafo
	 * @param no1
	 * @param no2
	 * @param peso
	 */
	public void addNodeAndEdge(No no1, No no2, Integer peso) {

		if(nodosHashMap.containsKey(no1.id)) {
			no1 = nodosHashMap.get(no1.id);
		} else {
			nodosList.add(no1);
			nodosHashMap.put(no1.id, no1);
		}

		if(nodosHashMap.containsKey(no2.id)) {
			no2 = nodosHashMap.get(no2.id);
		} else {
			nodosList.add(no2);
			nodosHashMap.put(no2.id, no2);
		}

		Aresta aresta = new Aresta(no1, no2, peso);
		arestasList.add(aresta);		// add a aresta na lista
		no1.addAdjNode(aresta);		// add a lista da matriz de adjacencia no no1
		no2.addAdjNode(aresta);		// adicionar a matriz de lista de adjacência para no2
	}

	public void RotViavel() {
		for(No no : nodosList)
			no.L = 0.0;
		double max;
		for(int i=1;i<=2;i++) {
			for(No node : nodosList) {
				max = 0;
				for(Aresta aresta : node.listaArestasAdj) {
					if(max <= ((double) aresta.peso - aresta.getOtherEnd(node).L))
						max = (double) aresta.peso - aresta.getOtherEnd(node).L;
				}

				node.L = max;
			}
		}
	//	Imprimir.PrintL(this);
	}

	public void ComputeZeroGraph(int countNo) {	// tem que ser computado em originalA e originalB em cada node

		/**
		 * 1) Keep the zero graph edges between nodes inside the same blossom for blossom expansion
		 * 2) Clear ZGNodes and ZGEdges list
		 * 3) Recompute ZG again for edges between nodes with blossom parent as itself
		 */
		for(Aresta aresta : zeroGrafoAres)		// Add arestas entre nodos dentro do dest para permanecer na lista
			if(mesmoDest(aresta) && !destaqueDentroArestas.contains(aresta))	// Ineficiente !@
				destaqueDentroArestas.add(aresta);

		zeroGrafoAres.clear();
		zeroGrafoNodos.clear();
		for(Aresta edge : arestasList) {
			if(!mesmoDest(edge) && (edge.originalA.L + edge.originalB.L) == (double) edge.peso )
			{
				zeroGrafoAres.add(edge);

				edge.originalA.zeroAdjArestas.add(edge);	// Lista de atualização zeroAdjEdges em cada extremidade do nó
				edge.originalB.zeroAdjArestas.add(edge);

				if(!edge.a.equals(edge.originalA))	// lista de atualização zeroAdjEdges em dest nó também
					edge.a.zeroAdjArestas.add(edge);
				if(!edge.b.equals(edge.originalB))
					edge.b.zeroAdjArestas.add(edge);

				if(!zeroGrafoNodos.contains(edge.a))	// add nodos dest no zeroGraphNodes
					zeroGrafoNodos.add(edge.a);
				if(!zeroGrafoNodos.contains(edge.b))
					zeroGrafoNodos.add(edge.b);
			}
		}

	//	Imprimir.PrintZeG(this);
	}

	/**
	 * if 2 nodos sao  o mesmo dest
	 * @param aresta
	 * @return
	 */
	private boolean mesmoDest(Aresta aresta) {
		if(aresta.a.paiDest.equals(aresta.b.paiDest))
			return true;
		return false;
	}

	/**
	 * Kruskal algorithm for MST
	 */
	public void Kruskal() {

		No ru, rv;
		for(No node: nodosList)
			MakeSet(node);
		for(Aresta edge : arestasList) {					// edges will already be in non-decreasing order
			ru = Find(edge.originalA);
			rv = Find(edge.originalB);
			if(!ru.equals(rv)) {
				mstArvArestasList.add(edge);
				edge.originalA.mstAresta.add(edge);
				edge.originalB.mstAresta.add(edge);
				Union(ru,rv);
			}
		}
	}
	private void Union(No ru, No rv) {
		if(ru.rank > rv.rank)
			rv.mstPai = ru;
		else if(rv.rank > ru.rank)
			ru.mstPai = rv;
		else {
			rv.mstPai = ru;
			++ru.rank;
		}
	}
	private No Find(No node) {
		if(node.mstPai.equals(node))
			return node;
		return Find(node.mstPai);
	}
	private void MakeSet(No node) {
		node.mstPai = node;
	}

	/**
	 * Find the odd degree nodes
	 * @return
	 */
	public List<No> OddDegreeNodes() {

		List<No> oddDegreeNodes = new LinkedList<No>();
		for(No node : nodosList)
			if(node.mstAresta.size() % 2 == 1)
				oddDegreeNodes.add(node);
		return oddDegreeNodes;
	}
}
