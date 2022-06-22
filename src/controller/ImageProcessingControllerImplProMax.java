package controller;

import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

import model.ImageProcessingModel;
import model.ImageState;
import model.macros.Downscale;
import model.macros.Mask;
import model.macros.Sepia;
import view.Features;
import view.ImageProcessingGUI;
import view.ImageProcessingView;

/**
 * An image processing controller with additional image manipulation commands.
 */
public class ImageProcessingControllerImplProMax extends ImageProcessingControllerImplPro
    implements Features {
  private final ImageProcessingGUI view;
  private String currentImage;

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
    this.view = null;
  }

  public ImageProcessingControllerImplProMax(ImageProcessingModel model, ImageProcessingGUI view) {
    super(model, view, new StringReader("q"));
    this.view = view;
    view.addFeatures(this);
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
          model.runCommand(new Downscale(getInt(sc),
                  getInt(sc)),
              sc.next(),
              sc.next());
          this.writeMessage("Downscaled image created.");
        });
    // TODO: Comment also add masking commands
//    addCommand("sepia",
//        sc -> model -> {
//          String imgName = sc.next();
//          String maskName = sc.next();
//          model.runCommand(new Mask(new Sepia(), model.getImage(maskName)), imgName, sc.next());
//          this.writeMessage("Sepia image created.");
//        });
  }

  @Override
  protected List<String> loadMenu() {
    List<String> list = super.loadMenu();
    list.add("downscale dest-image-height dest-image-width image-name dest-image-name");
    list.add("size image-name");
    return list;
  }

  @Override
  public void load(String filePath, String imgName) {
    this.doCommand("load", imgName, filePath);
  }

  @Override
  public void save(String filePath) {
    try {
      this.runCommand("save", new Scanner(filePath + " " + currentImage), model);
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
    }
  }

  @Override
  public void select(String name) {
    view.selectImage(name, toBufferedImage(name));
    this.currentImage = name;
  }

  @Override
  public void runCommand(String commandName, String newName) {
    this.doCommand(commandName, newName, currentImage);
  }

  @Override
  public void runBrightness(String newName, int increment) {
    this.doCommand("brighten", newName, increment + " " + currentImage);
  }

  @Override
  public void runDownscale(String newName, int height, int width) {
    this.doCommand("downscale", newName, height + " " + width + " " + currentImage);
  }

  private void doCommand(String commandName, String newName, String args) {
    if (newName.isBlank()) {
      newName = currentImage;
    }
    if (newName == null || newName.contains(" ")) {
      view.displayError("Name cannot contain spaces.");
      return;
    }
    try {
      this.runCommand(commandName, new Scanner(args + " " + newName), model);
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
      return;
    }
    view.addImage(newName);
    view.selectImage(newName, toBufferedImage(newName));
    this.currentImage = newName;
  }

  private BufferedImage toBufferedImage(String name) {
    ImageState img = model.getImage(name);
    BufferedImage buff = new BufferedImage(img.getWidth(), img.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    toImage(img, buff);
    return buff;
  }
}
