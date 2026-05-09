package ui

object InputHandler {
    /**
     * safely reads a command from console
     * and returns Command.Help if input is invalid
     */
        fun readCommand(): Command {
            println()
            print("\n> ")
            System.out.flush()
            val input = readLine()?.trim()

            if (input.isNullOrEmpty()) {
                return Command.Help
            }

            return try {
                Command.parse(input)
            } catch (e: NumberFormatException) {
                Display.printMessage("Error: expected a number.")
                Command.Help
            } catch (e: Exception) {
                Display.printMessage("Error: ${e.message}")
                Command.Help
            }
        }
    }