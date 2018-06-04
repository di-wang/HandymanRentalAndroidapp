package gatech.edu.project6400.Model.Superclasses;

import java.util.Random;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public class Tool {

    public String abbriviatedDescription;
    public double purchasePrice;
    public double rentalPrice;
    public double depositPrice;
    public String fullDescription;
    public String toolType;
    public String ID;
    public double rentalProfit = 0;
    public double totalProfit = 0;
    public double totalCosts = 0;

    public Tool(String abbrDesc, double purchasePrice, double rentalPrice, double depositPrice, String fullDesc) {
        this.abbriviatedDescription = abbrDesc;
        this.purchasePrice = purchasePrice;
        this.rentalPrice = rentalPrice;
        this.depositPrice = depositPrice;
        this.fullDescription = fullDesc;
        Random random = new Random();
        long num = random.nextLong();
        if (num < 0) {
            num = num*-1;
        }
        ID = String.valueOf(num);
        ID = ID.substring(0,5);
    }

    public Tool(String abbrDesc) {
        this.abbriviatedDescription = abbrDesc;
    }

    public Tool(String abbrDesc, String ID) {
        this.abbriviatedDescription = abbrDesc;
        this.ID = ID;
    }


}