
import java.util.Vector;

public class SimilariteCosinus {
	
	private Vector<Double> vecteur_req;
	private Vector<Double> vecteur_doc;
	
	// Constructeur
	public SimilariteCosinus(Vector<Double> vecteur_req, Vector<Double> vecteur_doc) {
		this.vecteur_req = vecteur_req;
		this.vecteur_doc = vecteur_doc;
		SimilariteCos(vecteur_req, vecteur_doc);
	}	
	
	// Calculer la similarité cosinus entre deux vecteurs
	public double SimilariteCos(Vector<Double> v1, Vector<Double> v2) {
		double prod_scalaire = 0;
		for(int i=0; i<v2.size(); ++i) {
			prod_scalaire += v2.get(i) * v1.get(i);
		}
		return prod_scalaire / (NormeVecteur(v2) * NormeVecteur(v1));		
	}	
	
	// Calculer la norme d'une vecteur
	public double NormeVecteur(Vector<Double> v) {
		double norme_vecteur = 0;
		for(int i=0; i<v.size(); ++i) {
			norme_vecteur += Math.pow(v.get(i), 2);
		}
		return Math.sqrt(norme_vecteur);		
	}
}

/*
 *	Dans la classe appelante :
 *	Représenter la requête comme un vecteur
 *	Représenter chaque document comme un vecteur
 *	À lancer pour le "vecteur de requête" et chaque "vecteur de document"
 *	Récupérer les similarités cosinus (scores) dans un tableau
 *	Tableau par ordre décroissant => Ordre d'affichage des résultats
*/