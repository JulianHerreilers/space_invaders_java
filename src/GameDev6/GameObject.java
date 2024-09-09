package GameDev6;

import edu.princeton.cs.introcs.StdDraw;


public abstract class GameObject { //Abstract makes it like act like a template for other Classes - Subtyping/Inheritance
    protected int x, y;
    protected ID id; //Used to make person reading code understand what is being added
    protected int velX, velY;
    protected int hp; //Used when checking a bullet or critter should be removed
    boolean isDead = false;

    //Constructor of abstract GameObject class that is inherited by the other Game-Related Classes in ObjectManager - this refers to this specific instance of the GameObject
    public GameObject(int x, int y, int VelX, int VelY, ID id, int hp) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.velX = VelX;
        this.velY = VelY;
        this.hp = hp;
    }

    //Used when checking an Game Object should be removed from the corresponding Linked List
    public boolean isDead(){
        return isDead;
    }


    //This is simply the function that is called when a Game-Related Class is to be displayed, each which has inherited and modified what their display call will do.
    public abstract void display();

    //All these functions are called through various classes to update their coresponding variables (for each specific Game-Related Object) and determine what is happening in the game

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void setVelX(int velX) {
        this.x = x;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelY(int velY) {
        this.x = x;
    }

    public int getVelY() {
        return velX;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }


    //Rotation Angle of the Player relative to the mouse cursor
    public double getRotationAngle() {
        double mX = StdDraw.mouseX(); //See External Sources entry 5
        double mY = StdDraw.mouseY(); //See External Sources entry 5
        int tempX = x;
        int tempY =  y;
        double angle = Math.atan2(mY - tempY, mX - tempX); //See External Source entry 1
        if(angle<-Math.PI/2){
            angle = Math.PI;
        }
        else if(angle<0 && angle>-Math.PI*3/2){
            angle = 0;
        }
        return angle;
    }

}