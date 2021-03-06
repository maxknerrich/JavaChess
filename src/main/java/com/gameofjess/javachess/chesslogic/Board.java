package com.gameofjess.javachess.chesslogic;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameofjess.javachess.chesslogic.pieces.*;


public class Board {
	/**
	 * The Class representing the Chessboard
	 */
	private static final Logger log = LogManager.getLogger(Board.class);

	private BidiMap<Position, Piece> board = new DualHashBidiMap<>();

	private King kingWhite;
	private King kingBlack;

	/**
	 * Create a new Chessboard and set it to the initial position
	 */
	public Board(){
        this.initialize();
		log.info("Creating board");
	}

	/**
	 * Constructor to deep clone a Chessboard
	 * @param board Board to be cloned
	 */
	public Board(Board board){
		log.trace("Cloning board");
		this.board = new DualHashBidiMap<>(board.getBoardMap().entrySet().stream().parallel().collect(Collectors.toMap(
				entry -> entry.getKey().getClone(), entry -> entry.getValue().getClone(this)
		)));
		this.kingWhite = (King) this.board.values().parallelStream().filter(piece -> piece instanceof King && piece.isWhite()).collect(Collectors.toList()).get(0);
		this.kingBlack = (King) this.board.values().parallelStream().filter(piece -> piece instanceof King && !piece.isWhite()).collect(Collectors.toList()).get(0);
	}

	/**
	 * Method to get the position of a given Piece
	 * @param piece Piece to et the Position from
	 * @return Position of the passed Piece
	 */
	public  Position getPosition(Piece piece){
		log.trace("Getting Position of {}", piece.getClass().getSimpleName());
		 return board.getKey(piece);
	}

	/**
	 * Set the Board to the initial position
	 */
	void initialize() {
        log.trace("Initializing board...");
		board.put(new Position(0, 0), new Rook(this, true, true));
		board.put(new Position(1, 0), new Knight(this, true));
		board.put(new Position(2, 0), new Bishop(this, true));
		kingWhite = new King(this, true);
		board.put(new Position(3, 0), kingWhite);
		board.put(new Position(4, 0), new Queen(this, true));
		// board.put(new Position(4, 0), new King(this, true));
		board.put(new Position(5, 0), new Bishop(this, true));
		board.put(new Position(6, 0), new Knight(this, true));
		board.put(new Position(7, 0), new Rook(this, true, true));

		for (int i = 0; i < 8; i++) {
			board.put(new Position(i, 1), new Pawn(this, true));
		}

		board.put(new Position(0, 7), new Rook(this, false, true));
		board.put(new Position(1, 7), new Knight(this, false));
		board.put(new Position(2, 7), new Bishop(this, false));
		kingBlack = new King(this, false);
		board.put(new Position(3, 7), kingBlack);
		board.put(new Position(4, 7), new Queen(this, false));
		// board.put(new Position(4, 7), new King(this, false));
		board.put(new Position(5, 7), new Bishop(this, false));
		board.put(new Position(6, 7), new Knight(this, false));
		board.put(new Position(7, 7), new Rook(this, false, true));

		for (int i = 0; i < 8; i++) {
			board.put(new Position(i, 6), new Pawn(this, false));
		}

	}

	void initialize_check(){
		kingBlack = new King(this, false);
		board.put(new Position(6, 7), kingBlack);
		kingWhite = new King(this, true);
		board.put(new Position(2, 2), kingWhite);
		board.put(new Position(4, 4), new Bishop(this, true));
		board.put(new Position(3, 6), new Queen(this, true));
	}

	void initialize_promotion(){
		kingBlack = new King(this, false);
		board.put(new Position(6, 7), kingBlack);
		kingWhite = new King(this, true);
		board.put(new Position(2, 2), kingWhite);
		board.put(new Position(4, 4), new Bishop(this, true));
		board.put(new Position(3, 6), new Queen(this, true));
		board.put(new Position(1,6), new Pawn(this, true));
	}

	void initialize_rochade(){
		board.put(new Position(0, 0), new Rook(this, true, true));
		kingWhite = new King(this, true);
		board.put(new Position(3, 0), kingWhite);
		board.put(new Position(7, 0), new Rook(this, true, true));
		board.put(new Position(0, 7), new Rook(this, false, true));
		kingBlack = new King(this, false);
		board.put(new Position(3, 7), kingBlack);
		board.put(new Position(7, 7), new Rook(this, false, true));

	}

	/**
	 * @return an immutable copy version of the Map representing the Chess bord
	 */
	public BidiMap<Position, Piece> getBoardMap() {
		return org.apache.commons.collections4.bidimap.UnmodifiableBidiMap.unmodifiableBidiMap(board);
	}

	/**
	 * @return the kingWhite
	 */
	public King getKingWhite() {
		return kingWhite;
	}

	/**
	 * @return the kingBlack
	 */
	public King getKingBlack() {
		return kingBlack;
	}

	/**
	 * Capture a Chess piece at a given Position
	 * @param position Position to get the Piece from
	 */
	public void capture(Position position){
		log.trace("Capturing Piece");
		board.removeValue(getPiece(position));
	}

	/**
	 * get the piece from a Position or null
	 * @param position Position to get the Piece from
	 * @return piece
	 */
	private Piece getPiece(Position position) {
		return board.get(position);
	}

	public void boardMapAdd(Position position, Piece piece){
		board.put(position, piece);
	}

	/**
	 * Check if a given move is legal on the current Board
	 * @param move Move to be checked
	 * @return boolean
	 */
	public boolean isMoveValid(Move move){
		log.trace("Checking move validity");
		Piece testPiece = board.get(move.getOrigin());
		Move[] moves = testPiece.getMoves();

		return Arrays.stream(moves).parallel().anyMatch(move::equals);
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (Map.Entry<Position, Piece> entry : board.entrySet()) {
			out.append(entry.getKey());
			out.append(" | ");
			out.append(entry.getValue());
			out.append("\n");
		}
		return out.toString();
	}

	/**
	 * @param kingBlack the kingBlack to set
	 */
	public void setKingBlack(King kingBlack) {
		this.kingBlack = kingBlack;
	}

	/**
	 * @param kingWhite the kingWhite to set
	 */
	public void setKingWhite(King kingWhite) {
		this.kingWhite = kingWhite;
	}

	/**
	 * remove piece from board map
	 * @param position Position of the Piece to remove
	 */
	public void boardMapRemove(Position position) {
		board.remove(position);
	}
}
