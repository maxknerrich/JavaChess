package org.example.javachess.chesslogic.pieces;

import java.util.ArrayList;
import java.util.List;

import org.example.javachess.chesslogic.Board;
import org.example.javachess.chesslogic.Move;

public class King extends Piece {
	public boolean rochade = true;

	public King(boolean isWhite, int[] position){
		super(isWhite, position);
	}

	public King(boolean isWhite, int[] position, boolean rochade){
		super(isWhite, position);
		this.rochade = rochade;
	}

	@Override
	public Move[] getMoves() {
		List<Move> moves = new ArrayList<Move>();

		Piece test;
		//hoch
		test = Board.getPosition(position[0], position[1] + 1);
		if (position[1] + 1 < 8 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0], position[1] + 1, test));
		}
		//hoch rechts
		test = Board.getPosition(position[0] + 1, position[1] + 1);
		if (position[0] + 1 < 8 && position[1] + 1 < 8 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0] + 1, position[1] + 1, test));
		}
		//rechts
		test = Board.getPosition(position[0] + 1 , position[1]);
		if (position[0] + 1 < 8 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0] + 1, position[1], test));
		}
		//runter rechts
		test = Board.getPosition(position[0] + 1, position[1] - 1);
		if (position[0] + 1 < 8 && position[1] - 1 >= 0 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0] + 1, position[1] - 1, test));
		}
		//runter
		test = Board.getPosition(position[0], position[1] - 1);
		if (position[1] - 1 >= 0 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0], position[1] - 1, test));
		}
		//runter links
		test = Board.getPosition(position[0] - 1, position[1] - 1);
		if (position[0] - 1 >= 0 && position[1] - 1 >= 0 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0] - 1, position[1] - 1, test));
		}
		//links
		test = Board.getPosition(position[0] - 1, position[1]);
		if (position[0] - 1 >= 0 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0] - 1, position[1], test));
		}
		//hoch links
		test = Board.getPosition(position[0] - 1, position[1] + 1);
		if (position[0] - 1 >= 0 && position[1] + 1 < 8 && (test == null || test.isWhite != isWhite)) {
			moves.add(new Move(position[0] - 1, position[1] + 1, test));
		}

		if (rochade && Board.getPosition(5, position[1]) == null && Board.getPosition(6, position[1]) == null) {
			Piece rook = Board.getPosition(7, position[1]);
			if (rook instanceof Rook) {
				if (((Rook) rook).rochade) {
					moves.add(new Move(7, position[1], true));
				}
			}
		}

		if (rochade && Board.getPosition(1, position[1]) == null && Board.getPosition(2, position[1]) == null && Board.getPosition(3, position[1]) == null) {
			Piece rook = Board.getPosition(0, position[1]);
			if (rook instanceof Rook) {
				if (((Rook) rook).rochade) {
					moves.add(new Move(0, position[1], true));
				}
			}
		}

		return moves.toArray(new Move[moves.size()]);
	}

	@Override
	public void makeMove(Move move){
		rochade = false;
		if (move.rochade) {
			Board.getPosition(move.destinationX, move.destinationY).position = position;
		}
		position[0] = move.destinationX;
		position[1] = move.destinationY;
		if (move.capture != null) {
			move.capture.position = null;
			Board.capturedPieces.add(move.capture);
			Board.pieces.remove(move.capture);
		}
	}

	public boolean checkCheck(){
		for (Piece piece : Board.pieces) {
			for (Move move : piece.getMoves()) {
				if (move.destinationX == position[0] && move.destinationY == position[1]) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkCheck(Move test_move, Piece test_piece){
		List<Piece> piecesCopy = new ArrayList<Piece>();

		for (Piece piece : Board.pieces) {
			piecesCopy.add(piece.getClone());
		}

		for (Piece piece : piecesCopy) {
			if (piece.position[0] == test_piece.position[0] && piece.position[1] == test_piece.position[1]) {
				piece.makeMove(test_move);
			}
		}

		for (Piece piece : piecesCopy) {
			for (Move move : piece.getMoves()) {
				if (move.destinationX == position[0] && move.destinationY == position[1]) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public King getClone() {
		return new King(isWhite, position, rochade);
	}
}
