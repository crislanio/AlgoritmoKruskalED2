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

	public Grafo() {
		nodosList = new LinkedList<No>();
		arestasList = new LinkedList<Aresta>();
		nodosHashMap = new HashMap<>();
		noDosNodos = 0;
		noDasArestas = 0;
		mstArvArestasList = new LinkedList<Aresta>();

	}

	/**
	 * add os nodos e arestas no grafo
	 * @param no1
	 * @param no2
	 * @param peso
	 */
	
	public void addNoEAresta(No no1, No no2, Integer peso) {

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
		no1.addNoAdj(aresta);		// add a lista da matriz de adjacencia no no1
		no2.addNoAdj(aresta);		// adicionar a matriz de lista de adjacência para no2
	}

	public void RotViavel() {
		for(No no : nodosList)
			no.L = 0.0;
		double max;
		for(int i=1;i<=2;i++) {
			for(No no : nodosList) {
				max = 0;
				for(Aresta aresta : no.listaArestasAdj) {
					if(max <= ((double) aresta.peso - aresta.getOutroFim(no).L))
						max = (double) aresta.peso - aresta.getOutroFim(no).L;
				}

				no.L = max;
			}
		}
	}
	/**
	 * Kruskal algorithm for MST
	 */
	public void Kruskal() {

		No ru, rv;
		for(No no: nodosList)
			MakeSet(no);
		for(Aresta aresta : arestasList) {					// arestas já estarão em ordem decrescente
			ru = Buscar(aresta.originalA);
			rv = Buscar(aresta.originalB);
			if(!ru.equals(rv)) {
				mstArvArestasList.add(aresta);
				aresta.originalA.mstAresta.add(aresta);
				aresta.originalB.mstAresta.add(aresta);
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
	private No Buscar(No no) {
		if(no.mstPai.equals(no))
			return no;
		return Buscar(no.mstPai);
	}
	private void MakeSet(No no) {
		no.mstPai = no;
	}
}
