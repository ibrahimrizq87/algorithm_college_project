import java.util.Random;

class SkipNode {

    public SkipNode[] forward;
    private String name;
    private int x;
    private int y;



    public SkipNode(String name,int x,int y, int level) {

        forward = new SkipNode[level + 1];
        this.name=name;
        this.x=x;
        this.y=y;
        for (int i = 0; i < level; i++) {
            forward[i] = null;
        }

    }

    public String getName(){
        return this.name;
    }
    public int getx(){
        return this.x;
    }
    public int gety(){
        return this.y;
    }
}
class SkipList {
    private SkipNode head;
    private int level;
    private int size;
    static private Random ran = new Random(); // Hold the Random class object

    public SkipList() {
        head = new SkipNode("", 0, 0,0);
        level = -1;
        size = 0;
    }
    public void dump(){
        System.out.println("\nSkipList Dump:");
        SkipNode x = head;
        System.out.printf("\n level: %d Value: null",x.forward.length);
        while(x.forward[0] != null){
            x=x.forward[0];
            System.out.printf("\n level: %d Value: ("+x.getName()+", %d, %d)",x.forward.length,x.getx(),x.gety());


        }
        /*for (int i = level; i >= 0; i--) {
            for (int j = 0; j < x.forward.length; j++)
            { // go forward

                 // Go one last step
                if (x.forward[i]==null){
                    System.out.printf("\n level: %d Value: null",i);
                }else{
                    x = x.forward[i];
                    System.out.printf("\n level: %d Value: ("+x.getName()+", %d, %d)",i,x.getx(),x.gety());
                }
    }
            }*/
        }

    public SkipNode find(String name) {

        SkipNode x = head; // Dummy header node
        for (int i = level; i >= 0; i--) {
            while ((x.forward[i] != null) && (x.forward[i].getName().compareTo(name) < 0))
            { // go forward
                x = x.forward[i]; // Go one last step

            }
        }
        x = x.forward[0]; // Move to actual record, if it exists
        if ((x != null) && (x.getName().compareTo(name) == 0))
        { return x; } // Got it
        else { return null; } // Its not there
    }
    int randomLevel() {
        int lev;
        for (lev = 0; Math.abs(ran.nextInt()) % 2 == 0; lev++) { // ran is random generator
            ; // Do nothing
        }
        return lev;
    }
    public void insert(String name,int X,int y) {
        int newLevel = randomLevel(); // New node's level
        if (newLevel > level) { // If new node is deeper
            adjustHead(newLevel); // adjust the header
        }

        SkipNode[] update = new SkipNode[level + 1];

        SkipNode x = head; // Start at header node
        for (int i = level; i >= 0; i--) { // Find insert position
            while ((x.forward[i] != null) && (x.forward[i].getName().compareTo(name) < 0)) {
                x = x.forward[i];

            }
            update[i] = x; // Track end at level i
        }
        x = new SkipNode(name, X,y, newLevel);
        for (int i = 0; i <= newLevel; i++) { // Splice into list
            x.forward[i] = update[i].forward[i]; // Who x points to
            update[i].forward[i] = x; // Who points to x
        }
        size++; // Increment dictionary size
    }

    private void adjustHead(int newLevel) {
        SkipNode temp = head;
        head = new SkipNode("", 0,0, newLevel);
        for (int i = 0; i <= level; i++) {
            head.forward[i] = temp.forward[i];

        }
        level = newLevel;
    }
}