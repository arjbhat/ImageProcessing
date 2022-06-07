package model;

import java.util.HashMap;

/**
 * An implementation of the ImageProcessingModel
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {
  HashMap<String, Image> images;
  HashMap<String, Macro> macros;

  public ImageProcessingModelImpl() {
    images = new HashMap<>();
    macros = new HashMap<>();
    this.initMacros();

  }

  private void initMacros() {
    macros.put("redGrayscale", img
            -> img.transform((c, y, x)
            -> new RGBColor(c.getRed(), c.getRed(), c.getRed())));
    macros.put("greenGrayscale", img
            -> img.transform((c, y, x)
            -> new RGBColor(c.getGreen(), c.getGreen(), c.getGreen())));
    macros.put("blueGrayscale", img
            -> img.transform((c, y, x)
            -> new RGBColor(c.getBlue(), c.getBlue(), c.getBlue())));
    macros.put("valueGrayscale", img
            -> img.transform((c, y, x)
            -> new RGBColor(c.getValue(), c.getValue(), c.getValue())));
    macros.put("intensityGrayscale", img
            -> img.transform((c, y, x)
            -> new RGBColor(c.getIntensity(), c.getIntensity(), c.getIntensity())));
    macros.put("lumaGrayscale", img
            -> img.transform((c, y, x)
            -> new RGBColor(c.getLuma(), c.getLuma(), c.getLuma())));
    macros.put("flipHorizontally", img
            -> img.transform((c, y, x)
            -> img.getColorAt(y, img.getWidth() - x - 1)));
    macros.put("flipVertically", img
            -> img.transform((c, y, x)
            -> img.getColorAt(img.getHeight() - y - 1, x)));
  }
}