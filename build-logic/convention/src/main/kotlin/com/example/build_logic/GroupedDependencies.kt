package com.example.build_logic

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import java.util.Optional

inline val DependencyHandlerScope.composeOfficialDependencies:List<Optional<Provider<MinimalExternalModuleDependency>>>
    get() {
        val dependencies = mutableListOf<Optional<Provider<MinimalExternalModuleDependency>>>()
        extensions.getByType<VersionCatalogsExtension>().named("libs").apply {
            dependencies.apply {
                val get = findLibrary("").get()
                val get1 = findLibrary("")
                add(findLibrary("androidx.compose.bom"))
                add(findLibrary("androidx.compose.runtime"))
                add(findLibrary("androidx.compose.foundation"))
                add(findLibrary("androidx.compose.foundation.layout"))

                add(findLibrary("androidx.compose.material"))
                add(findLibrary("androidx.compose.material.iconsExtended"))
                add(findLibrary("androidx.compose.material.iconsExtended"))
                add(findLibrary("androidx.compose.material3"))

                add(findLibrary("androidx.compose.ui"))
                add(findLibrary("androidx.compose.ui.tooling"))
                add(findLibrary("androidx.compose.ui.tooling.preview"))
                add(findLibrary("androidx.compose.ui.util"))
                add(findLibrary("androidx.compose.ui.test.junit4"))
                add(findLibrary("androidx.compose.ui.test.manifest"))
            }
        }
        return dependencies
    }