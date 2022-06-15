import org.junit.Test;

import java.util.ArrayList;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.Color;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import model.macros.Brighten;
import model.macros.Component;
import model.macros.HorizontalFlip;
import model.macros.Macro;
import model.macros.VerticalFlip;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the controller implementation.
 */
public class ImageProcessingControllerImplTest extends AbstractControllerTest {

  protected ImageProcessingController makeController(ImageProcessingModel model,
                                                     ImageProcessingView view,
                                                     Readable input) {
    return new ImageProcessingControllerImpl(model, view, input);
  }

  // Testing user input to Controller

  /**
   * Try pressing different keys in different orders along with invalids in order to test if
   * the keypress works as is intended.
   */
  @Test
  public void testControllerToModel() {
    UserIO[] interactions = new UserIO[]{
            inputs("load res/img1.ppm img"),
            prints("method: createImage image-name: img maxValue: 255"),
            inputs("red-component img red-img"),
            prints("method: runCommand image-name: img dest-image-name: red-img"),
            inputs("green-component img green-img"),
            prints("method: runCommand image-name: img dest-image-name: green-img"),
            inputs("blue-component img blue-img"),
            prints("method: runCommand image-name: img dest-image-name: blue-img"),
            inputs("value-component img value-img"),
            prints("method: runCommand image-name: img dest-image-name: value-img"),
            inputs("luma-component img luma-img"),
            prints("method: runCommand image-name: img dest-image-name: luma-img"),
            inputs("intensity-component img intensity-img"),
            prints("method: runCommand image-name: img dest-image-name: intensity-img"),
            inputs("horizontal-flip img horizontal-img"),
            prints("method: runCommand image-name: img dest-image-name: horizontal-img"),
            inputs("vertical-flip img vertical-img"),
            prints("method: runCommand image-name: img dest-image-name: vertical-img"),
            inputs("brighten 10 img brighten-img"),
            prints("method: runCommand image-name: img dest-image-name: brighten-img"),
            inputs("brighten -11 img brighten-img"),
            prints("method: runCommand image-name: img dest-image-name: brighten-img"),
            inputs("save res/out.ppm img"),
            prints("method: getImage image-name: img"),
            inputs("q")
    };

    StringBuilder modelLog = new StringBuilder();
    ArrayList<Macro> macroLog = new ArrayList<>();
    ImageProcessingModel model = new MockModel(modelLog, macroLog);
    ImageProcessingView view = new ImageProcessingViewImpl(new StringBuilder());

    String expected = runController(model, view, interactions);

    assertEquals(expected, modelLog.toString());
    assertTrue(macroLog.get(0) instanceof Component);
    assertEquals(this.imageAsComponent(img1, Color::getRed),
            macroLog.get(0).execute(img1));
    assertTrue(macroLog.get(1) instanceof Component);
    assertEquals(this.imageAsComponent(img1, Color::getGreen),
            macroLog.get(1).execute(img1));
    assertTrue(macroLog.get(2) instanceof Component);
    assertEquals(this.imageAsComponent(img1, Color::getBlue),
            macroLog.get(2).execute(img1));
    assertTrue(macroLog.get(3) instanceof Component);
    assertEquals(this.imageAsComponent(img1, Color::getValue),
            macroLog.get(3).execute(img1));
    assertTrue(macroLog.get(4) instanceof Component);
    assertEquals(this.imageAsComponent(img1, Color::getLuma),
            macroLog.get(4).execute(img1));
    assertTrue(macroLog.get(5) instanceof Component);
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
            + "255\n"
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
            inputs("brighten 10 test bright"),
            prints("Brightness changed image created."),
            prints("Type instruction:"),
            inputs("horizontal-flip test horiz"),
            prints("Horizontally flipped image created."),
            prints("Type instruction:"),
            inputs("vertical-flip test vert"),
            prints("Vertically flipped image created."),
            prints("Type instruction:"),
            inputs("save res/out1.ppm test"),
            prints("File is saved."),
            prints("Type instruction:"),
            inputs("save res/out2.ppm bright"),
            prints("File is saved."),
            prints("Type instruction:"),
            inputs("save res/out3.ppm horiz"),
            prints("File is saved."),
            prints("Type instruction:"),
            inputs("save res/out4.ppm vert"),
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
            + "255\n"
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
    testFile("res/out2.ppm", "P3\n"
            + "2 3\n"
            + "255\n"
            + "10\n"
            + "10\n"
            + "10\n"
            + "110\n"
            + "60\n"
            + "35\n"
            + "60\n"
            + "110\n"
            + "35\n"
            + "60\n"
            + "35\n"
            + "110\n"
            + "35\n"
            + "60\n"
            + "110\n"
            + "110\n"
            + "110\n"
            + "110\n");
    testFile("res/out3.ppm", "P3\n"
            + "2 3\n"
            + "255\n"
            + "100\n"
            + "50\n"
            + "25\n"
            + "0\n"
            + "0\n"
            + "0\n"
            + "50\n"
            + "25\n"
            + "100\n"
            + "50\n"
            + "100\n"
            + "25\n"
            + "100\n"
            + "100\n"
            + "100\n"
            + "25\n"
            + "50\n"
            + "100\n");
    testFile("res/out4.ppm", "P3\n"
            + "2 3\n"
            + "255\n"
            + "25\n"
            + "50\n"
            + "100\n"
            + "100\n"
            + "100\n"
            + "100\n"
            + "50\n"
            + "100\n"
            + "25\n"
            + "50\n"
            + "25\n"
            + "100\n"
            + "0\n"
            + "0\n"
            + "0\n"
            + "100\n"
            + "50\n"
            + "25\n");
  }

  @Test
  public void testBadInputs() {
    UserIO[] interactions = new UserIO[]{
            prints(this.welcomeMessage()),
            inputs(""),
            prints("Type instruction:"),
            prints("Unknown command, please try again. (╥﹏╥)"),
            prints("Type instruction:"),
            inputs("value-component"),
            prints("Command failed: not enough arguments provided"),
            prints("Type instruction:"),
            inputs("brighten quit"),
            prints("Command failed: Invalid input type instead of integer."),
            prints("Type instruction:"),
            inputs("brighten 10"),
            prints("Command failed: not enough arguments provided"),
            prints("Type instruction:"),
            inputs("brighten 10 jeffry matthew"),
            prints("Command failed: Unknown image."),
            prints("Type instruction:"),
            inputs("load res/donotcreate res"),
            prints("Command failed: Failed to load file."),
            prints("Type instruction:"),
            inputs("load tests/ColorTest.java test"),
            prints("Command failed: Invalid file format."),
            prints("Type instruction:"),
            inputs("quit"),
            prints(this.farewellMessage())
    };

    StringBuilder outputLog = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl();
    ImageProcessingView view = new ImageProcessingViewImpl(outputLog);

    String expected = this.runController(model, view, interactions);
    assertEquals(expected, outputLog.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNoMoreInputs() {
    this.runController(new MockModel(new StringBuilder(), new ArrayList<>()),
            new MockView(new StringBuilder()));
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
}
