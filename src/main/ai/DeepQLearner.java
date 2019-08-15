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
//		System.out.print("NN output [");
//		for(int i = 0;i < ranking.length;i++) {
//			System.out.print((Math.round(ranking[i]*100.0)/100.0) + " ");
//		}
//		System.out.println("]");
		int[] bestAction = new int[1];
		double highest = Double.NEGATIVE_INFINITY;
//		System.out.println("action space: " + actions.size());
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
		actionMemory.add(action);
		double[] input = new double[game.getBoard().length];
		for(int i = 0;i < input.length;i++) input[i] = game.getBoard()[i]*side;
		stateMemory.add(input);
		game.makeMove(action[0]);
		explorationRate-=exploration_delta;
	}
	
	public void train(double reward) {
		if(reward==0) {
			for(int i = stateMemory.size()-1;i >= 0;i--) {
				double[] expectedOutput = new double[9];
				for(int j = 0;j < expectedOutput.length;j++) {
					if(j != actionMemory.get(i)[0]) expectedOutput[j] = 1*Math.pow(discount, stateMemory.size()-i-1)*reward;
				}
				neuralNetwork.train(stateMemory.get(i), expectedOutput, learningRate);
			}
		}else {
			for(int i = stateMemory.size()-1;i >= 0;i--) {
				double[] expectedOutput = new double[9];
				expectedOutput[actionMemory.get(i)[0]] = 1*Math.pow(discount, stateMemory.size()-i-1)*reward;
				neuralNetwork.train(stateMemory.get(i), expectedOutput, learningRate);
			}
		}
	}
	
}
