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
}/*
class internalNode extends Node{
    internalNode(int x, int y, int value) {
        super(x, y, value);
    }
}
class leafNode extends Node{
    leafNode(int x, int y, int value) {
        super(x, y, value);
    }
}*/

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
     int numberofDubPoints=0;
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
        String space="";
        if (tree == null)
            return;
        for (int i=0;i<tree.level-1;i++){
            space+="  ";
        }
        System.out.printf("\n"+space+"Node at %d, %d, %d, %d:",
                 tree.boundry.getxMin(), tree.boundry.getyMin(),
                tree.boundry.getxMax(), tree.boundry.getyMax());

        if(tree.isDivided){

            System.out.printf(" Internal");
        }

        for (Node node : tree.nodes) {
            System.out.printf("\n");

            System.out.printf(space+"("+node.name+", %d, %d)", node.x, node.y);

        }

        if (tree.nodes.size() == 0) {
            System.out.printf(" Empty");

        }

        dfs(tree.northWest);
        dfs(tree.northEast);
        dfs(tree.southWest);
        dfs(tree.southEast);

    }

    void rearrange(QuadTree tree){
        List<Node> removed= new ArrayList<>();
        if(tree.northWest!= null && tree.northWest.nodes.size()>0){
            for (Node node : tree.northWest.nodes){
                if (tree.MAX_CAPACITY>tree.nodes.size()){
                    tree.nodes.add(node);
                    if (node.isDublicated && tree.northWest.numberofDubPoints>0){
                        tree.MAX_CAPACITY ++;
                        tree.numberofDubPoints++;

                        tree.northWest.numberofDubPoints --;
                        tree.northWest.MAX_CAPACITY --;
                    }
                    removed.add(node);
                    //tree.northWest.nodes.remove(node);

                }
            }
            for (Node node : removed){
                tree.northWest.nodes.remove(node);

            }


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
            rearrange(northWest);

            return;
        }else if(tree.northEast!= null && tree.northEast.nodes.size()>0){
            for (Node node : tree.northEast.nodes){
                if (tree.MAX_CAPACITY>tree.nodes.size()){
                    tree.nodes.add(node);
                    if (node.isDublicated && tree.northEast.numberofDubPoints>0){
                        tree.MAX_CAPACITY ++;

                        tree.numberofDubPoints++;

                        tree.northWest.numberofDubPoints --;
                        tree.northEast.MAX_CAPACITY --;
                    }
                    removed.add(node);
                    //tree.northEast.nodes.remove(node);

                }

            }
            for (Node node : removed){
                tree.northEast.nodes.remove(node);

            }
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
            rearrange(northEast);

            return;
        }else if(tree.southWest!= null && tree.southWest.nodes.size()>0){
            for (Node node : tree.southWest.nodes){
                if (tree.MAX_CAPACITY>tree.nodes.size()){
                    tree.nodes.add(node);
                    if (node.isDublicated && tree.southWest.numberofDubPoints>0){
                        tree.MAX_CAPACITY ++;

                        tree.numberofDubPoints++;

                        tree.northWest.numberofDubPoints --;
                        tree.southWest.MAX_CAPACITY --;
                    }
                    removed.add(node);
                    //tree.southWest.nodes.remove(node);
                }
            }
            for (Node nod : removed){
                tree.southWest.nodes.remove(nod);

            }
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
            rearrange(southWest);

            return;
        }else if(tree.southEast!= null && tree.southEast.nodes.size()>0){
            for (Node node : tree.southEast.nodes){
                if (tree.MAX_CAPACITY>tree.nodes.size()){
                    tree.nodes.add(node);
                    if (node.isDublicated && tree.southEast.numberofDubPoints>0){
                        tree.MAX_CAPACITY ++;

                        tree.numberofDubPoints++;

                        tree.northWest.numberofDubPoints --;
                        tree.southEast.MAX_CAPACITY --;
                    }
                    removed.add(node);
                    //tree.southEast.nodes.remove(node);

                }
            }
            for (Node nod : removed){
                tree.southEast.nodes.remove(nod);

            }
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
            rearrange(southEast);

            return;
        }

    }
