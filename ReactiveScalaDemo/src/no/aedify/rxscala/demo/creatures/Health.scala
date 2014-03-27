package no.aedify.rxscala.demo.creatures

import scala.util.Random

import rx.lang.scala.Observable

trait Health {
  private val healthRand = new Random()
  private var virus = healthRand.nextInt(100)
  private var alive = true

  def getVirus: Int = virus
  def isAlive: Boolean = alive

  def aliveObs = Observable(alive)

  def kill = {
    alive = false
    aliveObs.take(1)
  }

  def infect(level: Int): Unit = {
    this.virus = level
  }
}