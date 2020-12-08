
import java.util.Vector;

public class SimilariteCosinus {
	
	private Vector<Reference> vecteur_req;
	
	/// Constructeur
	public SimilariteCosinus(Vector<Reference> vecteur_req){
		this.vecteur_req = vecteur_req;
	}	
	
	/**
	 * Calculer la similarité cosinus de deux vecteurs
	 * @param vecteur_doc vecteur de Reference du document à comparer à la requête
	 * @return un float correspondant à la similarité
	 * @see Reference
	 */
	public float SimilariteCos(Vector<Reference> vecteur_doc) {
		//calcul du dividende
		float prod_scalaire = 0;
		for(int i=0; i<vecteur_doc.size(); ++i) {
			prod_scalaire += vecteur_doc.get(i).getPoids() * vecteur_req.get(i).getPoids();
		}

		//calcul du diviseur
		float div=(NormeVecteur(vecteur_doc) * NormeVecteur(vecteur_req));
		if(div>0){
			return prod_scalaire / div;
		} else return 0;
	}	
	
	/**
	 * Calcule la norme d'un vecteur
	 * @param v Vecteur de Reference dont il faut calcule la norme
	 * @return un float correspondant à la norme
	 * @see Reference
	 */
	private float NormeVecteur(Vector<Reference> v) {
		float norme_vecteur = 0;
		for(int i=0; i<v.size(); ++i) {
			norme_vecteur += Math.pow(v.get(i).getPoids(), 2);
		}
		return (float)Math.sqrt(norme_vecteur);		
	}
}