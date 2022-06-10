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
  // and 0 will always be opaque
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
      throw new IllegalArgumentException("Color channel cannot be below 0.");
    }

    this.red = red;
    this.green = green;
    this.blue = blue;
    this.transparency = transparency;
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
  public int getTransparency() {
    return transparency;
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
        && this.getTransparency() == that.getTransparency();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getRed(), this.getGreen(), this.getBlue(), this.getTransparency());
  }
}
