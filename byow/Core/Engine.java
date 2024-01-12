package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.*;
import java.util.Random;
import java.util.*;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;


public class Engine implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 108; //120
    public static final int HEIGHT = 57; //63

    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int TILE_SIZE = 16;

    World tempWorld = null;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        mainMenu();
        int temp = waitForInput();
        if (temp == 0) {
            interactWithInputString(solicitSeed());
        } else if (temp == 1){
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(tempWorld.getBoard());
            interact(tempWorld);
        } else {
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(tempWorld.getBoard());
            interactReplay(tempWorld);
        }
    }

    public int waitForInput() {
        boolean flag = false;
        while (!flag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'n' || c == 'N') {
                    flag = true;
                    return 0;
                }
                if (c == 'l' || c == 'L') {
                    tempWorld = World.getCurrentState();
                    flag = true;
                    return 1;
                }
                if (c == 'r' || c == 'R') {
                    Long tempSeed = World.getCurrentState().getSeed();
                    String moves = World.getCurrentState().getInputs();
                    tempWorld = new World(tempSeed);
                    tempWorld.createWorld();
                    tempWorld.setInputs(moves);
                    flag = true;
                    return 2;
                }
                if (c == 'q' || c == 'Q') {
                    flag = true;
                    return 0;
                }
            }
        }
        return 0;
    }

    public String solicitSeed() {
        String temp = " Enter Seed and then press the 's' key: ";
        String result = "n";
        drawFrame(temp, 20, WIDTH / 2, HEIGHT / 2);
        StdDraw.show();
        for (int i = 0; i < 19; i++) {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(0, HEIGHT - 30, WIDTH, (HEIGHT / 2) - 25);
                drawFrame(temp, 20, WIDTH / 2, HEIGHT / 2);
                StdDraw.show();
            }
            char c = StdDraw.nextKeyTyped();
            temp += c;
            result += c;
            if (temp.charAt(temp.length() - 1)  == 's' || temp.charAt(temp.length() - 1) == 'S') {
                return result;
            }
            drawFrame(temp, 20, WIDTH / 2, HEIGHT / 2);
            StdDraw.show();
        }
        return temp;
    }

    public void mainMenu() {
        StdDraw.setCanvasSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        StdDraw.clear(new Color(0, 0, 0));
        drawFrame("CS61B: THE GAME", 30, WIDTH / 2, HEIGHT - (HEIGHT / 3));
        drawFrame("New Game (N)", 20, WIDTH / 2, (HEIGHT / 3) + 2);
        drawFrame("Load Game (L)", 20, WIDTH / 2, (HEIGHT / 3));
        drawFrame("Replay (R)", 20, WIDTH / 2, (HEIGHT / 3) - 2);
        drawFrame("Quit (Q)", 20, WIDTH / 2, (HEIGHT / 3) - 4);

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    public void drawFrame(String s, int fontSize, int xpos, int ypos) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, fontSize);
        StdDraw.setFont(fontBig);
        StdDraw.text(xpos, ypos, s);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

