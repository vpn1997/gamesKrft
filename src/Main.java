import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {


    public interface ProbabilityGenerator {

        String getNextString();

    }

    public static class ProbabilityGeneratorImpl implements ProbabilityGenerator {

        private List<Integer> distributionList;
        private List<String> stringList;
        private String filePath;

        public ProbabilityGeneratorImpl(String filePath) throws FileNotFoundException {
            this.filePath = filePath;
            buildArray();

        }

        public List<Integer> getDistributionList() {
            return distributionList;
        }

        public void setDistributionList(List<Integer> distributionList) {
            this.distributionList = distributionList;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }

        public void buildArray() throws FileNotFoundException {
            try {
                Scanner sc = new Scanner(new File(getFilePath()));
                sc.useDelimiter("\n");
                List<String> strings = new ArrayList<String>();
                List<Double> probabilities = new ArrayList<Double>();
                int maxDecimalPlaces = 0;
                while (sc.hasNext()) {
                    String data = sc.next();
                    String[] values = data.split(",");
                    if (values.length == 0)
                        continue;
                    strings.add(values[0]);
                    Double d = Double.parseDouble(values[values.length - 1]);
                    d = Math.round(d * 1000.0) / 1000.0;  // Rounding off to three digits
                    String text = Double.toString(Math.abs(d));
                    int integerPlaces = text.indexOf('.');
                    int decimalPlaces = text.length() - integerPlaces - 1;
                    maxDecimalPlaces = Math.max(maxDecimalPlaces, decimalPlaces);
                    probabilities.add(d);
                }
                sc.close();
                List<Integer> finalList = new ArrayList<>();
                for (int j = 0; j < probabilities.size(); j++) {
                    Double val = probabilities.get(j);
                    for (int i = 0; i < maxDecimalPlaces; i++) {
                        val = val * 10;
                    }
                    probabilities.set(j, val);
                    for (int i = 0; i < val.intValue(); i++) {
                        finalList.add(j);
                    }
                }
                setDistributionList(finalList);
                setStringList(strings);
            } catch (Exception e) {
                System.out.println("Error " + e);
                throw e;
            }
        }

        public String getNextString() {

            Random rand = new Random();
            int randIdx = rand.nextInt(1000000);
            if (distributionList == null || stringList == null || distributionList.size() == 0)
                return "Error";

            randIdx = randIdx % distributionList.size();
            String nextString = stringList.get(distributionList.get(randIdx));
            return nextString;
        }

    }


    public static void main(String[] args) throws FileNotFoundException {
        try {
            ProbabilityGeneratorImpl impl = new ProbabilityGeneratorImpl("src/prob.csv");
            Scanner inp = new Scanner(System.in);
            Scanner myInput = new Scanner(System.in);

            System.out.print("Enter numberOfTimes: ");

            int numberOfTimes = myInput.nextInt();

            for (int i = 0; i < numberOfTimes; i++) {

                String nextStr = impl.getNextString();
                System.out.println(nextStr);

            }
        } catch (Exception e) {
            System.out.println("File not found, Please check the file Path");
        }

    }
}
