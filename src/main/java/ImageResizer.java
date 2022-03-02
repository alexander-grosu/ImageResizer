import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageResizer implements Runnable {
    Logger LOGGER = Logger.getLogger(String.valueOf(ImageResizer.class));

    private final File[] files;
    private final int newWidth;
    private final String destination;
    private final long start;

    public ImageResizer(File[] files, int newWidth, String destination, long start) {
        this.files = files;
        this.newWidth = newWidth;
        this.destination = destination;
        this.start = start;
    }

    @Override
    public void run() {

        for (File file : files) {
            BufferedImage bufferedImage;

            try {
                bufferedImage = ImageIO.read(file);
                if (bufferedImage == null) {
                    continue;
                }
                int newHeigth = (int) Math.round
                        (bufferedImage.getHeight() / (bufferedImage.getWidth() / (double) newWidth));

                BufferedImage newImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, newWidth, newHeigth);
                File imgFile = new File(destination + "/" + file.getName());
                ImageIO.write(newImage, "jpg", imgFile);

            } catch (IOException | ArithmeticException e) {
                e.printStackTrace();
            }
        }
        LOGGER.log(Level.INFO,
                "thread stopped, images has been resizing successfully, time: " + (System.currentTimeMillis() - start) + " ms");
    }
}