//        BYOW_DIR.mkdir();
//        SAVE_STATES.mkdir();

        World w = new World(42); // placehold seed :)
        boolean readSeed = false;
        boolean preparedToQuit = false;
        boolean initializedRendering = false;
        String seed = "";
        for (Character c : input.toCharArray()) {
            if (readSeed) {
                if (Character.isDigit(c)) {
                    seed += c;
                } else {
                    w = new World(Long.parseLong(seed));
                    w.createWorld();
                    readSeed = false;
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(w.getBoard());
                    initializedRendering = true;
                    interact(w);
                }
            }
            if (c == 'N' || c == 'n') {
                readSeed = true;
            } else if (c == 'L' || c == 'l') {
                w = World.getCurrentState();
            } else if (c == 'w' || c == 'W') {

            } else if (c == 'a' || c == 'A') {

            } else if (c == 's' || c == 'S') {

            } else if (c == 'd' || c == 'D') {

            } else if (c == 'h' || c == 'H') {

            } else if (c == ':') {
                preparedToQuit = true;
            } else if (c == 'Q' || c == 'q') {
                w.quitAndSave(preparedToQuit);
                return w.getBoard();
            }
        }

        if (w.getSeed() == 42) {
            w.createWorld();
        }

        TETile[][] finalWorldFrame = w.getBoard();
        if (!initializedRendering) {
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(finalWorldFrame);
        }
        return finalWorldFrame;
    }

    public void interact(World w) {
        boolean flag = false;
        String temp = "";
        while (!flag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'q' || c == 'Q') {
                    w.move(c);
                }
                temp += c;
                w.setInputs(temp);
                flag = w.move(c);
                ter.renderFrame(w.getBoard());
            }
        }
    }

    public void interactReplay(World w) {
        for (Character c : w.inputs.toCharArray()) {
            w.move(c);
            ter.renderFrame(w.getBoard());
            StdDraw.pause(300);
        }
        boolean flag = false;
        String temp = "";
        while (!flag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                temp += c;
                flag = w.move(c);
                ter.renderFrame(w.getBoard());
            }
        }
    }

    private class World implements Serializable {
        private UserIcon playableCharacter = null;
        private boolean readyToQuit = false;
        private TETile[][] board;
        private Random random;
        private long seed;
        private String inputs = "";
        private HashMap<Position, Pickup> listPickUp = new HashMap();

        World(long seed) {
            random = new Random(seed);
            board = initializeWorld();
            this.seed = seed;
            playableCharacter = new UserIcon();
        }

        public String getInputs() {
            return inputs;
        }

        public void setInputs(String s) {
            inputs = s;
        }

        public long getSeed() {
            return seed;
        }

        public TETile[][] getBoard() {
            return board;
        }

        public TETile[][] initializeWorld() {
            TETile[][] world = new TETile[WIDTH][HEIGHT];
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    world[x][y] = Tileset.NOTHING;
                }
            }
            return world;
        }

        public boolean move(Character input) {
            if (listPickUp.isEmpty()) {
                return true;
            }
            if (input == ':') {
                readyToQuit = true;
                return false;
            }
            if (input == 'q' || input == 'Q') {
                this.quitAndSave(readyToQuit);
                System.exit(-1);
            }
            if (input == 'w' || input == 'W') {
                readyToQuit = false;
                if (board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.NOTHING
                        || board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.WALL) {
                    return false;
                }
                if (board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.LIGHTSOURCE1
                        || board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.LIGHTSOURCE2) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX()][playableCharacter.getY() + 1] = playableCharacter.icon;
                    listPickUp.get(new Position(playableCharacter.getX(),
                            playableCharacter.getY() + 1)).removeLights(playableCharacter.getX(), playableCharacter.getY() + 1);
                    listPickUp.remove(new Position(playableCharacter.getX(), playableCharacter.getY() + 1));
                    playableCharacter.setXandY(playableCharacter.getX(), playableCharacter.getY() + 1);
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                if (board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.LIGHT4
                        || board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.LIGHT3
                        || board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.LIGHT2
                        || board[playableCharacter.getX()][playableCharacter.getY() + 1] == Tileset.LIGHT1) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX()][playableCharacter.getY() + 1] = playableCharacter.icon;
                    playableCharacter.setXandY(playableCharacter.getX(), playableCharacter.getY() + 1);
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                board[playableCharacter.getX()][playableCharacter.getY() + 1] = playableCharacter.icon;
                playableCharacter.setXandY(playableCharacter.getX(), playableCharacter.getY() + 1);
            } else if (input == 'a' || input == 'A') {
                readyToQuit = false;
                if (board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.NOTHING
                        || board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.WALL) {
                    return false;
                }
                if (board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.LIGHTSOURCE1
                        || board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.LIGHTSOURCE2) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX() - 1][playableCharacter.getY()] = playableCharacter.icon;
                    listPickUp.get(new Position(playableCharacter.getX() - 1,
                            playableCharacter.getY())).removeLights(playableCharacter.getX() - 1, playableCharacter.getY());
                    listPickUp.remove(new Position(playableCharacter.getX() - 1, playableCharacter.getY()));
                    playableCharacter.setXandY(playableCharacter.getX() - 1, playableCharacter.getY());
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                if (board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.LIGHT4
                        || board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.LIGHT3
                        || board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.LIGHT2
                        || board[playableCharacter.getX() - 1][playableCharacter.getY()] == Tileset.LIGHT1) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX() - 1][playableCharacter.getY()] = playableCharacter.icon;
                    playableCharacter.setXandY(playableCharacter.getX() - 1, playableCharacter.getY());
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                board[playableCharacter.getX() - 1][playableCharacter.getY()] = playableCharacter.icon;
                playableCharacter.setXandY(playableCharacter.getX() - 1, playableCharacter.getY());
            } else if (input == 's' || input == 'S') {
                readyToQuit = false;
                if (board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.NOTHING
                        || board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.WALL) {
                    return false;
                }
                if (board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.LIGHTSOURCE1
                        || board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.LIGHTSOURCE2) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX()][playableCharacter.getY() - 1] = playableCharacter.icon;
                    listPickUp.get(new Position(playableCharacter.getX(),
                            playableCharacter.getY() - 1)).removeLights(playableCharacter.getX(), playableCharacter.getY() - 1);
                    listPickUp.remove(new Position(playableCharacter.getX(), playableCharacter.getY() - 1));
                    playableCharacter.setXandY(playableCharacter.getX(), playableCharacter.getY() - 1);
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                if (board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.LIGHT4
                        || board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.LIGHT3
                        || board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.LIGHT2
                        || board[playableCharacter.getX()][playableCharacter.getY() - 1] == Tileset.LIGHT1) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX()][playableCharacter.getY() - 1] = playableCharacter.icon;
                    playableCharacter.setXandY(playableCharacter.getX(), playableCharacter.getY() - 1);
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                board[playableCharacter.getX()][playableCharacter.getY() - 1] = playableCharacter.icon;
                playableCharacter.setXandY(playableCharacter.getX(), playableCharacter.getY() - 1);
            } else if (input == 'd' || input == 'D') {
                readyToQuit = false;
                if (board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.NOTHING
                        || board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.WALL) {
                    return false;
                }
                if (board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.LIGHTSOURCE1
                        || board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.LIGHTSOURCE2) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX() + 1][playableCharacter.getY()] = playableCharacter.icon;
                    listPickUp.get(new Position(playableCharacter.getX() + 1,
                            playableCharacter.getY())).removeLights(playableCharacter.getX() + 1, playableCharacter.getY());
                    listPickUp.remove(new Position(playableCharacter.getX() + 1, playableCharacter.getY()));
                    playableCharacter.setXandY(playableCharacter.getX() + 1, playableCharacter.getY());
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                if (board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.LIGHT4
                        || board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.LIGHT3
                        || board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.LIGHT2
                        || board[playableCharacter.getX() + 1][playableCharacter.getY()] == Tileset.LIGHT1) {
                    board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                    board[playableCharacter.getX() + 1][playableCharacter.getY()] = playableCharacter.icon;
                    playableCharacter.setXandY(playableCharacter.getX() + 1, playableCharacter.getY());
                    for (Pickup p : listPickUp.values()) {
                        createAllLights(p);
                    }
                    return false;
                }
                board[playableCharacter.getX()][playableCharacter.getY()] = Tileset.FLOOR;
                board[playableCharacter.getX() + 1][playableCharacter.getY()] = playableCharacter.icon;
                playableCharacter.setXandY(playableCharacter.getX() + 1, playableCharacter.getY());
            } else if (input == 'h' || input == 'H') {
                readyToQuit = false;
                turnOffAllLights(listPickUp);
            }
            return false;
        }

        public void createWorld() {
            BinaryRoom bigRoom = new BinaryRoom((WIDTH / 20), WIDTH - (WIDTH / 20),
                    HEIGHT - (HEIGHT / 12), (HEIGHT / 20), random);
            bigRoom.split();
            createBinaryRooms(bigRoom);
            createCorridors(getRightCorridorStart(bigRoom), getUpCorridorStart(bigRoom));
            playableCharacter.placeCharacter(scanForAvailablePlaces());
            for (int i = 0; i < 10; i++) {
                List<Position> tempList = scanForAvailablePlaces();
                int rPosition = random.nextInt(tempList.size());
                listPickUp.put(tempList.get(rPosition), placePickup(tempList.get(rPosition)));
            }
            for (Pickup p : listPickUp.values()) {
                createAllLights(p);
            }
        }

        public Pickup placePickup(Position p) {
            Pickup tempPickUp = new Pickup();
            board[p.getX()][p.getY()] = Tileset.LIGHTSOURCE1;
            tempPickUp.setXandY(p.getX(), p.getY());
            return tempPickUp;
        }

        public void createAllLights(Pickup tempPickUp) {
            tempPickUp.createLight4(tempPickUp.getX(), tempPickUp.getY());
            tempPickUp.createLight3(tempPickUp.getX(), tempPickUp.getY());
            tempPickUp.createLight2(tempPickUp.getX(), tempPickUp.getY());
            tempPickUp.createLight1(tempPickUp.getX(), tempPickUp.getY());
        }

        public void turnOffAllLights(HashMap<Position, Pickup> l) {
            for (Pickup p : l.values()) {
                if (board[p.getX()][p.getY()] == Tileset.LIGHTSOURCE1) {
                    p.removeLights(p.getX(), p.getY());
                    board[p.getX()][p.getY()] = Tileset.LIGHTSOURCE2;
                    continue;
                }
                if (board[p.getX()][p.getY()] == Tileset.LIGHTSOURCE2) {
                    createAllLights(p);
                    board[p.getX()][p.getY()] = Tileset.LIGHTSOURCE1;
                }
            }
        }

        public List<Position> scanForAvailablePlaces() {
            ArrayList<Position> p = new ArrayList<>();
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    if (board[x][y] == Tileset.FLOOR) {
                        p.add(new Position(x, y));
                    }
                }
            }
            return p;
        }

        public void createBinaryRooms(BinaryRoom r) {
            if (r.isLeaf()) {
                createBinaryRoomsHelper(r);
            } else {
                if (r.getLeftRoom() != null) {
                    r.getLeftRoom().split();
                }
                if (r.getRightRoom() != null) {
                    r.getRightRoom().split();
                }
                if (r.getLeftRoom() != null) {
                    createBinaryRooms(r.getLeftRoom());
                }
                if (r.getRightRoom() != null) {
                    createBinaryRooms(r.getRightRoom());
                }
            }
        }

        private void createBinaryRoomsHelper(BinaryRoom r) {
            for (int i = r.getTempRoom().getLeft(); i < r.getTempRoom().getRight(); i++) {
                for (int j = r.getTempRoom().getBottom(); j < r.getTempRoom().getTop(); j++) {
                    board[i][j] = Tileset.FLOOR;
                    if (i == r.getTempRoom().getLeft() && j == r.getTempRoom().getBottom()) {
                        board[i - 1][j - 1] = Tileset.WALL;
                    }
                    if (i == r.getTempRoom().getLeft() && j == r.getTempRoom().getTop() - 1) {
                        board[i - 1][j + 1] = Tileset.WALL;
                    }
                    if (i == r.getTempRoom().getRight() - 1 && j == r.getTempRoom().getBottom()) {
                        board[i + 1][j - 1] = Tileset.WALL;
                    }
                    if (i == r.getTempRoom().getRight() - 1 && j == r.getTempRoom().getTop() - 1) {
                        board[i + 1][j + 1] = Tileset.WALL;
                    }
                    if (i == r.getTempRoom().getLeft()) {
                        board[i - 1][j] = Tileset.WALL;
                    }
                    if (i == r.getTempRoom().getRight() - 1) {
                        board[i + 1][j] = Tileset.WALL;
                    }
                    if (j == r.getTempRoom().getBottom()) {
                        board[i][j - 1] = Tileset.WALL;
                    }
                    if (j == r.getTempRoom().getTop() - 1) {
                        board[i][j + 1] = Tileset.WALL;
                    }
                }
            }
        }

        public void createCorridors(List<Position> rightValues, List<Position> upValues) {
            for (Position p : rightValues) {
                for (int i = p.getX(); i < WIDTH; i++) {
                    int rUpCorridor = random.nextInt(100);
                    if (board[i][p.getY()].equals(Tileset.FLOOR)) {
                        break;
                    }
                    if (i == WIDTH - (WIDTH / 20)) {
                        board[i][p.getY()] = Tileset.WALL;
                        board[i][p.getY() + 1] = Tileset.WALL;
                        board[i][p.getY() - 1] = Tileset.WALL;
                        break;
                    }
                    board[i][p.getY()] = Tileset.FLOOR;
                    board[i][p.getY() + 1] = Tileset.WALL;
                    board[i][p.getY() - 1] = Tileset.WALL;
                    if (rUpCorridor <= 5) {
                        upValues.add(new Position(i, p.getY() + 1));
                    }
                }
            }
            for (Position p : upValues) {
                for (int i = p.getY(); i < HEIGHT; i++) {
                    if (board[p.getX()][i].equals(Tileset.FLOOR)) {
                        break;
                    }
                    if (i == HEIGHT - (HEIGHT / 12) + 1 || i == HEIGHT - (HEIGHT / 12) + 2 || i == HEIGHT - (HEIGHT / 12) + 3) {
                        board[p.getX()][i] = Tileset.WALL;
                        board[p.getX() + 1][i] = Tileset.WALL;
                        board[p.getX() - 1][i] = Tileset.WALL;
                        break;
                    }
                    board[p.getX()][i] = Tileset.FLOOR;
                    board[p.getX() + 1][i] = Tileset.WALL;
                    board[p.getX() - 1][i] = Tileset.WALL;
                }
            }
        }

        public List<Position> getRightCorridorStart(BinaryRoom r) {
            List<Position> connections = new ArrayList<>();
            if (!r.isLeaf()) {
                if (r.getRightRoom() != null) {
                    connections.addAll(getRightCorridorStart(r.getRightRoom()));
                }
                if (r.getLeftRoom() != null) {
                    connections.addAll(getRightCorridorStart(r.getLeftRoom()));
                }
            } else {
                if (r.getTempRoom().getRight() == WIDTH - (WIDTH / 20)) {
                    return connections;
                }
                int temp = random.nextInt(100);
                if (temp <= 20) {
                    int tempRandom1 = random.nextInt(r.getTempRoom().getBottom() + 1,
                            r.getTempRoom().getTop());
                    connections.add(new Position(r.getTempRoom().getRight(), tempRandom1));
                }
                int tempRandom = random.nextInt(r.getTempRoom().getBottom() + 1,
                        r.getTempRoom().getTop() - 1);
                connections.add(new Position(r.getTempRoom().getRight(), tempRandom));
            }
            return connections;
        }

        public List<Position> getUpCorridorStart(BinaryRoom r) {
            List<Position> connections = new ArrayList<>();

            if (!r.isLeaf()) {
                if (r.getRightRoom() != null) {
                    connections.addAll(getUpCorridorStart(r.getRightRoom()));
                }
                if (r.getLeftRoom() != null) {
                    connections.addAll(getUpCorridorStart(r.getLeftRoom()));
                }
            } else {
                if (r.getTempRoom().getTop() == HEIGHT - (HEIGHT / 12)) {
                    return connections;
                }
                int temp = random.nextInt(100);
                if (temp <= 65) {
                    int tempRandom1 = random.nextInt(r.getTempRoom().getLeft() + 1,
                            r.getTempRoom().getRight());
                    connections.add(new Position(tempRandom1, r.getTempRoom().getTop()));
                }
                int tempRandom = random.nextInt(r.getTempRoom().getLeft() + 1,
                        r.getTempRoom().getRight());
                connections.add(new Position(tempRandom, r.getTempRoom().getTop()));
            }
            return connections;
        }

        private class UserIcon implements Serializable {
            TETile icon = Tileset.AVATAR;
            int x;
            int y;

            public UserIcon() {
                x = 0;
                y = 0;
            }

            public void placeCharacter(List<Position> l) {
                int r = random.nextInt(l.size());
                Position p = l.get(r);
                board[p.getX()][p.getY()] = icon;
                x = p.getX();
                y = p.getY();
            }

            public void setXandY(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public int getY() {
                return y;
            }

            public int getX() {
                return x;
            }
        }

        private class Pickup implements Serializable {
            TETile icon = Tileset.LIGHTSOURCE1;
            int x;
            int y;
            List<Position> light4Positions = new ArrayList<>();
            List<Position> light3Positions = new ArrayList<>();
            List<Position> light2Positions = new ArrayList<>();
            List<Position> light1Positions = new ArrayList<>();

            public Pickup() {
                x = 0;
                y = 0;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public void setXandY(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public void removeLights(int xpos, int ypos) {
                for (int x = xpos - 4; x <= xpos + 4; x++) {
                    for (int y = ypos - 4; y <= ypos + 4; y++) {
                        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
                            continue;
                        }
                        if (board[x][y] == Tileset.LIGHTSOURCE1) {
                            board[x][y] = Tileset.FLOOR;
                        }
                        if (board[x][y] == Tileset.FLOOR
                                || board[x][y] == Tileset.LIGHT4
                                || board[x][y] == Tileset.LIGHT3
                                || board[x][y] == Tileset.LIGHT2
                                || board[x][y] == Tileset.LIGHT1) {
                            board[x][y] = Tileset.FLOOR;
                        }
                    }
                }
            }

            public void createLight4(int xpos, int ypos) {
                ArrayList<Position> positionList = new ArrayList<>();
                for (int x = xpos - 4; x <= xpos + 4; x++) {
                    for (int y = ypos - 4; y <= ypos + 4; y++) {
                        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
                            continue;
                        }
                        if (board[x][y] == Tileset.LIGHTSOURCE1) {
                            continue;
                        }
                        if (board[x][y] == Tileset.FLOOR) {
                            board[x][y] = Tileset.LIGHT4;
                            positionList.add(new Position(x, y));
                        }
                    }
                }
                light4Positions = positionList;
            }

            public void createLight3(int xpos, int ypos) {
                ArrayList<Position> positionList = new ArrayList<>();
                for (int x = xpos - 3; x <= xpos + 3; x++) {
                    for (int y = ypos - 3; y <= ypos + 3; y++) {
                        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
                            continue;
                        }
                        if (board[x][y] == Tileset.LIGHTSOURCE1) {
                            continue;
                        }
                        if (board[x][y] == Tileset.FLOOR || board[x][y] == Tileset.LIGHT4) {
                            board[x][y] = Tileset.LIGHT3;
                            positionList.add(new Position(x, y));
                        }
                    }
                }
                light3Positions = positionList;
            }
            public void createLight2(int xpos, int ypos) {
                ArrayList<Position> positionList = new ArrayList<>();
                for (int x = xpos - 2; x <= xpos + 2; x++) {
                    for (int y = ypos - 2; y <= ypos + 2; y++) {
                        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
                            continue;
                        }
                        if (board[x][y] == Tileset.LIGHTSOURCE1) {
                            continue;
                        }
                        if (board[x][y] == Tileset.FLOOR || board[x][y] == Tileset.LIGHT4 || board[x][y] == Tileset.LIGHT3) {
                            board[x][y] = Tileset.LIGHT2;
                            positionList.add(new Position(x, y));
                        }
                    }
                }
                light2Positions = positionList;
            }

            public void createLight1(int xpos, int ypos) {
                ArrayList<Position> positionList = new ArrayList<>();
                for (int x = xpos - 1; x <= xpos + 1; x++) {
                    for (int y = ypos - 1; y <= ypos + 1; y++) {
                        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
                            continue;
                        }
                        if (board[x][y] == Tileset.LIGHTSOURCE1) {
                            continue;
                        }
                        if (board[x][y] == Tileset.FLOOR || board[x][y] == Tileset.LIGHT4 || board[x][y] == Tileset.LIGHT3 || board[x][y] == Tileset.LIGHT2) {
                            board[x][y] = Tileset.LIGHT1;
                            positionList.add(new Position(x, y));
                        }
                    }
                }
                light1Positions = positionList;
            }
        }

        private record Position(int x, int y) implements Serializable {

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }
        }

        public void quitAndSave(boolean b) {
            if (!b) {
                return;
            }
            try {
                File saveWorld = new File("gameState.txt");
                FileOutputStream stream = new FileOutputStream(saveWorld);
                ObjectOutputStream objectStream = new ObjectOutputStream(stream);
                objectStream.writeObject(this);
                objectStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static World getCurrentState() {
            File f = new File("gameState.txt");
            if (f.exists()) {
                try {
                    FileInputStream stream = new FileInputStream("gameState.txt");
                    ObjectInputStream objectStream = new ObjectInputStream(stream);
                    World tempWorld = (World) objectStream.readObject();
                    objectStream.close();
                    return tempWorld;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
