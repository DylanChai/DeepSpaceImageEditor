package com.example.deepspace;

public class UnionFind {
    //The number of elements in this union find
    public int size;

    //used to track the size of each of the components
    public int[] sz;

    //id[i] points to the parent of i, if id[i] = i then i is a root node
    public int[] id;

    //tracks the number of components in the union find
    public int numComponents;

    public UnionFind(int size) {
        if (size <= 0)
            throw new IllegalArgumentException();

        this.size = numComponents = size;
        sz = new int[size];
        id = new int[size];

        for (int i = 0; i < size; i++) {
            id[i] = i; //link to itself(self root)
            sz[i] = 1; //each component is originally of size 1
        }
    }

    public int find(int p) {
        int root = p;
        while(root != id[root])
            root = id[root];

        while(p!=root) {
            int next = id[p];
            id[p] = root;
            p = next;
        }
        return root;
    }

    public boolean connected(int p, int q){
        return find(p) == find(q);
    }

    public int componentSize(int p) {
        return sz[find(p)];
    }

    public int size(){
        return size;
    }

    public int components() {
        return numComponents;
    }

    public void unify(int p, int q){
        int root1 = find(p);
        int root2 = find(q);

        if(root1 == root2) return;

        if(sz[root1] < sz[root2]) {
            sz[root2] += sz[root1];
            id[root1] = root2;
        } else {
            sz[root1] += sz[root2];
            id[root2] = root1;
        }
        numComponents--;
}

}