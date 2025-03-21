import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 500;
        int boardHeight = 750;

        JFrame frame = new JFrame("Flappy Dragon");
        frame.setVisible(true);  
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flappyBird FD = new flappyBird();
        frame.add(FD);
        frame.pack();
        FD.requestFocus();
        frame.setVisible(true);
    }
}
