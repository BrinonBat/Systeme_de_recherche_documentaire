
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
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
	private static final long serialVersionUID = 1L;
	private JPanel conteneur;
	private FlowLayout flow_layout;
	private JMenuBar menu_bar;
	private JMenu menu_options;
	private JMenuItem item_changeDossier,item_quitter;
	private JTextField champ_recherche;
	private JButton btn_recherche;
	private Font police_1, police_2;
	private JTextArea area_recherche, area_nbr_resultat;
	private JTable table;
	private JScrollPane scroll_pane_1, scroll_pane_2, scroll_pane_3;
	
	int NB_RESULTS_AFFICHES=100;
	String sources;
	Indexer indexer;
	Requete saisie;
	ArrayList<String> resultat_a_afficher;
	Object[][] resultat_table;
	
	// Constructeur
	public Fenetre(){
		sources = "Assets/AP/"; // sources par défaut
		indexer= new Indexer(sources);
		int non_indexe = indexer.nEstPasAJour();

		// Si l'index n'est pas à jour
		if(non_indexe>0){
			new JOptionPane();
			int option = JOptionPane.showConfirmDialog(
					null, "l'index n'est pas à jour de "+non_indexe+" fichiers, voulez-vous le mettre à jour ?", "Mise a jour de l'index",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			// Si btn = ok
			if(option == JOptionPane.OK_OPTION){
				// Lancement des mises à jour
				indexer.indexation();
				// à la fin de la mise à jour afficher une boite d'information
				JOptionPane.showMessageDialog(null, "Mise à jour teminee, l'index est à jour", "Information",
				JOptionPane.INFORMATION_MESSAGE);					
			}
			
		}else { // sinon, on charge l'index 
			if(!indexer.chargerIndex()) {
				JOptionPane.showMessageDialog(null, "Erreur lors de la recuperation de l'index", "Erreur",
						JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "l'index est a jour", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
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
		menu_options = new JMenu("Options");
		item_quitter = new JMenuItem("Quitter");
		item_changeDossier = new JMenuItem("Dossier Sources");
		menu_options.add(item_changeDossier);
		menu_options.add(item_quitter);
		menu_bar.add(menu_options);
		
		// Ajout d'un champ de recherche
		champ_recherche = new JTextField();
		champ_recherche.setPreferredSize(new Dimension(900,35));
		champ_recherche.setFont(police_2);
		
		// Ajout d'un bouton recherche
		btn_recherche = new JButton("Rechercher");
		btn_recherche.setPreferredSize(new Dimension(160,35));
		
		// Text area pour afficher la recherche saisie par l'utilisateur
		area_recherche = new JTextArea(1,91);
		area_recherche.setFont(police_2);
		area_recherche.setEditable(false);
		area_recherche.setBackground(null);
	    scroll_pane_2 = new JScrollPane(area_recherche);
		scroll_pane_2.setBorder(BorderFactory.createEmptyBorder());

	  //Text area pour afficher le nombre de résultat de la recherche
	    area_nbr_resultat = new JTextArea(1,91);
		area_nbr_resultat.setFont(police_1);
		area_nbr_resultat.setEditable(false);
		area_nbr_resultat.setBackground(null);

		// Ajout du textarea au scroll pane 
		scroll_pane_3= new JScrollPane(area_nbr_resultat);
	    scroll_pane_3.setBorder(BorderFactory.createEmptyBorder());	
		
		// Fermer l'appli quand on clique sur quitter (menu)
		item_quitter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});	

		//Emplacement de l'affichage des résultats
		table = new JTable();
		String[] titres = {"<html><b>RESULTATS DE LA RECHERCHE</b></html>"};	
		DefaultTableModel tableModel = new DefaultTableModel(resultat_table, titres);		
		table.setModel(tableModel);		
		table.setRowHeight(25);        
		table.setPreferredScrollableViewportSize(new Dimension(1000,490));
		table.setFillsViewportHeight(true);
		table.setBackground(Color.WHITE);	
		
		
		// Ajout de la table au scroll pane
		scroll_pane_1 = new JScrollPane(table);
		
		// Ajout des différents composants au conteneur principal
		conteneur.add(champ_recherche);
		conteneur.add(btn_recherche);
		conteneur.add(scroll_pane_2);
		conteneur.add(scroll_pane_3);
		conteneur.add(scroll_pane_1);	
		
		// Bouton rechercher
		btn_recherche.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evenement){				
				if(!champ_recherche.getText().equals("")) {
					saisie = new Requete(indexer.getIndex(),new File(sources),champ_recherche.getText());
					ArrayList<File> resultats = saisie.documentsCorrespondants(indexer.getIndex());			
					
					resultat_table = new Object[resultats.size()][1];
					for(int i=0; i<resultats.size(); ++i) {
							resultat_table[i][0] = resultats.get(i);							
					}
					
					// Création de la table qui recevera les résultats de la recherche 
					DefaultTableModel tableModelresult = new DefaultTableModel(resultat_table, titres);
					table.setModel(tableModelresult);
					TableColumn col = table.getColumnModel().getColumn(0);
					col.setPreferredWidth(850);
									
					// Affichage requete utilisateur
					area_recherche.setText(" \" "+champ_recherche.getText()+" \" ");
					// Affichage nombre de resultats trouves
					area_nbr_resultat.setText(resultats.size()+" resultats trouves ");
					// Reinitialiser le champ de recherche
					champ_recherche.setText("");					
				}
			}
		});

		//Change le dossier dans lequel on récupére les sources à tester
		item_changeDossier.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evenement){

				//differents choix possibles
				Object[] choix={"Assets/AP/","Tests/AP/"};
				int src = JOptionPane.showOptionDialog(
					null, "choisis le dossiers dans lequel trouver les sources", "selection de source",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,choix,choix[0]
				);
				
				//traitement des différents choix
				switch(src){
					case 0: sources="Assets/AP/"; break;
					case 1: sources="Tests/AP/";break;
					default: break;
				}
				indexer.setSource(sources);
					
			}
		});
	}
}