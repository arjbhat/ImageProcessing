import java.io.InputStreamReader;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import view.ImageProcessingView;
import view.ImageProcessingViewImpl;

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
    ImageProcessingView output = new ImageProcessingViewImpl();
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, output, input);
    controller.control();
  }
}
