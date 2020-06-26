package src;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.awt.font.TextAttribute;

public class Board {
    public static final int NONE = 0;
    public static final int BLUE = 1;
    public static final int RED = 2;
    public static final int YELLOW = 3;
    public static final int GREEN = 4;
    public static final int BOARD_SIZE = 20;
    public static final int DEFAULT_RESOLUTION = 500;
    public static final Color BOARD_COLOR;
    public static final Color GRID_LINE_COLOR;
    public static final String OFF_BOARD_ERROR = "Piece must be placed entirely on the board.";
    public static final String ADJACENCY_ERROR = "Pieces of the same color cannot share edges with one another.";
    public static final String OVERLAP_ERROR = "Pieces cannot overlap.";
    public static final String START_ERROR = "Starting peice must occupy the player's respective corner.";
    public static final String CORNER_ERROR = "Pieces must be connected to at least one other piece of the the same color by the corner.";
    private int[][] grid = new int[20][20];
    private int[][] overlay = new int[20][20];
    private static boolean cblind;//changed

    public Board(boolean blind1) {
        this.reset(this.grid);
        this.reset(this.overlay);
        cblind = blind1;//changed
    }

    public boolean isValidMove(Piece piece, int xpos, int ypos, boolean firstmove) throws Board.IllegalMoveException {
        boolean isValid = false;

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                int isPiece = piece.getValue(row, col);
                boolean validBound = this.isInBounds(row + xpos, col + ypos);
                if (validBound) {
                    int isBlank = this.grid[row + xpos][col + ypos];
                    if (isBlank != 0) {
                        if (isPiece == 3) {
                            throw new Board.IllegalMoveException("Pieces cannot overlap.");
                        }

                        if (isBlank == piece.getColor()) {
                            if (isPiece == 2) {
                                throw new Board.IllegalMoveException("Pieces of the same color cannot share edges with one another.");
                            }

                            if (isPiece == 1) {
                                isValid = true;
                            }
                        }
                    } else if (firstmove && isPiece == 3 && (new Point(row + xpos, col + ypos)).equals(this.getCorner(piece.getColor()))) {
                        isValid = true;
                    }
                } else if (isPiece == 3) {
                    throw new Board.IllegalMoveException("Piece must be placed entirely on the board.");
                }
            }
        }

        if (!isValid) {
            throw new Board.IllegalMoveException(firstmove ? "Starting peice must occupy the player's respective corner." : "Pieces must be connected to at least one other piece of the the same color by the corner.");
        } else {
            return true;
        }
    }

    public boolean isValidMove(Piece piece, int xpos, int ypos) throws Board.IllegalMoveException {
        return this.isValidMove(piece, xpos, ypos, false);
    }

    public void placePiece(Piece piece, int xpos, int ypos, boolean firstmove) throws Board.IllegalMoveException {
        this.isValidMove(piece, xpos, ypos, firstmove);

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                if (piece.getValue(row, col) == 3) {
                    int x = row + xpos;
                    int y = col + ypos;
                    this.grid[x][y] = piece.getColor();
                }
            }
        }

    }

    public void placePiece(Piece piece, int xpos, int ypos) throws Board.IllegalMoveException {
        this.placePiece(piece, xpos, ypos, false);
    }

    public Point getCoordinates(Point point, int boardres) {
        return new Point(point.x / (boardres / 20), point.y / (boardres / 20));
    }
    
    public void scanGrid(Piece piece,Hint hint, boolean firstturn) {
    	int x = 0 - Piece.SHAPE_SIZE / 2;
    	int y = 0 - Piece.SHAPE_SIZE / 2;
    	boolean done = false;
    	
    	//TODO add first turn method
    	while(done == false) {
    	try {
    		if(isValidMove(piece,x,y,firstturn)) {
    			//TODO Add hint class
    			hint.setXcoord(x);
    			hint.setYcoord(y);
    		}
    		
    		//placePiece(piece,x,y,firstturn);
    		break;
    	}
    	catch(Exception t){
    	 y=y+1;
    	 if(y==20) {
    		 x=x+1;
    		 y = 0;
    	 }
    		
    	 if(x==20) {done = true;}
    	 continue;
    	}
    	}
    }

    public void overlay(Piece piece, int xpos, int ypos) {
        this.reset(this.overlay);

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                if (this.isInBounds(row + xpos - 3, col + ypos - 3) && piece.getValue(row, col) == 3) {
                    this.overlay[row + xpos - 3][col + ypos - 3] = piece.getColor();
                }
            }
        }

    }

    public BufferedImage render(boolean blind) {
        return this.render(500,  blind);
    }

    public BufferedImage render(int size, boolean blind) {
        BufferedImage bimage = new BufferedImage(size, size, 1);
        int blocksize = size / 20;
        Graphics2D graphics = (Graphics2D)bimage.getGraphics();

        for(int i = 0; i < 20; ++i) {
            for(int k = 0; k < 20; ++k) {
                graphics.setColor(getColor(this.grid[i][k]));

                if (blind){

                    if (getColor(this.grid[i][k]).equals(Color.BLUE)){
                        String s = "BB";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize,k * blocksize); }
                    if (getColor(this.grid[i][k]).equals(Color.GREEN)){
                        String s = "GG";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize,k * blocksize);}
                    if (getColor(this.grid[i][k]).equals(Color.YELLOW)){
                        String s = "YY";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize,k * blocksize);}
                    if (getColor(this.grid[i][k]).equals(Color.RED)){
                        String s = "RR";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize,k * blocksize);}

                }
                if (this.overlay[i][k] != 0) {
                    graphics.setColor(this.blend(graphics.getColor(), getColor(this.overlay[i][k]), 0.1F));


                    if (blind){

                        if (getColor(this.overlay[i][k]).equals(Color.BLUE)){
                            String s = "BB";
                            AttributedString as = new AttributedString(s);
                            as.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, 1);
                            graphics.drawString(as.getIterator(),i * blocksize,k * blocksize);}
                        if (getColor(this.overlay[i][k]).equals(Color.GREEN)){
                            String s = "GG";
                            AttributedString as = new AttributedString(s);
                            as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                            graphics.drawString(as.getIterator(),i * blocksize,k * blocksize);}
                        if (getColor(this.overlay[i][k]).equals(Color.YELLOW)){
                            String s = "YY";
                            AttributedString as = new AttributedString(s);
                            as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                            graphics.drawString(as.getIterator(),i * blocksize,k * blocksize);}
                        if (getColor(this.overlay[i][k]).equals(Color.RED)){
                            String s = "RR";
                            AttributedString as = new AttributedString(s);
                            as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                            graphics.drawString(as.getIterator(),i * blocksize,k * blocksize);}

                    }



                }

                graphics.fillRect(i * blocksize, k * blocksize, blocksize, blocksize);
                graphics.setColor(GRID_LINE_COLOR);

                graphics.drawRect(i * blocksize, k * blocksize, blocksize, blocksize);
                if (this.grid[i][k] == 0) {
                    boolean cblindtest = false;
                    boolean cblindp1 = false;
                    boolean cblindp2 = false;
                    boolean cblindp4 = false;
                    boolean cblindp3 = false;
                    Point point = new Point(i, k);
                    if (this.getCorner(1).equals(point)) {
                        graphics.setColor(getColor(1));
                        if (blind){cblindp1=true;}
                        else{ cblindtest = true;}


                    } else if (this.getCorner(2).equals(point)) {
                        graphics.setColor(getColor(2));
                        if (blind){cblindp2=true;}
                        else{ cblindtest = true;}
                    } else if (this.getCorner(4).equals(point)) {
                        graphics.setColor(getColor(4));
                        if (blind){cblindp4=true;}
                        else{ cblindtest = true;}


                    } else if (this.getCorner(3).equals(point)) {
                        graphics.setColor(getColor(3));
                        if (blind){cblindp3=true;}
                        else{ cblindtest = true;}

                    }

                    if (cblindtest) {
                        graphics.fillOval(i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6, blocksize / 3, blocksize / 3);
                    }
                    if (cblindp2) {
                        graphics.fillRect(i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6, 10 , 10 );
                        String s = "RR";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6);
                    }
                    if (cblindp4) {
                        graphics.fillRect(i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6, 10 , 10 );
                        String s = "GG";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6);
                    }
                    if (cblindp3) {
                        graphics.fillRect(i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6, 10 , 10 );
                        String s = "YY";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6);
                    }
                    if (cblindp1) {
                        graphics.fillRect(i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6, 10 , 10 );
                        String s = "BB";
                        AttributedString as = new AttributedString(s);
                        as.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, 1);
                        graphics.drawString(as.getIterator(),i * blocksize + blocksize / 2 - blocksize / 6, k * blocksize + blocksize / 2 - blocksize / 6);
                    }
                }
            }
        }

        return bimage;
    }


    private Color blend(Color color1, Color color2, float colorchange) {
        int var4 = (int)((float)color1.getRed() * colorchange + (float)color2.getRed() * (1.0F - colorchange));
        int var5 = (int)((float)color1.getGreen() * colorchange + (float)color2.getGreen() * (1.0F - colorchange));
        int var6 = (int)((float)color1.getBlue() * colorchange + (float)color2.getBlue() * (1.0F - colorchange));
        return new Color(var4, var5, var6);
    }

    public void resetOverlay() {
        this.reset(this.overlay);
    }

    private void reset(int[][] grid) {
        for(int row = 0; row < 20; ++row) {
            for(int col = 0; col < 20; ++col) {
                grid[row][col] = 0;
            }
        }

    }

    private boolean isInBounds(int xpos, int ypos) {
        return xpos >= 0 && ypos >= 0 && xpos < 20 && ypos < 20;
    }

    private Point getCorner(int playernum) {
        switch(playernum) {
            case 1:
                return new Point(0, 0);
            case 2:
                return new Point(19, 0);
            case 3:
                return new Point(19, 19);
            case 4:
                return new Point(0, 19);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Color getColor(int playernum) {//changed
        /*switch(var0) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.RED;
            case 3:
                return Color.YELLOW;
            case 4:
                return  Color.GREEN;
            default:
                return BOARD_COLOR;
        }*/
        if (cblind) {
            switch(playernum) {
                case 1:
                    return new Color(128,128,128);
                case 2:
                    return new Color(47,79,79);
                case 3:
                    return new Color(0,0,0);
                case 4:
                    return  new Color(119,136,153);
                default:
                    return BOARD_COLOR;
            }
        }
        else {
            switch(playernum) {
                case 1:
                    return Color.BLUE;
                case 2:
                    return Color.RED;
                case 3:
                    return Color.YELLOW;
                case 4:
                    return  Color.GREEN;
                default:
                    return BOARD_COLOR;
            }
        }
    }

    public static String getColorName(int colorname) {
        switch(colorname) {
            case 1:
                return "Blue";
            case 2:
                return "Red";
            case 3:
                return "Yellow";
            case 4:
                return "Green";
            default:
                return "Unknown";
        }
    }

    static {
        BOARD_COLOR = Color.LIGHT_GRAY;
        GRID_LINE_COLOR = Color.GRAY;
    }

    public static class IllegalMoveException extends Exception {
        public IllegalMoveException() {
        }

        public IllegalMoveException(String ex) {
            super(ex);
        }
    }
}
