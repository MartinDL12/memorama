package root;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class App extends Application{

    public static void main(String[] args){
        App.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root;
        root = (AnchorPane)  FXMLLoader.load(App.class.getResource("/root/rootView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Memorama");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
