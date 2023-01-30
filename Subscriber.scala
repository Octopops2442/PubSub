import java.net._
import java.io._

@SerialVersionUID(100L)
class Subscriber(val name: String,val portNumber: Int) extends Runnable with Serializable{


    // def sendMessage()={
    //     val socket = new Socket("localhost", 6969)
    //     var messageString:String = scala.io.StdIn.readLine("Enter message(1st should be \"register\"): ")
    //     var e = new PublishEvent(messageString, this)
    //     var os = socket.getOutputStream();
    //     var oos = new ObjectOutputStream(os);

    //     println("Sending values to the ServerSocket");
    //     println("Sending : "+ messageString + " by "+ name)

    //     oos.writeObject(e);
    //     socket.close()
    // }

    override def run = this.synchronized{
        val socket = new Socket("localhost",6969)
        val serverSocket = new ServerSocket(portNumber)
        // var messageString1:String = scala.io.StdIn.readLine("Enter topics interested(separated by spaces): ")
        var messageString2:String = scala.io.StdIn.readLine("Enter publisher(separated by spaces): ")
        var e = new SubscribeEvent(messageString2, this)
        // println("MADE EVENT")
        var os = socket.getOutputStream()
        var oos = new ObjectOutputStream(os)
        oos.writeObject(e)
        // println("SENT EVENT")
        socket.close()
        while (true) {
            var recieveSocket = serverSocket.accept()
            var is = recieveSocket.getInputStream()
            var ois = new ObjectInputStream(is)
            var obj1=ois.readObject().asInstanceOf[Event]
            var obj2 = obj1.asInstanceOf[MessageEvent]
            println(obj2.message)
            recieveSocket.close()
        }
    }
}

object MainSubscriber {
    def main(args: Array[String]):Unit={
        val subscriberName:String = scala.io.StdIn.readLine("Enter name: ")
        println("Enter Port number : ")
        val portNumber:Int = scala.io.StdIn.readInt()
        var executable = new Subscriber(subscriberName,portNumber)
        var subscriberThread = new Thread(executable)

        subscriberThread.start()
    }
}