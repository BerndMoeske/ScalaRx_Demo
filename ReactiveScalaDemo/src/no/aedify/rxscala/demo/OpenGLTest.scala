package no.aedify.rxscala.demo

import java.awt.event.{WindowAdapter, WindowEvent}

import javax.media.opengl.{GLCapabilities, GLProfile}
import javax.media.opengl.awt.GLCanvas
import javax.swing.JFrame

object OpenGLTest {

  def main(args: Array[String]) {

    // setup OpenGL Version 2
    val profile: GLProfile = GLProfile.get(GLProfile.GL2);
    val capabilities: GLCapabilities = new GLCapabilities(profile);

    // The canvas is the widget that's drawn in the JFrame
    val glcanvas: GLCanvas = new GLCanvas(capabilities);
    glcanvas.addGLEventListener(new Renderer());
    glcanvas.setSize(300, 300);

    val frame: JFrame = new JFrame("Hello World");
    frame.getContentPane().add(glcanvas);

    // shutdown the program on windows close event
    frame.addWindowListener(new WindowAdapter() {
      override def windowClosing(ev: WindowEvent) {
        System.exit(0);
      }
    });

    frame.setSize(frame.getContentPane().getPreferredSize());
    frame.setVisible(true);
  }
}