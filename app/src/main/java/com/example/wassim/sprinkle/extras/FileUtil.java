package com.example.wassim.sprinkle.extras;

import java.io.File;

/**
 * Classe utilitaire pour des opérations sur les fichiers
 */
public class FileUtil {

    /**
     * Fonction permettant de supprimer tout le contenu d'un répoertoire simple
     * Ne supprime pas les sous repertoires
     * @param path
     */
    public static void deleteDirectory(String path)
    {
        File directory = new File(path);
        String[] entries = directory.list();
        for (String entry : entries) {
            File currentFile = new File(directory.getPath(), entry);
            currentFile.delete();
        }
        directory.delete();
    }

    /**
     * Fonction permettant de vérifier l'existance d'un fichier/dossier
     * @param path
     * @return boolean
     */
    public static boolean exists(String path)
    {
        return new File(path).exists();
    }
}
