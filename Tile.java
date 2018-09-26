public class Tile {

    private char[] top;
    private char[] bottom;
    private char[] left;
    private char[] right;
    private int ID;

    public Tile(char[] h_or_ts, char[] colors) {
        top = new char[] {h_or_ts[0], colors[0]};
        bottom = new char[] {h_or_ts[1], colors[1]};
        left = new char[] {h_or_ts[2], colors[2]};
        right = new char[] {h_or_ts[3], colors[3]};
    }

    public Tile(Tile t) {
        top = t.getTop();
        bottom = t.getBottom();
        left = t.getLeft();
        right = t.getRight();
        ID = t.getID();
    }

    public char[] getTop() {

        return top;

    }

    public char[] getBottom() {

        return bottom;

    }

    public char[] getLeft() {

        return left;

    }

    public char[] getRight() {

        return right;

    }

    public int getID() {

        return ID;

    }

    //set
    public void setTopBP(char BP) {

        top[0] = BP;

    }

    public void setTopColor(char c) {

        top[1] = c;

    }

    public void setBottomBP(char BP) {

        bottom[0] = BP;

    }

    public void setBottomColor(char c) {

        bottom[1] = c;

    }

    public void setLeftBP(char BP) {

        left[0] = BP;

    }

    public void setLeftColor(char c) {

        left[1] = c;

    }

    public void setRightBP(char BP) {

        right[0] = BP;

    }

    public void setRightColor(char c) {

        right[1] = c;

    }

    public void setID(int i) {

        ID = i;

    }
}
