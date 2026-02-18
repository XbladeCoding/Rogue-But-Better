public class Site {
    // create variables for i and j inside of Site
    private final int i;
    private final int j;

    // instantiate a new Site for location (i, j) (makes a site exist)
    public Site(int i, int j) {
        // refer the Site(i, j) to the variables i and j in Site 
        this.i = i;
        this.j = j;
    }
    // get i coordinate (x - coord  in n x n matrix)
    public int i() {
        return i;
    }
    // get j coordinate (y - coord in n x n matrix)
    public int j() {
        return j;
    }
    // return Manhattan distance from invoking site to w (grid distance, shortest distance without diagonals)
    public int manhattan(Site w) {
        return Math.abs(w.i() - this.i()) + Math.abs(w.j() - this.j());
    }
    // is invoking site equal to w? (checking if sites are equal)
    public boolean equals(Site w) {
        if (w == null) return false;
        return ((w.i() == this.i()) && (w.j() == this.j()));
    }
    
    // return string representation for easy debugging
    public String toString() {
        return "(" + i + ", " + j + ")";
    }
    
    public static void main(String[] args) {
        // Test 1: Create sites and print them
        Site a = new Site(2, 3);
        Site b = new Site(5, 7);
        Site c = new Site(2, 3);
        
        System.out.println("Site a: " + a);  // Should print (2, 3)
        System.out.println("Site b: " + b);  // Should print (5, 7)
        System.out.println("Site c: " + c);  // Should print (2, 3)
        
        // Test 2: Test equals
        System.out.println("\na.equals(b): " + a.equals(b));  // Should be false
        System.out.println("a.equals(c): " + a.equals(c));    // Should be true
        
        // Test 3: Test Manhattan distance
        System.out.println("\nManhattan distance a to b: " + a.manhattan(b));  // |5-2| + |7-3| = 3 + 4 = 7
        System.out.println("Manhattan distance a to c: " + a.manhattan(c));    // Should be 0
        
        // Test 4: Test getters
        System.out.println("\na.i() = " + a.i() + ", a.j() = " + a.j());
    }
}
