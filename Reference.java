import java.lang.Math;

public class Reference {
    private float poids;
    private float tf;
    private Short quantite;

    ///constructeur avec poids de départ
    Reference(float poids_depart){
        quantite=0;
        tf=0;
        poids=poids_depart; // valeur par défaut en attendant de la calculer
    }

    ///constructeur sans poids de départ
    Reference(){
        new Reference(0);
    }

    //getters
    public float getPoids(){return this.poids;}
    public Short getQuantite(){ return quantite;}

    /**
     * calcule le poids à partir de l'IDF fourni et du TF enregistré
     * @param idf idf à partir duquel on va calculer le poids
     */
    public void setPoids(double idf){
        if(tf==0) idf=0; // on évite la division par 0
        //sinon, on calcule le poids
        else{
            this.poids=(float)(tf*(Math.log10(idf)));
            this.poids=(float)poids*100000; // mise de l'augmentation à part pour une suppression facile.
        }
    }

    public void setQte(Short qte){quantite=qte;}

    /**
     * mise à jour du Term Frequency
     * @param nb_mots nombre de mots total du document
     */
    public void majTf(int nb_mots){
        this.tf=((float)quantite/nb_mots);
    }

    ///override pour afficher le poids plutot que l'ID de l'objet
    public String toString(){
        return Float.toString(poids);
    }
}


