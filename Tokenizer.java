import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Tokenizer {
    private File sources;

    //constructeur
    Tokenizer(File src){
        sources=src;
    }


    // retourne la liste des Fichiers à traiter
    public ArrayList<File> listerFichiers(){
        ArrayList<File> result= new ArrayList<File>();
        BufferedReader br = null;
        String ligne="";

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader("assets/doclist.txt"));
            while((ligne = br.readLine()) != null){
                result.add(new File(sources+"/"+ligne));
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

    public Vector<ArrayList<String>> RecupereMots(File fichier){
        ArrayList<String> li_mots= new ArrayList<String>();
        Vector<ArrayList<String>> matrice_mots=new Vector<ArrayList<String>>();
        BufferedReader br = null;
        String ligne="";

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader(sources.getParent()+"/doclist.txt"));
            //br = new BufferedReader(new FileReader(fichier);

            while((ligne = br.readLine()) != null){
                if(ligne.charAt(0)!='<'){
                    li_mots.addAll(this.traiter(ligne));
                }
                else if(ligne.substring(1,4)=="HEAD"){
                    li_mots.addAll(this.traiter(ligne.substring(5,ligne.length()-6)));
                }
                else if(ligne.substring(1,4)=="/DOC"){
                    matrice_mots.add(li_mots);
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

    private ArrayList<String> recupereStopWords(File liste){
        ArrayList<String> result= new ArrayList<String>();
        BufferedReader br = null;
        String ligne="";

        try {
            //parcours du fichier et lecture
            br = new BufferedReader(new FileReader("assets/doclist.txt"));
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

    private ArrayList<String> traiter(String phrase){
        ArrayList<String> li_mots=new ArrayList<String>();
        ArrayList<String> stop_words=this.recupereStopWords(new File(sources.getParent()+"/stopwords.txt"));
        Stemmer stemmer= new Stemmer();
        String mot;

        // tokenisation
        for(int i=0;i<phrase.length();i++){
            if(Character.isSpaceChar(phrase.charAt(i))){ // séparateur de mots
                //passage au stemmer
                stemmer.stem();
                mot=stemmer.toString();

                //stoplist et ajout
                if(!stop_words.contains(mot)){
                    li_mots.add(mot);
                }
            }
            else{
                //on compose les mots avec ce filtre
                if(Character.isLetterOrDigit(phrase.charAt(i)) || phrase.charAt(i)=='-'){
                    stemmer.add(Character.toLowerCase(phrase.charAt(i)));
                }
            }
        }

        return li_mots;
    }

}
