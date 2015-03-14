package com.jarvis.games.engine.handlers;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;
import java.util.HashMap;

/**
 * @author Jubrail Faiz
 * @since 18-02-2014
 * Image loading	
 */

public class ImageUtil {

    public static HashMap<String, Image> images = new HashMap<String, Image>();

    /**
     * Loads an Image from the given file path.
     * @param filepath : The path to the image requested.
     * @return An Image of the given file path.
     */
    public static Image loadImage(String filepath) {
    	Image image = null;
    	if (images.containsKey(filepath)) {
    		return images.get(filepath);
    	} else {
    		try {
    			image = ImageIO.read(new File(filepath));
    			images.put(filepath, image);
    		} catch (Exception e) {e.getLocalizedMessage();}
    	}
    	return image;
    }

    /**
     * Loads a BufferedImage from the given file path.
     * @param filepath : The path to the image requested.
     * @return A BufferedImage of the given file path.
     */
    public static BufferedImage loadBufferedImage(String filepath) {
    	BufferedImage image = null;
    	if (images.containsKey(filepath)) {
    		return (BufferedImage) images.get(filepath);
    	} else {
        	try {
        		image = ImageIO.read(new File(filepath));
        		if (!images.containsKey(filepath)) {
        			images.put(filepath, image);
        		}
        	} 
        	catch (Exception e) {e.getLocalizedMessage();}
    	}
    	return image;
    }
}