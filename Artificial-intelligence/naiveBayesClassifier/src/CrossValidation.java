import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 1. Shuffle the dataset randomly.
 * 2. Split the dataset into k groups
 * 3. For each unique group:
 *     3.1 Take the group as a hold out or test data set
 *     3.2 Take the remaining groups as a training data set
 *     3.3 Fit a model on the training set and evaluate it on the test set
 *     3.4 Retain the evaluation score and discard the model
 * 4. Summarize the skill of the model using the sample of model evaluation scores
 */

public class CrossValidation {
    private int numberOfGroups;
    private int groupSize;
    private String[][] dataset;
    private List<String[][]> groups;

    public CrossValidation(int numberOfGroups, Dataset d){
        this.numberOfGroups = numberOfGroups;
        this.dataset = d.getDataset();
        this.groupSize = (int) Math.floor(this.dataset.length / numberOfGroups);
        shuffleData();

        this.groups = splitIntoKGroups();
    }

    public double crossValidate(){
        List<Double> accuracies = new ArrayList<>();

        for(int i =0; i < groups.size(); i++){
            String[][] testSet = groups.get(i);
            String[][] trainSet = getTrainSet(i);

            NaiveBayesClassifier classifier = new NaiveBayesClassifier(new Dataset(trainSet));
            accuracies.add(classifier.evaluateModel(testSet));
        }

        //Printing each score
        accuracies.stream().forEach(System.out::println);

        return accuracies.stream().mapToDouble(a -> a).average().getAsDouble();
    }

    private void shuffleData() {
        int size = this.dataset.length;
        Random rnd = new Random();

        for (int i = 0; i < size; i++) {
            swap(this.dataset, i, rnd.nextInt(i + 1));
        }
    }

    private void swap(String[][] matrix, int rowFrom, int rowTo) {
        String[] tmp = matrix[rowFrom];
        matrix[rowFrom] = matrix[rowTo];
        matrix[rowTo]= tmp;
    }

    private List<String[][]> splitIntoKGroups(){
        List<String[][]> groups = new ArrayList<>();

        int dataIndex = 0;
        for(int i = 0; i < numberOfGroups; i++){
            String[][] group = new String[this.groupSize][Dataset.NUMBER_ATTRIBUTES];
            for(int j = 0; j< this.groupSize; j++){
                group[j] = this.dataset[dataIndex++];
            }
            groups.add(group);
        }
        return groups;
    }

    private String[][] getTrainSet(int indexGroupToSkip){
        String[][] trainSet = new String[this.groupSize * (numberOfGroups - 1)][Dataset.NUMBER_ATTRIBUTES];

        int trainIndex =0;
        for(int i =0; i < this.groups.size(); i++){
            if(i != indexGroupToSkip){
                for(int j = 0; j < this.groupSize; j++) {
                    trainSet[trainIndex++] = groups.get(i)[j];
                }
            }
        }

        return trainSet;
    }
}
