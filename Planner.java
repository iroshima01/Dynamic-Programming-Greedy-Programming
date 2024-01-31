import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Planner {

    public final Task[] taskArray;
    public final Integer[] compatibility;
    public final Double[] maxWeight;
    public final ArrayList<Task> planDynamic;
    public final ArrayList<Task> planGreedy;
    int f;

    public Planner(Task[] taskArray) {
        // Should be instantiated with an Task array
        // All the properties of this class should be initialized here

        this.taskArray = taskArray;
        this.compatibility = new Integer[taskArray.length];
        maxWeight = new Double[taskArray.length];

        this.planDynamic = new ArrayList<>();
        this.planGreedy = new ArrayList<>();

        calculateCompatibility();

        int i = taskArray.length - 1;
        System.out.println("Calculating max array");
        System.out.println("---------------------");

        calculateMaxWeight(i);
        System.out.println();

        System.out.println("Calculating the dynamic solution");
        System.out.println("--------------------------------");

        solveDynamic(taskArray.length - 1);


    }

    /**
     * @param index of the {@link Task}
     * @return Returns the index of the last compatible {@link Task},
     * returns -1 if there are no compatible {@link Task}s.
     */
    public int binarySearch(int index) {
        // YOUR CODE HERE
        int low = 0;
        int high = index - 1;
        int result = -1;

        do {
            int mid = (high + low) / 2;

            LocalTime finishTime = LocalTime.parse(taskArray[mid].getFinishTime());
            LocalTime startTime = LocalTime.parse(taskArray[index].getStartTime());

            if (!finishTime.isAfter(startTime)){
                result = mid;
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        } while (high >= low);

        return result;

    }
    /**
     * {@link #compatibility} must be filled after calling this method
     */
    public void calculateCompatibility() {
        // YOUR CODE HERE
        compatibility[0] = -1;

        for (int i = 1; i < taskArray.length; i++) {
            int compatibleIndex = binarySearch(i);
            compatibility[i] = compatibleIndex;

        }
    }
    /**
     * Uses {@link #taskArray} property
     * This function is for generating a plan using the dynamic programming approach.
     * @return Returns a list of planned tasks.
     */
    public ArrayList<Task> planDynamic() {
        // YOUR CODE HERE

        int size = planDynamic.size();
        for (int j = 0; j < size / 2; j++) {
            Task temp = planDynamic.get(j);
            planDynamic.set(j, planDynamic.get(size - j - 1));
            planDynamic.set(size - j - 1, temp);
        }


        System.out.println();

        System.out.println("Dynamic Schedule");
        System.out.println("----------------");

        for(int a = 0; a < planDynamic.size(); a++ ){
            System.out.println("At " + planDynamic.get(a).getStartTime() + ", " + planDynamic.get(a).getName() + ".");
        }

        return planDynamic;
    }

    /**
     * {@link #planDynamic} must be filled after calling this method
     */
    public void solveDynamic(int i) {
        // YOUR CODE HERE
        f = 1;

        if (i < 0) {
            return;
        }
        System.out.println("Called solveDynamic(" + i + ")");

        if (maxWeight[i] == null) {
            calculateMaxWeight(i);
        }

        if (maxWeight[i] - taskArray[i].getWeight() == calculateMaxWeight(compatibility[i])) {
            planDynamic.add(taskArray[i]);
            solveDynamic(compatibility[i]);
        } else {
            solveDynamic(i-1);
        }
    }

    /**
     * {@link #maxWeight} must be filled after calling this method
     */
    /* This function calculates maximum weights and prints out whether it has been called before or not  */
    public Double calculateMaxWeight(int i) {
        // YOUR CODE HERE
        int count = 0;
        for (int a = 0; a < taskArray.length; a++){
            if (maxWeight[a] == null){
                count++;
            }
        }

       if (count == taskArray.length){
            maxWeight[0] = taskArray[0].getWeight();
            for (int a = 0; a < taskArray.length; a++){
                maxWeight[a] = -1.0;
            }
        }

       if (i < 0){
           if (f != 1) {
               System.out.println("Called calculateMaxWeight(" + i + ")");
           }
           return 0.0;
        }
        if (f != 1) {
            System.out.println("Called calculateMaxWeight(" + i + ")");
        }

        if ( i != 0 && maxWeight[i] != -1.0){
            return maxWeight[i];
        }

        double task1 = taskArray[i].getWeight() + calculateMaxWeight(compatibility[i]);

        double task2 = calculateMaxWeight(i - 1);


        if (task1 > task2){

            maxWeight[i] = task1;
        }
        else {

            maxWeight[i] = task2;

        }
        return maxWeight[i];

    }

    /**
     * {@link #planGreedy} must be filled after calling this method
     * Uses {@link #taskArray} property
     *
     * @return Returns a list of scheduled assignments
     */

    /*
     * This function is for generating a plan using the greedy approach.
     * */
    public ArrayList<Task> planGreedy() {
        // YOUR CODE HERE

        System.out.println("Greedy Schedule");
        System.out.println("---------------");

        Arrays.sort(taskArray, Comparator.comparing(Task::getFinishTime));
        LocalTime firstElStartTime = LocalTime.parse(taskArray[0].getStartTime());

        for(int i = 0; i < taskArray.length; i++){
            LocalTime start = LocalTime.parse(taskArray[i].getStartTime());
            if (start.compareTo(firstElStartTime) >= 0){
                planGreedy.add(taskArray[i]);
                firstElStartTime = LocalTime.parse(taskArray[i].getFinishTime());
            }
        }
        for (Task task : planGreedy) {
            System.out.println("At " + task.getStartTime() + ", " + task.getName() + ".");
        }

        return planGreedy;
    }
}

