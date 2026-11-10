package jp.kaleidot725.pulse.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.core.exception.KoAssertionFailedException
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * The rules for an app shipped as two products under [PulseModuleLayout.DesktopAndMobile], run over a clock app with a
 * desktop and a mobile product sharing a domain, and over the same app with one module added that breaks each rule.
 */
class PulseProductConventionsTest {
    /**
     * Both products have a feature named `clock`, and the product's directory in front of each name keeps them two
     * modules, so one cannot pass for the other in the dependency checks.
     */
    @Test
    fun `passes on an app split into a desktop and a mobile product`() {
        assertEquals(
            setOf(
                "core/domain",
                "desktop/app",
                "desktop/core/designsystem",
                "desktop/feature/clock",
                "mobile/app",
                "mobile/feature/clock",
            ),
            conforming.files.mapNotNull { it.pulseModule(PulseModuleLayout.DesktopAndMobile) }.toSet(),
        )

        conforming.assertPulseModuleConventions(PulseModuleLayout.DesktopAndMobile)
        conforming.assertPulseProductConventions()
    }

    /**
     * The desktop's feature keeps its ViewModel under `content`, which the mobile rules forbid, so the pass above shows
     * [assertPulseProductConventions] held each feature to its own product's rules rather than to both.
     */
    @Test
    fun `would fail the desktop feature under the mobile rules`() {
        assertFailsWith<KoAssertionFailedException> { conforming.assertPulseMobileConventions() }
    }

    /** A desktop app that opens a mobile screen ties the two products together. */
    @Test
    fun `reports a product that depends on the other`() {
        assertReports("DesktopOpensMobile") { assertProductsDoNotDependOnEachOther(PulseModuleLayout.DesktopAndMobile) }
    }

    /** Shared UI that draws with the desktop's window frame cannot be used by the mobile product. */
    @Test
    fun `reports a shared module that depends on a product`() {
        assertReports("SharedUiKnowsTheDesktop") { assertSharedModulesDoNotDependOnAProduct(PulseModuleLayout.DesktopAndMobile) }
    }

    /** A feature at the root belongs to neither product, so nothing knows which view layout to hold it to. */
    @Test
    fun `reports a feature outside both products`() {
        assertReports("StrayFeature") { assertAppsAndFeaturesBelongToAProduct(PulseModuleLayout.DesktopAndMobile) }
    }

    /** A mobile feature is one screen and one ViewModel, so a second ViewModel under `content` fails the mobile rules. */
    @Test
    fun `holds mobile features to the mobile rules`() {
        assertReports("SecondTwinViewModel") { assertPulseProductConventions() }
    }

    /** A project with one product has no product to place a feature in, so a feature at the root is where it belongs. */
    @Test
    fun `leaves a project with one product alone`() {
        violating.assertAppsAndFeaturesBelongToAProduct()
        violating.assertProductsDoNotDependOnEachOther()
    }

    private fun assertReports(
        name: String,
        assertion: KoScope.() -> Unit,
    ) {
        val failure = assertFailsWith<KoAssertionFailedException> { violating.assertion() }
        val message = failure.message ?: ""
        check(message.contains(name)) { "Expected the failure to name $name, but it said: $message" }
    }

    private fun fixture(path: String) = "library/architecture/src/jvmTest/resources/fixture/modules/products/$path"

    private val conforming get() = Konsist.scopeFromDirectory(fixture("conforming"))

    private val violating get() = conforming + Konsist.scopeFromDirectory(fixture("violating"))
}
