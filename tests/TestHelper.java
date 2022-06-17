import org.junit.Before;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.imageio.ImageIO;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.Color;
import model.Image;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import model.ImageState;
import model.ImageTransform;
import model.RGBColor;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

import static org.junit.Assert.fail;

/**
 * Useful methods and fields for all sorts of image testing.
 */
public abstract class TestHelper {
  protected ImageTransform img0;
  protected Color[][] img1arr;
  protected ImageTransform img1;
  protected Color[][] img2arr;
  protected ImageTransform img2;
  protected Color c1;
  protected Color c2;
  protected Color c3;
  protected Color c4;
  protected Color c5;
  protected Color c6;
  protected ImageProcessingModel model;
  protected ImageState testImage;
  protected StringBuilder input1;
  protected StringReader readInput1;
  protected StringBuilder print1;
  protected ImageProcessingController controller1;
  protected ImageProcessingView newView1;
  protected ImageState observableImage;
  protected ImageState obsInput;
  protected double[][] blur;
  protected double[][] sharpen;
  protected double[][] greyscale;
  protected double[][] sepia;

  @Before
  public void initImages() {
    c1 = new RGBColor(0, 0, 0);
    c2 = new RGBColor(100, 50, 25);
    c3 = new RGBColor(50, 100, 25);
    c4 = new RGBColor(50, 25, 100);
    c5 = new RGBColor(25, 50, 100);
    c6 = new RGBColor(100, 100, 100);
    img0 = new Image(new Color[][]{}, 1);
    img1arr = new Color[][]{{c1, c2}, {c3, c4}, {c5, c6}};
    img1 = new Image(img1arr, 255);
    img2arr = new Color[][]{{c1, c2, c3}, {c4, c5, c6}};
    img2 = new Image(img2arr, 255);
    model = new ImageProcessingModelImpl();
  }

  @Before
  public void initController() {
    this.initImages();
    input1 = new StringBuilder();
    readInput1 = new StringReader(input1.toString());
    print1 = new StringBuilder();
    newView1 = new ImageProcessingViewImpl(print1);
    controller1 = new ImageProcessingControllerImpl(model, newView1, readInput1);
  }

