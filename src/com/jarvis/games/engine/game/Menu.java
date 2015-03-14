package com.jarvis.games.engine.game;

/**
 * 
 * @author Jubrail Faiz
 * @since 16-02-2014
 * Start Menu.
 */

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.jarvis.games.engine.handlers.FlappyImages;

/*
 * This screen just waits for a mouseclick, whereupon it is removed from the JFrame and replaced with the game
 * I'd like not have to wait for the picture to download though, so I need to figure out how to package the images
 * within the Jar file. 
 */

public class Menu extends JPanel{
	
	public int scrollX = 0;								//scrolls the background
	
	private static final long serialVersionUID = 1L;
	public int highscore;
	public boolean startGame = false;						//the boolean toggle that starts the game over in ExecuteMe


	public Menu(){
		setFocusable(true);							//waits for a mouseclick, then toggles startGame
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				startGame = true;
			}

		});
	}
	
	public void moveMenu(){
	
		scrollX += -3;
		
		if (scrollX == -1800)	//this loops the background around after it's done
			scrollX = 0;
		
	}

	public void paint (Graphics g){
		super.paint(g);
		
		g.drawImage(FlappyImages.background, scrollX, 0, null);					//there are two backgrounds so you get that seamless transition, this is the first			
		g.drawImage(FlappyImages.background, scrollX + 1800, 0, null);			//number 2, exactly one background length away (1800 pixels)
		
		g.drawImage(FlappyImages.splashScreen, (Game.WIDTH / 2) - (FlappyImages.splashScreen.getWidth(this)/2)+10, (Game.HEIGHT/2) - (FlappyImages.splashScreen.getHeight(this)/2), null);
		repaint();

	}
}
