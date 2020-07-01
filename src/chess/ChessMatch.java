package chess;

import boardgame.Board;
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
        board.placePiece ( new Rook ( board,Color.WHITE ), new Position ( 2,1 ) );
        board.placePiece ( new King ( board, Color.WHITE ),new Position ( 0,5 )  );
    }
}
