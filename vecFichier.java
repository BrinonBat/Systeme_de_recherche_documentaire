import java.io.File;
import java.util.Vector;

public class vecFichier {
    private File fichier;
    Vector<Reference> vec_refs;

    //constructeurs
    public vecFichier(File fic){
        fichier=fic;
        vec_refs=new Vector<Reference>();
    }
    public vecFichier(File fic,Reference ref){
        new vecFichier(fic);
        
    }
    public float getPoids(int position){
        return vec_refs.get(position).getPoids();
    }

    public File getFichier(){
        return fichier;
    }

    public void ajoutRef(Reference ref){
        vec_refs.add(ref);
    }
}