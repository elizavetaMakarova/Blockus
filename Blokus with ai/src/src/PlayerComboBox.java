package src;

/**
 *
 *
 */
import javax.swing.JComboBox;


public class PlayerComboBox extends JComboBox{

    String[] options = {"Human", "Easy CPU", "Medium CPU", "Hard CPU"};

    public PlayerComboBox() {
        for (int i = 0; i<options.length; i++) {
            addItem(options[i]);
        }
    }

}