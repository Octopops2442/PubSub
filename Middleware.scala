import java.net._
import java.io._
import scala.collection.mutable._

class Middleware extends Runnable {

    val eventQueue = new Queue[Event]()
    var serverSocket = new ServerSocket(6969)
    var publisherMap: Map[String, ArrayBuffer[Int]] =  Map()

    override def run() = {
        try {
            while(true){
                getMessage()
            }
        } catch {
            case e: Exception => println(e)
        }
    }

    def getMessage() = {
        try {
            // listen to the port
            println("Waiting for Publisher/Subscriber...")
            
            var socket = serverSocket.accept()
            // add to the queue
            var is = socket.getInputStream()
            var ois = new ObjectInputStream(is)
            var obj1=ois.readObject().asInstanceOf[Event]
            
            // println("Values received from Client are:-");
            // println(obj1.getMessage());
            if(obj1.isSubscriber){
                println("Got SubscribeEvent ")
                var subscribeEvent = obj1.asInstanceOf[SubscribeEvent]
                handleSubscriberEvent(subscribeEvent)
                
            }
            else {
                println(" object recieved is instance of PublishEvent")
                var publishEvent = obj1.asInstanceOf[PublishEvent]
                handlePublishEvent(publishEvent)

            }
            

            println("Closing sockets.");
            socket.close();

            // eventQueue += message
        } catch {
            case e: Exception => println(e)
        }
    }

    def handleSubscriberEvent(subscribeEvent: SubscribeEvent)={
        var publishers = subscribeEvent.message.split(" ")
        var subscriberPortNumber = subscribeEvent.subscriber.portNumber

        for( publisher <- publishers){
            registerSubscriber(subscriberPortNumber,publisher)
        }

    }

    def handlePublishEvent(publishEvent: PublishEvent)={
        var message = publishEvent.message 
        var publisherName = publishEvent.publisher.name
        if(message == "register"){
            registerPublisher(publisherName)
        }
        else {
            println("Sending : "+ message +" to :")
            for(port <- publisherMap(publisherName)){
                var clientSocket = new Socket("localhost",port)
                var messageEvent:Event = new MessageEvent(message,publisherName)
                var os = clientSocket.getOutputStream()
                var oos = new ObjectOutputStream(os)
                // println("Sending values to the Subscriber socket..")
                println("Port:"+ port)
                oos.writeObject(messageEvent)

                clientSocket.close()
            }
        }
    }

    def registerSubscriber(subscriberPortNumber: Int,publisher: String) = {
        if(publisherMap.contains(publisher))publisherMap(publisher) += subscriberPortNumber
        else {
            publisherMap(publisher) = new ArrayBuffer[Int]()
            publisherMap(publisher) += (subscriberPortNumber)
        }
    }

    def registerPublisher(publisher: String) = {
        if(publisherMap.contains(publisher) == false){
            publisherMap += ( publisher -> new ArrayBuffer[Int]())
        }
    }
}

object MainMiddleware {
    def main(args:Array[String]) = {
        // val middleName:String = scala.io.StdIn.readLine("Enter name: ")
        var executable = new Middleware()
        var middlewareThread = new Thread(executable)

        middlewareThread.start()
    }
}