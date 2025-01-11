package com.group15.controllers;

import com.group15.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

import javafx.scene.SnapshotParameters;

public class stage5Controller {

    @FXML
    public Label nameSurname;
    @FXML
    public Label movieTitle;
    @FXML
    public Label dateTime;
    @FXML
    public Label hall;
    @FXML
    public Label takenSeats;
    @FXML
    public TextArea cart;
    @FXML
    public Label nameSurnameTicket;
    @FXML
    public Label dateTicket;
    @FXML
    public Label timeTicket;
    @FXML
    public Label hallTicket;
    @FXML
    public Label seatsTicket;
    @FXML
    public Button print;
    @FXML
    public Label transactionDate;
    @FXML
    private Label Surname;

    @FXML
    private Label Name;

    @FXML
    private Label Username;

    @FXML
    private Label Role;

    @FXML
    private ImageView qrkod;

    @FXML
    private ImageView filmseridi1;

    @FXML
    private ImageView filmseridi2;

    @FXML
    private ImageView filmseridi3;

    @FXML
    private ImageView yaprak;

    @FXML
    private ImageView seat1;

    @FXML
    private ImageView seat2;

    @FXML
    private ImageView colorful;

    @FXML
    private ImageView gibLogo;

    @FXML
    private Button closeButton;

    @FXML
    private Pane printReceipt;
    @FXML
    private Pane printTicket;

    DataPass data;

    Facade facade = new Facade();

    public Schedule schedule;
    public String selectedSeats;
    public Map<Product,Integer> products;
    public Movie movie;
    public String nameSurnameData;
    public double totalPrice;
    public double tax;
    public boolean discountApplied;

    public void setData(DataPass data){
        this.data = data;
        schedule = data.schedule;
        selectedSeats = data.getSeatsAsString();
        products=data.products;
        movie = data.movie;
        nameSurnameData=data.getNameSurname();
        totalPrice=data.absTotal;
        tax = data.tax;
        discountApplied=data.discountApplied;


        nameSurname.setText("Name Surname: "+nameSurnameData);
        movieTitle.setText("Movie: " + movie.getTitle());
        dateTime.setText("Date and Time: "+schedule.getDate()+" "+schedule.getSessionTime());
        hall.setText("Hall: "+schedule.getHall());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedSeats.length(); i+=2) {
            sb.append(selectedSeats.charAt(i));
            sb.append(selectedSeats.charAt(i+1));
            if (i < selectedSeats.length() - 2)  // Son karakterde değilse virgül ekle
                sb.append(",");

        }
        String result = sb.toString();
        takenSeats.setText("Seats: "+result);

        cart.setEditable(false);
        cart.setText(data.cartContent2);
        String currentText = cart.getText();
        currentText = currentText.replace("Total","Tax  ₺"+tax+"\n"+"Total");
        cart.setText(currentText);

        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        String formattedDate = today.format(formatter);
        transactionDate.setText(formattedDate);

