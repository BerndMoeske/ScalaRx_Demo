package no.aedify.rxscala.demo

import scala.concurrent.duration.{ Duration, MILLISECONDS }
import scala.util.Random

import no.aedify.rxscala.demo.creatures.Creature
import rx.lang.scala.{ Observable, Subscription }

class LifeCycle(creatures: scala.collection.mutable.HashSet[Creature]) extends BiosphereDimensions {
  val myRand = new Random()
  var creatureRef: scala.collection.mutable.HashSet[Creature] = creatures

  val condition: Creature => Boolean = _.isAlive

  def distanceTo(thatCreature: Creature, mX: Int, mY: Int): Int = {
    val xDiff = thatCreature.getX - mX
    val yDiff = thatCreature.getY - mY
    math.sqrt(xDiff * xDiff + yDiff * yDiff).toInt
  }

  def distanceTo(thisCreature: Creature, thatCreature: Creature): Int = {
    return distanceTo(thatCreature, thisCreature.getX, thisCreature.getY)
  }

  def isOverlapping(thatCreature: Creature, mX: Int, mY: Int, mSize: Int): Boolean = {
    return distanceTo(thatCreature, mX, mY) <= (mSize + thatCreature.getSize) / 2
  }

  def isOverlapping(thisCreature: Creature, thatCreature: Creature): Boolean = {
    return isOverlapping(thatCreature, thisCreature.getX, thisCreature.getY, thisCreature.getSize)
  }

  def isAdjacentTo(thisCreature: Creature, thatCreature: Creature): Boolean = {
    return distanceTo(thisCreature, thatCreature) == (thisCreature.getSize + thatCreature.getSize) / 2
  }

  def isOverlappingAny(thisCreature: Creature, x: Int, y: Int, size: Int): Boolean = {
    for (thatCreature <- creatureRef) {
      if (isOverlapping(thatCreature, x, y, size) && (thatCreature ne thisCreature)) return true
    }
    return false
  }

  def areaCrowdedness(thisCreature: Creature, radius: Int): Int = {
    var count = 0
    for (thatCreature <- creatureRef) {
      if (isOverlapping(thatCreature, thisCreature.getX, thisCreature.getY, radius) && (thatCreature ne thisCreature)) count += 1
    }
    return count
  }

  def isOverlappingAny(thisCreature: Creature): Boolean = {
    return isOverlappingAny(thisCreature, thisCreature.getX, thisCreature.getY, thisCreature.getSize)
  }

  var mating = Observable.interval(Duration(1500, MILLISECONDS))
  val matingSub: Subscription = mating.subscribe(n => {
    println("Filter before: " + creatureRef.size)
    creatureRef.retain(Creature => Creature.isAlive)
    println("Filter after: " + creatureRef.size)
    for (thisCreature <- creatureRef) {
      if (thisCreature.isAlive && (areaCrowdedness(thisCreature, 100) < 5))
        for (thatCreature <- creatureRef) {
          if ((isOverlapping(thisCreature, thatCreature)) && (thatCreature ne thisCreature) && ((thatCreature.getVariation - thisCreature.getVariation) > 20)) {
            println("MATING OPPORTUNITY, distance " + distanceTo(thisCreature, thatCreature) + ". Now " + creatureRef.size + " Crowdedness: " + areaCrowdedness(thisCreature, 100))
            var offspring: Creature = new Creature()
            //offspring.x = (thisCreature.x + thatCreature.x) / 2
            //offspring.y = (thisCreature.y + thatCreature.y) / 2
            offspring.infect((thisCreature.getVirus * 2 + thatCreature.getVirus) / 3)
            creatureRef.add(offspring)
          }
        }
    }
  })

  var consuming = Observable.interval(Duration(1100, MILLISECONDS))
  val consumingSub: Subscription = consuming.subscribe(n => {
    for (thisCreature <- creatureRef) {
      if (thisCreature.isAlive)
        for (thatCreature <- creatureRef) {
          if ((distanceTo(thisCreature, thatCreature) < 10) && (thisCreature.getSize >= thatCreature.getSize) && (thatCreature ne thisCreature)) {
            creatureRef.remove(thatCreature)
            thatCreature.terminate
            println("CONSUMING OPPORTUNITY, distance " + distanceTo(thisCreature, thatCreature) + ". Now " + creatureRef.size)
            if (thatCreature.getVirus > 70 && thisCreature.getVirus > 80) {
              println("Both highly infected. Aggressor dies too.")
              creatureRef.remove(thisCreature)
              thisCreature.terminate
            }
          }
        }
    }
  })

  var stressing = Observable.interval(Duration(1200, MILLISECONDS))
  val stressingSub: Subscription = stressing.subscribe(n => {
    for (thisCreature <- creatureRef) {
      if (thisCreature.isAlive)
        if (areaCrowdedness(thisCreature, 50) > 3) {
          println("TOO MANY CREATURES IN AREA. GOODBYE. STILL LEFT: " + creatureRef.size)
          creatureRef.remove(thisCreature)
          thisCreature.terminate
        }
    }
  })
}