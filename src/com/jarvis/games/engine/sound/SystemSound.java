package com.jarvis.games.engine.sound;

/**
 * @author Gabriel Jadderson
 * handles all internal sounds for games and GUI's
 */

public class SystemSound
{
	
	/**
	 * Sound management for FlappyBird
	 */
	public static class FlappyBirdSounds
	{
		public static final synchronized void playDeathSound()
		{
			new Sound(0, 0, false);
		}
		
		public static final synchronized void playHitSound()
		{
			new Sound(1, 0, false);
		}
		
		public static final synchronized void playPointSound()
		{
			new Sound(2, 0, false);
		}
		
		public static final synchronized void playSwooshingSound()
		{
			new Sound(3, 0, false);
		}
		
		public static final synchronized void playWingSound()
		{
			new Sound(4, 0, false);
		}
	}
	
} /* ============================================== SYSTEM SOUND ================================================== */
