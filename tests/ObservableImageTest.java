import org.junit.Before;
import org.junit.Test;

import model.ImageTransform;
import model.ObservableImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Tests for an observable image.
 */
public class ObservableImageTest extends TestHelper {
  @Before
  public void initObsImg() {
    model.createImage(img1arr, "obsTest", 255);
    obsInput = model.getImage("obsTest");
    observableImage = new ObservableImage(img1);
  }

  @Test
  public void testNullInput() {
    try {
      observableImage = new ObservableImage(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Image cannot be null.", e.getMessage());
    }
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
    assertEquals(255, obsInput.getMaxValue());
    assertEquals(255, observableImage.getMaxValue());
  }

  @Test
  public void getHeight() {
    assertEquals(3, obsInput.getHeight());
    assertEquals(3, observableImage.getHeight());
  }

  @Test
  public void getWidth() {
    assertEquals(2, obsInput.getWidth());
    assertEquals(2, observableImage.getWidth());
  }

  @Test
  public void getColorAt() {
    assertEquals(img1, this.imageFromState(obsInput));
    assertEquals(img1, this.imageFromState(observableImage));
  }
}
