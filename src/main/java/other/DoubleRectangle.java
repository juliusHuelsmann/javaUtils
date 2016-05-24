package other;

import java.io.Serializable;

public class DoubleRectangle implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private double x;
  private double y;
  private double width;
  private double height;

  public DoubleRectangle() { 
    
    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;
  }

  public DoubleRectangle(final double xx, final double xy, 
      final double xwidt, final double xheight) { 
    this.x = xx;
    this.y = xy;
    this.width = xwidt;
    this.height = xheight;
  }
  /**
   * @return the height
   */
  public double getHeight() {
    return height;
  }
  /**
   * @param height the height to set
   */
  public void setHeight(double height) {
    this.height = height;
  }
  /**
   * @return the width
   */
  public double getWidth() {
    return width;
  }
  /**
   * @param width the width to set
   */
  public void setWidth(double width) {
    this.width = width;
  }
  /**
   * @return the y
   */
  public double getY() {
    return y;
  }
  /**
   * @param y the y to set
   */
  public void setY(double y) {
    this.y = y;
  }
  /**
   * @return the x
   */
  public double getX() {
    return x;
  }
  /**
   * @param x the x to set
   */
  public void setX(double x) {
    this.x = x;
  }
  
  
}
