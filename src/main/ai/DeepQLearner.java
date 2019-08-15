package main.ai;

import java.util.Random;

import org.javatuples.Pair;

import main.ai.neuralNetwork.NeuralNetwork;
import main.game.Game;

public class DeepQLearner {
	
	private double learningRate, discount;
	private double explorationRate, exploration_delta = 0.01;
	
	private NeuralNetwork neuralNetwork;
	
	private Game game;
	
	public DeepQLearner(Game game, int[] struct, double learningRate, double discount, double explorationRate) {
		neuralNetwork = new NeuralNetwork(struct);
		this.learningRate = learningRate;
		this.discount = discount;
		this.explorationRate = explorationRate;
	}
	
	private double getQ(double[] state) {
		return neuralNetwork.feedForward(state)[0];
	}
	
	private Pair getNextAction(Pair[] actions) {
		if(Math.random() < explorationRate) {
			return actions[new Random().nextInt(actions.length)];
		}else {
			return bestAction(actions);
		}
	}
	
	private Pair bestAction(Pair[] actions) {
		//TODO use aneural network
		return actions[new Random().nextInt(actions.length)];
	}
	
}
