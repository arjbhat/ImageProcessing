## Image Processing (Assignment 4)

Names: Arjun Bhat & Alexander Chang-Davidson

### Overview (running the program):

The program's main method is in `src/ImageProcessingProgram.java`. The jar file can be found in
`res/imgProcessAssign6.jar` and optionally takes a `-file` command line argument with the file
to read the script from immediately after. It also optionally takes a `-text` command line
argument to run in text mode interact with the program by typing commands in the console
with one command per line. All other command line arguments are currently ignored.
When run without specifying a file, users interact via the GUI screen that opens.
The user may view available commands and their syntax at any point with the `menu` command in
text mode, or via the command list dropdown in GUI mode. There is an example script in
the `script.txt` file which can be used by running
`java -jar imgProcessAssign5.java -file script.txt` in the `res/` folder.
The provided sample images are in the `res/` folder.

### Extra Credit Changes (Assignment 6)

To implement the mask and downscale operations, we only had to add the corresponding macros
and expose the features in the controller and GUI. The mask commands were added in the Pro Max
by adding commands to override the previous versions, and calling the previous version if
not enough arguments were passed for the mask version.

### Design:

The code (besides the main class for the program) is split into 3 packages: model, view,
and controller. The model consists of the model, which manages many images, which are
made of a grid of colors. The logic for image operations is mostly handled by macros
such as Greyscale and Brighten. The controller makes use of the command design pattern
with the commands stored internally rather than declared as separate classes. The Pro version
of the controller extends the previously written controller and adds extra commands. The
Pro Max version of the controller extends the Pro version and adds a couple extra commands as
well as implementing support for use with the GUI. The View has the default implementation as
just a text view, and the GUI view interface and implementation with additional methods for more
control. The features class is used to pass user input to the controller.
The UML diagram is provided in the `res/UMLDiagram.png` file and should be referenced while
reading the following descriptions for a more full understanding of the structure.

- View:
    - (interface) ImageProcessingView: This represents a view that can be used by the
      controller. It renders a message to an output when the controller instructs it to.
    - (class) ImageProcessingViewImpl: This is a generic view implementation that prints
      messages exactly as is, defaulting to using the system output.
    - (interface) ImageProcessingGUI: This represents a view that additionally allows viewing
      the image in the program and makes error messages distinct.
    - (interface) Features: This represents an object that knows how to handle user inputs.
    - (class) ImageProcessingGUIFrame: This is a GUI view that passes user inputs to a provided
      features to be handled.
- Model:
    - (interface) Color: This represents a color, regardless of the format, as long as
      all the channels can be observed.
    - (class) RGBColor: This represents a color based on the red, green, and blue channels.
      This class offers no mutation.
    - (interface) ImageState: This represents an image that can be viewed pixel by pixel.
    - (interface) ImageTransform: This represents an image that can not only be observed,
      but can also be transformed into another image.
    - (interface) ColorFunction: This represents a function from a color, row, and column
      to a color.
    - (class) Image: This represents an image from a grid of its pixels. This class offers
      no mutation, only methods to create a new image similar to the current one.
    - (class) ObservableImage: This class is an adapter for ImageStates to prevent access
      of any methods other than offered by the ImageState interface.
    - (interface) ImageProcessingState: This represents the state of an image processing
      model, and can only observe the images.
    - (interface) ImageProcessingModel: This represents an image processing program that
      allows creating images and making new images by running macros on existing images.
    - (class) ImageProcessingModelImpl: This class stores images in a map for the controller
      to run macros on as needed.
    - (interface) Macro: This represents a function to transform an image somehow.
    - (class) Component: This represents a function transforming an image to a greyscale of
      one of its components Specified by a function from a color to its grayscale value.
    - (class) Brighten: This represents a function transforming an image to a brighter or
      darker version of the image depending on the value to change the brightness by.
    - (class) HorizontalFlip: This represents a function transforming an image into its
      horizontally flipped counterpart.
    - (class) VerticalFlip: This represents a function transforming an image into its
      vertically flipped counterpart.
    - (class) Convolve: This represents a function to transform an image by filtering the colors
      around each pixel based on a given kernel.
    - (class) Blur: This represents a function to transform an image into a more blurred version.
    - (class) Sharpen: This represents a function to transform an image into a sharper version.
    - (class) MatrixTransform: This represents a function to transform an image by manipulating
      the components of the color via a matrix multiplication.
    - (class) Greyscale: This represents a function to transform an image into a greyscale of
      its brightness, aka Luma.
    - (class) Sepia: This represents a function to transform an image into a sepia version of
      its colors.
    - (class) Downscale: This represents a function to scale an image to a smaller size.
    - (class) Mask: This represents a function to apply a different macro on only part of an image.
- Controller:
    - (interface) ImageProcessingController: This represents a controller that accepts
      user input to run an image processing model, displaying output to a given view.
    - (interface) ImageProcessingCommand: This represents a function to handle inputs
      for an individual command, and pass the values to the model to properly run the command.
    - (class) ImageProcessingControllerImpl: This class controls the model from inputs
      taken from a readable and handles commands line by line.
    - (class) ImageProcessingControllerImplPro: This class controls the model from inputs
      taken from a readable and handles commands line by line. It has the blur, sharpen, sepia,
      and greyscale commands that the previous controller did not have.
    - (class) ImageProcessingControllerImplProMax: This class controls the model from inputs
      taken from a readable or passed from the GUI. It additionally has the downscale and size
      commands as well as mask versions of all previous image manipulation commands.

### Design Changes Assignment 4 to 5

#### Naming changes:

- The Grayscale macro was renamed to Component.
- The getTransparency method on Color was renamed getAlpha.

These changes were to increase clarity as their previous names were misleading.

#### Behavior changes:

- RGBColor no longer throws exceptions for negative components, instead clamps it to 0 as the
  minimum value.
- Image no longer throws exceptions for values outside the valid range, instead clamping it to
  within the valid range of values.
- ImageProcessingControllerImpl now supports additional file extensions, and the ability to
  comment out lines.

The RGBColor and Image changes were primarily to make the macros simpler, as most macros required
clamping before the image could be created.

The choice to add the additional file extensions to the base controller rather than the new Pro
version was because it did not make sense to restrict the base feature set to only ppm files
given how common the other file types are.

#### Bug fixes:

- Images now properly throw IllegalArgumentException if any part of the given color grid is
  null or non-rectangular.
- ImageProcessingModelImpl now throws if a macro returns null instead of an image.

#### Implementation changes:

- ImageProcessingControllerImpl made some methods protected and slightly redesigned the menu
  to accommodate for subclassing.

### Design Changes Assignment 5 to 6

None!

### Citation:

All images named with a prefix of "arjun" that look like a face are derivative of the arjun.ppm
image. The arjun.ppm image was taken by Arjun Bhat, and is authorized for use in this project.
The img1.ppm image was manually crafted line by line by Arjun Bhat and Alexander Chang-Davidson,
and is also authorized for use in this project. All other files beginning in img1 are derivative
of img1.ppm.
