abstract class PaletteLoader(private val paletteList: PaletteList) {
    abstract fun load()

    protected fun onPaletteLoaded(palette:Palette)
    {
        paletteList.add(palette)
    }
}