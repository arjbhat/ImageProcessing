package view;

public interface Features {
  /**
   * Given a proper file path and an image name for the new image, an image will be loaded
   * onto the model by the feature controller.
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
   * @param name
   */
  void select(String name);

  void runCommand(String commandName, String newName);

  void runBrightness(String newName, int increment);

  void runDownscale(String newName, int height, int width);
}
