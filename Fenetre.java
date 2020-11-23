import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Fenetre extends JFrame {
	
	private JPanel conteneur;
	private FlowLayout flow_layout;
	private JMenuBar menu_bar;
	private JMenu menu_fichier, menu_aide;
	private JMenuItem item_quitter, item_rech_mise_a_jour;
	private JOptionPane boite_conf_mise_ajr, boite_info_mise_ajr_ok, boite_info_pas_de_mise_ajr;
	private JTextField champ_recherche;
	private JButton btn_recherche;
	private Font police_1, police_2;
	private JLabel label_titre, label_nbr_resultat;
	private JTextArea area_recherche;
	private JTable table;
	private JScrollPane scroll_pane_1, scroll_pane_2;
	
	// Constructeur
	public Fenetre(){
		this.setTitle("Projet - Recherche documentaire");
		this.setSize(new Dimension(1000,720));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initComposant();
		this.setJMenuBar(menu_bar);
		this.setContentPane(conteneur);
		this.setVisible(true);
	}	
	
	// Initialisation des composants
	public void initComposant() {
		conteneur = new JPanel();
		police_1 = new Font("Arial", Font.ITALIC, 10);
		police_2 = new Font("Arial", Font.ITALIC, 12);
		flow_layout = new FlowLayout();
		flow_layout.setHgap(10);
		flow_layout.setVgap(15);		
		conteneur.setLayout(flow_layout);

		// Ajout d'un menu
		menu_bar = new JMenuBar();
		menu_fichier = new JMenu("Fichier");
		menu_aide = new JMenu("Aide ?");
		item_quitter = new JMenuItem("Quitter");
		item_rech_mise_a_jour = new JMenuItem("Rechecher les mises à jour...");
		menu_fichier.add(item_quitter);
		//menu_fichier.addSeparator();
		menu_aide.add(item_rech_mise_a_jour);
		menu_bar.add(menu_fichier);
		menu_bar.add(menu_aide);
		
		champ_recherche = new JTextField();
		champ_recherche.setPreferredSize(new Dimension(800,30));
		champ_recherche.setFont(police_2);
		
		btn_recherche = new JButton("Rechercher");
		label_titre = new JLabel("Résultats de la recherche");
		label_nbr_resultat = new JLabel();
		//Text area pour afficher la recherche saisie par l'utilisateur
		area_recherche = new JTextArea(1,110);
		area_recherche.setFont(police_1);
		area_recherche.setEditable(false);
		area_recherche.setBackground(null);
	    scroll_pane_2 = new JScrollPane(area_recherche);	    
		
		// Titres des colonnes de la JTable
		String[] titres = {"N°", "ID", "C1", "C2", "C3"};
		// Il sera rempli par les résultats de la recherche
		Object[][] resultats = {
				{"Test n°","Test ID","Test c1","Test c2","Test c3"}				
		};
		
		// Création de la table qui recevera les résultats de la recherche 
		table = new JTable(resultats, titres);
		table.setPreferredScrollableViewportSize(new Dimension(900,450));
		table.setFillsViewportHeight(true);
		
		scroll_pane_1 = new JScrollPane(table);
		// Ajout des différents composants au conteneur principal
		conteneur.add(champ_recherche);
		conteneur.add(btn_recherche);
		conteneur.add(label_titre);
		conteneur.add(scroll_pane_1);
		//conteneur.add(table.getTableHeader());
		conteneur.add(label_nbr_resultat);
		conteneur.add(scroll_pane_2);
		
		
		// Fermer l'appli quand on clique sur quitter (menu)
		item_quitter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			});		
		
		// Vérification des mises à jour
		item_rech_mise_a_jour.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evenement) {
				// on lance la vérification
				// S'il n y a pas de mises à jour à effectuer
				// afficher une boite d'information
				/*boite_info_pas_de_mise_ajr = new JOptionPane();
				boite_info_pas_de_mise_ajr.showMessageDialog(null, "Aucune mise à jour trouvée", "Information",
						JOptionPane.INFORMATION_MESSAGE);*/
				
				
				// S'il y a des mises à jour à effectuer
				boite_conf_mise_ajr = new JOptionPane();
				int option = boite_conf_mise_ajr.showConfirmDialog(
						null, "Voulez-vous lancer la mise à jour ?", "Mise à jour",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				// Si ok
				if(option == JOptionPane.OK_OPTION){
					// Lancement des mises à jour
					
					// à la fin afficher une boite d'information
					boite_info_mise_ajr_ok = new JOptionPane();
					boite_info_mise_ajr_ok.showMessageDialog(null, "Mise à jour teminée", "Information",
					JOptionPane.INFORMATION_MESSAGE);
					
				}
			}
			});
		
		// Bouton rechercher
		btn_recherche.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evenement){				
				if(!champ_recherche.getText().equals("")) {
					// Appel
					
					
					
					// Nombre de résultats trouvés
					String nbr_resultat = Integer.toString(table.getRowCount())+" résultats trouvés ";
					label_nbr_resultat.setText(nbr_resultat);
					// Affichage de la recherche utilisateur
					area_recherche.setText(" \" "+champ_recherche.getText()+" \" ");
					champ_recherche.setText("");
				}
			}
		});
		
	}
	
	
}