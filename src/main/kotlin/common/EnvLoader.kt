package common

import annotations.EnvVar
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties

object EnvLoader {
    private val logger = KotlinLogging.logger { }

    /**
     * Load all propeties value in class
     * When value is got from System.getenv(), it will be converted by {@link convertedValue}
     *
     * @return generic class instance
     *
     * @throws IllegalAccessError       if property cannot be accessible (immutable)
     * @throws IllegalArgumentException if property value just contains whitespace or empty
     * */
    fun <T : Any> load(clazz: KClass<T>): Result<T, Throwable> =
        runCatching {
            val instance = clazz.createInstance()
            val properties = clazz.declaredMemberProperties
            properties.forEach {
                it
                    .takeIf {
                        it.annotations.any { ann -> ann is EnvVar }
                    }?.let {
                        it as KMutableProperty<*>
                    }?.let { mutableProp ->
                        val envVar = mutableProp.annotations.filterIsInstance(EnvVar::class.java).first()
                        val envValue = System.getenv(envVar.name.uppercase())
                        if (envValue == null || envValue.trim().isBlank()) {
                            throw IllegalArgumentException(
                                "Environment variable ${envVar.name} for ${mutableProp.name} is undefined or empty",
                            )
                        }
                        val convertedValue =
                            convertedValue(envValue, mutableProp.returnType.classifier).getOrElse {
                                return@load Err(it)
                            }
                        mutableProp.setter.call(instance, convertedValue)
                    }
            }
            return Ok(instance)
        }.getOrElse { return Err(it) }

    /**
     * Converted from String (System.getenv output) to @param type
     *
     * @throws IllegalArgumentException if needed type is not supported
     * */
    fun convertedValue(
        value: String,
        type: Any?,
    ): Result<Any, IllegalArgumentException> =
        when (type) {
            String::class -> Ok(value)
            Boolean::class -> Ok(value.toBoolean())
            Int::class -> Ok(value.toInt())
            else -> Err(IllegalArgumentException("Unsupported type: $type"))
        }
}
