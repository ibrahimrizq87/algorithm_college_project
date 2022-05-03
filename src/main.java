import java.util.ArrayList;
import java.util.List;

class Node {
    int x, y, name;

    Node(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.name = value; /* some data*/
    }
}
class internalNode extends Node{
    internalNode(int x, int y, int value) {
        super(x, y, value);
    }
}
class leafNode extends Node{
    leafNode(int x, int y, int value) {
        super(x, y, value);
    }
}
/* Using two points of Rectangular (Top,Left) & (Bottom , Right)*/
class Boundry {
    public int getxMin() {
        return xMin;
    }

    public int getyMin() {
        return yMin;
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public Boundry(int xMin, int yMin, int xMax, int yMax) {
        super();
        /*
         *  Storing two diagonal points
         */
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }
    boolean intersect(Boundry boundry){
        /*return !( boundry.xMin-boundry.xMax > this.xMin + this.xMax
        ||        boundry.xMin+boundry.xMax < this.xMin - this.xMax
        ||        boundry.yMin-boundry.yMax > this.yMin + this.yMax
        ||        boundry.yMin+boundry.yMax < this.yMin - this.yMax );

    if (boundry.getxMax()<this.getxMin()){
        return false;
    }
        if (boundry.getxMin()>this.getxMax()){
            return false;
        }
        if (boundry.getyMax()<this.getyMin()){
            return false;
        }
        if (boundry.getyMin()>this.getyMax()){
            return false;
        }
        return true;
*/
        return(    boundry.getxMin() >= this.getxMin() && boundry.getxMin() <= this.getxMax()
                && boundry.getyMin() >= this.getyMin() && boundry.getyMin() <= this.getyMax()
        ||         boundry.getxMin() >= this.getxMin() && boundry.getxMin() <= this.getxMax()
                && boundry.getyMax() >= this.getyMin() && boundry.getyMax() <= this.getyMax()
        ||
                   boundry.getxMax() >= this.getxMin() && boundry.getxMax() <= this.getxMax()
                && boundry.getyMin() >= this.getyMin() && boundry.getyMin() <= this.getyMax()
        ||         boundry.getxMax() >= this.getxMin() && boundry.getxMax() <= this.getxMax()
                && boundry.getyMax() >= this.getyMin() && boundry.getyMax() <= this.getyMax()

        );

    }
  /*  boolean inIt(Boundry boundry){
        return(    (boundry.getxMin() >= this.getxMin() && boundry.getxMin() <= this.getxMax()
                && boundry.getyMin() >= this.getyMin() && boundry.getyMin() <= this.getyMax())
                &&
                (boundry.getxMin() >= this.getxMin() && boundry.getxMin() <= this.getxMax()
                        && boundry.getyMax() >= this.getyMin() && boundry.getyMax() <= this.getyMax())

                &&

                (boundry.getxMax() >= this.getxMin() && boundry.getxMax() <= this.getxMax()
                        && boundry.getyMin() >= this.getyMin() && boundry.getyMin() <= this.getyMax())

                &&
                (boundry.getxMax() >= this.getxMin() && boundry.getxMax() <= this.getxMax()
                        && boundry.getyMax() >= this.getyMin() && boundry.getyMax() <= this.getyMax())

        );
    }*/
    public boolean inRange(int x, int y) {
        return (x >= this.getxMin() && x <= this.getxMax()
                && y >= this.getyMin() && y <= this.getyMax());
    }public boolean outRange(int x, int y) {
        return (x >= this.getxMin() && x <= this.getxMax()
                && y >= this.getyMin() && y <= this.getyMax());
    }

    int xMin, yMin, xMax, yMax;

}

 class QuadTree {
    int MAX_CAPACITY = 4;
    int level = 0;
    List<Node> nodes;
    QuadTree northWest = null;
    QuadTree northEast = null;
    QuadTree southWest = null;
    QuadTree southEast = null;
    Boundry boundry;
    boolean isDivided =false;

    QuadTree(int level, Boundry boundry) {
        this.level = level;
        nodes = new ArrayList<Node>();
        this.boundry = boundry;
    }

