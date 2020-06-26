package src;


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.io.FileReader;

public class Blokus
{
    public static final int NUM_PLAYERS = 4;


    public Blokus(int players, boolean blind, boolean hints, int player1,int player2,int player3, boolean isSaved){

        BlokusFrame bf = new BlokusFrame(players,blind,hints,player1,player2,player3,isSaved);




    }

    public static class BlokusFrame extends JFrame
    {
        private Board board;
        private Player[] players;
        private int turn = -1;

        private int pieceIndex;
        private Point selected;
        private Hint hint = new Hint();

        private boolean blind;
        private JPanel mainPanel;
        private JPanel sidePanel;
        private JPanel piecesPanel;
        private JPanel boardPanel;
        private JLabel label,currentcoord;
        private ImageIcon boardImage;
        private JButton surrender,hints,save;
        private JTextArea hintsText, rules;
        public static int rotated = 0;

        public BlokusFrame(int playersNum , boolean blind1, boolean hints, int player1, int player2, int player3, boolean isSaved)
        {

            super("Blokus");
            board = new Board(blind1);
            blind = blind1;
            players = new Player[NUM_PLAYERS];
            if(player3==0) {
            	players[3] = new Player(Board.BLUE);
            }
            else {
            	players[3] = new Computer(Board.BLUE,player3);
            }
            
            if(player2==0) {
            	players[2] = new Player(Board.RED);
            }
            else {
            	players[2] = new Computer(Board.RED,player2);
            }
            if(player1==0) {
            	players[1] = new Player(Board.YELLOW);
            }
            else {
            	players[1] = new Computer(Board.YELLOW,player1);
            }
            players[0] = new Player(Board.GREEN);

            int rotated = 0;


            //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            if (isSaved){
            initializeGUI1(blind1,hints);}
            try{
                if (!isSaved){
            PrintWriter writer1 = new PrintWriter("data.txt");
            writer1.print("");
            writer1.close();}}
            catch(Exception K)
            {
                JOptionPane.showMessageDialog(null,"Saving file is damaged please start again");
            }


            initializeGUI(blind1,hints);
            startNewTurn();
        }
        public void initializeGUI1(boolean blind , boolean hint1){
        try {
            InputStream reader = new FileInputStream("data.txt");
            BufferedReader buf = new BufferedReader(new InputStreamReader(reader));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();


            int pieceID;
            int turn1;
            int b;
            int c,rot ;
            boolean firstMove;


            while(line != null) {
                sb.append(line);
                line = buf.readLine();
            }
            String fileAsString = sb.toString();
            var arrayW = fileAsString.split("/");
            for (int i = 0; i<arrayW.length; ++i){
                var arrayK = arrayW[i].split(",");

                pieceID = Integer.parseInt(arrayK[0]);
                turn1= Integer.parseInt(arrayK[1]);
                b = Integer.parseInt(arrayK[2])- Piece.SHAPE_SIZE / 2;
                c = Integer.parseInt(arrayK[3])- Piece.SHAPE_SIZE / 2;
                rot = Integer.parseInt(arrayK[5]);
                firstMove = Boolean.valueOf(arrayK[4]);
                if (rot>0){
                    for (int k =0 ; k!=rot; ++k){
                        players[turn1].pieces.get(pieceID).rotateClockwise();

                    }
                }
                else if (rot<0) {
                    for (int h =0 ; h!=Math.abs(rot); ++h){
                        players[turn1].pieces.get(pieceID).rotateCounterClockwise();

                    }
                }

                board.placePiece(players[turn1].pieces.get(pieceID), b, c, firstMove);
                //drawBoard(blind);
                players[turn1].pieces.remove(pieceID);
                players[turn1].firstMove = false;
                players[turn1].canPlay = players[turn1].pieces.size() != 0;

                //board.overlay(players[turn1].pieces.get(pieceID), b, c);
                turn = turn1;


            }



        } catch (Exception l) {
            try{
            PrintWriter writer1 = new PrintWriter("data.txt");
            writer1.print("");
            writer1.print("");}
            catch(Exception k ){
                JOptionPane.showMessageDialog(null,"Saving file is damaged please start again");
            }

        }
    }

        private void initializeGUI(boolean blind , boolean hint1)

