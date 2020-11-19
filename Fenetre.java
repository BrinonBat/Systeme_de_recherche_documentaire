

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Fenetre extends JFrame {
	
	private JPanel conteneur;
	private FlowLayout flow_layout;
	private JTextField champ_recherche;
	private JButton btn_recherche;
	private Font police;
	private JLabel label_titre, label_recherche, label_nbr_resultat;
	private JTable table;
	private JScrollPane scroll_pane;
	
	// Constructeur
	public Fenetre(){
		this.setTitle("Projet - recherche documentaire");
		this.setSize(new Dimension(740,680));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initComposant();
		this.setContentPane(conteneur);
		this.setVisible(true);
	}	
	
	// Initialisation des composants
	public void initComposant() {
		conteneur = new JPanel();
		flow_layout = new FlowLayout();
		conteneur.setLayout(flow_layout);
		flow_layout.setHgap(10);
		flow_layout.setVgap(15);
		
		police = new Font("Arial", Font.ITALIC, 12);	
		champ_recherche = new JTextField();
		champ_recherche.setPreferredSize(new Dimension(500,28));
		champ_recherche.setFont(police);		
		btn_recherche = new JButton("Rechercher");
		label_titre = new JLabel("Résultats de la recherche");
		label_nbr_resultat = new JLabel();
		label_recherche = new JLabel();
		label_recherche.setFont(police);
		
		// Préparation de la JTable qui va accueillir les résultats de la recherche
		String[] titres = {"N°", "ID", "C1", "C2", "C3"};
		Object[][] resultats = {
				//{"01", "AP001", "10", "c01", "c01"},
				//{"02", "AP002", "20", "c02", "c02"}
		};
		
		table = new JTable(resultats, titres);
		table.setPreferredScrollableViewportSize(new Dimension(600,400));
		table.setFillsViewportHeight(true);
		
		scroll_pane = new JScrollPane(table);
		
		conteneur.add(champ_recherche);
		conteneur.add(btn_recherche);
		conteneur.add(label_titre);
		conteneur.add(scroll_pane);
		conteneur.add(table.getTableHeader());
		conteneur.add(label_nbr_resultat);
		conteneur.add(label_recherche);
		
		btn_recherche.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evenement){				
				if(!champ_recherche.getText().equals("")) {
					// Appel
					
					
					
					// Nombre de résultats trouvés
					String nbr_resultat = Integer.toString(table.getRowCount())+" résultats trouvés ";
					label_nbr_resultat.setText(nbr_resultat+" ");
					// Affichage de la recherche utilisateur
					label_recherche.setText(champ_recherche.getText());
				}
			}
		});
		
	}
	
}
