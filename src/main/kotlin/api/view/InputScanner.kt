package api.view

interface InputScanner {
    abstract fun getNumbers(): List<Long>
    fun getBoolean(): Boolean
}
