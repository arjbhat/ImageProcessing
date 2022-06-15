import org.junit.Before;
import org.junit.Test;

import model.Color;
import model.RGBColor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Testing if all the methods in our RGB color system work.
 */
public class ColorTest {
  private Color red;
  private Color orange;
  private Color yellow;
  private Color lime;
  private Color blue;
  private Color indigo;
  private Color violet;
  private Color white;
  private Color black;
  private Color silver;
  private Color maroon;
  private Color olive;
  private Color green;
  private Color teal;
  private Color navy;

  /**
   * Let's start by initialising some colors.
   */
  @Before
  public void initColors() {
    red = new RGBColor(255, 0, 0);
    orange = new RGBColor(255, 127, 0);
    yellow = new RGBColor(255, 255, 0);
    lime = new RGBColor(0, 255, 0);
    blue = new RGBColor(0, 0, 255);
    indigo = new RGBColor(75, 0, 130);
    violet = new RGBColor(143, 0, 255);
    white = new RGBColor(255, 255, 255);
    black = new RGBColor(0, 0, 0);
    silver = new RGBColor(192, 192, 192);
    maroon = new RGBColor(128, 0, 0);
    olive = new RGBColor(128, 128, 0);
    green = new RGBColor(0, 128, 0);
    teal = new RGBColor(0, 128, 128);
    navy = new RGBColor(0, 0, 128);
  }

  /**
   * What happens if you try to make a color greater than 255 for any of the channels or lower
   * than 0.
   */
  @Test
  public void testConstructorMinValue() {
    this.red = null;
    // Try negative in each of the slots
    this.tryColor(-1, 0, 255);
    this.tryColor(0, -1, 255);
    this.tryColor(0, 255, -1);

    this.tryColor(-1, 0, 0, 255);
    this.tryColor(0, -1, 0, 255);
    this.tryColor(0, 0, -1, 255);
    this.tryColor(0, 0, 255, -1);
    assertNotNull(this.red);
    // We can construct colors that have a value greater than 255.
    // it's the image that adds the constraints.
  }

  private void tryColor(int... args) {
    if (args.length == 3) {
      try {
        this.red = new RGBColor(args[0], args[1], args[2]);
        assertEquals(Math.max(args[0], 0), this.red.getRed());
        assertEquals(Math.max(args[1], 0), this.red.getGreen());
        assertEquals(Math.max(args[2], 0), this.red.getBlue());
      } catch (IllegalArgumentException e) {
        fail("We couldn't construct the color.");
      }
    }
    if (args.length == 4) {
      try {
        this.red = new RGBColor(args[0], args[1], args[2], args[3]);
        assertEquals(Math.max(args[0], 0), this.red.getRed());
        assertEquals(Math.max(args[1], 0), this.red.getGreen());
        assertEquals(Math.max(args[2], 0), this.red.getBlue());
        assertEquals(Math.max(args[3], 0), this.red.getAlpha());
      } catch (IllegalArgumentException e) {
        fail("We couldn't construct the color.");
      }
    }
  }

  @Test
  public void getRed() {
    assertEquals(255, red.getRed());
    assertEquals(255, orange.getRed());
    assertEquals(255, yellow.getRed());
    assertEquals(0, lime.getRed());
    assertEquals(0, blue.getRed());
    assertEquals(75, indigo.getRed());
    assertEquals(143, violet.getRed());
    assertEquals(255, white.getRed());
    assertEquals(0, black.getRed());
    assertEquals(192, silver.getRed());
    assertEquals(128, maroon.getRed());
    assertEquals(128, olive.getRed());
    assertEquals(0, green.getRed());
    assertEquals(0, teal.getRed());
    assertEquals(0, navy.getRed());
  }

  @Test
  public void getGreen() {
    assertEquals(0, red.getGreen());
    assertEquals(127, orange.getGreen());
    assertEquals(255, yellow.getGreen());
    assertEquals(255, lime.getGreen());
    assertEquals(0, blue.getGreen());
    assertEquals(0, indigo.getGreen());
    assertEquals(0, violet.getGreen());
    assertEquals(255, white.getGreen());
    assertEquals(0, black.getGreen());
    assertEquals(192, silver.getGreen());
    assertEquals(0, maroon.getGreen());
    assertEquals(128, olive.getGreen());
    assertEquals(128, green.getGreen());
    assertEquals(128, teal.getGreen());
    assertEquals(0, navy.getGreen());
  }

