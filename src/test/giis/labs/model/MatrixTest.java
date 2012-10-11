package giis.labs.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 * @author Q-YAA
 */
public class MatrixTest {

    @Test
    public void testMultiplicity() {

        double[][] firstArray = {
            {2, 3},
            {0, -2},
            {-1, 4}
        };

        double[][] secondArray = {
            {1, -1, 0, 3},
            {2, 1, -2, -4}
        };

        double [][] resultArray = {
            {8, 1, -6, -6},
            {-4, -2, 4, 8},
            {7, 5, -8, -19}
        };

        Matrix firstMatrix = new Matrix(firstArray);
        Matrix secondMatrix = new Matrix(secondArray);
        Matrix resultExpected = new Matrix(resultArray);

        Matrix resultActual = firstMatrix.$times(secondMatrix);

        for (int i = 0; i < resultActual.matrixData().length; i++) {
            for (int j = 0; j < resultActual.matrixData()[i].length; j++) {
                Assert.assertThat(resultActual.matrixData()[i][j], is(resultExpected.matrixData()[i][j]));
            }
        }
    }
}
