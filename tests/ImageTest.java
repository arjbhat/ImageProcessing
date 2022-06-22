import org.junit.Test;

import model.Color;
import model.Image;
import model.ImageState;
import model.ImageTransform;
import model.RGBColor;
import model.macros.Component;
import model.macros.Convolve;
import model.macros.Macro;
import model.macros.MatrixTransform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Testing for Images.
 */
public class ImageTest extends TestHelper {

  @Test
  public void testNegativeMaxValue() {
    assertNotNull(img1arr);
    this.constructInvalidImage(img1arr, -1, "Invalid max value.");
    this.constructInvalidImage(img2arr, -5, "Invalid max value.");
  }

  @Test
  public void testNullArray() {
    img1 = null;
    this.constructInvalidImage(null, 255, "Color array cannot be null.");
    this.constructInvalidImage(null, 255, "Color array cannot be null.");
    assertNull(img1);
  }

  // Abstraction for constructing invalid images
  private void constructInvalidImage(Color[][] arr, int maxVal, String error) {
    try {
      img1 = new Image(arr, maxVal);
      fail("Shouldn't be able to construct this image, but it did.");
    } catch (IllegalArgumentException e) {
      assertEquals(error, e.getMessage());
    }
  }

  @Test
  public void getMaxValue() {
    assertEquals(1, img0.getMaxValue());
    assertEquals(255, img1.getMaxValue());
    assertEquals(255, img2.getMaxValue());
  }

  @Test
  public void getHeight() {
    assertEquals(0, img0.getHeight());
    assertEquals(3, img1.getHeight());
    assertEquals(2, img2.getHeight());
  }

  @Test
  public void getWidth() {
    assertEquals(0, img0.getWidth());
    assertEquals(2, img1.getWidth());
    assertEquals(3, img2.getWidth());
  }

  @Test
  public void getColorAt() {
    assertEquals(c1, img1.getColorAt(0, 0));
    assertEquals(c4, img1.getColorAt(1, 1));
    assertEquals(c6, img1.getColorAt(2, 1));

    assertEquals(c1, img2.getColorAt(0, 0));
    assertEquals(c4, img2.getColorAt(1, 0));
    assertEquals(c6, img2.getColorAt(1, 2));
  }

  @Test
  public void getInvalidColor() {
    assertNotNull(img0);
    this.getInvalidColor(img0, 0, 0);
    this.getInvalidColor(img0, 1, 1);
    this.getInvalidColor(img0, -1, 0);
    this.getInvalidColor(img0, 0, -1);

    assertNotNull(img1);
    this.getInvalidColor(img1, 0, 2);
    this.getInvalidColor(img1, 3, 0);
    this.getInvalidColor(img1, 3, 2);
    this.getInvalidColor(img1, -1, 0);
    this.getInvalidColor(img1, 0, -1);

    assertNotNull(img2);
    this.getInvalidColor(img2, 0, 3);
    this.getInvalidColor(img2, 2, 0);
    this.getInvalidColor(img2, 2, 3);
    this.getInvalidColor(img2, -1, 0);
    this.getInvalidColor(img2, 0, -1);
  }

  // Abstraction for getting invalid color from image
  private void getInvalidColor(ImageState img, int row, int col) {
    try {
      Color color = img.getColorAt(row, col);
      fail("We should not be able to get this color.");
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot get color from invalid location.", e.getMessage());
    }
  }

