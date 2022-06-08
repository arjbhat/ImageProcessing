package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import model.ImageProcessingModel;
import model.macros.BlueGrayscale;
import model.macros.Brightness;
import model.macros.GreenGrayscale;
import model.macros.HorizontalFlip;
import model.macros.IntensityGrayscale;
import model.macros.LumaGrayscale;
import model.macros.RedGrayscale;
import model.macros.ValueGrayscale;

/**
 * Implementation of the ImageProcessingController that works with the file passed in and
 * the model and the view in order to process the commands that the user passed in.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {
  private final Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;

  ImageProcessingControllerImpl() {
    knownCommands = new HashMap<>();
    this.loadCommands();
  }

  private void loadCommands() {
    knownCommands.put("load",
            sc -> {
              final FileReader file;
              try {
                file = new FileReader(sc.next());
              } catch (FileNotFoundException e) {
                return img -> {
                };
              }
              return img -> img.createImage(parse(file), sc.next());
            });
    knownCommands.put("save", sc -> new Save(sc.next(), sc.next()));
    knownCommands.put("red-component",
            sc -> img -> img.runCommand(new RedGrayscale(), sc.next(), sc.next()));
    knownCommands.put("green-component",
            sc -> img -> img.runCommand(new GreenGrayscale(), sc.next(), sc.next()));
    knownCommands.put("blue-component",
            sc -> img -> img.runCommand(new BlueGrayscale(), sc.next(), sc.next()));
    knownCommands.put("value-component",
            sc -> img -> img.runCommand(new ValueGrayscale(), sc.next(), sc.next()));
    knownCommands.put("intensity-component",
            sc -> img -> img.runCommand(new IntensityGrayscale(), sc.next(), sc.next()));
    knownCommands.put("luma-component",
            sc -> img -> img.runCommand(new LumaGrayscale(), sc.next(), sc.next()));
    knownCommands.put("horizontal-flip",
            sc -> img -> img.runCommand(new HorizontalFlip(), sc.next(), sc.next()));
    knownCommands.put("vertical-flip",
            sc -> img -> img.runCommand(new HorizontalFlip(), sc.next(), sc.next()));
    knownCommands.put("brighten",
            sc -> img -> img.runCommand(new Brightness(sc.nextInt()), sc.next(), sc.next()));
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