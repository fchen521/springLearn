package pl.kernelpanic.dbmonster.generator;

import java.util.Random;

import pl.kernelpanic.dbmonster.DBMonsterContext;
import pl.kernelpanic.dbmonster.util.Converter;


public class BooleanGenerator
        extends BasicDataGenerator
        implements Initializable {
    private int probability = 50;


    private DBMonsterContext context = null;


    private Random random = null;


    public final void initialize(DBMonsterContext ctx) throws Exception {
        this.context = ctx;
        this.random = (Random) this.context.getProperty("pl.kernelpanic.dbmonster.RANDOM_KEY");
    }


    public final Object generate() {
        if (this.nulls != 0 && this.random.nextInt(101) <= this.nulls) {
            return null;
        }

        if (this.probability != 0 && this.random.nextInt(101) <= this.probability) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }


    public final int getProbability() {
        return this.probability;
    }


    public final void setProbability(int prob) {
        this.probability = Converter.checkNulls(prob);
    }

    public final void reset() {
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\generator\BooleanGenerator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */