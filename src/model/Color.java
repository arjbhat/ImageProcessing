package model;

/**
 * The color interface that is used to represent colors that have a Red, Green, and Blue channel.
 * The intensity, luma, and value can also be observed.
 */
public interface Color {
  /**
   * Get the red component of this color (between 0-255).
   *
   * @return the red channel of the color
   */
  int getRed();

  /**
   * Get the green component of this color (between 0-255).
   *
   * @return the green channel of the color
   */
  int getGreen();

  /**
   * Get the blue component of this color (between 0-255).
   *
   * @return the blue channel of the color
   */
  int getBlue();

  /**
   * Get the transparency this color (between 0-255).
   *
   * @return the transparency of the color.
   */
  int getTransparency();

  /**
   * Get the largest component of this color (between 0-255).
   *
   * @return the largest channel of the color
   */
  int getValue();

  /**
   * Get the intensity of this color (average of the components).
   *
   * @return the intensity of the color
   */
  int getIntensity();

  /**
   * Get the weighted sum of the color using the
   *
   * @return the luminescence of the color
   */
  int getLuma();

  /**
   * Two RGBColors are equal if they have the same values for the red, green, and blue channels,
   * and are equally transparent.
   *
   * @param other the object that we are comparing this color to
   * @return true if the two colors are the same or false otherwise
   */
  boolean equals(Object other);

  /**
   * A hash that represents a specific color.
   *
   * @return a hash for that color
   */
  int hashCode();
}
