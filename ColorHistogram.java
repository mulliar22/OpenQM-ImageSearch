import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

//Does the computation to convert image into color histogram
public class ColorHistogram {
    private double[] histogram;
    private int depth;

    //In the case of image pixels array and depth, compute histogram
    public ColorHistogram(ColorImage image, int depth) {
        this.depth = depth;
        // Explicitly initialize the histogram array to avoid null pointer exceptions
        int size = (int) Math.pow(2, depth * 3); // For 3 channels (R, G, B)
        this.histogram = new double[size]; // Allocate memory for the histogram array
        Arrays.fill(this.histogram, 0.0); // Ensure all values are initialized to 0

        computeHistogram(image);
    }

    //In the case of text file already loaded with histogram data,
    //parse file and put into histogram variable.
    public ColorHistogram(String filename) throws IOException {
        // Assuming the file format matches the histogram array size, initialize accordingly
        this.histogram = new double[(int) Math.pow(2, 3 * 3)]; // Adjust if the depth is different.
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Assuming all histogram values are on a single line
            if (line != null) {
                String[] values = line.split("\\s+"); // Split the line by spaces
                for (int i = 0; i < values.length; i++) {
                    try {
                        this.histogram[i] = Double.parseDouble(values[i]);
                    } catch (NumberFormatException e) {
                        throw new IOException("Invalid number format in histogram file: " + filename, e);
                    }
                }
            }
        }
    }

    //Method that computes color histogram for given image
    //Iterate through image, each pixel scaled and left-shifted, sum
    //pixel colors together, that is bin number (color) for the histogram, increment
    //this value in the histogram array
    private void computeHistogram(ColorImage image) {
        if (this.histogram == null) {
            throw new IllegalStateException("Histogram array not initialized.");
        }

        int scaleFactor = (int) Math.pow(2, 8 - this.depth);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int[] pixel = image.getPixel(i, j);
                int bin = ((pixel[0] / scaleFactor) << (2 * this.depth)) +
                          ((pixel[1] / scaleFactor) << this.depth) +
                          (pixel[2] / scaleFactor);
                this.histogram[bin]++;
            }
        }
        normalize();
    }


    //Divides all numbers in histogram by the sum of all numbers
    private void normalize() {
        if (this.histogram == null) {
            throw new IllegalStateException("Histogram array not initialized.");
        }

        double sum = Arrays.stream(this.histogram).sum();
        for (int i = 0; i < this.histogram.length; i++) {
            this.histogram[i] /= sum;
        }
    }

    //Iterates through two histograms, sums together the number they have in common for each bin
    public double compare(ColorHistogram other) {
        double intersection = 0;
        for (int i = 0; i < this.histogram.length; i++) {
            intersection += Math.min(this.histogram[i], other.histogram[i]);
        }
        return intersection;
    }
}
