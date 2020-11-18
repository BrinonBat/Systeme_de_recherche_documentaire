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
        File index= new File("index.csv");
        //verification qu'au moins un fichier à été ajouté
        ArrayList<File> aTraiter=sources.list();

        //prends un fichier
    // tokenization
    // Stemmer
    // indexation & stopwords
    //private 

    }
    
    

}
