
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Fenetre extends JFrame {
	
	private JPanel conteneur;
	private FlowLayout flow_layout;
	private JMenuBar menu_bar;
	private JMenu menu_fichier, menu_aide;
	private JMenuItem item_quitter, item_rech_mise_a_jour;
	private JOptionPane boite_conf_mise_ajr, boite_info_mise_ajr_ok, boite_info_pas_de_mise_ajr, boite_info_erreur_de_mise_ajr;
	private JTextField champ_recherche;
	private JButton btn_recherche;
	private Font police_1, police_2;
	private JTextArea area_recherche, area_nbr_resultat;
	private JTable table;
	private JScrollPane scroll_pane_1, scroll_pane_2, scroll_pane_3;
	
	String sources;
	Indexer indexer;
	Requete saisie;
	//ArrayList<String> resultat_a_afficher;
	Object[][] resultat_table;
	
	// Constructeur
	public Fenetre(){
		sources = "Assets/AP/";
		indexer= new Indexer(sources);
		
		this.setTitle("Moteur de recherche - RD");
		this.setSize(new Dimension(1100,700));
		this.setResizable(true);
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
		new Font("Arial", Font.ITALIC, 10);
		police_1 = new Font("Arial", Font.BOLD, 12);
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
		item_rech_mise_a_jour = new JMenuItem("Rechecher les mises � jour...");
		menu_fichier.add(item_quitter);
		menu_aide.add(item_rech_mise_a_jour);
		menu_bar.add(menu_fichier);
		menu_bar.add(menu_aide);
		
		// Ajout d'un champ de recherche
		champ_recherche = new JTextField();
		champ_recherche.setPreferredSize(new Dimension(900,35));
		champ_recherche.setFont(police_2);
		
		// Ajout d'un bouton recherche
		btn_recherche = new JButton("Rechercher");
		btn_recherche.setBackground(new Color(104, 168, 218));
		
		// Text area pour afficher la recherche saisie par l'utilisateur
		area_recherche = new JTextArea(1,91);
		area_recherche.setFont(police_2);
		area_recherche.setEditable(false);
		area_recherche.setBackground(null);
	    scroll_pane_2 = new JScrollPane(area_recherche);
	    scroll_pane_2.setBorder(BorderFactory.createEmptyBorder());
	    
	  //Text area pour afficher le nombre de r�sultatde la recherche
	    area_nbr_resultat = new JTextArea(1,91);
		area_nbr_resultat.setFont(police_1);
		area_nbr_resultat.setEditable(false);
		area_nbr_resultat.setBackground(null);
		
		// Ajout du textarea au scroll pane 
	    scroll_pane_3 = new JScrollPane(area_nbr_resultat);
	    scroll_pane_3.setBorder(BorderFactory.createEmptyBorder());		
		
		// Ajout des diff�rents composants au conteneur principal
		conteneur.add(champ_recherche);
		conteneur.add(btn_recherche);
		conteneur.add(scroll_pane_2);
		conteneur.add(scroll_pane_3);
		
		
		// Fermer l'appli quand on clique sur quitter (menu)
		item_quitter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			});		
		
		// V�rification des mises � jour
		item_rech_mise_a_jour.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evenement) {
				// Cr�ation de l'index
				indexer= new Indexer(sources);
				
				int non_indexe = indexer.nEstPasAJour();
				
				// Si l'index n'est pas � jour
				if(non_indexe>0){
					boite_conf_mise_ajr = new JOptionPane();
					int option = boite_conf_mise_ajr.showConfirmDialog(
							null, "Index n'est pas a jour, voulez-vous le mettre a jour ?", "Mise a jour de l'index",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					// Si btn = ok
					if(option == JOptionPane.OK_OPTION){
						// Lancement des mises � jour
						indexer.indexation();
						// � la fin de la mise � jour afficher une boite d'information
						boite_info_mise_ajr_ok = new JOptionPane();
						boite_info_mise_ajr_ok.showMessageDialog(null, "Mise a jour teminee, l'index est a jour", "Information",
						JOptionPane.INFORMATION_MESSAGE);					
					}
					
				}else {
					if(!indexer.chargerIndex()) {
						boite_info_erreur_de_mise_ajr = new JOptionPane();
						boite_info_erreur_de_mise_ajr.showMessageDialog(null, "Erreur lors de la recuperation de l'index", "Erreur",
								JOptionPane.INFORMATION_MESSAGE);
					}else {
						boite_info_pas_de_mise_ajr = new JOptionPane();
						boite_info_pas_de_mise_ajr.showMessageDialog(null, "Aucune mise a jour trouvee, l'index est a jour", "Information",
							JOptionPane.INFORMATION_MESSAGE);
					}
				}
			  }
			});
		
		// Bouton rechercher
		btn_recherche.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evenement){				
				if(!champ_recherche.getText().equals("")) {
					saisie = new Requete(indexer.getIndex(),new File(sources),champ_recherche.getText());
					ArrayList<File> resultats = saisie.documentsCorrespondants(indexer.getIndex());
					
					//resultats.add(new File("File04")); 
					//resultats.add(new File("File05"));
					//System.out.println(resultats);
					// Remplissage de la table d'affichage
					/*resultat_a_afficher = new ArrayList<>();
					for(int i=0; i<resultats.size(); i++) {
						resultat_a_afficher.addAll((Collection<? extends String>) resultats.get(i));
					}*/					
					
					resultat_table = new Object[resultats.size()][1];
					for(int i=0; i<resultats.size(); ++i) {
							resultat_table[i][0] = resultats.get(i);							
					}
					
					/*System.out.println("********* Affichage ********\n");
					for(int x=0; x<resultat_table.length; ++x) {
						System.out.println(resultat_table[x][0]);
					}*/
					
					
					// Titres de la table d'affichage
					String[] titres = {"<html><b>RESULTATS DE LA RECHERCHE</b></html>"};					
					
					// Cr�ation de la table qui recevera les r�sultats de la recherche 
					table = new JTable();
					DefaultTableModel tableModel = new DefaultTableModel(resultat_table, titres);
			        table.setModel(tableModel);
					TableColumn col = table.getColumnModel().getColumn(0);
			        col.setPreferredWidth(850);
			        table.setRowHeight(25);        
					table.setPreferredScrollableViewportSize(new Dimension(1000,490));
					table.setFillsViewportHeight(true);
					table.setBackground(Color.WHITE);
					
					// Ajout de la table au scroll pane
					scroll_pane_1 = new JScrollPane(table);
					// Ajout du scroll pane au panel
					conteneur.add(scroll_pane_1);					
					
					// Affichage requete utilisateur
					area_recherche.setText(" \" "+champ_recherche.getText()+" \" ");
					// Affichage nombre de resultats trouves
					area_nbr_resultat.setText(table.getRowCount()+" resultats trouves ");
					// Reinitialiser le champ de recherche
					champ_recherche.setText("");					
				}
			}
		});
	}
}