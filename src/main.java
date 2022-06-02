import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Node {
    int x, y;
    String name;
boolean isDublicated= false;

    Node(int x, int y, String value) {
        this.x = x;
        this.y = y;
        this.name = value; /* some data*/
    }
}

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
        return(    (boundry.getxMin() >= this.getxMin() && boundry.getxMin() <= this.getxMax()
                && boundry.getyMin() >= this.getyMin() && boundry.getyMin() <= this.getyMax())


        ||

                (boundry.getxMin() >= this.getxMin() && boundry.getxMin() <= this.getxMax()
                && boundry.getyMax() >= this.getyMin() && boundry.getyMax() <= this.getyMax())


        ||
                (   boundry.getxMax() >= this.getxMin() && boundry.getxMax() <= this.getxMax()
                && boundry.getyMin() >= this.getyMin() && boundry.getyMin() <= this.getyMax())


        ||

                (boundry.getxMax() >= this.getxMin() && boundry.getxMax() <= this.getxMax()
                && boundry.getyMax() >= this.getyMin() && boundry.getyMax() <= this.getyMax())

        ||

                (boundry.getxMin() <= this.getxMin() && boundry.getxMax() >= this.getxMax()
                        && boundry.getyMin() <= this.getyMin() && boundry.getyMax() >= this.getyMax())


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
     int size;

     QuadTree(int level, Boundry boundry) {
         this.level = level;
         size = 1;
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
         //System.out.printf("QuadTree Size: %d QuadTree Nodes Printed.",size);

     }

     void rearrange(QuadTree tree) {
         List<Node> removed = new ArrayList<>();
         if (tree.northWest != null && tree.northWest.nodes.size() > 0) {
             for (Node node : tree.northWest.nodes) {
                 if (tree.MAX_CAPACITY > tree.nodes.size()) {
                     tree.nodes.add(node);
                     if (node.isDublicated && tree.northWest.numberofDubPoints > 0) {
                         tree.MAX_CAPACITY++;
                         tree.numberofDubPoints++;
                         tree.northWest.numberofDubPoints--;
                         tree.northWest.MAX_CAPACITY--;
                     }
                     removed.add(node);
                     //tree.northWest.nodes.remove(node);

                 }
             }
             for (Node node : removed) {
                 tree.northWest.nodes.remove(node);

             }

             //tree.MAX_CAPACITY += numberofDubPoints-1;
             //tree.northWest.MAX_CAPACITY -=tree.northWest.numberofDubPoints+1;
      /*  if(tree.southEast.nodes.size()==0
        &&tree.southWest.nodes.size()==0
        &&tree.northEast.nodes.size()==0
        &&tree.southWest.nodes.size()==0
        ){
            tree.southEast=null;
                    tree.southWest=null;
                    tree.northEast=null;
                    tree.southWest=null;

        }*//*if(b){    tree.MAX_CAPACITY --;
                tree.numberofDubPoints--;
                tree.northWest.numberofDubPoints ++;
                tree.northWest.MAX_CAPACITY ++;
            }*/
             rearrange(tree.northWest);

             return;
         } else if (tree.northEast != null && tree.northEast.nodes.size() > 0) {
             for (Node node : tree.northEast.nodes) {
                 if (tree.MAX_CAPACITY > tree.nodes.size()) {
                     tree.nodes.add(node);
                     if (node.isDublicated && tree.northEast.numberofDubPoints > 0) {
                         tree.MAX_CAPACITY++;
                         tree.numberofDubPoints++;

                         tree.northEast.numberofDubPoints--;
                         tree.northEast.MAX_CAPACITY--;
                     }
                     removed.add(node);
                     //tree.northEast.nodes.remove(node);

                 }

             }
             for (Node node : removed) {
                 tree.northEast.nodes.remove(node);

             }
             //tree.MAX_CAPACITY +=tree.numberofDubPoints-1;
             //tree.northEast.MAX_CAPACITY -=tree.northWest.numberofDubPoints+1;
          /*  if(tree.southEast.nodes.size()==0
                    &&tree.southWest.nodes.size()==0
                    &&tree.northEast.nodes.size()==0
                    &&tree.southWest.nodes.size()==0
            ){
                tree.southEast=null;
                tree.southWest=null;
                tree.northEast=null;
                tree.southWest=null;

            }*/
           /* if(b){    tree.MAX_CAPACITY --;
                tree.numberofDubPoints--;
                tree.northEast.numberofDubPoints ++;
                tree.northEast.MAX_CAPACITY ++;
            }*/
             rearrange(tree.northEast);

             return;
         } else if (tree.southWest != null && tree.southWest.nodes.size() > 0) {
             for (Node node : tree.southWest.nodes) {
                 if (tree.MAX_CAPACITY > tree.nodes.size()) {
                     tree.nodes.add(node);
                     if (node.isDublicated && tree.southWest.numberofDubPoints > 0) {
                         tree.MAX_CAPACITY++;

                         tree.numberofDubPoints++;

                         tree.southWest.numberofDubPoints--;
                         tree.southWest.MAX_CAPACITY--;
                     }
                     removed.add(node);
                     //tree.southWest.nodes.remove(node);
                 }
             }
             for (Node nod : removed) {
                 tree.southWest.nodes.remove(nod);

             }

             //tree.MAX_CAPACITY +=tree.numberofDubPoints-1;
             //tree.southWest.MAX_CAPACITY -=tree.southWest.numberofDubPoints+1;
            /*if(tree.southEast.nodes.size()==0
                    &&tree.southWest.nodes.size()==0
                    &&tree.northEast.nodes.size()==0
                    &&tree.southWest.nodes.size()==0
            ){
                tree.southEast=null;
                tree.southWest=null;
                tree.northEast=null;
                tree.southWest=null;

            }*/
             rearrange(tree.southWest);

             return;
         } else if (tree.southEast != null && tree.southEast.nodes.size() > 0) {
             for (Node node : tree.southEast.nodes) {
                 if (tree.MAX_CAPACITY > tree.nodes.size()) {
                     tree.nodes.add(node);
                     if (node.isDublicated && tree.southEast.numberofDubPoints > 0) {
                         tree.MAX_CAPACITY++;

                         tree.numberofDubPoints++;

                         tree.southEast.numberofDubPoints--;
                         tree.southEast.MAX_CAPACITY--;
                     }
                     removed.add(node);
                     //tree.southEast.nodes.remove(node);

                 }
             }
             for (Node nod : removed) {
                 tree.southEast.nodes.remove(nod);

             }

             //tree.MAX_CAPACITY +=tree.numberofDubPoints-1;
             //tree.southEast.MAX_CAPACITY -=tree.southEast.numberofDubPoints+1;
            /*if(tree.southEast.nodes.size()==0
                    &&tree.southWest.nodes.size()==0
                    &&tree.northEast.nodes.size()==0
                    &&tree.southWest.nodes.size()==0
            ){
                tree.southEast=null;
                tree.southWest=null;
                tree.northEast=null;
                tree.southWest=null;

            }*/
             rearrange(tree.southEast);

             return;
         }

     }

     void remove(String name) {
         boolean isDeleted = false;
         for (Node node : this.nodes) {
             //if (  name.equals(node.name))
             if (name.equals(node.name)) {
                 if (this.nodes.size() == 1) {
                     this.nodes.remove(node);
                     if (node.isDublicated) {
                         this.MAX_CAPACITY--;
                         this.numberofDubPoints--;
                     }
                     return;

                 } else {

                     if (node.isDublicated && this.numberofDubPoints > 0) {

                         this.MAX_CAPACITY--;
                         this.numberofDubPoints--;
                         this.nodes.remove(node);

                         isDeleted = true;
                         return;


                     }

                     this.nodes.remove(node);

                     rearrange(this);

                     return;
                 }
             } else if (isDivided && !isDeleted) {

                 this.northWest.remove(name);
                 isDeleted = true;
                 this.northEast.remove(name);
                 isDeleted = true;
                 this.southWest.remove(name);
                 isDeleted = true;
                 this.southEast.remove(name);
             }
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
     void removeOLD(int x, int y) {
         if (this.boundry.inRange(x, y)) {

             boolean isDeleted = false;
             for (Node node : this.nodes) {
                 //if (  name.equals(node.name))
                 if (x == node.x && node.y == y) {
                     if (this.nodes.size() == 1) {
                         this.nodes.remove(node);
                         if (node.isDublicated) {
                             this.MAX_CAPACITY--;
                             this.numberofDubPoints--;
                         }
                         return;

                     } else {

                         if (node.isDublicated && this.numberofDubPoints > 0) {

                             this.MAX_CAPACITY--;
                             this.numberofDubPoints--;
                             this.nodes.remove(node);

                             isDeleted = true;
                             return;


                         }

                         this.nodes.remove(node);

                         rearrange(this);

                         return;
                     }
                 } else if (isDivided && !isDeleted) {

                     this.northWest.remove(x, y);
                     isDeleted = true;
                     this.northEast.remove(x, y);
                     isDeleted = true;
                     this.southWest.remove(x, y);
                     isDeleted = true;
                     this.southEast.remove(x, y);
                 }
             }
         }
     }






     /*void remove(int x,int y) {
 if (this.boundry.inRange(x,y)){
     for (Node node : this.nodes) {
         if (x == node.x&& node.y==y) {
             System.out.println(" found");

             if (this.nodes.size() == 1 && !isDivided) {
                     this.nodes.remove(node);

                     if (node.isDublicated ) {
                         this.MAX_CAPACITY--;
                         this.numberofDubPoints--;
                     }

                     return;

                 } else {

                     if (node.isDublicated && this.numberofDubPoints>0) {

                         this.MAX_CAPACITY--;
                         this.numberofDubPoints--;
                         this.nodes.remove(node);

                         return;


                     }

                     this.nodes.remove(node);
                     rearrange(this);
                     return;
                 }
         }else{
             System.out.println("not found");
         }
     }
             } else if (this.isDivided  ) {

/*     else if (isDivided && !isDeleted &&(this.northWest.boundry.inRange(x,y)||
             this.northEast.boundry.inRange(x,y)||this.southWest.boundry.inRange(x,y)||
             this.southEast.boundry.inRange(x,y))) {

                 this.northWest.remove(x,y);
                 this.northEast.remove(x,y);
                 this.southWest.remove(x,y);
                 this.southEast.remove(x,y);

         }
     }
*/
     void split() {
         size = size * 4;
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


       /* int xOffset = this.boundry.getxMin()
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
*/
     }

     void insertOLD(int x, int y, String value) {
         if (!this.boundry.inRange(x, y)) {
             return;
         }

         Node node = new Node(x, y, value);
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
         // Exceeded the capacity so split it in FOUR
         if (!this.isDivided) {
             split();
             isDivided = true;
         }


         if (this.northWest.boundry.inRange(x, y))
             this.northWest.insert(x, y, value);
         else if (this.northEast.boundry.inRange(x, y))
             this.northEast.insert(x, y, value);
         else if (this.southWest.boundry.inRange(x, y))
             this.southWest.insert(x, y, value);
         else if (this.southEast.boundry.inRange(x, y))
             this.southEast.insert(x, y, value);
         else
             System.out.printf("Point REJECTED: (" + value + ", %d, %d)\n", x, y);

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






     List<Node> search(String name){
         List<Node> found = new ArrayList<>();
    for (Node node : this.nodes) {
        if (  name.equals(node.name)) {
            found.add(node);
        }
    }
         if (isDivided) {
             found.addAll(this.northWest.search(name));
             found.addAll(this.northEast.search(name));
             found.addAll(this.southWest.search(name));
             found.addAll(this.southEast.search(name));
         }

    return found;
    }


     List<Node> duplicates(){

        List<Node> found = new ArrayList<>();

        if (this.numberofDubPoints>0){
            for (Node node : this.nodes) {
                //if (  name.equals(node.name))
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

        //  System.out.printf("\n\t  [Xmin %d Ymin %d]   [Xmax %d Ymax %d]"+ this.boundry.intersect(boundry), x,y,w,h);
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


        if(this.northWest != null)found.addAll(this.northWest.regionsearch(x,y,w,h));
        if(this.northEast != null)found.addAll(this.northEast.regionsearch(x,y,w,h));
        if(this.southWest != null)found.addAll(this.southWest.regionsearch(x,y,w,h));
        if(this.southEast != null)found.addAll(this.southEast.regionsearch(x,y,w,h));

         return found;
    }
void dump(){

    for (Node node : this.nodes) {
        for (int i=0;i<this.level-1;i++){
            System.out.printf("  ");
        }
        System.out.printf("x=%d y=%d level=%d\n", node.x, node.y,this.level);
    }

   if (this.northWest != null){this.northWest.dump();}
   if (this.northEast != null){this.northEast.dump();}
   if (this.southEast != null){this.southEast.dump();}
   if (this.southWest != null){ this.southWest.dump();}


}

     }

class main{
    public static void main(String args[]) throws IOException {
        Boundry boundry=new Boundry(0, 0, 1024, 1024);
        QuadTree anySpace = new QuadTree(1,boundry );
        SkipList list=new SkipList();


        File file = new File("F:\\college\\third_year\\second semester\\algorithms\\project test cases\\P2test2.txt");


        BufferedReader br
                = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null){

            String[] splitStr = st.split("\\s+");

            for(int i=0;i<splitStr.length;i++){
                int x;
                int y;
                String name;
                switch (splitStr[i]){
                    case "insert":
                        x=Integer.parseInt(splitStr[i+2]);
                        y=Integer.parseInt(splitStr[i+3]);
                        name=splitStr[i+1];
                        if (boundry.inRange(x,y)&& (Character.isAlphabetic( name.charAt(0) )|| name.charAt(0) == '_')){
                            anySpace.insert(x,y,name);
                            list.insert(name,x,y);

                            System.out.printf("Point Inserted: ("+name+", %d, %d)\n",x,y);
                        }else{
                            System.out.printf("Point REJECTED: ("+name+", %d, %d)\n",x,y);
                        }
                        //System.out.println("insert"+splitStr[i+1]+splitStr[i+2]+splitStr[i+3]);
                        break;
                    case "duplicates":
                        System.out.print("\nDuplicate Points:");
                        Node temp=null;
                        for(Node node:anySpace.duplicates()){
                            if (temp==null||!( temp.x==node.x && temp.y==node.y)){
                                temp=node;
                                System.out.printf("\n(%d, %d)",node.x,node.y);

                            }

                        }
                        break;
                    case "dump":
                        list.dump();
                        System.out.print("\nQuadTree Dump:");
                        QuadTree.dfs(anySpace);
                        System.out.printf("\nQuadTree Size: %d QuadTree Nodes Printed.",anySpace.size);
                        break;
                    case "search":
                        if (list.find(splitStr[i+1])!=null){
                        System.out.printf("\nPoint Found ("+list.find(splitStr[i+1]).getName()+", %d, %d)"
                                ,list.find(splitStr[i+1]).getx(),
                                list.find(splitStr[i+1]).gety());}else{
                            System.out.println("\nPoint Not Found: "+splitStr[i+1]);

                        }
                        break;
                    case "remove":
                        boolean t=true;
                        try {
                            int d = Integer.parseInt(splitStr[i+1]);
                        } catch (NumberFormatException nfe) {
                            t= false;
                        }


                        if (t ){

                            x=Integer.parseInt(splitStr[i+1]);
                             y=Integer.parseInt(splitStr[i+2]);
                            if (boundry.inRange(x,y)){

                                Node found=anySpace.search(x,y);
                                if (found!=null) {
                                    anySpace.remove(x,y);
                                    list.removeAt(found.name);
                                }else{
                                    System.out.printf("\npoint Not Found: (%d, %d)",x,y);
                                }
                            }else{
                                System.out.printf("Point Rejected: (%d, %d)",x,y);

                            }

                            //System.out.println("remove point by value"+splitStr[i+1]+splitStr[i+2]);

                        }else{
                            name=splitStr[i+1];
                            SkipNode found =list.find(name);
                            if (found != null){

                                x=list.find(splitStr[i+1]).getx();
                                y=list.find(splitStr[i+1]).gety();
                                list.removeAt(name);
                                anySpace.remove(x,y);
                            }else{
                                System.out.println("point Not Removed: "+name);

                            }


                            //System.out.println("remove point by name"+splitStr[i+1]);

                        }
                        break;
                    case "regionsearch":
                        QuadTree.visitedNodes=0;
                        x=Integer.parseInt(splitStr[i+1]);
                        y=Integer.parseInt(splitStr[i+2]);
                        int h=Integer.parseInt(splitStr[i+3]);
                        int w=Integer.parseInt(splitStr[i+4]);

                        if (x<= h && y<=w){
                           List<Node> found= anySpace.regionsearch(x, y, h, w);
                            System.out.printf("\nPoints Intersecting Region: (%d, %d, %d, %d)",x,y,h,w);
                            for (Node node: found){
                                System.out.printf("\nPoint Found: ("+node.name+", %d, %d)",node.x,node.y);

                            }

                            System.out.printf("\n%d QuadTree Nodes Visited",QuadTree.visitedNodes);


                        }else{
                            System.out.printf("\nRectangle Rejected: (%d, %d, %d, %d)",x,y,h,w);
                        }
                        break;


                }
            }

        }

        /*

        anySpace.insert(100, 100, "a");
        anySpace.insert(100, 101, "b");
        anySpace.insert(100, -12, "c");
        anySpace.insert(700, 600, "d");
        anySpace.insert(800, 600, "d");
        anySpace.insert(800, 600, "dd");
        anySpace.insert(800, 600, "ddd");
        anySpace.insert(800, 600, "dddd");
        anySpace.insert(800, 600, "adddd");
        anySpace.insert(800, 600, "sdddd");
        anySpace.insert(800, 600, "cdddd");
        anySpace.insert(800, 600, "fdddd");
        anySpace.insert(800, 600, "edddd");

        anySpace.insert(900, 600, "f");
        anySpace.insert(510, 610, "g");
        anySpace.insert(520, 620, "h");
        anySpace.insert(530, 630, "j");
        anySpace.insert(540, 640, "k");
        anySpace.insert(550, 650, "l");
        anySpace.insert(550, 650, "lw");
        anySpace.insert(550, 650, "ls");
        anySpace.insert(550, 650, "ld");
        anySpace.insert(550, 650, "lr");
        anySpace.insert(550, 650, "lt");
        anySpace.insert(555, 655, "ih");
        anySpace.insert(561, 660, "yk");
        anySpace.insert(562, 660, "_14");
        anySpace.insert(-50, 660, "name");
        anySpace.insert(564, 660, "e");
        anySpace.insert(565, 660, "w");
        anySpace.insert(566, 660, "e");
        anySpace.insert(567, 660, "q");
        anySpace.insert(568, 660, "d");
        anySpace.insert(569, 660, "rt");
        anySpace.insert(570, 660, "yuhhg");

        anySpace.insert(571, 660, "23");
        anySpace.insert(572, 660, "24");
        anySpace.insert(573, 660, "25");
*/

        // anySpace.dump();
       // QuadTree.dfs(anySpace);



        /*





        list.insert("a",100, 1 );
        list.insert("b",100, 2);
        list.insert("c",100, 3);
        list.insert("d",700, 4);
        list.insert("d",800, 5);
        list.insert( "f",900, 6);
        list.insert("g",510, 7);
        list.insert("h",520, 8);
        list.insert("j",530, 9);
        list.insert("k",540, 10);
        list.insert("l",550, 11);
        list.insert("o",555, 12);
        list.insert("p",561, 13);
        list.insert("y",562, 14);
        list.insert("t",-50, 15);
        list.insert("r",564, 16);
        list.insert("e",565, 17);
        //QuadTree.dfs(anySpace);

        File file = new File("F:\\college\\third_year\\second semester\\algorithms\\project test cases\\P2test1.txt");


        BufferedReader br
                = new BufferedReader(new FileReader(file));
String st;
        while ((st = br.readLine()) != null){

        String[] splitStr = st.split("\\s+");

        for(int i=0;i<splitStr.length;i++){
            switch (splitStr[i]){
                case "insert":
                    System.out.println("insert"+splitStr[i+1]+splitStr[i+2]+splitStr[i+3]);
                    break;
                case "duplicates":
                    System.out.println("duplicates");
                    break;
                case "dump":
                    System.out.println("dump");
                    break;
                case "search":
                    System.out.println("search"+splitStr[i+1]);
                    break;
                case "remove":
                    boolean t=true;
                    try {
                        int d = Integer.parseInt(splitStr[i+1]);
                    } catch (NumberFormatException nfe) {
                        t= false;
                    }


                    if (t ){
                        System.out.println("remove point by value"+splitStr[i+1]+splitStr[i+2]);
                    }else{
                        System.out.println("remove point by name"+splitStr[i+1]);
                    }
                    break;
                case "regionsearch":
                    System.out.println("regionsearch"+splitStr[i+1]+splitStr[i+2]+splitStr[i+3]+splitStr[i+4]);
                    break;


            }
        }

        }



QuadTree.dfs(anySpace);
 for (Node node : anySpace.regionsearch(500,500,1000,1000)) {
            System.out.printf(" \n\t  x=%d y=%d", node.x, node.y);
        }
        anySpace.remove(510,610);
        anySpace.remove(520,620);
        anySpace.remove(530,630);
        anySpace.remove(540,640);

        QuadTree.dfs(anySpace);

    for (Node node : anySpace.search(1)) {
            System.out.printf(" \n\t  x=%d y=%d", node.x, node.y);
        }
    for (Node node : anySpace.query(new Boundry(0,0,500,500))) {
            System.out.printf(" \n\t  x=%d y=%d", node.x, node.y);
        }

        QuadTree.dfs(anySpace);

        anySpace.remove("1");
        anySpace.remove("1");
        anySpace.remove(530,630);
        anySpace.remove(540,640);

        QuadTree.dfs(anySpace);

for (Node node : anySpace.duplicates()) {
            System.out.printf(" \n\t  x=%d y=%d   value="+node.name, node.x, node.y);
        }








        anySpace.insert(100, 100, "a");
        anySpace.insert(100, 101, "b");
        anySpace.insert(100, -12, "c");
        anySpace.insert(700, 600, "d");
        anySpace.insert(800, 600, "d");
        anySpace.insert(800, 600, "dd");
        anySpace.insert(800, 600, "ddd");
        anySpace.insert(800, 600, "dddd");
        anySpace.insert(800, 600, "adddd");
        anySpace.insert(800, 600, "sdddd");
        anySpace.insert(800, 600, "cdddd");
        anySpace.insert(800, 600, "fdddd");
        anySpace.insert(800, 600, "edddd");

        anySpace.insert(900, 600, "f");
        anySpace.insert(510, 610, "g");
        anySpace.insert(520, 620, "h");
        anySpace.insert(530, 630, "j");
        anySpace.insert(540, 640, "k");
        anySpace.insert(550, 650, "l");
        anySpace.insert(550, 650, "lw");
        anySpace.insert(550, 650, "ls");
        anySpace.insert(550, 650, "ld");
        anySpace.insert(550, 650, "lr");
        anySpace.insert(550, 650, "lt");
        anySpace.insert(555, 655, "ih");
        anySpace.insert(561, 660, "yk");
        anySpace.insert(562, 660, "_14");
        anySpace.insert(-50, 660, "name");
        anySpace.insert(564, 660, "e");
        anySpace.insert(565, 660, "w");
        anySpace.insert(566, 660, "e");
        anySpace.insert(567, 660, "q");
        anySpace.insert(568, 660, "d");
        anySpace.insert(569, 660, "rt");
        anySpace.insert(570, 660, "yuhhg");

        anySpace.insert(571, 660, "23");
        anySpace.insert(572, 660, "24");
        anySpace.insert(573, 660, "25");
        QuadTree.dfs(anySpace);
        anySpace.remove(800, 600);
        anySpace.remove(566, 660);
        QuadTree.dfs(anySpace);


*/
    }

}