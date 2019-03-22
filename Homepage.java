
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

//@SuppressWarnings("serial")
public class Homepage extends JFrame {

    public JFrame frame;
    public JButton play, options, help, quit, logout;
    public JLabel title, label2 ,bgimg;
    public JMenuBar menubar;
    public JMenu menu_file, submenu;
    public JMenuItem item_options, item_quit;

    public static final Color BACKGROUND_COLOUR = new Color(238, 200, 150);

    public static final Font TITLE_FONTS = new Font("Comic Sans MS", Font.BOLD, 50);
    public static final Font FONT_BUTTONS = new Font("Comic Sans MS", Font.BOLD, 20);

    public static final int GRID_SIZE = 9;

    Image Background;
    {
        try {
            Background = ImageIO.read(getClass().getResource("Resources/background_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    Image Background_image = Background.getScaledInstance(1500, 1000, Image.SCALE_DEFAULT);
    ImageIcon BGIMG = new ImageIcon(Background_image);

    public Homepage() {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(1500, 1000));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Sudoku");
        frame.setLayout(null);
        frame.setResizable(false);
        
        bgimg = new JLabel("", BGIMG, JLabel.CENTER);
        bgimg.setBounds(0, 0, 1500, 1000);

        menubar = new JMenuBar();
        menu_file = new JMenu("File");
        item_quit = new JMenuItem("Quit");
        menu_file.setFont(FONT_BUTTONS);
        item_quit.setFont(FONT_BUTTONS);
        
        item_quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "Close?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });

        menu_file.add(item_quit);
        menubar.add(menu_file);

        play = new JButton("Play");
        options = new JButton("Options");
        help = new JButton("Help");
        quit = new JButton("Quit");

        play.setBounds(560, 420, 200, 50);
        options.setBounds(560, 480, 200, 50);
        help.setBounds(770, 420, 200, 50);
        quit.setBounds(770, 480, 200, 50);

        play.setFont(FONT_BUTTONS);
        options.setFont(FONT_BUTTONS);
        help.setFont(FONT_BUTTONS);
        quit.setFont(FONT_BUTTONS);

        title = new JLabel("Welcome to Sudoku-sama");
        title.setFont(new Font("Comic Sans", Font.BOLD, 30));
        title.setBounds(580, 100, 400, 70);

        label2 = new JLabel("© A product of JUSS Games Inc.");
        label2.setBounds(650, 880, 200, 50);

        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Help help = new Help();
                frame.dispose();
            }
        });

        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Options options = new Options();
                frame.dispose();
            }
        });
        quit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "Close?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Play play = new Play(1);
                
                frame.dispose();
            }
        });

        frame.setJMenuBar(menubar);
        bgimg.add(play);
        bgimg.add(options);
        bgimg.add(help);
        bgimg.add(quit);
        frame.add(title);
        frame.add(label2);
        frame.add(bgimg);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Homepage homepage = new Homepage();
    }


}
