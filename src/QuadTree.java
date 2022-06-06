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
    QuadTree parent;


    QuadTree(int level, Boundry boundry,QuadTree parent) {
        this.level = level;
        this.parent=parent;
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
        System.out.print("" + space + "Node at "+ tree.boundry.getxMin() +", "+tree.boundry.getyMin() +", "+(tree.boundry.getxMax() - tree.boundry.getxMin())+": ");



        for (Node node : tree.nodes) {
            System.out.printf("\n");

            System.out.print(space + "(" + node.name + ", "+node.x+", "+node.y+")");

        }
        if (tree.isDivided) {

            System.out.println("Internal");

        }else if (tree.nodes.size() == 0 ) {
            System.out.println("Empty");

        }else{
            System.out.printf("\n");

        }


        if (tree.isDivided) {
            QuadTree.size += 4;
            dfs(tree.southWest);
            dfs(tree.southEast);
        dfs(tree.northWest);
        dfs(tree.northEast);
    }

    }
    void merge(QuadTree tree){
        if (tree.northWest.nodes.size()
                +tree.northEast.nodes.size()
                +tree.southWest.nodes.size()
                +tree.southEast.nodes.size()>=tree.MAX_CAPACITY){
            List<Node> treeNodes = tree.northWest.nodes;
            //if (tree.northWest.nodes!= null) treeNodes.addAll(tree.northWest.nodes);
            if (tree.northEast.nodes!= null) treeNodes.addAll(tree.northEast.nodes);
            if (tree.southWest.nodes!= null) treeNodes.addAll(tree.southWest.nodes);
            if (tree.southEast.nodes!= null) treeNodes.addAll(tree.southEast.nodes);

            tree.isDivided=false;
            for(Node node:treeNodes){
                tree.insert(node.x,node.y,node.name);
            }
            tree.northEast.nodes= null;
            tree.southWest.nodes= null;
            tree.southEast.nodes= null;
            tree.northWest.nodes =null;

            if (tree.parent!= null)merge(tree.parent);

        }else{
            return;
        }
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
                      /*  if (this.nodes.size() == 1) {
                            this.nodes.remove(node);
                            if (node.isDublicated) {
                             //   this.MAX_CAPACITY--;
                                this.numberofDubPoints--;
                            }
                            return;

                        } else {

                            if (this.numberofDubPoints > 0) {

                               // this.MAX_CAPACITY--;
                                this.numberofDubPoints--;
                                this.nodes.remove(node);

                                return;


                            }*/

                            this.nodes.remove(node);


                             if (this.parent!= null) merge(this.parent);
                            return;

                        }
                    }


                }
        }else {
            return;
        }
    }
    void split() {
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
                yMax),this);
        northEast = new QuadTree(this.level + 1, new Boundry(
                xMin + xValue,
                yMin + yValue,
                xMax,
                yMax),this);

        southWest = new QuadTree(this.level + 1, new Boundry(
                xMin,
                yMin,
                xMin + xValue,
                yMin + yValue),this);
        southEast = new QuadTree(this.level + 1, new Boundry(
                xMin + xValue,
                yMin,
                xMax,
                yMin + yValue),this);


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
                        //this.MAX_CAPACITY++;
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

            if (isDivided) {
            if (this.northWest.search(x,y)!=null) return this.northWest.search(x,y);
            if (this.northEast.search(x,y)!=null) return this.northEast.search(x,y);
            if (this.southWest.search(x,y)!=null) return this.southWest.search(x,y);
            if (this.southEast.search(x,y)!=null) return this.southWest.search(x,y);

        }else {
            for (Node node : this.nodes) {
                if(node.x == x && node.y==y){

                    return node;
                }

            }
        }
        }
            return null;

    }
    List<Node>  regionsearch(int x,int y,int w,int h){
        Boundry boundry = new Boundry(x,y,w,h);
        List<Node> found = new ArrayList<>();
        QuadTree.visitedNodes ++;

if (this.isDivided){
    if(this.northWest.boundry.intersect(boundry))found.addAll(this.northWest.regionsearch(x,y,w,h));
    if(this.northEast.boundry.intersect(boundry))found.addAll(this.northEast.regionsearch(x,y,w,h));
    if(this.southWest.boundry.intersect(boundry))found.addAll(this.southWest.regionsearch(x,y,w,h));
    if(this.southEast.boundry.intersect(boundry))found.addAll(this.southEast.regionsearch(x,y,w,h));

}else {
    if ( !this.boundry.intersect(boundry) ){
        return found;
    }else {
        for (Node node : this.nodes) {
            if (boundry.inRange(node.x,node.y)){
                found.add(node);
            }
        }}

}

        return found;
    }

}
