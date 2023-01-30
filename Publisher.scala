import java.net._
import java.io._

@SerialVersionUID(100L)
class Publisher(val name:String) extends Runnable with Serializable{
    
    def sendMessage()={
        val socket = new Socket("localhost", 6969)
        var messageString:String = scala.io.StdIn.readLine("Enter message(1st should be \"register\"): ")
        var e = new PublishEvent(messageString, this)
        var os = socket.getOutputStream();
        var oos = new ObjectOutputStream(os);

        println("Sending values to the ServerSocket");
        println("Sending : "+ messageString + " by "+ name)

        oos.writeObject(e);
        socket.close()
    }

    override def run = this.synchronized{
        sendMessage()
        // val registerSocket = new Socket("localhost", 6969)
        
        // // var messageString:String = scala.io.StdIn.readLine("Enter message: ")
        // var msg = new PublishEvent("register", this)
        // var os1 = registerSocket.getOutputStream();
        // var oos1 = new ObjectOutputStream(os1);

        // println("Sending register message to the ServerSocket");
        // // println("Sending : "+ messageString + " by "+ name)

        // oos1.writeObject(msg);
        // registerSocket.close()
        while (true) {
            sendMessage()
            // val socket = new Socket("localhost", 6969)
            // var messageString:String = scala.io.StdIn.readLine("Enter message: ")
            // var e = new PublishEvent(messageString, this)
            // var os = socket.getOutputStream();
            // var oos = new ObjectOutputStream(os);

            // println("Sending values to the ServerSocket");
            // println("Sending : "+ messageString + " by "+ name)

            // oos.writeObject(e);
            // socket.close()
        }
    }
}

object MainPublisher {
    def main(args:Array[String]) : Unit = {
        val publisherName:String = scala.io.StdIn.readLine("Enter name: ")
        var executable = new Publisher(publisherName)
        var publisherThread = new Thread(executable)

        publisherThread.start()
    }
}