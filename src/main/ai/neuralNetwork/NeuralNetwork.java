package main.ai.neuralNetwork;

import java.util.ArrayList;

import matrix.Matrix;

public class NeuralNetwork {

	ArrayList<Layer>layers = new ArrayList<Layer>();

	public NeuralNetwork(int[] struct){
		for(int i = 1;i < struct.length;i++){
			layers.add(new Layer(struct[i-1], struct[i]));
		}
	}

	public double[] feedForward(double[] in){
		Matrix input = Matrix.fromArray(in);
		for(Layer l:layers){
			input = l.feedForward(input);
		}
		return input.toArray();
	}

	public void train(double[] in, double[] out, double learningRate){
		Matrix output = Matrix.fromArray(feedForward(in));
		Matrix error = Matrix.sub(Matrix.fromArray(out), output);
		for(int i = layers.size()-1;i >= 1;i--){
			error = layers.get(i).train(layers.get(i-1).getNodes(), error, learningRate);
		}
		layers.get(0).train(Matrix.fromArray(in), error, learningRate);
	}

}
