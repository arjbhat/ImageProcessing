import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImplPro;
import model.Color;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import model.ImageState;
import model.macros.Blur;
import model.macros.Brighten;
import model.macros.Component;
import model.macros.Greyscale;
import model.macros.HorizontalFlip;
import model.macros.Macro;
import model.macros.Sepia;
import model.macros.Sharpen;
import model.macros.VerticalFlip;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the controller implementation.
 */
public class ImageProcessingControllerImplProTest extends AbstractControllerTest {

  protected ImageProcessingController makeController(ImageProcessingModel model,
                                                     ImageProcessingView view,
                                                     Readable input) {
    return new ImageProcessingControllerImplPro(model, view, input);
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
        inputs("sepia img sepia-img"),
        prints("method: runCommand image-name: img dest-image-name: sepia-img"),
        inputs("greyscale img greyscale-img"),
        prints("method: runCommand image-name: img dest-image-name: greyscale-img"),
        inputs("sharpen img sharp-img"),
        prints("method: runCommand image-name: img dest-image-name: sharp-img"),
        inputs("blur img blur-img"),
        prints("method: runCommand image-name: img dest-image-name: blur-img"),
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

    testFile("res/out.ppm", "P3\n" + "1 1\n" + "255\n" + "0\n0\n0\n");
  }

