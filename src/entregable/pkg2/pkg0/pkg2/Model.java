/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entregable.pkg2.pkg0.pkg2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.io.ExceptionListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.HDGSentence;
import net.sf.marineapi.nmea.sentence.MDASentence;
import net.sf.marineapi.nmea.sentence.MWVSentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.XDRSentence;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.nmea.util.Measurement;



/**
 *
 * @author jsoler
 */
public class Model {


    private static Model model;

    private Model() {
    }
    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
    
    

    private SentenceReader reader;

    //True Wind Dir
    private final DoubleProperty TWD = new SimpleDoubleProperty();
    public DoubleProperty TWDProperty() {
        return TWD;
    }
    
    // True Wind Speed
    private final DoubleProperty TWS = new SimpleDoubleProperty();
    public DoubleProperty TWSProperty() {
        return TWS;
    }
    
    //Heading 
    private final DoubleProperty HDG = new SimpleDoubleProperty();
    public DoubleProperty HDGProperty() {
        return HDG;
    }
    
    // Position
    private final ObjectProperty<Position> GPS = new SimpleObjectProperty();
    public ObjectProperty<Position> GPSroperty() {
        return GPS;
    }
    
    // COG
    private final DoubleProperty COG = new SimpleDoubleProperty();
    public DoubleProperty COGProperty() {
        return COG;
    }
    // SOG
    private final DoubleProperty SOG = new SimpleDoubleProperty();
    public DoubleProperty SOGProperty() {
        return SOG;
    }
    
    private final DoubleProperty AWA = new SimpleDoubleProperty();
    public DoubleProperty AWAProperty() {
        return AWA;
    }
    
    private final DoubleProperty AWS = new SimpleDoubleProperty();
    public DoubleProperty AWSProperty() {
        return AWS;
    }
    
    private final DoubleProperty TEMP = new SimpleDoubleProperty();
    public DoubleProperty TEMProperty(){
        return TEMP;
    }
    
    private final DoubleProperty ROLL = new SimpleDoubleProperty();
    public DoubleProperty ROLLProperty(){
        return ROLL;
    }
    
    private final DoubleProperty PITCH = new SimpleDoubleProperty();
    public DoubleProperty PITCHProperty(){
        return PITCH;
    }
    
//    private final DoubleProperty SLID = new SimpleDoubleProperty();
//    public DoubleProperty SliProperty(){
//        return SLID;
//    }    

    //====================================================================
    //anadir tantos sentenceListener como tipos de sentence queremos tratar
    class HDGSentenceListener
            extends AbstractSentenceListener<HDGSentence> {

        @Override
        public void sentenceRead(HDGSentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence    
            HDG.set(sentence.getHeading());
        }
    };

    class MDASentenceListener
            extends AbstractSentenceListener<MDASentence> {

        @Override
        public void sentenceRead(MDASentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence 
            TWD.set(sentence.getTrueWindDirection());
            TWS.set(sentence.getWindSpeedKnots());
            TEMP.set(sentence.getAirTemperature());

        }
    }
    
    
    //========================================================================================
    // anade todas las clases de que extiendan AbstractSentenceListener que necesites
    class RMCSentenceListener
            extends AbstractSentenceListener<RMCSentence> {

        @Override
        public void sentenceRead(RMCSentence sentence) {
            GPS.set(sentence.getPosition());
            COG.set(sentence.getCourse());
            SOG.set(sentence.getSpeed());
        }
    }
    
    class MWVSentenceListener
        extends AbstractSentenceListener<MWVSentence> {
        
        @Override
        public void sentenceRead(MWVSentence sentence){
            AWA.set(sentence.getAngle());
            AWS.set(sentence.getSpeed());
        }
    }
    
    class XDRSentenceListener
        extends AbstractSentenceListener<XDRSentence>{
    
        @Override
        public void sentenceRead(XDRSentence sentence){
            for (Measurement me : sentence.getMeasurements()){
                if(me.getName().equals("PTCH")){
                    PITCH.set(me.getValue());
                } else {
                    if(me.getName().equals("ROLL"))
                    ROLL.set(me.getValue());
                }
            }
        }
    }
    
//    class SliListener {
//
//        public void sentenceRead(Slider sentence) {
//            SLID.set(sentence.getValue());
//        }
//        
//    }
    
    // falta por gestiona que solamente hay un senteceReader
    public void addSentenceReader(File file) throws FileNotFoundException {

        InputStream stream = new FileInputStream(file);
        if (reader != null) {  // esto ocurre si ya estamos leyendo un fichero
            reader.stop();
        }
        reader = new SentenceReader(stream);
 
        //==================================================================
        //============= Registra todos los sentenceListener que necesites
        HDGSentenceListener hdg = new HDGSentenceListener();
        reader.addSentenceListener(hdg);

        MDASentenceListener mda = new MDASentenceListener();
        reader.addSentenceListener(mda);

        MWVSentenceListener mwv = new MWVSentenceListener();
        reader.addSentenceListener(mwv);

        XDRSentenceListener xdr = new XDRSentenceListener();
        reader.addSentenceListener(xdr);
        
        RMCSentenceListener rmd = new RMCSentenceListener();
        reader.addSentenceListener(rmd);
        
//        SliListener sli = new SliListener();
//        reader.addSentenceListener(sli);
        
                
         //===============================================================

         //===============================================================
         //== Anadimos un exceptionListener para que capture las tramas que 
         // == no tienen parser, ya que no las usamos
         reader.setExceptionListener(e->{System.out.println(e.getMessage());});
         
         //================================================================
         //======== arrancamos el SentenceReader para que empieze a escucha             
        reader.start();
    }
}
