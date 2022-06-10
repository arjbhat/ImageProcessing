import org.junit.Before;
import org.junit.Test;

import model.Image;
import model.ImageState;
import model.ImageTransform;
import model.ObservableImage;

import static org.junit.Assert.*;

public class ObservableImageTest extends TestHelper {

  @Test
  public void testNullInput() {
    try {
      observableImage = new ObservableImage(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Image cannot be null.", e.getMessage());
    }
  }

  @Before
  public void initObsImg() {
    model.createImage(img1arr, "obsTest", 127);
    obsInput = model.getImage("obsTest");
  }

  @Test
  public void castingFailure() {
    assertFalse(obsInput instanceof ImageTransform);
    try {
      ImageTransform t = (ImageTransform) obsInput;
      fail("We were able to cast it down and do bad stuff.");
    } catch (ClassCastException e) {
      // Nothing happens! We couldn't do it!
    }
  }

  @Test
  public void getMaxValue() {
    assertEquals(127, obsInput.getMaxValue());
  }

  @Test
  public void getHeight() {
    assertEquals(3, obsInput.getHeight());
  }

  @Test
  public void getWidth() {
    assertEquals(2, obsInput.getWidth());
  }

  @Test
  public void getColorAt() {
    assertEquals(img1, this.imageFromState(obsInput));
  }
}