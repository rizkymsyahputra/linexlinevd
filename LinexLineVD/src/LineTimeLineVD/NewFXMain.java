/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LineTimeLineVD;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class NewFXMain extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        LineTimeLineVD videobject = new LineTimeLineVD();
        String initdir = System.getProperty("user.home")+"\\Downloads\\";
        String filesize="filesize : ";
        
        Button pastebtn = new Button();
        Button downbtn = new Button();
        Button savebtn= new Button();
        
        Label linklabel = new Label("Paste link here : ");
        Label sizelabel = new Label(filesize);
        Label dirlabel = new Label ("Save location   : ");
        
        TextField linkinput = new TextField();
        TextField savedirField = new TextField(initdir.concat("video.mp4"));
        
        ImageView imgvu=new ImageView();
        Image logoimage = new Image("file:./logo.png");
        ImageView logo=new ImageView(logoimage);
        
        FileChooser filesaver = new FileChooser();
        configureFileChooser(filesaver, initdir);
        
        savebtn.setText ("Save dir");
        pastebtn.setText(" Paste  ");
        downbtn.setText("DOWNLOAD");
        
        pastebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String paste = LineTimeLineVD.getClipboards();
                    linkinput.setText(paste);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });
        
        linkinput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldvalue, String newvalue) {
                try{
                    System.out.println(newvalue);
                    videobject.init(newvalue);
                    sizelabel.setText(filesize.concat(videobject.filesizeString()));
                    Image thumbimage = new Image(LineTimeLineVD.THUMBCACHEPATH);
                    imgvu.setPreserveRatio(true);
                    imgvu.setFitWidth(150);
                    imgvu.setImage(thumbimage);
                }catch (Exception ex) {
                    
                }
            }            
        });
        
        downbtn.setOnAction((ActionEvent event) -> {
            videobject.downloadvideo(savedirField.getText());
            JOptionPane.showMessageDialog(null, "Success");
            sizelabel.setText(filesize);
        });
        
        savebtn.setOnAction((ActionEvent event)->{
            File filedir = filesaver.showSaveDialog(primaryStage);
            if(filedir != null){
                String getdir = filedir.getPath();
                savedirField.setText(getdir);
            }
        });
        
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(10);
        
        GridPane.setConstraints(logo,1,0);
        GridPane.setConstraints(linklabel,0,1);
        GridPane.setConstraints(linkinput,1,1);
        GridPane.setConstraints(pastebtn,2,1);
        
        GridPane.setConstraints(dirlabel,0,2);
        GridPane.setConstraints(savedirField,1,2);
        GridPane.setConstraints(savebtn,2,2);

        GridPane.setConstraints(downbtn, 1,4);
        GridPane.setConstraints(imgvu, 1, 6);
        GridPane.setConstraints(sizelabel, 1, 5);
        
        grid.getChildren().addAll(logo,linklabel,linkinput,pastebtn,dirlabel,savedirField,savebtn,downbtn,imgvu,sizelabel);
        Scene scene = new Scene(grid, 500, 400);
        
        primaryStage.setTitle("LINE_VIDEO_DOWNLOADER");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private static void configureFileChooser(
        final FileChooser fileChooser, String defaultdir) {      
            fileChooser.setTitle("View Pictures");
            fileChooser.setInitialDirectory(
                new File(defaultdir)
            );                 
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 - Video", "*.mp4"),
                new FileChooser.ExtensionFilter("All files", "*.*")
            );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
