import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class Indexer {
    // [0][1-x] contient les noms de fichiers 
    // [1-x][0] contient les mots
    public Reference[][] index; 
    private File sources; // chemin vers les sources
    private ArrayList<File> li_fics; // liste de fichiers indéxés
    private ArrayList<String> li_mots; // liste de mots indexés
    File fichier_index= new File("index.csv"); // index

    //constructeur
    public Indexer(String chemin){
        sources = new File(chemin);
        li_fics=new ArrayList<File>(); 
        li_mots=new ArrayList<String>();
        //if(!chargerIndex()) System.out.println("erreur lors de la récupération de l'index");
    }

    public void supprime_index(){
        li_fics.clear(); // on vide la liste des fichiers traités
        li_mots.clear(); // on vide la liste des mots testés

        // on supprime l'index
        if(fichier_index.exists()){
            fichier_index.delete();
            System.out.println("index correctement supprimé !");
        } else {
            System.out.println("aucun index à supprimer.");
        }
    }

    // retourne le nombre de fichier manquant à l'index
    public int estAJour(){
        //verifie s'il y a un nouvea fichier à indexer
        ArrayList<File> a_traiter= new ArrayList<File>(); // liste des fichiers à traiter
        File[] li_sources=sources.listFiles(new FiltreSrc()); // prends tous les fichiers sources du dossier

        // chargement de la liste de fichiers
        BufferedReader br = null;
        String ligne="";
        String separateur= ";";

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader(fichier_index.getAbsolutePath()));
            ligne = br.readLine(); // récupération de la ligne listant les fichiers
            String[]cases= ligne.split(separateur);//séparation en plusieurs "cases"
            for(int num_col=1;num_col<cases.length;num_col++){
                File fic_charge= new File(sources.getAbsolutePath()+cases[num_col]);
                li_fics.add(fic_charge);
            }    
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try { // fermeture du lecteur de csv
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // retire des fichiers à traiter ceux qui l'ont déjà été
        for (File tmp_fic : li_sources){
            if(!li_fics.contains(tmp_fic)){
                a_traiter.add(tmp_fic);
                //System.out.println(tmp_fic); //affiche les fichiers scannés
            }
        }
        return(a_traiter.size());
    }

    public void indexation(){
        // ajout fichier en colonne
        // parcours du fichier          
            // tokenization
            // Stemmer
            // indexation & stopwords
                //si mot inconnu, ajout en bas de la liste.
                //si mot connu, augmente le nombre d'occurence
    }
    
    // génére l'index à partir du fichier csv. Retourne false s'il y a eu une erreur
    public boolean chargerIndex(){
        boolean isOk=false;

        BufferedReader br = null;
        String ligne="";
        String separateur= ";";

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader(fichier_index.getAbsolutePath()));
            for(int num_ligne=0;(ligne = br.readLine()) != null;num_ligne++) {

               //séparation en plusieurs "cases"
                String[]cases= ligne.split(separateur);
                
                // on récupère la liste des fichiers en première ligne
                if(num_ligne==0){
                    for(int num_col=1;num_col<cases.length;num_col++){
                        File fic_charge= new File(sources.getAbsolutePath()+cases[num_col]);
                        li_fics.add(fic_charge);
                    }
                } else { // on rempli la matrice
                    for(int num_col=1;num_col<cases.length;num_col++){
                        Reference ref= new Reference(Float.valueOf(cases[num_col]));
                        index[num_ligne][num_col]=ref;
                    }
                    li_mots.add(cases[0]);//on ajoute le mot à la liste des mots traités
                }
            } // fin du parcours du fichier
            isOk=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOk;
    }    

    private boolean sauverIndex(){
        boolean isOk=false;

        isOk=true;
        return isOk;
    }

}
