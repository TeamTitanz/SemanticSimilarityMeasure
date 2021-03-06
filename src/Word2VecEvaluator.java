import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Nisansa on 5/29/2017.
 */
public class Word2VecEvaluator {

    public static void main(String[] args) {
        Word2VecEvaluator w2ve=new Word2VecEvaluator();
        w2ve.run();
    }

    private void run(){
        String[] fileNames = {"Word2VecResult[G].csv" , "Word2VecResult[LR].csv", "Word2VecResult[LL].csv"};
        StringBuilder sb=new StringBuilder();
        for (int i = 0 ; i <3 ; i++) {
            //Loop for G, LR and LL files one by one {  //Keet must update this DONE
            String filename = "CSV/Word2VecEvaluator/"+fileNames[i]; //G, LR and LL files //Keet must update this DONE
            CSV csvObject = new CSV(); //Load this //Keet must update this DONE
            int numOfInptLines = Constants.TERM_COUNT;  //Keet must update this

            System.out.println("For File = " + filename);
            double error = 0;
            double recall = 0;

            int[] K = {20, 50, 100, 200, 500};
            for (int k : K) {

                for (int p = 0; p < numOfInptLines; p++) {
                    AllWordsCalculator allWordsCalculator = new AllWordsCalculator(filename);
                    String[] allWords = allWordsCalculator.getOutputWordList(p); //This is the 0th row of the rMatrix of p th input line //Keet must update this
                    String[] validWords = createPrunedArray(allWords, k);

                    PrReMath prMath = PrReMath.getInstance();
                    double err = prMath.calculateError(csvObject, validWords, p);
                    double rec = prMath.calculateRecall(csvObject, validWords, p);
                    error += err;
                    recall += rec;
                }
                error /= numOfInptLines;
                recall /= numOfInptLines;

                System.out.println("k = " + k);
               // System.out.printf("Error = %.2f\n", error);
                double f1=(2*(1-error) * recall / ((1-error) + recall));

                error=new BigDecimal(error).setScale(2, RoundingMode.HALF_UP).doubleValue();
                recall=new BigDecimal(recall).setScale(2, RoundingMode.HALF_UP).doubleValue();
                f1=new BigDecimal(f1).setScale(2, RoundingMode.HALF_UP).doubleValue();
                double p=1-error;
                sb.append(p);
                sb.append(" & ");
                sb.append(recall);
                sb.append(" & ");
                sb.append(f1);
                sb.append(" & ");


                System.out.printf("Precision = %.2f\n", p);

                System.out.printf("Recall = %.2f\n", recall);
                System.out.printf("F1 = %.2f\n", f1);
                System.out.println("..............");
            }
            sb.append("\n");
            System.out.println("----------------Next File-----------------");
        }
        System.out.println(sb.toString());
        //}  //uncomment //Keet must update this
    }

    private String[] createPrunedArray(String[] original,int k){
        String[] newArray=new String[k];
        for (int i = 0; i <k ; i++) {
            if(i<original.length){
                newArray[i]=original[i];
            }
            else{
                newArray[i]=""; //Just in case
            }
        }


        return newArray;
    }
}
