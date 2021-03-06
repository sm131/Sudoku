/**
 * Authors: Sohaib Mohiuddin, Umar Riaz, Jan O'Hanlon, Sailajan Sivalingam
 * Course: Principles of Software and Requirements (Winter 2019)
 * Due Date: March 27, 2019
 * Version 1
 * Github Link: https://github.com/sm131/Sudoku
 * 
 * 
 * Play.java 
 * This class is the play page where the sudoku game occurs. The user is shown how many remaining boxes are there to fill, a highscore 
 * list for when the game is finished, a timer for the user to know how long the game has been going on for, [a number panel to drag
 * values into the sudoku board(IN PROGRESS)] and buttons for music, hints, help and return to main menu. 
 */

 // IMPORTS FOR Play.java TO WORK
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicBorders;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

// CREATING THE CLASS THAT EXTENDS JFRAME
public class Play extends JFrame {

    public static JFrame frame;
    public JPanel panel1, num_panel, timer_panel;
    public JButton return_button, help;
    public JToggleButton hint;
    public static JToggleButton sound;
    public JLabel title_play, timer1, remainingCells, finalscore_label, bgimg, label2, himg, idimg, hhhelp;
    public JTextArea highscore_text;
    public JMenuBar menubar;
    public JMenu menu_file, submenu;
    public JMenuItem save, item_options, item_quit;

    // FINAL VARIABLE FOR GRID SIZE
    public static final int GRID_SIZE = 9;
    
    // FINAL VARIABLE FOR THE SUBGRID SIZE
    public static final int SUBGRID_SIZE = 3;

    //FINAL VARIABLE FOR EACH CELL SIZE IN THE GRID
    public static final int CELL_SIZE = 60;

    public static final Color RIGHT_ANSWER = Color.GREEN;
    public static final Color WRONG_ANSWER = Color.RED;
    public static final Color UNCLICKED_BOX = Color.white;
    public static final Color CLICKED_BOX = new Color(100, 100, 255);
    public static final Color BACKGROUND_COLOUR = new Color(238, 200, 150);
    public static final Font FONT_NUMBERS = new Font("Comic Sans MS", Font.BOLD, 20);
    public static final Font BUTTON_FONTS = new Font("Comic Sans MS", Font.BOLD, 15);
    public static final Font TITLE_FONTS = new Font("Comic Sans MS", Font.BOLD, 50);

    private int previousRowPicked;
    private int previousColPicked;
    boolean[][] hidden;

    gameGenerator newPuzzle = new gameGenerator();
	private int[][] puzzle = newPuzzle.getPuzzle();
    private boolean[][] mask;

    private JTextField[][] cells = new JTextField[GRID_SIZE][GRID_SIZE];
    private JButton[][] nums = new JButton[SUBGRID_SIZE][SUBGRID_SIZE];
    JPanel cell_panels = new JPanel();

    private static int cnt;
    private Timer timer;
    private int gamemodepicked;
    public int gamemode;
    private int remainingcells, initialcells = 0;
    public int score;
    private String username;
    public SimpleDateFormat sdf;

    // GETTING THE SAVEFILE
    public File file = new File("savefile.txt");
    
