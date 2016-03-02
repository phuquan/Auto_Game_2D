package RacingGame;

/**
 * Created by Hong-PC on 11/18/2015.
 */
public class Car {

    private float shift_x;
    private float shift_y;

    public Car() {}

    public Car(float x, float y) {
        this.shift_x = x;
        this.shift_y = y;
    }

    public void movingLeft(int delta) {
        shift_x -= delta * .2f;
    }

    public void movingRight(int delta) {
        shift_x += delta * .2f;
    }

    public void movingUp (int delta) {
        shift_y -= delta * .1f;
    }

    public void moingDown ( int delta) {
        shift_y += delta * .1f;
    }

    public void collisionWithLeftBound (int delta) {
        shift_x += delta * .2f;
        shift_y += delta * .2f;
    }

    public  void collisionWithRightBound (int delta) {
        shift_x -= delta * .2f;
        shift_y += delta * .2f;
    }

    public float getShift_x() {
        return shift_x;
    }

    public void setShift_x(float shift_x) {
        this.shift_x = shift_x;
    }

    public float getShift_y() {
        return shift_y;
    }

    public void setShift_y(float shift_y) {
        this.shift_y = shift_y;
    }
}