  @Test
  public void getBlue() {
    assertEquals(0, red.getBlue());
    assertEquals(0, orange.getBlue());
    assertEquals(0, yellow.getBlue());
    assertEquals(0, lime.getBlue());
    assertEquals(255, blue.getBlue());
    assertEquals(130, indigo.getBlue());
    assertEquals(255, violet.getBlue());
    assertEquals(255, white.getBlue());
    assertEquals(0, black.getBlue());
    assertEquals(192, silver.getBlue());
    assertEquals(0, maroon.getBlue());
    assertEquals(0, olive.getBlue());
    assertEquals(0, green.getBlue());
    assertEquals(128, teal.getBlue());
    assertEquals(128, navy.getBlue());
  }

  @Test
  public void getValue() {
    assertEquals(255, red.getValue());
    assertEquals(255, orange.getValue());
    assertEquals(255, yellow.getValue());
    assertEquals(255, lime.getValue());
    assertEquals(255, blue.getValue());
    assertEquals(130, indigo.getValue());
    assertEquals(255, violet.getValue());
    assertEquals(255, white.getValue());
    assertEquals(0, black.getValue());
    assertEquals(192, silver.getValue());
    assertEquals(128, maroon.getValue());
    assertEquals(128, olive.getValue());
    assertEquals(128, green.getValue());
    assertEquals(128, teal.getValue());
    assertEquals(128, navy.getValue());
  }

  @Test
  public void getIntensity() {
    assertEquals(85, red.getIntensity());
    assertEquals(127, orange.getIntensity());
    assertEquals(170, yellow.getIntensity());
    assertEquals(85, lime.getIntensity());
    assertEquals(85, blue.getIntensity());
    assertEquals(68, indigo.getIntensity());
    assertEquals(132, violet.getIntensity());
    assertEquals(255, white.getIntensity());
    assertEquals(0, black.getIntensity());
    assertEquals(192, silver.getIntensity());
    assertEquals(42, maroon.getIntensity());
    assertEquals(85, olive.getIntensity());
    assertEquals(42, green.getIntensity());
    assertEquals(85, teal.getIntensity());
    assertEquals(42, navy.getIntensity());
  }

  @Test
  public void getLuma() {
    assertEquals(54, red.getLuma());
    assertEquals(145, orange.getLuma());
    assertEquals(236, yellow.getLuma());
    assertEquals(182, lime.getLuma());
    assertEquals(18, blue.getLuma());
    assertEquals(25, indigo.getLuma());
    assertEquals(48, violet.getLuma());
    assertEquals(254, white.getLuma());
    assertEquals(0, black.getLuma());
    assertEquals(192, silver.getLuma());
    assertEquals(27, maroon.getLuma());
    assertEquals(118, olive.getLuma());
    assertEquals(91, green.getLuma());
    assertEquals(100, teal.getLuma());
    assertEquals(9, navy.getLuma());
  }

  @Test
  public void getTransparency() {
    red = new RGBColor(255, 0, 0);
    assertEquals(255, red.getAlpha());
    red = new RGBColor(255, 0, 0, 10);
    assertEquals(10, red.getAlpha());
    red = new RGBColor(255, 0, 0, 20);
    assertEquals(20, red.getAlpha());
  }

  @Test
  public void areColorsEqual() {
    assertNotEquals(red, orange);
    assertEquals(red, new RGBColor(255, 0, 0));
    assertNotEquals(lime, green);
    assertEquals(lime, new RGBColor(0, 255, 0));
    assertNotEquals(blue, indigo);
    assertEquals(blue, new RGBColor(0, 0, 255));
  }

  @Test
  public void areHashesEqual() {
    assertEquals(red.hashCode(), new RGBColor(255, 0, 0).hashCode());
    assertEquals(lime.hashCode(), new RGBColor(0, 255, 0).hashCode());
    assertEquals(blue.hashCode(), new RGBColor(0, 0, 255).hashCode());
    assertNotEquals(red.hashCode(), orange.hashCode());
    assertNotEquals(lime.hashCode(), green.hashCode());
    assertNotEquals(blue.hashCode(), indigo.hashCode());
    assertEquals(8520481, red.hashCode());
    assertEquals(1168831, lime.hashCode());
    assertEquals(931681, blue.hashCode());
  }
}
