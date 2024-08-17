package com.example.weathersdk

import androidx.viewbinding.ViewBinding
import io.mockk.MockKGateway
import java.lang.reflect.Field
import java.lang.reflect.Modifier


fun <T : ViewBinding> T.mockFields(
    relaxed: Boolean = false,
    relaxUnitFun: Boolean = false
): T {
    this::class.java.declaredFields.forEach { field ->
        if (Modifier.isPublic(field.modifiers)) {
            val value = MockKGateway.implementation().mockFactory.mockk(
                field.type.kotlin,
                field.name,
                relaxed,
                emptyArray(),
                relaxUnitFun
            )

            mockFinalFieldForDiffJavaVersion(field, value)

            if (value is ViewBinding) value.mockFields(relaxed, relaxUnitFun)
        }
    }
    return this
}

private fun Any.mockFinalFieldForDiffJavaVersion(
    field: Field,
    value: Any
) {
    val javaVersion = System.getProperty("java.version")
    if (javaVersion != null && javaVersion.split(".")[0].toInt() > 11) {
        mockFinalFieldJdk18(field, value)
    } else {
        mockFinalField(field, value)
    }
}

private fun Any.mockFinalFieldJdk18(
    field: Field,
    value: Any
) {
    field.isAccessible = true
    Field::class.java.declaredFields.forEach {
        it.isAccessible = true
        it[field] = it.modifiers and Modifier.FINAL.inv()
    }
    field[this] = value
}

private fun Any.mockFinalField(
    field: Field,
    value: Any
) {
    field.isAccessible = true
    Field::class.java.getDeclaredField("modifiers").also {
        it.isAccessible = true
        it[field] = field.modifiers and Modifier.FINAL.inv()
    }
    field[this] = value
}
