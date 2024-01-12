package byow.Core;

import java.io.Serializable;
import java.util.Random;

/** This is creates pseudo-random generated rooms utilizing binary space partioning and
 * procedural generating algorithms
 *
 * @Author David Kim
 */

@Source
public class BinaryRoom implements Serializable {
    private static final int MIN_WIDTH = 6;
    private static final int MAX_WIDTH = 40;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_HEIGHT = 30;

    private boolean horizontalSplit;
    private boolean verticalSplit;

    private BinaryRoom leftRoom;
    private BinaryRoom rightRoom;
    private final Room tempRoom;
    private final Random random;

    public BinaryRoom(int left, int right, int top, int bottom, Random r) {
        tempRoom = new Room(left, right, top, bottom);
        random = r;
        horizontalSplit = false;
        verticalSplit = false;
        leftRoom = null;
        rightRoom = null;
    }

    public boolean isLeaf() {
        return (!horizontalSplit) && (!verticalSplit);
    }

    public Room getTempRoom() {
        return tempRoom;
    }

    public BinaryRoom getLeftRoom() {
        return leftRoom;
    }

    public BinaryRoom getRightRoom() {
        return rightRoom;
    }

    public void randomVerticalSplit() {
        int i = random.nextInt(100);
        int rTop = random.nextInt(2);
        int rBottom = random.nextInt(2);
        if (i <= 10) {
            leftRoom = null;
            rightRoom = new BinaryRoom(tempRoom.right + 3 - (tempRoom.right - tempRoom.left) / 2,
                    tempRoom.right, tempRoom.top + rTop, tempRoom.bottom + rBottom, random);
            verticalSplit = true;
            return;
        }
        if (i >= 89) {
            leftRoom = new BinaryRoom(tempRoom.left,
                    (tempRoom.right - tempRoom.left) / 2 + tempRoom.left - 3,
                    tempRoom.top + rTop, tempRoom.bottom + rBottom, random);
            rightRoom = null;
            verticalSplit = true;
            return;
        }
        leftRoom = new BinaryRoom(tempRoom.left,
                (tempRoom.right - tempRoom.left) / 2 + tempRoom.left - 3,
                tempRoom.top + rTop, tempRoom.bottom + rBottom, random);
        rightRoom = new BinaryRoom(tempRoom.right + 3 - (tempRoom.right - tempRoom.left) / 2,
                tempRoom.right, tempRoom.top + rTop, tempRoom.bottom + rBottom, random);
        verticalSplit = true;
    }

    public void forceVerticalSplit() {
        leftRoom = new BinaryRoom(tempRoom.left,
                (tempRoom.right - tempRoom.left) / 2 + tempRoom.left - 3,
                tempRoom.top, tempRoom.bottom, random);
        rightRoom = new BinaryRoom(tempRoom.right + 3 - (tempRoom.right - tempRoom.left) / 2,
                tempRoom.right, tempRoom.top, tempRoom.bottom, random);
        verticalSplit = true;
    }

    public void randomHorizontalSplit() {
        int i = random.nextInt(100);
        int rTop = random.nextInt(2);
        int rBottom = random.nextInt(2);
        if (i <= 10) {
            leftRoom = null;
            rightRoom = new BinaryRoom(tempRoom.left, tempRoom.right,
                    tempRoom.top - 3 - (tempRoom.top - tempRoom.bottom) / 2 + rTop,
                    tempRoom.bottom + rBottom, random);
            horizontalSplit = true;
            return;
        }
        if (i >= 89) {
            leftRoom = new BinaryRoom(tempRoom.left, tempRoom.right, tempRoom.top + rTop,
                    (tempRoom.top - tempRoom.bottom) / 2 + tempRoom.bottom + 3 + rBottom, random);
            rightRoom = null;
            horizontalSplit = true;
            return;
        }
        leftRoom = new BinaryRoom(tempRoom.left, tempRoom.right, tempRoom.top + rTop,
                (tempRoom.top - tempRoom.bottom) / 2 + tempRoom.bottom + 3 + rBottom, random);
        rightRoom = new BinaryRoom(tempRoom.left, tempRoom.right,
                tempRoom.top - 3 - (tempRoom.top - tempRoom.bottom) / 2 + rTop, tempRoom.bottom + rBottom, random);
        horizontalSplit = true;
    }

    public void forceHorizontalSplit() {
        leftRoom = new BinaryRoom(tempRoom.left, tempRoom.right, tempRoom.top,
                (tempRoom.top - tempRoom.bottom) / 2 + tempRoom.bottom + 3, random);
        rightRoom = new BinaryRoom(tempRoom.left, tempRoom.right,
                tempRoom.top - 3 - (tempRoom.top - tempRoom.bottom) / 2, tempRoom.bottom, random);
        horizontalSplit = true;
    }

    public void split() {
        int tempRandom = random.nextInt(2);
        if (tempRoom.getWidth()
                > ((Engine.WIDTH - (Engine.WIDTH / 20)) - (Engine.WIDTH / 20)) / 2) {
            forceVerticalSplit();
            return;
        } else if (tempRoom.getHeight()
                > ((Engine.HEIGHT - (Engine.HEIGHT / 12)) - (Engine.HEIGHT / 12)) / 2) {
            forceHorizontalSplit();
            return;
        }
        if (tempRandom == 0 && tempRoom.getWidth() >= 2 * MIN_WIDTH) {
            randomVerticalSplit();
        } else if (tempRoom.getHeight() >= 2 * MIN_HEIGHT) {
            randomHorizontalSplit();
        }
    }

    public record Room(int left, int right, int top, int bottom) {

        public int getWidth() {
            return right - left;
        }

        public int getHeight() {
            return top - bottom;
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }

        public int getTop() {
            return top;
        }

        public int getBottom() {
            return bottom;
        }
    }
}
