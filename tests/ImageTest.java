import org.junit.Before;
import org.junit.Test;

import model.Image;
import model.RGBColor;

import static org.junit.Assert.*;

public class ImageTest extends TestHelper {

  @Test
  public void testIndexLargerThanMaxValue() {
    this.constructInvalidImage(img1arr, 50,
            "Row: 0 Col: 1 has a color value larger than channel size.");
    this.constructInvalidImage(img2arr, 25,
            "Row: 0 Col: 1 has a color value larger than channel size.");
  }

  @Test
  public void testNegativeMaxValue() {
    this.constructInvalidImage(img1arr, -1, "Invalid max value.");
    this.constructInvalidImage(img2arr, -5, "Invalid max value.");
  }

  @Test
  public void testNullArray() {
    this.constructInvalidImage(null, 255, "RGBColor array cannot be null.");
    this.constructInvalidImage(null, 255, "RGBColor array cannot be null.");
  }

  // Abstraction for constructing invalid images
  private void constructInvalidImage(RGBColor[][] arr, int maxVal, String error) {
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
    assertEquals(127, img1.getMaxValue());
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
    this.getInvalidColor(img0, 0, 0);
    this.getInvalidColor(img0, 1, 1);
    this.getInvalidColor(img0, -1, 0);
    this.getInvalidColor(img0, 0, -1);

    this.getInvalidColor(img1, 0, 2);
    this.getInvalidColor(img1, 3, 0);
    this.getInvalidColor(img1, 3, 2);
    this.getInvalidColor(img1, -1, 0);
    this.getInvalidColor(img1, 0, -1);

    this.getInvalidColor(img2, 0, 3);
    this.getInvalidColor(img2, 2, 0);
    this.getInvalidColor(img2, 2, 3);
    this.getInvalidColor(img2, -1, 0);
    this.getInvalidColor(img2, 0, -1);
  }

  // Abstraction for getting invalid color from image
  private void getInvalidColor(Image img, int row, int col) {
    try {
      RGBColor color = img.getColorAt(row, col);
      fail("We should not be able to get this color.");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid location.", e.getMessage());
    }
  }

  /**
   * Testing if transform really transforms the image.
   */
  @Test
  public void transform() {
    // Let's first try a null function
    try {
      Image nullImage = img0.transform(null);
      fail("We operated with a null???");
    } catch (IllegalArgumentException e) {
      assertEquals("Function cannot be null.", e.getMessage());
    }

    // Trying a grayscale function
    Image img1Value = this.imageAsComponent(img1, RGBColor::getValue);

    Image newValueImg1 = img1.transform((c, y, x)
            -> new RGBColor(c.getValue(), c.getValue(), c.getValue()));

    // new image is the same as the one we created
    assertEquals(img1Value, newValueImg1);

    // image is not mutated
    assertNotEquals(img1Value, img1);

    // Trying a flip function
    Image img1HorFlip = this.imageHorizontal(img1);

    Image newHorFlipImg1 = img1.transform((c, y, x)
            -> img1.getColorAt(y, img1.getWidth() - x - 1));

    // new image is the same as the one we created
    assertEquals(img1HorFlip, newHorFlipImg1);

    // image is not mutated
    assertNotEquals(img1HorFlip, img1);

    // Now we try to change the brightness by a factor of 1000 without using the appropriate macro
    // and reduce it below 0 (color should throw this exception)
    try {
      Image valueTooHigh = img1.transform((c, y, x)
              -> new RGBColor(1000, 1000, 1000));
      fail("We were able to set to a value above max value");
    } catch (IllegalArgumentException e) {
      assertEquals("Color channel cannot be set above max channel value.",
              e.getMessage());
    }

    try {
      Image valueTooLow = img1.transform((c, y, x)
              -> new RGBColor(-1000, -1000, -1000));
      fail("We were able to set to a value below 0");
    } catch (IllegalArgumentException e) {
      assertEquals("Color channel cannot be below 0.",
              e.getMessage());
    }
  }

  @Test
  public void testImageEquals() {
    assertEquals(img1, img1);
    assertEquals(img1, new Image(img1arr, 127));
    assertEquals(img2, img2);
    assertEquals(img2, new Image(img2arr, 255));
    assertNotEquals(img1, img2);
    assertNotEquals(img1, new Image(img1arr, 128));
  }

  @Test
  public void testImageHashcode() {
    assertEquals(img1.hashCode(), img1.hashCode());
    assertEquals(img1.hashCode(), new Image(img1arr, 127).hashCode());
    assertEquals(img2.hashCode(), img2.hashCode());
    assertEquals(img2.hashCode(), new Image(img2arr, 255).hashCode());
    assertEquals(-502773005, img1.hashCode());
    assertEquals(-567695158, img2.hashCode());
  }
}