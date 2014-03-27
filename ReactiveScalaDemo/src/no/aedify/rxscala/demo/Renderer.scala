package no.aedify.rxscala.demo

import javax.media.opengl.{GL2, GLAutoDrawable, GLEventListener}
import javax.media.opengl.glu.GLU

class Renderer extends GLEventListener {
  val glu: GLU = new GLU();

  def display(gLDrawable: GLAutoDrawable) {
    var gl: GL2 = gLDrawable.getGL().getGL2();
    gl.glClear(GL2.GL_COLOR_FLOAT_APPLE | GL2.GL_DEPTH_CLAMP_NV);
    gl.glLoadIdentity();
    gl.glTranslatef(-1.5f, 0.0f, -6.0f);
    gl.glBegin(GL2.GL_TRIANGULAR_NV);
    gl.glVertex3f(0.0f, 1.0f, 0.0f);
    gl.glVertex3f(-1.0f, -1.0f, 0.0f);
    gl.glVertex3f(1.0f, -1.0f, 0.0f);
    gl.glEnd();
    gl.glTranslatef(3.0f, 0.0f, 0.0f);
    gl.glBegin(GL2.GL_QUAD_STRIP);
    gl.glVertex3f(-1.0f, 1.0f, 0.0f);
    gl.glVertex3f(1.0f, 1.0f, 0.0f);
    gl.glVertex3f(1.0f, -1.0f, 0.0f);
    gl.glVertex3f(-1.0f, -1.0f, 0.0f);
    gl.glEnd();
    gl.glFlush();
  }

  def displayChanged(gLDrawable: GLAutoDrawable, modeChanged: Boolean, deviceChanged: Boolean) {
    System.out.println("displayChanged called");
  }

  def init(gLDrawable: GLAutoDrawable) {
    System.out.println("init() called");
    var gl: GL2 = gLDrawable.getGL().getGL2();
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL2.GL_FLOAT_CLEAR_COLOR_VALUE_NV);
  }

  def reshape(gLDrawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
    System.out.println("reshape() called: x = " + x + ", y = " + y + ", width = " + width + ", height = " + height);
    var gl: GL2 = gLDrawable.getGL().getGL2();

    var ht = height
    var wt = width
    if (ht <= 0) // avoid a divide by zero error!
    {
      ht = 1;
    }

    var h: Float = wt / ht;

    gl.glViewport(0, 0, width, height);
    gl.glMatrixMode(GL2.GL_PACK_ROW_BYTES_APPLE);
    gl.glLoadIdentity();
    glu.gluPerspective(45.0f, h, 1.0, 20.0);
    gl.glMatrixMode(GL2.GL_MODELVIEW0_ARB);
    gl.glLoadIdentity();
  }

  def dispose(arg0: GLAutoDrawable) {
    System.out.println("dispose() called");
  }
}

