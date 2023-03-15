import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.util.Random;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH= 600;
	static final int SCREEN_HEIGHT= 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 3;
	int appleEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	boolean firstRun = true;
	final Timer timer  = new Timer(DELAY,this);
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
	}
	public void gameStart(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD,40));
		FontMetrics metricsStart = getFontMetrics(g.getFont());
		g.drawString("Press space to start",(SCREEN_WIDTH - metricsStart.stringWidth("Press space to start"))/2 , SCREEN_HEIGHT/2);
	}
	
	public void startGame() {
		x[0] = SCREEN_WIDTH/2;
		y[0] = SCREEN_HEIGHT/2;
		newApple();
		running = true;
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(firstRun) {
			gameStart(g);
		}
		else
		draw(g);
	}
	
	public void draw(Graphics g) {

		if(running) {
			
//			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i *UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i *UNIT_SIZE, SCREEN_WIDTH, i *UNIT_SIZE);
//			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i  < bodyParts; i ++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + appleEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + appleEaten))/2 , g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i = bodyParts;  i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if((x[0]==appleX)&&(y[0]==appleY)) {
			bodyParts++;
			appleEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		for(int i = bodyParts;i>0; i--) {
			if((x[0] == x[i])&&(y[0] == y[i])) {
				running  = false;
			}
		}
		
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] >SCREEN_WIDTH ) {
			running = false;
		}
		if(y[0] <0) {
			running = false;
		}
		if(y[0] >SCREEN_HEIGHT) {
			running = false;
		}
	}
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD,40));
		FontMetrics metricsScore = getFontMetrics(g.getFont());
		g.drawString("Score: " + appleEaten,(SCREEN_WIDTH - metricsScore.stringWidth("Score: " + appleEaten))/2 , g.getFont().getSize());
	
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD,75));
		FontMetrics metricsEnd = getFontMetrics(g.getFont());
		g.drawString("Game Over",(SCREEN_WIDTH - metricsEnd.stringWidth("Game OVer"))/2 , SCREEN_HEIGHT/2);
		
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD,35));
		FontMetrics metricsRestart = getFontMetrics(g.getFont());
		g.drawString("Press space to restart",(SCREEN_WIDTH - metricsRestart.stringWidth("Press space to restart"))/2 , SCREEN_HEIGHT/2 + 100);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		 
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if((key == KeyEvent.VK_SPACE)&& (firstRun ==true)) {
				firstRun = false;
				startGame();
			}
			if((key == KeyEvent.VK_SPACE)&& (running ==false)) {
				x[0] = 0;
				y[0] = 0;
				direction = 'R';
				bodyParts = 3;
				appleEaten = 0;
				
				startGame();
				
			}
            switch(key) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;

			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;

			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
		}
	}

}
