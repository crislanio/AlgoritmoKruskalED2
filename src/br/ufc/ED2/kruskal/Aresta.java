package br.ufc.ED2.kruskal;

/**
 * 
 * @author Crislânio Macêdo
 * Classe Aresta que armazena uma aresta a para b e o peso 
 * 
 */
public class Aresta {

	public No a, b;
	public No originalA, originalB;
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
		this.originalA = a;
		this.originalB = b;
		this.peso = peso;
	}

	/**
	 * 
	 * Para a aresta (1,2), ambos os Nós 1 e 2 armazenam as arestas como (1,2). Então não sabemos qual é p adjNode para 1 e 2 indiviualmente	
	 * 
	 * @param no
	 * @param aresta
	 * @return
	 */
	public No getOtherEnd(No no) {
		if(a.equals(no))
			return b;
		else if(b.equals(no))
			return a;
		else if(originalA.equals(no))
			return originalB;
		else
			return originalA;
	}

	@Override
	public String toString() {
		return "("+ (int)originalA.L + "," + (int)originalB.L + ")=" + peso + " or ("+originalA.id+","+originalB.id+")";
	}

	public void UpdateEdge(No noP, No no) {
		if(a.equals(no))
			a = noP;
		else if(b.equals(no))
			b = noP;
	}

	/**
	 * Diz se esta aresta têm peso zero  ou não
	 * @return
	 */
	public boolean isZeroEdge() {
		if(originalA.L + originalB.L == (double) peso)
			return true;
		return false;
	}

	/**
	 * Returna o nó original que tinha a primeira incidência
	 * @param noParaExpandir
	 * @return
	 */
	public No getOriginalNode(No noParaExpandir) {
		if(a.equals(noParaExpandir))
			return originalA;
		return originalB;
	}

	public void UpdateOriginalValue() {
		a = originalA;
		b = originalB;
	}

}
