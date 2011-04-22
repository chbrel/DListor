/*
* Copyright 2007-2011, Hélène Collavizza, Jean-Paul Stromboni
* 
* This file is part of project 'Modele_de_Jeu'
* 
* 'Modele_de_Jeu' is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* 'Modele_de_Jeu'is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with 'Modele_de_Jeu'. If not, see <http://www.gnu.org/licenses/>.
*/
/** 
Cette classe abstraite est un Frame associ� � une instance de voix 
 * SI_VOX et qui impl�mente KeyListener.
 * Elle peut servir de classe m�re � toutes les fen�tres de vos jeux :
 * il suffit de d�finir la m�thode "init" pour initialiser les �l�ments du Frame
 */


package devintAPI;

import t2s.SIVOXDevint; // pour parler

import javax.swing.*;
import java.awt.event.*;


/** Classe abstraite avec un Frame, une instance de SI_VOX pour parler et 
 * qui écoute les événements clavier.
 * Par défaut, un son est lu à l'activation de la fenétre, 
 * on sort de la fenêtre par ESC et on obtient la régle du jeu par F1, l'aide par F2
 * 
 * @author helene
 *
 */
public abstract class DevintFrame extends JFrame implements  KeyListener{

	// la voix pour annoncer les actions
    protected SIVOXDevint voix; 
    
    // le fichier wav contenant le message d'accueil (lu é l'activation du jeu)
    protected String sonAccueil;

    // le fichier wav contenant le but du jeu (activé par F1)
    protected String sonRegle;

    // le fichier wav contenant le message d'aide (activé par F2)
    protected String sonAide;
   

    /**
     * @param title : titre de la fenétre
     * @param sonAccueil : nom du fichier .wav qui contient le message d'accueil 
     * @param sonAide : nom du fichier .wav qui contient le message d'aide
     * PRECOND : il s'agit de .wav qui se trouvent dans le répertoire "ressources/sons"
     */
    public DevintFrame(String title, String ac, String ar,String ai) {
    	super(title);
       	sonAccueil = ac; 
       	sonAide = ai;
            sonRegle = ar;
        // visible
    	this.setVisible(true);
    	// a le focus
    	this.requestFocus();
    	// prend toute la taille de la fenétre
    	this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // on ferme la fenétre en cliquant sur la croix 
    	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
     	// écoute les événements clavier
       	addKeyListener(this);
       	// méthode init é implémenter, elle construit ce qui est dans le frame
       	init();
       	// toujours annoncer ce que l'on doit faire en entrant dans une fenétre
    	voix = new SIVOXDevint();
    	
    	Thread tSon = new Thread() {
    		public void run() {
    			voix.playWav("../ressources/sons/" + sonAccueil);
    		}
    	};
    	tSon.start();
    }


    /** méthode abstraite à implémenter 
     * pour définir ce qu'il y a dans le Frame
     */
    protected abstract void init();

    //////////////////////////////////////////////
    // Gestion des événements clavier
    /////////////////////////////////////////////
    
    // méthodes nécessaires pour l'interface KeyListener
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e){}

    /** gestion des touches
     * ESC fait sortir de la fenétre courante
     * F1 invoque l'aide
     * Cette méthode peut étre surchargée par héritage pour réagir à d'autres touches
     * (voir un exemple dans la classe Jeu)
     */
    public void keyPressed(KeyEvent e) {
    	// toujours arréter la voix courante quand l'utilisateur fait une action
    	voix.stop();
    	// escape = sortir
    	if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
    		dispose();
    	}
    	// F1 = regle du jeu
    	if (e.getKeyCode()==KeyEvent.VK_F1){
    		voix.playWav("../ressources/sons/" + sonRegle);
    	}

    	// F2 = aide
    	if (e.getKeyCode()==KeyEvent.VK_F2){
    		voix.playWav("../ressources/sons/" + sonAide);
    	}

    }
}
