
package connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
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
    
    
    @Override
    public void start(Stage primaryStage) {
        
        circleOnMove.setStrokeWidth(1);
        Table.turnToRed(circleOnMove);
        
        VBox root=new VBox();
        HBox headerBox=new HBox();
        HBox footerBox=new HBox();
        
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);     
        spacer1.setMinWidth(Region.USE_PREF_SIZE);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);     
        spacer2.setMinWidth(Region.USE_PREF_SIZE);
        headerBox.setPadding(new Insets(10,10,10,10));
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setSpacing(10);
        headerBox.getChildren().addAll(redLabel,redMoves,spacer1,onMove,circleOnMove,spacer2,yellLabel,yellMoves);
        
        footerBox.getChildren().add(btn);
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(10,10,10,10));
        
        table=Table.drawTable();
        
        root.getChildren().addAll(headerBox,table,footerBox);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 612, 650);
        
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
        primaryStage.setResizable(false);
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
