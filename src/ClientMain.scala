import java.io.{BufferedReader, InputStreamReader}

object ClientMain {
  def main(args: Array[String]): Unit = {
    // Create client
    val client = new Client()

    // Get standard input
    val stdIn = new BufferedReader(new InputStreamReader(System.in))

    // Read host and port
    print(s"Enter hostname [${client.host}]: ")
    val host = stdIn.readLine()
    if (host != "") client.host = host

    print(s"Enter port [${client.port}]: ")
    val port = stdIn.readLine()
    if (port != "") client.port = port.toInt

    while (!client.connected) {
      if (!client.connect((postman) => {
        // This runs an infinite loop waiting to receive a message,
        // and calls the callback for each line
        postman.readLines((line) => println("\nThem: " + line))
        postman.listenForInput(stdIn)
      })) {
        // Couldn't connect, server not running
        // Wait 1 second before trying to connect again
        Thread.sleep(1000)
      } else {
        // When the program is quit, close the socket
        Runtime.getRuntime.addShutdownHook(new Thread {
          override def run(): Unit = client.disconnect()
        })
      }
    }
  }
}
