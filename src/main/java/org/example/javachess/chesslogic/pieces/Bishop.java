package org.example.javachess.chesslogic.pieces;

import java.util.ArrayList;
import java.util.List;

import org.example.javachess.chesslogic.Board;
import org.example.javachess.chesslogic.Move;

public class Bishop extends Piece {

	public Bishop(boolean isWhite, int[] position){
		super(isWhite, position);
		super.fen = "b";
	}

	@Override
	public Move[] getMoves(boolean checking) {
		List<Move> moves = new ArrayList<Move>();
		
		int i;
		//hoch rechts
		i = 1;
		while (position[0] + i < 8 && position[1] + i < 8 && (Board.getPosition(position[0] + i, position[1] + i) == null || Board.getPosition(position[0] + i, position[1] + i).isWhite != isWhite )) {
			Move test_move = new Move(position[0] + i, position[1] + i, Board.getPosition(position[0] + i, position[1] + i));
			if (isWhite) {
				if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			else{
				if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			i++;
		}
		//runter rechts
		i = 1;
		while (position[0] + i < 8 && position[1] - i >= 0 && (Board.getPosition(position[0] + i, position[1] - i) == null || Board.getPosition(position[0] + i, position[1] - i).isWhite != isWhite )) {
			Move test_move = new Move(position[0] + i, position[1] - i, Board.getPosition(position[0] + i, position[1] - i));
			if (isWhite) {
				if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			else{
				if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			i++;
		}
		//runter links
		i = 1;
		while (position[0] - i >= 0 && position[1] - i >= 0 && (Board.getPosition(position[0] - i, position[1] - i) == null || Board.getPosition(position[0] - i, position[1] - i).isWhite != isWhite )) {
			Move test_move = new Move(position[0] - i, position[1] - i, Board.getPosition(position[0] - i, position[1] - i));
			if (isWhite) {
				if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			else{
				if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			i++;
		}
		//hoch links
		i = 1;
		while (position[0] - i >= 0 && position[1] + i < 8 && (Board.getPosition(position[0] - i, position[1] + i) == null || Board.getPosition(position[0] - i, position[1] + i).isWhite != isWhite )) {
			Move test_move = new Move(position[0] - i, position[1] + i, Board.getPosition(position[0] - i, position[1] + i));
			if (isWhite) {
				if (!checking || !Board.kingWhite.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			else{
				if (!checking || !Board.kingBlack.checkCheck(test_move, this)) {
					moves.add(test_move);
				}
			}
			i++;
		}

		return moves.toArray(new Move[moves.size()]);
	}

	@Override
	public Bishop getClone() {
		return new Bishop(isWhite, position);
	}
}
