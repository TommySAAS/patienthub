package patienthub.binary.com.patienthub.data;

/**
 * Created by Mark Aziz on 7/10/2015.
 */
public class Dosage {

    private int patient_id;
    private int id;
    private String start_date;
    private int frequency;
    private String time_taken;
    private String created_at;
    private String updated_at;
    private String treatment_name;

    public Dosage(int patient_id, int id, String start_date, int frequency, String time_taken, String created_at, String updated_at, String treatment_name) {
        this.patient_id = patient_id;
        this.id = id;
        this.start_date = start_date;
        this.frequency = frequency;
        this.time_taken = time_taken;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.treatment_name = treatment_name;
    }

    public Dosage(){}

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(String time_taken) {
        this.time_taken = time_taken;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTreatment_name() {
        return treatment_name;
    }

    public void setTreatment_name(String treatment_name) {
        this.treatment_name = treatment_name;
    }
}