    // GETTING THE UNMUTE IMAGE FOR THE SOUND TOGGLE BUTTON FROM THE RESOURCES FOLDER
    Image musicOn;
    {
        try {
            musicOn = ImageIO.read(getClass().getResource("Resources/speaker.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GETTING THE MUTE IMAGE FOR THE SOUND TOGGLE BUTTON FROM THE RESOURCES FOLDER
    Image MusicOff;
    {
        try {
            MusicOff = ImageIO.read(getClass().getResource("Resources/mute.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Image speakerOnImage = musicOn.getScaledInstance(120, 100, Image.SCALE_DEFAULT);
    Image speakerOffImage = MusicOff.getScaledInstance(120, 100, Image.SCALE_DEFAULT);

    // GETTING THE BACKGROUND IMAGE FOR THE JFRAME FROM THE RESOURCES FOLDER
    Image Background;
    {
        try {
            Background = ImageIO.read(getClass().getResource("Resources/background_image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GETTING THE HINT OFF IMAGE FOR THE HINT TOGGLE BUTTON FROM THE RESOURCES FOLDER
    Image hint_off;
    {
        try{
            hint_off = ImageIO.read(getClass().getResource("Resources/lightbulboff.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // GETTING THE HINT ON IMAGE FOR THE HINT TOGGLE BUTTON FROM THE RESOURCES FOLDER
    Image hint_on;
    {
        try{
            hint_on = ImageIO.read(getClass().getResource("Resources/lightbulbon.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // GETTING THE HELP IMAGE FOR THE HELP BUTTON FROM THE RESOURCES FOLDER
    Image help_popup;
    {
        try{
            help_popup = ImageIO.read(getClass().getResource("Resources/Help_me.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    Image Background_image = Background.getScaledInstance(1500, 1000, Image.SCALE_DEFAULT);
    ImageIcon BGIMG = new ImageIcon(Background_image);

    Image hint_off_image = hint_off.getScaledInstance(120, 100, Image.SCALE_DEFAULT);
    ImageIcon hint_off_icon = new ImageIcon(hint_off_image);

    Image hint_on_image = hint_on.getScaledInstance(120, 100, Image.SCALE_DEFAULT);
    ImageIcon hint_on_icon = new ImageIcon(hint_on_image);

    Image help_popup_image = help_popup.getScaledInstance(120, 100, Image.SCALE_DEFAULT);
    ImageIcon help_popup_icon = new ImageIcon(help_popup_image);

    /**
     * CREATING THE CONSTRUCTOR THAT INITIATES THE JFRAME AND ALL COMPONENTS CONTAINED IN THE JFRAME
     * 
     * @param gmode
     */
    public Play(int gmode) {

        // CREATING THE NEW FRAME THAT HAS A SET SIZE, TITLE, CLOSEOPERATION, AND LAYOUT
        frame = new JFrame();
        frame.setSize(1500, 1000);
        frame.setTitle("Play");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        
        // CREATING A LABEL FOR THE BACKGROUND IMAGE TO BE PUT IN
        bgimg = new JLabel("", BGIMG, JLabel.CENTER);
        bgimg.setBounds(0, 0, 1500, 1000);

        // A COPYRIGHT LABEL BECAUSE WHY NOT
        label2 = new JLabel("© A product of JUSS Games Inc.");
        label2.setBounds(650, 880, 200, 50);

        // INITIATING GAMEMODE TO 0
        gamemode = 0;
        this.gamemode = gmode;

        // SETTING THE VARIABLE MASK AS THE METHOD maskGenerator()
        mask = maskGenerator();

        menubar = new JMenuBar();
        menu_file = new JMenu("File");
        item_options = new JMenuItem("Options");
        item_quit = new JMenuItem("Quit");
        menu_file.setFont(FONT_NUMBERS);
        item_options.setFont(FONT_NUMBERS);
        item_quit.setFont(FONT_NUMBERS);
        
        // ACTIONLISTENER FOR THE MENU ITEM QUIT
        item_quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "Close?",  JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });

        // ACTIONLISTENER FOR THE MENU ITEM Options
        item_options.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // IF STATEMENT TO CHECK WHETHER Options.java EXISTS OR NOT
                if (Options.frame == null) {
                    new Options();
                    Options.Beginner.setVisible(false);
                    Options.Intermediate.setVisible(false);
                    Options.Expert.setVisible(false);
                    Options.modeLabel.setVisible(false);
                    Options.Return.setVisible(false);
                    Options.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    Options.frame.setVisible(true);
                } else {
                    if (Options.clip.isRunning()) {
                        Options.soundButton.setSelected(true);
                    } else {
                        Options.soundButton.setSelected(false);
                    }
                    Options.Beginner.setVisible(false);
                    Options.Intermediate.setVisible(false);
                    Options.Expert.setVisible(false);
                    Options.modeLabel.setVisible(false);
                    Options.Return.setVisible(false);
                    Options.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    Options.frame.setVisible(true);
                }
                    
            }
        });

        // CREATING A TIMER 
        timer1 = new JLabel();
        sdf = new SimpleDateFormat("hh:mm:ss a");
        timer1.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        timer1.setBackground(Color.BLACK);
        timer1.setForeground(Color.BLACK);
        timer1.setHorizontalAlignment(SwingConstants.CENTER);
        timer1.setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.black));
        
        // ACTIONLISTENER FOR THE TIMER
         ActionListener actListner = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
                //cnt = 0;
                cnt += 1;
                if (cnt < 10) {
                    timer1.setText("Timer: 00:0" + Integer.toString(cnt));
                }
                else if (cnt < 60){
					timer1.setText("Timer: 00:" + Integer.toString(cnt));
                }
                else if (cnt % 60 < 10) {
                    timer1.setText("Timer: " + Integer.toString(cnt/60) + ":0" + Integer.toString(cnt%60));
                }
                else if (cnt < 600){
                    timer1.setText("Timer: 0" + Integer.toString(cnt/60) + ":" + Integer.toString(cnt%60));
                }
                else {
                    timer1.setText("Timer: " + Integer.toString(cnt/60) + ":" + Integer.toString(cnt%60));
                }
                
                timer1.setBounds(115,100,200,40);
                timer1.setHorizontalAlignment(JLabel.CENTER);
            }
        };
        Timer timer = new Timer(1000, actListner);
        timer.start();

        save = new JMenuItem("Save");
        save.setFont(FONT_NUMBERS);
        menu_file.add(save); menu_file.add(item_options); menu_file.add(item_quit);
        menubar.add(menu_file);

        // SETTING THE TITLE OF THE FRAME 
        title_play = new JLabel("Sudoku-sama");
        title_play.setBounds(610, 100, 340, 70);
        title_play.setFont(TITLE_FONTS);
        Map<TextAttribute, Object> attributes = new HashMap<>(TITLE_FONTS.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        title_play.setFont(TITLE_FONTS.deriveFont(attributes));

        // LABEL TO DISPLAY THE AMOUNT OF REMAINING CELLS LEFT TO FILL ON THE BOARD
        remainingCells = new JLabel("", JLabel.CENTER);
        remainingCells.setBounds(870,150,300,40);
        remainingCells.setForeground(Color.white);
        remainingCells.setFont(BUTTON_FONTS);
        remainingCells.setText("Remaining boxes: --");

        // BUTTON TO RETURN TO MAIN MENU
        return_button = new JButton("Return to Main Menu");
        return_button.setFont(new Font("Comic Sans", Font.BOLD, 30));
        return_button.setBounds(1150, 860, 300, 50);
        return_button.setBackground(BACKGROUND_COLOUR);
        return_button.setBorder(new EmptyBorder(0,0,0,0));
        return_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to go to Main Menu?" + "\n" + 
                                                        "You will lose your progress." , "Proceed to Main Menu?", 
                                                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION)
                {
                    cnt = 0;
                    timer.stop();
                    timer.restart();
                    
                    Homepage homepage = new Homepage();
                    frame.setVisible(false);
                }
            }
        });

        // ICON TO CLICK FOR HELP
        help = new JButton();
        help.setIcon(help_popup_icon);
        help.setFont(BUTTON_FONTS);
        help.setBounds(0, 800, 120, 100);
        help.setContentAreaFilled(false);
        help.setFocusPainted(false);
        help.setBorderPainted(false);
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JOptionPane.showMessageDialog(null, "To play, you first pick a blue box and enter the number you think it is, \n" + 
                                                    "then you press enter and see if you are correct. If the box turns green \n" + 
                                                    "you are correct, if it is red then you are incorrect. The objective \n" + 
                                                    "of Sudoku is to fill up the boxes with a number between 1-9 that \n" + 
                                                    "doesn't repeat in the rows, columns, or subgrids.");
            }
        });

        // ICON TO TOGGLE FOR HINTS ON OR OFF
        hint = new JToggleButton("");
        hint.setIcon(hint_on_icon);
        hint.setFont(BUTTON_FONTS);
        hint.setBounds(150, 800, 120, 100);
        hint.setContentAreaFilled(false);
        hint.setFocusPainted(false);
        hint.setBorderPainted(false);
        hint.setSelected(true);
        hint.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    hint.setIcon(hint_off_icon);
                } else {
                    hint.setIcon(hint_on_icon);
                }
            }
        });
        
        // ICON TO TOGGLE SOUND ON OR OFF
        sound = new JToggleButton();
        sound.setFont(BUTTON_FONTS);
        sound.setBounds(300, 800, 120, 100);
        sound.setContentAreaFilled(false);
        sound.setFocusPainted(false);
        sound.setBorderPainted(false);
        if (Options.soundButton.isEnabled() || Options.soundButton.isSelected()) {
            sound.setIcon(new ImageIcon(speakerOnImage));
        } else {
            sound.setIcon(new ImageIcon(speakerOffImage));
        }
        
        // LABEL TO DISPLAY THE USER'S FINAL SCORE
        finalscore_label = new JLabel("Final score: Finish to reveal", JLabel.CENTER);
        finalscore_label.setBounds(90,300,250,40);
        finalscore_label.setFont(BUTTON_FONTS);
        finalscore_label.setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.black));

        // TEXT AREA TO DISPLAY ALL THE PREVIOUS HIGH SCORES STORED ON THE SAVEFILE
        highscore_text = new JTextArea();
        highscore_text.setFont(BUTTON_FONTS);
        highscore_text.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.black));
        highscore_text.setEditable(false);
        // SCROLLPANE THAT CONTAINS THE TEXTAREA HOLDING THE PREVIOUS HIGH SCORES FROM THE SAVEFILE
        JScrollPane scroller = new JScrollPane(highscore_text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setBounds(90,350,250,200);
        scroller.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.black));

        // ACTIONLISTENER FOR THE SOUND ICON
        sound.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // IF STATEMENT THAT LOOKS AT ALL THE VARIATIONS OF BUTTON STATES, CLIP STATES AND OPTION.FRAME STATES
                if (e.getStateChange() == ItemEvent.SELECTED && Options.clip.isRunning() && Options.frame != null) {
                    sound.setIcon(new ImageIcon(speakerOnImage));
                    Options.soundButton.setSelected(true);
                }
                else if (e.getStateChange() == ItemEvent.SELECTED && !Options.clip.isRunning() && Options.frame != null) {
                    sound.setIcon(new ImageIcon(speakerOnImage));
                    Options.soundButton.setSelected(true);
                    Options.playSound();
                } 
                else if (e.getStateChange() == ItemEvent.SELECTED && !Options.clip.isRunning() && Options.frame == null) {
                    new Options();
                    Options.frame.setVisible(false);
                    sound.setIcon(new ImageIcon(speakerOnImage));
                    Options.soundButton.setSelected(true);
                    Options.playSound();
                }
                else if (e.getStateChange() == ItemEvent.DESELECTED && Options.clip.isRunning() && Options.frame != null) {
                    sound.setIcon(new ImageIcon(speakerOffImage));
                    Options.soundButton.setSelected(false);
                    Options.stopSound();
                }
                else if (e.getStateChange() == ItemEvent.DESELECTED && !Options.clip.isRunning() && Options.frame != null) {
                    sound.setIcon(new ImageIcon(speakerOffImage));
                    Options.soundButton.setSelected(false);
                }
            }
        });

        // CREATING THE PANEL THAT WILL CONTAIN THE NUMBERS TO DRAG AND DROP ON THE SUDOKU BOARD
        num_panel = new JPanel();
        num_panel.setBackground(Color.PINK);
        num_panel.setLayout(new GridLayout(3, 3));
        num_panel.setBounds(1200, 400, CELL_SIZE*3, CELL_SIZE*3);
        num_panel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));

        // CREATING THE PANEL THAT WILL CONTAIN THE SUDOKU BOARD
        panel1 = new JPanel();
        panel1.setBackground(Color.PINK);
        panel1.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        panel1.setBounds(430, 180, CELL_SIZE*GRID_SIZE+100, CELL_SIZE*GRID_SIZE+100);
        panel1.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));

        // CREATING AN ABSTRACTACTION FOR THE TEXTFIELDS WHERE THE USER WILL INPUT THE NUMBER
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowPicked = -1;
                int colPicked = -1;

                // SOURCE OF THE ACTION IN THE JTEXTFIELD
                JTextField source = (JTextField)e.getSource();

                boolean found = false;
                for (int row = 0; row < GRID_SIZE && !found; ++row) {
                    for (int col = 0; col < GRID_SIZE && !found; ++col) {
                        if (cells[row][col] == source) {
                            rowPicked = row;
                            colPicked = col;
                            found = true;  // IF THE SOURCE IS FOUND, THE LOOP IS LEFT
                        }
                    }
                }

                // NESTED IF STATEMENT TO SET THE BACKGROUND COLOUR OF THE CELLS ONCE ANOTHER CELL IS SELECTED
                if (previousRowPicked != -1 && previousColPicked != -1) {
                    if(mask[previousRowPicked][previousColPicked]) {
                        cells[previousRowPicked][previousColPicked].setBackground(UNCLICKED_BOX);
                    } else {
                        cells[previousRowPicked][previousColPicked].setBackground(UNCLICKED_BOX);
                    }
                }

                // VARIABLE user_input PARSES THE STRING VALUE OF THE CELLS TO INTEGER
                int user_input = Integer.parseInt(cells[rowPicked][colPicked].getText());

                if(hint.isSelected()) {
                    if (user_input == puzzle[rowPicked][colPicked]) {
                        cells[rowPicked][colPicked].setBackground(RIGHT_ANSWER);
                        cells[rowPicked][colPicked].setForeground(new Color(0, 0, 153));
                        cells[rowPicked][colPicked].setEditable(false);
                        remainingcells--;
                        score++;
                        remainingCells.setText("Remaining boxes: " +remainingcells);
                    } else {
                        cells[rowPicked][colPicked].setBackground(WRONG_ANSWER);
                        cells[rowPicked][colPicked].setText("");
                        score--;
                    }
                    if (remainingcells == 0) {
                        timer.stop();
                        int finalScore = (int)(((double)(score)/(double)(initialcells))*100);
                        finalscore_label.setText("Your final score is: " + finalScore + "%");
                        finalscore_label.setVisible(true);
                        username = JOptionPane.showInputDialog("What is your name?");
                        saveToFile(username + ":" + finalScore);
                        loadFromFile();
                    }
                }

                previousRowPicked = rowPicked;
                previousColPicked = colPicked;
            }
        };

        // CREATING AN ABSTRACTACTION FOR THE BUTTONS WHICH THE USER CAN DRAG AND DROP ON THE SUDOKU BOARD
        Action button_action = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowPicked_button = -1;
                int colPicked_button = -1;

                // SOURCE OF THE ACTION FOR THE JBUTTON
                JButton button_source = (JButton)e.getSource();

                boolean found_button = false;

                for (int i = 0; i < SUBGRID_SIZE && !found_button; ++i) {
                    for (int j = 0; j < SUBGRID_SIZE && !found_button; ++j) {
                        if(nums[i][j] == button_source) {
                            rowPicked_button = i;
                            colPicked_button = j;   
                            found_button = true;   // IF THE SOURCE IS FOUND, THE LOOP IS LEFT
                        }
                    }
                }
                int button_user_input = Integer.parseInt(nums[rowPicked_button][colPicked_button].getText());
                System.out.println(button_user_input);
            }
        };

        int button_numbers = 1;
        // NESTED FOR LOOP TO CREATE THE JBUTTONS FOR THE NUMBER PANEL
        for (int i = 0; i < SUBGRID_SIZE; ++i) {
            for (int j = 0; j < SUBGRID_SIZE; ++j) {
                nums[i][j] = new JButton(button_numbers + "");
                nums[i][j].setBorder(BorderFactory.createLineBorder(Color.black));

                // ADDING THE ACTION TO EACH CELL FROM THE ABSTRACTACTION
                nums[i][j].addActionListener(button_action);

                /**
                 * FIXME:
                 * Fix dragging values from number panel
                 *
                 *
                int button_user_input = Integer.parseInt(nums[i][j].getText());
                nums[i][j].setTransferHandler(new ValueExportTransferHandler(Integer.toString(button_user_input)));

                nums[i][j].addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        JButton button = (JButton) e.getSource();
                        TransferHandler handle = button.getTransferHandler();
                        handle.exportAsDrag(button, e, TransferHandler.COPY);
                    }
                });

                 */
                button_numbers++;
                // ADDING EACH BUTTON TO THE PANEL
                num_panel.add(nums[i][j]);
            }
        }

        // NESTED FOR LOOP TO CREATE THE JTEXTFIELDS FOR THE SUDOKU BOARD
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {

                cells[i][j] = new JTextField();
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                //cells[i][j].setTransferHandler(new ValueImportTransferHandler());

                // ADDING THE ACTION TO EACH CELL FROM THE ABSTRACTACTION
                cells[i][j].addActionListener(action);
                cells[i][j].addKeyListener(keyListener);
                
                // IF STATEMENT TO SET THE BORDERS FOR THE SUBGRIDS
                if (i % 3 == 0 && i != 0){
					cells[i][j].setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.black));
				}
				if (j % 3 == 0 && j != 0){
					cells[i][j].setBorder(BorderFactory.createMatteBorder(1, 4, 1, 1, Color.black));
				}
				if (i % 3 == 0 && j % 3 == 0 && j != 0 && i != 0){
					cells[i][j].setBorder(BorderFactory.createMatteBorder(4, 4, 1, 1, Color.black));
                }
                
                // ADDING EACH CELL TO THE PANEL
                panel1.add(cells[i][j]);

                // IF STATEMENT THAT SETS THE CELLS AS TRUE OR FALSE FOR IF VALUES ARE ALREADY AVAILABLE ON THE SUDOKU BOARD
                if (mask[i][j]) {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                    cells[i][j].setBackground(CLICKED_BOX);
                    //cells[i][j].setForeground(new Color(0, 0, 153));
                    cells[i][j].setForeground(new Color(0, 0, 0));
                    initialcells++;
                    
                 } else {
                    
                    // FOR LOOP THAT SETS THE TEXT OF THE VALUES THAT ARE SHOWN TO THE USER AND SETS EDITABLE TO FALSE SO THE VALUE
                    // CANNOT BE CHANGED
                    for (int x = 0; x < GRID_SIZE; x++) {
                        for (int y = 0; y < GRID_SIZE; y++) {
                            cells[i][j].setText(puzzle[i][j] + "");
                        }
                    }
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(Color.white);
                    cells[i][j].setForeground(new Color(0, 0, 153));
                 }

                 // ALIGNS ALL VALUES TO THE CENTER OF THE JTEXTFIELD FOR AESTHETICS
                 cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                 cells[i][j].setFont(FONT_NUMBERS);
                 remainingcells = initialcells;
                 
            }
        }

        // ADDING ALL COMPONENTS TO THE FRAME AND BACKGROUND LABEL
        frame.getContentPane().add(title_play);
        frame.setJMenuBar(menubar);
        bgimg.add(return_button);
        bgimg.add(help);
        bgimg.add(label2);
        bgimg.add(hint);
        bgimg.add(sound);
        bgimg.add(remainingCells);
        bgimg.add(finalscore_label);
        bgimg.add(scroller);
        bgimg.add(panel1);
        bgimg.add(num_panel);
        bgimg.add(timer1);
        frame.add(bgimg);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    // METHOD THAT CREATED THE MASK FOR THE SUDOKU BOARD
    public boolean[][] maskGenerator() {
        Random random = new Random();
        boolean[][] cover = new boolean[GRID_SIZE][GRID_SIZE];
        
        /**
         * SWITCH CASE FOR THE SELECTED GAMEMODE
         * case 1: gamemode = 2 --> Beginner
         * case 2: gamemode = 3 --> Intermediate
         * case 3: gamemode = 4 --> Expert
         * 
         * gamemode CORRESPONDS TO THE RANDOM VALUE 
         */
        switch(gamemode) {
            case 1:
            gamemode = 2;
            break;

            case 2:
            gamemode = 3;
            break;

            case 3:
            gamemode = 4;
            break;
        }
        /**
         * NESTED FOR LOOP THAT GENERATES RANDOM NUMBERS LESS THAN 
         * AND EQUAL TO THE GAMEMODE SELECTED
         */
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int randomnum = random.nextInt(5) + 1;
                if (randomnum <= gamemode) {
                    cover[i][j] = true;
                } else {
                    cover[i][j] = false;
                }
            }
        }
        return cover;
    }    

    public void setSelected(boolean b) {
        b = true;
    }

    /**
     * THE METHOD THAT TAKES THE SCORE AND ENTERS IT INTO THE SAVEFILE
     * 
     * @param finalScore
     */
    public void saveToFile(String finalScore) {

        /**
         * TRY CATCH BLOCK NEEDED FOR ANY EXCEPTIONS
         * 
         * FILEWRITER CREATED, 
         * THEN BUFFEREDWRITER CREATED WITH THE FILEWRITER,
         * THEN PRINTWRITER CREATED WITH THE BUFFEREDWRITER 
         */
        try (FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {

            out.println(finalScore);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * THE METHOD LOADS ALL NAMES AND RESPECTIVE SCORES
     */
    public void loadFromFile() {
        try {
            String token1;
            Scanner scanner = new Scanner(file);          
            List<String> temps = new ArrayList<String>();
            while (scanner.hasNext()) {
                token1 = scanner.next();
                temps.add(token1);
            }
            scanner.close();

            highscore_text.setText("Scores: \n");
            for (int s = 0; s < temps.size(); s++) {
                String label = temps.get(s);
                highscore_text.append((s+1) + ") " + label + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * A KEYLISTENER THAT LISTENS TO THE SOURCE AND LIMITS THE AMOUNT OF NUMBERS ENTERED IN EACH TEXTFIELD AND 
     * ALSO DOES NOT ALLOW CHARACTERS SUCH AS (A-Z) TO BE ENTERED IN THE TEXTFIELDS
     */
    KeyListener keyListener = new KeyListener() {
        public void keyPressed(KeyEvent keyEvent) {
        }
        public void keyReleased(KeyEvent keyEvent) {
        }

        public void keyTyped(KeyEvent keyEvent) {
            limit(keyEvent);
        }

        private void limit(KeyEvent keyEvent) {
            int rowPicked = -1;
            int colPicked = -1;

            //Source of the action
            JTextField source = (JTextField)keyEvent.getSource();

            boolean found = false;
            for (int row = 0; row < 9 && !found; ++row) {
                for (int col = 0; col < 9 && !found; ++col) {
                    if (cells[row][col] == source) {
                        rowPicked = row;
                        colPicked = col;
                        found = true;  //Leaves the loop when found
                    }
                }
            }

            char c = keyEvent.getKeyChar();
            if (cells[rowPicked][colPicked].getText().length() >= 1 || Character.isAlphabetic(c))
                keyEvent.consume();
        }

    };

    // CREATES BORDER COLOURS FOR COMPONENTS ASSOCIATED WITH
    Icon icon = new Icon() {
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2  = (Graphics2D)g.create();
            Point2D start  = new Point2D.Float(0f, 0f);
            Point2D end    = new Point2D.Float(99f, 0f);
            float[] dist   = {0.0f, 0.5f, 1.0f};
            Color[] colors = { Color.RED, Color.BLUE, Color.GREEN };
            g2.setPaint(new LinearGradientPaint(start, end, dist, colors));
            g2.fillRect(x, y, 100, 10);
            g2.dispose();
        }
        @Override public int getIconWidth()  { return 100; }
        @Override public int getIconHeight() { return 10;  }
    };

    // CLASS THAT CREATES ROUNDED BORDERS FOR ANY COMPONENT IT IS ASSOCIATED WITH
    private static class RoundedBorder implements Border {

        private int radius;


        RoundedBorder(int radius) {
            this.radius = radius;
        }


        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }


        public boolean isBorderOpaque() {
            return true;
        }


        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }

    /**
     * TODO:
     * fix classes to drag values from num panel in the future
     * 
     * 
    public static class ValueExportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
        private String value;

        public ValueExportTransferHandler(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            Transferable t = new StringSelection(getValue());
            return t;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            super.exportDone(source, data, action);
            // Decide what to do after the drop has been accepted
        }

    }

    public static class ValueImportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

        public ValueImportTransferHandler() {
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean accept = false;
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                    if (value instanceof String) {
                        Component component = support.getComponent();
                        if (component instanceof JLabel) {
                            ((JLabel) component).setText(value.toString());
                            accept = true;
                        }
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
            return accept;
        }
    }
    *
    *
    **/
}
