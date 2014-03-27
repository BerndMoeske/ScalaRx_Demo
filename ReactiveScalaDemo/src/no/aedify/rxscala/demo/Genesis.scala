package no.aedify.rxscala.demo

import scala.concurrent.duration.{Duration, MILLISECONDS}

import rx.lang.scala.Observable

object Genesis {

  def main(args: Array[String]) {
    val biosphereX = new BioshpereX()
    biosphereX.open();
    val incubator = Observable.interval(Duration(500, MILLISECONDS)).take(40)
    incubator.subscribe(n => biosphereX.addCreature())

    println("done")
  }
}