package com.ali.ultimateweather.internal

import kotlinx.coroutines.*

/**
 * Created by Ali Noureddine on 12/12/2019.
 */
fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}