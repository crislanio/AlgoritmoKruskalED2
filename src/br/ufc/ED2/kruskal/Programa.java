package br.ufc.ED2.kruskal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Programa {

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		FuncoesExtras of = new FuncoesExtras();

		/**
		 * Ler a entrada do arquivo
		 */

//		BufferedReader br = new BufferedReader(new FileReader("Ex1.txt"));
		
//		BufferedReader br = new BufferedReader(new FileReader("Ex2.txt"));
		
		BufferedReader br = new BufferedReader(new FileReader("Ex3.txt"));
		
		
		String linha = null;
		of.analisarEntradaPrimeiraLinha(br.readLine());		// ler a primeira linha para definir a no de nós, arestas e nó raiz
		while( (linha = br.readLine()) != null)
			of.addAOGrafo(linha);
		br.close();

		of.ExecFunc();

	}

}
