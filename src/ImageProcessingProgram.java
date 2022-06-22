import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImplProMax;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;
import view.ImageProcessingGUI;
import view.ImageProcessingGUIFrame;
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
    boolean gui = true;
    for (int i = 0; i < args.length; i++) {
      if ("-file".equals(args[i]) && args.length > i + 1) {
        String fileName = args[++i];
        file = new FileReader(fileName);
      } else if ("-text".equals(args[i])) {
        gui = false;
      }
    }
    if (file != null) {
      gui = false;
    }

    ImageProcessingModel model = new ImageProcessingModelImpl();
    Readable input = file == null ? new InputStreamReader(System.in) : file;
    ImageProcessingView output = new ImageProcessingViewImpl();
    ImageProcessingGUI view = new ImageProcessingGUIFrame();
    ImageProcessingController controller;
    if (gui) {
      controller = new ImageProcessingControllerImplProMax(model, view);
    } else {
      controller = new ImageProcessingControllerImplProMax(model, output, input);
    }
    controller.control();
    if (file != null) {
      file.close();
    }
  }
}
