package com.example.deepspace;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


import java.io.File;
import java.net.URL;
import java.util.*;

import static jdk.internal.org.jline.utils.AttributedStyle.BLACK;
import static jdk.internal.org.jline.utils.AttributedStyle.WHITE;

public class HelloController implements Initializable {

    @FXML
    public ImageView Image1;
    @FXML
    public CheckBox numberscb;
    @FXML
    public Button fileButton;
    @FXML
    public ImageView Image2;
    @FXML
    public TextField Luminance;
    @FXML
    public Button UpdateLuminanceButton;
    @FXML
    public TextField FiltherByPixel;
    @FXML
    public Button Filther;

    public Button objectFinderButton;
    FileChooser fileChooser = new FileChooser();
    private Color sampleColour;

    static Image image;
    private int[][] imageArray;
    private int width;
    private int height;
    private WritableImage newBWImage;
    private WritableImage newPCBImage;
    public UnionFind unionFind;
    private int luminanceValue = 40;
    HashMap<Integer, ArrayList<Integer>> FunkyMap;
    Hashtable<Integer, ArrayList<Integer>> theHashTable;
    public WritableImage  BlackAndWhite;


    public void fileButton(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        // Set the initial directory for the displayed file dialog
        fc.setInitialDirectory(new File("C:\\Users\\Dylan"));
//\OneDrive-Waterford Institute of Technology\Applied Computing Year 2\Semester 2\Data Structures 2//
        // Set the selected file or null if no file has been selected
        File file = fc.showOpenDialog(null); // Shows a new file open dialog.
        if (file != null) {

            // URI that represents this abstract pathname
            Image1.setImage(new Image(file.toURI().toString()));
            Image2.setImage(new Image(file.toURI().toString()));
            image = new Image(file.toURI().toString());   //imageDetails();
            blackAndWhite();

        } else {
            System.out.println("A file is invalid!");
        }


    }


    public WritableImage blackAndWhite() {
        Image2.setImage(Image1.getImage());
        Image image = Image2.getImage();
        PixelReader pixelReader = image.getPixelReader();

        height = (int) image.getHeight();
        width = (int) image.getWidth();

        imageArray = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color color = pixelReader.getColor(col, row);
                int red = (int) (color.getRed() * 255);
                int green = (int) (color.getGreen() * 255);
                int blue = (int) (color.getBlue() * 255);
                int luminance = (red + green + blue) / 3;

                if (luminance > luminanceValue) {
                    imageArray[row][col] = WHITE;
                } else {
                    imageArray[row][col] = BLACK;
                }
            }
        }

        // Create a UnionFind object with the number of pixels in the image as size
        unionFind = new UnionFind(height * width);

        // Iterate over the imageArray and union adjacent white pixels
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int currentPixel = imageArray[row][col];
                int currentIndex = row * width + col;

                if (currentPixel == WHITE) {
                    // Check if the pixel to the right is white
                    if (col + 1 < width && imageArray[row][col + 1] == WHITE) {
                        int rightIndex = row * width + col + 1;
                        unionFind.unify(currentIndex, rightIndex);
                    }

                    // Check if the pixel below is white
                    if (row + 1 < height && imageArray[row + 1][col] == WHITE) {
                        int belowIndex = (row + 1) * width + col;
                        unionFind.unify(currentIndex, belowIndex);
                    }
                }
            }
        }

        // Create a new image from the processed image array
        WritableImage processedImage = new WritableImage(width, height);
        PixelWriter pixelWriter = processedImage.getPixelWriter();
        Image2.setImage(processedImage);

        // Create a hashmap to track each star
        FunkyMap = new HashMap<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int color = imageArray[row][col];

                if (color == WHITE) {
                    int index = row * width + col;
                    int root = unionFind.find(index);

                    // Add the pixel to the appropriate ArrayList in the hashmap
                    if (!FunkyMap.containsKey(root)) {
                        FunkyMap.put(root, new ArrayList<>());
                    }
                    FunkyMap.get(root).add(index); //new int[]{col, row});
                }
                // Set the pixel color in the processed image
                if (color == WHITE) {
                    pixelWriter.setColor(col, row, Color.WHITE);
                } else {
                    pixelWriter.setColor(col, row, Color.BLACK);
                }
            }
        }
        //for test
        int largestRoot= Collections.max(FunkyMap.keySet(),(a, b)->FunkyMap.get(a).size()-FunkyMap.get(b).size());
        List lis=FunkyMap.get(largestRoot);


        BlackAndWhite = processedImage;
       // displayStarsWithRandomColor();

        return processedImage;
    }

        public void updateImageView() {
            String text = Luminance.getText();
            int luminance = Integer.parseInt(text);

            // Check if luminance is within the valid range of 10 to 255
            if (luminance < 10) {
                luminance = 10;
            } else if (luminance > 255) {
                luminance = 255;
            }

            // Set the luminance value to 40 if it's not provided in the text field
            if (text.isEmpty()) {
                luminance = 40;
            }

            // Update the image view with the new luminance value
            Image2.setEffect(new ColorAdjust(0, 0, 0, (luminance - 10) / 245.0));
        }

    @FXML
    public void UpdateLuminance(ActionEvent event) {
        int newLuminance;
        try {
            newLuminance = Integer.parseInt(Luminance.getText());
            if (newLuminance < 10 || newLuminance > 255) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            // Invalid input, revert to old luminance value
            Luminance.setText(Integer.toString(luminanceValue));
            return;
        }
        luminanceValue = newLuminance;
        blackAndWhite();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}