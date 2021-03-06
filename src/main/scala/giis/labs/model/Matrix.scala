package giis.labs.model

/**
 * Class that represent two dimensional matrix.
 *
 * @author Q-YAA
 */
class Matrix(array: Array[Array[Double]]) {

    val matrixData = array

    /**
     * Returns the value that placed in i row and j column.
     *
     * @param i row number
     * @param j column number
     * @return Double value from the matrix
     */
    def getValue(i: Int, j: Int): Double = matrixData(i)(j)

    /**
     * Returns matrix row with i number.
     *
     * @param i row number
     * @return Array[Double] row from matrix
     */
    def getRow(i: Int): Array[Double] = matrixData(i)

    /**
     * Set the value in i row and j column.
     *
     * @param i row number
     * @param j column number
     * @param value value to set
     */
    def setValue(i: Int, j: Int, value: Double) {
        matrixData(i)(j) = value
    }

    /**
     * Multiplication of the two matrix.
     *
     * @param matrix second matrix to multiply
     * @return Matrix result matrix
     */
    def *(matrix: Matrix): Matrix = {

        if (matrixData(0).length != matrix.matrixData.length) {
            throw new IllegalStateException("Wrong MatrixTest to multiply!")
        }

        val result = Array.ofDim[Double](matrixData.length, matrix.matrixData(0).length)

        val r = matrixData.length
        val m = matrix.matrixData(0).length

        for (i <- 0 until matrixData.length) {

            for (j <- 0 until matrix.matrixData(0).length) {

                for (k <- 0 until matrix.matrixData.length) {
                    result(i)(j) = result(i)(j) + matrixData(i)(k) * matrix.matrixData(k)(j)
                }
            }
        }

        new Matrix(result)
    }
}
