
public class Main {

    public static void main(String[] args) {
        //indexation des sources AP si nécessaire
        Indexer indexer= new Indexer("Assets/AP");
        indexer.supprime_index();
            // compare les fichiers source et les colonnes de l'index, s'il en manque une lance l'indexation

        //lancement de l'interface
            // interface lance traitement de requête
            // affiche le résultat
    }

}
