package model;

import java.util.function.Function;

public class Image {
  private final int height;
  private final int width;
  private final RGBColor[][] pane;

  public Image(int[][][] img) {
    this.height = img.length;

    if (height == 0) {
      this.width = 0;
    } else {
      this.width = img[0].length;
    }

    this.pane = new RGBColor[height][width];

    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        pane[row][col] = new RGBColor(img[row][col][0], img[row][col][1], img[row][col][2]);
      }
    }
  }

  private Image(RGBColor[][] pane) {
    this.pane = pane;
    this.height = pane.length;
    if (height == 0) {
      this.width = 0;
    } else {
      this.width = pane[0].length;
    }
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public RGBColor getColorAt(int row, int col) {
    if (row < 0 || col < 0 || row >= height || col >= width) {
      throw new IllegalArgumentException("Invalid location");
    }
    return pane[row][col];
  }

  public Image transformed(Function<RGBColor, RGBColor> transform) {
    RGBColor[][] newPane = new RGBColor[height][width];
    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        newPane[row][col] = transform.apply(this.pane[row][col]);
      }
    }
    return new Image(newPane);
  }

  public Image asRedGrayscale() {
    return this.transformed(c -> new RGBColor(c.getRed(), c.getRed(), c.getRed()));
  }

  public Image asGreenGrayscale() {
    return this.transformed(c -> new RGBColor(c.getGreen(), c.getGreen(), c.getGreen()));
  }

  public Image asBlueGrayscale() {
    return this.transformed(c -> new RGBColor(c.getBlue(), c.getBlue(), c.getBlue()));
  }

  public Image asValueGrayscale() {
    return this.transformed(c -> new RGBColor(c.getValue(), c.getValue(), c.getValue()));
  }

  public Image asIntensityGrayscale() {
    return this.transformed(c -> new RGBColor(c.getIntensity(), c.getIntensity(), c.getIntensity()));
  }

  public Image asLumaGrayscale() {
    return this.transformed(c -> new RGBColor(c.getLuma(), c.getLuma(), c.getLuma()));
  }

  public Image flipHoriz() {
    RGBColor[][] newPane = new RGBColor[height][width];
    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        newPane[row][width - col - 1] = this.pane[row][col];
      }
    }
    return new Image(newPane);
  }

  public Image flipVert() {
    RGBColor[][] newPane = new RGBColor[height][width];
    for (int row = 0; row < height; row += 1) {
      for (int col = 0; col < width; col += 1) {
        newPane[height - row - 1][col] = this.pane[row][col];
      }
    }
    return new Image(newPane);
  }
}