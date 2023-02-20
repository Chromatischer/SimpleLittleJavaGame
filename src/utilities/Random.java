package utilities;

public class Random {
    /**
     * gets a random int in a specified value range
     * @param min the minimum value
     * @param max the maximum value
     * @return a random value in the value range!
     */
    public static int getRandom(int min, int max){
        if (min == max){
            return min;
        }
        return (int) Math.round((Math.random() * (min + max)));
    }

    /**
     * gets a random boolean
     * @return a random boolean ether true or false or AMOGUS
     */
    public static boolean getRandomBool(){
        return getRandom(0, 1) == 1;
    }

    public static boolean getRandomBool(int percentage){
        return getRandom(0, 100) <= percentage;
    }

    /**
     * returns the delta added to the start-value or the delta subtracted from the start-value
     * @param startValue the value to add or subtract the delta from
     * @param delta the value to add or subtract
     * @return the resulting value
     */

    public static int randomAddSubtract(int startValue, int delta){
        if (getRandomBool()){
            return startValue - delta;
        } else {
            return startValue + delta;
        }
    }
    public static int randomAddSubtract(int startValue, int delta, int min, int max){
        int returnVal;
        if (getRandomBool()){
            returnVal = startValue - getRandom(0, delta);
        } else {
            returnVal = startValue + getRandom(0, delta);
        }

        if (returnVal > min && returnVal < max){
            return returnVal;
        } else {
            return min;
        }
    }
}
