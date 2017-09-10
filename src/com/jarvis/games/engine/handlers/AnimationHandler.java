package com.jarvis.games.engine.handlers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Gabriel Jadderson
 * @since 16-02-2014
 * Animation Handling
 */

public class AnimationHandler
{
	
	private static boolean animate = true; // used in all methods to act as a switch in the timers.
	
	private static Image $IMAGE_0 = null; // used for #createAnimation(...).
	private static Image $IMAGE_1 = null; // used for #loadAnimation(...).
	private static Image Last;
	
	public static final synchronized void createAnimation(Image FIRST_IMAGE, Image SECOND_IMAGE, Image LAST_IMAGE, int TIMER_DELAY, int TIMER_INITIAL_DELAY, boolean REPEAT_ANIMATION, boolean COALESCE)
	{
		final Image first = FIRST_IMAGE;
		final Image second = SECOND_IMAGE;
		
		$IMAGE_0 = first;


//		LAST_IMAGE = first;
		
		Timer timer = new Timer(TIMER_DELAY, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				animate = !animate;
				Last = animate ? first : second;
			}
		});
		timer.setInitialDelay(TIMER_INITIAL_DELAY);
		timer.setRepeats(REPEAT_ANIMATION);
		timer.setCoalesce(COALESCE);
		timer.start();
	}
	
	public static final synchronized void loadAnimation(String IMAGE_1, String IMAGE_2, int TIMER_DELAY, int TIMER_INITIAL_DELAY, boolean REPEAT_ANIMATION, boolean COALESCE)
	{
		final Image img1;
		final Image img2;
		
		img1 = ImageUtil.loadImage(IMAGE_1);
		img2 = ImageUtil.loadImage(IMAGE_2);
		$IMAGE_1 = img1;
		
		Timer timer = new Timer(TIMER_DELAY, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				animate = !animate;
				$IMAGE_1 = animate ? img1 : img2;
			}
		});
		timer.setInitialDelay(TIMER_INITIAL_DELAY);
		timer.setRepeats(REPEAT_ANIMATION);
		timer.setCoalesce(COALESCE);
		timer.start();
	}
	
	
}
