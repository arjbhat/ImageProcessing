import org.junit.Test;

import java.util.function.Function;

import model.Color;
import model.Image;
import model.ImageState;
import model.macros.Brighten;
import model.macros.Grayscale;
import model.macros.HorizontalFlip;
import model.macros.Macro;
import model.macros.VerticalFlip;

import static org.junit.Assert.*;

public class ImageProcessingModelImplTest extends TestHelper {

  @Test
  public void createImage() {
    // We added the image we had to the model.
    model.createImage(img1arr, "twoByThree", 127);
    ImageState twoByThreeState = model.getImage("twoByThree");
    // Let's create an array with the image with received
    Image twoByThree = this.imageFromState(twoByThreeState);
    assertNotEquals(img2, twoByThree);
    assertEquals(img1, twoByThree);

    // We added the image2 we had to the model.
    model.createImage(img2arr, "threeByTwo", 255);
    ImageState threeByTwoState = model.getImage("threeByTwo");
    // Let's create an array with the image with received
    Image threeByTwo = this.imageFromState(threeByTwoState);
    assertNotEquals(img1, threeByTwo);
    assertEquals(img2, threeByTwo);

    // Let's see if we can override images (override with img1)
    model.createImage(img1arr, "threeByTwo", 127);
    ImageState newThreeByTwoState = model.getImage("threeByTwo");
    // Let's create an array with the image with received
    Image newThreeByTwo = this.imageFromState(newThreeByTwoState);
    assertEquals(img1, newThreeByTwo);
    assertNotEquals(img2, newThreeByTwo);
    // And we successfully overrode the old file name :)!
  }

  @Test
  public void createImageExceptions() {
    this.createImageException(null, "arjun", 255,
        "Color array cannot be null.");
    this.createImageException(img1arr, "twoByThree", 10,
        "Row: 0 Col: 1 has a color value larger than channel size.");
    this.createImageException(img1arr, null, 255,
        "String name cannot be null.");
  }

  private void createImageException(Color[][] arr, String name, int maxValue, String exe) {
    try {
      model.createImage(arr, name, maxValue);
      fail("Invalid image created.");
    } catch (IllegalArgumentException e) {
      assertEquals(exe, e.getMessage());
    }
  }

  @Test
  public void runCommand() {
    // Let's load images first
    model.createImage(img1arr, "twoByThree", 127);
    model.createImage(img2arr, "threeByTwo", 255);

    // and let's save these images that we loaded
    Image twoByThree = this.imageFromState(model.getImage("twoByThree"));
    Image threeByTwo = this.imageFromState(model.getImage("threeByTwo"));

    // Time to test Macros!

    // On Image 1:
    // Macro 1: RedGrayscale
    this.testGrayscaleCommand("twoByThree", "redTwoByThree", img1, Color::getRed);
    // Macro 2: GreenGrayscale
    this.testGrayscaleCommand("twoByThree", "greenTwoByThree", img1, Color::getGreen);
    // Macro 3: BlueGrayscale
    this.testGrayscaleCommand("twoByThree", "blueTwoByThree", img1, Color::getBlue);
    // Macro 4: LumaGrayscale
    this.testGrayscaleCommand("twoByThree", "lumaTwoByThree", img1, Color::getLuma);
    // Macro 5: ValueGrayscale
    this.testGrayscaleCommand("twoByThree", "valueTwoByThree", img1, Color::getValue);
    // Macro 6: IntensityGrayscale
    this.testGrayscaleCommand("twoByThree", "intensityTwoByThree", img1, Color::getIntensity);
    // Macro 7: Brighten
    this.testBrightnessCommand("twoByThree", "up10TwoByThree", img1, 10);
    this.testBrightnessCommand("twoByThree", "down10TwoByThree", img1, -10);
    this.testBrightnessCommand("twoByThree", "up1000TwoByThree", img1, 1000);
    this.testBrightnessCommand("twoByThree", "down1000TwoByThree", img1, -1000);
    // Macro 8: Horizontal Flip
    this.testHorizontalCommand("twoByThree", "horizontalFlipTwoByThree", img1);
    // Macro 9: Vertical Flip
    this.testVerticalCommand("twoByThree", "verticalFlipTwoByThree", img1);

    // On Image 2:
    // Macro 1: RedGrayscale
    this.testGrayscaleCommand("threeByTwo", "redThreeByTwo", img2, Color::getRed);
    // Macro 2: GreenGrayscale
    this.testGrayscaleCommand("threeByTwo", "greenThreeByTwo", img2, Color::getGreen);
    // Macro 3: BlueGrayscale
    this.testGrayscaleCommand("threeByTwo", "blueThreeByTwo", img2, Color::getBlue);
    // Macro 4: LumaGrayscale
    this.testGrayscaleCommand("threeByTwo", "lumaThreeByTwo", img2, Color::getLuma);
    // Macro 5: ValueGrayscale
    this.testGrayscaleCommand("threeByTwo", "valueThreeByTwo", img2, Color::getValue);
    // Macro 6: IntensityGrayscale
    this.testGrayscaleCommand("threeByTwo", "intensityThreeByTwo", img2, Color::getIntensity);
    // Macro 7: Brighten
    this.testBrightnessCommand("threeByTwo", "up10ThreeByTwo", img2, 10);
    this.testBrightnessCommand("threeByTwo", "down10ThreeByTwo", img2, -10);
    this.testBrightnessCommand("threeByTwo", "up1000ThreeByTwo", img2, 1000);
    this.testBrightnessCommand("threeByTwo", "down1000ThreeByTwo", img2, -1000);
    // Macro 8: Horizontal Flip
    this.testHorizontalCommand("threeByTwo", "horizontalFlipThreeByTwo", img2);
    // Macro 9: Vertical Flip
    this.testVerticalCommand("threeByTwo", "verticalFlipThreeByTwo", img2);
  }

