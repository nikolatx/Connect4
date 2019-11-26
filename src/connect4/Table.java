
package connect4;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Table {

    static int tablePadding=2;
    static int panePadding=2;
    static int circlePadding=2;
    
    public static GridPane drawTable1() {
        GridPane table=new GridPane();
        for(int i=0;i<7;i++)
            for(int j=0;j<6;j++) {
                StackPane pane=new StackPane();
                table.add(pane,i,j);
                Circle circle=new Circle(40);
                circle.setStrokeType(StrokeType.INSIDE);
                circle.setStroke(Color.WHITE);
                circle.setFill(Color.WHITE);
                pane.setPadding(new Insets(3,3,3,3));
                pane.getChildren().add(circle);
                pane.setStyle("-fx-background-color:  #9999b5;");
            }
        table.setStyle("-fx-line-visible: true;-fx-vgap: 1;-fx-hgap: 1;-fx-alignment: CENTER;-fx-padding: 1;");
        return table;
    }

    public static GridPane drawTable(Scene scene) {
        GridPane table=new GridPane();
        for(int i=0;i<7;i++)
            for(int j=0;j<6;j++) {
                StackPane pane=new StackPane();
                table.add(pane,i,j);
                Circle circle=new Circle();
                
                //NumberBinding circleRProperty=(Bindings.subtract(Bindings.min(scene.widthProperty(), scene.heightProperty()), 2*tablePadding).divide(7).subtract(2*panePadding).subtract(2*circlePadding)).divide(2);
                NumberBinding circleRProperty=(Bindings.subtract(scene.widthProperty(), 2*tablePadding).divide(7).subtract(2*panePadding).subtract(2*circlePadding)).divide(2).subtract(0);
                circle.radiusProperty().bind(circleRProperty);
                
                //circle.setStrokeType(StrokeType.INSIDE);
                //circle.setStroke(Color.WHITE);
                circle.setFill(Color.WHITE);
                pane.setPadding(new Insets(3,3,3,3));
                pane.getChildren().add(circle);
                pane.setStyle("-fx-background-color:  #9999b5;");
            }
        table.setStyle("-fx-line-visible: true;-fx-vgap: 1;-fx-hgap: 1;-fx-alignment: CENTER;-fx-padding: 2;");
        return table;
    }
    
    
    public static void cleanTable(GridPane pane) {
        ObservableList<Node> children = pane.getChildren();
        for (Node node1 : children) {
            Pane pane1=(Pane)node1;
            Node node=pane1.getChildren().get(0);
            if (node instanceof Circle) {
                Circle circle=(Circle) node;
                resetCircle(circle);
            }
        }
    }
    
    public static void cleanCell(GridPane pane, int row, int col) {
        Circle result = null;
        ObservableList<Node> children = pane.getChildren();
        for (Node node1 : children) {
            Pane pane1=(Pane) node1;
            Node node=pane1.getChildren().get(0);
            if(pane.getRowIndex(node1) == row && pane.getColumnIndex(node1) == col && node instanceof Circle) {
                result = (Circle) node;
                break;
            }
        }
        if (result!=null)
            resetCircle(result);
    }
    
    
    private static void resetCircle(Circle circle) {
        circle.setStroke(Color.WHITE);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.WHITE);
    }
    
    public static void turnToRed(Circle circle) {
        circle.setFill(new RadialGradient(260,0,0.5,1,1,true,CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#F5D0D2")), 
            new Stop(1, Color.web("#B0050E")) ));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Color.web("#B0031f"));
    }

    public static void turnToYellow(Circle circle) {
        circle.setFill(new RadialGradient(260,0,0.5,1,1,true,CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#f0e1c7")), 
            new Stop(1, Color.web("#e0d500")) ));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Color.web("#e3d90e"));
    }
    
    
}
