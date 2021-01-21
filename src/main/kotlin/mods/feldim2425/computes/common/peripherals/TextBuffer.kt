package mods.feldim2425.computes.common.peripherals

import kotlin.math.max
import kotlin.math.min

class TextBuffer(nsize: Pair<Int, Int> = Pair(51, 19)) {
    var textBuffer = MutableList(nsize.first * nsize.second) { _ -> ' ' }
    var size = nsize
        set(value) {
            resize(field, value)
            field = value
        }

    private var dirty = false

    init {

    }

    private fun resize(current: Pair<Int, Int>, size: Pair<Int, Int>) {
        val newBuffer = MutableList<Char>(size.first * size.second) { index ->
            val x: Int = max(index % size.first, current.first)
            val y: Int = max(index / size.first, current.second)
            textBuffer[(y * current.second) + x]
        }
        textBuffer = newBuffer
        dirty = true
    }

    fun writeString(string: String, x: Int, y: Int) {
        if (x >= size.first || y >= size.second) {
            return
        }
        val len: Int = min(string.length, size.first - x)
        val startIndex = (y * size.first) + x
        for (i in 0 until len) {
            textBuffer[startIndex + i] = string[i]
        }
        dirty = true
    }

    fun isDirty(): Boolean {
        return dirty
    }

    fun setClear() {
        dirty = false
    }

    fun update(size: Pair<Int, Int>, textBuffer: List<Char>) {
        this.size = size
        this.textBuffer = ArrayList(textBuffer)
        dirty = true
    }
}