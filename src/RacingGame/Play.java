package RacingGame;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Hong-PC on 11/15/2015.
 */
public class Play extends BasicGameState {

    private static final int POSITION1 = 300;
    private static final int POSITION2 = 385;
    private static final int POSITION3 = 470;
    private static final float INITAL_Y = 480;

    String directionServer = "NoKeyDown", directionClient = "NoKeyDown";

    Image road, carServerImage, roadTran, carClientImage, mine, mine1;

    boolean quit = false;
    boolean collsion = true;
    boolean isCollisionWithMine = false;
    boolean sendWin = false;
    boolean isServer = true;

    Car car_server = new Car();
    Car car_client = new Car();
    float roadPosX = 0;
    float roadPosY = -400;
    float distance = 0;

    String receiveLocation;

    DatagramSocket socket = null;
    DatagramPacket receivePacket;
    DatagramPacket sendPacket;

    private static int PORT1 = 10049;
    private static int PORT = 28040;
    private static String IP = "192.168.43.165";

    Shape shapeFirst;
    Shape shapeSecond;
    Shape shapeMine_1, shapeMine_2;

    private ArrayList<Mine> listPosMine = new ArrayList<>();
    int randomPos_1 = randomPositionMine();
    int randomPos_2 = randomPositionMine();

    public Play(int state) {

        car_server.setShift_x(POSITION1);
        car_server.setShift_y(INITAL_Y);

        listPosMine.add(new Mine(POSITION1, 320));
        listPosMine.add(new Mine(POSITION1, 160));
        listPosMine.add(new Mine(POSITION2, 320));
        listPosMine.add(new Mine(POSITION2, 160));
        listPosMine.add(new Mine(POSITION3, 320));
        listPosMine.add(new Mine(POSITION3, 160));

        Thread thread = new Thread() {
            public void run() {
                try {
                    socket = new DatagramSocket(PORT);
                    byte[] buffer = new byte[1024];
                    receivePacket = new DatagramPacket(buffer, buffer.length);

                    while (true) {
                        socket.receive(receivePacket);
                        byte[] data = receivePacket.getData();
                        receiveLocation = new String (data, 0, receivePacket.getLength());
                        String[] location = receiveLocation.split(",");
                        car_client.setShift_x(Float.parseFloat(location[0]));
                        car_client.setShift_y(Float.parseFloat(location[1]));
                        directionClient = location[2];
                        String checkServer = location[3];
                        String checkWin = location[8];
                        if (checkServer.equals("true")) {
                            listPosMine.get(randomPos_1).setX(Float.parseFloat(location[4]));
                            listPosMine.get(randomPos_1).setY(Float.parseFloat(location[5]));

                            listPosMine.get(randomPos_2).setX(Float.parseFloat(location[6]));
                            listPosMine.get(randomPos_2).setY(Float.parseFloat(location[7]));
                        }

                        if (car_client.getShift_x() == POSITION1) {
                            car_server.setShift_x(POSITION3);
                        }

                        if (checkWin.equals("true")) {
                            isCollisionWithMine = true;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        road = new Image("res/road.png");
        roadTran = new Image("res/road.png");
        mine = new Image("res/mine.png");
        mine1 = new Image("res/mine.png");

        carServerImage = new Image("res/car/car1.png");
        carClientImage = new Image("res/car/car6.png");

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

        if (car_client.getShift_x() == POSITION1) {
            car_server.setShift_x(POSITION3);
        }

        shapeFirst = new Rectangle(car_server.getShift_x(), car_server.getShift_y(),
                        carServerImage.getWidth() - 5, carServerImage.getHeight() - 7);
        shapeSecond = new Rectangle(car_client.getShift_x(), car_client.getShift_y(),
                        carClientImage.getWidth() - 5, carClientImage.getHeight() - 7);


        //shapeMine_1 = new Rectangle(listPosMine.get(randomPos_1).getX(), listPosMine.get(randomPos_1).getY() - 400,
        //                mine.getWidth(), mine.getHeight());
        //shapeMine_2 = new Rectangle(listPosMine.get(randomPos_2).getX(), listPosMine.get(randomPos_2).getY() - 400,
        //                mine.getWidth(), mine.getHeight());

        road.draw(roadPosX, roadPosY - distance * 1000);

        roadTran.draw(roadPosX, roadPosY - distance * 1000 - 998);

        //mine.draw(listPosMine.get(randomPos_1).getX(), listPosMine.get(randomPos_1).getY() - 400);

        //mine.draw(listPosMine.get(randomPos_2).getX(), listPosMine.get(randomPos_2).getY() - 400);

        carServerImage.draw(car_server.getShift_x(), car_server.getShift_y());
        //graphics.drawString("Me", car_server.getShift_x() + STRING_POSITION_X, car_server.getShift_y() + STRING_POSITION_Y);

        carClientImage.draw(car_client.getShift_x(), car_client.getShift_y());
        //graphics.drawString("You", car_client.getShift_x() + STRING_POSITION_X, car_client.getShift_y() + STRING_POSITION_Y);

        if (isCollisionWithMine) {
            graphics.drawString("You Win", 400, 300);
        }

        if (sendWin) {
            graphics.drawString("You lost", 400, 300);
        }


        if (quit) {
            graphics.drawString("Resume (R)", 250, 200);
            graphics.drawString("Quit Game (Q)", 250, 300);

            if (!quit) {
                graphics.clear();
            }
        }

        //graphics.drawString("X: " + car_client.getShift_x(), 300, 300);

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {

        if (!sendWin && !isCollisionWithMine) {
            roadPosY += delta * .5f;
            listPosMine.get(randomPos_1).setY(listPosMine.get(randomPos_1).getY() + delta *.1f);
            listPosMine.get(randomPos_2).setY(listPosMine.get(randomPos_2).getY() + delta * .1f);
        }

        if (roadPosY > (600 + distance * 1000)) {
            distance++;
        }

       if (isServer) {
           if (listPosMine.get(randomPos_1).getY() >= 1000) {
               if (randomPos_1 == 0 || randomPos_1 == 2 || randomPos_1 == 4) {
                   listPosMine.get(randomPos_1).setY(320);
               }
               randomPos_1 = randomPositionMine();
           }

           if (listPosMine.get(randomPos_2).getY() >= 1000) {
               if (randomPos_2 == 1 || randomPos_2 == 3 || randomPos_2 == 5) {
                   listPosMine.get(randomPos_2).setY(160);
               }
               randomPos_2 = randomPositionMine();
           }
       }

        Input input = gameContainer.getInput();

        if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
            car_server.movingLeft(delta);

            if (car_server.getShift_x() < 240) {
                car_server.collisionWithLeftBound(delta);
            }

            if ( car_client.getShift_x() < 240) {
                car_client.collisionWithLeftBound(delta);
            }

            if (isCollision()) {
                if (directionClient.equals("right")){
                    car_server.setShift_x(car_server.getShift_x() + delta * 1f);
                    car_server.setShift_y(car_server.getShift_y() + delta * .5f);
                    collsion = true;
                } else if (directionClient.equals("NoKeyDown") || directionClient.equals("right")) {
                    collsion = false;
                }
                directionServer = "left";
            }
        }

        if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
            car_server.movingRight(delta);

            if (car_server.getShift_x() > 526) {
                car_server.collisionWithRightBound(delta);
            }

            if (car_client.getShift_x() > 526) {
                car_client.collisionWithRightBound(delta);
            }

            if (isCollision()) {

                if (directionClient.equals("left")) {
                    car_server.setShift_x(car_server.getShift_x() - delta * 1f);
                    car_server.setShift_y(car_server.getShift_y() + delta * .5f);
                    collsion = true;
                } else if (directionClient.equals("NoKeyDown") || directionClient.equals("left")) {
                    collsion = false;
                }
                directionServer = "right";

            }
        }

        if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
            car_server.movingUp(delta);

            if (isCollision()) {
                if (directionClient.equals("down")){
                    car_server.setShift_y(car_server.getShift_y() + delta * 5f);
                    collsion = true;
                }else if (directionClient.equals("NoKeyDown") || directionClient.equals("up")) {
                    collsion = false;
                }
                directionServer = "up";
            }
        }

        if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
            car_server.moingDown(delta);

            if (isCollision()) {
                if (directionClient.equals("up")) {
                    car_server.setShift_y(car_server.getShift_y() - delta * 5f);
                    collsion = true;
                }else if (directionClient.equals("NoKeyDown") || directionClient.equals("down")) {
                    collsion = false;
                }
                directionServer = "down";
            }
        }

        if (isCollision() && !collsion){
            if (!isKeyDown(input)) {


                if (car_server.getShift_x() < 240) {
                    car_server.collisionWithLeftBound(delta);
                }

                if ( car_client.getShift_x() < 240) {
                    car_client.collisionWithLeftBound(delta);
                }

                if (car_server.getShift_x() > 526) {
                    car_server.collisionWithRightBound(delta);
                }

                if (car_client.getShift_x() > 526) {
                    car_client.collisionWithRightBound(delta);
                }

                if (directionClient.equals("left")) {
                    car_server.setShift_x(car_server.getShift_x() - delta * .2f);
                } else if (directionClient.equals("right")) {
                    car_server.setShift_x(car_server.getShift_x() + delta * .2f);
                } else if (directionClient.equals("up")) {
                    car_server.setShift_y(car_server.getShift_y() - delta * .1f);
                }else if (directionClient.equals("down")){
                    car_server.setShift_y(car_server.getShift_y() + delta * .1f);
                }
            }
        }

        if (!isKeyDown(input)) {
            directionServer = "NoKeyDown";
        }

        if (isCollisionWithMine()) {
            sendWin = true;
        }

        try {
            InetAddress host = InetAddress.getByName(IP);

            String sendLoca = car_server.getShift_x() + "," + car_server.getShift_y() + ","
                            + directionServer + "," + isServer + ","
                            + listPosMine.get(randomPos_1) .getX() + "," + listPosMine.get(randomPos_1).getY() + ","
                            + listPosMine.get(randomPos_2).getX() + "," + listPosMine.get(randomPos_2).getY() + ","
                            + sendWin;
            byte[] sendData = sendLoca.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, host, PORT);
            socket.send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getID() {
        return 1;
    }

    public boolean isCollision () {
        if (shapeSecond != null) {
            return shapeFirst.intersects(shapeSecond);
        }

        return false;
    }

    public boolean isKeyDown (Input input) {
        if (!input.isKeyDown(Input.KEY_LEFT) && !input.isKeyDown(Input.KEY_RIGHT) &&
                !input.isKeyDown(Input.KEY_UP) && !input.isKeyDown(Input.KEY_DOWN)) {
            return false;
        }

        return true;
    }

    public boolean isCollisionWithMine() {
       if (shapeMine_1 != null && shapeMine_2 != null) {
           if (shapeFirst.intersects(shapeMine_1) || shapeFirst.intersects(shapeMine_2)) {
               return true;
           }
       }

        return false;
    }

    public void collisionWhenKeyLeftDow(Car car1, Car car2, int delta) {
//        //shiftX_first += delta * 7f;
//        //shiftX_second -= delta * 4f;
//        car_server.setShift_x(car_server.getShift_x() + delta * 7f);
//        car_client.setShift_x(car_client.getShift_x() - delta * 4f);
//
////                shiftY_fisrt += delta * 2f;
////                shiftY_second += delta * 2f;
//        car_server.setShift_y(car_server.getShift_y() + delta * 2f);
//        car_client.setShift_y(car_client.getShift_y() + delta * 2f);

        car1.setShift_x(car1.getShift_x() + delta * 7f);
        car1.setShift_y(car1.getShift_y() + delta * 2f);

        car2.setShift_x(car2.getShift_x() - delta * 4f);
        car2.setShift_y(car2.getShift_y() + delta * 2f);
    }

    public void collisionWhenKeyRightDown(Car car1, Car car2, int delta) {
//                shiftX_first -= delta * 7f;
//                shiftX_second += delta * 4f;
//        car_server.setShift_x(car_server.getShift_x() - delta * 7f);
//        car_client.setShift_x(car_client.getShift_x() + delta * 4f);
//
////                shiftY_fisrt += delta * 2f;
////                shiftY_second += delta * 2f;
//        car_server.setShift_y(car_server.getShift_y() + delta * 2f);
//        car_client.setShift_y(car_client.getShift_y() + delta * 2f);

        car1.setShift_x(car1.getShift_x() - delta * 7f);
        car1.setShift_y(car1.getShift_y() + delta * 2f);

        car2.setShift_x(car2.getShift_x() + delta * 4f);
        car2.setShift_y(car2.getShift_y() + delta * 2f);
    }

    public void collisionWhenKeyUpDown(Car car1, Car car2, int delta) {
//        //                shiftY_fisrt += delta * 20.5f;
////                shiftY_second -= delta * 20.2f;
//
//        car_server.setShift_y(car_server.getShift_y() + delta * 20.5f);
//        car_client.setShift_y(car_client.getShift_y() - delta * 20.2f);

        car1.setShift_y(car1.getShift_y() + delta * 20.5f);
        car2.setShift_y(car2.getShift_y() - delta * 20.2f);
    }

    public void collisionWhenKeyDownDown(Car car1, Car car2, int delta) {
    //                shiftY_fisrt -= delta * 5f;
//                shiftY_second += delta * 8f;
        car1.setShift_y(car1.getShift_y() - delta * 5f);
        car2.setShift_y(car2.getShift_y() + delta * 8f);
    }

    public int randomPositionMine() {
        Random random = new Random();
        return random.nextInt(6);
    }
}