/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package optimize;//package name

import java.awt.Graphics2D;
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

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;

public class Optimize extends BasicGame {

    Graphics2D dr;
    int mX, mY, px = 250, py = 250, zoom = 1, tx = -60, ty = -22;
    Polygon[][] hex = new Polygon[20][20];
    int[][][] grid = new int[500][500][3];
    ArrayList<Unit> units = new ArrayList<Unit>();
    long time;

    public Optimize(String s) {//program name
        super(s);
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
        for (int r = 0; r < 20; r++) {
            for (int c = 0; c < 20; c++) {
                Hexagon(r, c);
            }
        }
        spawn(5 + px, 5 + py);
    }

    public static void main(String[] args) throws SlickException {
//        Optimize panel = new Optimize();
//        JFrame f = new JFrame("");
//        f.setResizable(false);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.add(panel);
//        f.setSize(603, 704);
//        f.setVisible(true);
//        f.setLocationRelativeTo(null);
        AppGameContainer app = new AppGameContainer(new Optimize("ww"));
        app.setDisplayMode(600, 676, false);
        app.setSmoothDeltas(true);
        app.setTargetFrameRate(60);
        app.setShowFPS(false);
        app.start();
    }

    public void Hexagon(int r, int c) {
        // Polygon pp = new
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
        if (c >= s / 2 + s - 2) {
            y -= 1;
        }
        p.addPoint(s + r * s * 3, y + s * c * 3);
        p.addPoint(3 * s + r * s * 3, y + s * c * (float) 3.001);//
        p.addPoint(4 * s + r * s * 3, y + s * c * 3 + (float) (s * Math.sqrt(3.0)));
        p.addPoint(3 * s + r * s * 3, y + s * c * 3 + (float) (s * 2 * Math.sqrt(3.0)));
        p.addPoint(s + r * s * 3, y + s * c * 3 + (float) (s * 2 * Math.sqrt(3.0)));
        p.addPoint(r * s * 3, y + s * c * 3 + (float) (s * Math.sqrt(3.0)));

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

    /*  public Polygon borders(int r, int c) {
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
     }*/
//    public void paintComponent(Graphics g) {
//        dr = (Graphics2D) g;
//        dr.setFont(new Font("TimesRoman", Font.PLAIN, 20));
//
//        dr.scale(zoom, zoom);
//        dr.fillRect(0, 0, 1000, 1000);
//        dr.translate(-12, -22);
//        for (int r = 0; r < 17; r++) {
//            for (int c = 0; c < 17; c++) {
//                dr.setStroke(new BasicStroke(1));
//                dr.setColor(col(grid[r + px][c + py][0]));
//                dr.fillPolygon(hex[r][c]);
//                dr.draw(hex[r][c]);
//                dr.setColor(Color.black);
//                dr.drawPolygon(hex[r][c]);
//            }
//        }
//        //dr.fillRect(12, 43, 333, 455);
//
//        repaint();
//        requestFocus();
//        setFocusTraversalKeysEnabled(false);
//    }
    @Override
    public void init(GameContainer gc) throws SlickException {
        
    }

    public void mouseWheelMoved(int change){
        if (zoom < 3 && e.getPreciseWheelRotation() < 0) {
            zoom++;
        } else if (zoom > -5 && e.getPreciseWheelRotation() > 0) {
            zoom--;
        }
    }
            
    @Override
    public void update(GameContainer gc, int i) throws SlickException {   
        if (gc.getInput().isMouseButtonDown(0)) {
            if (System.currentTimeMillis() - time > 100) {
                if (gc.getInput().getMouseX() > mX && px > 0) {
                    mX = gc.getInput().getMouseX();
                    px--;
                } else if (gc.getInput().getMouseX() < mX && px < 500) {
                    mX = gc.getInput().getMouseX();
                    px++;
                }
                if (gc.getInput().getMouseY() > mY && py > 0) {
                    mY = gc.getInput().getMouseY();
                    py--;
                } else if (gc.getInput().getMouseY() < mY && py < 500) {
                    mY = gc.getInput().getMouseY();
                    py++;
                }
                time = System.currentTimeMillis();
            }
        }

        if (gc.getInput().isMousePressed(0)) {
            mX = gc.getInput().getMouseX();
            mY = gc.getInput().getMouseY();
        }

        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
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
    public void render(GameContainer gc, org.newdawn.slick.Graphics g) throws SlickException {
        //g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        g.scale(zoom, zoom);
        g.fillRect(0, 0, 1000, 1000);
        g.translate(tx, ty);
        for (int r = 0; r < 20; r++) {
            for (int c = 0; c < 20; c++) {
                //g.setStroke(new BasicStroke(1));

                g.setColor(col(grid[r + px][c + py][0]));
                g.fill(hex[r][c]);
                g.draw(hex[r][c]);
                g.setColor(Color.black);
                g.draw(hex[r][c]);

                if (grid[r + px][c + py][1] != 0) {
                    g.setColor(Color.red);
                    g.fillRect(hex[r][c].getCenterX() - (float) 5.5, hex[r][c].getCenterY() - 6, 12, 12);
                }
            }

        }

        if (py > 0 && (gc.getInput().isKeyPressed(Input.KEY_W) || gc.getInput().isKeyPressed(Input.KEY_UP))) {
            if (ty < -60) {
                ty += 48;
                py += 2;
            } else {
                ty -= 48;
            }
        } else if (py < 500 && (gc.getInput().isKeyPressed(Input.KEY_S) || gc.getInput().isKeyPressed(Input.KEY_DOWN))) {
            if (ty > -60) {
                ty -= 48;
                py -= 2;
            } else {
                ty += 48;
            }
        }
        if (px > 0 && (gc.getInput().isKeyPressed(Input.KEY_A) || gc.getInput().isKeyPressed(Input.KEY_LEFT))) {
            if (tx < -60) {
                tx += 48;
                px += 2;
            } else {
                tx -= 48;
            }
        } else if (px < 500 && (gc.getInput().isKeyPressed(Input.KEY_D) || gc.getInput().isKeyPressed(Input.KEY_RIGHT))) {
            if (tx > -60) {
                tx -= 48;
                px -= 2;
            } else {
                tx += 48;
            }
        }
    }
}
