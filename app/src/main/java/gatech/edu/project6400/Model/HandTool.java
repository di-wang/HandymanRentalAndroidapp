package gatech.edu.project6400.Model;

import gatech.edu.project6400.Model.Superclasses.Tool;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public class HandTool extends Tool {

    public HandTool(String abbrDesc, double purchasePrice, double rentalPrice, double depositPrice, String fullDesc) {
        super(abbrDesc, purchasePrice, rentalPrice, depositPrice, fullDesc);
        this.toolType = "Hand Tool";
    }
}
