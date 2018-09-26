import java.util.Random;
import java.util.ArrayList;

public class Individual {

    private Tile[][] grid;
    private final int BP_CAPACITY = 100;
    private final int colors_ht_CAPACITY = 50;
    private final double max_matches = 40;
    private final int cols = 5;
    private final int rows = 5;

    public Individual(Tile[] tile_set) {
        Random random = new Random();
        ArrayList<Tile> tile_set_AL = new ArrayList<>();
        grid = new Tile[rows][cols];
        int rand_index;

        for (int i = 0; i < tile_set.length; i++) {
            tile_set_AL.add(tile_set[i]);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rand_index = random.nextInt(tile_set_AL.size());
                grid[i][j] = tile_set_AL.get(rand_index);
                tile_set_AL.remove(rand_index);
            }
        }
    }

    public Individual() {

        grid = new Tile[rows][cols];

    }

    public Tile getTile(int i, int j) {

        return grid[i][j];

    }

    public double getFitness() {

        //iterate across row by row.
        //if last column, only check bottom connection
        //if last row, only check right connections
        //if last tile in entire grid, check none

        int fit_val = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j != 4 && i != 4) {
                    if ((grid[i][j].getBottom()[0] != grid[i + 1][j].getTop()[0]) &&
                            (grid[i][j].getBottom()[1] == grid[i + 1][j].getTop()[1])) {
                        fit_val++;
                    }
                    if ((grid[i][j].getRight()[0] != grid[i][j + 1].getLeft()[0]) &&
                            (grid[i][j].getRight()[1] == grid[i][j + 1].getLeft()[1])) {
                        fit_val++;
                    }
                } else if (i != 4) {
                    if ((grid[i][j].getBottom()[0] != grid[i + 1][j].getTop()[0]) &&
                            (grid[i][j].getBottom()[1] == grid[i + 1][j].getTop()[1])) {
                        fit_val++;
                    }
                }
            }
        }
        return fit_val/max_matches;
    }

    public void setTile (int rows, int cols, int ID, Population p) {

        grid[rows][cols] = p.getTSTile(ID);

    }

    public void printIDs() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%d ", grid[i][j].getID());
            }
            System.out.println();
        }
    }

    public void printGrid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(" ------ ");
            }
            System.out.println();
            for (int j = 0; j < 5; j++) {
                System.out.printf("|  %c%c  |", grid[i][j].getTop()[0], 
                                  grid[i][j].getTop()[1]);
            }
            System.out.println();
            for (int j = 0; j < 5; j++) {
                if (grid[i][j].getID() > 9) {
                    System.out.printf("|%c %d %c|", grid[i][j].getLeft()[0],
                            grid[i][j].getID(), grid[i][j].getRight()[0]);
                } else {
                    System.out.printf("|%c %d  %c|", grid[i][j].getLeft()[0],
                            grid[i][j].getID(), grid[i][j].getRight()[0]);
                }
            }
            System.out.println();
            for (int j = 0; j < 5; j++) {
                System.out.printf("|%c    %c|", grid[i][j].getLeft()[1], 
                                  grid[i][j].getRight()[1]);
            }
            System.out.println();
            for (int j = 0; j < 5; j++) {
                System.out.printf("|  %c%c  |", grid[i][j].getBottom()[0], 
                                  grid[i][j].getBottom()[1]);
            }
            System.out.println();
            for (int j = 0; j < 5; j++) {
                System.out.printf(" ------ ");
            }
            System.out.println();
        }
        System.out.println();
    }
}



