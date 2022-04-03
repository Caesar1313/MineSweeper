package com.company;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

public class MineSweeperGUI extends JFrame implements Runnable, ActionListener, MouseListener {
    String[] d = new String[22];
    String[] m = new String[496];
    private final Color clr = new Color(132, 167, 176);
    private final Color clr2 = new Color(30, 120, 180);
    private int dimensions = 0;
    private int numOfMines = 0;
    private int firstNumOfMines;
    private final String defaultFileName = "Default Settings.bin";
    final Player player = MineSweeper.player;
    final JFrame frame = new JFrame("MineSweeper by Caesar");
    private final JFrame loadFrame = new JFrame("Loading");
    final JPanel gridPanel;
    final JPanel info = new JPanel(null);
    final JLabel timeLabel = new JLabel("Time:         seconds");
    JLabel time = new JLabel();
    final JLabel minesLabel = new JLabel("Mines: ");
    JLabel mines;
    final JLabel byMe = new JLabel("MineSweeper by Caesar");
    private final ImageIcon sON = new ImageIcon("Resources\\soundOn1.png");
    private final ImageIcon sOff = new ImageIcon("Resources\\soundOff1.png");
    final FrameBackGround backGroundImage = new FrameBackGround();
    private static Component[] allButtons;
    private final Image icon = Toolkit.getDefaultToolkit().getImage("Resources\\icon2.png");
    private final ImageIcon tile = new ImageIcon("Resources\\tile.png");
    private final ImageIcon one = new ImageIcon("Resources\\1.png");
    private final ImageIcon two = new ImageIcon("Resources\\2.png");
    private final ImageIcon three = new ImageIcon("Resources\\3.png");
    private final ImageIcon four = new ImageIcon("Resources\\4.png");
    private final ImageIcon five = new ImageIcon("Resources\\5.png");
    private final ImageIcon six = new ImageIcon("Resources\\6.png");
    private final ImageIcon seven = new ImageIcon("Resources\\7.png");
    private final ImageIcon eight = new ImageIcon("Resources\\8.png");
    private final ImageIcon flag = new ImageIcon("Resources\\flag1.png");
    private boolean[] hasFlag;
    private static boolean firstRun = true;
    private static int fullTime;
    private static final File file = new File("Resources\\Tetris.wav");
    private static AudioInputStream audio;
    private static Clip clip;
    private static final File file2 = new File("Resources\\Explosion.wav");
    private static AudioInputStream audio2;
    private static Clip clip2;

    private void prepareSounds() {
        try {
            MineSweeperGUI.audio = AudioSystem.getAudioInputStream(file);
            MineSweeperGUI.clip = AudioSystem.getClip();
            MineSweeperGUI.clip.open(audio);

            MineSweeperGUI.audio2 = AudioSystem.getAudioInputStream(file2);
            MineSweeperGUI.clip2 = AudioSystem.getClip();
            MineSweeperGUI.clip2.open(audio2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.frame, "Error playing sound", "Error", JOptionPane.ERROR_MESSAGE, null);
        }
    }

    MineSweeperGUI() throws InterruptedException {
        if (firstRun)
            loading();
        askForInput();
        prepareSounds();
        while (dimensions == 0)
            Thread.sleep(1);
        new MineSweeper(dimensions, numOfMines);
        gridPanel = new JPanel(new GridLayout(dimensions, dimensions, 0, 0));
        this.firstNumOfMines = numOfMines;
        preparePanels();
        prepareFrame();
        fillGridPanel();
        initializeFlags();
        Thread t = new Thread(this);
        t.start();
    }

    MineSweeperGUI(int dimens, int minesCount) {
        this.dimensions = dimens;
        this.numOfMines = minesCount;
        this.firstNumOfMines = numOfMines;
        prepareSounds();
        new MineSweeper(dimens, minesCount);
        gridPanel = new JPanel(new GridLayout(dimens, dimens, 0, 0));
        preparePanels();
        prepareFrame();
        fillGridPanel();
        initializeFlags();
        Thread t = new Thread(this);
        t.start();
    }

