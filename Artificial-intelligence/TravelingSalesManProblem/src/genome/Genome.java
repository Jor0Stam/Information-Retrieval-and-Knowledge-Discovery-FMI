package genome;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Genome {
    int size;
    int fitness;
    Point startPoint;
    List<Point> genome;

    /**
     *  Keys of pricesPerCity are all the cities.
     *  For every key(current city) we have map with ->
     *  key: city number, value: cost from current city
     */
    Map<Point, Map<Point, Double>> pricesPerCity;

    public Genome(Map<Point, Map<Point, Double>> costs, Point startPoint) {
        this.pricesPerCity = costs;
        this.startPoint = startPoint;
        this.size = costs.keySet().size() + 1;
        this.genome = getRandomGenome();
        this.fitness = calcFitness();
    }

    public Genome(Genome oldGenome, List<Point> newGenomeSequence) {
        this.pricesPerCity = oldGenome.pricesPerCity;
        this.startPoint = oldGenome.startPoint;
        this.genome = new ArrayList<>(newGenomeSequence);
        this.fitness = calcFitness();
    }

    public int getFitness() {
        return this.fitness;
    }

    public List<Point> getGenome() {
        return this.genome;
    }

    private List<Point> getRandomGenome() {
        List<Point> randomGenome = new ArrayList<>();

        for (Point city: this.pricesPerCity.keySet()) {
            if (!city.equals(this.startPoint)) {
                randomGenome.add(city);
            }
        }

        Collections.shuffle(randomGenome);
        return randomGenome;
    }

    private int calcFitness() {
        int fitness = 0;
        Point currentCity = (Point) this.startPoint.clone();

        for (Point nextCity: this.genome) {
            fitness += this.pricesPerCity.get(currentCity).get(nextCity);
            currentCity = nextCity;
        }

        fitness += this.pricesPerCity.get(currentCity).get(startPoint);
        return fitness;
    }

    public int compareTo(Genome otherGenome) {
        return Integer.compare(this.fitness, otherGenome.getFitness());
    }

    @Override
    public String toString() {
        return "Path length: " + this.getFitness();
    }
}