import java.io.File;
import java.io.FilenameFilter;


public class FiltreSrc implements FilenameFilter {
        /**
         * Refuse les noms qui terminent par '.txt' et '.Z' ainsi que le readme
         * @param dir dossier à tester
         * @param name nom du fichier à filtrer
         * @return booléen valant true si le fichier est accepté, false sinon
         */
        @Override
        public boolean accept(File dir, String name) {
     
            if (name.endsWith(".txt") || name.endsWith(".Z") || name.endsWith("README")) {
                return false;
            }
     
            return true;
        }
}