void remove(String name){
    boolean isDeleted=false;
    for (Node node : this.nodes) {
        //if (  name.equals(node.name))
        if (name.equals(node.name)) {
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

                    isDeleted=true;
                    return;


                }

                this.nodes.remove(node);

                rearrange(this);

                return;
            }
        } else if (isDivided && !isDeleted ) {

            this.northWest.remove(name);
            isDeleted=true;
            this.northEast.remove(name);
            isDeleted=true;
            this.southWest.remove(name);
            isDeleted=true;
            this.southEast.remove(name);
        }
    }
}



     void remove(int x,int y) {
 boolean isDeleted=false;
         for (Node node : this.nodes) {
             //if (  name.equals(node.name))
             if (x == node.x&& node.y==y) {
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

                         isDeleted=true;
                         return;


                     }

                     this.nodes.remove(node);

                     rearrange(this);

                     return;
                 }
             } else if (isDivided && !isDeleted &&(this.northWest.boundry.inRange(x,y)||
                     this.northEast.boundry.inRange(x,y)||this.southWest.boundry.inRange(x,y)||
                     this.southEast.boundry.inRange(x,y))) {

                 this.northWest.remove(x,y);
                 isDeleted=true;
                 this.northEast.remove(x,y);
                 isDeleted=true;
                 this.southWest.remove(x,y);
                 isDeleted=true;
                 this.southEast.remove(x,y);
             }
         }
     }

    void split() {
int xValue=(this.boundry.getxMax()-this.boundry.getxMin())/2;
        int yValue=(this.boundry.getyMax()-this.boundry.getyMin())/2;

        northWest = new QuadTree(this.level + 1, new Boundry(
                this.boundry.getxMin(),
                this.boundry.getyMin()+yValue,
                this.boundry.getxMin()+xValue,
                this.boundry.getyMax()));
        northEast = new QuadTree(this.level + 1, new Boundry(
                this.boundry.getyMin()+xValue,
                this.boundry.getyMin()+yValue,
                this.boundry.getxMax(),
                this.boundry.getyMax()));

        southWest = new QuadTree(this.level + 1, new Boundry(
                this.boundry.getxMin(),
                this.boundry.getyMin(),
                this.boundry.getyMin()+xValue,
                this.boundry.getyMin()+yValue));
        southEast = new QuadTree(this.level + 1, new Boundry(
                this.boundry.getyMin()+xValue,
                this.boundry.getyMin(),
                this.boundry.getxMax(),
                this.boundry.getyMin()+yValue));


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

    void insert(int x, int y, String value) {
        if (!this.boundry.inRange(x, y)|| (!Character.isAlphabetic( value.charAt(0) )&& value.charAt(0)!= '_')) {
            System.out.printf("Point REJECTED: ("+value+", %d, %d)\n",x,y);
            return;
        }

        Node node = new Node(x, y, value);
        if (nodes.size() < MAX_CAPACITY) {
            for (Node node2 : this.nodes) {
            if ((node2.x == node.x&& node2.y == node.y)
                    &&(node2.name== node.name)
            ){
                System.out.printf("Point REJECTED: ("+value+", %d, %d)\n",x,y);
                return;

            }else if(node2.x == node.x&& node2.y == node.y)
            {
                node.isDublicated=true;
                node2.isDublicated=true;

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
                    System.out.printf("Point REJECTED: ("+value+", %d, %d)\n",x,y);

    }
     List<Node> search(String name){
         List<Node> found = new ArrayList<>();
    for (Node node : this.nodes) {
        //if (  name.equals(node.name))
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
    /*
    Node  search(int x, int y){
        if (this.boundry.inRange(x,y)){
             for (Node node : this.nodes) {
                 if(node.x == x && node.y==y){
                     return node;
                 }
          /* System.out.printf("\n\t  XXX %d YYYY %d", node.x,node.y);
           System.out.printf("\n\t  BBX %d BBY %d", this.boundry.getxMax(),this.boundry.getyMax());

                 }
             }

         if (isDivided) {
             this.northWest.search(x,y);
             found.addAll(this.northEast.query(boundry));
             found.addAll(this.southWest.query(boundry));
             found.addAll(this.southEast.query(boundry));

         }
         return found;
     }*/
    List<Node>  regionsearch(int x,int y,int w,int h){
        Boundry boundry = new Boundry(x,y,w,h);
    List<Node> found = new ArrayList<>();
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
        QuadTree anySpace = new QuadTree(1, new Boundry(0, 0, 1000, 1000));
        anySpace.insert(100, 100, "a");
        anySpace.insert(100, 101, "b");
        anySpace.insert(100, -12, "c");
        anySpace.insert(700, 600, "d");
        anySpace.insert(800, 600, "d");
        anySpace.insert(900, 600, "f");
        anySpace.insert(510, 610, "g");
        anySpace.insert(520, 620, "h");
        anySpace.insert(530, 630, "j");
        anySpace.insert(540, 640, "k");
        anySpace.insert(550, 650, "l");
        anySpace.insert(555, 655, "i");
        anySpace.insert(561, 660, "y");
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
        SkipList list=new SkipList();
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
        QuadTree.dfs(anySpace);
        list.dump();

  /*

*/

        // anySpace.dump();
       // QuadTree.dfs(anySpace);



        /*
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

*/
    }

}