
import java.util.Vector;

public class SimilariteCosinus {
	
	private Vector<Reference> vecteur_req;
	
	// Constructeur
	public SimilariteCosinus(Vector<Reference> vecteur_req){
		this.vecteur_req = vecteur_req;
		System.out.println("requete : "+vecteur_req);
	}	
	
	// Calculer la similarit� cosinus de deux vecteurs
	public float SimilariteCos(Vector<Reference> vecteur_doc) {
		//System.out.println("vecteur doc : "+vecteur_doc);
		float prod_scalaire = 0;
		for(int i=0; i<vecteur_doc.size(); ++i) {
			prod_scalaire += vecteur_doc.get(i).getPoids() * vecteur_req.get(i).getPoids();
		}
		
		// A MODIFIER AVEC LE COURS CAR TEMPORAIRE
		float div=(NormeVecteur(vecteur_doc) * NormeVecteur(vecteur_req));
		if(div>0){
			System.out.println("resut cos : "+prod_scalaire / div);
			return prod_scalaire / div;
		} else return 0;
	}	
	
	// Calculer la norme d'un vecteur
	private float NormeVecteur(Vector<Reference> v) {
		float norme_vecteur = 0;
		for(int i=0; i<v.size(); ++i) {
			norme_vecteur += Math.pow(v.get(i).getPoids(), 2);
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