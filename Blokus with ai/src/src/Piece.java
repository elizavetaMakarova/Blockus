package src;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Piece {
    public static final int SHAPE_SIZE = 7;
    public static final int PIECE = 3;
    public static final int ADJACENT = 2;
    public static final int CORNER = 1;
    public static final int BLANK = 0;
    public static final int DEFAULT_RESOLUTION = 120;
    private int[][] grid;
    private int color;

    public Piece(int[][] x, int y) {
        if (x.length == 7 && x[0].length == 7) {
            this.grid = (int[][])x.clone();
            this.color = y;
        } else {
            throw new IllegalArgumentException("Shape array must be 7x7.");
        }
    }

    public void rotateClockwise() {
        int[][] temp = new int[7][7];

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                temp[7 - col - 1][row] = this.grid[row][col];
            }
        }

        this.grid = temp;
    }

    public void rotateCounterClockwise() {
        int[][] temp = new int[7][7];

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                temp[col][7 - row - 1] = this.grid[row][col];
            }
        }

        this.grid = temp;
    }

    public void flipOver() {
        int[][] temp = new int[7][7];

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                temp[7 - row - 1][col] = this.grid[row][col];
            }
        }

        this.grid = temp;
    }

    public int getValue(int xpos, int ypos) {
        return this.grid[xpos][ypos];
    }

    public int getColor() {
        return this.color;
    }

    public int getPoints() {
        int points = 0;

        for(int col = 0; col < 7; ++col) {
            for(int row = 0; row < 7; ++row) {
                if (this.grid[row][col] == 3) {
                    ++points;
                }
            }
        }

        return points;
    }

    public BufferedImage render(int blocksize) {
        BufferedImage block = new BufferedImage(blocksize, blocksize, 1);
        int blockscale = blocksize / 7;
        Graphics2D blockrender = (Graphics2D)block.getGraphics();
        blockrender.setColor(Color.WHITE);
        blockrender.fillRect(0, 0, blocksize, blocksize);

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                if (this.grid[row][col] == 3) {
                    blockrender.setColor(Board.getColor(this.color));
                    blockrender.fillRect(row * blockscale, col * blockscale, blockscale, blockscale);
                    blockrender.setColor(Color.BLACK);
                    blockrender.drawRect(row * blockscale, col * blockscale, blockscale, blockscale);
                }
            }
        }

        return block;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();

        for(int row = 0; row < 7; ++row) {
            for(int col = 0; col < 7; ++col) {
                str.append(this.grid[col][row]);
                str.append(" ");
            }

            str.append("\n");
        }

        return str.toString();
    }

    public static int[][][] getAllShapes() {
        int[][][] blockarr = new int[21][7][7];
        byte var1 = 0;
        int var2 = var1 + 1;
        blockarr[var1] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {1, 2, 2, 2, 2, 2, 1}, {2, 3, 3, 3, 3, 3, 2}, {1, 2, 2, 2, 2, 2, 1}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 1, 2, 1, 0, 0, 0}, {0, 2, 3, 2, 2, 2, 1}, {0, 2, 3, 3, 3, 3, 2}, {0, 1, 2, 2, 2, 2, 1}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 1, 2, 1, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 2, 3, 2, 1, 0}, {0, 0, 2, 3, 3, 2, 0}, {0, 0, 1, 2, 3, 2, 0}, {0, 0, 0, 1, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 1, 2, 3, 2, 2, 1}, {0, 2, 3, 3, 3, 3, 2}, {0, 1, 2, 2, 2, 2, 1}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 1, 2, 3, 2, 1, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 1, 2, 2, 3, 2, 0}, {0, 0, 0, 1, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 1, 2, 3, 2, 1, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 1, 2, 3, 2, 1, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 1, 2, 2, 2, 1, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 2, 3, 2, 3, 2, 0}, {0, 1, 2, 1, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 1, 2, 2, 2, 1, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 1, 2, 3, 3, 2, 0}, {0, 0, 1, 2, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 2, 1, 0}, {0, 0, 1, 2, 3, 2, 0}, {0, 1, 2, 3, 3, 2, 0}, {0, 2, 3, 3, 2, 1, 0}, {0, 1, 2, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 1, 2, 3, 2, 1, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 1, 2, 2, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 1, 2, 1, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 2, 3, 2, 2, 1}, {0, 0, 2, 3, 3, 3, 2}, {0, 0, 1, 2, 2, 2, 1}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 2, 1, 0}, {0, 0, 2, 3, 3, 2, 0}, {0, 1, 2, 3, 2, 1, 0}, {0, 2, 3, 3, 2, 0, 0}, {0, 1, 2, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 1, 2, 1, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 2, 1, 0}, {0, 1, 2, 3, 3, 2, 0}, {0, 2, 3, 3, 2, 1, 0}, {0, 1, 2, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 1, 2, 2, 1, 0, 0}, {0, 2, 3, 3, 2, 0, 0}, {0, 2, 3, 3, 2, 0, 0}, {0, 1, 2, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 1, 2, 3, 2, 1, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 1, 2, 2, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 1, 2, 2, 2, 2, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 1, 2, 2, 3, 2, 0}, {0, 0, 0, 1, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 1, 2, 2, 2, 1, 0}, {0, 2, 3, 3, 3, 2, 0}, {0, 1, 2, 2, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 2, 3, 2, 1, 0}, {0, 0, 2, 3, 3, 2, 0}, {0, 0, 1, 2, 2, 1, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        blockarr[var2++] = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 2, 3, 2, 0, 0}, {0, 0, 1, 2, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        return blockarr;
    }
}
