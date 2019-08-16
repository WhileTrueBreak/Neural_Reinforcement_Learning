package main.ai.neuralNetwork;

import matrix.Activation;
import matrix.Matrix;

public class Layer {

	private int nodeNum;

	private Matrix nodes, weights, bias;

	public Layer(int in, int out){
		this.nodeNum = out;

		weights = new Matrix(in, out);
		bias = new Matrix(1, out);
		
		weights.randomiseMatrix(-1, 1);
		bias.randomiseMatrix(-1, 1);
	}

	public Matrix feedForward(Matrix in){
		//calc node
		nodes = Matrix.matrixProduct(weights, in);
		nodes.applyFunc(new Activation() {public double function(double n) {return (float)(1/( 1 + Math.pow(Math.E,(-1*n))))*2-1;}});
		return nodes;
	}

	public Matrix train(Matrix prevNode, Matrix error, double learningRate){
		//get next error
		Matrix weights_t = Matrix.transpose(weights);
		Matrix next_err = Matrix.matrixProduct(weights_t, error);
		//calc gradient
		nodes.applyFunc(new Activation() {public double function(double n) {return ((n+1)/2)*(1-((n+1)/2));}});
		nodes = Matrix.hadamardProduct(nodes, error);
		nodes = Matrix.scalarProduct(nodes, learningRate);
		//update bias
		bias = Matrix.add(bias, nodes);
		//get deltas
		Matrix prev_node_t = Matrix.transpose(prevNode.cloneMatrix());
		Matrix weights_delta = Matrix.matrixProduct(nodes, prev_node_t);
		//update weights
		weights = Matrix.add(weights, weights_delta);

		return next_err;
	}

	public int getNodeNum() {
		return nodeNum;
	}

	public Matrix getNodes() {
		return nodes;
	}

}
