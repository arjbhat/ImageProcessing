package model;

import java.util.Arrays;
import java.util.Objects;

/**
 * The immutable image that we are working on. Any changes to the image will only produce a new
 * image. An image is a 2D array of colors and a max value for the channels.
 */
public class Image implements ImageTransform {
  // INVARIANT: Is a non-negative integer
  // Reason: You can have an image with no height (blank image)
  private final int height;
  // INVARIANT: Is a non-negative integer
  // Reason: You can have an image with no width (blank image)
  private final int width;
  // INVARIANT: Is a positive integer
  // Reason: Cannot be 0 because 0 only represents black but can be any positive integer
  // because we can always change what white represents (and base our color scale accordingly)
  private final int maxValue;
  // INVARIANT: Non-null Color array
  // INVARIANT: All rows are non-null
  // INVARIANT: All rows have length this.width
  private final Color[][] pane;

  /**
   * We can construct a new image by passing in a 2D array of Colors.
   *
   * @param img the color grid that we want to construct an image with
   * @throws IllegalArgumentException if the img is invalid
   */
  public Image(Color[][] img) {
    this(img, 255);
  }

  /**
   * We can construct a new image by passing in a 2D array of Colors and the maximum value for
   * each of the 3 channels.
   *
   * @param img the color grid that we want to construct an image with
   * @throws IllegalArgumentException if the img is invalid or the maxValue is not positive
   */
  public Image(Color[][] img, int maxValue) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Color array cannot be null.");
    }
    if (maxValue < 1) {
      throw new IllegalArgumentException("Invalid max value.");
    }
    this.height = img.length;
    this.maxValue = maxValue;

    if (height == 0 || img[0] == null) {
      this.width = 0;
    } else {
      this.width = img[0].length;
    }

    this.pane = new Color[height][width];

    for (int row = 0; row < height; row += 1) {
      if (img[row] == null) {
        throw new IllegalArgumentException("Pixel row cannot be null.");
      }
      if (img[row].length != this.width) {
        throw new IllegalArgumentException("Color grid is not rectangular.");
      }
      for (int col = 0; col < width; col += 1) {
        Color c = img[row][col];
        if (c == null) {
          throw new IllegalArgumentException("Pixel color cannot be null.");
        }
        this.pane[row][col] = new RGBColor(clamp(c.getRed()),
                clamp(c.getGreen()), clamp(c.getBlue()));
      }
    }
  }

  // So we don't have to make a copy unnecessarily - every time we transform the image.
  private Image(Color[][] pane, int height, int width, int maxValue) {
    this.pane = pane;
    this.height = height;
    this.width = width;
    this.maxValue = maxValue;
  }

  @Override
  public int getMaxValue() {
    return this.maxValue;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public Color getColorAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || col < 0 || row >= height || col >= width) {
      throw new IllegalArgumentException("Invalid location.");
    }
    return pane[row][col];
  }

  @Override
  public ImageTransform transform(ColorFunction map) throws IllegalArgumentException {
    if (map == null) {
      throw new IllegalArgumentException("Function cannot be null.");
    }
    Color[][] newPane = new Color[height][width];
    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        Color c = map.apply(this.pane[row][col], row, col);
        if (c == null) {
          throw new IllegalArgumentException("Pixel color cannot be null.");
        }
        newPane[row][col] = new RGBColor(clamp(c.getRed()),
                clamp(c.getGreen()), clamp(c.getBlue()));
      }
    }
    return new Image(newPane, height, width, maxValue);
  }

  private int clamp(int n) {
    return Math.max(Math.min(n, maxValue), 0);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ImageState)) {
      return false;
    }

    ImageState that = (ImageState) obj;
    if (!(this.height == that.getHeight()
            && this.width == that.getWidth()
            && this.maxValue == that.getMaxValue())) {
      return false;
    }
    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        if (!this.pane[row][col].equals(that.getColorAt(row, col))) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(height, width, maxValue, Arrays.deepHashCode(pane));
  }
}
