package jetbrains.buildServer.agent.ansibleRunner.cmd

import junit.framework.Assert.assertEquals
import org.testng.annotations.Test

class CommandLineBuilderTest {

    private fun setupBuilder(): CommandLineBuilder {
        val builder = CommandLineBuilder()
        builder.executablePath = "myExecutable"
        builder.workingDir = "myWorkingDir"
        return builder
    }

    @Test
    fun testArgumentWithNameAndValue() {
        val builder = setupBuilder()
        builder.addArgument("arg","value")
        assertEquals(
            builder.build().arguments,
            listOf(
                "arg", "value"
            )
        )
    }

    @Test
    fun testArgumentWithNameOnly() {
        val builder = setupBuilder()
        builder.addArgument("arg")
        assertEquals(
            builder.build().arguments,
            listOf("arg")
        )
    }

    @Test
    fun testArgumentWithValueOnly() {
        val builder = setupBuilder()
        builder.addArgument(value = "value")
        assertEquals(
            builder.build().arguments,
            listOf("value")
        )
    }

}