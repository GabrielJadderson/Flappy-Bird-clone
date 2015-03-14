package com.jarvis.games.engine.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.jarvis.games.engine.game.Game;
import com.jarvis.games.engine.game.Menu;

/**
 * @author Jubrail Faiz
 * @since 16-02-2014
 * Game frame.		
 */

public class GUI extends JFrame {
	
	public static final long serialVersionUID = 1L;

	/** number of miliseconds until refreshing screen*/
	public static final int refreshTime = 20;

	/** Houses all the Components where all this takes place in*/
	public final JFrame q;
	/** the menu used in each instance */
	public final Menu menu; 
	/** the game used in each instance */
	public final Game game; 
	/** animation timer for the game */
	public final Timer animationTimer;
	
	public GUI() {
	    menu = new Menu();
	    game = new Game();
	    q = new JFrame();
	    q.setSize(Game.WIDTH, Game.HEIGHT);
	    q.setVisible(true);
	    q.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    q.setLocationRelativeTo(null);
	    q.add(menu);

	    menu.setVisible(true);

	    animationTimer = new Timer(refreshTime, new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	            game.repaint();
//	            game.move();
	        };
	    });
	    q.revalidate(); //makes sure the menu is displayed (Step 1.5)
	    q.repaint();
	}
}
