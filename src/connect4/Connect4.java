
package connect4;

import java.util.List;
import java.util.Optional;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Connect4 extends Application {
    
    private Label redLabel=new Label("Broj poteza crvenog:");
    private static Label redMoves=new Label("0");
    private static int redMovesNum=0;
    private Label yellLabel=new Label("Broj poteza žutog:");
    private static Label yellMoves=new Label("0");
    private static int yellMovesNum=0;
    private Label onMove=new Label("Na potezu:");
    private Button btn=new Button("Nova igra");
    static Circle circleOnMove=new Circle(25);
    private static GridPane table=new GridPane();
    private int prevCol=-1;
    private int prevRow=-1;
    private static int tableMatrix[][]=new int[6][7];
    private static boolean finished=true;
    private static boolean redTurn=true;
    TranslateTransition tt = new TranslateTransition(); 
    Optional<ButtonType> alertResult;
    Font font;

    private DoubleProperty fontSize = new SimpleDoubleProperty(10);
    
    @Override
    public void start(Stage primaryStage) {
        
        //podesavanje centralnog gornjeg kruga - takmicar na potezu
        circleOnMove.setStrokeWidth(1);
        Table.turnToRed(circleOnMove);
        
        VBox leviVB = new VBox();
        HBox leviHB = new HBox();
        VBox srednjiHB = new VBox();
        VBox desniVB = new VBox();
        HBox desniHB = new HBox();

        //natpis o broju poteza i broj poteza crvenog takmicara
        leviHB.getChildren().add(redMoves);
        leviHB.setAlignment(Pos.CENTER);
        leviVB.getChildren().addAll(redLabel, leviHB);
        leviVB.setAlignment(Pos.CENTER_LEFT);
        
        //natpis o broju poteza i broj poteza zutog takmicara
        desniHB.getChildren().add(yellMoves);
        desniHB.setAlignment(Pos.CENTER);
        HBox desniLabelBox=new HBox();
        //dodaje se spacer (rastegljivi region) jer je levi natpis duži od desnog, da bi krug u header-u bio u sredini
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        desniLabelBox.getChildren().addAll(spacer, yellLabel);
        desniVB.getChildren().addAll(desniLabelBox, desniHB);
        desniVB.setAlignment(Pos.CENTER_LEFT);
        //da bi natpis crvenog i zutog takmicara bili iste sirine radi se bind
        desniLabelBox.prefWidthProperty().bind(leviVB.widthProperty());
        
        //podesavanje srednjeg dela header-a
        srednjiHB.setAlignment(Pos.CENTER);
        srednjiHB.getChildren().add(circleOnMove);
        
        //ubacivanje u header natpisa i kruga
        BorderPane headerBox=new BorderPane();
        headerBox.setLeft(leviVB);
        headerBox.setRight(desniVB);
        headerBox.setCenter(srednjiHB);
        
        //podesavanje footer-a: dugmeta za novu igru
        HBox footerBox=new HBox();
        footerBox.getChildren().add(btn);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(10,10,10,10));
        
        //glavni layout manager je BorderPane u cija se polja smestaju header, footer i AnchorPane
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 570, 650);
        table=Table.drawTable(scene);
        
        //tabela se povecava pri resize
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);
        
        //header i footer se ubacuju u BorderPane
        root.setTop(headerBox);
        root.setBottom(footerBox);
        
        //tebela se ubacuje u AnchorPane koji se pozicionira u centralni deo BorderPane
        AnchorPane ap=new AnchorPane();
        ap.getChildren().add(table);
        root.setCenter(ap);
        
        //tabela se ankerise za gornju, levu i desnu ivicu AnchorPane
        AnchorPane.setTopAnchor(table, 0.0);
        AnchorPane.setLeftAnchor(table, 0.0);
        AnchorPane.setRightAnchor(table, 0.0);
        AnchorPane.setBottomAnchor(table, 0.0);
        
        //velicinu prozora diktira poluprecnik kruga u tabeli i to je faktor za skalitanje svih kontrola na prozoru
        NumberBinding circleRProperty=(Bindings.subtract(scene.widthProperty(), 2*Table.TABLE_PADING).divide(7).
                                        subtract(2*Table.PANE_PADDING).subtract(2*Table.CIRCLE_PADDING)).divide(2);
        //podesavanje velicine kruga za oznacavanje takmicara na potezu
        circleOnMove.radiusProperty().bind(circleRProperty);
        
        //podesavanje minimalnih dimenzija prozora
        primaryStage.setMinWidth(250);
        primaryStage.setMinHeight(350);
        
        //promena sirine mora da bude pracena promenom visine prozora    
        DoubleBinding w = primaryStage.widthProperty().multiply(0.85).add(footerBox.heightProperty().multiply(1.7)).add(headerBox.heightProperty());
        primaryStage.minHeightProperty().bind(w);
        primaryStage.maxHeightProperty().bind(w);
        
        //podesavanje maksimalne sirine prozora tako da visina ne predje visinu ekrana
        double screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
        primaryStage.setMaxWidth(screenHeight*0.94);
        
        //podesavanje skaliranja padding-a header boxa
        headerBox.setPadding(new Insets(circleRProperty.doubleValue()/8));
        
        //podesavanje skaliranja teksta u prozoru
        fontSize.bind(scene.widthProperty().divide(40));
        redLabel.styleProperty().bind(Bindings.concat("-fx-font-weight: bold;-fx-font-size:", fontSize.asString()));
        redMoves.styleProperty().bind(Bindings.concat("-fx-font-weight: bold;-fx-font-size:", fontSize.asString()));
        yellLabel.styleProperty().bind(Bindings.concat("-fx-font-weight: bold;-fx-font-size:", fontSize.asString()));
        yellMoves.styleProperty().bind(Bindings.concat("-fx-font-weight: bold;-fx-font-size:", fontSize.asString()));
        btn.styleProperty().bind(Bindings.concat("-fx-font-weight: bold;-fx-font-size:", fontSize.asString()));
        
        //preview sledeceg poteza
        table.setOnMouseMoved(e->{
            if (finished) {
                //utvrdjivanje celije GridPane-a na koju je kliknuto
                Node target=(Node) e.getTarget();
                //krug je potomak StackPane-a, pa se uzima roditelj kako bi se utvrdilo polje GridPane-a u kome je kursor
                if (target instanceof Circle)
                        target=target.getParent();
                if (target instanceof StackPane) {
                    //brise kuglicu sa prethodne pozicije
                    if (prevRow!=-1 && prevCol!=-1) 
                        Table.cleanCell(table, prevRow, prevCol);
                    //utvrdjuje kolonu i vrstu (polje) gridpane-a u koje treba ubaciti preview kuglicu
                    int colInd=GridPane.getColumnIndex(target);
                    int rowInd=Controller.getFreeRow(tableMatrix,colInd);
                    //ukoliko postoji slobodna vrsta u koloni ubaciti preview kuglicu
                    if (rowInd>=0) {
                        Circle circleObj=new Circle();
                        //pribavljanje reference na krug u zeljenom polju
                        circleObj=Controller.getCircleByRowColumnIndex(rowInd, colInd, table);
                        if (circleObj!=null) {
                            //markiranje kruga odgovarajucom bojom
                            if (redTurn) 
                                Table.turnToRed(circleObj);
                            else 
                                Table.turnToYellow(circleObj);
                            prevRow=rowInd;
                            prevCol=colInd;
                        }
                    }
                }
            }
        });
        
        //kad kursor izadje iz okvira tabele, preview kuglica se brise
        table.setOnMouseExited(e->{
            if (prevRow!=-1 && prevCol!=-1 && finished) {
                Table.cleanCell(table, prevRow, prevCol);
                prevRow=-1;
                prevCol=-1;
            }
        });
        
        //ubacivanje kuglice u zeljeno polje
        table.setOnMouseClicked(e->{
            if (finished) {
                //utvrdjivanje celije GridPane-a na koju je kliknuto
                Node target=(Node) e.getTarget();
                //krug je potomak StackPane-a, pa se uzima roditelj kako bi se utvrdilo polje GridPane-a na koje je kliknuto
                if (target instanceof Circle)
                        target=target.getParent();
                if (target instanceof StackPane) {
                    //obrisi prethodno postavljenu lopticu mousemove handlerom
                    if (prevRow!=-1 && prevCol!=-1 && finished) {
                        Table.cleanCell(table, prevRow, prevCol);
                        prevRow=-1;
                        prevCol=-1;
                    }
                    //utvrdjivanje kolone na koju je kliknuto i prve slobodne vrste za smestaj kuglice u istoj koloni
                    int colInd=GridPane.getColumnIndex(target);
                    int rowInd=Controller.getFreeRow(tableMatrix,colInd);
                    //ako postoji slobodna vrsta u koloni u nju se ubacuje kuglica
                    if (rowInd>=0) {
                        //pribavljanje reference na objekat kruga u zeljenom polju
                        final Circle circleObj=Controller.getCircleByRowColumnIndex(rowInd, colInd, table);
                        if (circleObj!=null) {
                            
                            //animacija spustanja loptice
                            //referenca na objekat kruga u prvoj vrsti i odabranoj koloni (pocetna tacka putanje animacije)
                            Circle circle1=Controller.getCircleByRowColumnIndex(0, colInd, table);
                            //krug koji se pomera
                            Circle animated=new Circle(40);
                            animated.radiusProperty().bind(circleRProperty);
                            //zadavanje boje u skladu sa igracem na potezu
                            if (redTurn)
                                Table.turnToRed(animated);
                            else
                                Table.turnToYellow(animated);
                            //dodavanje kruga u GridPanel
                            table.getChildren().add(animated);

                            //podesavanje parametara animacije
                            TranslateTransition tt = new TranslateTransition(); 
                            //podesavanje trajanja animacije
                            tt.setDuration(Duration.seconds(0.7*(rowInd+1)/6)); 
                            //odabir objekta za animaciju
                            tt.setNode(animated); 
                            //podesavanje putanje objekta, pocetna - gornja tacka
                            Bounds bounds=circle1.getBoundsInParent();
                            double startX=(int) (bounds.getMinX());
                            double startY=(int) (bounds.getMinY());

                            //podesavanje putanje objekta - zavrsna - donja tacka
                            Bounds bounds1=circleObj.getParent().getBoundsInParent();
                            double endX=(int) (bounds1.getMinX());
                            double endY=(int) (bounds1.getMinY());
                            tt.setFromX(endX-1.5); //padding
                            tt.setFromY(0);
                            tt.setToX(endX-1.5);
                            
                            if (endY>10)
                                endY=endY-(rowInd+1)*3;
                            tt.setToY(endY);
                            //podesavanje broja ciklusa animacije
                            tt.setCycleCount(1); 
                            tt.setAutoReverse(false); 
                            //pokretanje animacije
                            tt.play(); 

                            finished=false;

                            //nastavak kad se animacija zavrsi
                            tt.setOnFinished(f->{
                                finished=true;
                                tt.stop();
                                table.getChildren().remove(animated);

                                //postavljanje kuglice na zeljeno mesto
                                makeAMove(circleObj);
                                tableMatrix[rowInd][colInd]=redTurn?1:2;
                                //da se izbegne brisanje u MouseMoved handleru
                                prevRow=-1;
                                prevCol=-1;
                                //provera da li je kraj igre
                                List<Tuple> lista=Controller.checkGameOver(tableMatrix, rowInd,colInd, redMovesNum, yellMovesNum, redTurn);
                                
                                //ukoliko je igra zavrsena zapocinjanje nove igre ili izlazak iz aplikacije u zavisnosti od izbora korisnika
                                if (lista.size()>0) {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    //postavljanje ikone za alert box
                                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                    stage.getIcons().add(new Image(this.getClass().getResource("/resources/trophy.png").toString()));
                                     alert.setTitle("Kraj igre");
                                    ButtonType btnDa = new ButtonType("Da");
                                    ButtonType btnNe = new ButtonType("Ne");
                                    alert.getButtonTypes().setAll(btnDa, btnNe);
                                    if (lista.size()==1) alert.setHeaderText("Rezultat je nerešen!");
                                    else {
                                        //obelezavanje dobitne kombinacije
                                        for(Tuple t:lista) {
                                            Circle circ=Controller.getCircleByRowColumnIndex(t.getValue1(), t.getValue2(), table);
                                            Table.turnToBlue(circ, !redTurn);
                                        }
                                        alert.setHeaderText("Pobednik je "+ (redTurn?"Crveni":"Žuti") + " igrač!");
                                    }
                                    alert.setContentText("Da li želite novu igru?");
                                    Platform.runLater( () -> {
                                        alertResult=alert.showAndWait();
                                        if (alertResult.get()==btnNe)
                                            Platform.exit();
                                        else 
                                            startNewGame();
                                    });
                                } else
                                    redTurn=!redTurn;
                            });
                        }
                    }
                }
            }
        });
        
        //zapocinjanje nove igre klikom na dugme Nova igra
        btn.setOnAction(e-> {
            if(finished)
                startNewGame();
        });
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/resources/trophy.png"));
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
    
    //priprema nove igre
    private static void startNewGame() {
        redMovesNum=0;
        yellMovesNum=0;
        redMoves.setText("0");
        yellMoves.setText("0");
        Table.cleanTable(table);
        Table.turnToRed(circleOnMove);
        for(int i=0;i<6;i++)
            for(int j=0;j<7;j++)
                tableMatrix[i][j]=0;
        redTurn=true;
        finished=true;
    }
    
    //belezi potez na tabeli
    private static void makeAMove(Circle circle) {
        if (redTurn) {
            Table.turnToRed(circle);
            Table.turnToYellow(circleOnMove);
            redMovesNum++;
            redMoves.setText(String.valueOf(redMovesNum));
        } else {
            Table.turnToYellow(circle);
            Table.turnToRed(circleOnMove);
            yellMovesNum++;
            yellMoves.setText(String.valueOf(yellMovesNum));
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
