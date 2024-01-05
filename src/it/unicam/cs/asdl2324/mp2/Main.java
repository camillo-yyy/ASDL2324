package it.unicam.cs.asdl2324.mp2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

public class Main {

 public static void main(String[] args) {

  Graph<String> gr = new AdjacencyMatrixUndirectedGraph<String>();
  KruskalMST<String> alg = new KruskalMST<String>();
  Set<GraphEdge<String>> result = new HashSet<GraphEdge<String>>();
  assertTrue(alg.computeMSP(gr).equals(result));
  GraphNode<String> a = new GraphNode<String>("a");
  gr.addNode(a);
  assertTrue(alg.computeMSP(gr).equals(result));
  GraphNode<String> b = new GraphNode<String>("b");
  gr.addNode(b);
  assertTrue(alg.computeMSP(gr).equals(result));
  gr.addEdge(new GraphEdge<String>(a, b, false, 1));
  result.add(new GraphEdge<String>(a, b, false, 1));

      
  GraphNode<String> c = new GraphNode<String>("c");
  gr.addNode(c);
  GraphNode<String> d = new GraphNode<String>("d");
  gr.addNode(d);
  GraphNode<String> e = new GraphNode<String>("e");
  gr.addNode(e);
  System.out.println("ciao");
  Set<GraphEdge<String>> dx = alg.computeMSP(gr);
  System.out.println("ciao");
  for(GraphEdge<String> asd : dx) {
    System.out.println(asd.getNode1());
    System.out.println(asd.getNode2());
    
  }
      System.out.println("ciao");

 }
 
}
