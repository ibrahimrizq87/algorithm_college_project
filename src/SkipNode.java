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