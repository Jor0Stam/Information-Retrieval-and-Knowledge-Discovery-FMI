package invoker;

import solver.SalesmanSolver;
import solver.SelectionType;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Invoker {
    public static void main(String[] Args) {
        Scanner sc = new Scanner(System.in);
        List<Point> cities;
        System.out.print("Number of cities: ");
        int numberOfPoints = sc.nextInt();
        sc.close();

        Map<Point, Map<Point, Double>> costs = new HashMap<>();
        cities = Invoker.populateGrid(numberOfPoints);
        Invoker.calculateCosts(costs, cities);

        SalesmanSolver solver = new SalesmanSolver(costs, cities.size(), cities.get(0), 20, 10, SelectionType.TOURNAMENT);
        System.out.println("After final iteration: " + solver.evolve());
    }

    private static List<Point> populateGrid(int numberOfPoints) {
        Random rand = new Random();
        Set<Point> population = new HashSet<>();

        do {
            population.add(new Point(rand.nextInt(10000), rand.nextInt(10000)));
        }
        while (population.size() < numberOfPoints);

        return new ArrayList<>(population);
    }

    private static void calculateCosts(Map<Point, Map<Point, Double>> costs, List<Point> population) {
        for (Point city: population) {
            Map<Point, Double> costsFromCurrentCity = new HashMap<>();
            for (Point nextCity: population) {
                if (nextCity != city) {
                    costsFromCurrentCity.put(nextCity, city.distance(nextCity));
                }
            }
            costs.put(city, costsFromCurrentCity);
        }
    }
}
