// TileMainAutoload.java
// This provided main program uses your TileManager class.
// It displays a DrawingPanel, creates several random Tile objects,
// and adds them to your manager.  It also listens for mouse clicks, notifying
// your tile manager when the mouse buttons are pressed.

// A left-click raises a tile to the top of the Z-order.
// A Shift-left-click lowers a tile to the bottom of the Z-order.
// A right-click (or a Ctrl-left-click for Mac people) deletes a tile.
// A Shift-right-click (or a Shift-Ctrl-left-click for Mac people) deletes 
// all tiles touching the mouse point.
//
// Version 1.0 - Originally written by UW CSE 143, assignment 1
// Version 2.0 - Modified by Lauren Bricker 3/3/15
// Version 3.0 - Modified by Jake Kurlander Sometime in Spring 2016
// Version 3.1 - Modified by Lauren Bricker to fix up compiler warnings 3/5/17

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class TileMain
{
   // constants for the drawing panel size, tile sizes, and # of tiles
   /** DrawingPanel width */
   public static final int WIDTH = 300;

   /** DrawingPanel height */
   public static final int HEIGHT = 300;
   
   /** The minimum size of a tile */
   public static final int MIN_SIZE = 20;

   /** The maximum size of a tile */
   public static final int MAX_SIZE = 100;
   
   /** The number of tiles to initially have on the screen */
   public static final int TILES = 20;
   
   /** Variable that when set to true to catch and print any exceptions that occur */
   private static final boolean CATCH_EXCEPTIONS = true;
   
   /** Variable that contains all of the constructors for types
       of tiles that the program finds in the same directory */
   private static ArrayList<Constructor> tileConstructors;
   
   public static void main(String[] args) 
   {
      Random rand = new Random(36);
      
      DrawingPanel panel = new DrawingPanel(WIDTH, HEIGHT);
      Graphics g = panel.getGraphics();
        
      try {   
         //Create a new file, which goes into the directory this is being run from. 
         //Then, check what directory this new file is in. This is where to look for Tile files. 
         String filePathOfDirectory = new java.io.File( "." ).getCanonicalPath();
        
        
         //Look for Tile files, and make them into classes.
         ArrayList<Class<?>> classes = getClasses(filePathOfDirectory);
        
        
         //Finally, get the constructors for all of these Tile classes.
         tileConstructors = getConstructors(classes); 
      }
      catch (Exception e) {
         System.out.println("Very bad things just happened. Sorry. Program probably won't work at all now. Yikes. Sorry.");
         //uh oh. things go very bad if this doesn't work.
      }
   
      
      // create several random tiles and put them into a manager
      TileManager list = new TileManager();
      for (int i = 0; i < TILES; i++) 
      {
         //Uses the new Class-randomizing tile-making method!
         Tile tile = makeATile(tileConstructors, rand);
         list.addTile(tile);
      }
      list.drawAll(g);
      
      // listen for key presses
      TileKeyListener listener = new TileKeyListener(panel, list, rand);
      panel.addKeyListener(listener);
      
      // listen for mouse clicks
      TileMouseListener listener2 = new TileMouseListener(panel, list);
      panel.addMouseListener(listener2);
   }
   
   /**
   * This method creates a list of all of the classes that extend "Tile" from the directory TileMain is stored in.                            //This one!
   * It takes in a parameter, which is the directory to find the classes in.                                                                  
   * It makes a list of all of the files in the folder, then removes all of them that have names ending in something other than ".class"
   * Then it casts all of those as Classes, and finds a list of them that extend Tile. Finally, it returns the list.
   * @param directoryName this is the name of the folder to find all the classes in. In this program, it's passed in as whatever directory this is in, but it can be changed easily.
   * @return classes the ArrayList<Class> of classes, all of which must extend Tile, in the directory passed in.
   */
   public static ArrayList<Class<?>> getClasses (String directoryName) {
      java.io.File directory = new java.io.File(directoryName);
      
      //I get all the files from the directory, then put them in an arraylist.      
      ArrayList<File> files =  new ArrayList<File>(Arrays.asList(directory.listFiles())); 

      // Go through all the files in the directory.
      for (int ii = 0; ii < files.size(); ii++) { 
      try {
         
            //If it's not a .class file remove it.
            String name = files.get(ii).getName();
            if (!name.substring(name.length()-6).equals(".class") ) { 
               files.remove(ii);
               ii--;
            } 
         }
         //If the file name is shorter than 6 characters ("a.txt") , the previous thing throws an exception, so remove it in that case.
         catch (IndexOutOfBoundsException e) { 
            files.remove(ii);
            ii--;
         }
      }
      Class<?> tileClass = null;
   
      // This is the ArrayList that eventually gets returned.            
      ArrayList<Class<?>> classes = new ArrayList<Class<?>>(); 
      
      // The ClassLoader constructor is private, so you can't 
      // build new ones. I have to get the one used to load this class.
      ClassLoader classLoader = ClassLoader.getSystemClassLoader(); 
      
      // Need to use the Tile class to figure out whether or not various 
      // classes extend Tile, so grab it here.
      try {
         tileClass = classLoader.loadClass("Tile");
      }
      catch (ClassNotFoundException e) {
         System.out.println("Put the Tile Class in the same folder as this program!");
         System.out.println("Ending Program...");
         System.exit(0);
      }
   
      //Then, for each of these files (all of which are .class files)...
      for (int ii = 0; ii < files.size(); ii++) {
      
         Class<?> aClass = null;
         
         try {
         
            //Load the .class file into a class object,
            String name = files.get(ii).getName();
            String classNameWithoutFileExtension = name.substring(0, name.length()-6); // .class
            aClass = classLoader.loadClass(classNameWithoutFileExtension); 
               
            //And if it extends Tile but isn't just Tile, add it to the ArrayList.
            if (tileClass.isAssignableFrom(aClass) && !aClass.equals(tileClass)) { 
               classes.add(aClass); 
            }  
            
         }
          
         //This should never happen. We just got this class from a file, its file should still be there.
         catch (ClassNotFoundException e) {
            System.out.println(e);
         }
      }
      
      
      if (classes.size() == 0) {
         System.out.println("You need to have some Tile-extending classes in the same directory as TileMain!");
         System.out.println("Put some there and try again!");
         System.out.println("Ending Program...");         
         System.exit(0);
      }
      
      //And now we've got all the .class files that extend Tile loaded up in this ArrayList<Class>!
      return classes; 
   
   }   
      
   
   /**
   * This method gets an arraylist of constructors from an arraylist of classes.
   * For each class, it gets the constructor, and if it can create a new Tile object from the constructor,
   * It adds the constructor to the new ArrayList. Then, after it's all done, it returns the
   * Arraylist.
   * @param classes ArrayList of classes to get Constructors from.
   * @return constructors Arraylist of constructors.
   */
   public static ArrayList<Constructor> getConstructors (ArrayList<Class<?>> classes) {
      
      //This is the eventually-returned ArrayList of constructors.
      ArrayList<Constructor> constructors = new ArrayList<Constructor>();
      

      //Go through each class...      
      for (int ii = 0; ii < classes.size(); ii++) {
         try {

            //Get the constructor from each class...
            //This first array is to avoid compiler warnings
            Class<?>[] types = new Class[] {int.class,  Color.class};
            Constructor<?> c = classes.get(ii).getConstructor(types[0], types[0], types[0], 
                                                              types[0], types[1]);
            
            //Make an array of a default parameter set for a tile, to pass into the example Tile.
            Object[] obs = {0, 0, 0, 0, Color.BLACK};
            
            //If this new sample Tile's instantiation fails, it will skip onto the catch blocks, 
            //and the constructor will not be added to the ArrayList.                
            Tile o = (Tile) c.newInstance(obs);
         
            // Try to grab the constructor from this chosen class and add it to our new ArrayList<Constructor>.
            constructors.add(c); 
         }
         catch (NoSuchMethodException e) {
            System.out.println("There is no constructor with the parameters Int-Int-Int-Int-Color in the class "+ classes.get(ii).getName()+"!");  
         }
         catch (InstantiationException e) {
         }
         catch (IllegalAccessException e) {
         }
         catch (InvocationTargetException e) {
         
         }  
      }
      
      return constructors;
      
   }
   
   
   /**
   * This method makes a new Tile, choosing randomly what kind of Tile to create from an ArrayList of tile classes passed in.
   * It also randomly chooses other things, such as the Tile's width, height, x, y, and color using a random object passed in.                   
   * @param tiles ArrayList<Constructor> of tile constructors to choose from.
   * @param r Random object with which to create the random parameters for the tile.
   * @return t Tile 
   */
   public static Tile makeATile (ArrayList<Constructor> tiles, Random r) {
      
      //Create random atributes for the tile.
      //Randomly choose a type of tile.
      int randomTileType = r.nextInt(tiles.size()); 
      //Choose a width and height for the tile.      
      int randomWidth = r.nextInt(MAX_SIZE-MIN_SIZE) + MIN_SIZE;
      int randomHeight = r.nextInt(MAX_SIZE-MIN_SIZE) + MIN_SIZE;
       //Choose an X and Y position for the Tile.
      int randomX = r.nextInt(WIDTH-randomWidth);
      int randomY = r.nextInt(HEIGHT-randomHeight);
      //Choose a random R, G, and B value for the color
      int randomR = r.nextInt(256); 
      int randomG = r.nextInt(256); 
      int randomB = r.nextInt(256);
      Color randomColor = new Color (randomR, randomG, randomB);
   
      Tile t = null;
      
      //
      try {
         t = (Tile)tiles.get(randomTileType).newInstance(randomX, randomY, randomWidth, 
                                                         randomHeight, randomColor);
      }
      catch (InstantiationException e) {
         System.out.println("Do not make your tile classes abstract!" + e);
      }
      catch (IllegalAccessException e) {
         System.out.println("Do not make your tile classes private!" + e);
      }
      catch (Exception e){
         System.out.println(e);
      }
      return t;
   }
   
   
   /**
    * A class for responding to mouse clicks on the drawing panel.
    */
   public static class TileMouseListener extends MouseInputAdapter 
   {
      private DrawingPanel panel;
      private TileManager list;
      
      public TileMouseListener(DrawingPanel panel, TileManager list) 
      {
         this.panel = panel;
         this.list = list;
      }
      
      public void mousePressed(MouseEvent event) 
      {
         int x = event.getX() / panel.getZoom();
         int y = event.getY() / panel.getZoom();
         
         try {
            if (event.isControlDown() || SwingUtilities.isRightMouseButton(event)) 
            {
               // treat right-clicks and control-left-clicks as the same (for Mac users)
               if (event.isShiftDown()) 
               {
                  list.deleteAll(x, y);
               } 
               else 
               {
                  list.delete(x, y);
               }
            } 
            else 
            {
               if (event.isShiftDown()) 
               {
                  list.lower(x, y);
               } 
               else 
               {
                  list.raise(x, y);
               }
            }
         
            // repaint all of the tiles
            panel.clear();
            Graphics g = panel.getGraphics();
            list.drawAll(g);
         } 
         catch (RuntimeException e) 
         {
            if (CATCH_EXCEPTIONS) 
            {
               e.printStackTrace(System.err);
            } 
            else 
            {
               throw e;
            }
         }
      }
   }

   /** A class for responding to key presses on the drawing panel.
    */
   public static class TileKeyListener extends KeyAdapter 
   {
      private DrawingPanel panel;
      private TileManager list;
      private Random rand; 
      
      public TileKeyListener(DrawingPanel panel, TileManager list, Random rand) 
      {
         this.panel = panel;
         this.list = list;
         this.rand = rand; 
      }
      
      public void keyPressed(KeyEvent event) 
      {
         int code = event.getKeyCode();
         if (code == KeyEvent.VK_N) 
         {
            Tile newTile = makeATile(tileConstructors, rand);
            list.addTile(newTile);
            Graphics g = panel.getGraphics();
            list.drawAll(g);
         } 
         else if (code == KeyEvent.VK_S) 
         {
            list.shuffle(WIDTH, HEIGHT);
            Graphics g = panel.getGraphics();
            panel.clear();
            list.drawAll(g);
         }
      }
   }
}
