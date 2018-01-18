package main;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;


public class Snake implements ActionListener, KeyListener {

    public static Snake snake;

    public JFrame jframe;
    public Square square, target;
    public Board board;
    public int SPEED = 10;
    public Timer timer = new Timer(500 / SPEED, this);

    public ArrayList<Point> snakeParts = new ArrayList<>();
    public ArrayList<Square> openPoints = new ArrayList<>();
    public ArrayList<Square> closedPoints = new ArrayList<>();


    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, SCALE = 15, WIDTH = 410, HEIGHT = 450;

    public int ticks = 0;
    public int direction = DOWN;
    public int tailLength = 10;

    public Point head, cherry;

    public Random random;

    public boolean Gameover = false, paused;


    public Snake() {
        jframe = new JFrame("SnakeAI");
        jframe.setVisible(true);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setResizable(false);
        jframe.add(board = new Board());
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        Gameover = false;
        paused = true;
        tailLength = 3;
        ticks = 0;
        direction = DOWN;
        head = new Point(10, 0);
        random = new Random();
        snakeParts.clear();
        cherry = new Point(random.nextInt((WIDTH / SCALE) - 2), random.nextInt((HEIGHT / SCALE) - 3));
        timer.start();
        System.out.println("H = " + String.valueOf(getHeuristic(head, cherry)));


    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        if (head != null && !Gameover && !paused) {
            snakeParts.add(new Point(head.x, head.y));


            if (direction == UP) {
                if (head.y - 1 >= 0 && noTailAt(head.x, head.y - 1)) {
                    head = new Point(head.x, head.y - 1);
                } else {
                    Gameover = true;

                }
            }

            if (direction == DOWN) {
                if (head.y + 1 < (HEIGHT / SCALE) - 2 && noTailAt(head.x, head.y + 1)) {
                    head = new Point(head.x, head.y + 1);
                } else {
                    Gameover = true;
                }
            }

            if (direction == LEFT) {
                if (head.x - 1 >= 0 && noTailAt(head.x - 1, head.y)) {
                    head = new Point(head.x - 1, head.y);
                } else {
                    Gameover = true;
                }
            }

            if (direction == RIGHT) {
                if (head.x + 1 < (WIDTH / SCALE) && noTailAt(head.x + 1, head.y)) {
                    head = new Point(head.x + 1, head.y);
                } else {
                    Gameover = true;
                }
            }

            if (snakeParts.size() > tailLength) {
                snakeParts.remove(0);

            }


            System.out.println(head.toString() + "  to :  " + cherry.toString());
            getWalkable(new Square(head));
            //int[] squareScore = new int[closedPoints.size()];
            for (Square i : openPoints) {
                if (snakeParts.contains(i)) {
                    openPoints.remove(i);
                }
            }
            for (Square i : closedPoints) {
                Square point = i;
                int h ;
                if (openPoints.contains(i))
                    point = openPoints.get(openPoints.indexOf(i));

                if (target == null)
                    target = point;
                    h = getHeuristic(point.location,cherry);

                if (target != null && point.getSquareScore() < target.getSquareScore()
                        || h<getHeuristic(point.location,cherry))
                    target = point;

                System.out.println("Point: " + point.getLocation());
                System.out.println("H = " + point.getSquareScore());
            }
            direction = getDirection(head, target.getLocation());
            System.out.println("Head :" + new Square(head).getLocation());
            System.out.println("Target :" + target.location);
            System.out.println("Cherry :" + cherry);

            System.out.println();
            System.out.println("Direction :" + direction);

            if (cherry != null) {
                if (head.equals(cherry)) {
                    tailLength++;
                    int x = random.nextInt((WIDTH / SCALE) - 2);
                    int y = random.nextInt((HEIGHT / SCALE) - 4);
                    for (Point i : snakeParts) {
                        Point cherry = new Point(x, y);
                        if (cherry.equals(i) || new Point(x + 1, y + 1).equals(i)) {
                            x = random.nextInt((WIDTH / SCALE) - 2);
                            y = random.nextInt((HEIGHT / SCALE) - 4);
                        }
                    }
                    cherry.setLocation(x, y);
                    System.out.println("Got it");
                    System.out.println("//////////////////////////////////");
                }
            }
            target = null;
            board.repaint();

        }
    }

    public boolean noTailAt(int x, int y) {
        for (Point point : snakeParts) {
            if (point.equals(new Point(x, y))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        snake = new Snake();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int i = e.getKeyCode();

        if ((i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) && direction != RIGHT) {
            direction = LEFT;
        }

        if ((i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) && direction != LEFT) {
            direction = RIGHT;
        }

        if ((i == KeyEvent.VK_W || i == KeyEvent.VK_UP) && direction != DOWN) {
            direction = UP;
        }

        if ((i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) && direction != UP) {
            direction = DOWN;
        }

        if (i == KeyEvent.VK_SPACE) {
            if (Gameover) {
                startGame();
            } else {
                paused = !paused;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public int getHeuristic(Point headPoint, Point goalPoint) {
        /*
         * To get the estimated movement cost from the head point to the cherry point.
         * The Euclidean distance s the "ordinary" straight-line distance between two points
         */
        double Gx = goalPoint.getX();
        double Hx = headPoint.getX();
        double Gy = goalPoint.getY();
        double Hy = headPoint.getY();
        Double distance = Math.abs((Gx - Hx)) + Math.abs(Gy - Hy);
        return distance.intValue() * SCALE;
    }

    public void getWalkable(Square square) {
        closedPoints.clear();
        int Hx = square.getX();
        int Hy = square.getY();

        Point W1 = new Point(Hx - 1, Hy);
        Point W2 = new Point(Hx + 1, Hy);
        Point W3 = new Point(Hx, Hy - 1);
        Point W4 = new Point(Hx, Hy + 1);

        Point[] allPoints = {W1, W2, W3, W4};

        for (Point i : allPoints) {
            if (!isWall(i) && !snakeParts.contains(i) && !snakeParts.contains(new Point(i.x + 1, i.y))
                    && !snakeParts.contains(new Point(i.x - 1, i.y)) && !snakeParts.contains(new Point(i.x, i.y + 1))
                    && !snakeParts.contains(new Point(i.x, i.y - 1))) {

                square = new Square(i);
                square.setHeuristic(getHeuristic(i, cherry));
                square.setLocation(i);
                closedPoints.add(square);
                if (!openPoints.contains(square)) {
                    openPoints.add(square);
                } else {
                    square.setCost(square.getCost() + 1);
                }
            }
            if (isWall(i)) {
                System.out.println("Wall found!");
            }
        }
    }


    public boolean isWall(Point point) {
        return (point.x < 0 || point.y < 0) ? true : false;
    }

    public int getMinScore(int[] scorePoints) {
        Arrays.sort(scorePoints);

        return scorePoints[0];
    }

    public int getDirection(Point head, Point target) {
        int headX = head.x;
        int headY = head.y;
        if (target.x > headX && headY == target.y && direction != LEFT) {
            return RIGHT;
        } else if (target.x < headX && headY == target.y && direction != RIGHT)
            return LEFT;
        else if (target.x == headX && target.y > headY && direction != UP)
            return DOWN;
        else if (target.x == headX && target.y < headY && direction != DOWN)
            return UP;
        else {
            return 202;
        }
    }



}
