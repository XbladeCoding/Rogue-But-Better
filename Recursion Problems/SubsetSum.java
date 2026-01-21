public class SubsetSum {
    public static boolean checkSum(int[] numbers, int remaining_sum, int index) {
        if (remaining_sum == 0) {
            return true;
        }
        if (remaining_sum < 0) {
            return false;
        }
        if (index >= numbers.length) {
            return false;
        }
        boolean includeCurrent = checkSum(numbers, remaining_sum - numbers[index], index + 1);
        boolean exlcudeCurrent = checkSum(numbers, remaining_sum, index + 1);
        return includeCurrent || exlcudeCurrent;  
    }
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5};
        System.out.println(checkSum(array, 1000, 0) + "");
        System.out.println(checkSum(array, 6, 0) + "");
        System.out.println(checkSum(array, 1, 0) + "");
        System.out.println(checkSum(array, 15, 0) + "");
    }
}
