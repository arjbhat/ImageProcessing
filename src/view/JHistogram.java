package view;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 * Produces a JPanel which shows a histogram for a buffered image (with a key) - and the ability to
 * look at the logarithmic scale.
 */
public class JHistogram extends JPanel {
  private static final int TOP_HEIGHT = 120;
  private final int[] red;
  private final int[] green;
  private final int[] blue;
  private final int[] intensity;
  private final JLabel yKey;
  private boolean logarithmic;
  private BufferedImage img;
  private int maxFrequency;

  /**
   * Constructs a default histogram.
   */
  public JHistogram() {
    this(256, 256);
  }

  /**
   * Constructs a histogram with the width and height that we provide.
   * (The histogram that it creates is determined by mutating the underlying image)
   */
  public JHistogram(int width, int height) {
    this.red = new int[256];
    this.green = new int[256];
    this.blue = new int[256];
    this.intensity = new int[256];
    this.setPreferredSize(new Dimension(width, height));
    this.logarithmic = false;

    this.yKey = new JLabel("Y axis: frequency in linear scale 0 to 0");
    this.add(this.topSection(width));
    this.add(this.graphSection(width, height));
  }

  /**
   * Set the image that the histogram is made for.
   *
   * @param img the buffered image that we want a histogram for
   * @throws IllegalArgumentException if the image is null
   */
  public void setImage(BufferedImage img) {
    if (img == null) {
      throw new IllegalArgumentException("Histogram given null image");
    }
    this.img = img;
    this.maxFrequency = 0;
    for (int i = 0; i < red.length; i += 1) {
      red[i] = 0;
      green[i] = 0;
      blue[i] = 0;
      intensity[i] = 0;
    }
    this.calcFrequencies();
    this.updateYKey();
    this.repaint();
  }

  // Calculate all the frequencies for the red, green, blue, and intensity values.
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
      maxFrequency = Integer.max(value, maxFrequency);
    }
    for (int value : green) {
      maxFrequency = Integer.max(value, maxFrequency);
    }
    for (int value : blue) {
      maxFrequency = Integer.max(value, maxFrequency);
    }
    for (int value : intensity) {
      maxFrequency = Integer.max(value, maxFrequency);
    }
  }


  public int getMaxFrequency() {
    return maxFrequency;
  }

  private JPanel topSection(int width) {
    JPanel section = new JPanel();
    section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
    JPanel key = this.makeKey();
    key.setPreferredSize(new Dimension(width - 10, TOP_HEIGHT - 20));
    section.add(this.makeKey());
    JCheckBox button = this.makeToggleButton();
    button.setPreferredSize(new Dimension(width - 10, 20));
    section.add(button);
    return section;
  }

  private JPanel makeKey() {
    JPanel keyPanel = new JPanel();
    keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
    keyPanel.add(new JLabel("Key:"));
    keyPanel.add(new JLabel("Red: red component"));
    keyPanel.add(new JLabel("Green: green component"));
    keyPanel.add(new JLabel("Blue: blue component"));
    keyPanel.add(new JLabel("Magenta: intensity component"));
    keyPanel.add(new JLabel("X axis: value from 0 to 255"));
    keyPanel.add(yKey);
    return keyPanel;
  }

  private JCheckBox makeToggleButton() {
    JCheckBox scaleButton = new JCheckBox("logarithmic");
    scaleButton.addActionListener(a -> {
      this.logarithmic = scaleButton.isSelected();
      this.updateYKey();
      this.repaint();
    });
    return scaleButton;
  }

  private JPanel graphSection(int width, int height) {
    JPanel section = new JPanel();
    section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
    JPanel upper = new JPanel();
    JHistGraph graph = new JHistGraph();
    graph.setPreferredSize(new Dimension(width - 50, height - TOP_HEIGHT - 50));
    JLabel yAxis = new JLabel("Y axis");

    upper.add(yAxis);
    upper.add(graph);

    JLabel xAxis = new JLabel("X axis");

    section.add(upper);
    section.add(xAxis);
    return section;
  }

  private void updateYKey() {
    this.yKey.setText(String.format("Y axis: frequency in %s scale 0 to %d",
        this.logarithmic ? "logarithmic" : "linear", this.maxFrequency));
  }

  private class JHistGraph extends JPanel {
    JHistGraph() {
      this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    // Paint the histogram with multiple lines
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

    // Draw a line for a component
    private void drawLine(Graphics g, Color c, int[] source) {
      int totalHeight = this.getHeight();
      int totalWidth = this.getWidth();
      int prevX = 0;
      int prevY = 0;

      g.setColor(c);
      for (int index = 0; index < source.length; index += 1) {
        int xPos = (index * totalWidth) / (source.length - 1);
        int yPos = logarithmic
            ? (int) ((Math.log(source[index] + 1) * totalHeight) // log-scale
            / Math.ceil(Math.log(maxFrequency + 1)))
            : source[index] * totalHeight / maxFrequency;

        g.drawLine(prevX, totalHeight - prevY, xPos, totalHeight - yPos);

        prevX = xPos;
        prevY = yPos;
      }
    }
  }
}
