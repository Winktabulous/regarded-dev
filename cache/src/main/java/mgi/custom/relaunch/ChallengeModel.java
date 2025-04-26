package mgi.custom.relaunch;

public class ChallengeModel {
    //5012
    public int category_id = 0;
    //5015
    public int param_5015 = 500;
    //5009
    public String display = "";
    //5014
    public int sort = 0;
    //5016
    public int param_5016 = 0;
    //5011
    public int param_5011 = 0;

    public ChallengeModel() {
    }

    public ChallengeModel(int category_id, int param_5015, String display, int sort, int param_5016, int param_5011) {
        this.category_id = category_id;
        this.param_5015 = param_5015;
        this.display = display;
        this.sort = sort;
        this.param_5016 = param_5016;
        this.param_5011 = param_5011;
    }

    public int getSort() {
        return sort;
    }
}
