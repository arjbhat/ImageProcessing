package view;

import java.awt.image.BufferedImage;

/**
 * Represents a more sophisticated view that handles different kinds of messages like errors
 * and can display the image. This view is asynchronous, and passes user inputs to a Features
 * object that is added to the view.
 */
public interface ImageProcessingGUI extends ImageProcessingView {
  /**
   * Change the current image selection, adds the image name to the list if not already present.
   * Display the given image as the current image.
   *
   * @param imageName the name of the image to select
   * @param img       the image to display
   */
  void selectImage(String imageName, BufferedImage img);

  /**
   * Display an error message to the user.
   *
   * @param err the message to show
   */
  void displayError(String err);

  /**
   * Add a features object to handle user inputs.
   *
   * @param f the object that knows what to do for all user inputs
   */
  void addFeatures(Features f);
}
