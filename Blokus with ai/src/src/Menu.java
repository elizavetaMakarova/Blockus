package src;

/**
 *
 *
 */
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;



public class Menu extends JFrame
{

    private JButton exit, start, colorblind, hints, load;
    private PlayerComboBox one, two, three;
    private File myFile;
    private JPanel gLay,sLay, hLay;
    private JLabel logo;
    private JTextArea note;
    int players=0;
    boolean colorbool = false;
    boolean hintbool = true;
    boolean isSaved = false;
    /**
     * Constructor for objects of class Menu
     */
    public Menu() throws IOException
    {
        super("Menu");
        myFile = new File("data.txt");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
        gLay = new JPanel();
        sLay = new JPanel();
        hLay = new JPanel();
        gLay.setLayout(new GridLayout(4,1));
        sLay.setLayout(new GridLayout(2,1));
        hLay.setLayout(new GridLayout(4,1));

        note = new JTextArea("Select number of players");
        note.setEditable(false);
        note.setSize(200, 120);

        one = new PlayerComboBox();
        one.addActionListener(new Action());

        two = new PlayerComboBox();
        two.addActionListener(new Action());

        three = new PlayerComboBox();
        three.addActionListener(new Action());

        start = new JButton("New Game");
        start.addActionListener(new Action());

        load = new JButton("Load Game");
        load.addActionListener(new Action());

        exit = new JButton("Exit");
        exit.addActionListener(new Action());

        colorblind = new JButton("Colorblind Mode OFF");
        colorblind.addActionListener(new Action());

        hints = new JButton("Hints ON");
        hints.addActionListener(new Action());


        ImageIcon img = new ImageIcon(this.getClass().getResource("logo.jpg"));

        logo = new JLabel();
        logo.setIcon(img);
        logo.setPreferredSize( new Dimension(200, 80));




        gLay.add(one);
        gLay.add(two);
        gLay.add(three);
        gLay.add(exit);
        sLay.add(logo);
        sLay.add(note);
        hLay.add(colorblind);
        hLay.add(hints);
        hLay.add(start);
        hLay.add(load);


        add(gLay);
        add(sLay);
        add(hLay);

        setResizable(false);


        setVisible(true);
    }

    public class Action implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource()==exit)
            {
                System.exit(0);
            }
            if(e.getSource()==start)
            {

                try {

                    int playercount = 1;//Number of human players
                    if (one.getSelectedIndex()==0) {//If the players are on human setting it will increase
                        playercount += 1;
                    }
                    if (two.getSelectedIndex()==0) {//If the players are on human setting it will increase
                        playercount += 1;
                    }
                    if (three.getSelectedIndex()==0) {//If the players are on human setting it will increase
                        playercount += 1;
                    }

                    Blokus v= new Blokus(playercount,colorbool,hintbool,one.getSelectedIndex(),two.getSelectedIndex(),three.getSelectedIndex(),false);

                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(null,"Something is wrong");
                }

            }
            if(e.getSource()==load)
            {
                try{
                InputStream reader = new FileInputStream("data.txt");
                BufferedReader buf = new BufferedReader(new InputStreamReader(reader));
                String line = buf.readLine();
                if (line.length() != 0){
                    isSaved= true;
                }
                }
                catch (Exception h){

                }


                try {

                    int playercount = 1;//Number of human players
                    if (one.getSelectedIndex()==0) {//If the players are on human setting it will increase
                        playercount += 1;
                    }
                    if (two.getSelectedIndex()==0) {//If the players are on human setting it will increase
                        playercount += 1;
                    }
                    if (three.getSelectedIndex()==0) {//If the players are on human setting it will increase
                        playercount += 1;
                    }
                    if (isSaved) {

                        Blokus v = new Blokus(playercount, colorbool, hintbool, one.getSelectedIndex(), two.getSelectedIndex(), three.getSelectedIndex(), isSaved);
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"No Saved File. Start New Game");
                    }

                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(null,"Something is wrong");
                }

            }

            if(e.getSource()==colorblind)
            {
                try {
                    if (colorbool == false) {
                        colorbool = true;
                        colorblind.setText("Colorblind Mode ON");
                    }
                    else {
                        colorbool = false;
                        colorblind.setText("Colorblind Mode OFF");
                    }



                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(null,"Something is wrong 3");
                }

            }

            if(e.getSource()==hints)
            {
                try {
                    if (hintbool == false) {
                        hintbool = true;
                        hints.setText("Hints ON");
                    }
                    else {
                        hintbool = false;
                        hints.setText("Hints OFF");
                    }



                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(null,"Something is wrong 3");
                }

            }
        }
    }
}
