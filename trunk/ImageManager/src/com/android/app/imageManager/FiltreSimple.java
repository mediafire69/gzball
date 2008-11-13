package com.android.app.imageManager;

import java.io.File;
import java.io.FileFilter;

/**
 * Class implementing FileFilter interface that permit to create a simple file
 * filter from a string representing an extension (*.jpg, *.mp3, ...)  
 */
public class FiltreSimple implements FileFilter {
	   
	 // The extension to filter
	  private String extension;
	  
	   /**
	    * Constructor
	    */
	   public FiltreSimple(String extension){
	      if(extension == null){
	         throw new NullPointerException("L'extension ne peut être null.");
	      }
	     
	      this.extension = extension;
	   }
	   /**
	    * FileFilter implementation
	    */
	   public boolean accept(File file){
	      if(file.isDirectory()) { 
	         return true; 
	      } 
	      String nomFichier = file.getName().toLowerCase(); 

	      return nomFichier.endsWith(extension);
	   }
	}