    /* Traveling the Graph using Depth First Search*/
    static void dfs(QuadTree tree) {
        if (tree == null)
            return;

        System.out.printf("\nLevel = %d [X1=%d Y1=%d] \t[X2=%d Y2=%d] ",
                tree.level, tree.boundry.getxMin(), tree.boundry.getyMin(),
                tree.boundry.getxMax(), tree.boundry.getyMax());

        for (Node node : tree.nodes) {
            System.out.printf(" \n\t  x=%d y=%d", node.x, node.y);
        }
        if (tree.nodes.size() == 0) {
            System.out.printf(" \n\t  Leaf Node.");
        }
        dfs(tree.northWest);
        dfs(tree.northEast);
        dfs(tree.southWest);
        dfs(tree.southEast);

    }

    void split() {
        int xOffset = this.boundry.getxMin()
                + (this.boundry.getxMax() - this.boundry.getxMin()) / 2;
        int yOffset = this.boundry.getyMin()
                + (this.boundry.getyMax() - this.boundry.getyMin()) / 2;

        northWest = new QuadTree(this.level + 1, new Boundry(
                this.boundry.getxMin(), this.boundry.getyMin(), xOffset,
                yOffset));
        northEast = new QuadTree(this.level + 1, new Boundry(xOffset,
                this.boundry.getyMin(), xOffset, yOffset));
        southWest = new QuadTree(this.level + 1, new Boundry(
                this.boundry.getxMin(), xOffset, xOffset,
                this.boundry.getyMax()));
        southEast = new QuadTree(this.level + 1, new Boundry(xOffset, yOffset,
                this.boundry.getxMax(), this.boundry.getyMax()));

    }

    void insert(int x, int y, int value) {
        if (!this.boundry.inRange(x, y)) {
            return;
        }

        Node node = new Node(x, y, value);
        if (nodes.size() < MAX_CAPACITY) {
            for (Node node2 : this.nodes) {
            if ((node2.x == node.x&& node2.y == node.y) &&(node2.name== node.name) ){
                return;
            }else if(node2.x == node.x&& node2.y == node.y)
            {
                nodes.add(node);
                this.MAX_CAPACITY++;
                return;
            }
            }
            nodes.add(node);
            return;
        }
        // Exceeded the capacity so split it in FOUR
        if (!this.isDivided ) {
            split();
            isDivided = true;
        }
 // Check coordinates belongs to which partition


                if (this.northWest.boundry.inRange(x, y))
                    this.northWest.insert(x, y, value);
                else if (this.northEast.boundry.inRange(x, y))
                    this.northEast.insert(x, y, value);
                else if (this.southWest.boundry.inRange(x, y))
                    this.southWest.insert(x, y, value);
                else if (this.southEast.boundry.inRange(x, y))
                    this.southEast.insert(x, y, value);
                else
                    System.out.printf("ERROR : Unhandled partition %d %d", x, y);

    }

    List<Node>  query(Boundry boundry){
    List<Node> found = new ArrayList<>();
        if ( !this.boundry.intersect(boundry) ){
            return found;
        }else {
       for (Node node : this.nodes) {
       if (boundry.inRange(node.x,node.y)){
            found.add(node);
          /* System.out.printf("\n\t  XXX %d YYYY %d", node.x,node.y);
           System.out.printf("\n\t  BBX %d BBY %d", this.boundry.getxMax(),this.boundry.getyMax());
*/
       }
       }}

    if (isDivided) {
        found.addAll(this.northWest.query(boundry));
        found.addAll(this.northEast.query(boundry));
        found.addAll(this.southWest.query(boundry));
        found.addAll(this.southEast.query(boundry));

    }
         return found;
    }
}
class main{
    public static void main(String args[]) {
        QuadTree anySpace = new QuadTree(1, new Boundry(0, 0, 1000, 1000));
        anySpace.insert(100, 100, 1);
        anySpace.insert(100, 100, 5);
        anySpace.insert(100, 100, 6);
        anySpace.insert(700, 600, 1);
        anySpace.insert(800, 600, 1);
        anySpace.insert(900, 600, 1);
        anySpace.insert(510, 610, 1);
        anySpace.insert(520, 620, 1);
        anySpace.insert(530, 630, 1);
        anySpace.insert(540, 640, 1);
        anySpace.insert(550, 650, 1);
        anySpace.insert(555, 655, 1);
        anySpace.insert(560, 660, 1);
        for (Node node : anySpace.query(new Boundry(0,0,600,600))) {
            System.out.printf(" \n\t  x=%d y=%d", node.x, node.y);
        }

QuadTree.dfs(anySpace);
    /*  for (Node node : anySpace.query(new Boundry(0,0,500,500))) {
            System.out.printf(" \n\t  x=%d y=%d", node.x, node.y);
        }
*/
    }
}