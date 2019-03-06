package port_oop;

import java.util.Random;

public class DemoPort {

    public static void main(String[] args) {
        Port port = new Port(5,2,2,2);
        port.start();
        for (int i = 0; i < 22 ; i++) {
            Ship ship = new Ship("Ship "+(i+1),port);
            ship.start();
        }
    }
    static int random(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
