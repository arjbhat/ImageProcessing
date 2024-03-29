package view;

import java.awt.Color;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.SpinnerNumberModel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The GUI view that the user can manipulate images with. It is a JFrame with many panels.
 */
public class ImageProcessingGUIFrame extends JFrame implements ImageProcessingGUI {
  private static final Color BACKGROUND_COLOR = new Color(0x1c1c1c);
  private static final int IMAGE_HEIGHT = 600;
  private final JLabel imageSlot;
  private final JButton loadButton;
  private final JButton saveButton;
  private final JList<String> imageSelection;
  private final DefaultListModel<String> imagesList;
  private final JComboBox<String> commandSelector;
  private final CardLayout commandLayout;
  private final JTextField newName;
  private final JButton applyButton;
  private final JSlider brightenSlider;
  private final JLabel downScaleText;
  private final SpinnerNumberModel downScaleHeight;
  private final SpinnerNumberModel downScaleWidth;
  private final JHistogram histogram;

  /**
   * Constructs the GUI frame with all the buttons and panes that the user can operate on images
   * with.
   */
  public ImageProcessingGUIFrame() {
    // sets the title of the frame
    this.setTitle("Image Processing GUI");
    // exit out of the application when X button clicked
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // disable frame from being resizable.
    this.setResizable(false);
    // sets the x-dimension and y-dimension of the frame
    this.setSize(1100, 670);
    // set the background of the frame
    this.getContentPane().setBackground(BACKGROUND_COLOR);

    // left side image view
    this.imageSlot = new JLabel();
    this.loadButton = new JButton("Load Image");
    this.saveButton = new JButton("Save Image");
    this.imagesList = new DefaultListModel<>();
    this.imageSelection = new JList<>(imagesList);

    // commands
    this.commandSelector = new JComboBox<>();
    this.commandLayout = new CardLayout();
    this.brightenSlider = new JSlider(JSlider.HORIZONTAL, -256, 256, 0);
    this.downScaleText = new JLabel("New dimensions must be smaller");
    this.downScaleWidth = new SpinnerNumberModel(1, 1, 1, 1);
    this.downScaleHeight = new SpinnerNumberModel(1, 1, 1, 1);
    this.newName = new JTextField();
    this.applyButton = new JButton("Apply");

    // histogram
    this.histogram = new JHistogram(400, 350);

    this.add(this.makeContents());

    this.setVisible(true); // makes the frame visible
  }

  @Override
  public void selectImage(String imageName, BufferedImage img) {
    if (imageName != null && !imagesList.contains(imageName)) {
      this.imagesList.addElement(imageName);
    }
    this.imageSelection.setSelectedIndex(this.imagesList.indexOf(imageName));
    this.imageSlot.setIcon(new ImageIcon(img));
    this.downScaleText.setText("New dimensions can be at most " + img.getWidth()
        + "x" + img.getHeight());
    this.downScaleWidth.setMaximum(img.getWidth());
    this.downScaleHeight.setMaximum(img.getHeight());
    this.downScaleWidth.setValue(img.getWidth());
    this.downScaleHeight.setValue(img.getHeight());
    this.histogram.setImage(img);
  }

