package gatech.edu.project6400.Model;

import java.util.List;

import gatech.edu.project6400.Model.Superclasses.Tool;

/**
 * Created by Gene Hynson on 4/4/2016.
 */
public class PowerTool extends Tool {
    public List<String> accessories;

    public PowerTool(String abbrDesc, double purchasePrice, double rentalPrice, double depositPrice, String fullDesc, List<String> accessories) {
        super(abbrDesc, purchasePrice, rentalPrice, depositPrice, fullDesc);
        this.accessories = accessories;
        this.toolType = "Power Tool";
    }
}
