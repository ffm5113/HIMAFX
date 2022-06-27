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
public class ItemList {
    public ArrayList<Item> itemArrayList = new ArrayList<>(); // Array List of items
    private String itemListFile = "itemListFile.ser"; // Serializable file name
    // Constructor
    public ItemList(){

        try {
        readItemListFile();
        }
        catch(Exception ex) {
            System.out.println("Item list file not found but will be created.");
            
        }
        if(itemArrayList.isEmpty() || itemArrayList == null) {
            createItemList();
            writeItemListFile();
            readItemListFile();
        }
        this.printItemList();
        System.out.println("Total number of items: " + itemArrayList.size() + ".\n"); // For debugging
    }

    public void readItemListFile() {
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(itemListFile);
            in = new ObjectInputStream(fis);
            itemArrayList = (ArrayList)in.readObject();
            in.close();
        }catch(FileNotFoundException ex) {
            System.out.println("Create new item list file.");
        }catch(IOException ex) {
            ex.printStackTrace();
        }catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }          
    }
    
    public void createItemList(){
        Item i = new Item("Sample Category Name", "Sample Item Name", "Sample Item ID", "Sample Item Location", "Sample Item Email"); // Create sample item
        itemArrayList.add(i); // Add item to the item list
    }
    
    public void writeItemListFile(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(itemListFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(itemArrayList);
            out.close();
        }catch(IOException ex) {
            ex.printStackTrace();
        }         
    }
    
    public void printItemList(){
//        System.out.println("The item list contains the following items: \n\n");
//        for(int i = 0; i < itemArrayList.size(); i++) {
//            Item currentItem = (Item) itemArrayList.get(i);
//            System.out.println(currentItem.getItemName());
//        }   
        System.out.print(toString());
    }
    @Override
    public String toString(){
        String itemListAsString = "The item list contains the following items: \n\n";
        for(Item i : itemArrayList){
            itemListAsString+= (i.toString() + "\n\n");
        }
        return itemListAsString;
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<Item> itemList) {
        this.itemArrayList = itemList;
    }
}
