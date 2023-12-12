package it.unicam.cs.asdl2324.es11;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class MapAdjacentListUndirectedGraphTest {

    @Test
    final void testNodeCount() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertEquals(0, g.nodeCount());
        g.addNode(new GraphNode<String>("s"));
        assertEquals(1, g.nodeCount());
        g.addNode(new GraphNode<String>("u"));
        assertEquals(2, g.nodeCount());
    }

    @Test
    final void testEdgeCount() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertEquals(0, g.edgeCount());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertEquals(0, g.edgeCount());
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false, 10.1);
        g.addEdge(esu);
        assertEquals(1, g.edgeCount());
        g.addEdge(esu);
        assertEquals(1, g.edgeCount());
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, false, 5.12);
        g.addEdge(esx);
        assertEquals(2, g.edgeCount());
    }

    @Test
    final void testClear() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertTrue(g.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertFalse(g.isEmpty());
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false, 10.1);
        g.addEdge(esu);
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testIsDirected() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertFalse(g.isDirected());
    }

    @Test
    final void testGetNodes() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        Set<GraphNode<String>> nodes = g.getNodes();
        assertTrue(nodes.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        nodes = g.getNodes();
        Set<GraphNode<String>> testNodes = new HashSet<GraphNode<String>>();
        GraphNode<String> nsTest = new GraphNode<String>("s");
        GraphNode<String> nuTest = new GraphNode<String>("u");
        testNodes.add(nuTest);
        testNodes.add(nsTest);
        assertTrue(nodes.equals(testNodes));
        GraphNode<String> nuTestBis = new GraphNode<String>("u");
        g.addNode(nuTestBis);
        nodes = g.getNodes();
        assertTrue(nodes.equals(testNodes));
    }

    @Test
    final void testAddNode() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertThrows(NullPointerException.class, () -> g.addNode(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertFalse(g.containsNode(ns));
        g.addNode(ns);
        assertTrue(g.containsNode(nsTest));
        GraphNode<String> nu = new GraphNode<String>("u");
        GraphNode<String> nuTest = new GraphNode<String>("u");
        g.addNode(nu);
        assertTrue(g.containsNode(nuTest));
    }

    @Test
    final void testContainsNode() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertThrows(NullPointerException.class, () -> g.containsNode(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        GraphNode<String> nsTest = new GraphNode<String>("s");
        assertFalse(g.containsNode(nsTest));
        g.addNode(ns);
        assertTrue(g.containsNode(nsTest));
    }

    @Test
    final void testGetNodeOf() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertThrows(NullPointerException.class, () -> g.getNodeOf(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        ns.setColor(1);
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphNode<String> node = g.getNodeOf("s");
        assertEquals("s", node.getLabel());
        assertEquals(1, node.getColor());
        node = g.getNodeOf("u");
        assertEquals("u", node.getLabel());
        assertEquals(0, node.getColor());
    }

    @Test
    final void testGetAdjacentNodesOf() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.getAdjacentNodesOf(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        Set<GraphNode<String>> adjNodes = new HashSet<GraphNode<String>>();
        assertTrue(g.getAdjacentNodesOf(ns).equals(adjNodes));
        GraphNode<String> nsTest = new GraphNode<String>("s");
        GraphNode<String> nu = new GraphNode<String>("u");
        GraphNode<String> nuTest = new GraphNode<String>("u");
        assertThrows(IllegalArgumentException.class,
                () -> g.getAdjacentNodesOf(nu));
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false, 10.1);
        g.addEdge(esu);
        GraphNode<String> nx = new GraphNode<String>("x");
        GraphNode<String> nxTest = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, false, 5.12);
        g.addEdge(esx);
        adjNodes.add(nxTest);
        adjNodes.add(nuTest);
        assertTrue(g.getAdjacentNodesOf(nsTest).equals(adjNodes));
    }

    

    @Test
    final void testGetEdges() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        Set<GraphEdge<String>> edgesTest = new HashSet<GraphEdge<String>>();
        assertTrue(g.getEdges().equals(edgesTest));
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false);
        g.addEdge(esu);
        edgesTest.add(esu);
        assertTrue(g.getEdges().equals(edgesTest));
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, false, 5.12);
        g.addEdge(esx);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, false, 2.05);
        g.addEdge(eux);
        GraphEdge<String> exu = new GraphEdge<String>(nx, nu, false, 3.04);
        g.addEdge(exu);
        edgesTest.add(eux);
        edgesTest.add(esx);
        edgesTest.add(exu);
        assertTrue(g.getEdges().equals(edgesTest));
        GraphNode<String> ny = new GraphNode<String>("y");
        g.addNode(ny);
        GraphEdge<String> exy = new GraphEdge<String>(nx, ny, false, 2.0);
        g.addEdge(exy);
        GraphEdge<String> eys = new GraphEdge<String>(ny, ns, false, 7.03);
        g.addEdge(eys);
        edgesTest.add(eys);
        edgesTest.add(exy);
        assertTrue(g.getEdges().equals(edgesTest));
        g.clear();
        edgesTest.clear();
        assertTrue(g.getEdges().equals(edgesTest));
    }

    @Test
    final void testAddEdge() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.addEdge(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(ns, nu, false)));
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(nu, ns, false)));
        g.addNode(nu);
        assertThrows(IllegalArgumentException.class,
                () -> g.addEdge(new GraphEdge<String>(ns, nu, true)));
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false);
        g.addEdge(esu);
        assertTrue(g.containsEdge(new GraphEdge<String>(ns, nu, false)));
    }

    @Test
    final void testContainsEdge() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertThrows(NullPointerException.class,
                () -> g.containsEdge(null));
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        GraphNode<String> nu = new GraphNode<String>("u");
        assertThrows(IllegalArgumentException.class,
                () -> g.containsEdge(new GraphEdge<String>(ns, nu, false)));
        assertThrows(IllegalArgumentException.class,
                () -> g.containsEdge(new GraphEdge<String>(nu, ns, false)));
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false);
        assertFalse(g.containsEdge(new GraphEdge<String>(ns, nu, false)));
        g.addEdge(esu);
        assertTrue(g.containsEdge(new GraphEdge<String>(ns, nu, false)));
    }

    @Test
    final void testGetEdgesOf() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        Set<GraphEdge<String>> edgesTest = new HashSet<GraphEdge<String>>();
        assertThrows(NullPointerException.class,
                () -> g.getEdgesOf(null));
        GraphNode<String> nu = new GraphNode<String>("u");
        assertThrows(IllegalArgumentException.class,
                () -> g.getEdgesOf(nu));
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false);
        g.addEdge(esu);
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, false, 5.12);
        g.addEdge(esx);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, false, 2.05);
        g.addEdge(eux);
        GraphEdge<String> exu = new GraphEdge<String>(nx, nu, false, 3.04);
        g.addEdge(exu);
        GraphNode<String> ny = new GraphNode<String>("y");
        g.addNode(ny);
        GraphEdge<String> exy = new GraphEdge<String>(nx, ny, false, 2.0);
        g.addEdge(exy);
        GraphEdge<String> eys = new GraphEdge<String>(ny, ns, false, 7.03);
        g.addEdge(eys);
        GraphNode<String> nw = new GraphNode<String>("w");
        g.addNode(nw);
        GraphEdge<String> euw = new GraphEdge<String>(nu, nw, false, 7.07);
        g.addEdge(euw);
        edgesTest.add(esu);
        edgesTest.add(esx);
        edgesTest.add(eys);
        assertTrue(g.getEdgesOf(ns).equals(edgesTest));
        edgesTest.clear();
        edgesTest.add(esx);
        edgesTest.add(exy);
        edgesTest.add(eux);
        assertTrue(g.getEdgesOf(nx).equals(edgesTest));
        edgesTest.clear();
        edgesTest.add(exy);
        edgesTest.add(eys);
        assertTrue(g.getEdgesOf(ny).equals(edgesTest));
        edgesTest.clear();
        edgesTest.add(euw);
        assertTrue(g.getEdgesOf(nw).equals(edgesTest));
    }

    
    @Test
    final void testMapAdjacentListDirectedGraph() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testSize() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertTrue(g.size() == 0);
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertTrue(g.size() == 1);
        GraphNode<String> nu = new GraphNode<String>("u");
        g.addNode(nu);
        assertTrue(g.size() == 2);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false);
        g.addEdge(esu);
        assertTrue(g.size() == 3);
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        assertTrue(g.size() == 4);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, false, 5.12);
        g.addEdge(esx);
        assertTrue(g.size() == 5);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, false, 2.05);
        g.addEdge(eux);
        assertTrue(g.size() == 6);
        g.clear();
        assertTrue(g.size() == 0);
    }

    @Test
    final void testIsEmpty() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        assertTrue(g.isEmpty());
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    final void testGetDegreeOf() {
        Graph<String> g = new MapAdjacentListUndirectedGraph<String>();
        GraphNode<String> ns = new GraphNode<String>("s");
        g.addNode(ns);
        assertTrue(g.getDegreeOf(ns) == 0);
        assertThrows(NullPointerException.class,
                () -> g.getDegreeOf(null));
        GraphNode<String> nu = new GraphNode<String>("u");
        assertThrows(IllegalArgumentException.class,
                () -> g.getDegreeOf(nu));
        g.addNode(nu);
        GraphEdge<String> esu = new GraphEdge<String>(ns, nu, false);
        g.addEdge(esu);
        GraphNode<String> nx = new GraphNode<String>("x");
        g.addNode(nx);
        GraphEdge<String> esx = new GraphEdge<String>(ns, nx, false, 5.12);
        g.addEdge(esx);
        GraphEdge<String> eux = new GraphEdge<String>(nu, nx, false, 2.05);
        g.addEdge(eux);
        GraphEdge<String> exu = new GraphEdge<String>(nx, nu, false, 3.04);
        g.addEdge(exu);
        GraphNode<String> ny = new GraphNode<String>("y");
        g.addNode(ny);
        GraphEdge<String> exy = new GraphEdge<String>(nx, ny, false, 2.0);
        g.addEdge(exy);
        GraphEdge<String> eys = new GraphEdge<String>(ny, ns, false, 7.03);
        g.addEdge(eys);
        GraphNode<String> nw = new GraphNode<String>("w");
        g.addNode(nw);
        GraphEdge<String> euw = new GraphEdge<String>(nu, nw, false, 7.07);
        g.addEdge(euw);
        GraphNode<String> nz = new GraphNode<String>("z");
        g.addNode(nz);
        GraphEdge<String> ezy = new GraphEdge<String>(nz, ny, false, 7.107);
        g.addEdge(ezy);
        assertTrue(g.getDegreeOf(ns)==3);
        assertTrue(g.getDegreeOf(nu)==3);
        assertTrue(g.getDegreeOf(nx)==3);
        assertTrue(g.getDegreeOf(ny)==3);
        assertTrue(g.getDegreeOf(nz)==1);
        assertTrue(g.getDegreeOf(nw)==1);
    }

}
