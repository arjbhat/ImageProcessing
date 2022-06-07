package controller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Implementation of the ImageProcessingController that works with the file passed in and
 * the model and the view in order to process the commands that the user passed in.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {
  private final Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;

  /**
   * Parse an image file in the PPM format and create a 2D array of
   * arrays of size 3 (representing rgb values).
   *
   * @param file the readable that we parse
   */
  private int[][][] parse(Readable file) {
    Scanner sc = new Scanner(file);
    
  }

  @Override
  protected void processCommand(String userInstruction, Scanner sc, ImageProcessingModel model) {
    Function<Scanner, ImageProcessingCommand> cmd =
            knownCommands.getOrDefault(userInstruction, null);
    if (cmd == null) {
      super.processCommand(userInstruction, sc, model);
    } else {
      ImageProcessingCommand c = cmd.apply(sc);
      c.run(model);
    }
  }
}
