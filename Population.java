import java.util.ArrayList;
import java.util.Random;

public class Population {

    private int size;
    private final Tile[] tile_set;
    private Individual[] individuals;
    private final int TS_BP_CAP = 100;
    private final int TS_colors_ht_CAP = 50;
    private final int TS_size = 25;

    public Population(int size) {
        tile_set = initializeTileSet();
        this.size = size;
        individuals = new Individual[size];
        for (int i = 0; i < size; i++) {
            individuals[i] = new Individual(tile_set);
        }
    }

    public int getSize() {

        return size;

    }

    public Individual getFittest() {
        double max_fit = -100;
        int max_fit_index = 0;
        for (int i = 0; i < size; i++) {
            if (individuals[i].getFitness() > max_fit) {
                max_fit = individuals[i].getFitness();
                max_fit_index = i;
            }
        }
        return individuals[max_fit_index];
    }

    public Individual getIndividual(int index) {

        return individuals[index];

    }

    public Tile getTSTile(int ID) {
        for (int i = 0; i < tile_set.length; i++) {
            if (ID == tile_set[i].getID()) {
                return tile_set[i];
            }
        }
        return null;
    }

    public void crossover() {

        //determine fit_set
        //create "fitness wheel"
        //child1 w/ two rows from parents
        //child2 w/ two cols from parents
        //mutate

        double fit_sum = 0;
        double[] norm_vals_wheel = new double[size];
        double running_total = 0;
        Individual[] chosen = new Individual[size];
        Random random = new Random();
        int index;

        //get fitness sum
        for (int i = 0; i < size; i++) {
            fit_sum += individuals[i].getFitness();
        }

        //create array of incremental normed vals, aka 'wheel'
        for (int i = 0; i < norm_vals_wheel.length; i++) {
            norm_vals_wheel[i] =
                    individuals[i].getFitness()/fit_sum + running_total;
            running_total += individuals[i].getFitness()/fit_sum;
        }

        //create array of chosen individuals
        for (int i = 0; i < chosen.length; i++) {
            index = 0;
            double check = random.nextDouble();
            while (check > norm_vals_wheel[index]) {
                index++;
                if (index == 3) break;
            }
            chosen[i] = individuals[index];
        }

        //crossover the chosen individuals
        index = 0;
        for (int i = 0; i < chosen.length-1; i++) {
            Individual child1 = cross_helper_row(chosen[i], chosen[i+1]);
            Individual child2 = cross_helper_col(chosen[i], chosen[i+1]);
            //mutation
            mutate(child1);
            mutate(child2);
            individuals[index] = child1;
            individuals[index+1] = child2;
            index++;
        }
    }

    private void mutate(Individual child) {
        Random random = new Random();
        int swap_row1, swap_col1, swap_row2, swap_col2;
        if (random.nextDouble() >= .99) {
            int tempID;
            swap_row1 = random.nextInt(5);
            swap_col1 = random.nextInt(5);
            swap_row2 = random.nextInt(5);
            swap_col2 = random.nextInt(5);
            tempID = child.getTile(swap_row1, swap_col1).getID();
            child.setTile(swap_row1, swap_col1,
                    child.getTile(swap_row2, swap_col2).getID(),
                    this);
            child.setTile(swap_row2, swap_col2,
                    tempID, this);
        }
    }

    //crossover helper
    private Individual cross_helper_row(Individual m, Individual d) {

        Random random = new Random();
        ArrayList<int[]> holes = new ArrayList<>();
        ArrayList<Integer> IDpool = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            IDpool.add(i);
        }

        //pick two rows to give to child
        int row_m = random.nextInt(5);
        int row_d = random.nextInt(5);
        while (row_m == row_d) {
            row_d = random.nextInt(5);
        }

        //check rows for matches--store match indices in holes arraylist
        for (int i = 0; i < Math.sqrt(tile_set.length); i++) {
            for (int j = 0; j < Math.sqrt(tile_set.length); j++) {
                if (m.getTile(row_m, i).getID() == d.getTile(row_d, j).getID()) {
                    holes.add(new int[]{row_d, j});
                }
            }
        }

        //create new Individual w/ grid containing swapped tile rows.

        Individual child = new Individual();

        for(int i = 0; i < 5; i++) {
            child.setTile(row_m, i, m.getTile(row_m, i).getID(), this);
            IDpool.remove(IDpool.indexOf(m.getTile(row_m, i).getID()));
        }

        for(int i = 0; i < 5; i++) {
            boolean is_hole = false;
            for (int j = 0; j < holes.size(); j++) {
                if (holes.get(j)[1] == i) {
                    is_hole = true;
                }
            }
            if (!is_hole) {
                child.setTile(row_d, i, d.getTile(row_d, i).getID(), this);
                IDpool.remove(IDpool.indexOf(d.getTile(row_d, i).getID()));
            }
        }

