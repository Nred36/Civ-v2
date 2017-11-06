/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package optimize;//package name

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Optimize extends JPanel implements KeyListener, MouseMotionListener, MouseWheelListener, MouseListener {

    Graphics2D dr;
    int mX, mY, px=-520, py=-520;
    Polygon[][] hex = new Polygon[500][500];
    int[][][] grid = new int[500][500][3];
    ArrayList<Unit> units = new ArrayList<Unit>();
    long time;

    public Optimize() {//program name
        try {
            FileReader fr = new FileReader("map.txt");
            BufferedReader br = new BufferedReader(fr); //reads map from text file
            for (int i = 0; i < 500; i++) {
                for (int x = 0; x < 500; x++) {
                    grid[i][x][0] = Integer.parseInt(br.readLine());
                }
            }
            fr.close();
            br.close();
        } catch (IOException a) {
            System.out.println("Couldn't Load");
        }
        for (int r = 0; r < 500; r++) {
            for (int c = 0; c < 500; c++) {
                Hexagon(r, c);
            }
        }
        addKeyListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        repaint();
    }

    public static void main(String[] args) {
        Optimize panel = new Optimize();
        JFrame f = new JFrame("");
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panel);
        f.setSize(595, 707);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    public void Hexagon(int r, int c) {
        Polygon p = new Polygon();
        int s = 12, y = (int) (c * 3 * Math.sqrt(3.0));
        if (r % 2 == 0) {
            y += (int) (s * Math.sqrt(3.0));
        }
        if (s / 2 > c) {
            y++;
        }
        if (c >= s - 1) {
            y -= 1;
        }
        p.addPoint(s + r * s * 3, y + s * c * 3);
        p.addPoint(3 * s + r * s * 3, y + s * c * 3);
        p.addPoint(4 * s + r * s * 3, y + s * c * 3 + (int) (s * Math.sqrt(3.0)));
        p.addPoint(3 * s + r * s * 3, y + s * c * 3 + (int) (s * 2 * Math.sqrt(3.0)));
        p.addPoint(s + r * s * 3, y + s * c * 3 + (int) (s * 2 * Math.sqrt(3.0)));
        p.addPoint(r * s * 3, y + s * c * 3 + (int) (s * Math.sqrt(3.0)));
        hex[r][c] = p;
    }

    public void spawn(int r, int c) {
        units.add(new Settler());
        grid[r][c][1] = 1;
    }

    public Color col(int i) {
        Color c;
        switch (i) {
            case (0):
                c = new Color(12, 12, 33);
                break;
            case (1):
                c = new Color(12, 112, 140);
                break;
            case (2):
                c = new Color(142, 212, 233);
                break;
            case (3):
                c = new Color(182, 192, 133);
                break;
            case (4):
                c = new Color(122, 92, 13);
                break;
            case (5):
                c = new Color(1, 72, 73);
                break;
            case (6):
                c = new Color(255, 72, 73);
                break;
            default:
                c = new Color(250, 250, 250);
                break;
        }
        return c;
    }

    public Polygon borders(int r, int c) {
        Polygon p = new Polygon();

        p.addPoint(hex[r][c - 1].xpoints[0], hex[r][c - 1].ypoints[0]);
        p.addPoint(hex[r][c - 1].xpoints[1], hex[r][c - 1].ypoints[1]);
        p.addPoint(hex[r][c - 1].xpoints[2], hex[r][c - 1].ypoints[2]);
        p.addPoint(hex[r + 1][c - 1].xpoints[1], hex[r + 1][c - 1].ypoints[1]);
        p.addPoint(hex[r + 1][c - 1].xpoints[2], hex[r + 1][c - 1].ypoints[2]);
        p.addPoint(hex[r + 1][c - 1].xpoints[3], hex[r + 1][c - 1].ypoints[3]);
        p.addPoint(hex[r + 1][c].xpoints[2], hex[r + 1][c].ypoints[2]);
        p.addPoint(hex[r + 1][c].xpoints[3], hex[r + 1][c].ypoints[3]);
        p.addPoint(hex[r + 1][c].xpoints[4], hex[r + 1][c].ypoints[4]);
        p.addPoint(hex[r][c + 1].xpoints[3], hex[r][c + 1].ypoints[3]);
        p.addPoint(hex[r][c + 1].xpoints[4], hex[r][c + 1].ypoints[4]);
        p.addPoint(hex[r][c + 1].xpoints[5], hex[r][c + 1].ypoints[5]);
        p.addPoint(hex[r - 1][c].xpoints[3], hex[r - 1][c].ypoints[3]);
        p.addPoint(hex[r - 1][c].xpoints[4], hex[r - 1][c].ypoints[4]);
        p.addPoint(hex[r - 1][c].xpoints[5], hex[r - 1][c].ypoints[5]);
        p.addPoint(hex[r - 1][c].xpoints[0], hex[r - 1][c].ypoints[0]);
        p.addPoint(hex[r - 1][c - 1].xpoints[5], hex[r - 1][c - 1].ypoints[5]);
        p.addPoint(hex[r - 1][c - 1].xpoints[0], hex[r - 1][c - 1].ypoints[0]);
        p.addPoint(hex[r - 1][c - 1].xpoints[1], hex[r - 1][c - 1].ypoints[1]);
        return p;
    }

    public void paintComponent(Graphics g) {
        dr = (Graphics2D) g;
        dr.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        
        
        
        super.paintComponents(g);
dr.translate(px, py);
        for (int r = 0; r < 500; r++) {
            for (int c = 0; c < 500; c++) {
                dr.setStroke(new BasicStroke(1));
                dr.setColor(col(grid[r][c][0]));
                dr.fillPolygon(hex[r][c]);
                dr.setColor(Color.black);
                dr.drawPolygon(hex[r][c]);
            }
        }
        //dr.fillRect(12, 43, 333, 455);

        repaint();
        requestFocus();
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (py > 0 && (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)) {
            py -= 1;
        } else if (py < 500 && (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)) {
            py++;
        }
        if (px > 0 && (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)) {
            px--;
        } else if (px < 500 && (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)) {
            px++;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            //runs if escape is pressed
            try {
                FileWriter fw = new FileWriter("save.txt");//set place to write to in "Files"
                PrintWriter pw = new PrintWriter(fw); //starts writing
                //pw.println();
                System.out.println("Saved");//it worked
                pw.close(); //stop writing
            } catch (IOException a) {
                System.out.println("ERROR");//it didnt work
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e
    ) {
        /* if (zoom < 3 && e.getPreciseWheelRotation() < 0) {
            zoom++;
        } else if (zoom > 1 && e.getPreciseWheelRotation() > 0) {
            zoom--;
        }*/
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (System.currentTimeMillis() - time > 50) {
            if (e.getX() > mX && px > 0) {
                mX = e.getX();
                px--;
            } else if (e.getX() < mX && px < 500) {
                mX = e.getX();
                px++;
            }
            if (e.getY() > mY && py > 0) {
                mY = e.getY();
                py--;
            } else if (e.getY() < mY && py < 500) {
                mY = e.getY();
                py++;
            }
            time = System.currentTimeMillis();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 2) {
            mX = e.getX();
            mY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {
    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {
    }

    @Override
    public void mouseExited(MouseEvent e
    ) {
    }

}
