import org.junit.Test;

import java.util.function.Function;

import model.Color;
import model.Image;
import model.ImageState;
import model.ImageTransform;
import model.RGBColor;
import model.macros.Blur;
import model.macros.Brighten;
import model.macros.Component;
import model.macros.Downscale;
import model.macros.Greyscale;
import model.macros.HorizontalFlip;
import model.macros.Macro;
import model.macros.Mask;
import model.macros.Sepia;
import model.macros.Sharpen;
import model.macros.VerticalFlip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests for the image processing model implementation.
 */
public class ImageProcessingModelImplTest extends TestHelper {
  @Test
  public void createImage() {
    // We added the image we had to the model.
    model.createImage(img1arr, "twoByThree", 255);

    // Let's create an array with the image with received
    ImageState twoByThree = model.getImage("twoByThree");
    assertNotEquals(img2, twoByThree);
    assertEquals(img1, twoByThree);

    // We added the image2 we had to the model.
    model.createImage(img2arr, "threeByTwo", 255);

    // Let's create an array with the image with received
    ImageState threeByTwo = model.getImage("threeByTwo");
    assertNotEquals(img1, threeByTwo);
    assertEquals(img2, threeByTwo);

    // Let's see if we can override images (override with img1)
    model.createImage(img1arr, "threeByTwo", 255);

    // Let's create an array with the image with received
    ImageState newThreeByTwo = model.getImage("threeByTwo");
    assertEquals(img1, newThreeByTwo);
    assertNotEquals(img2, newThreeByTwo);
    // And we successfully overrode the old file name :)!
  }

  @Test
  public void createImageExceptions() {
    assertNotNull(model);
    this.createImageException(null, "arjun", "Color array cannot be null.");
    this.createImageException(img1arr, null, "String name cannot be null.");
  }

  // Setting the max value to 10, and seeing all colors be maxed at 10.
  @Test
  public void maxValueCap() {
    model.createImage(img1arr, "twoByThree", 50);
    ImageState cappedImage = model.getImage("twoByThree");
    c1 = new RGBColor(0, 0, 0);
    c2 = new RGBColor(50, 50, 25);
    c3 = new RGBColor(50, 50, 25);
    c4 = new RGBColor(50, 25, 50);
    c5 = new RGBColor(25, 50, 50);
    c6 = new RGBColor(50, 50, 50);
    img1arr = new Color[][]{{c1, c2}, {c3, c4}, {c5, c6}};
    ImageState expectedImage = new Image(img1arr, 50);
    assertEquals(expectedImage, cappedImage);
  }

  private void createImageException(Color[][] arr, String name, String exe) {
    try {
      model.createImage(arr, name, 255);
      fail("Invalid image created.");
    } catch (IllegalArgumentException e) {
      assertEquals(exe, e.getMessage());
    }
  }

