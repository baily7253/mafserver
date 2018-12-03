package cn.com.mafentity;

public class ParamResultEntity {

    private String Exec_id;//执行ID
    private String scenario_cases_id;//场景用例ID
    private String param_id;//数据ID
    private String param_name;//数据名称
    private int Case_seq;//用例顺序
    private String Exec_status;//执行状态
    private String Test_result;//测试结果
    private String create_by;//创建者id


    public String getExec_id() {
        return Exec_id;
    }

    public void setExec_id(String exec_id) {
        Exec_id = exec_id;
    }

    public String getScenario_cases_id() {
        return scenario_cases_id;
    }

    public void setScenario_cases_id(String scenario_cases_id) {
        this.scenario_cases_id = scenario_cases_id;
    }

    public String getParam_id() {
        return param_id;
    }

    public void setParam_id(String param_id) {
        this.param_id = param_id;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getExec_status() {
        return Exec_status;
    }

    public void setExec_status(String exec_status) {
        Exec_status = exec_status;
    }

    public int getCase_seq() {
        return Case_seq;
    }

    public void setCase_seq(int case_seq) {
        Case_seq = case_seq;
    }


    public String getTest_result() {
        return Test_result;
    }

    public void setTest_result(String test_result) {
        Test_result = test_result;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }


}
