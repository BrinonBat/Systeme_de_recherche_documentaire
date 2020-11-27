
import java.util.Vector;

public class SimilariteCosinus {
	
	private Vector<Float> vecteur_req;
	private Vector<Float> vecteur_doc;
	
	// Constructeur
	public SimilariteCosinus(Vector<Float> vecteur_req, Vector<Float> vecteur_doc) {
		this.vecteur_req = vecteur_req;
		this.vecteur_doc = vecteur_doc;
		SimilariteCos(vecteur_req, vecteur_doc);
	}	
	
	// Calculer la similarit� cosinus de deux vecteurs
	public Float SimilariteCos(Vector<Float> v1, Vector<Float> v2) {
		float prod_scalaire = 0;
		for(int i=0; i<v2.size(); ++i) {
			prod_scalaire += v2.get(i) * v1.get(i);
		}
		return prod_scalaire / (NormeVecteur(v2) * NormeVecteur(v1));		
	}	
	
	// Calculer la norme d'un vecteur
	public float NormeVecteur(Vector<Float> v) {
		float norme_vecteur = 0;
		for(int i=0; i<v.size(); ++i) {
			norme_vecteur += Math.pow(v.get(i), 2);
		}
		return (float)Math.sqrt(norme_vecteur);		
	}
}

/*
 *	Dans la classe appelante :
 *	Repr�senter la requ�te comme un vecteur
 *	Repr�senter chaque document comme un vecteur
 *	� lancer pour le "vecteur de requ�te" et chaque "vecteur de document"
 *	R�cup�rer les similarit�s cosinus (scores) dans un tableau
 *	Tableau par ordre d�croissant => Ordre d'affichage des r�sultats
*/