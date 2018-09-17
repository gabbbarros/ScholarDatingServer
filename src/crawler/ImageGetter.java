package crawler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageGetter {
	public static BufferedImage getPhoto(String userId) {
		if(userId == null)
			return null;
		String url = CoAuthor.photoURL_START + userId + CoAuthor.photoURL_END;
		
		try {
			URL url2 = new URL(url);
			
			BufferedImage image = ImageIO.read(url2);
			return image;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		//BufferedImage image = getPhoto("OK9tw1AAAAAJ");
		BufferedImage image = getPhoto("q4Qs3_AAAAAJ");
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.setVisible(true);System.out.println("here");
	}
}
