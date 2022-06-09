import java.io.InputStreamReader;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;

/**
 * The image processing program that is used to manipulate images.
 */
public class ImageProcessingProgram {
  /**
   * The main method of the image processing program.
   *
   * @param args Arguments passed from the commandline
   */
  public static void main(String[] args) {
    ImageProcessingModel model = new ImageProcessingModelImpl();
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, input, output);
    controller.control();
  }
}