  @Test
  public void testControllerToView() {
    UserIO[] interactions = new UserIO[]{
        prints("method: renderMessage"), // welcome
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"), // type input
        inputs("menu"),
        prints("method: renderMessage"), // 13 menu options
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
        prints("method: renderMessage"),
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
        inputs("sepia arj sepia-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("greyscale arj grey-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("sharpen arj sharp-arj"),
        prints("method: renderMessage"), // confirmation
        prints("method: renderMessage"), // type input
        inputs("blur arj blur-arj"),
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
        inputs("load res/img1.jpg test1"),
        prints("File is loaded."),
        prints("Type instruction:"),
        inputs("load res/img1.png test2"),
        prints("File is loaded."),
        prints("Type instruction:"),
        inputs("load res/img1.jpeg test3"),
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
        inputs("brighten 10 test1 bright1"),
        prints("Brightness changed image created."),
        prints("Type instruction:"),
        inputs("horizontal-flip test1 horiz1"),
        prints("Horizontally flipped image created."),
        prints("Type instruction:"),
        inputs("vertical-flip test1 vert1"),
        prints("Vertically flipped image created."),
        prints("Type instruction:"),
        inputs("brighten 10 test2 bright2"),
        prints("Brightness changed image created."),
        prints("Type instruction:"),
        inputs("horizontal-flip test2 horiz2"),
        prints("Horizontally flipped image created."),
        prints("Type instruction:"),
        inputs("vertical-flip test2 vert2"),
        prints("Vertically flipped image created."),
        prints("Type instruction:"),
        inputs("brighten 10 test3 bright3"),
        prints("Brightness changed image created."),
        prints("Type instruction:"),
        inputs("horizontal-flip test3 horiz3"),
        prints("Horizontally flipped image created."),
        prints("Type instruction:"),
        inputs("vertical-flip test3 vert3"),
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

        inputs("save res/out1.jpg test"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out2.jpg bright"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out3.jpg horiz"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out4.jpg vert"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out1.png test"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out2.png bright"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out3.png horiz"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out4.png vert"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out1.bmp test"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out2.bmp bright"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out3.bmp horiz"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out4.bmp vert"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out11.ppm test1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out21.ppm bright1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out31.ppm horiz1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out41.ppm vert1"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out11.jpg test1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out21.jpg bright1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out31.jpg horiz1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out41.jpg vert1"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out11.png test1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out21.png bright1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out31.png horiz1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out41.png vert1"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out11.bmp test1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out21.bmp bright1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out31.bmp horiz1"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out41.bmp vert1"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out12.ppm test2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out22.ppm bright2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out32.ppm horiz2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out42.ppm vert2"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out12.jpg test2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out22.jpg bright2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out32.jpg horiz2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out42.jpg vert2"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out12.png test2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out22.png bright2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out32.png horiz2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out42.png vert2"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out12.bmp test2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out22.bmp bright2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out32.bmp horiz2"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out42.bmp vert2"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out13.ppm test3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out23.ppm bright3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out33.ppm horiz3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out43.ppm vert3"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out13.jpg test3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out23.jpg bright3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out33.jpg horiz3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out43.jpg vert3"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out13.png test3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out23.png bright3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out33.png horiz3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out43.png vert3"),
        prints("File is saved."),
        prints("Type instruction:"),

        inputs("save res/out13.bmp test3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out23.bmp bright3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out33.bmp horiz3"),
        prints("File is saved."),
        prints("Type instruction:"),
        inputs("save res/out43.bmp vert3"),
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

    ImageState img1 = model.getImage("test");
    ImageState img2 = model.getImage("bright");
    ImageState img3 = model.getImage("horiz");
    ImageState img4 = model.getImage("vert");
    ImageState img11 = model.getImage("test1");
    ImageState img21 = model.getImage("bright1");
    ImageState img31 = model.getImage("horiz1");
    ImageState img41 = model.getImage("vert1");
    ImageState img12 = model.getImage("test2");
    ImageState img22 = model.getImage("bright2");
    ImageState img32 = model.getImage("horiz2");
    ImageState img42 = model.getImage("vert2");

    assertEquals(img1, fileToImage("res/out1.png"));
    assertEquals(img2, fileToImage("res/out2.png"));
    assertEquals(img3, fileToImage("res/out3.png"));
    assertEquals(img4, fileToImage("res/out4.png"));
    // assertEquals(img1, fileToImage("res/out1.jpg")); // remove jpg test, lossy compression
    // assertEquals(img2, fileToImage("res/out2.jpg"));
    // assertEquals(img3, fileToImage("res/out3.jpg"));
    // assertEquals(img4, fileToImage("res/out4.jpg"));
    assertEquals(img1, fileToImage("res/out1.bmp"));
    assertEquals(img2, fileToImage("res/out2.bmp"));
    assertEquals(img3, fileToImage("res/out3.bmp"));
    assertEquals(img4, fileToImage("res/out4.bmp"));
    assertEquals(img11, fileToImage("res/out11.png"));
    assertEquals(img21, fileToImage("res/out21.png"));
    assertEquals(img31, fileToImage("res/out31.png"));
    assertEquals(img41, fileToImage("res/out41.png"));
    // assertEquals(img1, fileToImage("res/out11.jpg")); // remove jpg test, lossy compression
    // assertEquals(img2, fileToImage("res/out21.jpg"));
    // assertEquals(img3, fileToImage("res/out31.jpg"));
    // assertEquals(img4, fileToImage("res/out41.jpg"));
    assertEquals(img11, fileToImage("res/out11.bmp"));
    assertEquals(img21, fileToImage("res/out21.bmp"));
    assertEquals(img31, fileToImage("res/out31.bmp"));
    assertEquals(img41, fileToImage("res/out41.bmp"));
    assertEquals(img12, fileToImage("res/out12.png"));
    assertEquals(img22, fileToImage("res/out22.png"));
    assertEquals(img32, fileToImage("res/out32.png"));
    assertEquals(img42, fileToImage("res/out42.png"));
    // assertEquals(img1, fileToImage("res/out12.jpg")); // remove jpg test, lossy compression
    // assertEquals(img2, fileToImage("res/out22.jpg"));
    // assertEquals(img3, fileToImage("res/out32.jpg"));
    // assertEquals(img4, fileToImage("res/out42.jpg"));
    assertEquals(img12, fileToImage("res/out12.bmp"));
    assertEquals(img22, fileToImage("res/out22.bmp"));
    assertEquals(img32, fileToImage("res/out32.bmp"));
    assertEquals(img42, fileToImage("res/out42.bmp"));

    String img1Cont = "P3\n"
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
        + "100\n";
    String img2Cont = "P3\n"
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
        + "110\n";
    String img3Cont = "P3\n"
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
        + "100\n";
    String img4Cont = "P3\n"
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
        + "25\n";
    String img1Cont2 = "P3\n"
        + "2 3\n"
        + "255\n"
        + "13\n"
        + "0\n"
        + "16\n"
        + "65\n"
        + "46\n"
        + "68\n"
        + "73\n"
        + "54\n"
        + "76\n"
        + "83\n"
        + "64\n"
        + "86\n"
        + "62\n"
        + "57\n"
        + "77\n"
        + "101\n"
        + "96\n"
        + "116\n";
    String img2Cont2 = "P3\n"
        + "2 3\n"
        + "255\n"
        + "23\n"
        + "10\n"
        + "26\n"
        + "75\n"
        + "56\n"
        + "78\n"
        + "83\n"
        + "64\n"
        + "86\n"
        + "93\n"
        + "74\n"
        + "96\n"
        + "72\n"
        + "67\n"
        + "87\n"
        + "111\n"
        + "106\n"
        + "126\n";
    String img3Cont2 = "P3\n"
        + "2 3\n"
        + "255\n"
        + "65\n"
        + "46\n"
        + "68\n"
        + "13\n"
        + "0\n"
        + "16\n"
        + "83\n"
        + "64\n"
        + "86\n"
        + "73\n"
        + "54\n"
        + "76\n"
        + "101\n"
        + "96\n"
        + "116\n"
        + "62\n"
        + "57\n"
        + "77\n";
    String img4Cont2 = "P3\n"
        + "2 3\n"
        + "255\n"
        + "62\n"
        + "57\n"
        + "77\n"
        + "101\n"
        + "96\n"
        + "116\n"
        + "73\n"
        + "54\n"
        + "76\n"
        + "83\n"
        + "64\n"
        + "86\n"
        + "13\n"
        + "0\n"
        + "16\n"
        + "65\n"
        + "46\n"
        + "68\n";

    testFile("res/out1.ppm", img1Cont);
    testFile("res/out2.ppm", img2Cont);
    testFile("res/out3.ppm", img3Cont);
    testFile("res/out4.ppm", img4Cont);
    testFile("res/out11.ppm", img1Cont2);
    testFile("res/out21.ppm", img2Cont2);
    testFile("res/out31.ppm", img3Cont2);
    testFile("res/out41.ppm", img4Cont2);
    testFile("res/out12.ppm", img1Cont);
    testFile("res/out22.ppm", img2Cont);
    testFile("res/out32.ppm", img3Cont);
    testFile("res/out42.ppm", img4Cont);
    testFile("res/out13.ppm", img1Cont2);
    testFile("res/out23.ppm", img2Cont2);
    testFile("res/out33.ppm", img3Cont2);
    testFile("res/out43.ppm", img4Cont2);

    testFileEqual("res/out11.jpg", "res/out13.jpg");
    testFileEqual("res/out1.jpg", "res/out12.jpg");
    testFileEqual("res/out11.png", "res/out13.png");
    testFileEqual("res/out1.png", "res/out12.png");
    testFileEqual("res/out11.bmp", "res/out13.bmp");
    testFileEqual("res/out1.bmp", "res/out12.bmp");
    testFileEqual("res/out21.jpg", "res/out23.jpg");
    testFileEqual("res/out2.jpg", "res/out22.jpg");
    testFileEqual("res/out21.png", "res/out23.png");
    testFileEqual("res/out2.png", "res/out22.png");
    testFileEqual("res/out21.bmp", "res/out23.bmp");
    testFileEqual("res/out2.bmp", "res/out22.bmp");
    testFileEqual("res/out31.jpg", "res/out33.jpg");
    testFileEqual("res/out3.jpg", "res/out32.jpg");
    testFileEqual("res/out31.png", "res/out33.png");
    testFileEqual("res/out3.png", "res/out32.png");
    testFileEqual("res/out31.bmp", "res/out33.bmp");
    testFileEqual("res/out3.bmp", "res/out32.bmp");
    testFileEqual("res/out41.jpg", "res/out43.jpg");
    testFileEqual("res/out4.jpg", "res/out42.jpg");
    testFileEqual("res/out41.png", "res/out43.png");
    testFileEqual("res/out4.png", "res/out42.png");
    testFileEqual("res/out41.bmp", "res/out43.bmp");
    testFileEqual("res/out4.bmp", "res/out42.bmp");
  }

  @Test
  public void testBadInputs() {
    UserIO[] interactions = new UserIO[]{
        prints(this.welcomeMessage()),
        inputs(""),
        prints("Type instruction:"),
        prints("Command failed: Unknown command, please try again."),
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
        inputs("menu"),
        prints(this.printMenu()),
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

  @Test
  public void testFileInput() {
    File testScript = new File("res/testScript.txt");
    testScript.deleteOnExit();
    StringBuilder script = new StringBuilder();
    StringBuilder expected = new StringBuilder();
    StringBuilder outputLog = new StringBuilder();
    ImageProcessingModel model = new ImageProcessingModelImpl();
    ImageProcessingView view = new ImageProcessingViewImpl(outputLog);
    UserIO[] interactions = new UserIO[]{
        prints(this.welcomeMessage()),
        inputs(""),
        prints("Type instruction:"),
        prints("Command failed: Unknown command, please try again."),
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
        inputs("menu"),
        prints(this.printMenu()),
        prints("Type instruction:"),
        inputs("quit"),
        prints(this.farewellMessage())
    };
    for (UserIO act : interactions) {
      act.apply(script, expected);
    }
    try {
      Files.write(Paths.get(testScript.getPath()), script.toString().getBytes());
    } catch (IOException e) {
      fail("Could not write to file");
    }
    try (FileReader reader = new FileReader(testScript)) {
      new ImageProcessingControllerImplPro(model, view, reader).control();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(expected.toString(), outputLog.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNoMoreInputs() {
    this.runController(new MockModel(new StringBuilder(), new ArrayList<>()),
        new MockView(new StringBuilder()));
  }

  private String[] welcomeMessage() {
    return new String[]{"Welcome to the Image Processing program!",
        "By: Arjun Bhat & Alexander Chang-Davidson\n",
        "[Type menu to read support user instructions.]"};
  }

  private String[] printMenu() throws IllegalStateException {
    return new String[]{"Supported user instructions are: ",
        " ➤ load image-path image-name "
            + "(Loads an image from the specified path and refers to it henceforth in the program "
            + "by the given name)",
        " ➤ save image-path image-name "
            + "(Saves the image with the given name to the specified path which includes "
            + "the name of the file)",
        " ➤ (component name)-component image-name dest-image-name "
            + "(Create a greyscale image with the (component name) component of the image with "
            + "the given name."
            + " [supported (component name): red, green, blue, value, luma, intensity])",
        " ➤ horizontal-flip image-name dest-image-name "
            + "(Flip an image horizontally to create a new image, "
            + "referred to henceforth by the given destination name)",
        " ➤ vertical-flip image-name dest-image-name "
            + "(Flip an image vertically to create a new image, "
            + "referred to henceforth by the given destination name)",
        " ➤ brighten increment image-name dest-image-name "
            + "(Brighten the image by the given increment to create a new image, referred to "
            + "henceforth by the given destination name - the increment may be positive "
            + "(brightening) or negative (darkening))",
        " ➤ blur image-name dest-image-name "
            + "(Blur an image to create a new image, "
            + "referred to henceforth by the given destination name)",
        " ➤ sharpen image-name dest-image-name "
            + "(Sharpen an image to create a new image, "
            + "referred to henceforth by the given destination name)",
        " ➤ greyscale image-name dest-image-name "
            + "(Find the greyscale version of an image to create a new image, "
            + "referred to henceforth by the given destination name)",
        " ➤ sepia image-name dest-image-name "
            + "(Find the sepia version of an image to create a new image, "
            + "referred to henceforth by the given destination name)",
        " ➤ menu (Print supported instruction list)",
        " ➤ q or quit (quit the program)"};
  }

  private String[] farewellMessage() {
    return new String[]{"Thank you for using this program!"};
  }
}
