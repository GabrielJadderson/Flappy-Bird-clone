package com.jarvis.games.engine.game;

/**
 * 
 * @author Jubrail Faiz
 * @since 16-02-2014
 * the background
 */

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.Timer;

import com.jarvis.games.engine.handlers.FlappyImages;

/* Here's how it works:
 * 
 * 	||		||			||		||
 *	|| 		||	 -->  	||		||		--REPEAT-->
 * 	||		||			||		||
 * wall	   wall2	   wall2   wall(loops back around at a different height)
 * 
 */

public class Wall {

	public static Timer deadTimer;

	
	Random rnd = new Random();						//used to generate a random height for dat gap

	public int x ;											//the x position of the wall, always changing (right to left)
	public int y = rnd.nextInt(Game.HEIGHT - 400) + 200;	//generates the y value that is the top of the bottom wall
	public static int speed = - 6;							//scrolling speed
	public int WIDTH = 45;									//width of a wall, it's a constant 
	public int height = Game.HEIGHT - y;					//height of the wall, just the height of the window - how high the wall is
	public int GAP = 200;									//gap size (also a constant)

	public Wall(int i){								//allows me to differentiate the x positions of the two walls
		this.x = i;
	}

	//draws the wall
	public void paint(Graphics g){
		g.drawImage(FlappyImages.pipe, x, y, null);								//top part 
		g.drawImage(FlappyImages.pipe, x, ( -Game.HEIGHT ) + ( y - GAP), null);	//bottom part
	}

	public void move() {

		x += speed;								//scrolls the wall

		//These Rectanlges are used to detect collisions
		Rectangle wallBounds = new Rectangle(x, y, WIDTH, height);
		Rectangle wallBoundsTop = new Rectangle(x, 0, WIDTH, Game.HEIGHT - (height + GAP));

		//If birdman collids with a wall, he dies and  the game, bird, and walls are all reset
		if ( (wallBounds.intersects(BirdMan.getBounds()) ) || (wallBoundsTop.intersects(BirdMan.getBounds()))){
			BirdMan.reset();
			died();
		}

		//pushes the wall back to just off screen on the right when it gets offscreen on the left (the loop)
		if (x <= 0 - WIDTH){
			x = Game.WIDTH;
			y = rnd.nextInt(Game.HEIGHT - 400) + 200;
			height = Game.HEIGHT - y;
		}		
	}


	//this is executed on death, just sets a random y value and tells Game that the bird died :(
	public void died(){
		y = rnd.nextInt(Game.HEIGHT - 400) + 200;
		height = Game.HEIGHT - y; 
		Game.dead = true;
	}
}