        {


            class BoardClickListener implements MouseListener, MouseMotionListener, MouseWheelListener
            {
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getButton() == MouseEvent.BUTTON3)
                    {
                        flipPiece(blind);
                    }
                    else
                    {
                        try
                        {


                            board.placePiece(players[turn].pieces.get(
                                    pieceIndex), selected.x - Piece.SHAPE_SIZE / 2,
                                    selected.y - Piece.SHAPE_SIZE / 2, players[turn].firstMove);
                            try{

                                FileWriter writer = new FileWriter("data.txt",true);
                                int a = pieceIndex;
                                int turn1 = turn;
                                int b = selected.x ;
                                int c = selected.y ;
                                boolean d =  players[turn].firstMove;
                                writer.write(a+",");
                                writer.write(turn1+",");
                                writer.write(b+",");
                                writer.write(c+",");
                                writer.write(d+",");
                                writer.write(rotated +"/");
                                writer.close();
                            }
                            catch(Exception h){

                            }

                            drawBoard(blind);
                            players[turn].pieces.remove(pieceIndex);
                            players[turn].firstMove = false;
                            players[turn].canPlay = players[turn].pieces.size() != 0;

                            startNewTurn();
                            rotated = 0;
                        }
                        catch (Board.IllegalMoveException ex)
                        {
                            displayMessage(ex.getMessage(), "Illegal Move!");
                        }
                    }
                }

                public void mousePressed(MouseEvent e)
                {

                }

                public void mouseReleased(MouseEvent e)
                {

                }

                public void mouseEntered(MouseEvent e)
                {

                }

                public void mouseExited(MouseEvent e)
                {
                    selected = null;
                    board.resetOverlay();
                    drawBoard(blind);
                }

                public void mouseDragged(MouseEvent e)
                {

                }

                public void mouseMoved(MouseEvent e)
                {
                    Point p = board.getCoordinates(e.getPoint(), Board.DEFAULT_RESOLUTION);
                    if (!p.equals(selected))
                    {
                        selected = p;
                        board.overlay(players[turn].pieces.get(pieceIndex), selected.x, selected.y);
                        currentcoord.setText("X:"+(selected.x)+"Y:"+(selected.y));
                        drawBoard(blind);
                    }
                }

