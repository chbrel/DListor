package fr.unice.polytech.devint.dlistor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import devintAPI.DevintFrame;

public class LauncherFrame extends DevintFrame {

	private GridBagLayout placement;
	private GridBagConstraints regles;

	private Color couleurTexte;
	private Color couleurTexteSelectionne;
	private Color couleurBouton;
	private Color couleurBoutonSelectionne;
	private Font fonteBouton;

	private JButton[] boutonOption;
	private File[] fileRepertoires;
	private Integer[] gamesAnnee;
	private String[] gamesShortDescription;
	private String[] gamesPublic;
	private String[] excludedDirectories;
	
	private JPanel boutonsPane;
	private JScrollPane boutons;

	// l'option courante qui est sélectionnée
	private int optionCourante;

	static final int HBOUTON = 120;   // hauteur des boutons
	private int nbOptions;
	// pour le scroll
	private static final boolean UP = true;
	private static final boolean DOWN = false;
	Dimension scSize ;
	// l'indice de la vue visible
	int placeView = 0;
	// nb de boutons visibles pour 1 vue
	int nbBoutonByView = 0;
	
	static final Dimension SCREEN_DIM = Toolkit.getDefaultToolkit().getScreenSize();
	static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	private JPanel rightPanel;
	private JLabel descriptionLabel;
	private JPanel mainPane;
	
	// le répertoire qui contient les jeux
	//    private String pathRepertoire = "../../";
	private String pathRepertoire;

	public LauncherFrame() {
		super("Jeux DeViNT", "listorAccueil.wav", "aide.wav", "aide.wav");
	}

	protected void init() {
		this.pathRepertoire = ".."+File.separator+".."+File.separator;
		//this.excludedDirectories = new String[]{"jre","lib","Listor","DListor","Aide"};
		this.excludedDirectories = new String[]{"jre","lib","DListor","Aide"};
		
		this.optionCourante = -1;

		creerLayout();
		creerAttributs();

		Entete entete = new Entete(couleurTexteSelectionne, couleurBoutonSelectionne);
		placement.setConstraints(entete, regles);
		this.add(entete);
		
		this.boutons = creerOption();

		creerMainPane();
		
		// poids relatif de 3 (i.e 3 fois plus grand que l'entête
		regles.weighty=6;
		// on ajuste verticalement et horizontalement
		regles.fill = GridBagConstraints.BOTH;
		placement.setConstraints(this.mainPane, regles);
		this.add(this.mainPane);

		JButton quitter = creerQuitter();
		regles.weighty=1;
		// espace vertical avant de le placer
//		regles.ipady=1000;
		// on ajuste seulement horizontalement
		regles.fill = GridBagConstraints.BOTH;
		placement.setConstraints(quitter, regles);
		this.add(quitter);
		
		scSize = this.boutons.getSize();
		nbBoutonByView = (scSize.height / HBOUTON)-1;
		
		Dimension boutonsPaneDim = new Dimension(scSize.width, this.nbOptions * HBOUTON);
		this.boutonsPane.setSize(boutonsPaneDim);
		this.boutonsPane.setPreferredSize(boutonsPaneDim);
		this.boutonsPane.setMaximumSize(boutonsPaneDim);
		
		Dimension appDim = new Dimension(SCREEN_DIM.width, SCREEN_DIM.height-100);
		
		this.setSize(appDim);
		this.setPreferredSize(appDim);
		this.setMinimumSize(appDim);
		this.setMaximumSize(appDim);
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.pack();
	}
	
