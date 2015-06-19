package br.ufc.ED2.kruskal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class DriverProgramMaximumWeigthPerfectMatching {

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		OperationFunction of = new OperationFunction();

		/**
		 * Read the input from the file
		 */
		BufferedReader br = new BufferedReader(new FileReader("Ex1.txt"));
		String line = null;
		of.parseFirstLineInput(br.readLine());		// read the first line to set the no of nodes, edges and root node
		while( (line = br.readLine()) != null)
			of.addToGraph(line);
		br.close();

		of.MaximumWeightedMatching();

	}

}
