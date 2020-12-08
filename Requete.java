import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Requete {
    private Vector<Reference> vec_ref = new Vector<Reference>();

    /**
     *  le constructeur fait directement le traitement de la requête
     * @param index index contenant les vecteurs de Reference des documents
     * @param src Chemin vers la source
     * @param phrase Requête
     * @see vecFichier
     */
    Requete(ArrayList<vecFichier> index,File src, String phrase){
        Tokenizer tokenizer= new Tokenizer(src);
        ArrayList<String> li_mots=tokenizer.actualiserIndexMots(); //li_mots pas OK

        //liste de mots contenus dans la requête qui sont à traiter
        ArrayList<String>mots_phrase=tokenizer.traiter(phrase);

        System.out.println("Traitement de la requete ...");

        //traitement de la requête en calculant les TF
        for(int pos=0; pos<li_mots.size();pos++){
            Short quantite=0;
            Short nb_rep_mot=(short)Collections.frequency(mots_phrase, li_mots.get(pos));

            //traitement de chaque mot
            Reference ref=new Reference();
            ref.setQte(nb_rep_mot); // ajout de la frequence du mot testé.
            ref.majTf(li_mots.size()); // calcul du Tf 
            vec_ref.add(ref);

            // calcul de l'IDF du mot
            for(vecFichier vecteur : index){
                if(vecteur.getPoids(pos)>0){
                    quantite++;
                }
            }

            // maj du poids avec l'IDF
            if(quantite>0){
                ref.setPoids((index.size()/quantite)+1);
            } else {
                ref.setPoids(1); // dans ce cas là, on calcule le poids avec 1 d'idf pour que son log soit de 0.
            }
        }
        System.out.println("Traitement terminé !");
    } 

    /**
     * compare les documents à la requête
     * @param index index 
     * @return liste des fichiers correspondant à la requête dans l'ordre de pertinence
     */
    public ArrayList<File> documentsCorrespondants(ArrayList<vecFichier> index){
        ArrayList<Float> coefs = new ArrayList<Float>(index.size());
        SimilariteCosinus calculateur=new SimilariteCosinus(vec_ref);

        //on calcule la similarité de chaque document à la requête
        System.out.println("Calcul des coefs ...");
        for(vecFichier actuel : index){
            coefs.add(calculateur.SimilariteCos(actuel.getRefs()));
        }

        System.out.println("Tri du résultat ...");
        return place((ArrayList<vecFichier>)index.clone(),coefs);
        
    }

    /**
     * trie les fichiers en fonction de leur pertinence
     * @param index index (permettant d'accéder à la liste de documents et leurs poids)
     * @param coefs indicateurs de pertinence calculés au préalable
     * @return la liste de documents ayant un poids supérieur à 0 triés par ordre décroissant
     */
    private ArrayList<File> place(ArrayList<vecFichier> index, ArrayList<Float> coefs){
        ArrayList<File> resultat=new ArrayList<File>(index.size());
        float val_max=0;

        // on continue tant que tous les fichiers n'ont pas été traités
        while(!index.isEmpty()){
            //on récupére la meilleure correspondance
            val_max=0;
            for(int i=0;i<coefs.size();i++){           
                if(coefs.get(i)>=val_max) val_max=coefs.get(i);
            }
            if(val_max==0.0) break; // on arrête si les fichiers suivants ne correspondent pas à la recherche
            //System.out.println("max is "+val_max+" at index "+coefs.indexOf(val_max));
            // on ajoute le fichier correspondant aux résultats, puis on le supprime des fichiers à traiter
            resultat.add(new File(index.get(coefs.indexOf(val_max)).getNomFichier()));
            index.remove(coefs.indexOf(val_max));
            coefs.remove(coefs.indexOf(val_max));
        }
        System.out.println("Tri terminé! voici le résultat!");
        return resultat;
    }
}
