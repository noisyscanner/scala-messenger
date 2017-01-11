import java.io._
import java.net.{Socket, SocketException}

/**
  * Class which is used to send messages to the socket
  *
  * @param socket The socket to read/write data to/from
  */
class Postman(val socket: Socket) {
  // Output stream, write to it to write to the socket
  val outputStream: OutputStream = socket.getOutputStream
  val out = new BufferedWriter(new OutputStreamWriter(outputStream))

  // Input stream, reading from the socket
  val inputStream: InputStream = socket.getInputStream
  val in = new BufferedReader(new InputStreamReader(inputStream))

  var loopJob: Thread = _
  var connected = false

  /**
    * Loops over the input stream and call callback with each line
    *
    * @param callback The function to call for each line in the stream
    */
  def readLines(callback: (String) => Unit): Unit = {
    loopJob = new Thread {
      override def run(): Unit = {
        while (true) {
          if (!Thread.currentThread().isInterrupted) {
            try {
              var line: String = null
              while ({line = in.readLine(); line != null}) {
                callback(line)
              }
            } catch {
              case _: InterruptedException => Thread.currentThread().interrupt()
              case _: SocketException => println("The other guy disconnected.")
            }
          }
        }
      }
    }
    loopJob.start()
  }

  def listenForInput(in: BufferedReader): Unit = {
    var nextLine: String = ""
    while ({
      print("You: ")
      nextLine = in.readLine()
      nextLine != null
    }) {
      // Write each line from stdin to the output stream
      writeLine(nextLine)
    }
  }

  def stopReading(): Unit = if (loopJob != null) loopJob.interrupt()

  /**
    * Write to the output stream
    */
  def writeLine(line: String): Unit = {
    try {
      out write line + "\n"
      out.flush()
    } catch {
      case _: IOException => println("The other person disconnected."); connected = false
    }
  }

}
