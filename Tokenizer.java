import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Tokenizer {
    private File sources;

    /// constructeur
    Tokenizer(File src){sources=src;}

    /// retourne la liste des Fichiers à traiter selon le doc list
    public ArrayList<File> listerFichiersDuDoclist(){
        ArrayList<File> result= new ArrayList<File>();
        BufferedReader br = null;
        String ligne="";

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader(new File(sources.getParent()+"/doclist.txt")));
            while((ligne = br.readLine()) != null){
                result.add(new File(ligne));
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

    /**
     * retourne la liste des Fichiers à traiter selon les sources, en les mettant à jour
     * @param maj mise à jour si true, analyse uniquement si false
     * @return la liste des fichiers à traiter
     */
    public ArrayList<File> listerFichiers(boolean maj){
        ArrayList<File> li_fics= new ArrayList<File>();
        BufferedReader br = null;
        String ligne="";

        try {
            if(maj){// remise à 0 du contenu du fichier
                FileWriter writer=new FileWriter(sources.getParent()+"/doclist.txt");
                writer.close();
            } 
            //parcours du fichier et lecture
            for(File fichier : (sources.listFiles(new FiltreSrc()))){
                br = new BufferedReader(new FileReader(fichier));

                while((ligne = br.readLine()) != null){
                    if(ligne.length()>8){ // évite les erreurs de substring sur les ligne trop petites
                        if(ligne.substring(0,7).equals("<DOCNO>")){
                            li_fics.add(new File(sources+"/"+ligne.substring(8,ligne.length()-8)));
                            if(maj){
                                FileWriter writer=new FileWriter(sources.getParent()+"/doclist.txt",true);
                                writer.append(sources+"/"+ligne.substring(8,ligne.length()-8)+"\n");
                                writer.flush();    
                                writer.close();
                            } 
                        }
                        // autres données non-traitées.
                        else {}
                    }
                }
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
        return li_fics;
    }

    /**
     * transforme un fichier en liste de tokens pour chaque document
     * @param fichier fichier à analyser
     * @return le vecteur (chaque element est un document) de liste de mots.
     */
    public Vector<ArrayList<String>> RecupereMots(File fichier){
        ArrayList<String> li_mots= new ArrayList<String>();
        Vector<ArrayList<String>> matrice_mots=new Vector<ArrayList<String>>();
        BufferedReader br = null;
        String ligne="";

        try {
            br = new BufferedReader(new FileReader(fichier));

            //parcours du fichier et lecture
            while((ligne = br.readLine()) != null){
                if(ligne.isEmpty() || ligne.isBlank())ligne=br.readLine(); // cas de ligne vide
                if(ligne.charAt(0)!='<'){ // cas de ligne de texte
                    li_mots.addAll(this.traiter(ligne));
                }
                else if(ligne.substring(1,5).equals("HEAD")){ // cas de ligne header
                    li_mots.addAll(this.traiter(ligne.substring(5,ligne.length()-7)));

                }
                else if(ligne.substring(1,5).equals("/DOC")){ // cas de fin
                    matrice_mots.add((ArrayList<String>)li_mots.clone());
                    li_mots.clear();
                }
                // autres données non-traitées.
                else {}
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
        return matrice_mots;
    }

    /**
     * récupére la liste de stopwords
     * @param liste fichier contenant la liste de stopwords
     * @return liste de tokens stopwords
     */
    private ArrayList<String> recupereStopWords(File liste){
        ArrayList<String> result= new ArrayList<String>();
        BufferedReader br = null;
        String ligne;

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader(liste));
            while((ligne = br.readLine()) != null){
                result.add(ligne);
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

    /**
     * transforme une phrase en une serie de tokens
     * @param phrase la phrase à découper et passer au stemmer & stopwords
     * @return les mots tokens composant la phrase
     */
    public ArrayList<String> traiter(String phrase){
        phrase=phrase+" "; //ajout d'un espace pour conclure le dernier mot et ainsi le traiter
        ArrayList<String> li_mots=new ArrayList<String>();
        ArrayList<String> stop_words=this.recupereStopWords(new File(sources.getParent()+"/stopwords.txt"));
        Stemmer stemmer= new Stemmer();
        String mot;
        String TMPASUPPRIMER="";
        boolean est_mot=false;

        // tokenisation
        for(int i=0;i<phrase.length();i++){
            
            if(Character.isSpaceChar(phrase.charAt(i))){ // séparateur de mots
                if(est_mot){ // si un mot était en cours de formation
                    //passage au stemmer
                    stemmer.stem();
                    mot=stemmer.toString();
                    TMPASUPPRIMER="";
                    est_mot=false;

                    //stoplist et ajout
                    if(!stop_words.contains(mot)){
                        li_mots.add(mot);
                    }
                }
            }
            else{
                est_mot=true;
                //on compose les mots avec ce filtre
                if(Character.isLetterOrDigit(phrase.charAt(i)) || phrase.charAt(i)=='-'){
                    stemmer.add(Character.toLowerCase(phrase.charAt(i)));
                    TMPASUPPRIMER+=phrase.charAt(i);
                }
            }
        }
        return li_mots;
    }

    /// retourne la liste de mots contenue dans stem.txt
    public ArrayList<String> actualiserIndexMots(){
        System.out.println("Récupération de la liste de mots dans l'index ...");
        ArrayList<String> result= new ArrayList<String>();
        BufferedReader br = null;
        String ligne="";
        String mot;

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader(sources.getParent()+"/stem.txt"));
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
