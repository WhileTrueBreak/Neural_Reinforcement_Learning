package main;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import main.ai.DeepQLearner;
import main.display.Display;
import main.game.Game;
import main.input.KeyManager;
import main.input.MouseManager;

public class Main implements Runnable {
	
	private int width, height;
	private String title;
	
	private Display display;
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;

	//input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//game
	private Game game;
	
	//AI
	private DeepQLearner blackAI;
	private DeepQLearner whiteAI;
	
	//timer
	private int renderTimer = 4;
	private int counter = 10;
	
	public Main(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	private void init() {
		display = new Display(title, width, height);
		
		display.getJFrame().addKeyListener(keyManager);
		display.getJFrame().addMouseListener(mouseManager);
		display.getJFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		
		game = new Game(width, height);
		
		int[] structure = {9, 9, 9};
		
		blackAI = new DeepQLearner(game, structure, 0.01d, 0.95d, 1, -1);
		whiteAI = new DeepQLearner(game, structure, 0.01d, 0.95d, 0, 1);
	}

	public void run() {
		running = true;
		init();
		
		int fps = 10000;
		double timeperTick = 1000000000/fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime)/timeperTick;
			timer += now - lastTime;
			lastTime = now;
			if(delta >= 1) {
				update();
				render();
				ticks++;
				delta--;
				if(delta > 1)
					delta = 0;
			}
			if(timer >= 1000000000) {
				System.out.println("[Main]\t\t" + ticks + " current fps");
				System.out.println("[Main]\t\t" + fps + " set fps");
				System.out.println("[Main]\t\t" + counter + " seconds");
				if(counter <= 0) fps = 1;
				else counter--;
				timeperTick = 1000000000/fps;
				ticks = 0;
				timer = 0;
			}
		}
		
		stop();
	}
		
	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.setName("Main");
		thread.start();
	}
	
	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void observe() {
		while(game.getActions().size() != 0 && game.hasWon() == 0) {
			if(game.getTurn() == 1) {
				whiteAI.makeMove();
			}else {
				blackAI.makeMove();
			}
		}
	}

	private void update() {
		game.reset();
		observe();
		whiteAI.learn();
		blackAI.learn();
	}
	
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		// Draw 
		game.render(g);
		// End draw
		bs.show();
		g.dispose();
	}
	
	////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Display getDisplay() {
		return display;
	}
	public KeyManager getKeyManager() {
		return keyManager;
	}
	public MouseManager getMouseManager() {
		return mouseManager;
	}

}
