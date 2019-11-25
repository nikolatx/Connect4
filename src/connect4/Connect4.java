
package connect4;

import static connect4.Table.tablePadding;
import java.awt.Color;
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
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class Connect4 extends Application {
    
    private Label redLabel=new Label("Broj poteza crvenog:");
    private static Label redMoves=new Label("0");
    private static int redMovesNum=0;
    private Label yellLabel=new Label("Broj poteza zutog:");
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
    private int[] winningComb=new int[4];
    private static int choice=0;
    
    int tablePadding=2;
    int panePadding=2;
    int circlePadding=2;
    Font font;
    private DoubleProperty fontSize = new SimpleDoubleProperty(10);
    
    
    @Override
    public void start(Stage primaryStage) {
        
        
        circleOnMove.setStrokeWidth(1);
        Table.turnToRed(circleOnMove);
        
        HBox leviHB = new HBox();
        VBox leviVB1 = new VBox();
        VBox leviVB2 = new VBox();
        
        VBox srednjiHB = new VBox();
        HBox desniHB = new HBox();
        VBox desniVB1 = new VBox();
        VBox desniVB2 = new VBox();
        
        leviVB1.getChildren().add(redLabel);
        leviVB2.getChildren().add(redMoves);
        leviVB1.setAlignment(Pos.CENTER_LEFT);
        leviVB2.setAlignment(Pos.CENTER_RIGHT);
        
        desniVB1.getChildren().add(yellLabel);
        desniVB2.getChildren().add(yellMoves);
        desniVB1.setAlignment(Pos.CENTER_RIGHT);
        desniVB2.setAlignment(Pos.CENTER_RIGHT);
        
        leviHB.getChildren().addAll(leviVB1, leviVB2);
        leviHB.setAlignment(Pos.CENTER_LEFT);
        leviHB.setSpacing(2);
        desniHB.setAlignment(Pos.CENTER_RIGHT);
        desniHB.setSpacing(2);
        desniHB.getChildren().addAll(desniVB1, desniVB2);
        
        srednjiHB.setAlignment(Pos.CENTER);
        srednjiHB.getChildren().add(circleOnMove);
        
        
        leviHB.setPadding(new Insets(14));
        desniHB.setPadding(new Insets(10));
        srednjiHB.setPadding(new Insets(4));
        
        BorderPane headerBox=new BorderPane();
        headerBox.setLeft(leviHB);
        headerBox.setRight(desniHB);
        headerBox.setCenter(srednjiHB);
        
        //srednjiHB.setStyle("-fx-background-color: red;");



        
        HBox footerBox=new HBox();
        footerBox.getChildren().add(btn);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(10,10,10,10));
        
        
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);
        
       
        
        
        
        
        
        //VBox root = new VBox();
        BorderPane root = new BorderPane();
        
        
        //root.getChildren().addAll(headerBox, table, footerBox);
        root.setTop(headerBox);
        //root.setCenter(table);
        root.setBottom(footerBox);


        
        
        
        Scene scene = new Scene(root, 380, 650);
        table=Table.drawTable(scene);
        
        
        AnchorPane ap=new AnchorPane();
        
        ap.getChildren().add(table);
        //sp.setStyle("-fx-background-color: red;");
        root.setCenter(ap);
        
        
        
        
        
        NumberBinding circleRProperty=(Bindings.subtract(scene.widthProperty(), 2*tablePadding).divide(7).subtract(2*panePadding).subtract(2*circlePadding)).divide(2);
        circleOnMove.radiusProperty().bind(circleRProperty);
        
        //leviHB.prefHeightProperty().bind(circleRProperty.multiply(2.0));
        //desniHB.prefHeightProperty().bind(circleRProperty.multiply(2.0));
        
        primaryStage.setMinWidth(180);
        primaryStage.setMinHeight(350);
        
        DoubleBinding w = primaryStage.heightProperty().subtract(70);
        primaryStage.minWidthProperty().bind(w);
        primaryStage.maxWidthProperty().bind(w);
        
        AnchorPane.setTopAnchor(table, 4.0);
        AnchorPane.setLeftAnchor(table, (scene.getWidth()-ap.getWidth())/50.0);
        
        
        
        
        fontSize.bind(scene.widthProperty().divide(35));
        
        redLabel.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        redMoves.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        yellLabel.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        yellMoves.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        
        leviVB2.setPrefWidth(circleRProperty.doubleValue());
        desniVB2.setPrefWidth(circleRProperty.doubleValue());
        
        leviHB.setMinWidth(circleRProperty.doubleValue());
        leviHB.setMaxWidth(circleRProperty.doubleValue());
        
        leviVB2.setStyle("-fx-background-color: red;");
        desniVB2.setStyle("-fx-background-color: red;");
        
        srednjiHB.setStyle("-fx-background-color: yellow;");
        
        leviHB.setStyle("-fx-background-color: orange;");
        desniHB.setStyle("-fx-background-color: orange;");
        
        desniHB.minWidthProperty().bind(leviHB.widthProperty());
        desniHB.maxWidthProperty().bind(leviHB.widthProperty());
        
        
        /* rezerva dobra 
        VBox leviHB = new VBox();
        VBox srednjiHB = new VBox();
        VBox desniHB = new VBox();
        leviHB.getChildren().addAll(redLabel, redMoves);
        leviHB.setAlignment(Pos.CENTER_LEFT);
        leviHB.setSpacing(10);
        desniHB.setAlignment(Pos.CENTER_RIGHT);
        desniHB.setSpacing(10);
        
        srednjiHB.setAlignment(Pos.CENTER);
        srednjiHB.getChildren().add(circleOnMove);
        
        desniHB.getChildren().addAll(yellLabel, yellMoves);
        
        leviHB.setPadding(new Insets(14));
        desniHB.setPadding(new Insets(14));
        srednjiHB.setPadding(new Insets(4));
        
        
        
        leviHB.setStyle("-fx-background-color: red;");
        
        
        //footerBox.getChildren().add(btn);
        //footerBox.setAlignment(Pos.CENTER);
        //footerBox.setPadding(new Insets(10,10,10,10));
        
        
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);
        
        
        AnchorPane.setTopAnchor(desniHB, 0.0);
        AnchorPane.setRightAnchor(desniHB, 0.0);
        
        AnchorPane.setLeftAnchor(srednjiHB, 0.0);
        AnchorPane.setRightAnchor(srednjiHB, 0.0);
        
        AnchorPane.setTopAnchor(srednjiHB, 0.0);
        
        
        Scene scene = new Scene(root, 380, 550);
        table=Table.drawTable(scene);
        AnchorPane.setBottomAnchor(table, 0.0);
        AnchorPane.setLeftAnchor(table, 0.0);
        AnchorPane.setRightAnchor(table, 0.0);
        
        root.getChildren().addAll(leviHB, srednjiHB, desniHB, table);
        NumberBinding circleRProperty=(Bindings.subtract(scene.widthProperty(), 2*tablePadding).divide(7).subtract(2*panePadding).subtract(2*circlePadding)).divide(2);
        circleOnMove.radiusProperty().bind(circleRProperty);
        
        leviHB.prefHeightProperty().bind(circleRProperty.multiply(2.0));
        desniHB.prefHeightProperty().bind(circleRProperty.multiply(2.0));
        
        primaryStage.setMinWidth(180);
        primaryStage.setMinHeight(350);
        
        DoubleBinding w = primaryStage.heightProperty().subtract(10);
        primaryStage.minWidthProperty().bind(w);
        primaryStage.maxWidthProperty().bind(w);
        
        fontSize.bind(scene.widthProperty().divide(35));
        
        redLabel.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        redMoves.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        yellLabel.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        yellMoves.styleProperty().bind(Bindings.concat("-fx-font-size:", fontSize.asString()));
        
        
        
        
        */
        
        
        
        
        
        
        /*
        primaryStage.widthProperty().addListener((o, oldValue, newValue)->{
            //primaryStage.setHeight(primaryStage.getHeight()+newValue.intValue()-oldValue.intValue());
            
            if(newValue.intValue()>oldValue.intValue() && newValue.intValue() > table.getHeight()+20) {
            primaryStage.setResizable(false);
            //primaryStage.setWidth(400);
            primaryStage.setWidth(oldValue.intValue());
            primaryStage.setResizable(true);
            }
        });
        */
        
        
        //primaryStage.setMaxWidth(table.getWidth()+10);
        

        //root.setAlignment(Pos.CENTER);
        
        
        //table.prefHeightProperty().bind(Bindings.min(scene.heightProperty(), scene.widthProperty()));
        //table.prefWidthProperty().bind(Bindings.min(scene.heightProperty(), scene.widthProperty()));
        
        
        
        //table.prefHeightProperty().bind(scene.heightProperty());
        //table.prefHeightProperty().bind(table.prefWidthProperty());
        
        /*
        int tablePadding=5;
        int panePadding=3;
        int circlePadding=5;
        StackPane sp=new StackPane();
        Circle circle=new Circle(40);
        sp.getChildren().add(circle);
        circle.setFill(javafx.scene.paint.Color.RED);
        NumberBinding circleRProperty=(Bindings.subtract(scene.widthProperty(), 2*tablePadding).divide(7).subtract(2*panePadding).subtract(2*circlePadding)).divide(2);

        circle.radiusProperty().bind(circleRProperty);
        root.setCenter(sp);
        */
        
        
        
        //radi preview sledeceg poteza
        table.setOnMouseMoved(e->{
            if (finished) {
                //utvrdjivanje celije GridPane-a na koju je kliknuto
                Node target=(Node) e.getTarget();
                //krug je potomak StackPane-a, pa se uzima roditelj kako bi se utvrdilo polje GridPane-a u kome je kursor
                if (target instanceof Circle)
                        target=target.getParent();
                if (target instanceof StackPane) {
                    //brise kuglicu sa prethodne pozicije
                    if (prevRow!=-1 && prevCol!=-1) {
                        Table.cleanCell(table, prevRow, prevCol);
                    }
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
        
        //kad kursor izadje iz okvira table, preview kuglica se brise
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
                            tt.setDuration(Duration.seconds(0.8)); 
                            //odabir objekta za animaciju
                            tt.setNode(animated); 
                            //podesavanje putanje objekta, pocetna - gornja tacka
                            Bounds bounds=circle1.getBoundsInParent();
                            int startX=(int) (bounds.getMinX());
                            int startY=(int) (bounds.getMinY());

                            //podesavanje putanje objekta - zavrsna - donja tacka
                            Bounds bounds1=circleObj.getParent().getBoundsInParent();
                            int endX=(int) (bounds1.getMinX());
                            int endY=(int) (bounds1.getMinY());
                            tt.setFromX(endX-3); //padding
                            tt.setFromY(startY-3);
                            tt.setToX(endX-3);
                            tt.setToY(endY-3);
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
                                int result=Controller.checkGameOver(tableMatrix, rowInd,colInd, redMovesNum, yellMovesNum, redTurn);
                                choice=0;


                                Platform.runLater(()->{
                                    //ispisivanje odgovarajuce poruke ukoliko je igra zavrsena, i upit za novu igru ili izlaz iz aplikacije
                                    if (result==1)
                                        choice=JOptionPane.showConfirmDialog(null,"Pobednik je "+ (redTurn?"Crveni":"Zuti") + " igrac!\nDa li zelite novu igru?","Kraj",JOptionPane.YES_NO_OPTION);
                                    else if (result==0)
                                        choice=JOptionPane.showConfirmDialog(null,"Rezultat je neresen!\nDa li zelite novu igru?","Kraj",JOptionPane.YES_NO_OPTION);
                                    else
                                        redTurn=!redTurn;
                                    //zapocinjanje nove igre ili izlazak iz aplikacije u zavisnosti od izbora korisnika
                                    if (result>=0) {
                                        if (choice==1)
                                            Platform.exit();
                                        else {
                                            startNewGame();
                                        }
                                    }



                                });
                                /*
                                //ispisivanje odgovarajuce poruke ukoliko je igra zavrsena, i upit za novu igru ili izlaz iz aplikacije
                                if (result==1)
                                    choice=JOptionPane.showConfirmDialog(null,"Pobednik je "+ (redTurn?"Crveni":"Zuti") + " igrac!\nDa li zelite novu igru?","Kraj",JOptionPane.YES_NO_OPTION);
                                else if (result==0)
                                    choice=JOptionPane.showConfirmDialog(null,"Rezultat je neresen!\nDa li zelite novu igru?","Kraj",JOptionPane.YES_NO_OPTION);
                                else
                                    redTurn=!redTurn;
                                //zapocinjanje nove igre ili izlazak iz aplikacije u zavisnosti od izbora korisnika
                                if (result>=0) {
                                    if (choice==1)
                                        Platform.exit();
                                    else {
                                        startNewGame();
                                    }
                                }
                                */
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
        //primaryStage.setResizable(false);
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(scene);
        
        primaryStage.show();
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
