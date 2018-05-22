/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entregable.pkg2.pkg0.pkg2;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author pausic
 */
public class VistaInicialController implements Initializable {

    @FXML
    private MenuItem modoDiurno;
    @FXML
    private MenuItem modoNocturno;
    @FXML
    private LineChart<Double, Double> direccionChart;
    @FXML
    private LineChart<Double, Double> intensidadChart;
    @FXML
    private Label twdText;
    @FXML
    private Label twsText;
    @FXML
    private Label awaText;
    @FXML
    private Label awsText;
    @FXML
    private Slider acotaSlider;
    @FXML
    private Label latText;
    @FXML
    private Label longText;
    @FXML
    private Label cogText;
    @FXML
    private Label sogText;
    @FXML
    private Label hdgText;
    @FXML
    private Label tempText;
    @FXML
    private Label pitchText;
    @FXML
    private Label rollText;
    @FXML
    private BorderPane raiz;

    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        acotaSlider.setMin(2);
        acotaSlider.setMax(10);
        XYChart.Series intensi = new XYChart.Series();
        XYChart.Series direc = new XYChart.Series();

        raiz.setId("raiz");
        model=Model.getInstance();
        try{
            File file = new File("Jul_20_2017_1871339_0183.NMEA");
            model.addSentenceReader(file);
            
        }catch(FileNotFoundException e){
            
        }
        
        
        
        model.HDGProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue) + "º";
                 Platform.runLater(() -> {
                hdgText.setText(dat);
            });
        });
        model.TWDProperty().addListener((observable, oldValue, newValue) -> {
            String dat = String.valueOf(newValue) + "º";
            Platform.runLater(() -> {
                long millis = System.currentTimeMillis();
                int seconds = (int) (millis / 1000) % 60 ;
                int minutes = (int) ((millis / (1000*60)) % 60);
                String X = minutes + ":" + seconds;
                twdText.setText(dat);
                direc.getData().add(new XYChart.Data(X ,newValue));
                while(acotaSlider.getValue() < direc.getData().size()){
                    direc.getData().remove(0);
                }
            });
        });
        model.TWSProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "Kn";
            Platform.runLater(() -> {
                long millis = System.currentTimeMillis();
                int seconds = (int) (millis / 1000) % 60 ;
                int minutes = (int) ((millis / (1000*60)) % 60);
                String X = minutes + ":" + seconds;
                twsText.setText(dat);
                intensi.getData().add(new XYChart.Data(X ,newValue));
                while(acotaSlider.getValue() < intensi.getData().size()){
                    intensi.getData().remove(0);
                }
            });
        });
       
        
        model.GPSroperty().addListener((observable, oldValue, newValue)-> {
            Platform.runLater(() -> {
                longText.setText(String.valueOf(newValue.getLongitude()) + " " + newValue.getLongitudeHemisphere());
                latText.setText(String.valueOf(newValue.getLatitude()) + " " + newValue.getLatitudeHemisphere());
            });
        });
        
        model.COGProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "Kn";
            Platform.runLater(() -> {
                cogText.setText(dat);
            });
        });
        
        model.SOGProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "Kn";
            Platform.runLater(() -> {
                sogText.setText(dat);
            });
        });
        
        model.AWSProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "Kn";
            Platform.runLater(() -> {
                awsText.setText(dat);
            });
        });
        
        model.AWAProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "Kn";
            Platform.runLater(() -> {
                awaText.setText(dat);
            });
        });
        
        model.TEMProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "º";
            Platform.runLater(() -> {
                tempText.setText(dat);
            });
        });
        
        model.ROLLProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "º";
            Platform.runLater(() -> {
                rollText.setText(dat);
            });
        });
        
        model.PITCHProperty().addListener((observable, oldValue, newValue)-> {
            String dat = String.valueOf(newValue) + "º";
            Platform.runLater(() -> {
                pitchText.setText(dat);
            });
        });
        intensidadChart.getData().add(intensi);
        direccionChart.getData().add(direc);
    }    

    @FXML
    private void actionDiurno(ActionEvent event) {
        raiz.getStylesheets().clear();
    }

    @FXML
    private void actionNocturno(ActionEvent event) {
        raiz.getStylesheets().clear();
        raiz.getStylesheets().add(Entregable202.class.getResource("ModoNocturno.css").toExternalForm());                
    }
    
}
