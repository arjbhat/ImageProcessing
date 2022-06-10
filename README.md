## Image Processing (Assignment 4)

Names: Arjun Bhat & Alexander Chang-Davidson

### Overview (running the program):

The program's main method is in `src/ImageProcessingProgram.java`
and does not take any command line arguments. Users interact with the program
by typing commands in the console with one command per line. The user may view
available commands and their syntax at any point with the `menu` command.
There is an example script in the `script.txt` file which can be used by running the
program, and pasting the contents of the file either line by line, or all at once in the
console to the system input. The provided sample images are in the `res/` folder.

### Design:

The code (besides the main class for the program) is split into 3 packages: model, view,
and controller. The view and controller structure is mostly self-explanatory, with details
provided below. The model consists of the model, which manages many images, which are
made of a grid of colors. The logic for image operations is mostly handled by macros
such as Grayscale and Brighten. The controller makes use of the command design pattern
with the commands stored internally rather than declared as separate classes.
The UML diagram is provided in the `UMLDiagram.png` file.

- View:
    - (interface) ImageProcessingView: This represents a view that can be used by the
      controller. It renders a message to an output when the controller instructs it to.
    - (class) ImageProcessingViewImpl: This is a generic view implementation that prints
      messages exactly as is, defaulting to using the system output.
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
    - (class) Grayscale: This represents a function transforming an image to a grayscale of
      one of its components Specified by a function from a color to its grayscale value.
    - (class) Brighten: This represents a function transforming an image to a brighter or
      darker version of the image depending on the value to change the brightness by.
    - (class) HorizontalFlip: This represents a function transforming an image into its
      horizontally flipped counterpart.
    - (class) VerticalFlip: This represents a function transforming an image into its
      vertically flipped counterpart.
- Controller:
    - (interface) ImageProcessingController: This represents a controller that accepts
      user input to run an image processing model, displaying output to a given view.
    - (interface) ImageProcessingCommand: This represents a function to handle inputs
      for an individual command, and pass the values to the model to properly run the command.
    - (class) ImageProcessingControllerImpl: This class controls the model from inputs
      taken from a readable and handles commands line by line.

### Citation:

All images named with a prefix of "arjun" are derivative of the arjun.ppm image.
The arjun.ppm image was taken by Arjun Bhat, and is authorized for use in this project.
The img1.ppm image was manually crafted line by line by Arjun Bhat and Alexander Chang-Davidson,
and is also authorized for use in this project.
