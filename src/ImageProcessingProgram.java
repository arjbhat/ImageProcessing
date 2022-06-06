
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
    String filename;

    if (args.length>0) {
      filename = args[0];
    }
    else {
      filename = "sample.ppm";
    }

    ImageUtil.readPPM(filename);
  }
}
