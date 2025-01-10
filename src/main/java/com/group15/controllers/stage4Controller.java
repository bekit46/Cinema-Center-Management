package com.group15.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.group15.DataPass;
import com.group15.Facade;
import com.group15.Product;
import com.group15.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class stage4Controller {

    @FXML
    private Label Surname;

    @FXML
    private Label Name;

    @FXML
    private Label Username;

    @FXML
    private Label Role;

    @FXML
    private TextArea cart;

    @FXML
    private Button ButtonNext;

    @FXML
    private Spinner<Integer> discountTicketsSpinner;

    @FXML
    private TextField nameAndSurnameField;

    @FXML
    private Spinner<Integer> spinner1;

    @FXML
    private Spinner<Integer> spinner2;

    @FXML
    private Spinner<Integer> spinner3;

    @FXML
    private Spinner<Integer> spinner4;

    @FXML
    private Spinner<Integer> spinner5;

    @FXML
    private Spinner<Integer> spinner6;

    @FXML
    private Spinner<Integer> spinner7;

    @FXML
    private Spinner<Integer> spinner8;

    @FXML
    private Spinner<Integer> spinner9;

    @FXML
    private ImageView glassesImage;

    @FXML
    private ImageView waterImage;

    @FXML
    private ImageView candyImage;

    @FXML
    private ImageView chipsImage;

    @FXML
    private ImageView colaImage;

    @FXML
    private ImageView hotdogImage;

    @FXML
    private ImageView icecreamImage;

    @FXML
    private ImageView popcornImage;

    @FXML
    private ImageView spriteImage;

    @FXML
    private Button closeButton;

    @FXML
    private Label price1;
    @FXML
    private Label price2;
    @FXML
    private Label price3;
    @FXML
    private Label price4;
    @FXML
    private Label price5;
    @FXML
    private Label price6;
    @FXML
    private Label price7;
    @FXML
    private Label price8;
    @FXML
    private Label price9;

    DataPass data;

    Facade facade = new Facade();



    private User user;

    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        Username.setText("Username: "+user.getUsername());
        Name.setText("Name: "+user.getName());
        Surname.setText("Surname: "+user.getSurname());
        Role.setText("Role: "+user.getRole());
    }

    private final Map<Spinner<Integer>, Product> spinnerProductMap = new HashMap<>();

    public double absTotal;
    public double tax;

    public void setData(DataPass data){
        this.data = data;
        absTotal = data.getTotalPrice();
        tax = data.tax;
        cart.setText(data.cartContent);

        cart.appendText("\nTotal   ₺"+absTotal);
        Image image1 = new Image(getClass().getResource("/images/products/popcorn.png").toExternalForm());
        glassesImage.setImage(image1);
        Image image2 = new Image(getClass().getResource("/images/products/cola.png").toExternalForm());
        waterImage.setImage(image2);
        Image image3 = new Image(getClass().getResource("/images/products/candy.png").toExternalForm());
        candyImage.setImage(image3);
        Image image4 = new Image(getClass().getResource("/images/products/chips.png").toExternalForm());
        chipsImage.setImage(image4);
        Image image5 = new Image(getClass().getResource("/images/products/hot_dog.png").toExternalForm());
        colaImage.setImage(image5);
        Image image6 = new Image(getClass().getResource("/images/products/ice_cream.png").toExternalForm());
        hotdogImage.setImage(image6);
        Image image7 = new Image(getClass().getResource("/images/products/water.png").toExternalForm());
        icecreamImage.setImage(image7);
        Image image8 = new Image(getClass().getResource("/images/products/3d_glasses.png").toExternalForm());
        popcornImage.setImage(image8);
        Image image9 = new Image(getClass().getResource("/images/products/sprite.png").toExternalForm());
        spriteImage.setImage(image9);


        List<Product> products=facade.getAllProducts();

        price1.setText("₺"+String.valueOf(products.get(0).getPrice()+(products.get(0).getPrice()/100*products.get(0).getTax())));
        price2.setText("₺"+String.valueOf(products.get(1).getPrice()+(products.get(1).getPrice()/100*products.get(1).getTax())));
        price3.setText("₺"+String.valueOf(products.get(2).getPrice()+(products.get(2).getPrice()/100*products.get(2).getTax())));
        price4.setText("₺"+String.valueOf(products.get(3).getPrice()+(products.get(3).getPrice()/100*products.get(3).getTax())));
        price5.setText("₺"+String.valueOf(products.get(4).getPrice()+(products.get(4).getPrice()/100*products.get(4).getTax())));
        price6.setText("₺"+String.valueOf(products.get(5).getPrice()+(products.get(5).getPrice()/100*products.get(5).getTax())));
        price7.setText("₺"+String.valueOf(products.get(6).getPrice()+(products.get(6).getPrice()/100*products.get(6).getTax())));
        price8.setText("₺"+String.valueOf(products.get(7).getPrice()+(products.get(7).getPrice()/100*products.get(7).getTax())));
        price9.setText("₺"+String.valueOf(products.get(8).getPrice()+(products.get(8).getPrice()/100*products.get(8).getTax())));

        spinner1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(0).getStockQuantity(),0,1));
        spinner2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(1).getStockQuantity(),0,1));
        spinner3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(2).getStockQuantity(),0,1));
        spinner4.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(3).getStockQuantity(),0,1));
        spinner5.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(4).getStockQuantity(),0,1));
        spinner6.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(5).getStockQuantity(),0,1));
        spinner7.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(6).getStockQuantity(),0,1));
        spinner8.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(7).getStockQuantity(),0,1));
        spinner9.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, products.get(8).getStockQuantity(),0,1));

        spinnerProductMap.put(spinner1,products.get(0));
        spinnerProductMap.put(spinner2,products.get(1));
        spinnerProductMap.put(spinner3,products.get(2));
        spinnerProductMap.put(spinner4,products.get(3));
        spinnerProductMap.put(spinner5,products.get(4));
        spinnerProductMap.put(spinner6,products.get(5));
        spinnerProductMap.put(spinner7,products.get(6));
        spinnerProductMap.put(spinner8,products.get(7));
        spinnerProductMap.put(spinner9,products.get(8));

        spinnerProductMap.keySet().forEach(this::setupSpinnerListener);

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,data.seatSize(), 0, 1);
        discountTicketsSpinner.setValueFactory(valueFactory);
        discountTicketsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            discountedMethod(oldValue,newValue);
        });
    }

    @FXML
    private void initialize() {
        ButtonNext.setVisible(false);
        nameAndSurnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            ButtonNext.setVisible(!newValue.isEmpty()); // İçerik boş değilse görünür yap
        });

    }

    public void setupSpinnerListener(Spinner<Integer> spinner){
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            Product product = spinnerProductMap.get(spinner);
            updateTextArea(product, oldValue, newValue);
        });
    }

    private void updateTextArea(Product product, int oldValue, int newValue) {
        
        String currentText = cart.getText();
        String productLine = product.getProductName()+"  x"+String.valueOf(newValue)+"   ₺"+(product.getPrice()+(product.getPrice()/100)*product.getTax())*newValue;
        String oldLine = product.getProductName()+"  x"+String.valueOf(oldValue)+"   ₺"+(product.getPrice()+(product.getPrice()/100)*product.getTax())*oldValue;

        currentText = currentText.replace("\nTotal   ₺"+absTotal, "");
        if(oldValue>newValue){
            absTotal-=(oldValue-newValue)*(product.getPrice()+(((double)product.getPrice()/100)*product.getTax()));
            tax -= (product.getPrice()/100)*product.getTax();
        }else{
            absTotal+=(newValue-oldValue)*(product.getPrice()+(((double)product.getPrice()/100)*product.getTax()));
            tax += (product.getPrice()/100)*product.getTax();
        }
        // Eğer değer 0'dan farklıysa ve satır eklenmemişse
        if (newValue == 0) {
            currentText = currentText.replace(oldLine + "\n", "");
        } else if (oldValue == 0) {
            currentText += productLine + "\n";
        } else {
            currentText = currentText.replace(oldLine, productLine);
        }
        
        cart.setText(currentText);
        cart.appendText("\nTotal   ₺"+absTotal);
    }


    @FXML
    public void logout(){
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
    private void NextScreen(ActionEvent event) {
        try {
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/stage5.fxml"));
            Parent root = loader.load();
            data.absTotal=absTotal;
            data.tax=tax;
            data.cartContent2=cart.getText();
            data.setNameSurname(nameAndSurnameField.getText());
            data.setDiscountedTicketCount(discountTicketsSpinner.getValue());

            Map<Product,Integer> products = new HashMap<>();
            for(Spinner<Integer> spinner:spinnerProductMap.keySet()){
                products.put(spinnerProductMap.get(spinner),spinner.getValue());
            }
            data.products = products;

            stage5Controller nextScreenData = loader.getController();
            nextScreenData.setData(data);
            nextScreenData.setUser(user);
            // Create a new scene and set it to the current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Back(ActionEvent event) {
        try {
            String Hall = data.schedule.getHall();
            if(Hall.equals("Hall A"))
                Hall = "hallA";
            else
                Hall = "hallB";
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/"+Hall+".fxml"));
            Parent root = loader.load();
            if(Hall.equals("hallB"))
            {
                hallBController prevScreenData = loader.getController();
                data.clearSelectedSeats();
                prevScreenData.setData(data);
                prevScreenData.setUser(user);
            }
            else{
                hallAController prevScreenData = loader.getController();
                data.clearSelectedSeats();
                prevScreenData.setData(data);
                prevScreenData.setUser(user);
            }
            
            // Create a new scene and set it to the current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void discountedMethod(Integer oldValue,Integer newValue){
        double nonDiscTicket = (double)data.movie.getPrice()+((double)data.movie.getPrice()/100)*data.movie.getTax();
        
        String currentText = cart.getText();
        double money = (double)(newValue*((double)data.movie.getPrice()+(((double)data.movie.getPrice()/100)*data.movie.getTax())))/100*data.movie.getDiscount();
        String productLine = "Discount   -₺"+money;
        money = (double)(oldValue*((double)data.movie.getPrice()+(((double)data.movie.getPrice()/100)*data.movie.getTax())))/100*data.movie.getDiscount();
        String oldLine = "Discount   -₺"+money;
        data.setDiscountedTicketCount(newValue);

        currentText = currentText.replace("\nTotal   ₺"+absTotal, "");

        if(oldValue>newValue){
            absTotal += (oldValue-newValue)*(nonDiscTicket/100)*data.movie.getDiscount();
            tax += (((double)data.movie.getPrice()/100)*data.movie.getTax())/100*data.movie.getDiscount();
        }else{
            absTotal -= (newValue-oldValue)*(nonDiscTicket/100)*data.movie.getDiscount();
            tax -= (((double)data.movie.getPrice()/100)*data.movie.getTax())/100*data.movie.getDiscount();
        }
        if(newValue==0){
            data.discountApplied=false;
        } else{
            data.discountApplied=true;
        }

        // Eğer değer 0'dan farklıysa ve satır eklenmemişse
        if (newValue == 0) {
            currentText = currentText.replace(oldLine + "\n", "");
        } else if (oldValue == 0) {
            currentText += productLine + "\n";
        } else {
            currentText = currentText.replace(oldLine, productLine);
        }
        
        cart.setText(currentText);
        cart.appendText("\nTotal   ₺"+absTotal);
    }
    
}
