package com.jarvis.games.engine.game;

/**
 * @author Gabriel Jadderson
 * @since 16-02-2014
 * Game class main handler.
 */

import com.jarvis.games.engine.handlers.FlappyImages;
import com.jarvis.games.engine.sound.SystemSound.FlappyBirdSounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Game extends JPanel
{
	
	public static final int HEIGHT = 800;            //height of the window
	public static final int WIDTH = 600;            //width of the window
	BirdMan birdy = new BirdMan();                    //makes a new bird
	Wall wall = new Wall(WIDTH);                    //makes the first wall you see
	Wall wall2 = new Wall(WIDTH + (WIDTH / 2));        //makes the second wall you see
	public static int score = 0;                    //the score (how many walls you've passed)
	public int scrollX = 0;                            //scrolls the background
	public static boolean dead = false;                //used to reset the walls
	public static String deathMessage = "";        // "you died, try again";
	
	private boolean opened = true;
	
	public Game()
	{
		//this mouseAdapter just listens for clicks, whereupon it then tells the bird to jump.
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.getButton() == e.BUTTON1)
				{
					birdy.jump();
				}
			}
		});
		addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == e.VK_SPACE)
				{
					birdy.jump();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
			}
			
			@Override
			public void keyTyped(KeyEvent e)
			{
			}
		});
	
		/* ALL ANIMATION MUST BE PLACED HERE involing the bird. */
		FlappyImages.bird = FlappyImages.bird_1;
		Timer timer = new Timer(200, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				opened = !opened;
				FlappyImages.bird = opened ? FlappyImages.bird_1 : FlappyImages.bird_4; // here we switch between a few sprites to animate the bird. not an efficient way. but probably the best without using any third-party libraries...
			}
		});
		timer.setRepeats(true);
		timer.setCoalesce(true);
		timer.start();
	}
	
	@SuppressWarnings("static-access")
	public void paint(Graphics g)
	{
		super.paint(g);
		
		g.drawImage(FlappyImages.background, scrollX, 0, null);                    //there are two backgrounds so you get that seamless transition, this is the first
		g.drawImage(FlappyImages.background, scrollX + 1800, 0, null);            //number 2, exactly one background length away (1800 pixels)
		
		wall.paint(g);            //paints the first wall
		wall2.paint(g);            //the second wall
		birdy.paint(g);            //the wee little birdy
		
		g.setFont(new Font("comicsans", Font.BOLD, 40));
		
		paintScore(g);
		
		g.drawString(deathMessage, 10, 200);                //paints "" if the player has not just died, paints "you died, try again" if the user just died
	}
	
	@SuppressWarnings("static-access")
	public void move()
	{
		
		wall.move();            //moves the first wall
		wall2.move();            //moves the second wall
		birdy.move();            //moves the wee little birdy
		
		scrollX += Wall.speed;    //scrolls the wee little background
		
		if (scrollX == -1800)    //this loops the background around after it's done
			scrollX = 0;
		
		if (dead)
		{                //this block essentially pushes the walls back 600 pixels on birdy death
			wall.x = 600;
			wall2.x = 600 + (WIDTH / 2);
			dead = false;
			
			FlappyBirdSounds.playHitSound();
			Timer deathSoundTimer = new Timer(100, new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					FlappyBirdSounds.playDeathSound();
				}
				
				;
			});
			deathSoundTimer.setRepeats(false);
			deathSoundTimer.setCoalesce(true);
			deathSoundTimer.start();
			
			try
			{
				Thread.sleep(1000);
			} catch (Exception e)
			{
				e.getLocalizedMessage();
			}
			
		}
		
		if ((wall.x == BirdMan.X) || (wall2.x == BirdMan.X))    //Increments the score when the player passes a wall
			score();
	}
	
	public static void score()
	{
		score += 1;
		FlappyBirdSounds.playPointSound();
	}
	
	
	private void paintScore(Graphics g)
	{
		switch (score)
		{ //the worst possible way to handle scoring.... TODO: remove this and make a more intelligent system.
		/* 1s */
			case 0:
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 1:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 2:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 3:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 4:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 5:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 6:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 7:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 8:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 9:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 10s */
			case 10:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 11:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 12:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 13:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 14:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 15:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 16:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 17:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 18:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 19:
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		
		/* 20s */
			case 20:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 21:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 22:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 23:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 24:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 25:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 26:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 27:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 28:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 29:
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 30s */
			case 30:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 31:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 32:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 33:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 34:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 35:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 36:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 37:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 38:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 39:
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 40s */
			case 40:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 41:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 42:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 43:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 44:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 45:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 46:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 47:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 48:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 49:
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 50s */
			case 50:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 51:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 52:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 53:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 54:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 55:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 56:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 57:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 58:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 59:
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 60s */
			case 60:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 61:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 62:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 63:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 64:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 65:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 66:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 67:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 68:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 69:
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 70s */
			case 70:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 71:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 72:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 73:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 74:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 75:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 76:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 77:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 78:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 79:
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 80s */
			case 80:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 81:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 82:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 83:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 84:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 85:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 86:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 87:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 88:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 89:
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
		/* 90s */
			case 90:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
			case 91:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_1, WIDTH / 2 - 20, 700, null);
				break;
			case 92:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_2, WIDTH / 2 - 20, 700, null);
				break;
			case 93:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_3, WIDTH / 2 - 20, 700, null);
				break;
			case 94:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_4, WIDTH / 2 - 20, 700, null);
				break;
			case 95:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_5, WIDTH / 2 - 20, 700, null);
				break;
			case 96:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_6, WIDTH / 2 - 20, 700, null);
				break;
			case 97:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_7, WIDTH / 2 - 20, 700, null);
				break;
			case 98:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_8, WIDTH / 2 - 20, 700, null);
				break;
			case 99:
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 35, 700, null);
				g.drawImage(FlappyImages.score_9, WIDTH / 2 - 20, 700, null);
				break;
			
			default:
				g.drawImage(FlappyImages.score_0, WIDTH / 2 - 20, 700, null);
				break;
		}
	}
	
}