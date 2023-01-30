// import java.io.{Serializable}

@SerialVersionUID(100L)
class PublishEvent(val message: String, val publisher: Publisher) extends Serializable with Event(message,false) {
    // override def speak() = println("Woof")
    // def comeToMaster() = println("Here I come!")
}