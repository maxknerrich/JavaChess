package org.example.javachess.chesslogic;


import org.example.javachess.chesslogic.pieces.Piece;

public class Move {
	public Position destination;
    public Piece capture = null;
    public boolean rochade = false;
    public boolean enpassant = false;

    public Move(Position destination) {
        this.destination = destination;
	}

    public Move(boolean enpassant, Position destination) {
        this.destination = destination;
		this.enpassant = enpassant;
    }

    public Move(Position destination, Piece capture) {
		this.destination = destination;
        this.capture = capture;
    }

    public Move(Position destination, boolean rochade) {
		this.destination = destination;
        this.rochade = rochade;
    }
}
