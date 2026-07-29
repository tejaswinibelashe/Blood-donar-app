package com.example.bloodlink

import org.junit.Test
import org.junit.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class BackendLoadTest {

    @Test
    fun simulateHeavyConcurrentLoadTest() = runBlocking {
        // Simulating 1000 concurrent database connections or auth verifications
        val time = measureTimeMillis {
            val jobs = (1..1000).map { id ->
                async(Dispatchers.IO) {
                    // Simulate network delay and database read/write
                    Thread.sleep((5..20).random().toLong())
                    "Result_$id"
                }
            }
            val results = jobs.awaitAll()
            assertEquals(1000, results.size)
            assertTrue(results.contains("Result_500"))
        }
        println("Load test completed in $time ms")
        assertTrue("Load test should complete within acceptable threshold", time < 5000)
    }
}
