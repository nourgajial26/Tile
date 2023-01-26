// Nour G.
// Period 1
// Assignment 11
// This program manages tiles within an ArrayList 
// and edits the list to perform a specific action.
// TileManager
// March 25, 2021

import java.util.*;
import java.util.ArrayList;
import java.awt.*;

public class TileManager 
{
   /*
   * The array list which is used to store tiles
   */
   private ArrayList<Tile> tileList;
   
   /** 
   *  Constructs TileManager and initializes any instance variables
   */
   public TileManager()
   {
      this.tileList = new ArrayList<>();
   }
   
   /**
   * Adds a tile to the end of the tileList list
   * @param tile the tile to add
   */
   public boolean addTile(Tile tile)
   {
      if(tile == null)
      {
         return false;
      }
      
      else
      {
         tileList.add(tile);
         return true;
      }
   }
   
   /**
   * Draws a tile from the tileList Array list
   * @param g draws shape on screen
   */
   public void drawAll(Graphics g)
   {
      for(int i = 0; i < this.tileList.size(); i++)
      {
         Tile t = tileList.get(i);
         t.draw(g);
      }
   }
   
   /**
   * Loops through the tile ArrayList starting from index 0
   * @param x x coordinate of area hit
   * @param y y coordinate of area hit
   */
   private Tile loop(int x, int y)
   {
      for(int i = 0; i < this.tileList.size(); i++)
      {
         Tile t = tileList.get(i);
         if(t.isHit(x, y) == true)
         {
            return t;
         }
      }
      return null;
   }
   
   /**
   * Loops through the tile ArrayList starting from last index
   * @param x x coordinate of area hit
   * @param y y coordinate of area hit
   */
   private Tile backLoop(int x, int y)
   {
      for(int i = this.tileList.size()-1; i >= 0; i--)
      {
         Tile t = tileList.get(i);
         if(t.isHit(x, y) == true)
         {
            return t;
         }
      }
      return null;
   }
      
   /** 
   * Moves a tile to the end of a list (moves forward on screen)
   * @param x x position of area hit
   * @param y y position of area hit
   */
   public void raise(int x, int y)
   {
      Tile raise = loop(x, y);
      if(raise != null)
      {
         tileList.remove(raise);
         tileList.add(raise);
      }
   }
   
   /** 
   * Moves tile to the beginning of the list (moves back on screen)
   * @param x x position of area hit
   * @param y y position of area hit
   */
   public void lower(int x, int y)
   {
      Tile lower = backLoop(x, y);
      if(lower != null)
      {
         tileList.remove(lower);
         tileList.add(0, lower);
      }
   }
   
   /** 
   * Deletes tiles which are clicked on from the topmost of the list
   * @param x x position of area hit
   * @param y y position of area hit
   */
   public void delete(int x, int y)
   {
      Tile t = backLoop(x, y);
      tileList.remove(t);
   }
   
   /** 
   * If the tiles touch each other, and the user shift-right clicks,
   * all tiles under the hit coordinate will be removed
   * @param x x position of area hit
   * @param y y position of area hit
   */
   public void deleteAll(int x, int y)
   {
      for(int i = 0; i < tileList.size(); i ++)
      {
         Tile t = loop(x, y);
         tileList.remove(t);
      }
   }
   
   /** 
   * Re-orders the tiles in the list and moves the tiles to new positions.
   * @param width width of drawing panel
   * @param height height of drawing panel
   */
   public void shuffle(int width, int height)
   {
      Collections.shuffle(tileList);
      for(int i = 0; i < tileList.size(); i++)
      {
         Tile t = tileList.get(i);
         int xMax = width - t.getWidth();
         int yMax = height - t.getHeight();
         
         Random r = new Random();
         t.setX(r.nextInt(xMax));
         t.setY(r.nextInt(yMax));
      }
   }
}