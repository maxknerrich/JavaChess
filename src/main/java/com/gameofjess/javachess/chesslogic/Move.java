package com.gameofjess.javachess.chesslogic;

public class Move {
	/**
	 * This Class represents a Move from a Piece on the Chessboard,
	 * including the Variations: Capture, Castling and Enpassant
	 */

//TODO geter seters

	Position origin;
	Position destination;
    Position capturePosition = null;
    boolean rochade = false;
    boolean enpassant = false;

    public Move(Position origin, Position destination) {
		this.origin = origin;
        this.destination = destination;
	}

    public Move(Position origin, Position destination, Position capturePosition, boolean enpassant) {
        this.origin = origin;
		this.destination = destination;
		this.capturePosition = capturePosition;
		this.enpassant = enpassant;
    }

    public Move(Position origin, Position destination, Position capturePosition) {
		this.origin = origin;
		this.destination = destination;
        this.capturePosition = capturePosition;
    }

    public Move(Position origin, Position destination, boolean rochade) {
		this.origin = origin;
		this.destination = destination;
        this.rochade = rochade;
    }

	/**
	 * @return the destination
	 */
	public Position getDestination() {
		return destination;
	}

	/**
	 * @return the capture
	 */
	public Position getCapturePosition() {
		return capturePosition;
	}

	public boolean getRochade() {
		return rochade;
	}

	public boolean getEnpassant() {
		return enpassant;
	}

	/**
	 * @return the origin
	 */
	public Position getOrigin() {
		return origin;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Move)) {
			return false;
		}
		else if (this == obj) {
			return true;
		}
		else{
			Move test = (Move) obj;
			if (origin == test.getOrigin() && destination == test.getDestination() && capturePosition == test.getCapturePosition() && rochade == test.getRochade() && enpassant == test.getEnpassant()) {
				return true;
			}
			else{
				return false;
			}
		}
	}
	
}
