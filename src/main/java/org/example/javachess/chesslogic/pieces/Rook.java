package org.example.javachess.chesslogic.pieces;

import java.util.ArrayList;
import java.util.List;

import org.example.javachess.chesslogic.Board;
import org.example.javachess.chesslogic.Move;
import org.example.javachess.chesslogic.Position;

public class Rook extends Piece {
    public boolean rochade = true;

    public Rook(boolean isWhite) {
        super(isWhite);
        super.fen = "r";
    }

    public Rook(boolean isWhite, boolean rochade) {
        super(isWhite);
        this.rochade = rochade;
    }

    @Override
    public Move[] getMoves(boolean checking) {

        List<Move> moves = new ArrayList<Move>();
		Position position = Board.getPosition(this);

        int i;
        // rechts
        i = 1;
        while (position.x + i < 8 && (Board.board.get( new Position(position.x + i, position.y)) == null
                || Board.board.get( new Position(position.x + i, position.y)).isWhite != isWhite)) {
            Move test_move = new Move(new Position(position.x + i, position.y),
                    Board.board.get( new Position(position.x + i, position.y)));
            if (isWhite) {
                if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            } else {
                if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            }
            i++;
        }
        // links
        i = 1;
        while (position.x - i >= 0 && (Board.board.get( new Position(position.x - i, position.y)) == null
                || Board.board.get( new Position(position.x - i, position.y)).isWhite != isWhite)) {
            Move test_move = new Move(new Position(position.x - i, position.y),
                    Board.board.get( new Position(position.x - i, position.y)));
            if (isWhite) {
                if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            } else {
                if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            }
            i++;
        }
        // hoch
        i = 1;
        while (position.y + i < 8 && (Board.board.get( new Position(position.x, position.y + i)) == null
                || Board.board.get( new Position(position.x, position.y + i)).isWhite != isWhite)) {
            Move test_move = new Move(new Position(position.x, position.y + i),
                    Board.board.get( new Position(position.x - i, position.y + i)));
            if (isWhite) {
                if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            } else {
                if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            }
            i++;
        }
        // runter
        i = 1;
        while (position.y - i >= 0 && (Board.board.get( new Position(position.x, position.y - i)) == null
                || Board.board.get( new Position(position.x, position.y - i)).isWhite != isWhite)) {
            Move test_move = new Move(new Position(position.x, position.y - i),
                    Board.board.get( new Position(position.x, position.y - i)));
            if (isWhite) {
                if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            } else {
                if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
                    moves.add(test_move);
                }
            }
            i++;
        }

        return moves.toArray(new Move[moves.size()]);
    }

    @Override
    public void makeMove(Move move) {
		rochade = false;
		if (move.capture != null) {
			Board.capturedPieces.add(move.capture);
		}
		Board.board.remove(Board.getPosition(this));
        Board.board.put(move.destination , this);
    }


    @Override
    public Rook getClone() {
        return new Rook(isWhite, rochade);
    }

}
