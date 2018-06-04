package gatech.edu.project6400.Model;

import java.util.List;

import gatech.edu.project6400.Model.Superclasses.Tool;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public class ConstructionTool extends Tool {

    public ConstructionTool(String abbrDesc, double purchasePrice, double rentalPrice, double depositPrice, String fullDesc) {
        super(abbrDesc, purchasePrice, rentalPrice, depositPrice, fullDesc);
        this.toolType = "Construction Equipment";
    }
}