	private void creerMainPane() {
		this.mainPane = new JPanel();
		//this.mainPane.setPreferredSize(new Dimension(800,600));
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		this.mainPane.setLayout(layout);
		//TODO là aussi
		//this.boutons.setPreferredSize(new Dimension(600,600));
		this.boutons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.5;
		c.weighty = 1.0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		
		this.mainPane.add(this.boutons, c);
		
		this.rightPanel = new JPanel();
		GridBagLayout layoutRP = new GridBagLayout();
		GridBagConstraints cRP = new GridBagConstraints();
		cRP.fill = GridBagConstraints.BOTH;
		cRP.weightx = 1.0;
		cRP.weighty = 1.0;
		cRP.gridwidth = 1;
		cRP.gridx = 0;
		cRP.gridy = 0;
		this.rightPanel.setLayout(layoutRP);
		this.rightPanel.setBackground(Color.WHITE);
		this.descriptionLabel = new JLabel("<html></html>");
		
//		this.descriptionLabel.setPreferredSize();
		
		this.rightPanel.add(this.descriptionLabel, cRP);

		Dimension dRP = new Dimension(10,600);
		this.rightPanel.setSize(dRP);
		this.rightPanel.setPreferredSize(dRP);
		this.rightPanel.setMaximumSize(dRP);
		this.rightPanel.setMinimumSize(dRP);
		
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 0.25;
		c.gridx = 4;
		c.gridy = 0;
		
		this.mainPane.add(rightPanel, c);
	}

	/** créé le layout pour placer les composants
	 */
	private void creerLayout() {
		placement = new GridBagLayout();
		regles = new GridBagConstraints();
		this.setLayout(placement);
		// par défaut on étire les composants horizontalement et verticalement
		regles.fill = GridBagConstraints.BOTH;
		// par défaut, tous les composants ont un poids de 1
		// on les répartit donc équitablement sur la grille
		regles.weightx = 1; 
		regles.weighty = 1;
		// espaces au bord des composants
		regles.insets = new Insets(10, 50, 10, 50);
		// pour placer en haut des zones
		regles.anchor= GridBagConstraints.NORTH;
		//pour aller à la ligne (chaque composant occupe tout le reste de la ligne)
		regles.gridwidth=GridBagConstraints.REMAINDER;
	}

	/** créé les attributs (couleurs, fonte, ...)
	 */
	private void creerAttributs() {
		// la couleur des textes 
		couleurTexte = Color.WHITE;
		couleurTexteSelectionne = new Color(10,0,150);
		// mise à jour des attributs des boutons
		fonteBouton = new Font("Tahoma",1,56);
		couleurBouton = new Color(10,0,150);
		couleurBoutonSelectionne = Color.WHITE;
	}

	/** 
	 * creer les boutons associés aux noms d'options
	 */
	private JScrollPane creerOption() {
		// affectation des tableaux liés aux répertoires des jeux
		File f = new File(this.pathRepertoire);
		String[] nomRepertoires = f.list();
		this.fileRepertoires = new File[nomRepertoires.length-this.excludedDirectories.length];//On enlève jre,lib,Listor,Aide

		// création des boutons
		// panel des boutons
		this.boutonsPane = new JPanel();
		//TODO Ajout là aussi
//		this.boutonsPane.setPreferredSize(new Dimension(600,600));
//		this.boutonsPane.setPreferredSize(new Dimension(600, boutons.getPreferredSize().height));
		this.boutonsPane.setLayout(new GridLayout(fileRepertoires.length, 1));

		// les boutons
		this.boutonOption = new JButton[fileRepertoires.length];
		// description des jeux
		this.gamesAnnee = new Integer[fileRepertoires.length];
		this.gamesShortDescription = new String[fileRepertoires.length];
		this.gamesPublic = new String[fileRepertoires.length];

		this.nbOptions = 0;

		for(int i =0; i < nomRepertoires.length; i++) {
			// n'affiche que les repertoires
			if (new File(pathRepertoire + "" + nomRepertoires[i]).isDirectory() 
					&& !excluded(nomRepertoires[i])) {

				// si le fichier d'info est present dans le projet :
				if ((f = new File(pathRepertoire + File.separator + nomRepertoires[i] + File.separator + "doc" + File.separator + "infos.xml")).exists()){
							creerBouton(nbOptions, nomRepertoires[i], f);
							this.boutonsPane.add(boutonOption[nbOptions]);
							nbOptions++;
				}
			}
		}

		// le scoll qui contient les boutons
		return new JScrollPane(this.boutonsPane);
	}
	
