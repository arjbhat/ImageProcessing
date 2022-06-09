package controller;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import model.ImageProcessingModel;
import model.ImageState;
import model.RGBColor;
import model.macros.BlueGrayscale;
import model.macros.Brighten;
import model.macros.GreenGrayscale;
import model.macros.HorizontalFlip;
import model.macros.IntensityGrayscale;
import model.macros.LumaGrayscale;
import model.macros.RedGrayscale;
import model.macros.ValueGrayscale;
import model.macros.VerticalFlip;

/**
 * Implementation of the ImageProcessingController that works with the file passed in and
 * the model and the view in order to process the commands that the user passed in.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {
  private final ImageProcessingModel model;
  private final Readable input;
  private final Appendable output;

  private final Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;

  public ImageProcessingControllerImpl(ImageProcessingModel model, Readable input, Appendable output) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    if (input == null) {
      throw new IllegalArgumentException("Readable cannot be null");
    }
    if (output == null) {
      throw new IllegalArgumentException("Appendable cannot be null");
    }

    this.model = model;
    this.input = input;
    this.output = output;

    knownCommands = new HashMap<>();
    this.loadCommands();
  }

  private void loadCommands() {
    knownCommands.put("load",
            sc -> {
              String fileName = sc.next();
              String imgName = sc.next();
              return model -> {
                try (FileReader file = new FileReader(fileName)) {
                  this.parsePPM(file, imgName, model);
                } catch (IOException e) {
                  throw new IllegalArgumentException("Cannot load file");
                }
              };
            });
    knownCommands.put("save",
            sc -> {
              String fileName = sc.next();
              String imgName = sc.next();
              return model -> {
                ImageState img = model.getImage(imgName);
                try (FileWriter file = new FileWriter(fileName);
                     BufferedWriter writer = new BufferedWriter(file);
                     PrintWriter print = new PrintWriter(writer)) {
                  print.append(toPPM(img));
                } catch (IOException e) {
                  throw new IllegalArgumentException("Cannot save file");
                }
              };
            });
    knownCommands.put("red-component",
            sc -> model -> model.runCommand(new RedGrayscale(), sc.next(), sc.next()));
    knownCommands.put("green-component",
            sc -> model -> model.runCommand(new GreenGrayscale(), sc.next(), sc.next()));
    knownCommands.put("blue-component",
            sc -> model -> model.runCommand(new BlueGrayscale(), sc.next(), sc.next()));
    knownCommands.put("value-component",
            sc -> model -> model.runCommand(new ValueGrayscale(), sc.next(), sc.next()));
    knownCommands.put("luma-component",
            sc -> model -> model.runCommand(new LumaGrayscale(), sc.next(), sc.next()));
    knownCommands.put("intensity-component",
            sc -> model -> model.runCommand(new IntensityGrayscale(), sc.next(), sc.next()));
    knownCommands.put("horizontal-flip",
            sc -> model -> model.runCommand(new HorizontalFlip(), sc.next(), sc.next()));
    knownCommands.put("vertical-flip",
            sc -> model -> model.runCommand(new VerticalFlip(), sc.next(), sc.next()));
    knownCommands.put("brighten",
            sc -> model -> model.runCommand(new Brighten(getInt(sc)), sc.next(), sc.next()));
  }

  @Override
  public void control() throws IllegalStateException {
    Scanner sc = new Scanner(input);
    boolean quit = false;

    // print the welcome message
    this.welcomeMessage();

    while (!quit) { //continue until the user quits
      writeMessage("Type instruction:"); //prompt for the instruction name
      String userInstruction;
      try {
        userInstruction = sc.next(); //take an instruction name
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("No Inputs left");
      }
      if (userInstruction.equals("quit") || userInstruction.equals("q")) {
        quit = true;
      } else if (userInstruction.equals("menu")) {
        this.printMenu();
      } else {
        processCommand(userInstruction, sc, model);
      }
    }

    //after the user has quit, print farewell message
    this.farewellMessage();

  }

  /**
   * Parse an image file in the PPM format and create a 2D array of
   * arrays of size 3 (representing rgb values).
   *
   * @param file the readable that we parse
   */
  private void parsePPM(Readable file, String name, ImageProcessingModel model) {
    Scanner sc = new Scanner(file);
    StringBuilder lines = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        lines.append(s).append(System.lineSeparator());
      }
    }
    sc = new Scanner(lines.toString());

    if (!sc.next().equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }

    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    RGBColor[][] pane = new RGBColor[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        pane[row][col] = new RGBColor(r, g, b);
      }
    }
    model.createImage(pane, name, maxValue);
  }

  private String toPPM(ImageState img) {
    StringBuilder lines = new StringBuilder();
    lines.append("P3").append(System.lineSeparator());
    lines.append(img.getWidth()).append(" ");
    lines.append(img.getHeight()).append(System.lineSeparator());
    lines.append(255).append(System.lineSeparator());

    for (int row = 0; row < img.getHeight(); row += 1) {
      for (int col = 0; col < img.getWidth(); col += 1) {
        RGBColor c = img.getColorAt(row, col);
        lines.append(c.getRed()).append(System.lineSeparator());
        lines.append(c.getGreen()).append(System.lineSeparator());
        lines.append(c.getBlue()).append(System.lineSeparator());
      }
    }
    return lines.toString();
  }

  private void processCommand(String userInstruction, Scanner sc, ImageProcessingModel model)
          throws IllegalStateException {
    Function<Scanner, ImageProcessingCommand> cmd =
            knownCommands.getOrDefault(userInstruction, null);
    if (cmd == null) {
      this.writeMessage("Unknown command, please try again");
    } else {
      try {
        ImageProcessingCommand c = cmd.apply(sc);
        c.run(model);
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("Readable ran out of inputs");
      } catch (IllegalArgumentException e) {
        this.writeMessage("Command failed: " + e.getMessage());
      }
    }
  }

  private void writeMessage(String message) throws IllegalStateException {
    try {
      output.append(message).append(System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private int getInt(Scanner sc) throws IllegalArgumentException {
    try {
      return sc.nextInt();
    } catch (NoSuchElementException e) {
      throw new IllegalArgumentException("Invalid integer");
    }
  }

  private void welcomeMessage() throws IllegalStateException {
    writeMessage("Welcome to the Image Processing program!");
    printMenu();
  }

  private void printMenu() throws IllegalStateException {
    writeMessage("Supported user instructions are: ");
    writeMessage("load image-path image-name "
            + "(Loads an image from the specified path and refers to it henceforth in the program "
            + "by the given name)");
    writeMessage("save image-path image-name "
            + "(Saves the image with the given name to the specified path which includes "
            + "the name of the file)");
    writeMessage("(component name)-component image-name dest-image-name "
            + "(Create a greyscale image with the (component name) component of the image with "
            + "the given name."
            + " [supported (component name): red, green, blue, value, luma, intensity])");
    writeMessage("horizontal-flip image-name dest-image-name "
            + "(Flip an image horizontally to create a new image, "
            + "referred to henceforth by the given destination name)");
    writeMessage("vertical-flip image-name dest-image-name "
            + "(Flip an image vertically to create a new image, "
            + "referred to henceforth by the given destination name)");
    writeMessage("brighten increment image-name dest-image-name "
            + "(Brighten the image by the given increment to create a new image, referred to "
            + "henceforth by the given destination name - the increment may be positive "
            + "(brightening) or negative (darkening))");
    writeMessage("menu (Print supported instruction list)");
    writeMessage("q or quit (quit the program) ");
  }

  private void farewellMessage() throws IllegalStateException {
    writeMessage("Thank you for using this program!");
  }
}