package view;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class JHistogram extends JPanel {
  private final int[] red;
  private final int[] green;
  private final int[] blue;
  private final int[] intensity;
  private BufferedImage img;
  private int maxFequency;

  public JHistogram() {
    this(256, 256);
  }

  public JHistogram(int width, int height) {
    this.red = new int[256];
    this.green = new int[256];
    this.blue = new int[256];
    this.intensity = new int[256];
    this.setPreferredSize(new Dimension(width, height));
  }

  public void setImage(BufferedImage img) {
    this.img = img;
    this.maxFequency = 0;
    for (int i = 0; i < red.length; i += 1) {
      red[i] = 0;
      green[i] = 0;
      blue[i] = 0;
      intensity[i] = 0;
    }
    this.calcFrequencies();
    repaint();
  }

  private void calcFrequencies() {
    for (int row = 0; row < img.getHeight(); row += 1) {
      for (int col = 0; col < img.getWidth(); col += 1) {
        Color c = new Color(img.getRGB(col, row));
        int intense = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
        red[c.getRed()] += 1;
        green[c.getGreen()] += 1;
        blue[c.getBlue()] += 1;
        intensity[intense] += 1;
      }
    }

    for (int value : red) {
      maxFequency = Integer.max(value, maxFequency);
    }
    for (int value : green) {
      maxFequency = Integer.max(value, maxFequency);
    }
    for (int value : blue) {
      maxFequency = Integer.max(value, maxFequency);
    }
    for (int value : intensity) {
      maxFequency = Integer.max(value, maxFequency);
    }
  }

  private void drawLine(Graphics g, Color c, int[] source) {
    int totalHeight = this.getHeight();
    int totalWidth = this.getWidth();
    int prevX = 0;
    int prevY = 0;

    g.setColor(c);
    for (int index = 0; index < source.length; index += 1) {
      int xPos = (index * totalWidth) / (source.length - 1);
      int yPos = (int) ((Math.log(source[index] + 1) * totalHeight)
          / Math.ceil(Math.log(maxFequency + 1)));

      g.drawLine(prevX, totalHeight - prevY, xPos, totalHeight - yPos);

      prevX = xPos;
      prevY = yPos;
    }
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (img == null) {
      return;
    }
    this.drawLine(g, Color.RED, red);
    this.drawLine(g, Color.GREEN, green);
    this.drawLine(g, Color.BLUE, blue);
    this.drawLine(g, Color.MAGENTA, intensity);
  }

  public int getMaxFequency() {
    return maxFequency;
  }
}