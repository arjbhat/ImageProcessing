import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImplPro;
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
  public static void main(String[] args) throws IOException {
    FileReader file = null;
    for (int i = 0; i < args.length; i++) {
      if ("-file".equals(args[i]) && args.length > i + 1) {
        String fileName = args[++i];
        file = new FileReader(fileName);
      }
    }

    ImageProcessingModel model = new ImageProcessingModelImpl();
    Readable input = file == null ? new InputStreamReader(System.in) : file;
    ImageProcessingView output = new ImageProcessingViewImpl();
    ImageProcessingController controller =
        new ImageProcessingControllerImplPro(model, output, input);
    controller.control();
    if (file != null) {
      file.close();
    }
  }
}
