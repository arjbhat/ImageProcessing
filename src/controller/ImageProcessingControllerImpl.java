package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import javax.imageio.ImageIO;

import model.Color;
import model.ImageProcessingModel;
import model.ImageState;
import model.RGBColor;
import model.macros.Brighten;
import model.macros.Component;
import model.macros.HorizontalFlip;
import model.macros.VerticalFlip;
import view.ImageProcessingView;

/**
 * Implementation of the ImageProcessingController that works with the file passed in and
 * the model in order to process the commands that the user passed in - sending any output to the
 * view. This specific implementation is centred around PPM files but can potentially be
 * applied to other image formats.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {
  //INVARIANT: Non-null Image Processing Model
  private final ImageProcessingModel model;
  //INVARIANT: Non-null Readable
  private final Readable input;
  //INVARIANT: Non-null Image Processing View
  private final ImageProcessingView output;
  //INVARIANT: Non-null Map
  private final Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;
  private final List<String> commandMenu;

  /**
   * In order to construct an image processing controller we need a model, readable, and a view.
   *
   * @param model  the Image Processing model that we work on
   * @param input  the readable (instructions for the model)
   * @param output the view that we relay information with
   * @throws IllegalArgumentException if the model, readable, or view are null
   */
  public ImageProcessingControllerImpl(ImageProcessingModel model, ImageProcessingView output,
                                       Readable input) throws IllegalArgumentException {
    // We ensure that none of the arguments are null
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    if (input == null) {
      throw new IllegalArgumentException("Readable cannot be null.");
    }
    if (output == null) {
      throw new IllegalArgumentException("View cannot be null.");
    }

    this.model = model;
    this.input = input;
    this.output = output;

    // and make a hashmap of commands
    this.knownCommands = new HashMap<>();
    this.commandMenu = this.loadMenu();
    // that we load commands into
    this.loadCommands();
  }

  // loads all the commands that the model can be operated on with
  // this makes it easy for the user to add features in the future
  protected void loadCommands() {
    // Command to load the file in the model
    addCommand("load",
        sc -> model -> this.loadImg(model, sc.next(), sc.next()));
    // Command to save the file from the model
    addCommand("save",
        sc -> model -> this.saveImg(model, sc.next(), sc.next()));
    // A command that changes an image to its red-grayscale representation
    addCommand("red-component",
        sc -> model -> {
          model.runCommand(new Component(Color::getRed), sc.next(), sc.next());
          this.writeMessage("Red-component image created.");
        });
    // A command that changes an image to its green-grayscale representation
    addCommand("green-component",
        sc -> model -> {
          model.runCommand(new Component(Color::getGreen), sc.next(), sc.next());
          this.writeMessage("Green-component image created.");
        });
    // A command that changes an image to its blue-grayscale representation
    addCommand("blue-component",
        sc -> model -> {
          model.runCommand(new Component(Color::getBlue), sc.next(), sc.next());
          this.writeMessage("Blue-component image created.");
        });
    // A command that changes an image to its value-grayscale representation
    addCommand("value-component",
        sc -> model -> {
          model.runCommand(new Component(Color::getValue), sc.next(), sc.next());
          this.writeMessage("Value-component image created.");
        });
    // A command that changes an image to its luma-grayscale representation
    addCommand("luma-component",
        sc -> model -> {
          model.runCommand(new Component(Color::getLuma), sc.next(), sc.next());
          this.writeMessage("Luma-component image created.");
        });
    // A command that changes an image to its intensity-grayscale representation
    addCommand("intensity-component",
        sc -> model -> {
          model.runCommand(new Component(Color::getIntensity), sc.next(), sc.next());
          this.writeMessage("Intensity-component image created.");
        });
    // A command that changes an image to be horizontally flipped
    addCommand("horizontal-flip",
        sc -> model -> {
          model.runCommand(new HorizontalFlip(), sc.next(), sc.next());
          this.writeMessage("Horizontally flipped image created.");
        });
    // A command that changes an image to be vertically flipped
    addCommand("vertical-flip",
        sc -> model -> {
          model.runCommand(new VerticalFlip(), sc.next(), sc.next());
          this.writeMessage("Vertically flipped image created.");
        });
    // A command that changes an image to be brightened or darkened
    addCommand("brighten",
        sc -> model -> {
          model.runCommand(new Brighten(getInt(sc)), sc.next(), sc.next());
          this.writeMessage("Brightness changed image created.");
        });
  }

  protected void addCommand(String name, Function<Scanner, ImageProcessingCommand> command) {
    knownCommands.put(name, command);
  }

  @Override
  public void control() throws IllegalStateException {
    Scanner sc = new Scanner(input);
    boolean quit = false;

    // print the welcome message
    this.welcomeMessage();

    while (!quit) { //continue until the user quits
      writeMessage("Type instruction:"); //prompt for the instruction name
      Scanner line;
      String userInstruction;
      try {
        line = new Scanner(sc.nextLine());
        userInstruction = line.hasNext() ? line.next() : "";
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("Unable to successfully receive input.");
      }
      if (userInstruction.equals("quit") || userInstruction.equals("q")) {
        quit = true;
      } else if (userInstruction.equals("menu")) {
        this.printMenu();
      } else if (userInstruction.startsWith("#")) {
        // comment pass-through
      } else {
        processCommand(userInstruction, line, model);
      }
    }

    //after the user has quit, print farewell message
    this.farewellMessage();
  }

  /**
   * Parse an image file in the PPM format and create a 2D array of
   * Colors that gets added to the model - with a specified String name.
   *
   * @param file the readable that we parse
   */
  protected void parsePPM(Readable file, String name, ImageProcessingModel model) {
    Scanner sc = new Scanner(file);
    StringBuilder lines = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (!s.startsWith("#")) {
        lines.append(s).append(System.lineSeparator());
      }
    }
    sc = new Scanner(lines.toString());

    if (!sc.next().equals("P3")) {
      throw new IllegalArgumentException("Invalid file format.");
    }

    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    Color[][] pane = new Color[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        pane[row][col] = new RGBColor(sc.nextInt(), sc.nextInt(), sc.nextInt());
      }
    }
    model.createImage(pane, name, maxValue);
  }

  // Converts an ImageState to PPM format so that it can be written to a file
  protected String toPPM(ImageState img) {
    StringBuilder lines = new StringBuilder();
    lines.append("P3").append(System.lineSeparator());
    lines.append(img.getWidth()).append(" ");
    lines.append(img.getHeight()).append(System.lineSeparator());
    lines.append(img.getMaxValue()).append(System.lineSeparator());

    for (int row = 0; row < img.getHeight(); row += 1) {
      for (int col = 0; col < img.getWidth(); col += 1) {
        Color c = img.getColorAt(row, col);
        lines.append(c.getRed()).append(System.lineSeparator());
        lines.append(c.getGreen()).append(System.lineSeparator());
        lines.append(c.getBlue()).append(System.lineSeparator());
      }
    }
    return lines.toString();
  }

  private void loadImg(ImageProcessingModel model, String fileName, String imgName) {
    File source = new File(fileName);
    try (FileReader file = new FileReader(source)) {
      BufferedImage img = ImageIO.read(source);
      if (img == null) {
        this.parsePPM(file, imgName, model);
      } else {
        Color[][] pane = new Color[img.getHeight()][img.getWidth()];
        for (int row = 0; row < pane.length; row += 1) {
          for (int col = 0; col < pane[row].length; col += 1) {
            int color = img.getRGB(col, row);
            int alpha = (color >> 24) & 0xff;
            int r = (color >> 16) & 0xff;
            int g = (color >> 8) & 0xff;
            int b = color & 0xff;
            pane[row][col] = new RGBColor(r, g, b, alpha);
          }
        }
        model.createImage(pane, imgName, 255);
      }

      this.writeMessage("File is loaded.");
    } catch (IOException | NoSuchElementException e) {
      throw new IllegalArgumentException("Failed to load file.");
    }
  }

  private void saveImg(ImageProcessingModel model, String fileName, String imgName) {
    ImageState img = model.getImage(imgName);
    if (fileName.endsWith(".ppm") || fileName.endsWith(".PPM")) {
      try (FileWriter file = new FileWriter(fileName)) {
        file.write(toPPM(img));
      } catch (IOException e) {
        throw new IllegalArgumentException("Failed to save file.");
      }
    } else {
      if (!this.saveImageFile(img, fileName)) {
        throw new IllegalArgumentException("Failed to save file.");
      }
    }
    this.writeMessage("File is saved.");
  }

  private boolean saveImageFile(ImageState img, String fileName) {
    String extension = fileName.contains(".") ?
        fileName.substring(fileName.lastIndexOf(".") + 1) : fileName;
    boolean isPNG = extension.equals("png") || extension.equals("PNG");
    boolean isBMP = extension.equals("bmp") || extension.equals("BMP");
    BufferedImage buff = new BufferedImage(img.getWidth(), img.getHeight(),
        isPNG ? BufferedImage.TYPE_INT_ARGB : isBMP ? BufferedImage.TYPE_INT_BGR
            : BufferedImage.TYPE_INT_RGB);
    for (int row = 0; row < img.getHeight(); row += 1) {
      for (int col = 0; col < img.getWidth(); col += 1) {
        Color c = img.getColorAt(row, col);
        buff.setRGB(col, row, (c.getAlpha() << 24)
            | (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue());
      }
    }
    try {
      return ImageIO.write(buff, extension, new File(fileName));
    } catch (IOException e) {
      return false;
    }
  }

  // Processes the User instruction and checks if we have a command with the same name
  private void processCommand(String userInstruction, Scanner sc, ImageProcessingModel model)
      throws IllegalStateException {
    Function<Scanner, ImageProcessingCommand> cmd =
        knownCommands.getOrDefault(userInstruction, null);
    if (cmd == null) {
      this.writeMessage("Unknown command, please try again. (╥﹏╥)");
    } else {
      try {
        ImageProcessingCommand c = cmd.apply(sc);
        c.run(model);
      } catch (NoSuchElementException e) {
        this.writeMessage("Command failed: not enough arguments provided");
      } catch (IllegalArgumentException e) {
        this.writeMessage("Command failed: " + e.getMessage());
      }
    }
  }

  // Sends a message to the view
  protected void writeMessage(String message) throws IllegalStateException {
    try {
      output.renderMessage(message + System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  // Throws an exception if the scanner input is not an integer
  protected int getInt(Scanner sc) throws IllegalArgumentException {
    try {
      return sc.nextInt();
    } catch (NoSuchElementException e) {
      throw new IllegalArgumentException("Invalid input type instead of integer.");
    }
  }

  private void welcomeMessage() throws IllegalStateException {
    writeMessage("✿ ✿ ✿ Welcome to the Image Processing program! ✿ ✿ ✿");
    writeMessage("   By: Arjun Bhat & Alexander Chang-Davidson\n");
    writeMessage("[Type menu to read support user instructions.]");
  }

  private void printMenu() throws IllegalStateException {
    writeMessage("Supported user instructions are: ");
    for (String msg : this.commandMenu) {
      writeMessage(" ➤ " + msg);
    }
    writeMessage(" ➤ menu (Print supported instruction list)");
    writeMessage(" ➤ q or quit (quit the program)");
  }

  protected List<String> loadMenu() {
    List<String> list = new ArrayList<>();
    list.add("load image-path image-name "
        + "(Loads an image from the specified path and refers to it henceforth in the program "
        + "by the given name)");
    list.add("save image-path image-name "
        + "(Saves the image with the given name to the specified path which includes "
        + "the name of the file)");
    list.add("(component name)-component image-name dest-image-name "
        + "(Create a greyscale image with the (component name) component of the image with "
        + "the given name."
        + " [supported (component name): red, green, blue, value, luma, intensity])");
    list.add("horizontal-flip image-name dest-image-name "
        + "(Flip an image horizontally to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("vertical-flip image-name dest-image-name "
        + "(Flip an image vertically to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("brighten increment image-name dest-image-name "
        + "(Brighten the image by the given increment to create a new image, referred to "
        + "henceforth by the given destination name - the increment may be positive "
        + "(brightening) or negative (darkening))");
    return list;
  }

  private void farewellMessage() throws IllegalStateException {
    writeMessage("Thank you for using this program! (っ◕‿◕)っ");
  }
}