  @Test
  public void runCommand() {
    // Let's load images first
    model.createImage(img1arr, "twoByThree", 255);
    model.createImage(img2arr, "threeByTwo", 255);
    model.createImage(img1MaskArr, "twoByThreeMask", 255);
    model.createImage(img2MaskArr, "threeByTwoMask", 255);

    // and let's save these images that we loaded
    assertNotNull(model.getImage("twoByThree"));
    assertNotNull(model.getImage("threeByTwo"));
    assertNotNull(model.getImage("twoByThreeMask"));
    assertNotNull(model.getImage("threeByTwoMask"));

    // Time to test Macros!

    // On Image 1:
    // Macro 1: RedGrayscale
    this.testGrayscaleCommand("twoByThree", "redTwoByThree",
        img1, Color::getRed);
    // Macro 2: GreenGrayscale
    this.testGrayscaleCommand("twoByThree", "greenTwoByThree",
        img1, Color::getGreen);
    // Macro 3: BlueGrayscale
    this.testGrayscaleCommand("twoByThree", "blueTwoByThree",
        img1, Color::getBlue);
    // Macro 4: LumaGrayscale
    this.testGrayscaleCommand("twoByThree", "lumaTwoByThree",
        img1, Color::getLuma);
    // Macro 5: ValueGrayscale
    this.testGrayscaleCommand("twoByThree", "valueTwoByThree",
        img1, Color::getValue);
    // Macro 6: IntensityGrayscale
    this.testGrayscaleCommand("twoByThree", "intensityTwoByThree",
        img1, Color::getIntensity);
    // Macro 7: Brighten
    this.testBrightnessCommand("twoByThree", "up10TwoByThree",
        img1, 10);
    this.testBrightnessCommand("twoByThree", "down10TwoByThree",
        img1, -10);
    this.testBrightnessCommand("twoByThree", "up1000TwoByThree",
        img1, 1000);
    this.testBrightnessCommand("twoByThree", "down1000TwoByThree",
        img1, -1000);
    // Macro 8: Horizontal Flip
    this.testHorizontalCommand("twoByThree", "horizontalFlipTwoByThree", img1);
    // Macro 9: Vertical Flip
    this.testVerticalCommand("twoByThree", "verticalFlipTwoByThree", img1);
    // Macro 10: Blur
    this.testBlurCommand("twoByThree", "blurTwoByThree", img1);
    // Macro 11: Sharpen
    this.testSharpenCommand("twoByThree", "sharpenTwoByThree", img1);
    // Macro 12: Greyscale
    this.testGreyscaleCommand("twoByThree", "greyscaleTwoByThree", img1);
    // Macro 13: Sepia
    this.testSepiaCommand("twoByThree", "sepiaTwoByThree", img1);
    // Macro 14: Downscale
    this.testDownscaleCommand("twoByThree", "downscaleTwoByThree", img1,
        2, 2);
    this.testDownscaleCommand("twoByThree", "downscaleTwoByThree", img1,
        2, 1);
    this.testDownscaleCommand("twoByThree", "downscaleTwoByThree", img1,
        1, 2);
    this.testDownscaleCommand("twoByThree", "downscaleTwoByThree", img1,
        1, 1);
    // The following commands are those supported by the Mask
    // Macro 1: RedGrayscale
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "redTwoByThreeMasked", img1, new Component(Color::getRed));
    // Macro 2: GreenGrayscale
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "greenTwoByThreeMasked", img1, new Component(Color::getGreen));
    // Macro 3: BlueGrayscale
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "blueTwoByThreeMasked", img1, new Component(Color::getBlue));
    // Macro 4: LumaGrayscale
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "lumaTwoByThreeMasked", img1, new Component(Color::getLuma));
    // Macro 5: ValueGrayscale
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "valueTwoByThreeMasked", img1, new Component(Color::getValue));
    // Macro 6: IntensityGrayscale
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "intensityTwoByThreeMasked", img1, new Component(Color::getIntensity));
    // Macro 7: Brighten
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "up10TwoByThreeMasked", img1, new Brighten(10));
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "down10TwoByThreeMasked", img1, new Brighten(-10));
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "up1000TwoByThreeMasked", img1, new Brighten(1000));
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "down1000TwoByThreeMasked", img1, new Brighten(-1000));
    // Macro 8: Horizontal Flip
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "horizontalFlipTwoByThreeMasked", img1, new HorizontalFlip());
    // Macro 9: Vertical Flip
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "verticalFlipTwoByThreeMasked", img1, new VerticalFlip());
    // Macro 10: Blur
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "blurTwoByThreeMasked", img1, new Blur());
    // Macro 11: Sharpen
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "sharpenTwoByThreeMasked", img1, new Sharpen());
    // Macro 12: Greyscale
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "greyscaleTwoByThreeMasked", img1, new Greyscale());
    // Macro 13: Sepia
    this.testMaskCommand("twoByThree", "twoByThreeMask",
        "sepiaTwoByThreeMasked", img1, new Sepia());

    // On Image 2:
    // Macro 1: RedGrayscale
    this.testGrayscaleCommand("threeByTwo", "redThreeByTwo", img2,
        Color::getRed);
    // Macro 2: GreenGrayscale
    this.testGrayscaleCommand("threeByTwo", "greenThreeByTwo", img2,
        Color::getGreen);
    // Macro 3: BlueGrayscale
    this.testGrayscaleCommand("threeByTwo", "blueThreeByTwo", img2,
        Color::getBlue);
    // Macro 4: LumaGrayscale
    this.testGrayscaleCommand("threeByTwo", "lumaThreeByTwo", img2,
        Color::getLuma);
    // Macro 5: ValueGrayscale
    this.testGrayscaleCommand("threeByTwo", "valueThreeByTwo", img2,
        Color::getValue);
    // Macro 6: IntensityGrayscale
    this.testGrayscaleCommand("threeByTwo", "intensityThreeByTwo", img2,
        Color::getIntensity);
    // Macro 7: Brighten
    this.testBrightnessCommand("threeByTwo", "up10ThreeByTwo", img2, 10);
    this.testBrightnessCommand("threeByTwo", "down10ThreeByTwo", img2, -10);
    this.testBrightnessCommand("threeByTwo", "up1000ThreeByTwo", img2, 1000);
    this.testBrightnessCommand("threeByTwo", "down1000ThreeByTwo", img2, -1000);
    // Macro 8: Horizontal Flip
    this.testHorizontalCommand("threeByTwo", "horizontalFlipThreeByTwo", img2);
    // Macro 9: Vertical Flip
    this.testVerticalCommand("threeByTwo", "verticalFlipThreeByTwo", img2);
    // Macro 10: Blur
    this.testBlurCommand("threeByTwo", "blurThreeByTwo", img2);
    // Macro 11: Sharpen
    this.testSharpenCommand("threeByTwo", "sharpenThreeByTwo", img2);
    // Macro 12: Greyscale
    this.testGreyscaleCommand("threeByTwo", "greyscaleThreeByTwo", img2);
    // Macro 13: Sepia
    this.testSepiaCommand("threeByTwo", "sepiaThreeByTwo", img2);
    // Macro 14: Downscale
    this.testDownscaleCommand("threeByTwo", "downscaleThreeByTwo", img2,
        2, 2);
    this.testDownscaleCommand("threeByTwo", "downscaleThreeByTwo", img2,
        2, 1);
    this.testDownscaleCommand("threeByTwo", "downscaleThreeByTwo", img2,
        2, 1);
    this.testDownscaleCommand("threeByTwo", "downscaleThreeByTwo", img2,
        1, 1);
    // The following commands are those supported by the Mask
    // Macro 1: RedGrayscale
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "redThreeByTwoMasked", img2, new Component(Color::getRed));
    // Macro 2: GreenGrayscale
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "greenThreeByTwoMasked", img2, new Component(Color::getGreen));
    // Macro 3: BlueGrayscale
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "blueThreeByTwoMasked", img2, new Component(Color::getBlue));
    // Macro 4: LumaGrayscale
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "lumaThreeByTwoMasked", img2, new Component(Color::getLuma));
    // Macro 5: ValueGrayscale
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "valueThreeByTwoMasked", img2, new Component(Color::getValue));
    // Macro 6: IntensityGrayscale
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "intensityThreeByTwoMasked", img2, new Component(Color::getIntensity));
    // Macro 7: Brighten
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "up10ThreeByTwoMasked", img2, new Brighten(10));
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "down10ThreeByTwoMasked", img2, new Brighten(-10));
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "up1000ThreeByTwoMasked", img2, new Brighten(1000));
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "down1000ThreeByTwoMasked", img2, new Brighten(-1000));
    // Macro 8: Horizontal Flip
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "horizontalFlipThreeByTwoMasked", img2, new HorizontalFlip());
    // Macro 9: Vertical Flip
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "verticalFlipThreeByTwoMasked", img2, new VerticalFlip());
    // Macro 10: Blur
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "blurThreeByTwoMasked", img2, new Blur());
    // Macro 11: Sharpen
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "sharpenThreeByTwoMasked", img2, new Sharpen());
    // Macro 12: Greyscale
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "greyscaleThreeByTwoMasked", img2, new Greyscale());
    // Macro 13: Sepia
    this.testMaskCommand("threeByTwo", "threeByTwoMask",
        "sepiaThreeByTwoMasked", img2, new Sepia());
  }

  private void testGrayscaleCommand(String oldName, String newName, ImageState expected,
                                    Function<Color, Integer> func) {
    this.testMacro(oldName, newName, new Component(func), this.imageAsComponent(expected, func));
  }

  private void testBrightnessCommand(String oldName, String newName, ImageState expected, int n) {
    this.testMacro(oldName, newName, new Brighten(n), this.imageBrightness(expected, n));
  }

  private void testHorizontalCommand(String oldName, String newName, ImageState expected) {
    this.testMacro(oldName, newName, new HorizontalFlip(), this.imageHorizontal(expected));
  }

  private void testVerticalCommand(String oldName, String newName, ImageState expected) {
    this.testMacro(oldName, newName, new VerticalFlip(), this.imageVertical(expected));
  }

  private void testBlurCommand(String oldName, String newName, ImageState expected) {
    this.testMacro(oldName, newName, new Blur(), this.imageConvolve(expected, blur));
  }

  private void testSharpenCommand(String oldName, String newName, ImageState expected) {
    this.testMacro(oldName, newName, new Sharpen(), this.imageConvolve(expected, sharpen));
  }

  private void testGreyscaleCommand(String oldName, String newName, ImageState expected) {
    this.testMacro(oldName, newName, new Greyscale(),
        this.imageMatrixTransform(expected, greyscale));
  }

  private void testSepiaCommand(String oldName, String newName, ImageState expected) {
    this.testMacro(oldName, newName, new Sepia(), this.imageMatrixTransform(expected, sepia));
  }

  private void testDownscaleCommand(String oldName, String newName, ImageState expected,
                                    int height, int width) {
    this.testMacro(oldName, newName, new Downscale(height, width),
        this.imageDownscale(expected, height, width));
  }

  private void testMaskCommand(String oldName, String maskImage, String newName,
                               ImageTransform expected, Macro macro) {
    ImageState maskImg = model.getImage(maskImage);
    this.testMacro(oldName, newName, new Mask(macro, maskImg),
        this.imageMask(expected, maskImg, macro));
  }

  private void testMacro(String oldName, String newName, Macro macro, ImageState expected) {
    try {
      macro.execute(null);
      fail("Macro should not execute with a null image.");
    } catch (IllegalArgumentException e) {
      assertEquals("Image cannot be null.", e.getMessage());
    }

    ImageState oldImg = model.getImage(oldName);
    model.runCommand(macro, oldName, newName);
    ImageState newImg = model.getImage(newName);
    // Proof that there was no mutation on the original image
    assertNotEquals(oldImg, newImg);
    // But that the image new image produced is the one that we expect
    assertEquals(expected, newImg);
  }

  @Test
  public void testRunCommand() {
    model.createImage(img1arr, "twoByThree", 255);
    StringBuilder out = new StringBuilder();
    model.runCommand(img -> {
      out.append("called once");
      return img;
    }, "twoByThree", "changedImage");
    assertEquals("called once", out.toString());
  }

  @Test
  public void runCommandExceptions() {
    model.createImage(img1arr, "twoByThree", 255);
    assertNotNull(model.getImage("twoByThree"));
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
    model.createImage(img1arr, "twoByThree", 255);
    model.createImage(img2arr, "threeByTwo", 255);

    // and let's save these images that we loaded
    ImageState twoByThree = model.getImage("twoByThree");
    ImageState threeByTwo = model.getImage("threeByTwo");

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
