import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

import controller.ImageProcessingControllerImpl;
import model.Color;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import model.macros.Brighten;
import model.macros.Grayscale;
import model.macros.HorizontalFlip;
import model.macros.Macro;
import model.macros.VerticalFlip;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

import static org.junit.Assert.*;

public class ImageProcessingControllerImplTest extends TestHelper {
  private static UserIO inputs(String in) {
    return (input, output) -> {
      input.append(in).append('\n');
    };
  }

  private static UserIO prints(String... lines) {
    return (input, output) -> {
      for (String line : lines) {
        output.append(line).append('\n');
      }
    };
  }

  /**
   * Testing Constructor Exceptions for the Controller.
   */
  @Test
  public void testNullExceptionsForController() {
    try {
      controller1 = new ImageProcessingControllerImpl(null, newView1, readInput1);
      fail("Reason: Constructor didn't throw an exception - even when it should");
    } catch (IllegalArgumentException e) {
      assertEquals("Model cannot be null.", e.getMessage());
    }

    try {
      controller1 = new ImageProcessingControllerImpl(model, null, readInput1);
      fail("Reason: Constructor didn't throw an exception - even when it should");
    } catch (IllegalArgumentException e) {
      assertEquals("View cannot be null.", e.getMessage());
    }

    try {
      controller1 = new ImageProcessingControllerImpl(model, newView1, null);
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
    controller1 = new ImageProcessingControllerImpl(model, newView1, readInput1);

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
    controller1 = new ImageProcessingControllerImpl(model, newView1, read);

    try {
      controller1.control();
      fail("Reason: Can't read readable");
    } catch (IllegalStateException e) {
      assertEquals("Unable to successfully receive input.", e.getMessage());
    }
  }

  // Testing user input to Controller

  /**
   * Try pressing different keys in different orders along with invalids in order to test if
   * the keypress works as is intended.
   */
  @Test
  public void testControllerToModel() {
    UserIO[] interactions = new UserIO[]{
        inputs("load res/arjun.ppm arj"),
        prints("method: createImage image-name: arj maxValue: 255"),
        inputs("red-component arj red-arj"),
        prints("method: runCommand image-name: arj dest-image-name: red-arj"),
        inputs("green-component arj green-arj"),
        prints("method: runCommand image-name: arj dest-image-name: green-arj"),
        inputs("blue-component arj blue-arj"),
        prints("method: runCommand image-name: arj dest-image-name: blue-arj"),
        inputs("value-component arj value-arj"),
        prints("method: runCommand image-name: arj dest-image-name: value-arj"),
        inputs("luma-component arj luma-arj"),
        prints("method: runCommand image-name: arj dest-image-name: luma-arj"),
        inputs("intensity-component arj intensity-arj"),
        prints("method: runCommand image-name: arj dest-image-name: intensity-arj"),
        inputs("horizontal-flip arj horizontal-arj"),
        prints("method: runCommand image-name: arj dest-image-name: horizontal-arj"),
        inputs("vertical-flip arj vertical-arj"),
        prints("method: runCommand image-name: arj dest-image-name: vertical-arj"),
        inputs("brighten 10 arj brighten-arj"),
        prints("method: runCommand image-name: arj dest-image-name: brighten-arj"),
        inputs("brighten -11 arj brighten-arj"),
        prints("method: runCommand image-name: arj dest-image-name: brighten-arj"),
        inputs("save res/out.ppm arj"),
        prints("method: getImage image-name: arj"),
        inputs("q")
    };

    StringBuilder modelLog = new StringBuilder();
    ArrayList<Macro> macroLog = new ArrayList<>();
    ImageProcessingModel model = new MockModel(modelLog, macroLog);
    ImageProcessingView view = new ImageProcessingViewImpl(new StringBuilder());

    String expected = runController(model, view, interactions);

    assertEquals(expected, modelLog.toString());
    assertTrue(macroLog.get(0) instanceof Grayscale);
    assertEquals(this.imageAsComponent(img1, Color::getRed),
        macroLog.get(0).execute(img1));
    assertTrue(macroLog.get(1) instanceof Grayscale);
    assertEquals(this.imageAsComponent(img1, Color::getGreen),
        macroLog.get(1).execute(img1));
    assertTrue(macroLog.get(2) instanceof Grayscale);
    assertEquals(this.imageAsComponent(img1, Color::getBlue),
        macroLog.get(2).execute(img1));
    assertTrue(macroLog.get(3) instanceof Grayscale);
    assertEquals(this.imageAsComponent(img1, Color::getValue),
        macroLog.get(3).execute(img1));
    assertTrue(macroLog.get(4) instanceof Grayscale);
    assertEquals(this.imageAsComponent(img1, Color::getLuma),
        macroLog.get(4).execute(img1));
    assertTrue(macroLog.get(5) instanceof Grayscale);
    assertEquals(this.imageAsComponent(img1, Color::getIntensity),
        macroLog.get(5).execute(img1));
    assertTrue(macroLog.get(6) instanceof HorizontalFlip);
    assertEquals(this.imageHorizontal(img1),
        macroLog.get(6).execute(img1));
    assertTrue(macroLog.get(7) instanceof VerticalFlip);
    assertEquals(this.imageVertical(img1),
        macroLog.get(7).execute(img1));
    assertTrue(macroLog.get(8) instanceof Brighten);
    assertEquals(this.imageBrightness(img1, 10),
        macroLog.get(8).execute(img1));
    assertTrue(macroLog.get(9) instanceof Brighten);
    assertEquals(this.imageBrightness(img1, -11),
        macroLog.get(9).execute(img1));

    testFile("res/out.ppm", "P3\n" + "0 0\n" + "0\n");
  }

  @Test
  public void testControllerToView() {
    UserIO[] interactions = new UserIO[]{
        prints("method: renderMessage"), // welcome
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"), // type input
        inputs("menu"),
        prints("method: renderMessage"), // 9 menu options
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"), // type input
        inputs(""),
        prints("method: renderMessage"), // command failed
        prints("method: renderMessage"), // type input
        inputs("garble"),
        prints("method: renderMessage"), // command failed
        prints("method: renderMessage"), // type input
        inputs("garble wkefnkwenf awef we fw ef wef wef wf weff wef we"),
        prints("method: renderMessage"), // command failed
        prints("method: renderMessage"), // type input
        inputs("red-component a"),
        prints("method: renderMessage"), // command failed
        prints("method: renderMessage"), // type input
        inputs("red-component a b"),
        prints("method: renderMessage"), // command failed
        prints("method: renderMessage"), // type input
        inputs("brighten fe"),
        prints("method: renderMessage"), // command failed
        prints("method: renderMessage"), // type input
        inputs("brighten 50 fas"),
        prints("method: renderMessage"), // command failed
        prints("method: renderMessage"), // type input
        inputs("load res/img1.ppm arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("red-component arj red-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("green-component arj green-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("blue-component arj blue-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("value-component arj value-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("luma-component arj luma-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("intensity-component arj intensity-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("horizontal-flip arj horizontal-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("vertical-flip arj vertical-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("brighten 10 arj brighten-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("brighten -11 arj brighten-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("save res/out.ppm arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("q"),
        prints("method: renderMessage"), // thank you
    };
    StringBuilder viewLog = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl();
    ImageProcessingView view = new MockView(viewLog);

    String expected = runController(model, view, interactions);

    assertEquals(expected, viewLog.toString());
    testFile("res/out.ppm", "P3\n"
        + "2 3\n"
        + "127\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "100\n"
        + "50\n"
        + "25\n"
        + "50\n"
        + "100\n"
        + "25\n"
        + "50\n"
        + "25\n"
        + "100\n"
        + "25\n"
        + "50\n"
        + "100\n"
        + "100\n"
        + "100\n"
        + "100\n");
  }

  @Test
  public void testFileIO() {
    UserIO[] interactions = new UserIO[]{
        prints(this.welcomeMessage()),
        prints("Type instruction:"),
        inputs("load res/img1.ppm test"),
        prints("File is loaded."),
        prints("Type instruction:"),
        inputs("save res/out1.ppm test"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("q"),
        prints(this.farewellMessage())
    };

    StringBuilder outputLog = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl();
    ImageProcessingView view = new ImageProcessingViewImpl(outputLog);

    String expected = this.runController(model, view, interactions);
    assertEquals(expected, outputLog.toString());
    assertEquals(model.getImage("test"), this.img1);
    testFile("res/out1.ppm", "P3\n"
        + "2 3\n"
        + "127\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "100\n"
        + "50\n"
        + "25\n"
        + "50\n"
        + "100\n"
        + "25\n"
        + "50\n"
        + "25\n"
        + "100\n"
        + "25\n"
        + "50\n"
        + "100\n"
        + "100\n"
        + "100\n"
        + "100\n");
  }

  @Test
  public void testBadInputs() {
    UserIO[] interactions = new UserIO[]{
        prints(this.welcomeMessage()),
        prints("Type instruction:"),
        inputs("load res/img1.ppm test"),
        prints("File is loaded."),
        prints("Type instruction:"),
        inputs("save res/out1.ppm test"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("q"),
        prints(this.farewellMessage())
    };

    StringBuilder outputLog = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl();
    ImageProcessingView view = new ImageProcessingViewImpl(outputLog);

    String expected = this.runController(model, view, interactions);
    assertEquals(expected, outputLog.toString());
  }

  private void testFile(String path, String contents) {
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

  private String runController(ImageProcessingModel model, ImageProcessingView view,
                               UserIO... interactions) {
    // Input is Input1
    StringBuilder expected = new StringBuilder(); // What we expect log to look like
    StringBuilder inputs = new StringBuilder(); // What we input

    // Interactions went here...

    for (UserIO i : interactions) {
      i.apply(inputs, expected);
    }

    new ImageProcessingControllerImpl(model, view, new StringReader(inputs.toString())).control();

    return expected.toString();
  }

  private String[] welcomeMessage() {
    return new String[]{"✿ ✿ ✿ Welcome to the Image Processing program! ✿ ✿ ✿",
        "   By: Arjun Bhat & Alexander Chang-Davidson\n",
        "[Type menu to read support user instructions.]"};
  }

  private String[] printMenu() throws IllegalStateException {
    return new String[]{"Supported user instructions are: ",
        " ➤ load image-path image-name ",
        "(Loads an image from the specified path and refers to it henceforth in the program ",
        "by the given name)",
        " ➤ save image-path image-name ",
        "(Saves the image with the given name to the specified path which includes ",
        "the name of the file)",
        " ➤ (component name)-component image-name dest-image-name ",
        "(Create a greyscale image with the (component name) component of the image with ",
        "the given name.",
        " [supported (component name): red, green, blue, value, luma, intensity])",
        " ➤ horizontal-flip image-name dest-image-name ",
        "(Flip an image horizontally to create a new image, ",
        "referred to henceforth by the given destination name)",
        " ➤ vertical-flip image-name dest-image-name ",
        "(Flip an image vertically to create a new image, ",
        "referred to henceforth by the given destination name)",
        " ➤ brighten increment image-name dest-image-name ",
        "(Brighten the image by the given increment to create a new image, referred to ",
        "henceforth by the given destination name - the increment may be positive ",
        "(brightening) or negative (darkening))",
        " ➤ menu (Print supported instruction list)",
        " ➤ q or quit (quit the program)"};
  }

  private String[] farewellMessage() {
    return new String[]{"Thank you for using this program! (っ◕‿◕)っ"};
  }

  private interface UserIO {
    void apply(StringBuilder sb1, StringBuilder sb2);
  }
}
