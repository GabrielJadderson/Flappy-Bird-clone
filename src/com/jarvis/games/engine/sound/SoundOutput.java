package com.jarvis.games.engine.sound;

/*This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation, version 2
 of the License.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 MA 02111-1307, USA.
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

/**
 * This class handles an OGG sound sample whose data are loaded prior to
 * playback to improve the performance (instead of streaming at real time). The
 * only drawback of this method is not to support samples with variable rates.
 * Moreover, the use is split in several steps to handle the resource carefully.
 * It is well fitted for programs that require a fine control on sounds, for
 * example games using lots of noises. It is possible to prepare a sample
 * without opening it in order to avoid busying a line uselessly. On the other
 * hand, it is possible to close a sample for the same reason. I do not try to
 * reopen the underlying clip because it is highly probable to fail and it is
 * more efficient to close the previous line cleanly and then to get a fresh
 * line by taking into account the modifications of the context that might have
 * happened between the latest opening and the latest closure (for example, the
 * mixer used previously might have become unavailable).
 * 
 * @author Julien Gouesse & Jubrail Faiz
 * 
 */
public final class SoundOutput {

	private Clip clip;

	private byte[] uncompressedDataArray;

	private AudioFormat audioFormat;

	/**
	 * 
	 * @param inputStream
	 * @throws IllegalArgumentException
	 */
	public SoundOutput(InputStream inputStream) throws IllegalArgumentException {
//		 pausedFramePosition=0;
		Map.Entry<byte[], int[]> result = loadOgg(inputStream);
		int rate = result.getValue()[0];
		int channels = result.getValue()[1];
		uncompressedDataArray = result.getKey();
		audioFormat = new AudioFormat(rate, 16, channels, true, false);
		
		if (uncompressedDataArray == null)
			throw new IllegalArgumentException("Sound sample not supported, data loading failed");
	}

	public SoundOutput(File file) throws IllegalArgumentException,
	FileNotFoundException {
		this(new BufferedInputStream(new FileInputStream(file)));
	}

	public SoundOutput(URL url) throws IllegalArgumentException,
	FileNotFoundException, URISyntaxException {
		this(new BufferedInputStream(new FileInputStream(new File(url.toURI()))));
	}

	public SoundOutput(SoundOutput soundHandler) {
		uncompressedDataArray = soundHandler.uncompressedDataArray;
		audioFormat = soundHandler.audioFormat;
		clip = null;
	}

	/**
	 * Open a sample to prepare it to be played. Do it prior to play the sample.
	 * 
	 * @throws IllegalArgumentException
	 */
	public final void open() throws IllegalArgumentException {
		if (clip != null && clip.isOpen())
			throw new UnsupportedOperationException("Impossible to open an already opened sample");
		// Now we are sure that the clip is null or closed
		DataLine.Info info = new DataLine.Info(Clip.class, audioFormat,
				AudioSystem.NOT_SPECIFIED);
		if (AudioSystem.isLineSupported(info)) {
			Mixer mixer = getBestFittedMixer(info);
			try {
				clip = (Clip) mixer.getLine(info);
				clip.open(audioFormat, uncompressedDataArray, 0, uncompressedDataArray.length);
			} catch (LineUnavailableException lue) {
				throw new IllegalArgumentException("Sound sample not supported", lue);
			}
		} else
			throw new IllegalArgumentException("Sound sample not supported, no line supported");
	}
	
	public final boolean isOpen() {
		return clip.isOpen();
	}

	/**
	 * Load an OGG file
	 * 
	 * @param inputStream
	 * @return data, rate and channels
	 */
	@SuppressWarnings("resource")
	private static final Map.Entry<byte[], int[]> loadOgg(InputStream inputStream) {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		byteOutputStream.reset();
		byte[] tmpBuffer = new byte[10240 * 8]; //increased from * 4 to * 8, for big files.
		OggInputStream oggInputStream = new OggInputStream(inputStream);
		boolean done = false;
		int bytesRead;
		while (!done) {
			try {
				bytesRead = oggInputStream.read(tmpBuffer, 0, tmpBuffer.length);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				bytesRead = 0;
			}
			byteOutputStream.write(tmpBuffer, 0, bytesRead);
			done = (bytesRead != tmpBuffer.length || bytesRead < 0);
		}
		byte[] uncompressedData = byteOutputStream.toByteArray();
		return (new SimpleEntry<byte[], int[]>(uncompressedData, new int[] {
				oggInputStream.getRate(), oggInputStream.getFormat() }));
	}

