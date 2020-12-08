import java.lang.Math;

public class Reference {
    private float poids;
    private float tf;
    private Short quantite;
    //constructeurs
    Reference(float poids_depart){
        quantite=0;
        tf=0;
        poids=poids_depart; // valeur par défaut en attendant de la calculer
    }
    Reference(){
        new Reference(0);
    }
    //accesseurs
    public float getPoids(){
        return this.poids;
    }

    public Short getQuantite(){ return quantite;}

    public void setPoids(double idf){
        if(tf==0) idf=0;
        else{
            this.poids=(float)(tf*(Math.log10(idf)));
            this.poids=(float)/*((Math.round(poids * 1000000000.0)) / 1000000.0)*/poids*100000;
        } ;
    }

    public void setQte(Short qte){
        //!\\ on ne fait pas la maj du TF à chaque ajout, car on a besoin du nombre total de mots du fichier pour l'avoir. 
        //!\\ On le calcule donc une fois l'analyse du fichier fini
        quantite=qte;
    }

    public void majTf(int nb_mots){
        this.tf=((float)quantite/nb_mots);
    }

    public String toString(){
        return Float.toString(poids);
    }
}


