package giis.labs.model

/**
 * @author Q-YAA
 */
class Matrix(array: Array[Array[Double]]) {

    val matrixData = array

    def getValue(i: Int, j: Int): Double = matrixData(i)(j)

    def getRow(i: Int): Array[Double] = matrixData(i)

    def setValue(i: Int, j: Int, value: Double) {
        matrixData(i)(j) = value
    }

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
