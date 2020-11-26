import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Requete {
    private Vector<Reference> vec_ref = new Vector<Reference>();

    Requete(File src, String phrase){
        Tokenizer tokenizer= new Tokenizer(src);
        ArrayList<String> li_mots= tokenizer.traiter(phrase);

        //récupére la liste de mots à évaluer
        li_mots=tokenizer.actualiserIndexMots();

        //traitement de la requête en calculant les TF
        for(String mot : li_mots){
            Short nb_rep_mot=(short)Collections.frequency(li_mots, mot);
            Reference ref=new Reference();
            ref.setQte(nb_rep_mot); // ajout de la frequence du mot testé
            ref.majTf(li_mots.size()); // calcul du Tf 
            vec_ref.add(ref);
        }

        //calcul des IDF et mise à jour des poids
        /* A FAIRE 
        // on mets à jour la matrice en ajoutant les IDF
        for(int ligne=0;ligne<li_mots.size();ligne++){
            Short nb_doc=0;
            //parcours des documents pour compter ceux qui ont au moins une occurence du mot
            for (int colonne=0;colonne<index.size();colonne++){
                if(index.get(colonne).contientMot(ligne)) nb_doc++;
            }
            //mise à jour du IDF
            double idf=((double)index.size()/nb_doc);
            //mise à jour des poids en fonction de l'IDF
            for (int colonne=0;colonne<index.size();colonne++){
                index.get(colonne).setIDF(ligne,idf);

            } */
    } 
}
