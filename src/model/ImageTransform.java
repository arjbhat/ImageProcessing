package model;

public interface ImageTransform extends ImageState {

  Image transform(ColorMap map);
}
