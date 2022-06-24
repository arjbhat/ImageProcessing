import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;

import controller.ImageProcessingControllerImplProMax;
import model.Color;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import model.macros.Blur;
import model.macros.Brighten;
import model.macros.Component;
import model.macros.Downscale;
import model.macros.Greyscale;
import model.macros.HorizontalFlip;
import model.macros.Macro;
import model.macros.Sepia;
import model.macros.Sharpen;
import model.macros.VerticalFlip;
import view.Features;
import view.ImageProcessingGUI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing implementation of features in the pro max controller.
 */
public class FeaturesTest extends TestHelper {
  private ImageProcessingModel model;
  private StringBuilder guiLog;
  private ImageProcessingGUI view;
  private Features controller;

  @Before
  public void init() {
    guiLog = new StringBuilder();
    model = new ImageProcessingModelImpl();
    view = new MockGUI(guiLog);
    controller = new ImageProcessingControllerImplProMax(model, view);
  }

  @Test
  public void testNonGUIMode() {
    controller = new ImageProcessingControllerImplProMax(model, view, new StringReader("q"));
    try {
      controller.load("", "");
    } catch (IllegalStateException e) {
      assertEquals("GUI method called in text mode.", e.getMessage());
    }
    try {
      controller.save("");
    } catch (IllegalStateException e) {
      assertEquals("GUI method called in text mode.", e.getMessage());
    }
    try {
      controller.select("");
    } catch (IllegalStateException e) {
      assertEquals("GUI method called in text mode.", e.getMessage());
    }
    try {
      controller.runCommand("", "");
    } catch (IllegalStateException e) {
      assertEquals("GUI method called in text mode.", e.getMessage());
    }
    try {
      controller.runBrightness("", 0);
    } catch (IllegalStateException e) {
      assertEquals("GUI method called in text mode.", e.getMessage());
    }
    try {
      controller.runDownscale("", 0, 0);
    } catch (IllegalStateException e) {
      assertEquals("GUI method called in text mode.", e.getMessage());
    }
  }

