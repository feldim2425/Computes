package mods.feldim2425.computes.util

enum class ClientKeyModifier(bit: Int) {
    CTRL(0),
    ALT(1),
    SHIFT(2);

    val bit = bit
    val bitmask: Int
        get() {
            return 1 shl bit
        }

    companion object {
        fun combine(mods: Collection<ClientKeyModifier>): Int {
            var combined = 0
            for (mod in mods) {
                combined = combined or mod.bitmask
            }
            return combined
        }

        fun list(combined: Int): Collection<ClientKeyModifier> {
            val list = ArrayList<ClientKeyModifier>()
            for (mod in values()) {
                if ((combined and mod.bitmask) != 0) {
                    list.add(mod)
                }
            }
            return list
        }
    }
}