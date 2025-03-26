import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

//Driver class
//Requires 2 arguments, the path to the image
//and the path to the dataset of images
//Dataset of images must be precomputed text files, not images
public class SimilaritySearch {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SimilaritySearch <queryImage> <imageDatasetDirectory>");
            System.exit(1);
        }

        try {
            String queryImagePath = args[0];
            String datasetDirectoryPath = args[1];
            searchSimilarImages(queryImagePath, datasetDirectoryPath);
        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void searchSimilarImages(String queryImagePath, String datasetDirectoryPath) throws IOException {

        //Makes histogram for main search image
        ColorImage queryImage = new ColorImage(queryImagePath);
        ColorHistogram queryHistogram = new ColorHistogram(queryImage, 3); // 3 bits depth

        //Gets dataset of images into an array, files
        File datasetDir = new File(datasetDirectoryPath);
        File[] files = datasetDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) {
            throw new IOException("Dataset directory is empty or does not exist.");
        }

        //Stores each dataset image, along with its similarity to the search image
        //Each time an item is added, Priority Queue is reordered from greatest to least similarity
        PriorityQueue<ImageSimilarity> queue = new PriorityQueue<>((a, b) -> Double.compare(b.similarity, a.similarity));

        //Iterates through all images in the dataset
        for (File file : files) {
            //Converts each dataset text file into histogram
            ColorHistogram datasetHistogram = new ColorHistogram(file.getAbsolutePath());
            //Compare query search image to current dataset image
            double similarity = queryHistogram.compare(datasetHistogram);

            //Stores this dataset text file, and its similarity score in priority queue
            queue.add(new ImageSimilarity(file.getName(), similarity));
        }

        //Try to get top 5 similar images, or check if there is less than 5
        //Pops off the highest similarity image of the queue that many times
        //Prints this information to console.
        int count = Math.min(5, queue.size());
        for (int i = 0; i < count; i++) {
            ImageSimilarity sim = queue.poll();
            System.out.printf("%d. %s (Similarity: %.12f)%n", i + 1, sim.fileName, sim.similarity);
        }
    }

    //Stores the filename of the dataset image, along with its similarity score to the search image
    private static class ImageSimilarity {
        String fileName;
        double similarity;

        ImageSimilarity(String fileName, double similarity) {
            this.fileName = fileName;
            this.similarity = similarity;
        }
    }
}