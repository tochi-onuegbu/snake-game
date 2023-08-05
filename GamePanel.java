import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; //how big we want the objects in the game and grid
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE); //how many objects we can fit on the screen
    static final int DELAY = 200; //dictates how fast the snake is running
    final int x[] = new int[GAME_UNITS]; //holds the x coordinates of the body parts of the snake (including the head)
    final int y[] = new int[GAME_UNITS]; //holds the y coordinates of the body parts of the snake
    int bodyParts = 6; //body parts of the snake
    int applesEaten; //number of apples eaten is initially 0
    int appleX; //x coordinates of where the apple is located. it will move each time the snake eats the apple
    int appleY; //y coordinates of where the apple is located. it will move each time the snake consumes the apple
    char direction = 'R'; //snake's direction when the game begins. R=right, L=left, U=up, D=down
    boolean running = false;
    Timer timer;
    Random random;


    //constructor 
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }
    public void startGame(){
        newApple(); //a new apple is created 
        running = true; // the boolean running is set to true because it is initialized as false
        timer = new Timer(DELAY, this);
        timer.start();

    }
    public void paintComponent(Graphics g){
    super.paintComponent(g);
    if (running){
        draw(g);
    }
    else{
        gameOver(g);
    }
}

    
public void draw(Graphics g){
    // Draw the background (replace Color.white with an appropriate background)
    g.setColor(Color.white);
    g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    // Draw the apple
    g.setColor(new Color(255, 0, 0));
    g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

    // Draw the snake
    for (int i = 0; i < bodyParts; i++){
        if(i == 0){
            g.setColor(new Color(80, 216, 250));
        }
        else{
            // Add shading to the snake's body
            int shade = 180 - i * 10; // Adjust the shading intensity
            g.setColor(new Color(80, shade, 0));
        }
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
    }

    // Draw the scoreboard
    g.setColor(Color.black);
    g.setFont(new Font("Helvetica", Font.BOLD, 20));
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

    // Add a highlight to the apple and the snake's head
    g.setColor(Color.white);
    g.fillOval(x[0] + UNIT_SIZE / 6, y[0], UNIT_SIZE / 4, UNIT_SIZE / 4); // Snake's head highlight

    // Add a shadow beneath the apple and the snake
    g.setColor(new Color(200, 200, 200, 100));
    g.fillRect(appleX + UNIT_SIZE / 8, appleY + UNIT_SIZE, UNIT_SIZE, 5); // Apple shadow
    
}
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

    }
    //the snake will move with this method
    public void move(){
        for(int i= bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
        case 'U':
            y[0]= y[0] - UNIT_SIZE;
            break;
        case 'D':
            y[0]= y[0] + UNIT_SIZE;
            break;
        case 'L':
            x[0]= x[0] - UNIT_SIZE;
            break;
        case 'R':
            x[0]= x[0] + UNIT_SIZE;
            break;
        }
    }
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollisions(){
        //we need to check if the head of the snake collides with its body
        for(int i=bodyParts; i>0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        //check to see if the head of snake collides with left border
        if (x[0] < 0){
            running = false;
        }

        //check to see if the head of snake collides with right border
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE){
            running = false;
        }
        //check to see if the head of snake collides with top border
        if (y[0] < 0){
            running = false;
        }
        //check to see if the head of snake collides with bottom border
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE){
            running = false;
        }

        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("Courier", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        //final score
        g.setColor(Color.red);
            g.setFont(new Font("Helvetica", Font.BOLD, 20));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    }

    @Override //this made the snake move
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
        
    }

    //inner class
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){ //controls the snake using keys
            switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if(direction != 'R'){
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(direction != 'L'){
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if(direction != 'D'){
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if(direction != 'U'){
                    direction = 'D';
                }
                break;
            }
        } 
    }
    
}
