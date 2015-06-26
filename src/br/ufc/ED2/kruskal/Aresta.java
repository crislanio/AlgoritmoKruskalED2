package br.ufc.ED2.kruskal;

/**
 * Classe Aresta que armazena uma aresta a para b e o peso 
 * 
 */
public class Aresta {

	public No a, b;
	public No originA, originB;
	public Integer peso;

	/**
	 * Construtor Padrão
	 */
	public Aresta() {
	}

	/**
	 * @param a
	 * @param b
	 * @param peso
	 */
	public Aresta(No a, No b, Integer peso) {
		this.a = a;
		this.b = b;
		this.originA = a;
		this.originB = b;
		this.peso = peso;
	}

	/**
	 * 
	 * Para a aresta (1,2), ambos os Nós 1 e 2 armazenam as arestas como (1,2). Então não sabemos qual é p adjNo para 1 e 2 indiviualmente	
	 * 
	 * @param no
	 * @param aresta
	 * @return
	 */
	public No getOutroFim(No no) {
		if(a.equals(no))
			return b;
		else if(b.equals(no))
			return a;
		else if(originA.equals(no))
			return originB;
		else
			return originA;
	}	
}
