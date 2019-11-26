
package connect4;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Controller {
    
    //provera da li je igra zavrsena
    public static List<Tuple> checkGameOver(int[][] mat, int r, int c, int redMoves, int yellMoves, boolean redTurn) {
        
        List<Tuple> lista = new ArrayList<>();
        //inicijalizacija pomocnih promenljivih za dobitnu kombinaciju
        int i=0, j=0, ind=0;
        int p=0, startRow, startCol, player=redTurn?1:2;
        //provera po glavnoj dijagonali
        if (r>=c) {
            startRow=Math.abs(r-c);
            startCol=0;
        } else {
            startRow=0;
            startCol=Math.abs(r-c);
        }
        for(i=startRow, j=startCol; i<6 && j<7 && p<4; i++, j++) { 
            if (mat[i][j]==player)
                p++;
            else 
                p=0;
        }
        //ako je pronadjena kombinacija sacuvaj koordinate
        if (p==4) {
            for(int i1=i-1, j1=j-1; p>0; i1--, j1--) {
                lista.add(new Tuple(i1,j1));
                p--;
            }
            return lista;
        }
        
        //provera po sporednoj dijagonali
        startRow=r+Math.min(c,5-r);
        startCol=c-Math.min(c,5-r);
        p=0;
        for(i=startRow, j=startCol; i>0 && j<7 && p<4; i--, j++) { 
            if (mat[i][j]==player)
                p++;
            else
                p=0;
        }
        //ako je pronadjena kombinacija sacuvaj koordinate
        if (p==4) {
            for(int i1=i+1, j1=j-1; p>0; i1++, j1--) {
                lista.add(new Tuple(i1,j1));
                p--;
            }
            return lista;
        }
        //provera po vertikali
        p=0;
        for(i=0;i<6 && p<4;i++) {
            if (mat[i][c]==player) 
                p++;
            else 
                p=0;
        }
        //ako je pronadjena kombinacija sacuvaj koordinate
        if (p==4) {
            for(int i1=i-1; p>0; i1--) {
                lista.add(new Tuple(i1,c));
                p--;
            }
            return lista;
        }
        //provera po horizontali
        p=0;
        for(i=0;i<7 && p<4;i++) {
            if (mat[r][i]==player)
                p++;
            else
                p=0;
        }
        //ako je pronadjena kombinacija sacuvaj koordinate
        if (p==4) {
            for(int i1=i-1; p>0; i1--) {
                lista.add(new Tuple(r, i1));
                p--;
            }
            return lista;
        }
        else if ((redMoves+yellMoves)==42) {
            //ako je nereseno, dodaj samo jedan element u listu
            lista.add(new Tuple(5,5));
            return lista;
        }
        //ukoliko nije kraj igre vrati praznu listu
        else
            return lista;
    }
    
    //vraca broj vrste prvog slobodnog polja u koloni col
    public static int getFreeRow(int[][] mat, int col) {
        for(int i=5;i>=0;i--)
            if (mat[i][col]==0)
                return i;
        return -1;
    }
    
    //vraca referencu na krug u polju (row, col) GridPane-a; u svakom polju GridPane-a je StackPane a u njemu krug
    public static Circle getCircleByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Circle result = null;
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node1 : children) {
            Node node=((StackPane)node1).getChildren().get(0);
            if(gridPane.getRowIndex(node1) == row && gridPane.getColumnIndex(node1) == column && node instanceof Circle) {
                result = (Circle)node;
                break;
            }
        }
        return result;
    }
    
    
    
}
