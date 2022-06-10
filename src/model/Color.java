package model;

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
   * @return the blue channel of the color
   */
  int getTransparency() {
    return transparency;
  }

  /**
   * Get the largest component of this color (between 0-255).
   *
   * @return the largest channel of the color
   */
  int getValue() {
    return Math.max(Math.max(red, green), blue);
  }

  /**
   * Get the intensity of this color (average of the components).
   *
   * @return the intensity of the color
   */
  int getIntensity() {
    return (red + green + blue) / 3;
  }

  /**
   * Get the weighted sum of the color using the
   *
   * @return the luminescence of the color
   */
  int getLuma() {
    return (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
  }

}
