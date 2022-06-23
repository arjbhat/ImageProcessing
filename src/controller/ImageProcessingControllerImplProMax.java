package controller;

import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

import model.Color;
import model.ImageProcessingModel;
import model.ImageState;
import model.macros.Blur;
import model.macros.Brighten;
import model.macros.Component;
import model.macros.Downscale;
import model.macros.Greyscale;
import model.macros.HorizontalFlip;
import model.macros.Macro;
import model.macros.Mask;
import model.macros.Sepia;
import model.macros.Sharpen;
import model.macros.VerticalFlip;
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
    Function<Scanner, ImageProcessingCommand> brighten = this.getCommand("brighten");
    addCommand("brighten",
        sc -> model -> {
          int inc = getInt(sc);
          String sourceImg = sc.next();
          String maskImg = sc.next();
          if (!sc.hasNext()) {
            brighten.apply(new Scanner(inc + " " + sourceImg + " " + maskImg)).run(model);
            return;
          }
          model.runCommand(new Mask(new Brighten(inc), model.getImage(maskImg)), sourceImg,
              sc.next());
          this.writeMessage("Partially brightness changed image created.");
        });
    makeMasked("red-component", () -> new Component(Color::getRed),
        "Partially red-component image created.");
    makeMasked("green-component", () -> new Component(Color::getGreen),
        "Partially green-component image created.");
    makeMasked("blue-component", () -> new Component(Color::getBlue),
        "Partially blue-component image created.");
    makeMasked("value-component", () -> new Component(Color::getValue),
        "Partially value-component image created.");
    makeMasked("luma-component", () -> new Component(Color::getLuma),
        "Partially luma-component image created.");
    makeMasked("intensity-component", () -> new Component(Color::getIntensity),
        "Partially intensity-component image created.");
    makeMasked("horizontal-flip", HorizontalFlip::new,
        "Partially horizontally flipped image created");
    makeMasked("vertical-flip", VerticalFlip::new,
        "Partially vertically flipped image created");
    makeMasked("blur", Blur::new,
        "Partially blurred image created");
    makeMasked("sharpen", Sharpen::new,
        "Partially sharpened image created");
    makeMasked("greyscale", Greyscale::new,
        "Partially greyscale image created");
    makeMasked("sepia", Sepia::new,
        "Partially sepia image created");
  }

  private void makeMasked(String name, Supplier<Macro> makeMacro, String successMessage) {
    Function<Scanner, ImageProcessingCommand> oldCommand = this.getCommand(name);
    addCommand(name,
        sc -> model -> {
          String sourceImg = sc.next();
          String maskImg = sc.next();
          if (!sc.hasNext()) {
            oldCommand.apply(new Scanner(sourceImg + " " + maskImg)).run(model);
            return;
          }
          model.runCommand(new Mask(makeMacro.get(), model.getImage(maskImg)),
              sourceImg, sc.next());
          this.writeMessage(successMessage);
        });
  }

  @Override
  protected List<String> loadMenu() {
    List<String> list = super.loadMenu();
    list.add("downscale dest-image-height dest-image-width image-name dest-image-name " +
        "(Create a smaller version of an image).");
    list.add("(component name)-component image-name mask-name dest-image-name "
        + "(Create a partially greyscale image with the (component name) component of the image "
        + "with the given name."
        + " [supported (component name): red, green, blue, value, luma, intensity])");
    list.add("horizontal-flip image-name mask-name dest-image-name "
        + "(Partially flip an image horizontally to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("vertical-flip image-name mask-name dest-image-name "
        + "(Partially flip an image vertically to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("brighten increment image-name mask-name dest-image-name "
        + "(Partially brighten the image by the given increment to create a new image, referred to "
        + "henceforth by the given destination name - the increment may be positive "
        + "(brightening) or negative (darkening))");
    list.add("blur image-name mask-name dest-image-name "
        + "(Partially blur an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("sharpen image-name mask-name dest-image-name "
        + "(Partially sharpen an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("greyscale image-name mask-name dest-image-name "
        + "(Find the partially greyscale version of an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("sepia image-name mask-name dest-image-name "
        + "(Find the partially sepia version of an image to create a new image, "
        + "referred to henceforth by the given destination name)");
    list.add("size image-name " + "(get the dimensions of an image).");
    return list;
  }

  @Override
  public void load(String filePath, String imgName) {
    this.doCommand("load", imgName, filePath);
  }

  @Override
  public void save(String filePath) {
    if (view == null) {
      throw new IllegalStateException("GUI method called in text mode.");
    }
    try {
      this.runCommand("save", new Scanner(filePath + " " + currentImage), model);
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
    }
  }

  @Override
  public void select(String name) {
    if (view == null) {
      throw new IllegalStateException("GUI method called in text mode.");
    }
    if (name == null) {
      throw new IllegalArgumentException("Trying to select a null name.");
    }
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
    this.doCommand("downscale", newName, height + " " + width + " "
        + currentImage);
  }

  private void doCommand(String commandName, String newName, String args) {
    if (view == null) {
      throw new IllegalStateException("GUI method called in text mode.");
    }
    if (newName == null || newName.isBlank()) {
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
