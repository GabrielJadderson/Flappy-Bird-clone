package com.jarvis.games.engine.game;

/**
 * @author Gabriel Jadderson
 * @since 16-02-2014
 * this class contains info about the bird itself
 */

import com.jarvis.games.engine.handlers.FlappyImages;
import com.jarvis.games.engine.sound.SystemSound.FlappyBirdSounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BirdMan
{
	
	public static int DIAMETER = 25;                                    //Diameter of the bird.
	public static int X = (Game.WIDTH / 2) - (DIAMETER / 2);        //The x position of the bird. Does not change at any time. Should be exactly centered.
	public static int y = Game.HEIGHT / 2;                                //The STARTING y position of the bird. Will change constantly.
	private static int height = Game.HEIGHT - 195;                        //The height of the bird, this changes as the bird jumps.
	public static int acceleration = 1;                                    //Used in the gravity simulation below.
	public static int speed = 2;                                        //The speed at which the bird will fall (constantly increased by acceleration (1))
	
	private boolean opened = true; //->deleted<-
	
	public BirdMan()
	{
	}
	
	//This is called when the bird jumps (on mouse click). It just temporarily sets the speed to -17 (arbitrary number), then is slowly taken back down because 
	//of "gravity"
	public void jump()
	{
		speed = -17;
		FlappyBirdSounds.playWingSound();
	}
	
	//all movement stuff is here 
	public static void move()
	{
		
		//only moves if the bird is between the top and bottom of the window
		if ((y > 0) && (y < height))
		{
			speed += acceleration;    //Here's the gravity I was talking about the speed is just increased by 1 all the time, even after a jump
			y += speed;        //The actual movement, y location equals (where it was) + (how far it should go)
		} else
		{ //or else the game resets (Bird is dead!)
			reset();    //rests bird's postion, actual method below
			Game.dead = true;    //bird is dead! This is used in the Main method to reset the walls after a death
		}
		
	}
	
	public static void reset()
	{     //called after the bird dies
		y = Game.HEIGHT / 2;    //resets position, speed, etc.
		speed = 2;
		Game.score = 0;
		Game.deathMessage = "";    //also shows this lovely message
		//This timer just makes the message dissapear after 3000 milliseconds
		Timer deathTimer = new Timer(3000, new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				Game.deathMessage = "";
			}
			
			;
		});
		deathTimer.start();
	}
	
	public static void paint(Graphics g)
	{
		g.drawImage(FlappyImages.bird, X, y, 50, 50, null);     //paints the bird's icon
	}
	
	public static Rectangle getBounds()
	{
		return new Rectangle(X, y, DIAMETER, DIAMETER);  //Gives a rectangle used to detect collisions in the Wall class
	}
	
}