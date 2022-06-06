import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

// On my honor:
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or unmodified.
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
// - I have not discussed coding details about this project with
// anyone other than the my partner, instructor, ACM/UPE tutors
// or the TAs assigned to this course.
// I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction

class Point2 {
    public static void main(String args[]) throws IOException {
        Boundry boundry=new Boundry(0, 0, 1024, 1024);
        QuadTree anySpace = new QuadTree(1,boundry,null );
        SkipList list=new SkipList();

        String fName = args[0];

//        File file = new File("F:\\college\\third_year\\second semester\\algorithms\\project test cases\\P2test2.txt");
        File file = new File(fName);


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

                            System.out.println("Point Inserted: ("+name+", "+x+", "+y+")");
                        }else{
                            System.out.println("Point REJECTED: ("+name+", "+x+", "+y+")");

                        }
                        break;
                    case "duplicates":
                        System.out.println("Duplicate Points:");
                        Node temp=null;
                        for(Node node:anySpace.duplicates()){
                            if (temp==null||!( temp.x==node.x && temp.y==node.y)){
                                temp=node;
                                System.out.println("("+node.x+", "+node.y+")");
                                //System.out.printf("\n(%d, %d)",node.x,node.y);

                            }

                        }
                        break;
                    case "dump":
                        QuadTree.size=1;
                        list.dump();
                        //System.out.print("\nQuadTree Dump:");
                        System.out.println("QuadTree Dump:");
                        QuadTree.dfs(anySpace);
                        //System.out.printf("\nQuadTree Size: %d QuadTree Nodes Printed.",QuadTree.size);
                        System.out.println("QuadTree Size: "+QuadTree.size+" QuadTree Nodes Printed.");
                        break;
                    case "search":
                        if (list.find(splitStr[i+1])!=null){
                            System.out.println("Point Found ("+list.find(splitStr[i+1]).getName()+", "+
                                            list.find(splitStr[i+1]).getx()
                                            +", "+list.find(splitStr[i+1]).gety()+")");
                        //System.out.printf("\nPoint Found ("+list.find(splitStr[i+1]).getName()+", %d, %d)"
                                //,list.find(splitStr[i+1]).getx(),
                                //list.find(splitStr[i+1]).gety());
                        }else{
//                            System.out.println("\nPoint Not Found: "+splitStr[i+1]);
                            System.out.println("Point Not Found: "+splitStr[i+1]);

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
                                    //System.out.printf("\npoint Not Found: (%d, %d)",x,y);
                                    System.out.println("point Not Found: ("+x+", "+y+")");
                                }
                            }else{
                                //System.out.printf("Point Rejected: (%d, %d)",x,y);
                                System.out.println("Point Rejected: ("+x+", "+y+")");

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
                            System.out.println("Points Intersecting Region: ("+x+", "+y+", "+h+", "+w+")");
                            for (Node node: found){
                                System.out.println("Point Found: ("+node.name+", "+node.x+", "+node.y+")");
                                //System.out.printf("\nPoint Found: ("+node.name+", %d, %d)",node.x,node.y);

                            }

                            System.out.println(QuadTree.visitedNodes+" QuadTree Nodes Visited");


                        }else{
                            //System.out.printf("Rectangle Rejected: (%d, %d, %d, %d)",x,y,h,w);
                            System.out.println("Rectangle Rejected: ("+x+", "+y+", "+h+", "+w+")");

                        }
                        break;


                }
            }

        }

    }

}