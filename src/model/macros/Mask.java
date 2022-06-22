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
  private final ImageState maskImage;

  /**
   * Constructs a mask macro.
   *
   * @param macro     the operation that we wish to perform on the selection
   * @param maskImage the mask that we will decide how to operate with
   */
  public Mask(Macro macro, ImageState maskImage) {
    if (macro == null) {
      throw new IllegalArgumentException("Macro cannot be null.");
    }
    if (maskImage == null) {
      throw new IllegalArgumentException("Mask image cannot be null.");
    }
    this.macro = macro;
    this.maskImage = maskImage;
  }

  @Override
  public ImageTransform execute(ImageTransform img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    if (img.getHeight() != maskImage.getHeight() || img.getWidth() != maskImage.getWidth()) {
      throw new IllegalArgumentException("Mask image dimensions must be the same " +
          "as image dimensions.");
    }

    // new edited image
    ImageTransform editedImage = macro.execute(img);
    if (editedImage == null || editedImage.getHeight() != maskImage.getHeight()
        || editedImage.getWidth() != maskImage.getWidth()) {
      throw new IllegalArgumentException("Resulting image dimensions must be the same " +
          "as image dimensions.");
    }

    return img.transform((c, y, x) -> {
      // if mask is black then
      if (new RGBColor(0, 0, 0).equals(maskImage.getColorAt(y, x))) {
        // choose the color from the edited image
        return editedImage.getColorAt(y, x);
      } else {
        // stick to the original color
        return img.getColorAt(y, x);
      }
    });
  }
}
