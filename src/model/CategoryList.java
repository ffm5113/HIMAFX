/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author forre
 */
public class CategoryList {
    public ArrayList<Category> categoryArrayList = new ArrayList<>(); // Array Category of categories
    private String categoryListFile = "categoryListFile.ser"; // Serializable file name
    // Constructor
    public CategoryList() throws IOException{

        try {
        readCategoryListFile();
        }
        catch(Exception ex) {
            System.out.println("Category list file not found but will be created.");
            
        }
        if(categoryArrayList.isEmpty() || categoryArrayList == null) {
            createCategoryList();
            writeCategoryListFile();
            readCategoryListFile();
        }
        this.printCategoryList();
        System.out.println("Total number of categories: " + categoryArrayList.size() + ".\n"); // For debugging
    }

    public void readCategoryListFile() throws IOException {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(categoryListFile);
            in = new ObjectInputStream(fis);
            categoryArrayList = (ArrayList)in.readObject();
            in.close();
        }catch(FileNotFoundException ex) {
            System.out.println("Create new category list file.");
        }catch(IOException ex) {
            ex.printStackTrace();
        }catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }          
    }
    
    public void createCategoryList(){
        Category c = new Category("Sample Category Name", "Sample Category ID", "Sample User Name"); // Create sample category
        categoryArrayList.add(c); // Add sample to the category
    }
    
    
    public void writeCategoryListFile(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(categoryListFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(categoryArrayList);
            out.close();
        }catch(IOException ex) {
            ex.printStackTrace();
        }         
    }
    
    public void printCategoryList(){
//        System.out.println("The category list contains the following categories: ");
//        for(int i = 0; i < categoryArrayList.size(); i++) {
//            Category currentItem = (Category) categoryArrayList.get(i);
//            System.out.println(currentItem.getCategoryName());
//        }   
        System.out.print(toString());
    }
    @Override
    public String toString() {
        String categoryListAsString = "The category list contains the following categories: \n\n";
        for(Category c : categoryArrayList){
            categoryListAsString += (c.toString() + "\n\n");
        }
        return categoryListAsString;
    }

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryList) {
        this.categoryArrayList = categoryList;
    }
}

