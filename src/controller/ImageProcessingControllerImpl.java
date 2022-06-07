package controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import model.ImageProcessingModel;

/**
 * Implementation of the ImageProcessingController that works with the file passed in and
 * the model and the view in order to process the commands that the user passed in.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {
  private final Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;

  ImageProcessingControllerImpl() {
    knownCommands = new HashMap<>();
  }

  /**
   * Parse an image file in the PPM format and create a 2D array of
   * arrays of size 3 (representing rgb values).
   *
   * @param file the readable that we parse
   */
  private int[][][] parse(Readable file) {
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

    int[][][] img = new int[height][width][3];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        img[i][j] = new int[]{r, g, b};
      }
    }
    return img;
  }

  protected void processCommand(String userInstruction, Scanner sc, ImageProcessingModel model) {
    Function<Scanner, ImageProcessingCommand> cmd =
            knownCommands.getOrDefault(userInstruction, null);
    if (cmd == null) {
      throw new IllegalArgumentException("");
    } else {
      ImageProcessingCommand c = cmd.apply(sc);
      c.run(model);
    }
  }
}