    private void initializeFlags() {
        Arrays.fill(hasFlag, false);
    }

    private void loading() throws InterruptedException {
        String label = "    Starting MineSweeper.";
        JLabel starting = new JLabel(label);
        JLabel wait = new JLabel("This may take a few seconds");
        wait.setBounds(125, 150, 175, 200);
        wait.setFont(new Font("Arial", Font.PLAIN, 13));
        wait.setForeground(Color.WHITE);
        wait.setBackground(clr);
        JPanel loadPanel = new JPanel(null);
        loadPanel.setBounds(0, 0, 525, 525);
        loadPanel.setBackground(clr);
        loadFrame.setIconImage(icon);
        loadFrame.setUndecorated(true);
        loadFrame.setVisible(true);
        starting.setBounds(75, 185, 400, 60);
        starting.setFont(new Font("Serif", Font.ITALIC, 30));
        starting.setBackground(clr);
        starting.setForeground(Color.WHITE);
        starting.setOpaque(true);
        loadPanel.add(wait, 0);
        loadPanel.add(starting, 1);
        loadFrame.setBounds(500, 150, 500, 500);
        loadFrame.setLayout(null);
        loadFrame.add(loadPanel);
        loadFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int times = 0;
        while (times != 5) {
            Thread.sleep(200);
            label += ".";
            starting.setText(label);
            Thread.sleep(200);
            label += ".";
            starting.setText(label);
            Thread.sleep(200);
            label = label.substring(0, label.length() - 2);
            starting.setText(label);
            times++;
        }
        loadFrame.dispose();
    }

    private void fillGridPanel() {
        for (int i = 0; i < MineSweeper.gridDimensions; i++)
            for (int j = 0; j < MineSweeper.gridDimensions; j++) {
                JButton b = new JButton(j + " " + i);
                b.setIcon(tile);
                b.addActionListener(this);
                b.addMouseListener(this);
                gridPanel.add(b);
            }
        allButtons = gridPanel.getComponents();
        hasFlag = new boolean[allButtons.length];
    }

    private int geti(JButton b) {
        try {
            boolean hasSpace = false;
            String buttonID = b.getText();
            int i = 0;
            while (buttonID.charAt(i) != ' ') {

                i++;
            }
            if (buttonID.charAt(i) == ' ')
                hasSpace = true;
            if (!hasSpace)
                return -1;
            else {
                i = Integer.parseInt(buttonID.substring(0, i));
                return i;
            }
        } catch (Exception e) {
            System.out.print("");
        }
        return -1;
    }

