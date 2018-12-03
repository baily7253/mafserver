package cn.com.mafentity;


public class TestStepResultEntity {

    private String Exec_id;//用例id
    private String scenario_cases_id;//场景用例ID
    private String Param_id;//数据id
    private String Step_num;//步骤号
    private String Step_name;//步骤名称
    private String Test_Action;//测试动作
    private String Operate_data;//操作数据
    private String Operate_flag;//操作成功标志
    private String Expect_result;//预期结果
    private String Actual_result;//实际结果
    private String Exec_date;//执行日期
    private String Exec_time;//执行时间
    private String Step_img;//截图


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
        return Param_id;
    }

    public void setParam_id(String param_id) {
        Param_id = param_id;
    }

    public String getStep_num() {
        return Step_num;
    }

    public void setStep_num(String step_num) {
        Step_num = step_num;
    }

    public String getStep_name() {
        return Step_name;
    }

    public void setStep_name(String step_name) {
        Step_name = step_name;
    }

    public String getTest_Action() {
        return Test_Action;
    }

    public void setTest_Action(String test_Action) {
        Test_Action = test_Action;
    }

    public String getOperate_data() {
        return Operate_data;
    }

    public void setOperate_data(String operate_data) {
        Operate_data = operate_data;
    }

    public String getOperate_flag() {
        return Operate_flag;
    }

    public void setOperate_flag(String operate_flag) {
        Operate_flag = operate_flag;
    }

    public String getExpect_result() {
        return Expect_result;
    }

    public void setExpect_result(String expect_result) {
        Expect_result = expect_result;
    }

    public String getActual_result() {
        return Actual_result;
    }

    public void setActual_result(String actual_result) {
        Actual_result = actual_result;
    }

    public String getExec_date() {
        return Exec_date;
    }

    public void setExec_date(String exec_date) {
        Exec_date = exec_date;
    }

    public String getExec_time() {
        return Exec_time;
    }

    public void setExec_time(String exec_time) {
        Exec_time = exec_time;
    }

    public String getStep_img() {
        return Step_img;
    }

    public void setStep_img(String step_img) {
        Step_img = step_img;
    }


}

