import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final long start = System.currentTimeMillis();

    public static void main(String[] args) {

        String source = "src/main/resources/source";
        String destination = "src/main/resources/destination";

        resizeImages(source,destination,3); //pathToSourceFolder, destinationPath, countOfThreads

    }
    private static void resizeImages(String sourcePath, String destinationPath, int threads) {
        Logger LOGGER = Logger.getLogger(String.valueOf(Main.class));

        File imagesDirectory = new File(sourcePath);
        File[] images = imagesDirectory.listFiles();

        try {
            List<File[]> list = new ArrayList<>();
            int lengthOfArrayInThread = 0; //length of arrays in thread

            if (images.length > threads && images.length <= 100) {
                lengthOfArrayInThread += ((int) Math.ceil((double) images.length / 10)) * 10 / threads;
            }
            if (images.length > threads && images.length <= 1000 && images.length > 100) {
                lengthOfArrayInThread += ((int) Math.ceil((double) images.length / 100)) * 100 / threads;
            }
            if (images.length > threads && images.length <= 10000 && images.length > 1000) {
                lengthOfArrayInThread += ((int) Math.ceil((double) images.length / 1000)) * 1000 / threads;
            }
            int i = 0;
            while (i + lengthOfArrayInThread < images.length) {
                File[] arraycopy = new File[lengthOfArrayInThread];
                System.arraycopy(images, i, arraycopy, 0, arraycopy.length);
                list.add(arraycopy);
                i += lengthOfArrayInThread;
            }
            if (i + lengthOfArrayInThread >= images.length) {
                File[] arraycopy2 = new File[images.length - i];
                System.arraycopy(images, images.length - (images.length - i), arraycopy2, 0, arraycopy2.length);
                list.add(arraycopy2);
            }
            int threadNum = 0;
            for (File[] arrays : list) {
                threadNum++;
                // start resizer
                ImageResizer resizer = new ImageResizer(arrays, 1400, destinationPath, start); //new width 1400
                new Thread(resizer).start();
                LOGGER.log(Level.INFO,"thread #"+ threadNum + " 'resizer' is starting ...");

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}

