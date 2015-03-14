package com.jarvis.games.engine.sound;

import java.io.File;

/**
 * 
 * @author Jubrail Faiz
 * sound class
 */

public class Sound extends Thread {
	
	public static final float GAIN_ZERO = 0.0f; //null
	public static final float GAIN_MAX = 6.0f; // maks
	
	private static final String songs[] = {
		"sounds/FlappyBird/sfx_die.ogg", 
		"sounds/FlappyBird/sfx_hit.ogg",
		"sounds/FlappyBird/sfx_point.ogg",
		"sounds/FlappyBird/sfx_swooshing.ogg",
		"sounds/FlappyBird/sfx_wing.ogg"};
	
	public Sound(int SONG_ID, int LOOP_COUNT, boolean LOOP) {
		setDaemon(true);
		playSound(SONG_ID, LOOP_COUNT, LOOP);
		start();
	}
	
	private final void playSound(int SONG_ID, int REPEAT_TIMES, boolean LOOP) {
		boolean exception = false;
		try {
			File file = new File(songs[SONG_ID]);
			SoundOutput sound = new SoundOutput(file);
			if (LOOP && REPEAT_TIMES <= 0) {
				sound.open();
				sound.setGain(GAIN_ZERO);
				sound.loop();
			} else if (!LOOP && REPEAT_TIMES == 0) {
				sound.open();
				sound.setGain(GAIN_ZERO);
				sound.play();
			} else if (LOOP && REPEAT_TIMES > 0) {
				sound.open();
				sound.setGain(GAIN_ZERO);
				sound.loop(REPEAT_TIMES);
			} 
		} catch (Exception e) {
			exception = true;
			e.printStackTrace();
		} finally {
			if (exception == false) {
				System.out.println("[Sound] played " + songs[SONG_ID]);
			}
		}
	}
}
