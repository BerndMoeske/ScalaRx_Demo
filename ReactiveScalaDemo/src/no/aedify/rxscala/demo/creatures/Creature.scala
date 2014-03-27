package no.aedify.rxscala.demo.creatures

import java.awt.Color

import scala.concurrent.duration.{Duration, MILLISECONDS}
import scala.util.Random

import rx.lang.scala.{Observable, Subscription}

class Creature() extends Phenotype with Health with Movement {
  private val myRand = new Random()
  initializeMovement(getSize)

  def terminate() {
    kill
    terminateMovement
    ageingSub.unsubscribe
    ageing = Observable.never
  }

  var ageing = Observable.interval(Duration(600, MILLISECONDS))
  val ageingSub: Subscription = ageing.subscribe(n => {
    if (isAlive) {
      ageBy(1)
      if (getAge > 70) {
        changeColorTo(Color.RED)
      }
      val generalRiskofDying = myRand.nextInt(50)
      val riskOfDyingRelativeToAge = generalRiskofDying + getAge
      val totalRiskOfDying = riskOfDyingRelativeToAge + getVirus / 2
      if (totalRiskOfDying > 150) {
        terminate
        println("Someone just died. Total = " + totalRiskOfDying + ", Chance = " + generalRiskofDying + ", age = " + getAge + ", viral infection = " + getVirus)
      }
    }
  })
}