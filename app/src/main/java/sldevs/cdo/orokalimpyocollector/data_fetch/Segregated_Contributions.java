package sldevs.cdo.orokalimpyocollector.data_fetch;

public class Segregated_Contributions {

    public String segregation_id;
    public String consolidator_id;
    public String consolidator_name;

    public String waste_type;
    public String plastic_type;
    public String plastic_name;
    public String brand;

    public String kilo;
    public String date;
    public String time;

    public Segregated_Contributions() {
    }

    public Segregated_Contributions(String segregation_id, String consolidator_id, String consolidator_name, String waste_type, String brand, String kilo, String date, String time) {
        this.segregation_id = segregation_id;
        this.consolidator_id = consolidator_id;
        this.consolidator_name = consolidator_name;
        this.waste_type = waste_type;
        this.brand = brand;
        this.kilo = kilo;
        this.date = date;
        this.time = time;
    }


    public String getSegregation_id() {
        return segregation_id;
    }

    public void setSegregation_id(String segregation_id) {
        this.segregation_id = segregation_id;
    }

    public String getConsolidator_id() {
        return consolidator_id;
    }

    public void setConsolidator_id(String consolidator_id) {
        this.consolidator_id = consolidator_id;
    }

    public String getConsolidator_name() {
        return consolidator_name;
    }

    public void setConsolidator_name(String consolidator_name) {
        this.consolidator_name = consolidator_name;
    }

    public String getWaste_type() {
        return waste_type;
    }

    public void setWaste_type(String waste_type) {
        this.waste_type = waste_type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getKilo() {
        return kilo;
    }

    public void setKilo(String kilo) {
        this.kilo = kilo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
