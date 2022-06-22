package view;

public interface Features {
  void load(String filePath, String imgName);

  void save(String filePath);

  void select(String name);

  void runCommand(String commandName, String newName);

  void runBrightness(String newName, int increment);

  void runDownscale(String newName, int height, int width);
}
