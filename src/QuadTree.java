import java.util.ArrayList;
import java.util.List;

class QuadTree {
    int MAX_CAPACITY = 3;
    int level = 0;
    int numberofDubPoints = 0;
    List<Node> nodes;
    QuadTree northWest = null;
    QuadTree northEast = null;
    QuadTree southWest = null;
    QuadTree southEast = null;
    Boundry boundry;
    boolean isDivided = false;
    static int visitedNodes = 0;
    static int size ;


    QuadTree(int level, Boundry boundry) {
        this.level = level;
        nodes = new ArrayList<Node>();
        this.boundry = boundry;
    }

    /* Traveling the Graph using Depth First Search*/
    static void dfs(QuadTree tree) {

        String space = "";
        if (tree == null)
            return;
        for (int i = 0; i < tree.level - 1; i++) {
            space += "  ";
        }
        System.out.printf("\n" + space + "Node at %d, %d, %d: ",
                tree.boundry.getxMin(), tree.boundry.getyMin(),
                tree.boundry.getxMax()-tree.boundry.getxMin());

        if (tree.isDivided) {

            System.out.printf("Internal");

        }

        for (Node node : tree.nodes) {
            System.out.printf("\n");

            System.out.printf(space + "(" + node.name + ", %d, %d)", node.x, node.y);

        }

        if (tree.nodes.size() == 0 && !tree.isDivided) {
            System.out.printf("Empty");

        }

        dfs(tree.northWest);
        dfs(tree.northEast);
        dfs(tree.southWest);
        dfs(tree.southEast);

    }

    void remove(int x, int y) {
        if (this.boundry.inRange(x, y)) {
            if (this.isDivided){
                this.northWest.remove(x, y);
                this.northEast.remove(x, y);
                this.southWest.remove(x, y);
                this.southEast.remove(x, y);
            }else {
                for (Node node : this.nodes) {
                    if (x == node.x && node.y == y) {
                        if (this.nodes.size() == 1) {
                            this.nodes.remove(node);
                            if (node.isDublicated) {
                                this.MAX_CAPACITY--;
                                this.numberofDubPoints--;
                            }
                            return;

                        } else {

                            if (this.numberofDubPoints > 0) {

                                this.MAX_CAPACITY--;
                                this.numberofDubPoints--;
                                this.nodes.remove(node);

                                return;


                            }

                            this.nodes.remove(node);

                            //rearrange(this);

                            return;

                        }
                    } else {
                        return;
                    }


                }}}}
    void split() {
        QuadTree.size += 4;
        int xValue = (this.boundry.getxMax() - this.boundry.getxMin()) / 2;
        int yValue = (this.boundry.getyMax() - this.boundry.getyMin()) / 2;
        int xMin=this.boundry.getxMin();
        int xMax=this.boundry.getxMax();
        int yMin=this.boundry.getyMin();
        int yMax=this.boundry.getyMax();


        northWest = new QuadTree(this.level + 1, new Boundry(
                xMin,
                yMin + yValue,
                xMin + xValue,
                yMax));
        northEast = new QuadTree(this.level + 1, new Boundry(
                xMin + xValue,
                yMin + yValue,
                xMax,
                yMax));

        southWest = new QuadTree(this.level + 1, new Boundry(
                xMin,
                yMin,
                xMin + xValue,
                yMin + yValue));
        southEast = new QuadTree(this.level + 1, new Boundry(
                xMin + xValue,
                yMin,
                xMax,
                yMin + yValue));


    }
    void insert(int x, int y, String value) {
        if (!this.boundry.inRange(x, y)) {
            return;
        }

        Node node = new Node(x, y, value);
        if (!this.isDivided) {
            //if (nodes.size() < MAX_CAPACITY) {
            if (nodes.size() < MAX_CAPACITY) {
                for (Node node2 : this.nodes) {
                    if ((node2.x == node.x && node2.y == node.y)
                            && (node2.name == node.name)
                    ) {

                        return;

                    } else if (node2.x == node.x && node2.y == node.y) {
                        node.isDublicated = true;
                        node2.isDublicated = true;

                        nodes.add(node);
                        this.MAX_CAPACITY++;
                        this.numberofDubPoints++;

                        return;
                    }
                }
                nodes.add(node);
                return;
            }
            else{
                split();

                for (Node nod : this.nodes){
                    if (this.northWest.boundry.inRange(nod.x, nod.y))
                        this.northWest.insert(nod.x, nod.y, nod.name);
                    else if (this.northEast.boundry.inRange(nod.x, nod.y))
                        this.northEast.insert(nod.x, nod.y, nod.name);
                    else if (this.southWest.boundry.inRange(nod.x, nod.y))
                        this.southWest.insert(nod.x, nod.y, nod.name);
                    else if (this.southEast.boundry.inRange(nod.x, nod.y))
                        this.southEast.insert(nod.x, nod.y, nod.name);

                }
                if (this.northWest.boundry.inRange(x,y))
                    this.northWest.insert(x, y,value);
                else if (this.northEast.boundry.inRange(x, y))
                    this.northEast.insert(x, y, value);
                else if (this.southWest.boundry.inRange(x, y))
                    this.southWest.insert(x, y, value);
                else if (this.southEast.boundry.inRange(x, y))
                    this.southEast.insert(x, y, value);
                isDivided=true;
                nodes.removeAll(nodes);
            }
        } else {

            if (this.northWest.boundry.inRange(x, y))
                this.northWest.insert(x, y, value);
            else if (this.northEast.boundry.inRange(x, y))
                this.northEast.insert(x, y, value);
            else if (this.southWest.boundry.inRange(x, y))
                this.southWest.insert(x, y, value);
            else if (this.southEast.boundry.inRange(x, y))
                this.southEast.insert(x, y, value);
            else
                System.out.printf("Point REJECTED: ("+value+", %d, %d)\n",x,y);
        }
        this.nodes.removeAll(this.nodes);
        isDivided = true;
    }
    List<Node> duplicates(){

        List<Node> found = new ArrayList<>();

        if (this.numberofDubPoints>0){
            for (Node node : this.nodes) {
                if (   node.isDublicated) {
                    found.add(node);
                }
            }
        }
        if (isDivided) {
            found.addAll(this.northWest.duplicates());
            found.addAll(this.northEast.duplicates());
            found.addAll(this.southWest.duplicates());
            found.addAll(this.southEast.duplicates());
        }

        return found;
    }

    Node  search(int x, int y){
        if (this.boundry.inRange(x,y)){
            for (Node node : this.nodes) {
                if(node.x == x && node.y==y){
                    return node;
                }

            }
        }

        if (isDivided) {
            this.northWest.search(x,y);
            this.northEast.search(x,y);
            this.southWest.search(x,y);
            this.southEast.search(x,y);

        }
        return null;
    }
    List<Node>  regionsearch(int x,int y,int w,int h){
        Boundry boundry = new Boundry(x,y,w,h);
        List<Node> found = new ArrayList<>();
        QuadTree.visitedNodes ++;
        if ( !this.boundry.intersect(boundry) ){
            return found;
        }else {
            for (Node node : this.nodes) {
                if (boundry.inRange(node.x,node.y)){
                    found.add(node);
                }
            }}


        if(this.northWest != null)found.addAll(this.northWest.regionsearch(x,y,w,h));
        if(this.northEast != null)found.addAll(this.northEast.regionsearch(x,y,w,h));
        if(this.southWest != null)found.addAll(this.southWest.regionsearch(x,y,w,h));
        if(this.southEast != null)found.addAll(this.southEast.regionsearch(x,y,w,h));

        return found;
    }

}
