package com.jarvis.games.engine;

import com.jarvis.games.engine.game.Game;
import com.jarvis.games.engine.game.Menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Gabriel Jadderson
 * @since 16-02-2014
 * Game class main handler.
 */

public class Launchbird
{
	
	/**
	 * number of miliseconds until refreshing screen
	 */
	private static final int refreshTime = 20;
	
	/**
	 * Houses all the Components where all this takes place in
	 */
	private final JFrame frame;
	/**
	 * the menu used in each instance
	 */
	private final Menu menu;
	/**
	 * the game used in each instance
	 */
	private final Game game;
	/**
	 * animation timer for the game
	 */
	private final Timer animationTimer;
	
	/**
	 * Declares JFrame with sane defaults and place it in the middle of the
	 * screen. Initializes Game and Menu
	 */
	public Launchbird()
	{
		menu = new Menu();
		game = new Game();
		frame = new JFrame();
		frame.setSize(Game.WIDTH, Game.HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(menu);
		
		menu.setVisible(true);
		
		animationTimer = new Timer(refreshTime, new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				game.repaint();
				game.move();
			}
			
			;
		});
		
		frame.revalidate(); //makes sure the menu is displayed (Step 1.5)
		frame.repaint();
	}
	
	/**
	 * Runs the game
	 *
	 * @throws InterruptedException
	 */
	public void runnit() throws InterruptedException
	{
		while (menu.startGame == false)
		{ //waits until the mouse is clicked in the Menu
			Thread.sleep(10);
		}
		frame.remove(menu); //Removes menu when mouse is clicked(Step 2)
		frame.add(game); //adds the game in its place (Step 3)
		game.setVisible(true); //makes sure the game is displayed (Step 3.5)
		frame.revalidate();
		animationTimer.start(); //begins animation timer, and the game begins
	}
}