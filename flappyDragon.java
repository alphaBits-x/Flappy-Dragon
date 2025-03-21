import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class flappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 500;
    int boardHeight = 750;

    //images
    Image backgroundImage;
    Image dragonImage;
    Image topImage;
    Image bottomImage;

    //dragon
    int dragonx = boardWidth/8;
    int dragony = boardHeight/2;
    int dragonWidth = 55;
    int dragonHeight = 45;

    class Dragon{
        int x = dragonx;
        int y = dragony;
        int width = dragonWidth;
        int height = dragonHeight;
        Image img;

        public Dragon(Image img){
            this.img = img;
        }
    }
    //pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        public Pipe(Image img){
            this.img = img;

        }


    }

    // game logic
    Dragon dragon;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();


    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;



    public flappyBird() {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);


        //load images
        backgroundImage = new ImageIcon(getClass().getResource("./mountain_scene_resized.png")).getImage();
        dragonImage = new ImageIcon(getClass().getResource("./dragon_transparent.png")).getImage();
        topImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        dragon = new Dragon(dragonImage);
        pipes = new ArrayList<Pipe>();

        //place pipe timer
        placePipesTimer = new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();



        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

    }
     public void placePipes(){

        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
        
        Pipe bottomPipe = new Pipe(bottomImage);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);

     }





    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        g.drawImage(dragon.img, dragon.x, dragon.y, dragon.width, dragon.height, null);

        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Ariel", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        velocityY += gravity;
        dragon.y += velocityY;
        dragon.y = Math.max(dragon.y, 0 );

        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && dragon.x >pipe.x +pipe.width){
                pipe.passed = true;
                score += 0.5;
            }

            if(collision(dragon, pipe)){
                gameOver = true;
            }
        }


        if(dragon.y > boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(Dragon a, Pipe b){
        return a.x < b.x + b.width && a.x+a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if(gameOver){
                dragon.y = dragony;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
