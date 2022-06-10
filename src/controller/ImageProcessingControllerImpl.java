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

import model.Color;
import model.ImageProcessingModel;
import model.ImageState;
import model.RGBColor;
import model.macros.Brighten;
import model.macros.Grayscale;
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

  /**
   * In order to construct an image processing controller we need a model, readable, and a view.
   *
   * @param model  the Image Processing model that we work on
   * @param input  the readable (instructions for the model)
   * @param output the view that we relay information with
   * @throws IllegalArgumentException if the model, readable, or view are null
   */
  public ImageProcessingControllerImpl(ImageProcessingModel model, ImageProcessingView output,
                                       Readable input)
      throws IllegalArgumentException {
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
    knownCommands = new HashMap<>();
    // that we load commands into
    this.loadCommands();
  }

  // loads all the commands that the model can be operated on with
  // this makes it easy for the user to add features in the future
  private void loadCommands() {
    // Command to load the file in the model
    knownCommands.put("load",
        sc -> {
          String fileName = sc.next();
          String imgName = sc.next();
          return model -> {
            try (FileReader file = new FileReader(fileName)) {
              this.parsePPM(file, imgName, model);
              this.writeMessage("File is loaded.");
            } catch (IOException e) {
              throw new IllegalArgumentException("Cannot load file.");
            }
          };
        });
    // Command to save the file from the model
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
              this.writeMessage("File is saved.");
            } catch (IOException e) {
              throw new IllegalArgumentException("Cannot save file.");
            }
          };
        });
    // A command that changes an image to its red-grayscale representation
    knownCommands.put("red-component",
        sc -> model -> {
          model.runCommand(new Grayscale(Color::getRed), sc.next(), sc.next());
          this.writeMessage("Red-component image created.");
        });
    // A command that changes an image to its green-grayscale representation
    knownCommands.put("green-component",
        sc -> model -> {
          model.runCommand(new Grayscale(Color::getGreen), sc.next(), sc.next());
          this.writeMessage("Green-component image created.");
        });
    // A command that changes an image to its blue-grayscale representation
    knownCommands.put("blue-component",
        sc -> model -> {
          model.runCommand(new Grayscale(Color::getBlue), sc.next(), sc.next());
          this.writeMessage("Blue-component image created.");
        });
    // A command that changes an image to its value-grayscale representation
    knownCommands.put("value-component",
        sc -> model -> {
          model.runCommand(new Grayscale(Color::getValue), sc.next(), sc.next());
          this.writeMessage("Value-component image created.");
        });
    // A command that changes an image to its luma-grayscale representation
    knownCommands.put("luma-component",
        sc -> model -> {
          model.runCommand(new Grayscale(Color::getLuma), sc.next(), sc.next());
          this.writeMessage("Luma-component image created.");
        });
    // A command that changes an image to its intensity-grayscale representation
    knownCommands.put("intensity-component",
        sc -> model -> {
          model.runCommand(new Grayscale(Color::getIntensity), sc.next(), sc.next());
          this.writeMessage("Intensity-component image created.");
        });
    // A command that changes an image to be horizontally flipped
    knownCommands.put("horizontal-flip",
        sc -> model -> {
          model.runCommand(new HorizontalFlip(), sc.next(), sc.next());
          this.writeMessage("Horizontally flipped image created.");
        });
    // A command that changes an image to be vertically flipped
    knownCommands.put("vertical-flip",
        sc -> model -> {
          model.runCommand(new VerticalFlip(), sc.next(), sc.next());
          this.writeMessage("Vertically flipped image created.");
        });
    // A command that changes an image to be brightened or darkened
    knownCommands.put("brighten",
        sc -> model -> {
          model.runCommand(new Brighten(getInt(sc)), sc.next(), sc.next());
          this.writeMessage("Brightness changed image created.");
        });
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
  private void parsePPM(Readable file, String name, ImageProcessingModel model) {
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
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }

    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    Color[][] pane = new Color[height][width];

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

  // Converts an ImageState to PPM format so that it can be written to a file
  private String toPPM(ImageState img) {
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
  private void writeMessage(String message) throws IllegalStateException {
    try {
      output.renderMessage(message + System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  // Throws an exception if the scanner input is not an integer
  private int getInt(Scanner sc) throws IllegalArgumentException {
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
    writeMessage(" ➤ load image-path image-name "
        + "(Loads an image from the specified path and refers to it henceforth in the program "
        + "by the given name)");
    writeMessage(" ➤ save image-path image-name "
        + "(Saves the image with the given name to the specified path which includes "
        + "the name of the file)");
    writeMessage(" ➤ (component name)-component image-name dest-image-name "
        + "(Create a greyscale image with the (component name) component of the image with "
        + "the given name."
        + " [supported (component name): red, green, blue, value, luma, intensity])");
    writeMessage(" ➤ horizontal-flip image-name dest-image-name "
        + "(Flip an image horizontally to create a new image, "
        + "referred to henceforth by the given destination name)");
    writeMessage(" ➤ vertical-flip image-name dest-image-name "
        + "(Flip an image vertically to create a new image, "
        + "referred to henceforth by the given destination name)");
    writeMessage(" ➤ brighten increment image-name dest-image-name "
        + "(Brighten the image by the given increment to create a new image, referred to "
        + "henceforth by the given destination name - the increment may be positive "
        + "(brightening) or negative (darkening))");
    writeMessage(" ➤ menu (Print supported instruction list)");
    writeMessage(" ➤ q or quit (quit the program)");
  }

  private void farewellMessage() throws IllegalStateException {
    writeMessage("Thank you for using this program! (っ◕‿◕)っ");
  }
}
