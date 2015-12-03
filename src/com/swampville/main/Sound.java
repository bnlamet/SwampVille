package com.swampville.main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound {

	public static void playSound(String filename){
		URL url = Main.class.getResource("/sounds/" + filename);
		AudioClip clip = Applet.newAudioClip(url);
		clip.play();
	}
	
}
