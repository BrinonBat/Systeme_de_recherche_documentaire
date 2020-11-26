import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.Array;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Indexer {
    // [0][1-x] contient les noms de fichiers 
    // [1-x][0] contient les mots
    public ArrayList<vecFichier> index; 
    private File sources; // chemin vers les sources
    private ArrayList<String> li_mots; // liste de mots indexés
    File fichier_index; // index

    //constructeur
    public Indexer(String chemin){
        sources = new File(chemin);
        fichier_index=new File("index.csv");
        li_mots=new ArrayList<String>();
        index=new ArrayList<vecFichier>();
        if(!chargerIndex()) System.out.println("erreur lors de la récupération de l'index");
    }

    public void supprime_index(){
        li_mots.clear(); // on vide la liste des mots testés
        index.clear(); // on vide l'index

        // on supprime l'index
        if(fichier_index.exists()){
            //fichier_index.delete();
            System.out.println("index correctement supprimé !");
        } else {
            System.out.println("aucun index à supprimer.");
        }
    }

    // retourne le nombre de fichier manquant à l'index
    // !!! A METTRE A JOUR (appel à Tokenizer.listerFichier)
    public int nEstPasAJour(){

        // uniquement s'il y a un index
        if(fichier_index.exists()){

            //verifie s'il y a un nouvea fichier à indexer
            ArrayList<File> a_traiter= new ArrayList<File>(); // liste des fichiers à indexer
            File[] li_source;
            ArrayList<File> li_fics= new ArrayList<File>(); // liste de fichiers indéxés

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
                    File fic_charge= new File(sources+"/"+cases[num_col]);
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
            /*
            // retire des fichiers à traiter ceux qui l'ont déjà été
            for (File tmp_fic : li_sources){
                if(!li_fics.contains(tmp_fic)){
                    a_traiter.add(tmp_fic);
                    //System.out.println(tmp_fic); //affiche les fichiers scannés
                }
            }*/
            return(a_traiter.size());

        } else return(sources.listFiles(new FiltreSrc()).length); // s'il n'y a pas d'index, on retourne le nombre de fichiers existants
        
    }

    public void indexation(){
        //préparation à l'indexation
        supprime_index();
        Tokenizer tokenizer= new Tokenizer(sources);
        Vector<ArrayList<String>> vec_li_mots= new Vector<ArrayList<String>>();
        short nb_rep_mot;

        //parcours la liste des fichiers
        //ArrayList<File> li_sources=tokenizer.listerFichiers(); // prends tous les fichiers sources du dossier
        
        li_mots=actualiserIndexMots();
        
        //on traite chaque fichier donné en source
        for(File tmp_fic : (sources.listFiles(new FiltreSrc()))){
            short num_fic=0;
            System.out.println("traitement du fichier "+tmp_fic); //affiche les fichiers scannés
 
            // tokenization & stemmer & stopwords
            vec_li_mots=tokenizer.RecupereMots(tmp_fic);

            //indexation de chaque document contenu dans le fichier source
            for (ArrayList<String> fic_traite : vec_li_mots){
                System.out.println(" ----> traitement dans "+tmp_fic+" du document"+fic_traite);
                //creation du vecFichier correspond au fichier actuel
                num_fic++;
                vecFichier vecFichier_tmp = new vecFichier(tmp_fic,num_fic);

                //pour chaque mot, on vérifie son nombre d'occurence dans le fichier
                for(String mot : li_mots){
                    nb_rep_mot=(short)Collections.frequency(fic_traite, mot);
                    Reference ref=new Reference();
                    ref.setQte(nb_rep_mot); // ajout de la frequence du mot testé
                    ref.majTf(fic_traite.size()); // calcul du Tf 
                    vecFichier_tmp.ajoutRef(ref);
                }
                System.out.println("OK1");
                index.add(vecFichier_tmp);
                System.out.println("OK2");
            } 
        }

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

            }
        }
        System.out.println("sauvegarde en cours");
        sauverIndex();
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
                System.out.println("chargement du mot numero "+num_ligne);

               //séparation en plusieurs "cases"
                String[]cases= ligne.split(separateur);
                
                // les fichiers sont analysés et ajoutés à la liste 
                if(num_ligne==0){ 
                    for(int num_col=1;num_col<cases.length;num_col++){
                        index.add(new vecFichier(new File(sources+"/"+cases[num_col])));// retrait des légendes du csv
                    }
                }else{// on rempli la matrice
                    for(int num_col=1;num_col<cases.length;num_col++){
                        Reference ref= new Reference(Float.valueOf(cases[num_col]));
                        index.get(num_col-1).ajoutRef(ref);// retrait des légendes du csv
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

    //exporte l'index en fichier .csv
    private boolean sauverIndex(){
        boolean isOk=false;
        boolean first = true;
        
        try{
            FileWriter writer = new FileWriter(fichier_index);
            char separators=';';
            
            for(int ligne=0;ligne<li_mots.size();ligne++){
                System.out.println("sauvegarde du mot numero "+ligne+"/"+(li_mots.size()-1));
                ArrayList<String> a_ajouter=new ArrayList<String>();
                //ajout de l'en-tête
                if(ligne==0){
                    //ajout du mot tampon
                    a_ajouter.add("Mots");
                    //ajout des noms de chaque fichier
                    for(int numFic=0;numFic<index.size();numFic++){
                        a_ajouter.add(index.get(numFic).getNomFichier());
                    }
                }else{
                    //ajout du mot traité
                    a_ajouter.add(li_mots.get(ligne-1));
                    //ajout des poids de chaque fichier pour ce mot
                    for(int numFic=0;numFic<index.size();numFic++){
                        a_ajouter.add(Float.toString(index.get(numFic).getPoids(ligne)));
                    }
                }
                //ajout des autres lignes
                StringBuilder sb = new StringBuilder();
                for (String value : a_ajouter) {
                    sb.append(value).append(separators);
                    first = false;
                }
                sb.append("\n");
                writer.append(sb.toString());
            }
                writer.flush();
                writer.close();
                isOk=true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        
        return isOk;
    }

    public void afficherIndex(){
        //Mots est hors des listes, ont doit donc l'afficher à part
        System.out.print("Mots | ");

        //affichage de la liste de fichiers
        for(int fic=0;fic<index.size();fic++){
            System.out.print(index.get(fic).getNomFichier()+" | ");
        }

        //affichage du contenu de la matrice
        for(int mot=0;mot<li_mots.size();mot++){
            System.out.print("\n"+li_mots.get(mot)+" | ");
            for(int fic=0;fic<index.size();fic++){
                System.out.print("     "+index.get(fic).getPoids(mot));
            }
        }

        //saut de ligne pour plus de confort visuel
        System.out.print("\n");
    }

    
    private ArrayList<String> actualiserIndexMots(){
        ArrayList<String> result= new ArrayList<String>();
        BufferedReader br = null;
        String ligne="";
        String mot;

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader("Assets/stem.txt"));
            ligne = br.readLine(); // retrait de la première ligne contenant ROOT | WORDS 

            // on récupére le mot ROOT pour chaque ligne
            while((ligne = br.readLine()) != null){
                mot="";
                for (int i=0; i<ligne.length();i++){
                    if(ligne.charAt(i)!=('\s')){
                        mot=mot+ligne.charAt(i);
                    } else break;
                }
                result.add(mot);
                System.out.println("ajout du mot "+mot+" dans l'index");
            }

        //gestion des erreurs et fermeture du bufferedReader
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
        return(result);
    }

}
