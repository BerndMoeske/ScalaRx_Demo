package no.aedify.rxscala.demo

import java.awt.{Color, Dimension, Graphics2D}
import java.awt.image.BufferedImage
import java.io.{File, IOException}

import scala.concurrent.duration.{Duration, MILLISECONDS}
import scala.swing.{Frame, Panel}
import scala.swing.event.WindowClosing
import scala.util.Random

import javax.imageio.ImageIO
import no.aedify.rxscala.demo.creatures.Creature
import rx.lang.scala.{Observable, Subscription}

class BioshpereX extends Frame with BiosphereDimensions {
  var img: BufferedImage = null;
  try {
    img = ImageIO.read(new File("resources/biohazard.png"));
  } catch {
    case e: IOException => e.printStackTrace()
  }
  background_=(new Color(220, 240, 255))
  size_=(new Dimension(xDim, yDim))
  title_=("BioshpereX")
  centerOnScreen
  var creatures: collection.mutable.HashSet[Creature] = new collection.mutable.HashSet[Creature]
  val lifeCycle = new LifeCycle(creatures)

  contents = new Panel {
    preferredSize_=(new Dimension(xDim, yDim))
    opaque = true

    override def paint(g: Graphics2D) = {
      val maxrad = if (size.width > size.height) size.height else size.width
      val myRand = new Random()

      for (creature <- creatures) {
        g.setColor(creature.getCol)
        creature.getVariation match {
          case it if 0 until 40 contains it => {
            g.fillOval(creature.getX, creature.getY, creature.getSize, creature.getSize)
            g.setColor(Color.BLACK)
            g.drawOval(creature.getX, creature.getY, creature.getSize, creature.getSize)
          }
          case it if 41 until 90 contains it => {
            g.fillRoundRect(creature.getX, creature.getY, creature.getSize, creature.getSize, 3, 3)
            g.setColor(Color.BLACK)
            g.drawRoundRect(creature.getX, creature.getY, creature.getSize, creature.getSize, 3, 3)
          }
          case _ => {
            g.drawRoundRect(creature.getX, creature.getY, creature.getSize, creature.getSize, 3, 3)
          }
        }

        if (creature.getVirus > 70) {
          val signSize: Int = (creature.getSize * .7).toInt
          val diffSize = (creature.getSize - signSize) / 2
          g.drawImage(img, creature.getX + diffSize, creature.getY + diffSize, signSize, signSize, null)
        }
      }
    }
    repaint
  }

  val repaintFrame = Observable.interval(Duration(40, MILLISECONDS))
  val repaintFrameSub: Subscription = repaintFrame.subscribe(n => {
    this.repaint
  })

  def addCreature() = {
    var creature: Creature = new Creature()
    println("Biosphere introduced creature nr: " + creatures.size)
    creatures.add(creature)
  }

  reactions += {
    case WindowClosing(e) => {
      println("Exiting 2...")
      System.exit(0)
    }
  }
}

object BiosphereX {
  def apply() {
    println("Creating Bioshpere...")
  }

  def unapply() {
    println("Destroying Biosphere...")
  }
}

