import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Requete {
    private Vector<Reference> vec_ref = new Vector<Reference>();

    Requete(ArrayList<vecFichier> index,File src, String phrase){
        Tokenizer tokenizer= new Tokenizer(src);
        ArrayList<String> li_mots=tokenizer.actualiserIndexMots(); //li_mots pas OK

        //liste de mots contenus dans la requête qui sont à traiter
        ArrayList<String>mots_phrase=tokenizer.traiter(phrase);
        System.out.println("phrase traitée :"+mots_phrase);

        System.out.println("traitement de la requete ...");



        //System.out.println("actualisation d'index terminée; li_mots:"+li_mots);
        //traitement de la requête en calculant les TF
        for(int pos=0,quantite=0; pos<li_mots.size();pos++){
            Short nb_rep_mot=(short)Collections.frequency(mots_phrase, li_mots.get(pos));
            Reference ref=new Reference();
            ref.setQte(nb_rep_mot); // ajout de la frequence du mot testé
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
                ref.setPoids((li_mots.size()/quantite));
            } else {
                ref.setPoids(1); // dans ce cas là, on calcule le poids avec 1 d'idf pour que son log soit de 0.
            }
            System.out.println("mot numero "+vec_ref.size()+"/"+li_mots.size()+" traité :"+li_mots.get(pos)+"dans "+mots_phrase);
        }
        System.out.println("traitement terminé !");
    } 


    public ArrayList<File> documentsCorrespondants(ArrayList<vecFichier> index){
        ArrayList<Float> coefs = new ArrayList<Float>(index.size());
        SimilariteCosinus calculateur=new SimilariteCosinus(vec_ref);

        //on calcule la similarité de chaque document à la requête
        System.out.println("calcul des coefs ...");
        for(vecFichier actuel : index){
            coefs.add(calculateur.SimilariteCos(actuel.getRefs()));
        }

        System.out.println("tri du résultat ...");
        return place((ArrayList<vecFichier>)index.clone(),coefs);
        
    }

    private ArrayList<File> place(ArrayList<vecFichier> index, ArrayList<Float> coefs){
        ArrayList<File> resultat=new ArrayList<File>(index.size());
        float val_max=0;

       // System.out.println(index+"/"+coefs);

        // on continue tant que tous les fichiers n'ont pas été traités
        while(!index.isEmpty()){
            //on récupére la meilleure correspondance
            for(int i=0;i<coefs.size();i++){
                val_max=0;
                if(coefs.get(i)>=val_max) val_max=coefs.get(i);
            }
            // on ajoute le fichier correspondant aux résultats, puis on le supprime des fichiers à traiter
            resultat.add(new File(index.get(coefs.indexOf(val_max)).getNomFichier()));
            index.remove(coefs.indexOf(val_max));
            coefs.remove(coefs.indexOf(val_max));
        }
        System.out.println(resultat);
        return resultat;
    }
}
