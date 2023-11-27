package sldevs.cdo.orokalimpyocollector.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class other_functions {

    List<String> waste_type = new ArrayList<String>();

    List<String> consolidator_waste_type = new ArrayList<String>();
    List<String> consolidator_plastic_type = new ArrayList<String>();

    List<String> pet_list = new ArrayList<String>();
    List<String> hpde_list = new ArrayList<String>();
    List<String> pvc_list = new ArrayList<String>();
    List<String> ldpe_list = new ArrayList<String>();
    List<String> pp_list = new ArrayList<String>();
    List<String> ps_list = new ArrayList<String>();
    List<String> other_list = new ArrayList<String>();

    List<String> brand_list = new ArrayList<String>();

    List<String> sort_list_segregation = new ArrayList<String>();


    public List<String> sortSegregationList(){
        sort_list_segregation.add("Waste Type");
        sort_list_segregation.add("Plastic Type");
        sort_list_segregation.add("Plastic Name");
        sort_list_segregation.add("Date and Time");
        sort_list_segregation.add("Brand Waste");
        return sort_list_segregation;
    }
    public List<String> populateWasteType(){
        waste_type.add("Recyclable");
        waste_type.add("Biodegradable");
        waste_type.add("Residual");
        waste_type.add("Special Waste");
        waste_type.add("Non-compliant");
        return waste_type;
    }


    public List<String> populateConsolidatorWasteType(){
        consolidator_waste_type.add("Plastic Waste");
        consolidator_waste_type.add("Metal");
        consolidator_waste_type.add("Bottle");
        consolidator_waste_type.add("Paper");
        consolidator_waste_type.add("Other...");
        return consolidator_waste_type;

    }

    public List<String> populateConsolidatorPlasticType(){
        consolidator_plastic_type.add("PET (Polyethylene Terephthalate)");
        consolidator_plastic_type.add("HDPE (High-Density Polyethylene)");
        consolidator_plastic_type.add("PVC (Polyvinyl Chloride)");
        consolidator_plastic_type.add("LDPE (Low-Density Polyethylene)");
        consolidator_plastic_type.add("PP (Polypropylene)");
        consolidator_plastic_type.add("PS (Polystyrene)");
        consolidator_plastic_type.add("Other...");
        return consolidator_plastic_type;
    }

    public List<String> populatePETList(){
        pet_list.add("Water Bottles");
        pet_list.add("Jars");
        pet_list.add("Caps");
        pet_list.add("Other...");
        return pet_list;
    }

    public List<String> populateHDPEList(){
        hpde_list.add("Shampoo Bottles");
        hpde_list.add("Grocery Bag");
        hpde_list.add("Chemical Container");
        hpde_list.add("Other...");
        return hpde_list;
    }

    public List<String> populatePVCList(){
        pvc_list.add("Cable Wire");
        pvc_list.add("Water Pipe");
        pvc_list.add("Candy Wrappers");
        pvc_list.add("Other...");
        return pvc_list;
    }

    public List<String> populateLDPEList(){
        ldpe_list.add("Grocery Bag");
        ldpe_list.add("Plastic Wrapper");
        ldpe_list.add("Plastic Film");
        ldpe_list.add("Other...");
        return ldpe_list;
    }

    public List<String> populatePPList(){
        pp_list.add("Yogurt Cups");
        pp_list.add("Straw");
        pp_list.add("Hanger");
        pp_list.add("Other...");
        return pp_list;
    }

    public List<String> populatePSList(){
        ps_list.add("Packaging Material");
        ps_list.add("Disposable Coffee Cups");
        ps_list.add("Plastic Food Boxes");
        ps_list.add("Other...");
        return ps_list;
    }

    public List<String> populateOtherList(){
        other_list.add("Nylon");
        other_list.add("CDs");
        other_list.add("Plastic Food Boxes");
        other_list.add("Other...");
        return other_list;
    }

    public List<String> populateBrandList(){
        brand_list.add("Coca-Cola");
        brand_list.add("Nestlé");
        brand_list.add("Unilever");
        brand_list.add("Other...");
        return brand_list;
    }


}
