package model;

public interface ColorMap {
  RGBColor apply(RGBColor color, int row, int col);
}
