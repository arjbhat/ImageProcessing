import org.junit.Before;

import java.io.StringReader;
import java.util.function.Function;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.Color;
import model.Image;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import model.ImageState;
import model.RGBColor;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

public abstract class TestHelper {
  protected Image img0;
  protected Color[][] img1arr;
  protected Image img1;
  protected Color[][] img2arr;
  protected Image img2;
  protected Color c1;
  protected Color c2;
  protected Color c3;
  protected Color c4;
  protected Color c5;
  protected Color c6;
  protected ImageProcessingModel model;
  protected Image testImage;
  protected StringBuilder input1;
  protected StringReader readInput1;
  protected StringBuilder print1;
  protected ImageProcessingController controller1;
  protected ImageProcessingView newView1;

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
    img1 = new Image(img1arr, 127);
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

  protected Image imageAsComponent(Image img, Function<Color, Integer> component) {
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

  protected Image imageBrightness(Image img, int num) {
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

  protected Image imageHorizontal(Image img) {
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

  protected Image imageVertical(Image img) {
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

  protected Image imageFromState(ImageState s) {
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
}
