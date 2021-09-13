import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext


fun main() = runBlocking {

    val scope = CoroutineScope(Job())


    scope.launch {
        logContext("Child 1")
        try {
            delay(10)
            logMessage("Hello")
        } catch (e: Exception) {
            logMessage(e.localizedMessage)
        }

    }

    scope.launch {
        logContext("Child 2")
        throw IndexOutOfBoundsException("oops!")
        delay(10)
        logMessage("World!")
    }



    delay(100)
    logMessage("The End.")

}


fun logMessage(msg: String) {
    println("Running on: [${Thread.currentThread().name}] | $msg")
}


fun CoroutineScope.logContext(id: String) {
    coroutineContext.logDetails(id)
}


fun CoroutineContext.logDetails(id: String) {
    sequenceOf(
        Job,
        ContinuationInterceptor,
        CoroutineExceptionHandler,
        CoroutineName
    )
        .mapNotNull { key -> this[key] }
        .forEach { logMessage("id: $id ${it.key} = $it") }
}