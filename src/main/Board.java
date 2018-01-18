package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;


public class Board extends JPanel
{

	public static final Color GREEN = new Color(1666073);

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Snake snake = Snake.snake;

		g.setColor(GREEN);
		
		g.fillRect(0, 0, Snake.WIDTH, Snake.HEIGHT);
		g.setColor(Color.white);

		for (int x = 0; x <= Snake.WIDTH * Snake.SCALE; x += Snake.SCALE) {
            g.drawLine(x, 0, x, Snake.HEIGHT * Snake.SCALE);
        }
		for (int y = 0; y <= Snake.HEIGHT * Snake.SCALE; y += Snake.SCALE) {
			g.drawLine(0, y, Snake.WIDTH * Snake.SCALE, y);
		}


		g.setColor(Color.BLUE);

		for (Point point : snake.snakeParts)
		{
			g.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		}
		g.setColor(Color.ORANGE);

		g.fillRect(snake.head.x * Snake.SCALE, snake.head.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		
		g.setColor(Color.RED);
		
		g.fillRect(snake.cherry.x * Snake.SCALE, snake.cherry.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		
//
	}
}
