import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Dataset {
    public static final int NUMBER_EXAMPLES = 435;
    public static final int NUMBER_ATTRIBUTES = 17;

    private String[][] dataset;

    private int numberExamples;
    private int numberRepublicans;
    private int numberDemocrats;


    public Dataset(String file){
        this.dataset = readData(file);
        this.numberExamples = NUMBER_EXAMPLES;

        fillMissingData();
        countClassOfExamples();
    }

    public Dataset(String[][] dataset){
        this.dataset = dataset;
        this.numberExamples = dataset.length;
        countClassOfExamples();
    }

    private void fillMissingData(){
        for(int i=0; i< this.numberExamples; i++){
            for(int j = 0; j < NUMBER_ATTRIBUTES; j++){
                if(this.dataset[i][j].equals("?")){
                    this.dataset[i][j] = getMeanValueForMissingAttribute(j, this.dataset[i][0]);
                }
            }
        }
    }

    private String getMeanValueForMissingAttribute(int attrIndex, String attrClass){
        Map<String, Integer> attrValues = new HashMap<>();

        for(int i=0; i< this.numberExamples; i++){
            if(this.dataset[i][0].equals(attrClass)) {
                if (!attrValues.containsKey(this.dataset[i][attrIndex])) {
                    attrValues.put(this.dataset[i][attrIndex], 1);
                } else {
                    int count = attrValues.get(this.dataset[i][attrIndex]) + 1;
                    attrValues.put(this.dataset[i][attrIndex], count);
                }
            }
        }

        String meanValue = "";
        int countMaxOccurance = 0;

        for (Map.Entry<String, Integer> entry : attrValues.entrySet()) {
            if(entry.getValue() > countMaxOccurance){
                meanValue = entry.getKey();
                countMaxOccurance = entry.getValue();
            }
        }

        return meanValue;
    }

    private void countClassOfExamples(){
        int positiveExamplesCount = 0, negativeExamplesCount = 0;

        for(int i=0; i< this.numberExamples; i++){
            if (this.dataset[i][0].equals("republican")) {
                positiveExamplesCount++;
            } else {
                negativeExamplesCount++;
            }
        }

        this.numberRepublicans = positiveExamplesCount;
        this.numberDemocrats = negativeExamplesCount;
    }

    private String[][] readData(String fileName){
        String[][] dataset= new String[NUMBER_EXAMPLES][NUMBER_ATTRIBUTES];
        int rowNumber = 0;

        try(final Scanner in = new Scanner(new FileReader(fileName))) {
            while (in.hasNext()) {
                String[] columns = in.next().split(",");
                dataset[rowNumber] = columns;
                rowNumber++;
            }
        } catch(FileNotFoundException e){
            System.out.println("Incorrect path to file");
        }

        return dataset;
    }

    public int getNumberExamples(){
        return this.numberExamples;
    }

    public int getNumberRepublicans(){
        return this.numberRepublicans;
    }

    public int getNumberDemocrats(){
        return this.numberDemocrats;
    }

    public String[][] getDataset(){
        return this.dataset;
    }
}