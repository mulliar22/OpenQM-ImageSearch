import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

//Image class
public class ColorImage {
    private int[][][] pixels; // 3D array to store RGB values of each pixel
    private int width;
    private int height;
    private int depth = 8; // Standard depth for RGB images

    //Loads JPG or PPM images
    public ColorImage(String filename) throws IOException {
        if (filename.endsWith(".jpg")) {
            loadJpgImage(filename);
        } else if (filename.endsWith(".ppm")) {
            loadPpmImage(filename);
        } else {
            throw new IOException("Unsupported file format.");
        }
    }

    //Gets JPG into 3D array of pixels
    private void loadJpgImage(String filename) throws IOException {
        BufferedImage image = ImageIO.read(new File(filename));
        if (image == null) {
            throw new IOException("Invalid image file: " + filename);
        }
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width][height][3];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                pixels[i][j][0] = (rgb >> 16) & 0xFF;
                pixels[i][j][1] = (rgb >> 8) & 0xFF;
                pixels[i][j][2] = rgb & 0xFF;
            }
        }
    }

    //Gets PPM into 3D array of pixels
    private void loadPpmImage(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            if (!br.readLine().equals("P3")) {
                throw new IOException("Invalid PPM file: " + filename);
            }
            String[] dimensions = br.readLine().split("\\s+");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);
            depth = Integer.parseInt(br.readLine()); // This reads the max color value, assuming it's 255
            pixels = new int[width][height][3];
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    pixels[i][j][0] = Integer.parseInt(br.readLine());
                    pixels[i][j][1] = Integer.parseInt(br.readLine());
                    pixels[i][j][2] = Integer.parseInt(br.readLine());
                }
            }
        }
    }
    //Accessor function for each pixel
    public int[] getPixel(int i, int j) {
        return pixels[i][j];
    }

    //Accessor function for width of image
    public int getWidth() {
        return width;
    }

    //Accessor function for height of image
    public int getHeight() {
        return height;
    }

    // Additional methods as needed
}
