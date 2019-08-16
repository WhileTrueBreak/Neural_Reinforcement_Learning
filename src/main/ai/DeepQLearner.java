package main.ai;

import java.util.ArrayList;
import java.util.Random;

import main.ai.neuralNetwork.NeuralNetwork;
import main.game.Game;

public class DeepQLearner {
	
	private ArrayList<double[]> stateMemory;
	private ArrayList<int[]> actionMemory;
	
	private double learningRate, discount;
	private double explorationRate, exploration_delta = 0.001;
	
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
	}
	
	private double getQ(double[] state, int[] action) {
		double[] Qs = neuralNetwork.feedForward(state);
		return Qs[action[0]];
	}
	
	private int[] getNextAction(ArrayList<int[]> actions) {
		if(Math.random() < explorationRate) {
			return actions.get(new Random().nextInt(actions.size()));
		}else {
			return bestAction(actions);
		}
	}
	
	private int[] bestAction(ArrayList<int[]> actions) {
		double[] input = new double[game.getBoard().length];
		for(int i = 0;i < input.length;i++) input[i] = game.getBoard()[i]*side;
		double[] ranking = neuralNetwork.feedForward(input);
		System.out.print("NN output [");
		for(int i = 0;i < ranking.length;i++) {
			System.out.print((Math.round(ranking[i]*100.0)/100.0) + " ");
		}
		System.out.println("]");
		int[] bestAction = new int[1];
		double highest = Double.NEGATIVE_INFINITY;
		for(int i = 0;i < ranking.length;i++) {
			boolean isValid = false;
			for(int[] act:actions) if(act[0]==i) isValid = true;
			if(ranking[i] > highest && isValid) {
				bestAction[0] = i;
				highest = ranking[i];
			}
		}
		return bestAction;
	}
	
	public void makeMove(ArrayList<int[]> actions) {
		int[] action = getNextAction(actions);
		double[] state = new double[9];
		for(int i = 0;i < state.length;i++) state[i] = game.getBoard()[i];
		stateMemory.add(state);
		game.makeMove(action[0]);
		actionMemory.add(action);
		double[] expectedOut = new double[9];
		expectedOut[action[0]] = -1;
		neuralNetwork.train(state, expectedOut, learningRate);
	}
	
	
}
