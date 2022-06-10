package model;

/**
 * A function with 3 inputs (RGBColor, row, and column) and one output (a RGBColor).
 */
public interface ColorFunction {

  /**
   * Applies this function to the given color with that row and column (in the image).
   *
   * @param color the color of the image
   * @param row   the row that the color is in
   * @param col   the column that the color is in
   * @return the new color that this color should be mapped to
   */
  RGBColor apply(RGBColor color, int row, int col);
}