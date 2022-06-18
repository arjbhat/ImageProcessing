package model.macros;

import model.Color;
import model.ImageTransform;
import model.RGBColor;

/**
 * Represents a convolution, filtering an image based on a kernel. It is used to change an image
 * by changing each pixel based on the surrounding pixels.
 */
public class Convolve implements Macro {
  private final double[][] kernel;

  public Convolve(double[][] kernel) {
    if (kernel == null) {
      throw new IllegalArgumentException("Kernel cannot be null.");
    }
    if (kernel.length % 2 == 0) {
      throw new IllegalArgumentException("Invalid Kernel size.");
    }
    for (double[] row : kernel) {
      if (row == null) {
        throw new IllegalArgumentException("Kernel row cannot be null.");
      }
      if (row.length != kernel.length) {
        throw new IllegalArgumentException("Kernel must be square");
      }
    }
    this.kernel = kernel;
  }

  /**
   * Transforms all the pixels based on the pixels around it relative to the scale in the kernel.
   *
   * @param img the image that we're working on
   * @return a new image that has undergone the transformation
   * @throws IllegalArgumentException if the image passed in is null
   */
  @Override
  public ImageTransform execute(ImageTransform img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    int kernelCentre = kernel.length / 2;
    int imgHeight = img.getHeight();
    int imgWidth = img.getWidth();

    return img.transform((c, y, x) -> {
      double r = 0;
      double g = 0;
      double b = 0;
      for (int row = -kernelCentre; row < kernelCentre + 1; row += 1) {
        for (int col = -kernelCentre; col < kernelCentre + 1; col += 1) {
          int sumR = y + row;
          int sumC = x + col;
          if (sumR >= 0 && sumR < imgHeight && sumC >= 0 && sumC < imgWidth) {
            Color sumColor = img.getColorAt(sumR, sumC);
            double value = kernel[row + kernelCentre][col + kernelCentre];
            r += sumColor.getRed() * value;
            g += sumColor.getGreen() * value;
            b += sumColor.getBlue() * value;
          }
        }
      }

      return new RGBColor((int) r, (int) g, (int) b);
    });
  }
}
