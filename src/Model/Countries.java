package Model;

public class Countries {
    private final int id;
    private final String name;

    public Countries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}

    public String getName() {return name;}
}
