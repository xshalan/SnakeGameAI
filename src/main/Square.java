package main;

import java.awt.*;

public class Square  {

    Point location ;



    int x;
    int y;
    int Heuristic ;
    int Cost = 1 ;
    int squareScore ;

    public Square(Point location) {
        this.location = location;
        this.x = location.x ;
        this.y = location.y ;
    }

    public int getSquareScore() {
        squareScore = Heuristic + Cost ;
        return squareScore;
    }

    public void setSquareScore(int squareScore) {
        this.squareScore = squareScore;
    }

    public int getX() {
        return location.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return location.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getHeuristic() {
//        /*
//         * To get the estimated movement cost from the head point to the cherry point.
//         * The Manhattan distance is the sum of the absolute values of the horizontal and the vertical distance
//         */
//        Heuristic = Math.abs(goalPoint.x - location.x) + Math.abs(goalPoint.y - location.y);
        return Heuristic;
    }

    public void setHeuristic(int heuristic) {
        Heuristic = heuristic;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }
}
