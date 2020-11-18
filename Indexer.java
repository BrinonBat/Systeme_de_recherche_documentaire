import java.io.File;
import java.util.ArrayList;

public class Indexer {
    private File sources;
    private ArrayList<File> li_fics;

    //constructeur
    public Indexer(String chemin){
        sources = new File(chemin);
        li_fics=new ArrayList<File>(); 

    }

    public void supprime_index(){
        li_fics.clear(); // on vide la liste des fichiers traités

        // on supprime l'index
        File index= new File("index.csv");
        if(index.exists()){
            System.out.println("index correctement supprimé !");
            index.delete();
        } else {
            System.out.println("aucun index à supprimer.");
        }
    }

    public void indexation(){

        //verifie s'il y a un nouvea fichier à indexer
        ArrayList<File> aTraiter= new ArrayList<File>(); // liste des fichiers à traiter
        File[] li_sources=sources.listFiles(new FiltreSrc()); // prends tous les fichiers sources du dossier
        // retire des fichiers à traiter ceux qui l'ont déjà été
        for (File tmp_fic : li_sources){
            if(!li_fics.contains(tmp_fic)){
                aTraiter.add(tmp_fic);
                //System.out.println(tmp_fic); //affiche les fichiers scannés
            }
        }




        // on traite les fichiers qui sont à traiter jusqu'à ce qu'il n'y en ai plus.
        for(File fichier : aTraiter){
            
        }
        File index= new File("index.csv");
    // tokenization
    // Stemmer
    // indexation & stopwords
    //private 

    }
    
    

}
