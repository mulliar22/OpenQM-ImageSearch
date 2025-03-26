import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class ImageConverter{
    public static void main(String[] args){}

    //filename argument is the fullname of the file (ex. exampleImage.jpg)
    //outputType argument must be a string representing a valid image format (ex. "jpg", "png")
    public static void convertImage(String filename, String outputType) throws IOException {
        try{
            if(filename.endsWith(outputType)){
                throw new IOException("Input image must be different type from output type");
            }
            File inputFile = new File(filename);
            BufferedImage inputBuffer = ImageIO.read(inputFile);

            BufferedImage outputBuffer = new BufferedImage(inputBuffer.getWidth(),
                    inputBuffer.getHeight(), BufferedImage.TYPE_INT_RGB);

            outputBuffer.createGraphics().drawImage(inputBuffer, 0, 0, java.awt.Color.WHITE, null);

            ImageIO.write(outputBuffer, outputType, inputFile);
        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
        }

    }
}
