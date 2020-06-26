package src;


import java.util.Iterator;
import java.util.LinkedList;

public class Player {
    public LinkedList<Piece> pieces;
    public boolean firstMove = true;
    public boolean canPlay = true;

    public Player(int playernum) {
        int[][][] piecearr = Piece.getAllShapes();
        this.pieces = new LinkedList();

        for(int i = 0; i < piecearr.length; ++i) {
            this.pieces.add(new Piece(piecearr[i], playernum));
        }

    }

    public int getScore() {
        int score = 0;

        Piece piece;
        for(Iterator it = this.pieces.iterator(); it.hasNext(); score += piece.getPoints()) {
            piece = (Piece)it.next();
        }

        return score;
    }
}