  @Test
  public void testSelectNull() {
    try {
      controller.select(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Trying to select a null name.", e.getMessage());
    }
  }

  @Test
  public void testRunBlankNames() {
    controller.load("res/img1.ppm", "");
    controller.load("res/img1.ppm", "name");
    controller.runCommand("sepia", null);
    controller.runCommand("sepia", "");
    controller.runCommand("greyscale", null);
    controller.runCommand("red-component", "");
    controller.runBrightness(null, 0);
    controller.runBrightness("", 0);
    controller.runDownscale(null, 1, 1);
    controller.runDownscale("", 1, 1);
    controller.select("");
    assertEquals(joinLines(
            "method: addFeatures",
            "method: selectImage image-name: image image: 2x3",
            "method: selectImage image-name: name image: 2x3",
            "method: selectImage image-name: name image: 2x3",
            "method: selectImage image-name: name image: 2x3",
            "method: selectImage image-name: name image: 2x3",
            "method: selectImage image-name: name image: 2x3",
            "method: selectImage image-name: name image: 2x3",
            "method: selectImage image-name: name image: 2x3",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: displayError err: Unknown image."),
        guiLog.toString());
  }

  @Test
  public void testSpacesName() {
    controller.load("res/img1.ppm", "space name");
    controller.runCommand("sepia", "space name");
    controller.runCommand("red-component", "space name");
    controller.runBrightness("space name", 0);
    controller.runDownscale("space name", 1, 1);
    controller.select("space name");
    assertEquals(joinLines(
            "method: addFeatures",
            "method: displayError err: Name cannot contain spaces.",
            "method: displayError err: Name cannot contain spaces.",
            "method: displayError err: Name cannot contain spaces.",
            "method: displayError err: Name cannot contain spaces.",
            "method: displayError err: Name cannot contain spaces.",
            "method: displayError err: Unknown image."),
        guiLog.toString());
  }

  @Test
  public void testUnknownImage() {
    controller.runCommand("sepia", "notLoaded");
    controller.runCommand("red-component", "notLoaded");
    controller.runBrightness("notLoaded", 0);
    controller.runDownscale("notLoaded", 1, 1);
    controller.select("notLoaded");
    assertEquals(joinLines(
            "method: addFeatures",
            "method: displayError err: Unknown image.",
            "method: displayError err: Unknown image.",
            "method: displayError err: Unknown image.",
            "method: displayError err: Unknown image.",
            "method: displayError err: Unknown image."),
        guiLog.toString());
  }

  @Test
  public void testFailedLoad() {
    controller.load("res/donotcreate", "name");
    assertEquals(joinLines(
            "method: addFeatures",
            "method: displayError err: Failed to load file."),
        guiLog.toString());
  }

  @Test
  public void testBadDownscaleArgs() {
    controller.load("res/img1.ppm", "name");
    controller.runDownscale("name", -1, 1);
    controller.runDownscale("name", 1, -1);
    controller.runDownscale("name", 0, 0);
    controller.runDownscale("name", 0, 1);
    controller.runDownscale("name", 1, 0);
    controller.runDownscale("name", 4, 1);
    controller.runDownscale("name", 1, 4);
    controller.runDownscale("name", 4, 4);
    assertEquals(joinLines(
            "method: addFeatures",
            "method: selectImage image-name: name image: 2x3",
            "method: displayError err: Cannot downscale to non-positive dimensions.",
            "method: displayError err: Cannot downscale to non-positive dimensions.",
            "method: displayError err: Cannot downscale to non-positive dimensions.",
            "method: displayError err: Cannot downscale to non-positive dimensions.",
            "method: displayError err: Cannot downscale to non-positive dimensions.",
            "method: displayError err: "
                + "Dest-image's height and width must be less than or equal to the original's.",
            "method: displayError err: "
                + "Dest-image's height and width must be less than or equal to the original's.",
            "method: displayError err: "
                + "Dest-image's height and width must be less than or equal to the original's."),
        guiLog.toString());
  }

  @Test
  public void testValidCommands() {
    StringBuilder modelLog = new StringBuilder();
    ArrayList<Macro> macroLog = new ArrayList<>();
    controller = new ImageProcessingControllerImplProMax(new MockModel(modelLog, macroLog), view);

    controller.runCommand("red-component", "name");
    controller.runCommand("green-component", "name");
    controller.runCommand("blue-component", "name");
    controller.runCommand("value-component", "name");
    controller.runCommand("luma-component", "name");
    controller.runCommand("intensity-component", "name");
    controller.runCommand("horizontal-flip", "name");
    controller.runCommand("vertical-flip", "name");
    controller.runBrightness("name", 10);
    controller.runBrightness("name", -11);
    controller.runCommand("sepia", "name");
    controller.runCommand("greyscale", "name");
    controller.runCommand("sharpen", "name");
    controller.runCommand("blur", "name");
    controller.runDownscale("name", 1, 1);

    assertEquals(joinLines(
            "method: addFeatures",
            "method: addFeatures",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1",
            "method: selectImage image-name: name image: 1x1"),
        guiLog.toString());

    assertEquals(joinLines(
            "method: runCommand image-name: image dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name",
            "method: runCommand image-name: name dest-image-name: name",
            "method: getImage image-name: name"),
        modelLog.toString());

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
    assertTrue(macroLog.get(10) instanceof Sepia);
    assertEquals(this.imageMatrixTransform(img1, sepia),
        macroLog.get(10).execute(img1));
    assertTrue(macroLog.get(11) instanceof Greyscale);
    assertEquals(this.imageMatrixTransform(img1, greyscale),
        macroLog.get(11).execute(img1));
    assertTrue(macroLog.get(12) instanceof Sharpen);
    assertEquals(this.imageConvolve(img1, sharpen),
        macroLog.get(12).execute(img1));
    assertTrue(macroLog.get(13) instanceof Blur);
    assertEquals(this.imageConvolve(img1, blur),
        macroLog.get(13).execute(img1));
    assertTrue(macroLog.get(14) instanceof Downscale);
    assertEquals(this.imageDownscale(img1, 1, 1),
        macroLog.get(14).execute(img1));
  }

  private String joinLines(String... lines) {
    return String.join(System.lineSeparator(), lines) + System.lineSeparator();
  }
}
