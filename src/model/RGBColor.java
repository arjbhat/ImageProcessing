package model;

import java.util.Objects;

/**
 * An RGB color consists of three channels (red, green, and blue).
 * Each channel value is a positive integer.
 */
public class RGBColor {
  private final int red;
  private final int green;
  private final int blue;
  private final int transparency;

  /**
   * In order to construct a single RGB color with 0 transparency (it's opaque).
   *
   * @param red   The red component of the color
   * @param green The green component of the color
   * @param blue  The blue component of the color
   */
  public RGBColor(int red, int green, int blue) throws IllegalArgumentException {
    this(red, green, blue, 0);
  }

  /**
   * In order to construct a single RGB color with some transparency (it may not be opaque).
   *
   * @param red          The red component of the color
   * @param green        The green component of the color
   * @param blue         The blue component of the color
   * @param transparency The transparency of the color (0 for opaque)
   */
  public RGBColor(int red, int green, int blue, int transparency) throws IllegalArgumentException {
    if (red < 0 || green < 0 || blue < 0 || transparency < 0) {
      throw new IllegalArgumentException("Cannot represent this color composition.");
    }

    this.red = red;
    this.green = green;
    this.blue = blue;
    this.transparency = transparency;
  }

  /**
   * Get the red component of this color (between 0-255).
   *
   * @return the red channel of the color
   */
  public int getRed() {
    return red;
  }

  /**
   * Get the green component of this color (between 0-255).
   *
   * @return the green channel of the color
   */
  public int getGreen() {
    return green;
  }

  /**
   * Get the blue component of this color (between 0-255).
   *
   * @return the blue channel of the color
   */
  public int getBlue() {
    return blue;
  }

  /**
   * Get the transparency this color (between 0-255).
   *
   * @return the blue channel of the color
   */
  public int getTransparency() {
    return transparency;
  }

  /**
   * Get the largest component of this color (between 0-255).
   *
   * @return the largest channel of the color
   */
  public int getValue() {
    return Math.max(Math.max(red, green), blue);
  }

  /**
   * Get the intensity of this color (average of the components).
   *
   * @return the intensity of the color
   */
  public int getIntensity() {
    return (red + green + blue) / 3;
  }

  /**
   * Get the weighted sum of the color using the
   *
   * @return the luminescence of the color
   */
  public int getLuma() {
    return (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
  }
  
  /**
   * Two RGBColors are equal if they have the same values for the red, green, and blue channels.
   *
   * @param obj the object that we are comparing this color to
   * @return true if the two colors are the same or false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RGBColor)) return false;

    RGBColor that = (RGBColor) obj;
    return this.getRed() == that.getRed()
            && this.getBlue() == that.getBlue()
            && this.getGreen() == that.getGreen()
            && this.getTransparency() == that.getTransparency();
  }

  /**
   * A hash that represents a specific color.
   *
   * @return a hash for that color
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.getRed(), this.getGreen(), this.getBlue(), this.getTransparency());
  }
}
