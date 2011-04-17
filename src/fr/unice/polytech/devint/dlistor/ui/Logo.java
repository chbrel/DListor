package fr.unice.polytech.devint.dlistor.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Logo extends JPanel{
	BufferedImage bi = null;

	public void paintComponent(Graphics g){
		this.setBounds(5, 5, 120, 60);

		if (bi != null){
			Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(bi,null, 0, 0);
		}
	}

	public void loadLogo(){
		File l = new File("../ressources/img/logoDeViNT.gif");

		try{
			bi = ImageIO.read(l);
			repaint();
		}
		catch(IOException e){
			System.out.println("/!\\ Error while opening file logo\n");
		}
	}
}