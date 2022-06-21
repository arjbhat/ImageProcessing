package controller;

import java.util.List;

import model.ImageProcessingModel;
import model.ImageState;
import model.macros.Downscale;
import view.ImageProcessingView;

/**
 * An image processing controller with additional image manipulation commands.
 */
public class ImageProcessingControllerImplProMax extends ImageProcessingControllerImplPro {
  /**
   * In order to construct an image processing controller we need a model, readable, and a view.
   *
   * @param model  the Image Processing model that we work on
   * @param output the view that we relay information with
   * @param input  the readable (instructions for the model)
   * @throws IllegalArgumentException if the model, readable, or view are null
   */
  public ImageProcessingControllerImplProMax(ImageProcessingModel model, ImageProcessingView output,
                                             Readable input) throws IllegalArgumentException {
    super(model, output, input);
  }

  @Override
  protected void loadCommands() {
    super.loadCommands();
    // A command that returns the size of the image
    addCommand("size",
        sc -> model -> {
          ImageState image = model.getImage(sc.next());
          this.writeMessage("Width: " + image.getWidth() + " Height: " + image.getHeight());
        });
    // A command that downscales an image
    addCommand("downscale",
        sc -> model -> {
          model.runCommand(new Downscale(getInt(sc), getInt(sc)), sc.next(), sc.next());
          this.writeMessage("Downscaled image created.");
        });
  }

  @Override
  protected List<String> loadMenu() {
    List<String> list = super.loadMenu();
    list.add("downscale dest-image-height dest-image-width image-name dest-image-name");
    list.add("size image-name");
    return list;
  }
}
