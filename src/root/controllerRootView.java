package root;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import connector.connector;

public class controllerRootView implements Observer, Initializable {

    @FXML
    private ImageView esp1;

    @FXML
    private ImageView esp2;

    @FXML
    private ImageView esp3;

    @FXML
    private ImageView esp4;

    @FXML
    private ImageView esp5;

    @FXML
    private ImageView esp6;

    @FXML
    private Text txtSeg;

    @FXML
    private Text scoreTxt;
    @FXML
    private TextArea TopScore;

    @FXML
    private Button buttonIniciar;

    private Hashtable<Integer,Image> imagesRev = new Hashtable<>();
    private ArrayList<Image> tarjetas = new ArrayList<>();
    private ArrayList<ImageView> tarjetasView = new ArrayList<>();
    private boolean firstClicked = false;
    private boolean movible = false;
    private boolean seleccionado = false;
    private ImageView imagen1=new ImageView();
    private ImageView imagen = new ImageView();
    private int Ncompletados = 0;
    private int intUser = 0;
    private int intMax = 4;
    private boolean win = false;
    private Thread timer;
    public static boolean fin = false;
    public static int score = 0;
    private Connection conn1 = connector.getConnection();

    @FXML
    private Text intentoTxt;


    @FXML
    void inicio(ActionEvent event) throws SQLException {
        Hilo hilo = new Hilo();
        hilo.addObserver(this);

        timer = new Thread(hilo);
        timer.setDaemon(true);
        nuevaPartida();
        timer.start();
    }

    @FXML
    void buttonSalirOnMouseClicked(MouseEvent event) {
        System.exit(1);
    }

    @Override
    public void update(Observable o, Object arg) {

        String[] data = arg.toString().split(" ");

        txtSeg.setText(data[0]);
        int time = Integer.parseInt(data[0]);
        scoreTxt.setText(String.valueOf(score));
        System.out.println("int user: "+intUser);
        intentoTxt.setText(String.valueOf(intUser+1));
        if (data[1].equals("true")){
            try {
                nuevaPartida();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            intUser++;
            if(intUser == intMax) {
                win=true;
                movible = false;
                fin = true;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img = new Image("/root/1.png");
        Image img2 = new Image("/root/1.png");
        Image img3 = new Image("/root/3.png");
        Image img4 = new Image("/root/3.png");
        Image img5 = new Image("/root/4.jpg");
        Image img6 = new Image("/root/4.jpg");
        tarjetas.add(img);
        tarjetas.add(img2);
        tarjetas.add(img3);
        tarjetas.add(img4);
        tarjetas.add(img5);
        tarjetas.add(img6);
        tarjetasView.add(esp1);
        tarjetasView.add(esp2);
        tarjetasView.add(esp3);
        tarjetasView.add(esp4);
        tarjetasView.add(esp5);
        tarjetasView.add(esp6);
        esp1.setImage(img);
        revolver();
        firstClicked = true;
        try {
            topScore();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @FXML
    public void nuevaPartida() throws SQLException {

        Ncompletados = 0;
        seleccionado = false;
        System.out.println("inicio");
        if(firstClicked){
            firstClicked=false;
            ocultar();
        }
        else{
            revolver();
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        ocultar();
                    })
            );
            timeline.play();
            insertarScore();

        }

        System.out.println("Fin");
        buttonIniciar.setVisible(false);

    }

    public void revolver(){
        movible=false;
        System.out.println("movible false");
        ArrayList<Image> revolver = new ArrayList<>();
        for (Image t:
             tarjetas) {
            revolver.add(t);
        }
        int imgs=tarjetas.size()-1;
        int contador = 0;
        while(imgs>-1){
            int rand = (int)(Math.random()*imgs);
            Image img = revolver.remove(rand);
            tarjetasView.get(contador).setImage(img);
            tarjetasView.get(contador).setAccessibleRoleDescription(img.getUrl());
            tarjetasView.get(contador).setId(String.valueOf(contador));
            tarjetasView.get(contador).setOnMouseClicked(this::comprobar);
            imgs--;
            contador++;
        }
    }

    public void ocultar(){
        Image hide = new Image("root/0.jpg");
        for (ImageView t:
                tarjetasView) {
            t.setImage(hide);
        }
        movible=true;
        System.out.println("movible true");
    }

    @FXML
    void comprobar(MouseEvent event) {
        scoreTxt.setText(String.valueOf(score));
        if(!movible){}
        else if(fin){
            movible=false;
            infoAlert("Fin del juego","Time Over","your score is: "+score);
        }
        else{
            movible=false;
            Image hide = new Image("root/0.jpg");
            System.out.println("clicked");
            imagen = (ImageView)event.getSource();
            int id = Integer.parseInt(imagen.getId());
            Image img = new Image(imagen.getAccessibleRoleDescription());
            tarjetasView.get(id).setImage(img);

            if(seleccionado){
                int id2 = Integer.parseInt(imagen1.getId());
                if((imagen1.getAccessibleRoleDescription().compareTo(imagen.getAccessibleRoleDescription())==0) && (id!=id2)){
                    movible=true;
                    imagen1.setOnMouseClicked(null);
                    imagen.setOnMouseClicked(null);
                    seleccionado=false;
                    Ncompletados+=2;
                    score+=300;
                    if(Ncompletados==tarjetasView.size()){
                        infoAlert("","","+1000 puntos!");
                        score+=1000;
                        if(win){
                            infoAlert("Fin del juego","Time Over","your score is: "+score);
                            buttonIniciar.setVisible(true);
                        };
                    };
                }
                else{
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(1), e -> {
                                tarjetasView.get(Integer.parseInt(imagen.getId())).setImage(hide);
                                tarjetasView.get(Integer.parseInt(imagen1.getId())).setImage(hide);
                                movible=true;
                                if(score>0) score-=100;
                                System.out.println("termino timeline");
                            })
                    );
                    timeline.play();
                    seleccionado=false;
                }

                System.out.println("fin");
            }
            else {
                imagen = new ImageView();
                System.out.println("seleccionado");
                imagen1 = (ImageView)event.getSource();
                seleccionado=true;
                movible=true;
            }
        }
    }

    public static void infoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void topScore() throws SQLException {
        String query1 = "SELECT * FROM scores ORDER BY score DESC LIMIT 1";
        Statement st = conn1.createStatement();
        ResultSet rs = st.executeQuery(query1);
        while (rs.next()) {
            Score score1 = new Score(rs.getInt(2), rs.getString(3));
            TopScore.setText(score1.getUserName()+ ": "+ score1.getScore() );
        }

    }
     public  void insertarScore() throws SQLException {
         String query1 = "Insert into \"scores\" (\"score\", \"userName\") VALUES ('"+ score+"', 'Daniel')";
         Statement st = conn1.createStatement();
         st.execute(query1);
     }
}
