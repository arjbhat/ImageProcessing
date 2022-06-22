package view;

import java.awt.image.BufferedImage;

public interface ImageProcessingGUI extends ImageProcessingView {
  void addImage(String imageName);

  void selectImage(String imageName, BufferedImage img);

  void displayError(String err);

  void addFeatures(Features f);
}
