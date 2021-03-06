import java.io.File;
import java.util.Vector;

public class vecFichier {
    private String nom_fichier;
    Vector<Reference> vec_refs;

    //constructeurs
    public vecFichier(File fic){
        nom_fichier=fic.getName();
        vec_refs=new Vector<Reference>();
    }
    public vecFichier(File fic,short numero){
        String num=Integer.toString(numero);
        switch(num.length()){
            case(1):{
                num="000"+num;
                break;
            }
            case(2):{
                num="00"+num;
                break;
            }
            case(3):{
                num="0"+num;
                break;
            }
            default: break;
        };
        nom_fichier=fic+"-"+num;
        vec_refs=new Vector<Reference>();
    }

    //getters
    public float getPoids(int position){
        return vec_refs.get(position).getPoids();
    }

    public Vector<Reference> getRefs(){
        return vec_refs;
    }

    //setters
    public void setIDF(int position, double idf){
        vec_refs.get(position).setPoids(idf);
    }

    public String getNomFichier(){
        return nom_fichier;
    }

    public Short getQte(int position){
        return vec_refs.get(position).getQuantite();
    }

    public void ajoutRef(Reference ref){
        vec_refs.add(ref);
    }

    /**
     * indique si le mot à la position prise en paramètre est dans le fichier
     * @param position position du mot à tester dans la liste de mots
     * @return booléen indiquant si oui ou non le mot à la position prise en paramètre est contenu dans le fichier
     */
    public boolean contientMot(int position){
        return (vec_refs.get(position).getQuantite()>0);
    }
}