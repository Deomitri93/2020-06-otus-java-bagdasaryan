import java.util.ArrayList;
import java.util.List;

public class TestedClass {
    private final int LIST_OF_INTEGERS_SIZE = 15;
    private final int MATRIX_ROWS_COUNT = 5;
    private final int MATRIX_COLUMNS_COUNT = 4;
    private List<Integer> listOfIntegers = null;
    private float[][] matrix = null;
    private final int MIN_INTEGER_VALUES = -10;
    private final int MAX_INTEGER_VALUES = 10;
    private final float MIN_FLOAT_VALUES = -10.0f;
    private final float MAX_FLOAT_VALUES = 10.0f;

    @Before
    public void initListIntegers() {
        System.out.println("method \"initListOfIntegers()\" executed");
        listOfIntegers = new ArrayList<>();
        for (int i = 0; i < LIST_OF_INTEGERS_SIZE; i++) {
            listOfIntegers.add((int) ((Math.random() - 0.5) * (MAX_INTEGER_VALUES - MIN_INTEGER_VALUES)));
        }
    }

    @Before
    public void initMatrix() {
        System.out.println("method \"initMatrix()\" executed");
        matrix = new float[MATRIX_ROWS_COUNT][MATRIX_COLUMNS_COUNT];
        for (int i = 0; i < MATRIX_ROWS_COUNT; i++) {
            for (int j = 0; j < MATRIX_COLUMNS_COUNT; j++) {
                matrix[i][j] = (float) ((Math.random() - 0.5) * (MAX_FLOAT_VALUES - MIN_FLOAT_VALUES));
            }
        }
    }

    @Test
    private void testListOfIntegersAddingRandomInteger() {
        System.out.println("method \"testListOfIntegersAddingRandomInteger()\" executed");
        Integer addedValue = (int) ((Math.random() - 0.5) * (MAX_INTEGER_VALUES - MIN_INTEGER_VALUES));
        listOfIntegers.add(addedValue);
    }

    @Test
    public void testListOfIntegersAddingRandomIntegerToSpecifiedPosition() {
        System.out.println("method \"testListOfIntegersAddingRandomIntegerToSpecifiedPosition()\" executed");
        Integer addedValue = (int) ((Math.random() - 0.5) * (MAX_INTEGER_VALUES - MIN_INTEGER_VALUES));

        listOfIntegers.add(50, addedValue);
    }

    @Test
    public void testMatrixTransposing() {
        System.out.println("method \"testMatrixTransposing()\" executed");
        float[][] matrixTransposed = new float[matrix[0].length][matrix.length];

        for (int i = 0; i < MATRIX_ROWS_COUNT; i++) {
            for (int j = 0; j < MATRIX_COLUMNS_COUNT; j++) {
                matrixTransposed[j][i] = matrix[i][j];
            }
        }

        matrix = matrixTransposed;
    }

    @After
    public void nullAll() {
        System.out.println("method \"nullAll()\" executed");
        System.out.println();
        listOfIntegers = null;
        matrix = null;
    }
}