import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.ImageProcessingModel;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractControllerTest extends TestHelper {
  protected static UserIO inputs(String in) {
    return (input, output) -> {
      input.append(in).append('\n');
    };
  }

  protected static UserIO prints(String... lines) {
    return (input, output) -> {
      for (String line : lines) {
        output.append(line).append('\n');
      }
    };
  }

  protected abstract ImageProcessingController makeController(ImageProcessingModel model,
                                                              ImageProcessingView view,
                                                              Readable input);

  /**
   * Testing Constructor Exceptions for the Controller.
   */
  @Test
  public void testNullExceptionsForController() {
    try {
      controller1 = this.makeController(null, newView1, readInput1);
      fail("Reason: Constructor didn't throw an exception - even when it should");
    } catch (IllegalArgumentException e) {
      assertEquals("Model cannot be null.", e.getMessage());
    }

    try {
      controller1 = this.makeController(model, null, readInput1);
      fail("Reason: Constructor didn't throw an exception - even when it should");
    } catch (IllegalArgumentException e) {
      assertEquals("View cannot be null.", e.getMessage());
    }

    try {
      controller1 = this.makeController(model, newView1, null);
      fail("Reason: Constructor didn't throw an exception - even when it should");
    } catch (IllegalArgumentException e) {
      assertEquals("Readable cannot be null.", e.getMessage());
    }
  }

  /**
   * Test if IllegalStateException thrown instead of an IOException when the appendable errors.
   * We make a broken appendable that always errors, provide it to the view and then try to transmit
   * to it.
   */
  @Test
  public void testAppendableFailing() {
    newView1 = new ImageProcessingViewImpl(new BrokenAppendable());
    controller1 = this.makeController(model, newView1, readInput1);

    try {
      controller1.control();
      fail("Reason: Can't transmit output to a broken appendable");
    } catch (IllegalStateException e) {
      assertEquals("Unable to successfully transmit output", e.getMessage());
    }
  }

  /**
   * Test if IllegalStateException thrown instead of an IOException when the readable errors.
   */
  @Test
  public void testReadableFailing() {
    Readable read = new BrokenReadable();
    controller1 = this.makeController(model, newView1, read);

    try {
      controller1.control();
      fail("Reason: Can't read readable");
    } catch (IllegalStateException e) {
      assertEquals("Unable to successfully receive input.", e.getMessage());
    }
  }

  protected String runController(ImageProcessingModel model, ImageProcessingView view,
                                 UserIO... interactions) {
    // Input is Input1
    StringBuilder expected = new StringBuilder(); // What we expect log to look like
    StringBuilder inputs = new StringBuilder(); // What we input

    // Interactions went here...

    for (UserIO i : interactions) {
      i.apply(inputs, expected);
    }

    this.makeController(model, view, new StringReader(inputs.toString())).control();

    return expected.toString();
  }

  protected void testFile(String path, String contents) {
    File file = new File(path);
    file.deleteOnExit();
    assertTrue(file.exists());
    assertTrue(file.isFile());
    try (FileReader read = new FileReader(path)) {
      Scanner sc = new Scanner(read);
      StringBuilder fileContents = new StringBuilder();
      while (sc.hasNextLine()) {
        fileContents.append(sc.nextLine()).append(System.lineSeparator());
      }
      assertEquals(contents, fileContents.toString());
    } catch (IOException e) {
      fail("Can't find file");
    }
  }

  protected interface UserIO {
    void apply(StringBuilder sb1, StringBuilder sb2);
  }
}
