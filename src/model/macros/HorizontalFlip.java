package model.macros;

import model.Image;

public class HorizontalFlip implements Macro {
  @Override
  public Image execute(Image img) {
    return img.transform((c, y, x)
            -> img.getColorAt(y, img.getWidth() - x - 1));
  }
}
