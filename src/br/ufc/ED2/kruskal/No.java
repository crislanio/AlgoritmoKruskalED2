package br.ufc.ED2.kruskal;

import java.util.LinkedList;
import java.util.List;

public class No {

	public Integer id;
	public boolean visitado;
	public List<Aresta> listaArestasAdj;

	public No mstPai;
	public Integer rank;
	public List<Aresta> mstAresta;

	public double L;
	public No comp;
	public boolean visto;
	public boolean noExt;
	public No camiPai;
	public List<No> camiFilho;
	public List<Aresta> zeroAdjArestas;

	public No paiDest;
	public List<No> filhosDest;


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
		this.visitado = false;
		listaArestasAdj = new LinkedList<>();
		mstPai = null;
		rank = 0;
		mstAresta = new LinkedList<Aresta>();
		L = 0;
		comp = null;
		visto = false;
		noExt = false;
		camiPai = null;
		camiFilho = new LinkedList<No>();
		zeroAdjArestas = new LinkedList<Aresta>();
		paiDest = this;
		filhosDest = new LinkedList<No>();
	}

	/**
	 * add a aresta
	 * @param adj
	 */
	public void addAdjNode(Aresta adj) {
		this.listaArestasAdj.add(adj);
	}

	@Override
	public String toString() {				// Print the labels instead of id
		return "Nó: " + L + " (id:"+id+")";
	}

	public boolean isRealOuterNode() {
		if(filhosDest.size() == 0)
			return true;
		return false;
	}

	public Aresta getEdgeForGivenNode(No mate) {
		for(Aresta edge : listaArestasAdj)
		{
			if(edge.getOtherEnd(this).equals(mate))
				return edge;
		}

		return null;
	}

}