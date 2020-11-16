import java.util.Vector;
import java.lang.Math;

public class Reference {
    private float poids;
    private float tf;
    private Vector<Integer> liste_positions;

    //constructeur
    Reference(){
        liste_positions= new Vector<Integer>();
    }

    //accesseurs
    public float getPoids(){
        return this.poids;
    }
    public void setPoids(double idf){
        this.poids=(float)((1+Math.log10(tf))*(Math.log10(idf)));
    }

    public void majTf(int nb_mots){
        this.tf=(liste_positions.size()/nb_mots);
    }


    /// Ajoute une occurence du mot à la liste d'occurences
    public void ajoutOccurence(int position, int nb_mots){
        //ajout de l'occurence dans la liste des positions
        liste_positions.add(position);

        //!\\ on ne fait pas la maj du TF à chaque ajout, car on a besoin du nombre total de mots du fichier pour l'avoir. 
        //!\\ On le calcule donc une fois l'analyse du fichier fini
    }
}


