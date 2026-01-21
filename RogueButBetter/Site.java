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
        return ((w.i() == this.i()) && (w.j() == this.j()));
    }
    
    public static void main(String[] args) {

    }
}
