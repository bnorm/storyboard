package dev.bnorm.librettist.text

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal fun <T> List<Flow<T>>.concat(): Flow<T> {
    return flow {
        for (flow in this@concat) {
            emitAll(flow)
        }
    }
}

internal fun <T> Flow<T>.dedup(): Flow<T> {
    return flow {
        var initial: Any? = Any()
        collect {
            if (it != initial) {
                initial = it
                emit(it)
            }
        }
    }
}
