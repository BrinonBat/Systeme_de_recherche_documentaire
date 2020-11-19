import java.util.Vector;
import java.lang.Math;

public class Reference {
    private float poids;
    private float tf;
    private Short quantite;
    //constructeurs
    Reference(){
        quantite=0;
        poids=0;
    }
    Reference(Float poids_depart){
        quantite=0;
        poids=poids_depart.floatValue(); // valeur par défaut en attendant de la calculer
    }

    //accesseurs
    public float getPoids(){
        return this.poids;
    }
    public void setPoids(double idf){
        this.poids=(float)((1+Math.log10(tf))*(Math.log10(idf)));
    }

    public void upQte(){
        //!\\ on ne fait pas la maj du TF à chaque ajout, car on a besoin du nombre total de mots du fichier pour l'avoir. 
        //!\\ On le calcule donc une fois l'analyse du fichier fini
        quantite++;
    }

    public void majTf(int nb_mots){
        this.tf=(quantite/nb_mots);
    }



    
}


