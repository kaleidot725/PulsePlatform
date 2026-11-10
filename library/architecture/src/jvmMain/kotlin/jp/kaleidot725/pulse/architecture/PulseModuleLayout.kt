package jp.kaleidot725.pulse.architecture

/**
 * Which modules play which part in [assertPulseModuleConventions].
 *
 * Each layer is a list of path patterns matched against the end of a module's directory, with `*` standing for exactly
 * one directory: `core/data` matches `<root>/core/data`, and `feature/&#42;` matches every directory directly under
 * `feature`. The layers are tried in the order they are declared here, so a broad pattern such as `core/&#42;` in
 * [support] only takes the modules no earlier layer claimed. A module that matches nothing is not checked.
 *
 * The default follows the module names of Now in Android. A project with names of its own passes a copy:
 *
 * ```kotlin
 * val layout =
 *     PulseModuleLayout(
 *         app = listOf("desktop"),
 *         features = listOf("features/&#42;"),
 *         domain = listOf("shared/domain"),
 *         data = listOf("shared/data"),
 *         sharedUi = listOf("shared/widget"),
 *         support = listOf("shared/&#42;"),
 *         designSystem = listOf("shared/theme"),
 *     )
 *
 * Konsist.scopeFromProduction().assertPulseModuleConventions(layout)
 * ```
 *
 * An app that ships on the desktop and on mobile is two products, not one product drawn twice: a desktop window and a
 * phone screen are used differently, and a desktop app made to match its phone app is hard to use. Each product keeps
 * its own app and its own features under a directory of its own, and both share the `core` modules at the root:
 *
 * ```
 * desktop/app                 desktop/feature/<name>      the desktop product
 * mobile/app                  mobile/feature/<name>       the Android and iOS product
 * core/domain  core/data  core/designsystem  core/ui      shared by both
 * ```
 *
 * [DesktopAndMobile] names that layout. The product's directory comes first, so `desktop/feature/music` and
 * `mobile/feature/music` are two modules, and a core module only one product uses may sit under its directory as
 * `desktop/core/<name>`. The two products need not have the same features.
 */
public data class PulseModuleLayout(
    /** The application, which wires everything together. */
    public val app: List<String> = listOf("app"),
    /**
     * One feature each. A trailing `api` or `impl` directory belongs to the feature above it, so `feature/foryou/api`
     * and `feature/foryou/impl` are one feature.
     */
    public val features: List<String> = listOf("feature/*", "feature/*/api", "feature/*/impl"),
    /**
     * The entities in `model`, the repository interfaces in `repository`, and the use cases, when there are any, in
     * `usecase`.
     */
    public val domain: List<String> = listOf("core/domain"),
    /**
     * The repository implementations in `repository`, the data sources they read in `local`, and the shapes those
     * sources store in `model`. A data source only persists; the repository decides how a value changes.
     */
    public val data: List<String> = listOf("core/data"),
    /** The composables shared between features that draw the domain's entities. */
    public val sharedUi: List<String> = listOf("core/ui"),
    /** Everything else a layer may use, such as `core/common` or `core/testing`. */
    public val support: List<String> = listOf("core/*"),
    /** The theme and the building blocks every screen is drawn with, which know nothing of the domain. */
    public val designSystem: List<String> = listOf("core/designsystem"),
    /**
     * The directories that hold the desktop product: its app, its features, and any core module only it uses. A module
     * belongs to the product when one of these sits directly above the part a layer matched, as `desktop` does above
     * `feature/music` in `desktop/feature/music`. Empty for a project with one product.
     */
    public val desktop: List<String> = emptyList(),
    /** The directories that hold the Android and iOS product, read the way [desktop] is. */
    public val mobile: List<String> = emptyList(),
) {
    public companion object {
        /** The module names of Now in Android. */
        public val NowInAndroid: PulseModuleLayout = PulseModuleLayout()

        /**
         * Now in Android's names, with the desktop and mobile products under `desktop` and `mobile`: `desktop/app`,
         * `desktop/feature/<name>`, `mobile/app` and `mobile/feature/<name>`, sharing `core/<name>`.
         */
        public val DesktopAndMobile: PulseModuleLayout = PulseModuleLayout(desktop = listOf("desktop"), mobile = listOf("mobile"))
    }

    /** Whether the project is split into products, so an app or a feature outside one is misplaced. */
    internal val hasProducts: Boolean
        get() = desktop.isNotEmpty() || mobile.isNotEmpty()

    private val products: List<Pair<ModuleProduct, List<String>>>
        get() = listOf(ModuleProduct.Desktop to desktop, ModuleProduct.Mobile to mobile)

    private val layers: List<Pair<ModuleLayer, List<String>>>
        get() =
            listOf(
                ModuleLayer.App to app,
                ModuleLayer.Feature to features,
                ModuleLayer.Domain to domain,
                ModuleLayer.Data to data,
                ModuleLayer.DesignSystem to designSystem,
                ModuleLayer.SharedUi to sharedUi,
                ModuleLayer.Support to support,
            )

    internal fun layerOf(directory: String): ModuleLayer? = match(directory)?.first

    /** The product a module belongs to, or null for a module both share or a project with one product. */
    internal fun productOf(directory: String): ModuleProduct? = productMatch(directory)?.first

    /**
     * The part of [directory] a pattern matched, which names the module in reports and in [pulseModule], with the
     * product's directory in front of it when it has one.
     */
    internal fun moduleOf(directory: String): String? {
        val (layer, pattern) = match(directory) ?: return null
        val matched = directory.split('/').takeLast(pattern.split('/').size).joinToString("/")
        val name = if (layer == ModuleLayer.Feature) matched.removeSuffix("/api").removeSuffix("/impl") else matched
        return productMatch(directory)?.let { (_, product) -> "$product/$name" } ?: name
    }

    private fun match(directory: String): Pair<ModuleLayer, String>? =
        layers.firstNotNullOfOrNull { (layer, patterns) ->
            patterns.firstOrNull { directory.endsWithPattern(it) }?.let { layer to it }
        }

    /** The product whose directory sits directly above the matched part, and that directory as it was matched. */
    private fun productMatch(directory: String): Pair<ModuleProduct, String>? {
        val (_, pattern) = match(directory) ?: return null
        val above = directory.split('/').dropLast(pattern.split('/').size)
        return products.firstNotNullOfOrNull { (product, patterns) ->
            patterns
                .firstOrNull { above.joinToString("/").endsWithPattern(it) }
                ?.let { product to above.takeLast(it.split('/').size).joinToString("/") }
        }
    }
}

/** The products a project split by [PulseModuleLayout.desktop] and [PulseModuleLayout.mobile] ships. */
internal enum class ModuleProduct { Desktop, Mobile }

private fun String.endsWithPattern(pattern: String): Boolean {
    val segments = split('/')
    val wanted = pattern.split('/')
    if (wanted.size > segments.size) return false
    return segments.takeLast(wanted.size).zip(wanted).all { (segment, part) -> part == "*" || part == segment }
}