        //all rows have been filled with swapped values.  now fill all other rows and holes.
        //fill holes

        for (int i = 0; i < holes.size(); i++) {
            int fill = random.nextInt(IDpool.size());
            child.setTile(holes.get(i)[0], holes.get(i)[1], IDpool.get(fill), this);
            IDpool.remove(fill);
        }

        for (int i = 0; i < 5; i++) {
            if (i != row_d && i != row_m) {
                for (int j = 0; j < 5; j++) {
                    int fill = random.nextInt(IDpool.size());
                    child.setTile(i, j, IDpool.get(fill), this);
                    IDpool.remove(fill);
                }
            }
        }
        return child;
    }

    private Individual cross_helper_col(Individual m, Individual d) {

        Random random = new Random();
        ArrayList<int[]> holes = new ArrayList<>();
        ArrayList<Integer> IDpool = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            IDpool.add(i);
        }

        //pick two cols to give to child
        int col_m = random.nextInt(5);
        int col_d = random.nextInt(5);
        while (col_m == col_d) {
            col_d = random.nextInt(5);
        }

        //check cols for matches--store match indices in holes arraylist
        for (int i = 0; i < Math.sqrt(tile_set.length); i++) {
            for (int j = 0; j < Math.sqrt(tile_set.length); j++) {
                if (m.getTile(i, col_m).getID() == d.getTile(j, col_d).getID()) {
                    holes.add(new int[]{j, col_d});
                }
            }
        }

        //create new Individual w/ grid containing swapped tile rows.

        Individual child = new Individual();

        for(int i = 0; i < 5; i++) {
            child.setTile(i, col_m, m.getTile(i, col_m).getID(), this);
            IDpool.remove(IDpool.indexOf(m.getTile(i, col_m).getID()));
        }

        for(int i = 0; i < 5; i++) {
            boolean is_hole = false;
            for (int j = 0; j < holes.size(); j++) {
                if (holes.get(j)[0] == i) {
                    is_hole = true;
                }
            }
            if (!is_hole) {
                child.setTile(i, col_d, d.getTile(i, col_d).getID(), this);
                IDpool.remove(IDpool.indexOf(d.getTile(i, col_d).getID()));
            }
        }

        //all rows have been filled with swapped values.  now fill all other rows and holes.

        for (int i = 0; i < holes.size(); i++) {
            int fill = random.nextInt(IDpool.size());
            child.setTile(holes.get(i)[0], holes.get(i)[1], IDpool.get(fill), this);
            IDpool.remove(fill);
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j != col_d && j != col_m) {
                    int fill = random.nextInt(IDpool.size());
                    child.setTile(i, j, IDpool.get(fill), this);
                    IDpool.remove(fill);
                }
            }
        }
        return child;
    }

    private Tile[] initializeTileSet() {

        Random random = new Random();
        ArrayList<Character> colors_h = new ArrayList<>(TS_colors_ht_CAP);
        ArrayList<Character> colors_t = new ArrayList<>(TS_colors_ht_CAP);
        ArrayList<Character> BP = new ArrayList<>(TS_BP_CAP);

        for (int i = 0; i < TS_colors_ht_CAP; i++) {
            if (i < 10) {
                colors_h.add('R');
                colors_t.add('R');
            } else if (i < 20) {
                colors_h.add('B');
                colors_t.add('B');
            } else if (i < 30) {
                colors_h.add('Y');
                colors_t.add('Y');
            } else if (i < 40) {
                colors_h.add('G');
                colors_t.add('G');
            } else {
                colors_h.add('O');
                colors_t.add('O');
            }
        }

        for (int i = 0; i < TS_BP_CAP; i++) {
            if (i < 50) BP.add('H');
            else BP.add('T');
        }

        Tile[] tile_set = new Tile[TS_size];
        int rand_index_BP;
        int rand_index_c;
        char[] BP_add = new char[4];
        char[] c_add = new char[4];

        for (int i = 0; i < TS_size; i++) {
            for (int k = 0; k < 4; k++) {

                rand_index_BP = random.nextInt(BP.size());
                if (BP.get(rand_index_BP) == 'H') {
                    rand_index_c = random.nextInt(colors_h.size());
                    c_add[k] = colors_h.get(rand_index_c);
                    colors_h.remove(rand_index_c);
                } else {
                    rand_index_c = random.nextInt(colors_t.size());
                    c_add[k] = colors_t.get(rand_index_c);
                    colors_t.remove(rand_index_c);
                }
                BP_add[k] = BP.get(rand_index_BP);
                BP.remove(rand_index_BP);
            }
            tile_set[i] = new Tile(BP_add, c_add);
            tile_set[i].setID(i);
        }
        return tile_set;
    }
}



