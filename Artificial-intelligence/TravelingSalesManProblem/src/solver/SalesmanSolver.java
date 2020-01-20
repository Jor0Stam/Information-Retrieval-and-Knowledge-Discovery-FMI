package solver;

import genome.Genome;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SalesmanSolver {
    private int populationSize;
    private int reproductionSize;
    private int generationSize;
    private int maxEpochs;
    private int genomeSize;
    private int tournamentSize;
    private int tournamentGroupSize;
    private double mutationRate;
    private Random rand;
    private Point startPoint;
    private SelectionType selectionType;
    private  Map<Point, Map<Point, Double>> costs;

    public SalesmanSolver(Map<Point, Map<Point, Double>> costs, int cities, Point startPoint,
                          int babyBoomersSize, int reproductionSize, SelectionType selectionType) {
        rand = new Random();
        this.costs = costs;
        this.startPoint = startPoint;
        this.populationSize = babyBoomersSize;
        this.selectionType = selectionType;
        this.reproductionSize = reproductionSize;
        this.genomeSize = cities - 1;

        this.generationSize = 1000;
        this.maxEpochs = 200;
        this.tournamentSize = 200;
        this.tournamentGroupSize = 50;
        this.mutationRate = 0.1;
    }

    public Genome evolve() {
        List<Genome> population = this.initializePopulation();
        Genome bestPath = population.get(0);
        for (int i = 0; i < this.maxEpochs; i++) {
            List<Genome> champions = this.selection(population);
            population = this.breedGenomes(champions);
            bestPath = getMinPath(population);

            if (i == 1 || i == 10 || i == 50 || i == 150) {
                System.out.println(String.format("After {%d}th iteration: ", i)  + bestPath);
            }
        }
        return bestPath;
    }

    private Genome getMinPath(List<Genome> population) {
        Genome minPath = population.get(0);

        for (Genome genome: population) {
            if (minPath.compareTo(genome) > 0) {
                minPath = genome;
            }
        }

        return minPath;
    }

    private List<Genome> breedGenomes(List<Genome> population) {
        List<Genome> generation = new ArrayList<>();
        int currGenerationSize = 0;

        generation.add(getMinPath(population));
        while(currGenerationSize < this.generationSize - 1) {
            List<Genome> parents = chooseRandomParents(population);
            List<Genome> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));

            generation.addAll(children);
            currGenerationSize += 2;
        }

        return generation;
    }

    private Genome mutate(Genome genome) {
        double chanceForMutation = rand.nextDouble();

        if (chanceForMutation < mutationRate) {
            List<Point> mutatedGenome = genome.getGenome();
            Collections.swap(mutatedGenome, rand.nextInt(mutatedGenome.size()), rand.nextInt(mutatedGenome.size()));
            return new Genome(genome, mutatedGenome);
        }

        return genome;
    }

    private List<Genome> crossover(List<Genome> parents) {
        int stop = rand.nextInt(this.genomeSize);
        List<Genome> children = new ArrayList<>();

        List<Point> firstParentGenome = new ArrayList<>(parents.get(0).getGenome());
        List<Point> secondParentGenome = new ArrayList<>(parents.get(1).getGenome());

        // Create first child
        for (int i = 0; i < stop; i++) {
            Point swapPoint;
            swapPoint = secondParentGenome.get(i);
            Collections.swap(firstParentGenome, firstParentGenome.indexOf(swapPoint), i);
        }

        children.add(new Genome(parents.get(0), firstParentGenome));
        firstParentGenome = parents.get(0).getGenome();

        // Create second child
        for (int i = stop; i < secondParentGenome.size(); i++) {
            Point swapPoint = firstParentGenome.get(i);
            Collections.swap(secondParentGenome, secondParentGenome.indexOf(swapPoint), i);
        }
        children.add(new Genome(parents.get(1), secondParentGenome));

        return children;
    }

    private List<Genome> chooseRandomParents(List<Genome> population) {
        int firstParent, secondParent;
        List<Genome> parents = new ArrayList<>();

        firstParent = rand.nextInt(population.size());
        do {
            secondParent = rand.nextInt(population.size());
        } while (firstParent == secondParent);

        parents.add(population.get(firstParent));
        parents.add(population.get(secondParent));

        return parents;
    }

    private List<Genome> initializePopulation() {
        List<Genome> population = new ArrayList<>();

        for (int i = 0; i < this.populationSize; i++) {
            population.add(new Genome(costs, startPoint));
        }
        return population;
    }

    private List<Genome> selection(List<Genome> population) {
        List<Genome> champions = new ArrayList<>();

        // Keeping best path so far
        champions.add(getMinPath(population));

        for (int i = 0; i < reproductionSize - 1; i++) {
             if (this.selectionType == SelectionType.RANK) {
                champions.add(this.getByRank(population));
            } else if (this.selectionType == SelectionType.ROULETTE) {
                champions.add(this.getByRoulette(population));
            } else if (this.selectionType == SelectionType.TOURNAMENT) {
                champions.add(this.getByTournament(population));
            }
        }

        return champions;
    }

    private Genome getByRoulette(List<Genome> population) {
        int totalFitness = population.stream()
                                     .map(Genome::getFitness)
                                     .mapToInt(Integer::intValue)
                                     .sum();

        int selectedValue = rand.nextInt(totalFitness);

        // Reverted values give better probability to smaller values, a.k.a. shorter paths
        double revertedValue = (double) 1 / selectedValue;

        double currentSum = 0;
        for (Genome genome: population) {
            currentSum += (double) 1 / genome.getFitness();
            if (currentSum >= selectedValue) {
                return genome;
            }
        }

        return population.get(rand.nextInt(this.populationSize - 1));
    }

    private Genome getByRank(List<Genome> population) {
        return null;
    }

    private Genome getByTournament(List<Genome> population) {
        Genome bestGenome = population.get(0);
        List<Genome> currentWinners;

        for (int i = 0; i < this.tournamentSize; i++) {
            currentWinners = getRandomGenomes(population, tournamentGroupSize);
            Genome thisGroupBest = getMinPath(currentWinners);
            if (thisGroupBest.getFitness() < bestGenome.getFitness()) {
                bestGenome = thisGroupBest;
            }
        }

        return bestGenome;
    }

    private List<Genome> getRandomGenomes(List<Genome> population, int size) {
        List<Genome> picked = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            picked.add(population.get(rand.nextInt(population.size())));
        }

        return picked;
    }
}
