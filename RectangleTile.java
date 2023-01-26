// Nour G.
// Period 1
// Assignment 11
// This class implements a rectangular tile on the screen
// RectangleTile
// March 25,2021

import java.awt.*;

public class RectangleTile extends Tile 
{

  /** 
   *  Constructs a new rectangular shaped tile with the given coordinates, size, and color. 
   *  @param x The top left x coordinate of the rectangle tile
   *  @param y The top left y coordinate of the rectangle tile
   *  @param width The width of the rectangle tile
   *  @param height The height of the rectangle tile
   *  @param color The fill color of this tile
   */
   public RectangleTile(int x, int y, int w, int h, Color c) 
   {
      super(x, y, w, h, c);
   }
    
   @Override
   public void draw(Graphics g) 
   {  
      int finalx = getX(); 
      int finaly = getY(); 
      int finalwidth = getWidth(); 
      int finalheight = getHeight(); 
      
      g.setColor(getColor());
      g.fillRect(finalx, finaly, finalwidth, finalheight);
      g.setColor(Color.BLACK);
      g.drawRect(finalx, finaly, finalwidth, finalheight);
   }

   @Override
   public boolean isHit(int x, int y) 
   { 
      int xDist = getX() + getWidth();
      int yDist = getY() + getHeight();
      if (x <= xDist && x >= getX() && y <= yDist && y >= getY()) 
      {
         return true;
      }
      return false;
   }
   
   @Override
   public String toString() {
      return "Rectangle: " + super.toString(); 
   }
}


      
      