        dateTicket.setText(String.valueOf(schedule.getDate()));
        timeTicket.setText(String.valueOf(schedule.getSessionTime()));
        hallTicket.setText(schedule.getHall());
        seatsTicket.setText(result);
        nameSurnameTicket.setText(nameSurnameData);

    }
    public User user;
    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        Username.setText("Username: "+user.getUsername());
        Name.setText("Name: "+user.getName());
        Surname.setText("Surname: "+user.getSurname());
        Role.setText("Role: "+user.getRole());
    }

    @FXML
    private void logout(ActionEvent event) {
        // Get the current stage
        Stage currentStage = (Stage) closeButton.getScene().getWindow();
        // Close the current stage
        currentStage.close();

        // Load the login scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/loginGUI.fxml"));
            Parent loginRoot = loader.load();

            // Create a new stage for the login scene
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Login");
            makeStageFillScreen(currentStage);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    public void makeStageFillScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Set the stage size to exactly match the screen dimensions
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
    }

    @FXML
    private void initialize() {
        Image image1 = new Image(getClass().getResource("/images/ticket/serit.png").toExternalForm());
        filmseridi1.setImage(image1);
        image1 = new Image(getClass().getResource("/images/ticket/serit.png").toExternalForm());
        filmseridi2.setImage(image1);
        image1 = new Image(getClass().getResource("/images/ticket/serit.png").toExternalForm());
        filmseridi3.setImage(image1);
        image1 = new Image(getClass().getResource("/images/ticket/leaf.png").toExternalForm());
        yaprak.setImage(image1);
        image1 = new Image(getClass().getResource("/images/ticket/seat.png").toExternalForm());
        seat1.setImage(image1);
        image1 = new Image(getClass().getResource("/images/ticket/seat.png").toExternalForm());
        seat2.setImage(image1);
        image1 = new Image(getClass().getResource("/images/ticket/qr.png").toExternalForm());
        qrkod.setImage(image1);
        image1 = new Image(getClass().getResource("/images/ticket/colorfol.png").toExternalForm());
        colorful.setImage(image1);
        image1 = new Image(getClass().getResource("/images/extra/gibLogo.png").toExternalForm());
        gibLogo.setImage(image1);
    }
    @FXML
    public void end(){

        for(Product product:products.keySet()){
            facade.updateStock(product.getId(),products.get(product));
        }
        String newSeating = facade.getSeatingById(schedule.getScheduleId());
        List<Integer> indices=getSeatIndices(selectedSeats, facade.getHallByScheduleId(schedule.getScheduleId()));
        StringBuilder seatingBuilder = new StringBuilder(newSeating);
        for(int index:indices){
            seatingBuilder.setCharAt(index - 1, 't');
        }

        facade.updateScheduleSeats(schedule.getScheduleId(),selectedSeats.length()/2,seatingBuilder.toString());

        facade.updateRevenue(((int)totalPrice-(int)tax),(int)tax,(int)totalPrice);

        String input = nameSurnameData; // Example input string
        StringBuilder word1 = new StringBuilder();
        String word2 = "";
        String[] words = input.split(" ");
        if (words.length >= 2) {
            int i=0;
            for(;i<words.length-1;i++)
                word1.append(words[i]);
            word2 = words[i];
        } else {
            word1.append(words[0]);
        }


        Date date = new Date(System.currentTimeMillis());
        int transactionId = facade.insertTransaction(word1.toString(),word2,date,schedule.getScheduleId(),selectedSeats.length()/2,selectedSeats,(int)totalPrice,(int)tax,discountApplied);

        for(Product product:products.keySet()){
            if(products.get(product)!=0)
                facade.insertTransactionItem(transactionId,product.getId(),products.get(product),(int)(products.get(product)*(product.getPrice()+(((double)product.getPrice())/100)*product.getTax())));
        }

        try {
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/stage1.fxml"));
            Parent root = loader.load();

            stage1Controller prev = loader.getController();
            prev.setUser(user);

            // Create a new scene and set it to the current stage
            Stage stage = (Stage) print.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        saveNodeAsPDF(printTicket, "C:\\Users\\ugurk\\Desktop\\Transactions\\" + word1.toString() + "_" + word2 + "_ticket.pdf");
        saveNodeAsPDF(printReceipt, "C:\\Users\\ugurk\\Desktop\\Transactions\\" + word1.toString() + "_" + word2 + "_receipt.pdf");

        showAlert("Success", "Receipt and ticket printed successfully");
    }

    public static List<Integer> getSeatIndices(String seats, String hall) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < seats.length(); i += 2) {
            char row = seats.charAt(i); // Get the row letter
            int column = Character.getNumericValue(seats.charAt(i + 1)); // Get the column number

            if (Objects.equals(hall, "Hall A")) {
                // Hall A: Rows A-D, Columns 1-4
                if (row < 'A' || row > 'D' || column < 1 || column > 4) {
                    throw new IllegalArgumentException("Invalid seat for Hall A: " + row + column);
                }
                int rowIndex = row - 'A'; // Convert row to zero-based index (A=0, B=1, ..., D=3)
                int index = rowIndex * 4 + column; // Calculate the 1-based index
                indices.add(index);
            } else {
                // Hall B: Rows A-F, Columns 1-8
                if (row < 'A' || row > 'F' || column < 1 || column > 8) {
                    throw new IllegalArgumentException("Invalid seat for Hall B: " + row + column);
                }
                int rowIndex = row - 'A'; // Convert row to zero-based index (A=0, B=1, ..., F=5)
                int index = rowIndex * 8 + column; // Calculate the 1-based index
                indices.add(index);
            }
        }

        return indices;
    }

    public static void saveNodeAsPDF(Node node, String filePath) {
        try {
            // Set up snapshot parameters for higher resolution
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setTransform(javafx.scene.transform.Transform.scale(2, 2)); // 2x scale for better resolution
            WritableImage writableImage = node.snapshot(snapshotParameters, null);

            // Convert WritableImage to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

            // Convert BufferedImage to ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            // Convert ByteArrayOutputStream to iText ImageData
            ImageData imageData = ImageDataFactory.create(byteArrayOutputStream.toByteArray());

            // Create PDF Writer and Document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Add image to PDF with scaling
            com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(imageData);
            pdfImage.scaleToFit(pdfDocument.getDefaultPageSize().getWidth() - 50,
                    pdfDocument.getDefaultPageSize().getHeight() - 50); // Adjust margins
            document.add(pdfImage);

            // Close the document
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);

        // Create custom content
        Label content = new Label(message);
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 18px;");

        // Set the custom content
        alert.getDialogPane().setContent(content);

        // Set a specific rectangular size for the alert
        alert.getDialogPane().setPrefSize(400, 200); // Width: 400, Height: 200

        alert.showAndWait();
    }

}
