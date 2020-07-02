package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private Board board;

    public ChessMatch( ) {
        this.board = new Board ( 8,8 );
        initialSetup ();
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
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.GREEN));
        placeNewPiece('c', 8, new Rook(board, Color.GREEN));
        placeNewPiece('d', 7, new Rook(board, Color.GREEN));
        placeNewPiece('e', 7, new Rook(board, Color.GREEN));
        placeNewPiece('e', 8, new Rook(board, Color.GREEN));
        placeNewPiece('d', 8, new King(board, Color.GREEN));

    }

    private void placeNewPiece(char collumn, int row, ChessPiece piece){            // calls place piece method with ChessPosition paramers trasnforming char collumn and int row with .toPosition
        board.placePiece(piece, new ChessPosition (collumn,row).toPosition ());     // transform Chessposition to Position and places a ChessPiece
    }
}
