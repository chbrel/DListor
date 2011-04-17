package fr.unice.polytech.devint.dlistor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Entete extends JPanel {

	public Entete(Color couleurTexteSelectionne, Color couleurBoutonSelectionne) {
		FlowLayout enteteLayout = new FlowLayout();
		enteteLayout.setAlignment(FlowLayout.CENTER);
		this.setLayout(enteteLayout);
		this.setBorder(new LineBorder(Color.GRAY,8));

		// le label
		JLabel lb1 = new JLabel("Jeux DeViNT");
		lb1.setFont(new Font("Georgia",1,96));
		lb1.setForeground(couleurTexteSelectionne);
		lb1.setBackground(couleurBoutonSelectionne);
		this.add(lb1);

		// le logo
		Logo logo = new Logo();
		this.add(logo);
		logo.loadLogo();
	}
}
