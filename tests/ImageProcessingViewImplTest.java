import org.junit.Test;

import java.io.IOException;

import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests for the image processing view implementation.
 */
public class ImageProcessingViewImplTest extends TestHelper {

  /**
   * Testing Constructor Exceptions for the Controller.
   */
  @Test
  public void testNullExceptionsForView() {
    try {
      newView1 = new ImageProcessingViewImpl(null);
      fail("Reason: View didn't throw an exception - even when it should");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid appendable of type null.", e.getMessage());
    }
  }

  /**
   * Test if IOException thrown when the appendable is known to be broken.
   * We make a broken appendable that always errors, provide it to the view and then try to
   * transmit to it.
   */
  @Test
  public void testAppendableFailing() {
    newView1 = new ImageProcessingViewImpl(new BrokenAppendable());

    try {
      newView1.renderMessage("Test");
    } catch (IOException e) {
      assertEquals("Unable to successfully transmit output", e.getMessage());
    }
  }

  /**
   * Test if we can render messages.
   */
  @Test
  public void renderMessage() {
    assertNotNull(newView1);
    this.tryToSendMessage("Hello", newView1, "Hello");
    this.tryToSendMessage(" my", newView1, "Hello my");
    this.tryToSendMessage(" name", newView1, "Hello my name");
    this.tryToSendMessage(" is", newView1, "Hello my name is");
    this.tryToSendMessage(" Arjun.", newView1, "Hello my name is Arjun.");
  }

  private void tryToSendMessage(String str, ImageProcessingView view, String expected) {
    try {
      newView1.renderMessage(str);
      assertEquals(expected, print1.toString());
    } catch (IOException e) {
      assertEquals("Unable to successfully transmit output", e.getMessage());
    }
  }
}