	private boolean excluded(String repertoire) {
		for (int i = 0; i < excludedDirectories.length; i++) {
			if(excludedDirectories[i].equals(repertoire)) {
				return true;
			}
		}
		return false;
	}

	private JButton creerQuitter() {
		// bouton pour quitter
		JButton quitter = new JButton("Quitter");
		quitter.setBackground(Color.YELLOW);
		quitter.setFont(fonteBouton);
		quitter.setBorder(new LineBorder(Color.BLACK,5));
		Dimension d = new Dimension(40,100);
		quitter.setSize(d);
		quitter.setPreferredSize(d);
		quitter.setMinimumSize(d);
		quitter.setMaximumSize(d);
		quitter.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});

		return quitter;
	}

	// ------------------------------
	// méthodes de gestion des boutons

	private void creerBouton(final int i, String nomRepertoire, File infosXml) {
		Integer annee = new Integer(0);
		String shortDescription = "";
		String publicString = "";
		String projTitle = "";

		SAXBuilder builder = new SAXBuilder();
		try {
			Element xmlRoot = builder.build(infosXml).getRootElement();

			annee = Integer.decode(xmlRoot.getChildText("year"));

			shortDescription = xmlRoot.getChildText("shortdescription");

			publicString = xmlRoot.getChildText("public");
			projTitle = xmlRoot.getChildText("title");

		} catch (JDOMException e) {

		} catch (IOException e) {

		}

		this.boutonOption[i] = new JButton();
		this.boutonOption[i].setText(projTitle);
		this.setProperties(this.boutonOption[i]);
		this.boutonOption[i].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (optionCourante!=-1) unFocusedButton(optionCourante);
				optionCourante=i;
				setFocusedButton(optionCourante);
				lancer(i);
			}

		});

		this.gamesAnnee[i] = annee;
		this.gamesShortDescription[i] = shortDescription;
		this.gamesPublic[i] = publicString;

		this.fileRepertoires[i] = (new File(pathRepertoire + File.separator + nomRepertoire)).getAbsoluteFile();

	}

	// mettre à jour les propriétés des boutons
	private void setProperties(JButton b) {
		b.setFocusable(false);
		b.setBackground(couleurBouton);	
		b.setForeground(couleurTexte);
		b.setFont(fonteBouton);
		b.setBorder(new LineBorder(Color.BLACK,5));
		//TODO Ici est le prefered size des boutons.
		b.setPreferredSize(new Dimension(SCREEN_WIDTH-200,HBOUTON));
	}

	// mettre le focus sur une option
	private void setFocusedButton(int i){
		voix.playText(boutonOption[i].getText());
		boutonOption[i].setBackground(couleurBoutonSelectionne);
		boutonOption[i].setForeground(couleurTexteSelectionne);
		
		String descriptionContent = "<html>";
		descriptionContent += "<div style=\"font-size: 18px;\"><u style=\"color:blue;font-weight:bold;\">Titre:</u> " + this.boutonOption[i].getText() + "</div><br/>";
		descriptionContent += "<div style=\"font-size: 18px\"><u style=\"color:blue;font-weight:bold;\">Année:</u> " + this.gamesAnnee[i] + "</div><br/>";
		descriptionContent += "<div style=\"font-size: 18px\"><u style=\"color:blue;font-weight:bold;\">Public:</u> " + this.gamesPublic[i] + "</div><br/>";
		descriptionContent += "<div style=\"font-size: 18px\"><u style=\"color:blue;font-weight:bold;\">Description:</u> " + this.gamesShortDescription[i] + "</div><br/>";
		descriptionContent += "</html>";
		
		this.descriptionLabel.setText(descriptionContent);
				
		this.pack();
	}

	private void unFocusedButton(int i){
		boutonOption[i].setBackground(couleurBouton);
		boutonOption[i].setForeground(couleurTexte);
	}

	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);

		// enter = sélectionner l'option
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
			lancer(optionCourante);
		}

		// se déplacer dans les options vers le bas
		if (e.getKeyCode() == KeyEvent.VK_DOWN){
			if (optionCourante==-1) {
				optionCourante = 0;
				setFocusedButton(optionCourante);
			} 
			else {
				placeView++;
				unFocusedButton(optionCourante);	    
				optionCourante = (optionCourante+1)%nbOptions;
				scrolle(DOWN,optionCourante);
				setFocusedButton(optionCourante);
			}
		}	 
		// se déplacer dans les options vers le haut
		if (e.getKeyCode() == KeyEvent.VK_UP){
			if (optionCourante==-1) {
				optionCourante = 0;
				setFocusedButton(optionCourante);
			} 
			else {
				placeView--;
				unFocusedButton(optionCourante);	     
				optionCourante = optionCourante-1;
				if (optionCourante==-1) optionCourante = nbOptions-1;
				scrolle(UP,optionCourante);
				setFocusedButton(optionCourante);
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			String gameDesc = "Titre du jeu: " + this.boutonOption[optionCourante].getText() + ".";
			gameDesc += "Public: ";
			if(this.gamesPublic[optionCourante].equals("MV")) {
				gameDesc += "Mal Voyant.";
			} else if(this.gamesPublic[optionCourante].equals("NV")) {
				gameDesc += "Non Voyant.";
			} else if(this.gamesPublic[optionCourante].equals("MV et NV")) {
				gameDesc += "Mal Voyant et Non Voyant.";
			}
			gameDesc += "Description: ";
			gameDesc += this.gamesShortDescription[optionCourante] + ".";
			
			voix.playText(gameDesc);
		}
	}

	// pour scroller pour voir toutes les options
	private void scrolle(boolean sens, int optionCourante) {
		JViewport p = boutons.getViewport();
		int viewX = (int)p.getViewPosition().getX();
		if (optionCourante==0) {
			p.setViewPosition(new Point(viewX,0));
		} else {
			if ( UP &&  placeView < 0) {
				int nvllePlace = (optionCourante-nbBoutonByView)*HBOUTON;
				p.setViewPosition(new Point(viewX,nvllePlace)); 
				placeView = nbBoutonByView;
			}
			else if (placeView > nbBoutonByView) {
				int nvllePlace = (optionCourante-1)*(HBOUTON);
				p.setViewPosition(new Point(viewX,nvllePlace)); 
				placeView = 0;
			}
		}
	}

	// lance le jeu associé au bouton n°i
	private void lancer(int i) {
		try{
			File repExec = new File(fileRepertoires[i], "bin");
			String command = "";

			if(OSValidator.isWindows()) {
				command = repExec + File.separator + "execution.bat";
			} else if(OSValidator.isUnix()) {
				command = repExec + File.separator + "execution.sh";
			} else if(OSValidator.isMac()) {
				command = repExec + File.separator + "execution.sh";
			}

			final Process process = 
				Runtime.getRuntime().exec(command,null,repExec);
			//consommation des flux de sortie du processus lancé
			new Thread() {
				public void run() {
					try {
						BufferedReader reader =
							new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line = "";
						try {
							while ((line=reader.readLine()) != null) {
							}
						} finally {
							reader.close();
						}
					} catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}.start();

			boolean finished= false;
			while(!finished) {
				try {
					Thread.sleep(10);
					// on utilise une exception générée par le Thread
					process.exitValue();
					finished=true;
				} catch (IllegalThreadStateException e) {
					//le process n'est pas encore terminé
				}
			}
			if(finished) {
				this.requestFocus();
				//on dit ce texte quand on sort du jeu lancé
				voix.playWav("../ressources/wav/listorRetour.wav");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