  /**
   * Testing if transform really transforms the image.
   */
  @Test
  public void transform() {
    // Let's first try a null function
    try {
      ImageTransform nullImage = img0.transform(null);
      fail("We operated with a null???");
    } catch (IllegalArgumentException e) {
      assertEquals("Function cannot be null.", e.getMessage());
    }

    // Trying a grayscale function
    ImageState img1Value = this.imageAsComponent(img1, Color::getValue);

    ImageTransform newValueImg1 = img1.transform((c, y, x)
        -> new RGBColor(c.getValue(), c.getValue(), c.getValue()));

    // new image is the same as the one we created
    assertEquals(img1Value, newValueImg1);

    // image is not mutated
    assertNotEquals(img1Value, img1);

    // Trying a flip function
    ImageState img1HorFlip = this.imageHorizontal(img1);

    ImageState newHorFlipImg1 = img1.transform((c, y, x)
        -> img1.getColorAt(y, img1.getWidth() - x - 1));

    // new image is the same as the one we created
    assertEquals(img1HorFlip, newHorFlipImg1);

    // image is not mutated
    assertNotEquals(img1HorFlip, img1);

    ImageState valueTooHigh = img1.transform((c, y, x)
        -> new RGBColor(1000, 1000, 1000));
    ImageState valueTooLow = img1.transform((c, y, x)
        -> new RGBColor(-1000, -1000, -1000));

    c1 = new RGBColor(255, 255, 255);
    img1arr = new Color[][]{{c1, c1}, {c1, c1}, {c1, c1}};
    ImageState expectedImage1 = new Image(img1arr, 255);
    assertEquals(expectedImage1, valueTooHigh);

    c2 = new RGBColor(0, 0, 0);
    img2arr = new Color[][]{{c2, c2}, {c2, c2}, {c2, c2}};
    ImageState expectedImage2 = new Image(img2arr, 255);
    assertEquals(expectedImage2, valueTooLow);
  }

  @Test
  public void macroExceptions() {
    // Component
    try {
      Macro component = new Component(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Get channel function cannot be null.", e.getMessage());
    }

    // Convolve
    // null kernel
    try {
      Macro convolve = new Convolve(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Kernel cannot be null.", e.getMessage());
    }
    // even kernel size
    try {
      Macro convolve = new Convolve(new double[][]{{}, {}});
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid Kernel size.", e.getMessage());
    }
    // null in kernel row
    try {
      Macro convolve = new Convolve(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, null});
    } catch (IllegalArgumentException e) {
      assertEquals("Kernel row cannot be null.", e.getMessage());
    }
    // kernel not a square
    try {
      Macro convolve = new Convolve(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}});
    } catch (IllegalArgumentException e) {
      assertEquals("Kernel must be square.", e.getMessage());
    }

    // Matrix Transform
    // null matrix
    try {
      Macro matrixTransform = new MatrixTransform(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Matrix cannot be null.", e.getMessage());
    }
    // matrix height 2
    try {
      Macro matrixTransform = new MatrixTransform(new double[][]{{}, {}});
    } catch (IllegalArgumentException e) {
      assertEquals("Matrix height must be 3.", e.getMessage());
    }
    // matrix height 4
    try {
      Macro matrixTransform = new MatrixTransform(new double[][]{{}, {}, {}, {}});
    } catch (IllegalArgumentException e) {
      assertEquals("Matrix height must be 3.", e.getMessage());
    }
    // null in matrix row
    try {
      Macro matrixTransform = new MatrixTransform(new double[][]{
          {0.0, 0.0, 0.0},
          {0.0, 0.0, 0.0},
          null
      });
    } catch (IllegalArgumentException e) {
      assertEquals("Matrix row cannot be null.", e.getMessage());
    }
    // matrix width 2
    try {
      Macro matrixTransform = new MatrixTransform(new double[][]{
          {0.0, 0.0, 0.0},
          {0.0, 0.0},
          {0.0, 0.0, 0.0}
      });
    } catch (IllegalArgumentException e) {
      assertEquals("Matrix width must be 3.", e.getMessage());
    }

  }


  @Test
  public void testImageEquals() {
    assertEquals(img1, img1);
    assertEquals(img1, new Image(img1arr, 255));
    assertEquals(img2, img2);
    assertEquals(img2, new Image(img2arr, 255));
    assertNotEquals(img1, img2);
    assertNotEquals(img1, new Image(img1arr, 128));
  }

  @Test
  public void testImageHashcode() {
    assertEquals(img1.hashCode(), img1.hashCode());
    assertEquals(img1.hashCode(), new Image(img1arr, 255).hashCode());
    assertEquals(img2.hashCode(), img2.hashCode());
    assertEquals(img2.hashCode(), new Image(img2arr, 255).hashCode());
    assertEquals(-494666157, img1.hashCode());
    assertEquals(-559592278, img2.hashCode());
  }
}
