

package com.ben.Day7.helper;

/******************************************************************************
 *  Compilation:  javac DepthFirstDirectedPaths.java
 *  Execution:    java DepthFirstDirectedPaths digraph.txt s
 *  Dependencies: Digraph.java Stack.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Determine reachability in a digraph from a given vertex using
 *  depth-first search.
 *  Runs in O(E + V) time.
 *
 *  % java DepthFirstDirectedPaths tinyDG.txt 3
 *  3 to 0:  3-5-4-2-0
 *  3 to 1:  3-5-4-2-0-1
 *  3 to 2:  3-5-4-2
 *  3 to 3:  3
 *  3 to 4:  3-5-4
 *  3 to 5:  3-5
 *  3 to 6:  not connected
 *  3 to 7:  not connected
 *  3 to 8:  not connected
 *  3 to 9:  not connected
 *  3 to 10:  not connected
 *  3 to 11:  not connected
 *  3 to 12:  not connected
 *
 ******************************************************************************/

import java.util.*;

/**
 * The {@code DepthFirstDirectedPaths} class represents a data type for
 * finding directed paths from a source vertex <em>s</em> to every
 * other vertex in the digraph.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 * worst case, where <em>V</em> is the number of vertices and <em>E</em>
 * is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DepthFirstDirectedPaths {
    private Map<String, Boolean> marked;  // marked[v] = true iff v is reachable from s
    private Map<String, String> edgeTo;      // edgeTo[v] = last edge on path from s to v
    private final String s;       // source vertex

    /**
     * Computes a directed path from {@code s} to every other vertex in {@code digraph}.
     *
     * @param digraph the digraph
     * @param s       the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DepthFirstDirectedPaths(Digraph digraph, String s) {
        marked = new LinkedHashMap<>();
        edgeTo = new LinkedHashMap<>();
        Set<String> strings = digraph.adj.keySet();
        for (String string : strings) {
            marked.put(string, false);
        }
        this.s = s;
        validateVertex(s);
        dfs(digraph, s);
    }

    private void dfs(Digraph digraph, String v) {
        marked.put(v, true);
        for (String w : digraph.adj(v)) {
            if (marked.get(w) == false) {
                edgeTo.put(w, v); // 記錄「怎麼走到 w 的」, 要走到 w，是從 v 走過來的
                dfs(digraph, w);
            }
        }
    }

    /**
     * Is there a directed path from the source vertex {@code s} to vertex {@code v}?
     *
     * @param v the vertex
     * @return {@code true} if there is a directed path from the source
     * vertex {@code s} to vertex {@code v}, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(String v) {
//        validateVertex(v);
//        return marked[v];
        return marked.put(v, true);
    }


    /**
     * Returns a directed path from the source vertex {@code s} to vertex {@code v}, or
     * {@code null} if no such path.
     *
     * @param v the vertex
     * @return the sequence of vertices on a directed path from the source vertex
     * {@code s} to vertex {@code v}, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<String> pathTo(String v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<String> path = new Stack<String>();
        /**
         * 之後在 pathTo(v) 時，會用 edgeTo[] 一路「倒推」回起點。
         *
         * 例如：
         * 3 → 5 → 4 → 2
         *
         *
         * 會記成：
         *
         * edgeTo[5] = 3
         * edgeTo[4] = 5
         * edgeTo[2] = 4
         */
        for (String x = v; !Objects.equals(x, s); x = edgeTo.get(x)) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(String v) {
//        int V = marked.length;
//        if (v < 0 || v >= V)
//            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    /**
     * Unit tests the {@code DepthFirstDirectedPaths} data type.
     * Unit tests the {@code DepthFirstDirectedPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
//        % java DepthFirstDirectedPaths tinyDG.txt 3
        In in = new In("output0.txt");
        Digraph digraph = new Digraph(in);
        // StdOut.println(digraph);

//        String source = "light_red";
        String v = "shiny_gold";


        Set<String> res = new HashSet<>();
        Set<String> strings = digraph.adj.keySet();
        for (String s : strings) {
            DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(digraph, s);
            if (dfs.hasPathTo(v)) {
//                System.out.println(s + " to " + v);
                for (String x : dfs.pathTo(v)) {
                    res.add(x);
                    if (Objects.equals(x, s)) {
                        StdOut.print(x);
                    } else {
                        StdOut.print(" -> " + x);
                    }
                }
                StdOut.println();
            } else {
//                System.out.println(s + " to " + v + ":  not connected");
            }
        }

        res.remove(v);
        System.out.println("ans: " + res.size());
//        for (String re : res) {
//            System.out.println(re);
//        }


    }

}
