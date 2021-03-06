import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    /// constructeur
    public Indexer(String chemin){
        sources = new File(chemin);
        fichier_index=new File("index.csv");
        li_mots=new ArrayList<String>();
        index=new ArrayList<vecFichier>();
    }

    /// supprime l'index actuel
    private void supprime_index(){
        li_mots.clear(); // on vide la liste des mots testés
        index.clear(); // on vide l'index

        // on supprime l'index
        if(fichier_index.exists()){
            fichier_index.delete();
            System.out.println("Index correctement supprimé !");
        } else {
            System.out.println("Aucun index à supprimer.");
        }
    }

    /// retourne le nombre de fichier manquant à l'index
    public int nEstPasAJour(){

        // uniquement s'il y a un index
        if(fichier_index.exists()){

            int nb_modifs;

            //verifie s'il y a un nouvea fichier à indexer
            Tokenizer tokenizer= new Tokenizer(sources);
            ArrayList<File> li_source=tokenizer.listerFichiers(false); // liste des fichiers dans les sources
            ArrayList<File> li_fics=tokenizer.listerFichiersDuDoclist(); // liste de fichiers indéxés
            ArrayList<File> li_fics_tmp=(ArrayList<File>)li_fics.clone(); // sauvegarde temporaire

            li_fics.removeAll(li_source); // convient les fichiers qui ont été supprimés
            li_source.removeAll(li_fics_tmp); // contient les fichiers qui ont été ajoutés

            nb_modifs=li_fics.size()+li_source.size(); // nombre de fichier à modifier
            System.out.println(li_fics.size()+" fichiers à supprimer et "+li_source.size()+" fichiers à ajouter");
            return(nb_modifs);

        } else return(sources.listFiles(new FiltreSrc()).length); // s'il n'y a pas d'index, on retourne le nombre de fichiers existants
        
    }

    /// regénére un nouvel index du dossier source
    public void indexation(){

        //préparation à l'indexation
        supprime_index();
        Tokenizer tokenizer= new Tokenizer(sources);
        Vector<ArrayList<String>> vec_li_mots= new Vector<ArrayList<String>>();
        short nb_rep_mot;

        //récupére la liste de mots à évaluer
        li_mots=tokenizer.actualiserIndexMots();
        
        //on traite chaque fichier donné en source
        for(File tmp_fic : (sources.listFiles(new FiltreSrc()))){
            short num_fic=0;
 
            // tokenization & stemmer & stopwords
            vec_li_mots=tokenizer.RecupereMots(tmp_fic);

            //indexation de chaque document contenu dans le fichier source
            for (ArrayList<String> fic_traite : vec_li_mots){
                num_fic++;

                System.out.println(" ----> traitement dans "+tmp_fic+" du document numero "+num_fic);

                //creation du vecFichier correspond au fichier actuel
                vecFichier vecFichier_tmp = new vecFichier(tmp_fic,num_fic);

                //pour chaque mot, on vérifie son nombre d'occurence dans le fichier
                for(String mot : li_mots){
                    nb_rep_mot=(short)Collections.frequency(fic_traite, mot);
                    Reference ref=new Reference();
                    ref.setQte(nb_rep_mot); // ajout de la frequence du mot testé
                    ref.majTf(fic_traite.size()); // calcul du Tf 
                    vecFichier_tmp.ajoutRef(ref);
                }
                index.add(vecFichier_tmp);
                System.out.println("Document ajouté");
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
            double idf=((double)index.size()/nb_doc)+1.0;
            //mise à jour des poids en fonction de l'IDF
            for (int colonne=0;colonne<index.size();colonne++){
                index.get(colonne).setIDF(ligne,idf);

            }
        }
        System.out.println("Sauvegarde en cours");
        sauverIndex();
        tokenizer.listerFichiers(true); // mise à jour de l'index de fichiers
    }

    /**
     * génére l'index à partir du fichier csv. Retourne 
     *  @return false s'il y a eu une erreur
     */
    public boolean chargerIndex(){
        if(!fichier_index.exists()) return true;
        boolean isOk=false;
        BufferedReader br = null;
        String ligne="";
        String separateur= ";";
        
        System.out.println("Chargement de l'index ...");

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader(fichier_index.getAbsolutePath()));
            for(int num_ligne=0;(ligne = br.readLine()) != null;num_ligne++) {
                

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

     /**
     * exporte l'index en fichier .csv
     *  @return false s'il y a eu une erreur
     */
    private boolean sauverIndex(){
        boolean isOk=false;
        
        try{
            FileWriter writer = new FileWriter(fichier_index);
            char separators=';';
            
            for(int ligne=0;ligne<=li_mots.size();ligne++){
                System.out.println("Sauvegarde du mot numero "+ligne+"/"+(li_mots.size()));
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
                        a_ajouter.add(Float.toString(index.get(numFic).getPoids(ligne-1)));
                    }
                }
                //ajout des autres lignes
                StringBuilder sb = new StringBuilder();
                for (String value : a_ajouter) {
                    sb.append(value).append(separators);
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
        System.out.println(" sauvegarde terminée ! vous pouvez faire une recherche");
        return isOk;
    }

    ///getter
    public ArrayList<vecFichier> getIndex(){return index;}

    /**
     * change la source de l'indexe
     * @param nouv_source chemin relatif de la nouvelle source
     */
    public void setSource(String nouv_source){
        sources=new File(nouv_source); // maj de la source
        indexation(); // maj du contenu
    }

}
