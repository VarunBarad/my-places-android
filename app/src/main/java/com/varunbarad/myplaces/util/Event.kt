package com.varunbarad.myplaces.util

/**
 * Value type to signify a stream event/notification that we are not interested in.
 * i.e. Can be used as a return type for Observable<Object>.
 * We can use Kotlin's Unit but this gives better compatibility for any future Java code
 */
enum class Event {
    IGNORE
}
