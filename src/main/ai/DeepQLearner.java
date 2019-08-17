package main.ai;

import java.util.ArrayList;
import java.util.Random;

import main.ai.neuralNetwork.NeuralNetwork;
import main.game.Game;

public class DeepQLearner {

	private ArrayList<double[]> stateMemory;
	private ArrayList<int[]> actionMemory;
	private ArrayList<Double> rewardMemory;

	private double learningRate, discount;
	private double explorationRate;

	private int side;

	private NeuralNetwork neuralNetwork;

	private Game game;

	public DeepQLearner(Game game, int[] struct, double learningRate, double discount, double explorationRate, int side) {
		this.game = game;
		neuralNetwork = new NeuralNetwork(struct);
		this.learningRate = learningRate;
		this.discount = discount;
		this.explorationRate = explorationRate;
		this.side = side;

		stateMemory = new ArrayList<double[]>();
		actionMemory = new ArrayList<int[]>();
		rewardMemory = new ArrayList<Double>();
	}

	public void makeMove() {
		int[] action = new int[1];
		double[] state = new double[game.getBoard().length];
		for(int i = 0;i < state.length;i++) state[i] = game.getBoard()[i];
		if(Math.random() < explorationRate) {
			action = game.getActions().get((int) Math.floor(Math.random()*game.getActions().size()));
		}else {
			double[] Q = neuralNetwork.feedForward(state);
			int highestQIndex = -1;
			double currentHighest = Double.NEGATIVE_INFINITY;
			for(int i = 0;i < Q.length;i++) {
				boolean valid = false;
				for(int[] a:game.getActions()) if(a[0] == i) valid = true;
				if(Q[i] > currentHighest && valid) {
					currentHighest = Q[i];
					highestQIndex = i;
				}
			}
			action[0] = highestQIndex;
		}
		game.makeMove(action[0]);
		stateMemory.add(state);
		actionMemory.add(action);
		if(game.hasWon() != 0) {
			rewardMemory.add((double) (game.hasWon()*side));
		}else {
			rewardMemory.add(-0.01d);
		}
	}
	
	public void learn() {
		for(int i = 0;i < stateMemory.size();i++) {
			double Rd = 0;
			for(int j = 0;j < rewardMemory.size()-i;j++) Rd += rewardMemory.get(j+i)*Math.pow(discount, j);
			double[] action = new double[9];
			if(i == stateMemory.size()-1) action[actionMemory.get(i)[0]] = rewardMemory.get(i);
			else action[actionMemory.get(i)[0]] = Rd;
			neuralNetwork.train(action, action, learningRate);
		}
	}

	public double[] getQs(double[] state) {
		return neuralNetwork.feedForward(state);
	}

}