  @Override
  public void displayError(String err) {
    JOptionPane.showMessageDialog(this, err, "Some Went Wrong",
        JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void addFeatures(Features f) {
    // when the user clicks load, prompt to choose a file and name, then ask to load it
    loadButton.addActionListener(e -> {
      final JFileChooser fileChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files",
          "jpg", "jpeg", "png", "bmp", "ppm");
      fileChooser.setFileFilter(filter);
      int signal = fileChooser.showOpenDialog(this);
      if (signal == JFileChooser.APPROVE_OPTION) {
        String newName = JOptionPane.showInputDialog("Please give the image a name");
        File file = fileChooser.getSelectedFile();
        f.load(file.getAbsolutePath(), newName);
      }
    });
    // when the user clicks save, prompt to choose a destination then ask to save it
    saveButton.addActionListener(e -> {
      final JFileChooser fileChooser = new JFileChooser(".");
      int signal = fileChooser.showSaveDialog(this);
      if (signal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        f.save(file.getAbsolutePath());
      }
    });
    // when the user clicks an image name, ask to select it
    imageSelection.addListSelectionListener(e -> {
      f.select(imageSelection.getSelectedValue());
    });
    // when the user asks to apply the command, ask to run the command, passing args if any
    applyButton.addActionListener(e -> {
      String selected = (String) commandSelector.getSelectedItem();
      String newName = this.newName.getText();
      if ("brighten".equals(selected)) {
        f.runBrightness(newName, brightenSlider.getValue());
      } else if ("downscale".equals(selected)) {
        f.runDownscale(newName, (int) downScaleHeight.getNumber(),
            (int) downScaleWidth.getNumber());
      } else {
        f.runCommand(selected, newName);
      }
    });

    // allow pressing the enter key in the new name box to apply
    newName.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // ignore event
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          applyButton.doClick();
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // ignore event
      }
    });
  }

  private JPanel imageView() {
    // top level
    JPanel imageViewPanel = new JPanel();
    imageViewPanel.setBackground(BACKGROUND_COLOR);
    imageViewPanel.setPreferredSize(new Dimension(620, IMAGE_HEIGHT + 50));

    // image & list
    JPanel imageWithSelector = new JPanel();
    // images list
    imageSelection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane imageListScroll = new JScrollPane(imageSelection);
    imageListScroll.setPreferredSize(new Dimension(100, IMAGE_HEIGHT));
    // image view
    imageSlot.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane imageScroll = new JScrollPane(imageSlot);
    imageScroll.setPreferredSize(new Dimension(500, IMAGE_HEIGHT));

    imageWithSelector.add(imageListScroll);
    imageWithSelector.add(imageScroll);

    imageViewPanel.add(imageWithSelector);
    return imageViewPanel;
  }

  private JPanel commandSelectorView() {
    // top level
    JPanel commandSelectorPanel = new JPanel();
    commandSelectorPanel.setPreferredSize(new Dimension(400, 200));

    JPanel commands = new JPanel(commandLayout);
    commands.setPreferredSize(new Dimension(400, 100));
    commandSelector.addActionListener(a -> {
      commandLayout.show(commands, (String) commandSelector.getSelectedItem());
    });

    commands.add(this.brightnessCard(), "brighten");
    commandSelector.addItem("brighten");
    commands.add(this.downScaleCard(), "downscale");
    commandSelector.addItem("downscale");

    // All other commands
    String[] blank = {
        "red-component", "green-component", "blue-component", "value-component", "luma-component",
        "intensity-component", "horizontal-flip", "vertical-flip", "blur", "sharpen", "greyscale",
        "sepia",
    };
    for (String name : blank) {
      JPanel panel = new JPanel();
      commands.add(panel, name);
      commandSelector.addItem(name);
    }

    commandSelectorPanel.add(commandSelector);
    commandSelectorPanel.add(commands);

    commandSelectorPanel.add(this.applyPanel());

    return commandSelectorPanel;
  }

  private JPanel brightnessCard() {
    JPanel brightenCard = new JPanel();
    brightenCard.add(new JLabel("Brightness change:"));
    brightenSlider.setPreferredSize(new Dimension(400, 50));
    brightenSlider.setMinorTickSpacing(32);
    brightenSlider.setMajorTickSpacing(128);
    brightenSlider.setPaintTicks(true);
    brightenSlider.setPaintLabels(true);
    brightenCard.add(brightenSlider);
    JLabel brightenState = new JLabel("0");
    brightenCard.add(brightenState);
    brightenSlider.addChangeListener(e -> {
      brightenState.setText(String.valueOf(brightenSlider.getValue()));
    });
    return brightenCard;
  }


  private JPanel downScaleCard() {
    JPanel downScaleCard = new JPanel();
    JPanel widthPanel = new JPanel();
    JPanel heightPanel = new JPanel();
    JSpinner downScaleWidthSpinner = new JSpinner(downScaleWidth);
    JSpinner downScaleHeightSpinner = new JSpinner(downScaleHeight);
    downScaleWidthSpinner.setPreferredSize(new Dimension(100, 20));
    downScaleHeightSpinner.setPreferredSize(new Dimension(100, 20));
    widthPanel.add(new JLabel("New Width: "));
    widthPanel.add(downScaleWidthSpinner);
    heightPanel.add(new JLabel("New Height: "));
    heightPanel.add(downScaleHeightSpinner);
    downScaleCard.add(this.downScaleText);
    downScaleCard.add(heightPanel);
    downScaleCard.add(widthPanel);

    return downScaleCard;
  }

  private JPanel applyPanel() {
    // new name & apply
    JPanel namePanel = new JPanel();

    // new image name field
    JLabel nameLabel = new JLabel("New name: ");
    newName.setPreferredSize(new Dimension(100, 20));

    namePanel.add(nameLabel);
    namePanel.add(newName);

    applyButton.setPreferredSize(new Dimension(75, 20));
    namePanel.add(applyButton);

    return namePanel;
  }

  // create the panel with load, save, and command buttons - along with the histogram
  private JPanel rightSide() {
    JPanel rightSide = new JPanel();
    rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));

    JPanel loadButtons = new JPanel();
    loadButtons.add(loadButton);
    loadButtons.add(saveButton);
    loadButtons.setBackground(BACKGROUND_COLOR);

    rightSide.add(this.histogram);
    rightSide.add(this.spacer(300, 30));
    rightSide.add(this.commandSelectorView());
    rightSide.add(this.spacer(300, 10));
    rightSide.add(loadButtons);

    return rightSide;
  }

  // bring all the panels together (image side and command side)
  private JPanel makeContents() {
    JPanel contents = new JPanel();
    contents.setBackground(BACKGROUND_COLOR);
    contents.add(this.imageView());
    contents.add(this.spacer(30, 100));
    contents.add(this.rightSide());
    return contents;
  }

  // the spacing for our GUI (between panels)
  private JPanel spacer(int width, int height) {
    JPanel space = new JPanel();
    space.setBackground(BACKGROUND_COLOR);
    space.setPreferredSize(new Dimension(width, height));
    return space;
  }

  /**
   * Do nothing. A GUI does not support any messages (other than errors).
   *
   * @param message the message to be transmitted
   * @throws IOException N/A
   */
  @Override
  public void renderMessage(String message) throws IOException {
    // Do nothing here.
  }
}
