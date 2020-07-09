package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnTheBoard = new ArrayList<> (  );
    private List<Piece> capturedPieces = new ArrayList<> (  );
    private boolean check;
    private boolean checkMate;


    public ChessMatch( ) {
        this.board = new Board ( 8,8 );
        currentPlayer = Color.WHITE;
        piecesOnTheBoard = new ArrayList<> (  );
        check = false;
        checkMate = false;
        turn = 1;
        initialSetup ();
    }
    public boolean getCheckMate() {
        return checkMate;
    }

    public boolean getCheck(){
        return check;
    }
    public int getTurn() {
        return turn;
    }
    public Color getCurrentPlayer() {
        return currentPlayer;
    }


    public ChessPiece[][] getPieces(){                                                    // Program deve retornar peças do tipo Chesspiece[][], não do tipo Piece[][] no pacote boardgame
        ChessPiece[][] mat = new ChessPiece[board.getRows ()][board.getColumns ()];       //peças não colocadas no tabuleiro contiunam retornando null
        for (int i=0; i<board.getRows ();i++){                                            // Percorre todos os elementos da matriz Piece[][] e faz downcast para ChessPiece[][]
            for (int j=0; j< board.getColumns ();j++){
                mat[i][j] = (ChessPiece) board.piece ( i,j );
            }
        }
        return mat;
    }

    private void initialSetup(){
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop (board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop (board, Color.WHITE));
        placeNewPiece('b', 1, new Knight (board, Color.WHITE));
        placeNewPiece('g', 1, new Knight (board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.GREEN));
        placeNewPiece('e', 8, new King(board, Color.GREEN));
        placeNewPiece('d', 8, new Queen(board, Color.GREEN));
        placeNewPiece('h', 8, new Rook(board, Color.GREEN));
        placeNewPiece('c', 8, new Bishop (board, Color.GREEN));
        placeNewPiece('f', 8, new Bishop (board, Color.GREEN));
        placeNewPiece('b', 8, new Knight (board, Color.GREEN));
        placeNewPiece('g', 8, new Knight (board, Color.GREEN));
        placeNewPiece('a', 7, new Pawn(board, Color.GREEN));
        placeNewPiece('b', 7, new Pawn(board, Color.GREEN));
        placeNewPiece('c', 7, new Pawn(board, Color.GREEN));
        placeNewPiece('d', 7, new Pawn(board, Color.GREEN));
        placeNewPiece('e', 7, new Pawn(board, Color.GREEN));
        placeNewPiece('f', 7, new Pawn(board, Color.GREEN));
        placeNewPiece('g', 7, new Pawn(board, Color.GREEN));
        placeNewPiece('h', 7, new Pawn(board, Color.GREEN));


    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition ();
        validateSourcePosition ( position );
        return  board.piece ( position ).possibleMoves ();
    }

    public ChessPiece performChessMove(ChessPosition sourcePositon, ChessPosition targetPosition){
        Position source = sourcePositon.toPosition ();
        Position target = targetPosition.toPosition ();
        validateSourcePosition(source);
        validateTargetPosition ( source, target );
        Piece capturedPiece = makeMove(source, target);

        if (testCheck ( currentPlayer )){                   //tests if current player put him self on check
            undoMove ( source, target, capturedPiece );     // if true, undo the move
            throw new ChessException ( "you can't put yourself in check" );
        }
        check = testCheck ( opponet ( currentPlayer ) )? true: false;        //tests if opponet of current player is on check after current player's move
                                                                                //if true, check propety gets true value
        if (testCheckMate ( opponet ( currentPlayer ) )){
            checkMate = true;
        }
        else {
            nextTurn ();
        }

        return (ChessPiece) capturedPiece;
    }
    private Piece makeMove(Position source,Position target){
        ChessPiece p =(ChessPiece) board.removePiece ( source );
        p.increaseMoveCount ();
        Piece capturedpPiece = board.removePiece ( target );
        if (capturedpPiece != null){
            piecesOnTheBoard.remove ( capturedpPiece );
            capturedPieces.add ((ChessPiece) capturedpPiece );       // if captured piece is != from null, adds captured piece in List captured Pieces
        }

        board.placePiece ( p, target );

        return capturedpPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece) board.removePiece ( target );
        p.decreaseMoveCount ();
        board.placePiece ( p,source );
        if (capturedPiece != null){
            board.placePiece ( capturedPiece,target );
            capturedPieces.remove ( capturedPiece );
            piecesOnTheBoard.add ( capturedPiece );
        }
    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece ( position )){
            throw  new ChessException ( "there is not a piece on source position" );
        }
        if (currentPlayer !=  ((ChessPiece) board.piece ( position )).getColor ()){
            throw new ChessException ( "chosen piece belongs to adversary" );
        }
        if (!board.piece ( position ).isThereAnyPossibleMove ()){
            throw new ChessException ( "there is not any possibles moves for the chosen piece" );
        }

    }

    private void validateTargetPosition(Position source, Position target){
        if(!board.piece ( source ).possibleMove ( target )){
            throw new ChessException ( "chosen piece cannot move to target position" );
        }
    }

    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.GREEN: Color.WHITE;
    }

    private Color opponet(Color color){
        return (color == Color.WHITE) ? Color.GREEN : Color.WHITE;
    }

    private ChessPiece king(Color color){                       // retturn king using color
        List<Piece> list = piecesOnTheBoard.stream ().filter ( x-> ((ChessPiece) x).getColor () == color ).collect( Collectors.toList());
        for (Piece p: list){
            if (p instanceof King){
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException ( "There is not " + color + "king on de board" );
    }

    private boolean testCheck(Color color){
        Position kingPosition = king ( color ).getChessPosition ().toPosition ();
        List<Piece> opponentPieces = piecesOnTheBoard.stream ().filter ( x -> ((ChessPiece) x).getColor ()== opponet ( color ) ).collect( Collectors.toList());
        for (Piece p: opponentPieces){
            boolean[][] mat = p.possibleMoves ();
            if (mat[kingPosition.getRow ()][kingPosition.getCollumn ()]){
                return true;
            }

        }
        return false;
    }
    private boolean testCheckMate(Color color){
        if(!testCheck ( color   ));
        List<Piece> list = piecesOnTheBoard.stream ().filter ( x-> ((ChessPiece) x).getColor () == color ).collect( Collectors.toList());
        for (Piece p: list){
            boolean[][] mat = p.possibleMoves ();
            for (int i=0; i< board.getRows ();i++){
                for(int j=0;j< board.getColumns ();j++){
                    if (mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition ().toPosition ();
                        Position target = new Position ( i,j );
                        Piece capturedPiece = makeMove ( source,target );
                        boolean testCheck = testCheck ( color );
                        undoMove ( source,target,capturedPiece );
                        if (!testCheck){
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }


    private void placeNewPiece(char collumn, int row, ChessPiece piece){            // calls place piece method with ChessPosition paramers trasnforming char collumn and int row with .toPosition
        board.placePiece(piece, new ChessPosition (collumn,row).toPosition ());     // transform Chessposition to Position and places a ChessPiece
        piecesOnTheBoard.add ( piece );
    }


}
