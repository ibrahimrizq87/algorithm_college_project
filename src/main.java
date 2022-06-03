import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class main{
    public static void main(String args[]) throws IOException {
        Boundry boundry=new Boundry(0, 0, 1024, 1024);
        QuadTree anySpace = new QuadTree(1,boundry );
        SkipList list=new SkipList();

QuadTree.visitedNodes=1;
        QuadTree.size=1;
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
                        System.out.printf("\nQuadTree Size: %d QuadTree Nodes Printed.",QuadTree.visitedNodes);
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

    }

}