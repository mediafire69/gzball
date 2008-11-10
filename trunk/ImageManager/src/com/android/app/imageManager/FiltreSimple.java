package com.android.app.imageManager;

import java.io.File;
import java.io.FileFilter;

public class FiltreSimple implements FileFilter {
	   // extension accept�e par le filtre
	  
	   private String extension;
	   //Constructeur � partir  de l'extension accept�e
	   public FiltreSimple(String extension){
	      if(extension ==null){
	         throw new NullPointerException("L'extension ne peut �tre null.");
	      }
	     
	      this.extension = extension;
	   }
	   //Impl�mentation de FileFilter
	   public boolean accept(File file){
	      if(file.isDirectory()) { 
	         return true; 
	      } 
	      String nomFichier = file.getName().toLowerCase(); 

	      return nomFichier.endsWith(extension);
	   }
	}