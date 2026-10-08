package com.prayagi.netraassistant

import org.junit.Assert.assertTrue
import org.junit.Test

class VersionTest {
    @Test
    fun versionIsBeta() {
        assertTrue(BuildConfigInfo.VERSION.contains("beta"))
    }
}
