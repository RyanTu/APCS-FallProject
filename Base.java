// Base attributes for plants/zombies?
public class Base {

    private int Health, AtkPwr;
    private String Name;

    public Base(String n) {
	setName(n);
    }

    public String toString() {
	return this.getName();
    }

    public void attack(Base other) {
	/* Idea: 
	timer (
	other.Health -= this.AtkPwr;
	*/
    }

    public void setName(String n) {
	this.n = n;
    }

    public String getName() {
	return this.name;
    }

}