  @Before
  public void initMatrices() {
    blur = new double[][]{
            {1 / 16., 1 / 8., 1 / 16.},
            {1 / 8., 1 / 4., 1 / 8.},
            {1 / 16., 1 / 8., 1 / 16.}};
    sharpen = new double[][]{
            {-1 / 8., -1 / 8., -1 / 8., -1 / 8., -1 / 8.},
            {-1 / 8., 1 / 4., 1 / 4., 1 / 4., -1 / 8.},
            {-1 / 8., 1 / 4., 1, 1 / 4., -1 / 8.},
            {-1 / 8., 1 / 4., 1 / 4., 1 / 4., -1 / 8.},
            {-1 / 8., -1 / 8., -1 / 8., -1 / 8., -1 / 8.}};
    greyscale = new double[][]{
            {0.393, 0.769, 0.189},
            {0.393, 0.769, 0.189},
            {0.393, 0.769, 0.189}};
    sepia = new double[][]{
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}};
  }

  protected ImageState imageAsComponent(ImageState img, Function<Color, Integer> component) {
    int height = img.getHeight();
    int width = img.getWidth();
    int maxValue = img.getMaxValue();
    Color[][] grayColorArr = new Color[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        int grayC = component.apply(img.getColorAt(row, col));
        grayColorArr[row][col] = new RGBColor(grayC, grayC, grayC);
      }
    }

    return new Image(grayColorArr, maxValue);
  }

  protected ImageState imageBrightness(ImageState img, int num) {
    int height = img.getHeight();
    int width = img.getWidth();
    int maxValue = img.getMaxValue();
    Color[][] colorArr = new Color[height][width];
    Function<Integer, Integer> minMax = n -> Math.max(Math.min(n + num, maxValue), 0);

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        int red = minMax.apply(img.getColorAt(row, col).getRed());
        int green = minMax.apply(img.getColorAt(row, col).getGreen());
        int blue = minMax.apply(img.getColorAt(row, col).getBlue());
        colorArr[row][col] = new RGBColor(red, green, blue);
      }
    }

    return new Image(colorArr, maxValue);
  }

  protected ImageState imageHorizontal(ImageState img) {
    int height = img.getHeight();
    int width = img.getWidth();
    int maxValue = img.getMaxValue();
    Color[][] colorArr = new Color[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        colorArr[row][col] = img.getColorAt(row, width - col - 1);
      }
    }

    return new Image(colorArr, maxValue);
  }

  protected ImageState imageVertical(ImageState img) {
    int height = img.getHeight();
    int width = img.getWidth();
    int maxValue = img.getMaxValue();
    Color[][] colorArr = new Color[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        colorArr[row][col] = img.getColorAt(height - row - 1, col);
      }
    }

    return new Image(colorArr, maxValue);
  }

  protected ImageState imageMatrixTransform(ImageState img, double[][] matrix) {
    if (matrix == null) {
      throw new IllegalArgumentException("Matrix cannot be null.");
    }
    if (matrix.length != 3) {
      throw new IllegalArgumentException("Matrix height must be a 3.");
    }
    for (double[] row : matrix) {
      if (row == null) {
        throw new IllegalArgumentException("Matrix row cannot be null.");
      }
      if (row.length != 3) {
        throw new IllegalArgumentException("Matrix width must be 3");
      }
    }

    int height = img.getHeight();
    int width = img.getWidth();
    int maxValue = img.getMaxValue();
    Color[][] colorArr = new Color[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        Color c = img.getColorAt(row, col);
        colorArr[row][col] = new RGBColor(calcChannel(matrix[0], c),
                calcChannel(matrix[1], c), calcChannel(matrix[2], c));
      }
    }

    return new Image(colorArr, maxValue);
  }

  private int calcChannel(double[] row, Color c) {
    return (int) (row[0] * c.getRed() + row[1] * c.getGreen() + row[2] * c.getBlue());
  }

  protected ImageState imageConvolve(ImageState img, double[][] kernel) {
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

    int height = img.getHeight();
    int width = img.getWidth();
    int kernelCentre = kernel.length / 2;
    int maxValue = img.getMaxValue();
    Color[][] colorArr = new Color[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        double r = 0;
        double g = 0;
        double b = 0;
        for (int kRow = -kernelCentre; kRow < kernelCentre + 1; kRow += 1) {
          for (int kCol = -kernelCentre; kCol < kernelCentre + 1; kCol += 1) {
            int sumR = row + kRow;
            int sumC = col + kCol;
            if (sumR >= 0 && sumR < height && sumC >= 0 && sumC < width) {
              Color sumColor = img.getColorAt(sumR, sumC);
              double value = kernel[kRow + kernelCentre][kCol + kernelCentre];
              r += sumColor.getRed() * value;
              g += sumColor.getGreen() * value;
              b += sumColor.getBlue() * value;
            }
          }
        }
        colorArr[row][col] = new RGBColor((int) r, (int) g, (int) b);
      }
    }

    return new Image(colorArr, maxValue);
  }

  protected ImageState imageFromState(ImageState s) {
    int height = s.getHeight();
    int width = s.getWidth();
    int maxValue = s.getMaxValue();
    Color[][] colorArr = new Color[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        colorArr[row][col] = s.getColorAt(row, col);
      }
    }

    return new Image(colorArr, maxValue);
  }

  protected ImageState fileToImage(String fileName) {
    File source = new File(fileName);
    BufferedImage img = null;
    try {
      img = ImageIO.read(source);
      if (img == null) throw new IOException();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to read file.");
    }
    Color[][] pane = new Color[img.getHeight()][img.getWidth()];
    for (int row = 0; row < pane.length; row += 1) {
      for (int col = 0; col < pane[row].length; col += 1) {
        int color = img.getRGB(col, row);
        int alpha = (color >> 24) & 0xff;
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;
        pane[row][col] = new RGBColor(r, g, b, alpha);
      }
    }
    return new Image(pane);
  }
}