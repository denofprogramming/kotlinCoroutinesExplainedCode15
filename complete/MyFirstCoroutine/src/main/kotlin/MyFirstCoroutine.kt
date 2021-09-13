import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

// Is the SupervisorJob the Direct Parent of Child#1 and Child#2 ...?
fun main() = runBlocking {

    val scope = CoroutineScope(Job())

    scope.launch(SupervisorJob()) {

        //Child 1
        launch {
            logContext("child 1")
            try {
                delay(10)
                logMessage("Hello")
            } catch (e: Exception) {
                logMessage(e.localizedMessage)
            }
        }


        //Child 2
        launch {
            logContext("child 2")
            throw IndexOutOfBoundsException("oops!")
            delay(10)
            logMessage("World!")
        }

    }

    delay(1000)
    logMessage("The End.")

}

/*
//Solution #1
fun main() = runBlocking {
    val scope = CoroutineScope(Job())

    scope.launch() {

        supervisorScope {
            //Child 1
            launch {
                logContext("child 1")
                try {
                    delay(10)
                    logMessage("Hello")
                } catch (e: Exception) {
                    logMessage(e.localizedMessage)
                }
            }

            launch() {
                logContext("child 2")
                throw IndexOutOfBoundsException("oops!")
                delay(10)
                logMessage("World!")
            }
        }
    }

    delay(1000)
    logMessage("The End.")

}
*/

/*
//Solution #2
fun main() = runBlocking {

    val scope = CoroutineScope(Job())

    val sharedJob = SupervisorJob()

    scope.launch(sharedJob) {
        logContext("child 1")
        delay(10)
        logMessage("Hello")

    }

    scope.launch(sharedJob) {
        logContext("child 2")
        throw IndexOutOfBoundsException("oops!")
        delay(10)
        logMessage("World!")
    }


    delay(1000)
    logMessage("The End.")

}
*/

/*
//Solution #3
fun main() = runBlocking {

    val scope = CoroutineScope(SupervisorJob())

    scope.launch {
        logContext("child 1")
        delay(10)
        logMessage("Hello")

    }

    scope.launch {
        logContext("child 2")
        throw IndexOutOfBoundsException("oops!")
        delay(10)
        logMessage("World!")
    }


    delay(1000)
    logMessage("The End.")

}
*/


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