package model;

public interface ImageState {

  int getHeight();

  int getWidth();

  RGBColor getColorAt(int row, int col);
}
