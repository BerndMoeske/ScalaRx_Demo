package no.aedify.rxscala.demo.creatures

import java.awt.Color

import scala.util.Random

trait Phenotype {
  private val phenotypeRand = new Random()
  private var size = phenotypeRand.nextInt(40) + 10;
  private val variation = phenotypeRand.nextInt(100)
  private var age = 1
  private var col = new Color(phenotypeRand.nextInt(200) + 55, phenotypeRand.nextInt(200) + 55, phenotypeRand.nextInt(200) + 55, 200)

  def getSize: Int = size
  def getVariation: Int = variation
  def getAge: Int = age
  def getCol: Color = col

  def ageBy(years: Int): Unit = {
    if (years > 0) age += years
  }

  def changeColorTo(newColor: Color): Unit = {
    col = newColor
  }
}