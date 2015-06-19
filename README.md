# AlgoritmoKruskalED2
Implemente o algoritmo de Kruskal para resolver o problema de Árvore Geradora Mínima em grafos
simples, não-direcionados e conexos.
Seu programa deve considerar grafos representados através de listas de adjacências ou de matriz de
adjacências. Para obter o grafo de entrada, faça com que seu algoritmo leia um arquivo no seguinte
formato:
N M
u_1 v_1 c_1
u_2 v_2 c_2
...
u_M v_M c_M
N e M na primeira linha representam respectivamente a ordem e o tamanho do grafo. Após, são seguidas
exatamente M linhas, cada uma dizendo respeito a uma aresta, onde u_i e v_i são os vértices que a i-
ésima aresta conecta e c_i é seu custo.
Lembre-se que o grafo deve ser simples e não-direcionado, portanto não podem ocorrer arestas para-
lelas nem laços, e se algum vértice u reconhece outro vértice v como seu vizinho, esse v também deve
necessariamente reconhecer u como seu vizinho.
Faça uso da estrutura de union-find conveniente às técnicas de path-compression e union-by-rank vistas
em sala.
Seu programa deve, portanto, retornar o custo de uma árvore geradora mínima do grafo obtido. Ou
seja, não é necessário armazenar quais arestas foram selecionadas, a não ser que essa informação seja
conveniente ao seu programa.
