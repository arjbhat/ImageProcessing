package model.macros;

import model.ImageState;
import model.ImageTransform;
import model.RGBColor;

/**
 * This macro can perform any operation on only a select number of pixels (based on the mask).
 * The operations are only performed on the pixels where the mask counterpart has a black pixel.
 */
public class Mask implements Macro {
  private final Macro macro;
  private final ImageState maskImg;

  /**
   * Constructs a mask macro.
   *
   * @param macro   the operation that we wish to perform on the selection
   * @param maskImg the mask that we will decide how to operate with
   */
  public Mask(Macro macro, ImageState maskImg) {
    if (maskImg == null) {
      throw new IllegalArgumentException("Mask image cannot be null.");
    }
    if (macro == null) {
      throw new IllegalArgumentException("Macro cannot be null.");
    }
    this.macro = macro;
    this.maskImg = maskImg;
  }

  /**
   * Apply the macro constructed with to the given image and produce an image showing the new image
   * pixels in only parts where the mask image was black.
   *
   * @param img the image that we're working on
   * @return the partially transformed image
   * @throws IllegalArgumentException if the image is null or the image before or after are
   *                                  different dimensions from the mask
   */
  @Override
  public ImageTransform execute(ImageTransform img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    if (img.getHeight() != maskImg.getHeight() || img.getWidth() != maskImg.getWidth()) {
      throw new IllegalArgumentException("Mask image dimensions must be the same " +
          "as image dimensions.");
    }

    // new edited image
    ImageTransform editedImage = macro.execute(img);
    if (editedImage == null || editedImage.getHeight() != maskImg.getHeight()
        || editedImage.getWidth() != maskImg.getWidth()) {
      throw new IllegalArgumentException("Resulting image dimensions must be the same " +
          "as image dimensions.");
    }

    return img.transform((c, y, x) -> {
      // if mask is black then
      if (new RGBColor(0, 0, 0).equals(maskImg.getColorAt(y, x))) {
        // choose the color from the edited image
        return editedImage.getColorAt(y, x);
      } else {
        // stick to the original color
        return img.getColorAt(y, x);
      }
    });
  }
}