                public void mouseWheelMoved(MouseWheelEvent e)
                {
                    if (e.getWheelRotation() > 0)
                    {
                        rotateClockwise(blind);
                        rotated+=1;


                    }
                    else
                    {
                        rotateCounterClockwise(blind);
                        rotated-=1;
                    }
                }
            }

            class SurrenderListener implements ActionListener
            {
                public void actionPerformed(ActionEvent event)
                {
                    players[turn].canPlay = false;
                    startNewTurn();
                }
            }
            class SaveListener implements ActionListener
            {
                public void actionPerformed(ActionEvent event)
                {
                    System.exit(0);
                }
            }

            class HintListener implements ActionListener//For hints
            {
                public void actionPerformed(ActionEvent event)
                {
                    board.scanGrid(players[turn].pieces.get(pieceIndex), hint, players[turn].firstMove);
                    hintsText.setText("Hint Location: X:"+(hint.getXcoord()+3)+"Y:"+(hint.getYcoord()+3));
                }
            }
            
            mainPanel = new JPanel();
            piecesPanel = new JPanel();
            GridLayout experimentLayout = new GridLayout(4,3);
            piecesPanel.setLayout(experimentLayout);
            currentcoord = new JLabel("X:"+0+"Y"+0);
            hintsText=new JTextArea();
            hintsText.setEditable(false);
            if (hint1 == false){
                hintsText.setText("Hints off");
            }
            else{
                hintsText.setText("Hints on");
            }



            surrender = new JButton("Give up");
            surrender.setPreferredSize(new Dimension(Piece.DEFAULT_RESOLUTION, 30));
            surrender.addActionListener(new SurrenderListener());


            hints = new JButton("Get a hint");
            hints.setPreferredSize(new Dimension(Piece.DEFAULT_RESOLUTION, 30));
            hints.addActionListener(new HintListener());

            save = new JButton("Save and Exit");
            save.setPreferredSize(new Dimension(Piece.DEFAULT_RESOLUTION, 30));
            save.addActionListener(new SaveListener());
            // hint.addActionListener(new HintListener());

            sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));


            boardPanel = new JPanel();

            boardImage = new ImageIcon(board.render(blind));

            label = new JLabel(boardImage);
            BoardClickListener bcl = new BoardClickListener();
            rules = new JTextArea("To Rotate the piece use the mouse wheel"+"\n"+ "To Flip the piece right click");
            rules.setEditable(false);
            label.addMouseListener(bcl);
            label.addMouseMotionListener(bcl);
            label.addMouseWheelListener(bcl);

            boardPanel.add(label);


            sidePanel.add(surrender);
            if (hint1){
                sidePanel.add(hints);
            }

            sidePanel.add(save);
            sidePanel.add(rules);
            sidePanel.add(currentcoord);
            mainPanel.add(sidePanel);
            mainPanel.add(boardPanel);
            mainPanel.add(piecesPanel);
            add(hintsText, BorderLayout.BEFORE_FIRST_LINE);


            getContentPane().add(mainPanel);
            pack();
            setVisible(true);
        }

        private void rotateClockwise(boolean blind)
        {
            players[turn].pieces.get(pieceIndex).rotateClockwise();
            board.overlay(players[turn].pieces.get(pieceIndex), selected.x, selected.y);
            drawBoard(blind);
        }

        private void rotateCounterClockwise(boolean blind)
        {
            players[turn].pieces.get(pieceIndex).rotateCounterClockwise();
            board.overlay(players[turn].pieces.get(pieceIndex), selected.x, selected.y);
            drawBoard(blind);
        }

        private void flipPiece(boolean blind)
        {
            players[turn].pieces.get(pieceIndex).flipOver();
            board.overlay(players[turn].pieces.get(pieceIndex), selected.x, selected.y);
            drawBoard(blind);
        }

        private void drawBoard(boolean blind)
        {
            boardImage.setImage( board.render(blind));
            label.repaint();
        }

        private void drawBorder()
        {
            JComponent piece = (JComponent) piecesPanel.getComponent(pieceIndex);
            piece.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        }

        private void clearBorder()
        {
            JComponent piece = (JComponent) piecesPanel.getComponent(pieceIndex);
            piece.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        private void displayMessage(String message, String title)
        {
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        }

        private class PieceLabelClickListener implements MouseListener
        {
            public void mouseClicked(MouseEvent e)
            {

                BlokusPieceLabel bp = (BlokusPieceLabel) e.getComponent();
                clearBorder();
                pieceIndex = bp.pieceIndex;
                drawBorder();
            }

            public void mousePressed(MouseEvent e)
            {

            }

            public void mouseReleased(MouseEvent e)
            {

            }

            public void mouseEntered(MouseEvent e)
            {

            }

            public void mouseExited(MouseEvent e)
            {

            }
        }

        private void startNewTurn()
        {
            turn++;
            turn %= NUM_PLAYERS;

            if (isGameOver())
            {
                gameOver();
            }

            if (!players[turn].canPlay)
            {
                startNewTurn();
                return;
            }

            piecesPanel.removeAll();

            for (int i = 0; i < players[turn].pieces.size(); i++)
            {
                BlokusPieceLabel pieceLabel =
                        new BlokusPieceLabel(i, players[turn].pieces.get(i), 50);
                pieceLabel.addMouseListener(new PieceLabelClickListener());
                pieceLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                piecesPanel.add(pieceLabel);
            }
            pieceIndex = 0;
            drawBorder();
            piecesPanel.repaint();

            pack();
            
            if(players[turn].getClass()==Computer.class) {//TODO AI PORTION
            	
            	
            	try
                {

            		Computer comp = (Computer)players[turn];
            		int ind = comp.playPiece();
                	board.scanGrid(players[turn].pieces.get(ind), hint, players[turn].firstMove);
                    board.placePiece(players[turn].pieces.get(
                    		ind), hint.getXcoord()+3 - Piece.SHAPE_SIZE / 2,
                            hint.getYcoord()+3 - Piece.SHAPE_SIZE / 2, players[turn].firstMove);
                   
                    
                    
                    try{

                        FileWriter writer = new FileWriter("data.txt",true);
                        int a = pieceIndex;
                        int turn1 = turn;
                        int b = selected.x ;
                        int c = selected.y ;
                        boolean d =  players[turn].firstMove;
                        writer.write(a+",");
                        writer.write(turn1+",");
                        writer.write(b+",");
                        writer.write(c+",");
                        writer.write(d+",");
                        writer.write(rotated +"/");
                        writer.close();
                    }
                    catch(Exception h){

                    }

                    drawBoard(blind);
                    players[turn].pieces.remove(ind);
                    players[turn].firstMove = false;
                    players[turn].canPlay = players[turn].pieces.size() != 0;

                    startNewTurn();
                    rotated = 0;
                }
                catch (Board.IllegalMoveException ex)
                {
                	startNewTurn();
                }
            }
        }

        private boolean isGameOver()
        {
            for (int i = 0; i < NUM_PLAYERS; i++)
            {
                if (players[i].canPlay) return false;
            }
            return true;
        }

        private void gameOver()
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < NUM_PLAYERS; i++)
            {
                sb.append(Board.getColorName(getPlayerColor(i)));
                sb.append(": ");
                sb.append(players[i].getScore());
                sb.append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE );
            System.exit(0);
        }

        private int getPlayerColor(int index)
        {
            switch (index)
            {
                case 0: return Board.BLUE;
                case 1: return Board.RED;
                case 2: return Board.YELLOW;
                case 3: return Board.GREEN;
                default: return 0;
            }
        }

    }

    public static class BlokusPieceLabel extends JLabel
    {
        public int pieceIndex;

        public BlokusPieceLabel(int pieceIndex, Piece bp, int size)
        {
            super(new ImageIcon(bp.render(size)));
            this.pieceIndex = pieceIndex;
        }
    }


}