    private int getj(JButton b) {
        try {
            boolean hasSpace = false;
            String buttonID = b.getText();
            int j = 0;
            while (buttonID.charAt(j) != ' ') {
                j++;
            }
            if (buttonID.charAt(j) == ' ')
                hasSpace = true;
            if (!hasSpace)
                return -1;
            else {
                j = Integer.parseInt(buttonID.substring(j + 1));
                return j;
            }
        } catch (Exception e) {
            System.out.print("");
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        int k = 0;
        while (!allButtons[k].equals(b)) {
            k++;
        }
        if (hasFlag[k])
            return;
        Player.clicksMade++;
        int i = geti(b);
        int j = getj(b);
        try {
            player.selectCell(i, j);

        } catch (Exception ex) {
            System.out.print("");
        }
        if (MineSweeper.mineExploded) {   // setting redmine.png to the clicked mine.
            ImageIcon redMine = new ImageIcon("Resources\\redmine.png");
            clip.stop();
            clip2.start();
            b.setText("");
            b.setIcon(resizeIcon(redMine, b.getWidth(), b.getHeight()));
            b.setBackground(new Color(239, 56, 46));
            return;
        }
        clickButton(b);
        updatePanelGrid();
    }

    private void clickButton(JButton button) {
        int k = 0;
        while (!allButtons[k].equals(button)) {
            k++;
        }
        if (hasFlag[k])
            return;
        int i = geti(button);
        int j = getj(button);
        if (i == -1 || j == -1) return;
        button.setEnabled(false);
        button.setText("checked");
        determineTextAndColor(button, i, j);
    }

    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JButton b = (JButton) e.getSource();
        if (e.getModifiers() == MouseEvent.BUTTON3_MASK && e.getClickCount() == 1) {
            int k = 0;
            while (!allButtons[k].equals(b)) {
                k++;
            }
            if (!allButtons[k].isEnabled())
                return;
            if (!hasFlag[k] && allButtons[k].isEnabled()) {
                b.setIcon(resizeIcon(flag, b.getWidth() + 15, b.getHeight()));
                mines.setText(String.valueOf(--numOfMines));
                hasFlag[k] = true;
            } else if (hasFlag[k] && allButtons[k].isEnabled()) {
                b.setIcon(tile);
                mines.setText(String.valueOf(++numOfMines));
                hasFlag[k] = false;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    static class FrameBackGround extends Canvas {
        public void paint(Graphics g) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("Resources\\2.jpg");
            g.drawImage(image, 0, 0, 1675, 825, this);
        }
    }

    static class ClockImage extends JPanel {
        private BufferedImage clock;

        ClockImage() {
            try {
                this.clock = ImageIO.read(new File("Resources\\clock.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(clock, 0, 0, this);
        }
    }

    static class MineImage extends JPanel {
        private BufferedImage mine;

        MineImage() {
            try {
                mine = ImageIO.read(new File("Resources\\mine.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(mine, 0, 0, this);
        }
    }

    static class Logo extends JPanel {
        private BufferedImage logo;

        Logo() {
            try {
                logo = ImageIO.read(new File("Resources\\logo111.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(logo, 0, 0, null);
        }
    }

    private void prepareFrame() {
        frame.setSize(1550, 864);
        byMe.setBounds(1360, 765, 145, 18);
        byMe.setBackground(new Color(53, 85, 104));
        byMe.setOpaque(true);
        byMe.setForeground(Color.white);
        frame.add(byMe);
        frame.setIconImage(icon);
        frame.add(backGroundImage, 1);
        frame.setVisible(true);
        frame.add(gridPanel, 0);
        frame.add(info, 0);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void preparePanels() {
        Font f = new Font("Leelawadee UI", Font.PLAIN, 26);
        JLabel mineSweeper = new JLabel("MineSweeper");
        JLabel byMe = new JLabel("By Caesar");
        mineSweeper.setForeground(Color.white);
        mineSweeper.setBackground(clr);
        mineSweeper.setBounds(70, 235, 155, 70);
        mineSweeper.setFont(new Font("Gabriola", Font.PLAIN, 35));
        byMe.setForeground(Color.white);
        byMe.setBackground(clr);
        byMe.setBounds(185, 255, 80, 40);
        byMe.setFont(new Font("Freestyle Script", Font.PLAIN, 22));
        Logo logo = new Logo();
        logo.setBackground(clr);
        MineImage mine = new MineImage();
        ClockImage clock = new ClockImage();
        clock.setBounds(5, 415, 48, 48);
        clock.setBackground(clr);
        mine.setBounds(5, 525, 48, 48);
        mine.setBackground(clr);
        logo.setBounds(55, 60, 175, 175);
        gridPanel.setBounds(625, 40, 715, 715);
        JToggleButton sound = new JToggleButton();
        sound.setIcon(sOff);
        sound.setBounds(110, 700, 85, 60);
        sound.setBackground(clr);
        sound.setBorder(BorderFactory.createEtchedBorder());
        sound.addItemListener(e -> {
            JToggleButton tb = (JToggleButton) e.getSource();
            if (tb.getIcon().equals(sON)) {
                tb.setIcon(sOff);
                MineSweeperGUI.clip.stop();
                MineSweeperGUI.clip.setMicrosecondPosition(0);
            } else if (tb.getIcon().equals(sOff)) {
                tb.setIcon(sON);
                MineSweeperGUI.clip.start();
            }
        });
        info.setBackground(Color.WHITE);
        info.setBounds(0, 0, 300, 810);
        info.setBackground(clr);
        timeLabel.setBounds(60, 420, 230, 40);
        timeLabel.setFont(f);
        timeLabel.setForeground(Color.white);
        time.setBounds(125, 423, 60, 35);
        time.setFont(f);
        time.setForeground(Color.white);
        minesLabel.setBounds(60, 530, 95, 40);
        minesLabel.setFont(f);
        minesLabel.setForeground(Color.white);
        mines = new JLabel(String.valueOf(MineSweeper.minesCount));
        mines.setBounds(153, 530, 60, 40);
        mines.setForeground(Color.white);
        mines.setFont(f);
        info.add(mineSweeper, 0);
        info.add(sound, 0);
        info.add(mine, 0);
        info.add(byMe, 0);
        info.add(logo, 0);
        info.add(clock, 0);
        info.add(timeLabel);
        info.add(time);
        info.add(minesLabel);
        info.add(mines);
    }

    private void updatePanelGrid() {
        String positionAsText;
        for (int i = 0; i < MineSweeper.gridDimensions; i++)
            for (int j = 0; j < MineSweeper.gridDimensions; j++) {
                if (MineSweeper.grid[j][i].cellState == CellState.REVEALED) {
                    positionAsText = MineSweeper.grid[j][i].position.x + " " + MineSweeper.grid[j][i].position.y;
                    for (int k = 0; k < allButtons.length; k++) {
                        if ("checked".equals(((JButton) allButtons[k]).getText())) continue;
                        if (positionAsText.equals(((JButton) allButtons[k]).getText())) {
                            clickButton((JButton) this.gridPanel.getComponent(k));
                            determineTextAndColor((JButton) allButtons[k], i, j);
                        }
                    }
                }
            }
    }

    private void determineTextAndColor(JButton b, int i, int j) {

        if (MineSweeper.grid[j][i].content == 0) {
            b.setText("");
            return;
        } else if (MineSweeper.grid[j][i].content == 1) {
            b.setText("");
            b.setIcon(resizeIcon(one, b.getWidth(), b.getHeight()));
        } else if (MineSweeper.grid[j][i].content == 2) {
            b.setText("");
            b.setIcon(resizeIcon(two, b.getWidth(), b.getHeight()));
        } else if (MineSweeper.grid[j][i].content == 3) {
            b.setText("");
            b.setIcon(resizeIcon(three, b.getWidth(), b.getHeight()));
        } else if (MineSweeper.grid[j][i].content == 4) {
            b.setText("");
            b.setIcon(resizeIcon(four, b.getWidth(), b.getHeight()));
        } else if (MineSweeper.grid[j][i].content == 5) {
            b.setText("");
            b.setIcon(resizeIcon(five, b.getWidth(), b.getHeight()));
        } else if (MineSweeper.grid[j][i].content == 6) {
            b.setText("");
            b.setIcon(resizeIcon(six, b.getWidth(), b.getHeight()));
        } else if (MineSweeper.grid[j][i].content == 7) {
            b.setText("");
            b.setIcon(resizeIcon(seven, b.getWidth(), b.getHeight()));
        } else if (MineSweeper.grid[j][i].content == 8) {
            b.setText("");
            b.setIcon(resizeIcon(eight, b.getWidth(), b.getHeight()));
        }
        b.setEnabled(true);
    }

    @Override
    public void run() {
        while (MineSweeper.state == GameState.RUNNINGGAME) {
            time.setText(String.valueOf(Timer.time));
            if (Consult() == -1) {
                MineSweeper.state = GameState.LOSE;
                setMinePics();
            } else if (Consult() == 1) {
                MineSweeper.state = GameState.WIN;
            }
        }
        showResult();
    }

    private int Consult() {
        if (MineSweeper.GameRules.GameOver()) {
            return -1;
        } else if (MineSweeper.GameRules.PlayerWin()) {
            MineSweeper.score = ((100000 * (MineSweeper.gridDimensions * MineSweeper.gridDimensions)) / (Player.clicksMade * Timer.time));
            return 1;
        } else {
            return 0;
        }
    }

    private void setMinePics() {
        Color c = new Color(198, 197, 198);
        final ImageIcon mine = new ImageIcon("Resources\\mine.png");
        String positionAsText;
        for (int i = 0; i < MineSweeper.gridDimensions; i++)
            for (int j = 0; j < MineSweeper.gridDimensions; j++) {
                if (MineSweeper.grid[j][i] instanceof MineCell) {
                    positionAsText = MineSweeper.grid[j][i].position.x + " " + MineSweeper.grid[j][i].position.y;
                    for (Component button : allButtons) {
                        if (positionAsText.equals(((JButton) button).getText())) {
                            ((JButton) button).setIcon(resizeIcon(mine, button.getWidth(), button.getHeight()));
                            ((JButton) button).setText("");
                            button.setBackground(c);
                        }
                    }
                }
            }
    }

    private void showResult() {
        MineSweeperGUI.fullTime = Timer.time;
        String[] options = {"New Game", "Restart", "Exit"};
        String message = "";
        String hours = String.valueOf(getHours());
        String minutes = String.valueOf(getMinutes());
        String seconds = String.valueOf(MineSweeperGUI.fullTime);
        int respond;
        if (MineSweeper.state == GameState.WIN)
            message = "                     ----- YOU WIN -----   " + "\nMines:                                                       " + MineSweeper.minesCount + "\n                                    ---" + "\nTime Spent:                                     " + hours + "h " + minutes + "m " + seconds + "s" + "\n                                    ---" + "\nScore:                                                   " + MineSweeper.score;
        else if (MineSweeper.state == GameState.LOSE)
            message = "                     ----- YOU LOSE -----   " + "\nMines:                                                       " + MineSweeper.minesCount + "\n                                    ---" + "\nTime Spent:                                     " + hours + "h " + minutes + "m " + seconds + "s" + "\n                                    ---" + "\nScore:                                                         " + MineSweeper.score;
        respond = JOptionPane.showOptionDialog(this.frame, message, "Result!", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);
        if (respond == 0) {
            try {
                reset();
                this.frame.dispose();
                new MineSweeperGUI();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else if (respond == 1) {
            reset();
            this.frame.dispose();
            new MineSweeperGUI(dimensions, firstNumOfMines);
        } else
            System.exit(0);
    }

    private void reset() {
        MineSweeperGUI.clip.stop();
        MineSweeperGUI.clip.setMicrosecondPosition(0);
        MineSweeper.mineExploded = false;
        Player.clicksMade = 0;
        MineSweeper.score = 0;
        Timer.time = 0;
        firstRun = false;
        Timer.firstRun = false;
    }

    private int getHours() {
        int hours = 0;
        while (fullTime >= 3600) {
            hours++;
            fullTime -= 3600;
        }
        return hours;
    }

    private int getMinutes() {
        int minutes = 0;
        while (fullTime >= 60) {
            minutes++;
            fullTime -= 60;
        }
        return minutes;
    }

    public void askForInput() {
        for (int i = 0; i < d.length; i++)
            d[i] = String.valueOf(i + 4);
        for (int j = 0; j < m.length; j++)
            m[j] = String.valueOf(j + 5);
        JFrame frame = new JFrame("StartUp");
        frame.setLayout(null);
        frame.setIconImage(icon);
        frame.setBounds(650, 320, 260, 180);
        JLabel gridDimensions = new JLabel("Grid Dimensions(nxn): ");
        JComboBox<String> gridDimensionsBox = new JComboBox<>(d);
        JLabel minesCount = new JLabel("Mines: ");
        JComboBox<String> minesBox = new JComboBox<>(m);
        JButton startButton = new JButton("Start");
        JButton defaultGame = new JButton("Default Game");
        JButton setAsDefault = new JButton("Set as Default");
        JLabel defaultDimensions = new JLabel();
        JLabel defaultMinesCount = new JLabel();


        // setting the default labels text.
        try {
            FileInputStream fileInputStream = new FileInputStream(defaultFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            int d = objectInputStream.readInt();
            int m = objectInputStream.readInt();
            defaultDimensions.setText(String.valueOf(d));
            defaultMinesCount.setText(String.valueOf(m));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "/'Default Settings.bin/' file does not exist.", "Missing File", JOptionPane.ERROR_MESSAGE, null);
        }


        startButton.addActionListener(e -> {
            numOfMines = Integer.parseInt(String.valueOf(minesBox.getItemAt(minesBox.getSelectedIndex())));
            dimensions = Integer.parseInt(String.valueOf(gridDimensionsBox.getItemAt(gridDimensionsBox.getSelectedIndex())));
            if (!isValid(numOfMines, dimensions)) {
                numOfMines = 0;
                dimensions = 0;
                JOptionPane.showMessageDialog(frame, "Too much mines for the entered grid dimensions, change one value at least.", "Incompatible input", JOptionPane.INFORMATION_MESSAGE, null);
                return;
            }
            frame.dispose();
        });

        defaultGame.addActionListener(e -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(defaultFileName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                dimensions = objectInputStream.readInt();
                numOfMines = objectInputStream.readInt();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "/'Default Settings.bin/' file does not exist.", "Missing File", JOptionPane.ERROR_MESSAGE, null);
            }
            if (!isValid(numOfMines, dimensions)) {
                numOfMines = 0;
                dimensions = 0;
                JOptionPane.showMessageDialog(frame, "Too much mines for the entered grid dimensions, change one value at least.", "Incompatible input", JOptionPane.INFORMATION_MESSAGE, null);
                return;
            }
            frame.dispose();
        });

        setAsDefault.addActionListener(e -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(defaultFileName);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeInt(Integer.parseInt(String.valueOf(gridDimensionsBox.getItemAt(gridDimensionsBox.getSelectedIndex()))));
                objectOutputStream.writeInt(Integer.parseInt(String.valueOf(minesBox.getItemAt(minesBox.getSelectedIndex()))));
                objectOutputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            defaultDimensions.setText(gridDimensionsBox.getItemAt(gridDimensionsBox.getSelectedIndex()));
            defaultMinesCount.setText(minesBox.getItemAt(minesBox.getSelectedIndex()));
        });


        startButton.setBounds(10, 80, 95, 25);
        defaultGame.setBounds(115, 80, 120, 25);
        setAsDefault.setBounds(10, 110, 225, 25);
        defaultDimensions.setBounds(215, 17, 20, 20);
        defaultDimensions.setForeground(clr2);
        defaultMinesCount.setBounds(215, 47, 20, 20);
        defaultMinesCount.setForeground(clr2);
        gridDimensions.setBounds(5, 15, 135, 20);
        gridDimensionsBox.setBounds(140, 17, 70, 20);
        minesCount.setBounds(5, 45, 50, 20);
        minesBox.setBounds(140, 47, 70, 20);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
        frame.setResizable(false);
        frame.add(gridDimensions);
        frame.add(gridDimensionsBox);
        frame.add(minesCount);
        frame.add(minesBox);
        frame.add(startButton);
        frame.add(defaultGame);
        frame.add(setAsDefault);
        frame.add(defaultDimensions);
        frame.add(defaultMinesCount);
        frame.setVisible(true);
    }

    private boolean isValid(int numOfMines, int dimensions) {
        return !(numOfMines >= (dimensions * dimensions) - 4);
    }
}