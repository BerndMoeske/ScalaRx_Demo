package no.aedify.rxscala.demo.creatures

import scala.concurrent.duration.{Duration, MILLISECONDS}
import scala.util.Random

import no.aedify.rxscala.demo.BiosphereDimensions
import rx.lang.scala.{Observable, Subscription}

trait Movement extends BiosphereDimensions {
  var movementRand = new Random()
  private var x, y, creatureSize, speed: Int = 0

  def initializeMovement(size: Int) {
    creatureSize = size
    speed = 2 * (movementRand.nextInt(25 - (size / 2)) + 5)
    x = Math.abs(movementRand.nextInt(xDim - size))
    y = Math.abs(movementRand.nextInt(yDim - size))
  }

  var movement = Observable.interval(Duration(60, MILLISECONDS))
  val movementSub: Subscription = movement.subscribe(n => {
    var newX, newY = 10000
    while (newX > (xDim - creatureSize)) newX = Math.abs(x - movementRand.nextInt(speed + 1) + speed / 2)
    while (newY > (yDim - creatureSize)) newY = Math.abs(y - movementRand.nextInt(speed + 1) + speed / 2)
    x = newX
    y = newY
  })

  def terminateMovement() {
    movementSub.unsubscribe
    movement = Observable.never
  }

  def getX: Int = x
  def getY: Int = y
}