package model;

import java.util.Objects;

/**
 * An RGB color consists of three channels (red, green, and blue).
 * Each channel value is a positive integer.
 */
public class RGBColor implements Color {
  // INVARIANT: Is a positive integer (0 inclusive)
  // Reason: The channel value can be as high as the image specification
  // and 0 will always be black
  private final int red;
  // INVARIANT: Is a positive integer (0 inclusive)
  // Reason: The channel value can be as high as the image specification
  // and 0 will always be black
  private final int green;
  // INVARIANT: Is a positive integer (0 inclusive)
  // Reason: The channel value can be as high as the image specification
  // and 0 will always be black
  private final int blue;
  // INVARIANT: Is a positive integer (0 inclusive)
  // Reason: The channel value can be as high as the image specification
  // and 0 will always be fully transparent
  private final int alpha;

  /**
   * In order to construct a single RGB color with 0 transparency (it's opaque).
   *
   * @param red   The red component of the color
   * @param green The green component of the color
   * @param blue  The blue component of the color
   */
  public RGBColor(int red, int green, int blue) {
    this(red, green, blue, 255);
  }

  /**
   * In order to construct a single RGB color with some alpha value.
   *
   * @param red   The red component of the color
   * @param green The green component of the color
   * @param blue  The blue component of the color
   * @param alpha The alpha value of the color (0 for transparent)
   */
  public RGBColor(int red, int green, int blue, int alpha) {
    this.red = Math.max(red, 0);
    this.green = Math.max(green, 0);
    this.blue = Math.max(blue, 0);
    this.alpha = Math.max(alpha, 0);
  }

  @Override
  public int getRed() {
    return red;
  }

  @Override
  public int getGreen() {
    return green;
  }

  @Override
  public int getBlue() {
    return blue;
  }

  @Override
  public int getAlpha() {
    return alpha;
  }

  @Override
  public int getValue() {
    return Math.max(Math.max(this.getRed(), this.getGreen()), this.getBlue());
  }

  @Override
  public int getIntensity() {
    return (this.getRed() + this.getGreen() + this.getBlue()) / 3;
  }

  @Override
  public int getLuma() {
    return (int) (0.2126 * this.getRed() + 0.7152 * this.getGreen() + 0.0722 * this.getBlue());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Color)) {
      return false;
    }

    Color that = (Color) obj;
    return this.getRed() == that.getRed()
        && this.getBlue() == that.getBlue()
        && this.getGreen() == that.getGreen()
        && this.getAlpha() == that.getAlpha();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha());
  }
}
