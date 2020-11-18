import java.io.File;
import java.util.ArrayList;

public class Indexer {
    public Reference[][] index;
    private File sources;
    private ArrayList<File> li_fics;
    File fichier_index= new File("index.csv");
    //constructeur
    public Indexer(String chemin){
        sources = new File(chemin);
        li_fics=new ArrayList<File>(); 
        if(!remplirIndex()) System.out.println("erreur lors de la récupération de l'index");
    }

    public void supprime_index(){
        li_fics.clear(); // on vide la liste des fichiers traités

        // on supprime l'index
        if(fichier_index.exists()){
            System.out.println("index correctement supprimé !");
            fichier_index.delete();
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

        //s'il y a des fichiers à traiter, on doit re-construire la matrice d'indexation

        // on traite les fichiers qui sont à traiter jusqu'à ce qu'il n'y en ai plus.
        for(File fichier : aTraiter){
            // ajout fichier en colonne
            // parcours du fichier 
                //si mot inconnu, ajout en bas de la liste.
                //si mot connu, augmente le nombre d'occurence 
        }
        File index= new File("index.csv");
    // tokenization
    // Stemmer
    // indexation & stopwords
    //private 

    }
    // génére l'index à partir du fichier csv. Retourne false s'il y a eu une erreur
    private boolean remplirIndex(){
        boolean isOk=false;
        
        isOk=true;
        return isOk;
    }    

    private boolean sauvIndex(){
        boolean isOk=false;

        isOk=true;
        return isOk;
    }

}
