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
    public boolean inRange(int x, int y) {
        return (x >= this.getxMin() && x <= this.getxMax()
                && y >= this.getyMin() && y <= this.getyMax());
    }public boolean outRange(int x, int y) {
        return (x >= this.getxMin() && x <= this.getxMax()
                && y >= this.getyMin() && y <= this.getyMax());
    }

    int xMin, yMin, xMax, yMax;

}