	/**
	 * Reopen a closed sample Notice that a new line might be used
	 * 
	 * @return true if the sample is already usable or if the operation has been
	 *         successful false if the operation has failed
	 */
	public final boolean reopen() {
		boolean success = true;
		if (clip != null && !clip.isOpen())
			try {
				open();
			} catch (IllegalArgumentException iae) {
				success = false;
			}
		return (success);
	}

	/**
	 * Release the line and the native resources NB: On Mac, the JVM 1.5 freezes
	 * after this call
	 */
	public final void close() {
		if ((clip != null && !clip.isOpen()) || clip == null)
			throw new UnsupportedOperationException(
					"Impossible to close a closed sample or a never-opened sample");
		try {
			clip.close();
		} catch (SecurityException se) {
			se.printStackTrace();
		}
	}

	/**
	 * Play the sample once from the beginning
	 */
	public final void play() {
		loop(0);
	}

	/**
	 * Pause the sample
	 */
	public final void pause() {
		if ((clip != null && !clip.isOpen()) || clip == null)
			throw new UnsupportedOperationException(
					"Impossible to pause a closed sample");
		if (clip.isRunning())
			clip.stop();
	}

	/**
	 * Resume a paused sample from the paused frame and play it count times Do
	 * nothing if the sample was not paused
	 * 
	 * @param count
	 */
	public void resume(int count) {
		if ((clip != null && !clip.isOpen()) || clip == null)
			throw new UnsupportedOperationException(
					"Impossible to resume a closed sample");
		if (!clip.isRunning()) {// FIXME: the line below should not be used,
			// that's the only way I've found
			// to repair this class when resuming a loop
			// clip.setFramePosition(0);
			clip.loop(count);
		}
	}

	/**
	 * Play it indefinitely from the beginning
	 */
	public final void loop() {
		loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Play it count times from the beginning
	 * 
	 * @param count
	 */
	public final void loop(int count) {
		if ((clip != null && !clip.isOpen()) || clip == null)
			throw new UnsupportedOperationException(
					"Impossible to play a closed sample");
		if (clip.isRunning())
			stop();
		clip.loop(count);
	}
	
	/**
	 * 
	 * @param gainLevel in decibels
	 */
	public final void setGain(float gainLevel) { 
		FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(gainLevel); // Reduce volume by 10 decibels.
	}
	
	/**
	 * 
	 * @param volumeLevel
	 */
	public final void setVolume(float volumeLevel) { 
		FloatControl volumeControl =  (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
		volumeControl.setValue(volumeLevel); // Reduce volume by 10 decibels.
	}

	/**
	 * Stop the sample and rewind it
	 */
	public final void stop() {
		if ((clip != null && !clip.isOpen()) || clip == null)
			throw new UnsupportedOperationException(
					"Impossible to stop a closed sample");
		clip.stop();
		clip.setFramePosition(0);
	}

	public final boolean isRunning() {
		return (clip != null && clip.isOpen() && clip.isRunning());
	}

	/**
	 * Try to find a mixer that both supports this line and supports as much
	 * line as possible
	 */
	private static final Mixer getBestFittedMixer(DataLine.Info info) {
		Mixer currentMixer = null;
		Mixer.Info[] mi = AudioSystem.getMixerInfo();
		Mixer bestMixer = null;
		for (int i = 0; i < mi.length; i++) {
			currentMixer = AudioSystem.getMixer(mi[i]);
			if (currentMixer.isLineSupported(info))
				if (bestMixer == null
				|| bestMixer.getMaxLines(info) < currentMixer
				.getMaxLines(info))
					bestMixer = currentMixer;
		}
		// The best mixer cannot be null as AudioSystem.isLineSupported returned
		// true
		return (bestMixer);
	}
}