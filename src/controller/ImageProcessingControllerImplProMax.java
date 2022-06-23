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
 * An image processing controller with additional image manipulation commands (for size, downscale,
 * and those for mask partial manipulations). This controller is also used by the GUI for features.
 * When used by the GUI - it is an asynchronous controller. (Otherwise it is synchronous)
 */
public class ImageProcessingControllerImplProMax extends ImageProcessingControllerImplPro
    implements Features {
  private final ImageProcessingGUI view;
  private String currentImage;

  /**
   * In order to construct an image processing controller we need a model, readable, and a view.
   * This constructor is used by the text view.
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

  /**
   * Construct the controller in GUI mode, which will exit from the scripting as soon as
   * possible if control is run, and listen to events from the GUI.
   *
   * @param model the Image Processing model that we work on
   * @param view  the view that we relay information with
   * @throws IllegalArgumentException if the model or view is null.
   */
  public ImageProcessingControllerImplProMax(ImageProcessingModel model, ImageProcessingGUI view)
      throws IllegalArgumentException {
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

    // A command that partially brightens an image, given the mask
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
    // partially find the red component of an image, given the mask
    makeMasked("red-component", () -> new Component(Color::getRed),
        "Partially red-component image created.");
    // partially find the green component of an image, given the mask
    makeMasked("green-component", () -> new Component(Color::getGreen),
        "Partially green-component image created.");
    // partially find the blue component of an image, given the mask
    makeMasked("blue-component", () -> new Component(Color::getBlue),
        "Partially blue-component image created.");
    // partially find the value component of an image, given the mask
    makeMasked("value-component", () -> new Component(Color::getValue),
        "Partially value-component image created.");
    // partially find the luma component of an image, given the mask
    makeMasked("luma-component", () -> new Component(Color::getLuma),
        "Partially luma-component image created.");
    // partially find the intensity component of an image, given the mask
    makeMasked("intensity-component", () -> new Component(Color::getIntensity),
        "Partially intensity-component image created.");
    // partially horizontally flip an image, given the mask
    makeMasked("horizontal-flip", HorizontalFlip::new,
        "Partially horizontally flipped image created.");
    // partially vertically flip an image, given the mask
    makeMasked("vertical-flip", VerticalFlip::new,
        "Partially vertically flipped image created.");
    // partially blur an image, given the mask
    makeMasked("blur", Blur::new,
        "Partially blurred image created.");
    // partially sharpen an image, given the mask
    makeMasked("sharpen", Sharpen::new,
        "Partially sharpened image created.");
    // partially greyscale an image, given the mask
    makeMasked("greyscale", Greyscale::new,
        "Partially greyscale image created.");
    // partially sepia an image, given the mask
    makeMasked("sepia", Sepia::new,
        "Partially sepia image created.");
  }

  // helper for masked commands (with no arguments other than a new name)
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
        "(Create a smaller version of an image)");
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
    list.add("size image-name " + "(get the dimensions of an image)");
    return list;
  }

  @Override
  public void load(String filePath, String imgName) {
    imgName = getName(imgName);
    if (imgName == null) {
      return;
    }
    try {
      this.loadImg(model, filePath, imgName);
      view.selectImage(imgName, toBufferedImage(imgName));
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
    }
    this.currentImage = imgName;
  }

  private String getName(String imgName) {
    this.assertGUIMode();
    if (imgName == null || imgName.isBlank()) {
      imgName = currentImage;
    }
    if (imgName == null || imgName.contains(" ")) {
      view.displayError("Name cannot contain spaces.");
      return null;
    }
    return imgName;
  }

  @Override
  public void save(String filePath) {
    this.assertGUIMode();
    try {
      this.saveImg(model, filePath, currentImage);
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
    }
  }

  @Override
  public void select(String name) {
    this.assertGUIMode();
    if (name == null) {
      throw new IllegalArgumentException("Trying to select a null name.");
    }
    try {
      view.selectImage(name, toBufferedImage(name));
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
      return;
    }
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
    this.assertGUIMode();
    newName = getName(newName);
    if (newName == null) {
      return;
    }
    try {
      this.runCommand(commandName, new Scanner(args + " " + newName), model);
      view.selectImage(newName, toBufferedImage(newName));
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
      return;
    }
    this.currentImage = newName;
  }

  private void assertGUIMode() {
    if (view == null) {
      throw new IllegalStateException("GUI method called in text mode.");
    }
  }

  private BufferedImage toBufferedImage(String name) {
    ImageState img = model.getImage(name);
    BufferedImage buff = new BufferedImage(img.getWidth(), img.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    toImage(img, buff);
    return buff;
  }
}
