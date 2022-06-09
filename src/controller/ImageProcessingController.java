package controller;

import java.util.Scanner;

import model.ImageProcessingModel;

/**
 * Represents an image processing Controller that relays information back and forth to the model,
 * and the view.
 */
public interface ImageProcessingController {
  void control() throws IllegalStateException;
}
