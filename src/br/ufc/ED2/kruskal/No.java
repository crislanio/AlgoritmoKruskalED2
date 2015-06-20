package br.ufc.ED2.kruskal;

import java.util.LinkedList;
import java.util.List;

public class No {

	public Integer id;
	public List<Aresta> listaArestasAdj;

	public No mstPai;
	public Integer rank;
	public List<Aresta> mstAresta;
	public double L;

	/**
	 * Construtor Padrão
	 */
	public No() {
		listaArestasAdj = new LinkedList<>();
	}

	/**
	 * @param id
	 */
	public No(Integer id) {
		this.id = id;
		listaArestasAdj = new LinkedList<>();
		mstPai = null;
		rank = 0;
		mstAresta = new LinkedList<Aresta>();
		L = 0;
	}

	/**
	 * add a aresta
	 * @param adj
	 */
	public void addNoAdj(Aresta adj) {
		this.listaArestasAdj.add(adj);
	}
}