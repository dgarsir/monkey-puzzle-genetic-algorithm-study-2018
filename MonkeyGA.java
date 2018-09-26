import java.util.Scanner;

public class MonkeyGA {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Please enter a positive integer for population size: ");
        int size = input.nextInt();
        System.out.print("Please enter a positive integer for number of generations: ");
        int gens = input.nextInt();
        System.out.println();

        Population test = new Population(size);
        int best_gen = 0;
        double best_gen_max = 0;

        int count = 0;
        while (count < gens) {

            if (test.getFittest().getFitness() > best_gen_max) {
                System.out.printf("Current max_fitness = %f\nGeneration = %d\n",
                        test.getFittest().getFitness(), count);
                best_gen_max = test.getFittest().getFitness();
                best_gen = count;
                test.getFittest().printGrid();
            }

            test.crossover();

            count++;
        }

        System.out.printf("Maximum fitness achieved: %f\nGeneration: %d", best_gen_max, best_gen);

    }
}
