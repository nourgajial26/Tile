// Tile.java
// This class implements a new type of objects representing tiles on a screen
//
// Version 1.0 - Original file CSE 143, Homework 1, Tiles
// Version 2.0 - Modified by Lauren Bricker 3/3/15

import java.awt.Color;
import java.awt.Graphics;

/** Each Tile object represents a 2D rectangular tile with a
    top-left x/y coordinate, width, height, and fill color. */
public abstract class Tile 
{
   
   /** The top left x coordinate of the bounding box of this tile */
   private int x;         
   
   /** The top left y coordinate of the bounding box of this tile */
   private int y;
   
   /* The width of the bounding box of this tile */
   private int width;

   /* The height of the bounding box of this tile */
   private int height;

   /* The fill color of this tile */
   private Color color;    
   
   /** 
    *  Constructs a new tile with the given coordinates, size, and color. 
    *  @param x The top left x coordinate of the bounding box of this tile
    *  @param y The top left y coordinate of the bounding box of this tile
    *  @param width The width of the bounding box of this tile
    *  @param height The height of the bounding box of this tile
    *  @param color The fill color of this tile
    */
   public Tile(int x, int y, int w, int h, Color c) 
   {
      this.x = x;
      this.y = y;
      this.width = w;
      this.height = h;
      this.color = c;
   }
   
   /** 
    * Draws this tile using the given graphics pen. 
    * @param g The graphics context on which to draw this tile. 
    */
   public abstract void draw(Graphics g); 

   /** 
    * Returns this tile's leftmost x coordinate. 
    * @return This tile's leftmost x coordinate of the bounding box. 
    */
   public int getX() 
   {
      return x;
   }
   
   /** 
    * Returns this tile's topmost y coordinate. 
    * @return This tile's topmost y coordinate of the bounding box. 
    */
   public int getY() 
   {
      return y;
   }
   
   /** 
    * Returns this tile's width
    * @return This tile's width of the bounding box. 
    */
   public int getWidth() 
   {
      return width;
   }
   
   /** 
    * Returns this tile's height. 
    * @return This tile's height of the bounding box. 
    */
   public int getHeight() 
   {
      return height;
   }
   
   /** 
    * Returns this tile's color. 
    * @return The color of this Tile
    */
   public Color getColor() 
   {
      return color;
   }

   /** 
    * Sets this tile's leftmost x-coordinate to be the given value. 
    * @param x The tile's leftmost x-coordinate of the bounding box
    */
   public void setX(int x) 
   {
      this.x = x;
   }

   /** 
    * Sets this tile's topmost y-coordinate to be the given value. 
    *  @param y This tile's topmost y coordinate of the bounding box. 
    */
   public void setY(int y) 
   {
      this.y = y;
   }

   /** 
    * Determines if this tile has been "hit" - if the x, y coordinate passed in is within the 
    * bounds of the shape. Note: Unless this is a rectangular shaped tile, the bounding box
    * will be bigger than the tile. The overridden methods must take this into account
    * @param x The x coordinate of the potential hit
    * @param y The y coordinate of the potential hit
    * @return Returns whether this tile was hit. The actual tile is circumscribed inside the bounding
    * box and that should be accounted for in the calculation. 
    */
   public abstract boolean isHit(int x, int y); 
  
  
   @Override
   public String toString() 
   {
      return "(x=" + x + ",y=" + y + ",w=" + width + ",h=" + height + ")";
   }
}
