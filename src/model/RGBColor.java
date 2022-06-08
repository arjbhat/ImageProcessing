package model;

import java.util.Objects;

/**
 * An RGB color consists of three channels (red, green, and blue).
 * Each channel value ranges between 0 and 255.
 */
public class RGBColor {
  //INVARIANT: Between 0 and 255 (inclusive)
  private final int red;
  //INVARIANT: Between 0 and 255 (inclusive)
  private final int green;
  //INVARIANT: Between 0 and 255 (inclusive)
  private final int blue;

  /**
   * In order to construct a single RGB color.
   *
   * @param red   The red component of the color
   * @param green The green component of the color
   * @param blue  The blue component of the color
   */
  public RGBColor(int red, int green, int blue) {
    if (red > 255 || red < 0 || green > 255 || green < 0 || blue > 255 || blue < 0) {
      throw new IllegalArgumentException("Cannot represent this color composition.");
    }

    this.red = red;
    this.green = green;
    this.blue = blue;
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

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RGBColor)) return false;
    RGBColor that = (RGBColor) obj;

    return this.getRed() == that.getRed()
            && this.getBlue() == that.getBlue()
            && this.getGreen() == that.getGreen();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getRed(), this.getBlue(), this.getGreen());
  }


}
