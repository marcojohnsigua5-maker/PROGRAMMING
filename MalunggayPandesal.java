import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class MalunggayPandesal extends JFrame implements ActionListener {

    JButton[] buttons = new JButton[9];
    JLabel status, scoreLabel;

    int scoreX = 0, scoreO = 0;

    // Player 
    String player1 = "X";
    String player2 = "O";
    boolean player1Turn = true;

    LinkedList<Integer> historyX = new LinkedList<>();
    LinkedList<Integer> historyO = new LinkedList<>();

    Color bgDark = new Color(18, 18, 18);
    Color cardDark = new Color(30, 30, 30);
    Color hoverDark = new Color(45, 45, 45);
    Color malunggayGreen = new Color(46, 204, 113);
    Color neonBlue = new Color(52, 152, 219);
    Color softWhite = new Color(236, 240, 241);
    Color dangerRed = new Color(231, 76, 60);

    public MalunggayPandesal() {

        chooseSymbol();

        setTitle(" Malunggay Pandesal ");
        setSize(450, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(bgDark);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        status = new JLabel("PLAYER 1 (" + player1 + ") TURN", JLabel.CENTER);
        status.setFont(new Font("Montserrat", Font.BOLD, 24));
        status.setForeground(malunggayGreen);

        scoreLabel = new JLabel("X: 0  •  O: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        scoreLabel.setForeground(softWhite);

        topPanel.add(status);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 12, 12));
        boardPanel.setBackground(bgDark);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 55));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(cardDark);
            buttons[i].setForeground(softWhite);
            buttons[i].setBorder(new LineBorder(new Color(60, 60, 60), 2));

            int index = i;

            //  hover
            buttons[i].addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (buttons[index].getText().equals("")) {
                        buttons[index].setBackground(hoverDark);
                    }
                }

                public void mouseExited(MouseEvent e) {
                    if (buttons[index].getText().equals("")) {
                        buttons[index].setBackground(cardDark);
                    }
                }
            });

            buttons[i].addActionListener(this);
            boardPanel.add(buttons[i]);
        }

        add(boardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottomPanel.setBackground(bgDark);

        JButton reset = new JButton("SETTINGS / RESET");
        reset.setPreferredSize(new Dimension(300, 50));
        reset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reset.setBackground(malunggayGreen);
        reset.setForeground(Color.BLACK);
        reset.setFocusPainted(false);
        reset.setBorder(null);

        reset.addActionListener(e -> showResetOptions());
        bottomPanel.add(reset);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // CHOOSE 
    public void chooseSymbol() {
        String[] options = {"Play as X", "Play as O"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose your symbol",
                "Start Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 1) {
            player1 = "O";
            player2 = "X";
        } else {
            player1 = "X";
            player2 = "O";
        }
    }

    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        int index = -1;
        for (int i = 0; i < 9; i++) {
            if (buttons[i] == clicked) {
                index = i;
                break;
            }
        }

        if (!clicked.getText().equals("")) return;

        String symbol = player1Turn ? player1 : player2;

        // REMOVE oldest per symbol
        if (symbol.equals("X") && historyX.size() == 3) {
            clearCell(historyX.removeFirst());
        }
        if (symbol.equals("O") && historyO.size() == 3) {
            clearCell(historyO.removeFirst());
        }

        clicked.setText(symbol);
        clicked.setForeground(symbol.equals("X") ? neonBlue : dangerRed);

        if (symbol.equals("X")) historyX.add(index);
        else historyO.add(index);

        player1Turn = !player1Turn;

        status.setText((player1Turn ? "PLAYER 1 (" + player1 + ")" : "PLAYER 2 (" + player2 + ")") + " TURN");
        status.setForeground(player1Turn ? malunggayGreen : dangerRed);

        checkWinner();
    }

    private void clearCell(int i) {
        buttons[i].setText("");
        buttons[i].setBackground(cardDark);
        buttons[i].setForeground(softWhite);
    }

    public void checkWinner() {
        int[][] win = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };

        for (int[] w : win) {
            String a = buttons[w[0]].getText();
            String b = buttons[w[1]].getText();
            String c = buttons[w[2]].getText();

            if (!a.equals("") && a.equals(b) && b.equals(c)) {

                if (a.equals("X")) scoreX++;
                else scoreO++;

                updateScore();
                JOptionPane.showMessageDialog(this, a + " WINS!");
                resetGame();
                return;
            }
        }
    }

    public void updateScore() {
        scoreLabel.setText("X: " + scoreX + "  •  O: " + scoreO);
    }

    public void resetGame() {
        for (int i = 0; i < 9; i++) {
            clearCell(i);
            buttons[i].setBorder(new LineBorder(new Color(60, 60, 60), 2));
        }

        historyX.clear();
        historyO.clear();

        player1Turn = true;
        status.setText("PLAYER 1 (" + player1 + ") TURN");
        status.setForeground(malunggayGreen);
    }

    public void resetScore() {
        scoreX = 0;
        scoreO = 0;
        updateScore();
    }

    public void showResetOptions() {
        String[] options = {"Reset Board", "Clear Scores", "Full Restart"};

        int choice = JOptionPane.showOptionDialog(this,
                "System Maintenance", "Settings",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 0) resetGame();
        else if (choice == 1) resetScore();
        else if (choice == 2) {
            resetGame();
            resetScore();
        }
    }

    public static void main(String[] args) {
        new MalunggayPandesal();
    }
}