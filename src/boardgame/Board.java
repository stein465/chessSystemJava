package boardgame;

import chess.ChessPiece;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows<1 || columns<1){
            throw  new BoardException ( "Error creating board: there must be at least one row and one column" );
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int columns){  //Returns a piece in board, if pieces isnt placed yed, returns null
        if (!positionExists ( row,columns )){  //tests if the position exists before returns a piece
            throw new BoardException ( "Position not on the board" );
        }
        return pieces[row][columns];
    }

    public Piece piece(Position position){
        if (!positionExists ( position )){ //tests if the position exists before returns a piece
            throw new BoardException ( "Position not on the board" );
        }
        return pieces[position.getRow ()][position.getCollumn ()];
    }

    public void placePiece(Piece piece, Position position){
        if (thereIsAPiece ( position )){
            throw new BoardException ( "There is already a piece in this position " + position);
        }
        pieces[position.getRow ()][position.getCollumn ()] = piece;
        piece.position = position;
    }

    public Piece removePiece(Position position){
        if (!positionExists ( position )){              // test if position existis
            throw new BoardException ( "position not on the board" );
        }

        if (piece ( position )== null){     //testa se existe peça na posição
            return null;
        }

        Piece aux = piece ( position );    //se houver peça, captura o valor e guarda em aux
        aux.position = null;               // atualiza posição de aux para null (retira aux do tabuleiro)
        pieces[position.getRow ()][position.getCollumn ()] = null;    // piece on pieces matrix becomes null (remove pieces from pieces matrix on the board)

        return  aux;
    }

    public boolean positionExists(Position position){   //tests if the position exists using a Position
        return positionExists ( position.getRow (), position.getCollumn () );
    }

    private boolean positionExists(int row, int column){       //tests if the position exists using row and column
        return row >=0 && row < rows && column >=0 && column < columns;
    }

    public boolean thereIsAPiece(Position position){
        if (!positionExists ( position )){
            throw new BoardException ( "Position not on the board" );
        }
      return piece ( position ) != null;
    }
}
