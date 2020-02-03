import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier {
    private Dataset dataset;
    private int size;

    // Index in list is number of attribute, key in map is value and value in map is number of occurrence per class
    private List<Map<String, Integer>> republicanAttributeFrequencies;
    private List<Map<String, Integer>> democratAttributeFrequencies;

    public NaiveBayesClassifier(Dataset d) {
        this.dataset = d;
        this.size = d.getNumberExamples();

        this.republicanAttributeFrequencies = calculateFrequencies("republican");
        this.democratAttributeFrequencies = calculateFrequencies("democrat");
    }

    private List<Map<String, Integer>> calculateFrequencies(String classification) {
        List<Map<String, Integer>> frequencies = new ArrayList<>(Dataset.NUMBER_ATTRIBUTES);

        // Initializing the hashmaps
        for (int i = 0; i < Dataset.NUMBER_ATTRIBUTES; i++) {
            Map<String, Integer> map = new HashMap<>();
            map.put("n", 0);
            map.put("y", 0);
            frequencies.add(map);
        }

        for (int i = 0; i < this.size; i++) {
            for (int j = 1; j < Dataset.NUMBER_ATTRIBUTES; j++) {
                if (this.dataset.getDataset()[i][0].equals(classification)) {
                    int count = frequencies.get(j).get(this.dataset.getDataset()[i][j]) + 1;
                    frequencies.get(j).put(this.dataset.getDataset()[i][j], count);
                }
            }
        }

        return frequencies;
    }

    public void printFrequencies() {
        System.out.println("Printing frequencies for republicans");
        for (int i = 0; i < this.republicanAttributeFrequencies.size(); i++) {
            System.out.println("For attribute: " + i);
            for (Map.Entry<String, Integer> entry : republicanAttributeFrequencies.get(i).entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue().toString() + ", ");
            }
            System.out.println();
        }

        System.out.println("Printing frequencies for democrats");
        for (int i = 0; i < this.democratAttributeFrequencies.size(); i++) {
            System.out.println("For attribute: " + i);
            for (Map.Entry<String, Integer> entry : democratAttributeFrequencies.get(i).entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue().toString() + ", ");
            }
            System.out.println();
        }
    }

    public String classifyExample(String[] example) {
        double republicanProbability = (double) this.dataset.getNumberRepublicans() / this.size;
        double democratProbability = (double) this.dataset.getNumberDemocrats() / this.size;

        for (int i = 1; i < this.republicanAttributeFrequencies.size(); i++) {
            republicanProbability *= (double) this.republicanAttributeFrequencies.get(i).get(example[i]) / this.dataset.getNumberRepublicans();
        }

        for (int i = 1; i < this.democratAttributeFrequencies.size(); i++) {
            democratProbability *= (double) this.democratAttributeFrequencies.get(i).get(example[i]) / this.dataset.getNumberDemocrats();
        }

        return republicanProbability > democratProbability ? "republican" : "democrat";
    }

    public double evaluateModel(String[][] testSet) {
        double modelAccuracy = 0.0;
        int countCorrectlyClassified = 0;

        for (int i = 0; i < testSet.length; i++) {
            String classification = classifyExample(testSet[i]);
            if (classification.equals(testSet[i][0])) {
                countCorrectlyClassified++;
            }
        }
        modelAccuracy = (double) (countCorrectlyClassified * 100) / testSet.length;
        return modelAccuracy;
    }

    public static void main(String[] args) {
        Dataset d = new Dataset("D:\\CodeHub\\Information-Retrieval-and-Knowledge-Discovery-FMI\\Artificial-intelligence\\naiveBayesClassifier\\src\\resources\\house-votes-84.txt");
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(d);
        CrossValidation kCrossValidation = new CrossValidation(10, d);

        //cross validation
        double accuracy = kCrossValidation.crossValidate();
        System.out.println("Model 10-fold-cross-validated accuracy is: " + accuracy);

        // Live Classification
        System.out.println("Live classification example:");
        String[] democrat = {"", "y", "y", "y", "n", "y", "y", "n", "y", "y", "y", "y", "n", "n", "n", "n", "y"};
        String[] republican = {"", "n", "n", "n", "y", "y", "y", "n", "n", "n", "n", "n", "y", "y", "y", "n", "n"};
        System.out.println("Example is classified as: " + classifier.classifyExample(democrat) + ", expected: democrat");
        System.out.println("Example1 is classified as: " + classifier.classifyExample(republican) + ", expected: republican");
    }
}
