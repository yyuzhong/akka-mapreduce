package geekie.mapred

import akka.actor.{Actor, ActorRef}

import scala.reflect.ClassTag

/**
 * Created by nlw on 08/04/15.
 * A mapper that takes an object (iterator) and forwards zero or more corresponding calculated objects to the reducer.
 *
 */
class MapperTask[A: ClassTag, B](output: ActorRef, f: A => Seq[B]) extends Actor {
  def receive = {
    case datum: A => f(datum) foreach (output ! _)
    case dataItr: Iterator[A] => dataItr flatMap f foreach (output ! _)
    case Forward(x) => output ! x
  }
}

object MapperTask {
  def apply[A: ClassTag, B](output: ActorRef)(f: A => Seq[B]) = new MapperTask(output, f)
}
