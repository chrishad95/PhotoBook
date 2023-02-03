import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageMontage {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Image Montage");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    MontagePanel panel = new MontagePanel();
    frame.add(panel);

    frame.pack();
    frame.setVisible(true);
  }
}

class MontagePanel extends JPanel {
  private BufferedImage[] images;

  public MontagePanel() {
    JFileChooser chooser = new JFileChooser();
    chooser.setMultiSelectionEnabled(true);
    int result = chooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File[] files = chooser.getSelectedFiles();
      Arrays.sort(files, new Comparator<File>() {
        @Override
        public int compare(File a, File b) {
          return Long.compare(a.lastModified(), b.lastModified());
        }
      });
      images = new BufferedImage[files.length];
      for (int i = 0; i < files.length; i++) {
        try {
          images[i] = ImageIO.read(files[i]);
        } catch (IOException e) {
          System.err.println("Could not read image: " + files[i]);
        }
      }
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int x = 0;
    int y = 0;
    int maxHeight = 0;

    // Determine the size of the frame based on the desired aspect ratio
    int frameWidth = images.length * 80;
    int frameHeight = 100;

    // Draw all the images next to each other
    for (BufferedImage image : images) {
      if (image == null) {
        continue;
      }
      g.drawImage(image, x, y, null);
      x += 80;
      maxHeight = Math.max(maxHeight, image.getHeight());
    }

    setSize(frameWidth, frameHeight);
  }
}
