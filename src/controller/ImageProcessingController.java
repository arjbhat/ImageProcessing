package controller;

/**
 * Represents an image processing Controller that relays information to the model and then to
 * the client (via the view). The job of the controller is to run the whole operation by parsing the
 * input, sending comprehensible information/operations to the model, and then feedback to the view.
 * Specific implementations have more features.
 */
public interface ImageProcessingController {

  /**
   * Controls the whole program until the user quits and performs specified operations from
   * the predefined known commands. This program only reads inputs one line at a time - limiting
   * any unknown bugs or unexpected behavior.
   *
   * @throws IllegalStateException only if the controller is unable to successfully transmit output
   *                               to the appendable or read from the readable (runs out of inputs).
   */
  void control() throws IllegalStateException;
}
