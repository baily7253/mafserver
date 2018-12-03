package cn.com.mafservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import cn.com.mafconn.MySQLConnectionFactory;
import cn.com.mafentity.ParamResultEntity;
import cn.com.mafentity.TestStepResultEntity;



@Controller
public class ServiceController {
	
	@RequestMapping(value="/hello")
	public String hello(){
		return "hello";
	}
	
	@RequestMapping(value="/getExecuteManageList",
    method=RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String getExecuteManageList(HttpServletRequest request,HttpServletResponse response) throws SQLException{
		
		 String executeName=request.getParameter("executeName");
		 Connection conn =MySQLConnectionFactory.createConnection();
		 StringBuilder sql=new StringBuilder();
		 sql.append("SELECT em.*, a.name as create_user,b.name as update_user FROM execute_manage em ");
		 sql.append("INNER JOIN sys_user a ON a.id = em.create_by ");
		 sql.append("INNER JOIN sys_user b ON b.id = em.update_by ");
		 if(executeName!=null){
			 sql.append("where em.execute_name like '%"+executeName+"%' ");
		 }
		 sql.append("order by update_date desc");
		 PreparedStatement stmt = conn.prepareStatement(sql.toString()); 
		 ResultSet rs = stmt.executeQuery();
		 
		 JSONArray json = new JSONArray();
		 while(rs.next()){
			 JSONObject jo = new JSONObject();
			 jo.put("id", rs.getString("id"));
			 jo.put("execute_name", rs.getString("execute_name"));
			 if (rs.getString("case_id")==null) {
				 jo.put("case_id", "");
			 } else {
				 jo.put("case_id", rs.getString("case_id"));
			 }
			 if (rs.getString("case_name")==null) {
				 
				 jo.put("case_name", "");
			 } else {
				 jo.put("case_name", rs.getString("case_name"));
			 }
			 if(rs.getString("scenario_id")==null){
				 jo.put("scenario_id", "");  
			 }else {
				 jo.put("scenario_id", rs.getString("scenario_id")); 
			 }
			 
			 if (rs.getString("scenario_name")==null) {
				 jo.put("scenario_name", "");
			 } else {
				 jo.put("scenario_name", rs.getString("scenario_name"));
			 }
			 
			 jo.put("create_user", rs.getString("create_user"));	
			 jo.put("create_date", rs.getString("create_date"));
			 jo.put("update_user", rs.getString("update_user"));
			 jo.put("update_date", rs.getString("update_date"));
			 jo.put("brower", rs.getString("brower"));
			 json.add(jo);
		 }
		 return json.toString();  
	}
	
	@RequestMapping(value="/SaveCaseResult",method=RequestMethod.POST,
			        produces= MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String SaveCaseResult(HttpServletRequest request,HttpServletResponse response){
		String ret="Fail";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));//post方式传递读取字符流
			String jsonStr = null;
			StringBuilder result = new StringBuilder();
			try {
			    while ((jsonStr = reader.readLine()) != null) {
	            result.append(jsonStr);
			    }
			}catch(IOException e) {
			   e.printStackTrace();
			}
			reader.close();// 关闭输入流
			if(saveCase(result.toString())){
				ret="Success";
				System.out.println("save case result success");
			}else {
				ret="Fail";
				System.out.println("save case result fail");
			}  
			
		} catch (Exception e) {
			ret="Fail";
			e.printStackTrace();
			
		}
		return ret;
	}

	private static boolean saveCase(String json) {
		
		boolean ret= false;
	    String [] arr=json.split("@@@");
		JSONObject jsonObject = JSONObject.parseObject(arr[0]);
		List<TestStepResultEntity> list=JSONArray.parseArray(arr[1],TestStepResultEntity.class);
		List<ParamResultEntity> paramlist=JSONArray.parseArray(arr[2], ParamResultEntity.class);
		try {
            Connection conn = MySQLConnectionFactory.createConnection();
            Statement statement = conn.createStatement();
            if (jsonObject != null) {
                String sql = String.format("insert into usecaseresult"
                                + "(Exec_id,Case_id,Case_name,Exec_date,Actual_starttime,Actual_enddtime,Consume_time,Exec_status,Test_result,create_by)"
                                + "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                        jsonObject.getString("exec_id"),
                        jsonObject.getString("case_id"),
                        jsonObject.getString("case_name"),
                        jsonObject.getString("exec_date"),
                        jsonObject.getString("actual_starttime"),
                        jsonObject.getString("actual_enddtime"),
                        jsonObject.getString("consume_time"),
                        jsonObject.getString("exec_status"),
                        jsonObject.getString("test_result"),
                        jsonObject.getString("create_by")
                );
                statement.addBatch(sql);
                for (int i = 0; i < paramlist.size(); i++) {
                    String paramsql = "sql" + i;
                    paramsql = String.format("insert into paramresult"
                                    + "(Exec_id,param_id,param_name,Case_seq,Exec_status,Test_result)"
                                    + "values('%s','%s','%s','%s','%s','%s')",
                            paramlist.get(i).getExec_id(),
                            paramlist.get(i).getParam_id(),
                            paramlist.get(i).getParam_name(),
                            paramlist.get(i).getCase_seq(),
                            paramlist.get(i).getExec_status(),
                            paramlist.get(i).getTest_result()
                    );

                    statement.addBatch(paramsql);
                }
                for (int i = 0; i < list.size(); i++) {
                    String sqlname = "sql" + i;

                    sqlname = String.format("insert into teststepresult"
                                    + "(Exec_id,param_id,Step_num,Step_name,Test_Action,Operate_data,Operate_flag,Expect_result,Actual_result,Exec_date,Exec_time,step_img)"
                                    + "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                            list.get(i).getExec_id(),
                            list.get(i).getParam_id(),
                            list.get(i).getStep_num(),
                            list.get(i).getStep_name(),
                            list.get(i).getTest_Action(),
                            ReplaceCode(list.get(i).getOperate_data()),
                            list.get(i).getOperate_flag(),
                            ReplaceCode(list.get(i).getExpect_result()),
                            ReplaceCode(list.get(i).getActual_result()),
                            list.get(i).getExec_date(),
                            list.get(i).getExec_time(),
                            list.get(i).getStep_img()
                    );
                    statement.addBatch(sqlname);
                }

                statement.executeBatch();
            }
            ret=true;
        } catch (SQLException ex) {
        	ret=false;
            ex.printStackTrace();
        }
		return ret;
		
	}
	
	@RequestMapping(value="/SaveScenarioResult",method=RequestMethod.POST,
	        produces= MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String SaveScenarioResult(HttpServletRequest request,HttpServletResponse response){
		String ret="Fail";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));//post方式传递读取字符流
			String jsonStr = null;
			StringBuilder result = new StringBuilder();
			try {
			    while ((jsonStr = reader.readLine()) != null) {
	            result.append(jsonStr);
			    }
			}catch(IOException e) {
			   e.printStackTrace();
			}
			reader.close();// 关闭输入流
			if(SaveScenario(result.toString())){
				ret="Success";
				System.out.println("save Scenario result success");
			}else {
				ret="Fail";
				System.out.println("save Scenario result fail");
			}
			
		} catch (Exception e) {
			ret="Fail";
			e.printStackTrace();
			
		}
		return ret;
	}
	
       private static boolean SaveScenario(String json) {
		
		boolean ret= false;
	    String [] arr=json.split("@@@");
		JSONObject jsonObject = JSONObject.parseObject(arr[0]);
		List<TestStepResultEntity> list=JSONArray.parseArray(arr[1],TestStepResultEntity.class);
		List<ParamResultEntity> paramlist=JSONArray.parseArray(arr[2], ParamResultEntity.class);
		try {
            Connection conn = MySQLConnectionFactory.createConnection();
            Statement statement = conn.createStatement();
            if (jsonObject != null) {
                String sql = String.format("insert into scenarioresult"
                        + "(Exec_id,Scenario_id,Scenario_name,Exec_date,Actual_starttime,Actual_enddtime,Consume_time,Exec_status,Test_result,create_by)"
                        + "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                        jsonObject.getString("exec_id"),
                        jsonObject.getString("scenario_id"),
                        jsonObject.getString("scenario_name"),
                        jsonObject.getString("exec_date"),
                        jsonObject.getString("actual_starttime"),
                        jsonObject.getString("actual_enddtime"),
                        jsonObject.getString("consume_time"),
                        jsonObject.getString("exec_status"),
                        jsonObject.getString("test_result"),
                        jsonObject.getString("create_by")
                );
                statement.addBatch(sql);
                for (int i = 0; i < paramlist.size(); i++) {
                    String paramsql = "sql" + i;
                    paramsql = String.format("insert into paramresult"
                            + "(Exec_id,scenario_cases_id,param_id,param_name,Case_seq,Exec_status,Test_result)"
                            + "values('%s','%s','%s','%s','%s','%s','%s')",
                    paramlist.get(i).getExec_id(),
                    paramlist.get(i).getScenario_cases_id(),
                    paramlist.get(i).getParam_id(),
                    paramlist.get(i).getParam_name(),
                    paramlist.get(i).getCase_seq(),
                    paramlist.get(i).getExec_status(),
                    paramlist.get(i).getTest_result()
                );
                 statement.addBatch(paramsql);
                }
                for (int i = 0; i < list.size(); i++) {
                    String sqlname = "sql" + i;

                    sqlname = String.format("insert into teststepresult"
                            + "(Exec_id,scenario_cases_id,param_id,Step_num,Step_name,Test_Action,Operate_data,Operate_flag,Expect_result,Actual_result,Exec_date,Exec_time,step_img)"
                            + "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                    list.get(i).getExec_id(),
                    list.get(i).getScenario_cases_id(),
                    list.get(i).getParam_id(),
                    list.get(i).getStep_num(),
                    list.get(i).getStep_name(),
                    list.get(i).getTest_Action(),
                    ReplaceCode(list.get(i).getOperate_data()),
                    list.get(i).getOperate_flag(),
                    ReplaceCode(list.get(i).getExpect_result()),
                    ReplaceCode(list.get(i).getActual_result()),
                    list.get(i).getExec_date(),
                    list.get(i).getExec_time(),
                    list.get(i).getStep_img()
            );
                    statement.addBatch(sqlname);
                }

                statement.executeBatch();
            }
            ret=true;
        } catch (SQLException ex) {
        	ret=false;
            ex.printStackTrace();
        }
		return ret;
	}
	
	 private static String ReplaceCode(String s) {
	    	if(s.contains("'")){
	    	    s= s.replace("'", "\\'");
	    	}if (s.contains("<")){
	    		s= s.replace("<", "&lt;"); 
			}if (s.contains(">")) {
				s=s.replace(">", "&gt;"); 
			}
	        return s;
	    }

}
