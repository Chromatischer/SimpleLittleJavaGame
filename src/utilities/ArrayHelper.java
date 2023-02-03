package utilities;

import java.awt.*;

public class ArrayHelper {
    /**
     * copies the values from one array to another and adds the wanted amount of lines to it in the process
     * @param array the start array
     * @param size the amount of array spaces to add
     * @return the array with the new length
     */
    public static String[] addFields(String[] array, int size){
        String[] returnArr = new String[array.length + size];
        System.arraycopy(array, 0, returnArr, 0, array.length);
        return returnArr;
    }
    public static String[] combineArrays(String[] array1, String[] array2){
        String[] returnArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, returnArr, 0, array1.length);
        System.arraycopy(array2, 0, returnArr, array1.length, array2.length);
        return returnArr;
    }
    public static int lengthNonNull(Rectangle[] array){
        int count = 0;
        for (Rectangle rectangle : array) {
            if (rectangle != null) count ++;
        }
        return count;
    }
}
