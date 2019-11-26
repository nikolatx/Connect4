
package connect4;


public class Tuple {
    
    private int value1;
    private int value2;

    public Tuple(int value1, int value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public Tuple() {
        this.value1 = 0;
        this.value2 = 0;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }
    
}
