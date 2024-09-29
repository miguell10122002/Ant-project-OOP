package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InputTreater {
    private int n;
    private int n1;
    private int a;
    private double alpha;
    private double beta;
    private double delta;
    private double eta;
    private double rho;
    private double gamma;
    private int nu;
    private double tau;
    private int[][] weights;

    public boolean processInput(String[] args) {
        if (args.length < 2) {
            System.out.println("Insufficient arguments.");
            return false;
        }

        if (args[0].equals("-r")) {
            if (args.length < 11) {
                System.out.println("Insufficient arguments for generating graph.");
                return false;
            }

            n = Integer.parseInt(args[1]);
            a = Integer.parseInt(args[2]);
            n1 = Integer.parseInt(args[3]);
            alpha = Double.parseDouble(args[4]);
            beta = Double.parseDouble(args[5]);
            delta = Double.parseDouble(args[6]);
            eta = Double.parseDouble(args[7]);
            rho = Double.parseDouble(args[8]);
            gamma = Double.parseDouble(args[9]);
            nu = Integer.parseInt(args[10]);
            tau = Double.parseDouble(args[11]);

            // No need to read from file, return success
            return true;
        } else if (args[0].equals("-f")) {
            if (args.length < 2) {
                System.out.println("No input file specified.");
                return false;
            }

            String inputFile = args[1];
            return readInputFromFile(inputFile);
        } else {
            System.out.println("Invalid command.");
            return false;
        }
    }

    public boolean readInputFromFile(String inputFile) {
        try {
            File file = new File(inputFile);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // Read the parameters from the input file
            String parametersLine = reader.readLine();
            String[] parameters = parametersLine.split(" ");
            n = Integer.parseInt(parameters[0]);
            n1 = Integer.parseInt(parameters[1]);
            alpha = Double.parseDouble(parameters[2]);
            beta = Double.parseDouble(parameters[3]);
            delta = Double.parseDouble(parameters[4]);
            eta = Double.parseDouble(parameters[5]);
            rho = Double.parseDouble(parameters[6]);
            gamma = Double.parseDouble(parameters[7]);
            nu = Integer.parseInt(parameters[8]);
            tau = Double.parseDouble(parameters[9]);
            a = -1; // Not used when reading from file

            // Read the adjacency matrix and store the weights
            weights = new int[n][n];
            for (int i = 0; i < n; i++) {
                String[] weightsLine = reader.readLine().split(" ");
                for (int j = 0; j < n; j++) {
                    weights[i][j] = Integer.parseInt(weightsLine[j]);
                }
            }

            // Close the reader
            reader.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error occurred while reading the input file.");
            e.printStackTrace();
            return false;
        }
    }

    public int getN() {
        return n;
    }

    public int getN1() {
        return n1;
    }

    public int getA() {
        return a;
    }

    public double getBeta() {
        return beta;
    }

    public double getDelta() {
        return delta;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getEta() {
        return eta;
    }

    public double getRho() {
        return rho;
    }

    public double getGamma() {
        return gamma;
    }

    public double getTau() {
        return tau;
    }

    public int getNu() {
        return nu;
    }

    public int[][] getWeights() {
        return weights;
    }
}
