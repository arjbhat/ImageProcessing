package view;

/**
 * Represents something that can determine what to do from what the user inputs.
 */
public interface Features {
  /**
   * Load an image from a given file to a given name.
   *
   * @param filePath the path to the file
   * @param imgName  the name for the image
   */
  void load(String filePath, String imgName);

  /**
   * Saves the current image into a file on the user's computer.
   *
   * @param filePath the file that the user wants to save the image to
   */
  void save(String filePath);

  /**
   * Select the image that we want to work on.
   *
   * @param name of the image that we want to work on
   */
  void select(String name);

  /**
   * Run a command on the image that we're working on and give it a new name.
   *
   * @param commandName the name of the command
   * @param newName     the new name of the image
   */
  void runCommand(String commandName, String newName);

  /**
   * Run the brightness command on the current image and give it a new name.
   *
   * @param newName   the new name of the image
   * @param increment the brightness factor
   */
  void runBrightness(String newName, int increment);

  /**
   * Run the downscale command on the current image and give it a new name.
   *
   * @param newName the new name of the image
   * @param height  the height of the new image
   * @param width   the width of the new image
   */
  void runDownscale(String newName, int height, int width);
}
