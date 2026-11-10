package jp.kaleidot725.pulse.architecture

import com.lemonappdev.konsist.api.container.KoScope

/**
 * Holds each product's features to the view layout of the artifact it is built on: the desktop's features to
 * [assertPulseMviConventions], and the mobile ones to [assertPulseMobileConventions].
 *
 * Which rules a feature answers to is read from where it sits under [layout], so a project does not pick the
 * directories itself, and a feature cannot fall between the two. Run [assertPulseModuleConventions] with the same layout
 * alongside it, which reports an app or a feature outside both products.
 *
 * ```kotlin
 * class PulseProductConventionTest {
 *     @Test
 *     fun `follows the PulsePlatform conventions of each product`() {
 *         val scope = Konsist.scopeFromProduction()
 *         scope.assertPulseModuleConventions(PulseModuleLayout.DesktopAndMobile)
 *         scope.assertPulseProductConventions()
 *     }
 * }
 * ```
 */
public fun KoScope.assertPulseProductConventions(layout: PulseModuleLayout = PulseModuleLayout.DesktopAndMobile) {
    featuresOf(ModuleProduct.Desktop, layout).assertPulseMviConventions()
    featuresOf(ModuleProduct.Mobile, layout).assertPulseMobileConventions()
}

private fun KoScope.featuresOf(
    product: ModuleProduct,
    layout: PulseModuleLayout,
): KoScope = slice { it.layerIn(layout) == ModuleLayer.Feature && it.productIn(layout) == product }
