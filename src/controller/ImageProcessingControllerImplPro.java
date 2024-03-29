package controller;

import java.util.List;

import model.ImageProcessingModel;
import model.macros.Blur;
import model.macros.Greyscale;
import model.macros.Sepia;
import model.macros.Sharpen;
import view.ImageProcessingView;

/**
 * An image processing controller with additional image manipulation commands. These include -
 * blur, sharpen, greyscale, and sepia.
 */
public class ImageProcessingControllerImplPro extends ImageProcessingControllerImpl {
  /**
   * In order to construct an image processing controller we need a model, readable, and a view.
   *
   * @param model  the Image Processing model that we work on
   * @param output the view that we relay information with
   * @param input  the readable (instructions for the model)
   * @throws IllegalArgumentException if the model, readable, or view are null
   */
  public ImageProcessingControllerImplPro(ImageProcessingModel model, ImageProcessingView output,
                                          Readable input) throws IllegalArgumentException {
    super(model, output, input);
  }

  @Override
  protected void loadCommands() {
    super.loadCommands();
    // A command that changes an image to its blurred version
    addCommand("blur",
        sc -> model -> {
          model.runCommand(new Blur(), sc.next(), sc.next());
          this.writeMessage("Blurred image created.");
        });
    // A command that changes an image to its sharpened version
    addCommand("sharpen",
        sc -> model -> {
          model.runCommand(new Sharpen(), sc.next(), sc.next());
          this.writeMessage("Sharpened image created.");
        });
    // A command that changes an image to its greyscale version
    addCommand("greyscale",
        sc -> model -> {
          model.runCommand(new Greyscale(), sc.next(), sc.next());
          this.writeMessage("Greyscale image created.");
        });
    // A command that changes an image to its sepia version
    addCommand("sepia",
        sc -> model -> {
          model.runCommand(new Sepia(), sc.next(), sc.next());
          this.writeMessage("Sepia image created.");
        });
  }

  @Override
  protected List<String> loadMenu() {
    List<String> list = super.loadMenu();
    list.add("blur image-name dest-image-name "
        + "(Blur an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("sharpen image-name dest-image-name "
        + "(Sharpen an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("greyscale image-name dest-image-name "
        + "(Find the greyscale version of an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("sepia image-name dest-image-name "
        + "(Find the sepia version of an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    return list;
  }
}