  private void testGrayscaleCommand(String oldName, String newName, Image expected,
                                    Function<Color, Integer> func) {
    this.testMacro(oldName, newName, new Grayscale(func), this.imageAsComponent(expected, func));
  }

  public void testBrightnessCommand(String oldName, String newName, Image expected, int n) {
    this.testMacro(oldName, newName, new Brighten(n), this.imageBrightness(expected, n));
  }

  public void testHorizontalCommand(String oldName, String newName, Image expected) {
    this.testMacro(oldName, newName, new HorizontalFlip(), this.imageHorizontal(expected));
  }

  public void testVerticalCommand(String oldName, String newName, Image expected) {
    this.testMacro(oldName, newName, new VerticalFlip(), this.imageVertical(expected));
  }

  private void testMacro(String oldName, String newName, Macro macro, Image expected) {
    Image oldImg = this.imageFromState(model.getImage(oldName));
    model.runCommand(macro, oldName, newName);
    Image newImg = this.imageFromState(model.getImage(newName));
    // Proof that there was no mutation on the original image
    assertNotEquals(oldImg, newImg);
    // But that the image new image produced is the one that we expect
    assertEquals(expected, newImg);
  }

  @Test
  public void testRunCommand() {
    model.createImage(img1arr, "twoByThree", 127);
    StringBuilder out = new StringBuilder();
    model.runCommand(img -> {
      out.append("called once");
      return img;
    }, "twoByThree", "changedImage");
    assertEquals("called once", out.toString());
  }

  @Test
  public void runCommandExceptions() {
    model.createImage(img1arr, "twoByThree", 127);
    this.runCommandException(null, "twoByThree", "threeByTwo",
        "Macro cannot be null.");
    this.runCommandException(new Brighten(5), null, "brighterBy5",
        "String target cannot be null.");
    this.runCommandException(new Brighten(5), "twoByThree", null,
        "New String name cannot be null.");
  }

  private void runCommandException(Macro command, String target, String newName, String exe) {
    try {
      model.runCommand(command, target, newName);
      fail("Invalid command run as valid.");
    } catch (IllegalArgumentException e) {
      assertEquals(exe, e.getMessage());
    }
  }

  @Test
  public void getImage() {
    // Let's load images first
    model.createImage(img1arr, "twoByThree", 127);
    model.createImage(img2arr, "threeByTwo", 255);

    // and let's save these images that we loaded
    Image twoByThree = this.imageFromState(model.getImage("twoByThree"));
    Image threeByTwo = this.imageFromState(model.getImage("threeByTwo"));

    // to see that they're what we expected
    assertEquals(img1, twoByThree);
    assertEquals(img2, threeByTwo);
    assertNotEquals(img2, twoByThree);
    assertNotEquals(img1, threeByTwo);
  }

  @Test
  public void testGetImageException() {
    try {
      model.getImage(null);
      fail("Invalid command run as valid.");
    } catch (IllegalArgumentException e) {
      assertEquals("Unknown image.", e.getMessage());
    }
  }
}