import zdy.*;

import java.text.SimpleDateFormat;

import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Thread;
import java.io.*;


public class test  extends Thread{
	  public connect con;
	  public Vector<ssh2_monitor> config;
	  public int i;
	  public String Lock_talbe;
	  public static Map<String,test> map=new HashMap<String,test>();
	  public static Map<String,Integer> map_status=new HashMap<String,Integer>();
	  public int is_kill ;//֪��ִ��kill
	  public void test()
	  {
	  	is_kill =0;
	  }
	  public void set_Lock_talbe(String Lock_talbe)
	  {
	  	this.Lock_talbe=Lock_talbe;
	  }
	  public String get_Lock_talbe()
	  {
	  	return this.Lock_talbe;
	  }
	  
	  public void set_index(int i)
	  {
	  	this.i=i;
	  }
	  public int get_index()
	  {
	  	return this.i;
	  }
	    public void set_con(connect con)
	  {
	  	this.con=con;
	  }
	    public void set_config(Vector<ssh2_monitor> config)
	  {
	  	this.config=config;
	  }
	  //��ӡ������
	  public static String getLineInfo()
    {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }
    //��ӡϵͳʱ��
    public static String get_time(){
	  SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"); //�������ڸ�ʽ
    return   df.format(new Date()) + ": "; // new Date()Ϊ��ȡ��ǰϵͳʱ��
	  
	}


    public static String replaceBlank(String str) {
        String dest = "";

        if (str != null) {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(str);
            m.find();
            dest = m.group();
        }
       

        return dest;
    }

    //ִ�е�ǰ���� ���Ϊ ������ ����
    public static void curren_action(ssh2_monitor tmp_monitor,
        String str_threshold) {
        if (tmp_monitor.get_cmd_action() != null) {
            RmtShellExecutor exe0 = new RmtShellExecutor(tmp_monitor.get_ip(),
                    tmp_monitor.get_user_name(), tmp_monitor.get_passwd());

            //ƴ���⵽��ֵ
            String alert_str2 = tmp_monitor.get_cmd_action();
            alert_str2 = alert_str2.replace("#", str_threshold);
            alert_str2 = alert_str2.replace("[]", tmp_monitor.get_name());
            System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" ִ�� CMD_ACTION����: " + alert_str2);
            //System.out.print();

            try {
                System.out.print(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" ִ�� CMD_ACTION�������� "+exe0.exec(alert_str2) + "\n");
            } catch (java.lang.Exception e) {e.printStackTrace();
            } finally {
            }
        }
    }

    //ִ�й�������    
    //start Ϊid ������ʼ��
    //tmp_monitorΪ ��ǰ������ ����
    //config ����vector
    //str_threshold ��ǰ��������ֵ
    public static void Relate_action(int start, ssh2_monitor tmp_monitor,
        Vector<ssh2_monitor> config, connect con, String str_threshold,String str_lock) {
        if ((tmp_monitor.get_other_action() > 0) &&
                (tmp_monitor.get_other_action() != tmp_monitor.get_id())) {
            System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" Ѱ�ҹ�������" + tmp_monitor.get_other_action());

            for (int j = start; j < config.size(); j++) {
                // �����������
                ssh2_monitor tmp_monitor2 = config.get(j);

                if (tmp_monitor2.get_id() != tmp_monitor.get_other_action()) {
                    continue;
                } else
                {
                	try {
                if(tmp_monitor2.get_cmd_type() == 3 ||tmp_monitor2.get_cmd_type() == 4 ){
                System.out.print(getLineInfo()+" " +get_time()+" "+tmp_monitor2.get_name()+"����¼: " );
                //����ǰ��¼
                String lock_str="update "+str_lock +" set cmd_type = 10 where id = "+tmp_monitor2.get_id();
                System.out.print(lock_str+"\n");
                //����¼sql
                con.do_update(lock_str);
                
                 //װ���߳�map 
                 System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor2.get_name() + " װ�� HashMap<String,test> \n");  	
                 
                map_status.put(tmp_monitor2.get_name(),tmp_monitor2.get_cmd_type());   
                 
                }
                 if (tmp_monitor2.get_cmd_type() == 3) {
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" �ҵ���������" + tmp_monitor2.get_id());
                    System.out.println("\n"+tmp_monitor.get_name()+" "+getLineInfo()+" " +get_time()+" "+"===============" + "  ��ʼִ��  " +
                        tmp_monitor2.get_name() + "===============\n");

                    //��½ ssh2
                    RmtShellExecutor exe2 = new RmtShellExecutor(tmp_monitor2.get_ip(),
                            tmp_monitor2.get_user_name(),
                            tmp_monitor2.get_passwd());

                    //ƴ��ִ������
                    String cmd_all2 = tmp_monitor2.get_cmd();

                    if (tmp_monitor2.get_cmd_param1() != null) {
                        cmd_all2 += (" " + tmp_monitor2.get_cmd_param1());
                    }

                    if (tmp_monitor2.get_cmd_param2() != null) {
                        cmd_all2 += (" " + tmp_monitor2.get_cmd_param2());
                    }

                    if (tmp_monitor2.get_cmd_param3() != null) {
                        cmd_all2 += (" " + tmp_monitor2.get_cmd_param3());
                    }

                    cmd_all2 = cmd_all2.replace("#", str_threshold);
                    cmd_all2 = cmd_all2.replace("[]", tmp_monitor2.get_name());

                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() +"ִ�� OTHER_ACTION������� " + cmd_all2);

                    //ִ������
                    try {
                        String cmd_return2 = exe2.exec(cmd_all2);
                        System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() +" ִ�й��������ֵ�� " + cmd_return2 + "\n");
                        //ִ�е�ǰ����
                        curren_action(tmp_monitor2, str_threshold);
                        //����ִ�����ӵĶ���
                        Relate_action(j, tmp_monitor2, config, con,
                            str_threshold, str_lock);
                    } catch (java.lang.Exception e) {e.printStackTrace();
                    } finally {
                    }
                }
                 if (tmp_monitor2.get_cmd_type() == 4) {
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() +" �ҵ���������" + tmp_monitor2.get_id());
                    System.out.println("\n"+tmp_monitor.get_name() +getLineInfo()+" " +get_time()+" "+"===============" + "  ��ʼִ��  " +
                        tmp_monitor2.get_name() + "===============\n");

                    //ƴ��ִ������
                    String cmd_all4 = tmp_monitor2.get_cmd();
                    cmd_all4 = cmd_all4.replace("#", str_threshold);
                    cmd_all4 = cmd_all4.replace("[]", tmp_monitor2.get_name());
                    
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() +" ִ�� OTHER_ACTION��SQL�� " + cmd_all4);
                    //ִ��sql
                    con.do_update(cmd_all4);
                    //ִ�е�ǰ����
                    curren_action(tmp_monitor2, str_threshold);
                    //����ִ�����ӵĶ���
                    Relate_action(j, tmp_monitor2, config, con, str_threshold, str_lock);
                }
                
              }
              
              catch(java.lang.Exception e){e.printStackTrace();}
                finally{
                                
                   //װ���߳�map           
                 if(map_status.containsKey(tmp_monitor2.get_name()))
                 { 
                 	System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor2.get_name() + " װ�� HashMap<String,test> \n");  	
                 	tmp_monitor2.set_cmd_type(map_status.get(tmp_monitor2.get_name()));
                   map_status.remove(tmp_monitor2.get_name());
                   String unlock_str2="update "+str_lock +" set cmd_type = "+tmp_monitor2.get_cmd_type()+",receiver = null"+" where id = "+tmp_monitor2.get_id();
                   System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor2.get_name() + "����sqlΪ" + ":"+unlock_str2);
                //������¼sql
                  con.do_update(unlock_str2);
                
                 
                }
                }
            }
        }
    }
  }

     public void run(){
            // �����������
            ssh2_monitor tmp_monitor = this.config.get(this.get_index());
           
           try{
           	if (tmp_monitor.get_cmd_type() == 10 &&tmp_monitor.get_receiver() !=null && tmp_monitor.get_receiver().equals("kill") ){
           		System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor.get_name() + " ��Ҫ��ֹ \n");  	
                 if(map.containsKey(tmp_monitor.get_name()) || map_status.containsKey(tmp_monitor.get_name()))
                 {
                 try{
                 //ɱ��Զ�˽���
                RmtShellExecutor exe = new RmtShellExecutor(tmp_monitor.get_ip(),
                tmp_monitor.get_user_name(), tmp_monitor.get_passwd());
                        
                tmp_monitor.set_cmd_type(map_status.get(tmp_monitor.get_name())*(-1));
                map_status.remove(tmp_monitor.get_name());
                map_status.put(tmp_monitor.get_name(),tmp_monitor.get_cmd_type());
                String unlock_str="update "+get_Lock_talbe() +" set cmd_type = "+tmp_monitor.get_cmd_type()+",receiver = null"+" where id = "+tmp_monitor.get_id();
                System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "����sqlΪ" + ":"+unlock_str);
                //������¼sql
                  this.con.do_update(unlock_str);
                
                if(tmp_monitor.get_cmd_action()!=null){
                String cmd_kill ="ps -ef|grep "+tmp_monitor.get_cmd_action()+"  |grep -v grep|awk '{print $2}'|xargs -i kill -9 {}";
                System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor.get_name() + " ��ֹ���"+cmd_kill+"\n");
                String cmd_return = exe.exec(cmd_kill);
                  
                System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor.get_name() + " ��ֹ����ؽ����"+cmd_return+"\n");
               }
               if(tmp_monitor.get_cmd()!=null && map_status.get(tmp_monitor.get_name())==-3 ){
                String cmd_kill ="ps -ef|grep "+tmp_monitor.get_cmd()+"  |grep -v grep|awk '{print $2}'|xargs -i kill -9 {}";
                System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor.get_name() + " ��ֹ���"+cmd_kill+"\n");
                String cmd_return = exe.exec(cmd_kill);  
                
                System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor.get_name() + " ��ֹ����ؽ����"+cmd_return+"\n");
               }
               

                

                  
                 }catch (java.lang.Exception e) {e.printStackTrace();
                    } finally {
                    }
                    
                    
                try{                
                if(map.containsKey(tmp_monitor.get_name())){  
                //map.get(tmp_monitor.get_name()).stop();	        	
                map.get(tmp_monitor.get_name()).is_kill=1;
                }
                
                }catch (java.lang.Exception e) {e.printStackTrace();
                    } finally {
                    }
           	}
          }
           	
               ///////////////////////////////////////////////////
            if ((tmp_monitor.get_cmd_type() == 1) ||
                    (tmp_monitor.get_cmd_type() == 2)) {
                    	
                 //װ���߳�map 
                 System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor.get_name() + " װ�� HashMap<String,test> \n");  	
                 map.put(tmp_monitor.get_name(),this);
                 map_status.put(tmp_monitor.get_name(),tmp_monitor.get_cmd_type());   
                 	
                System.out.println("\n"+getLineInfo()+" " +get_time()+" "+"===============" + "  ��ʼ���  " +
                    tmp_monitor.get_name() + "===============\n");

                SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"); //�������ڸ�ʽ
                System.out.print(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+"����¼: "); // new Date()Ϊ��ȡ��ǰϵͳʱ��
                //����ǰ��¼
                String lock_str="update "+get_Lock_talbe() +" set cmd_type = 10 where id = "+tmp_monitor.get_id();
                System.out.print(lock_str+"\n");
                //����¼sql
                        this.con.do_update(lock_str);
            }

            // ���������ֵ	
            long cmd_threshold = tmp_monitor.get_cmd_threshold();

            //��ֵ���ַ�����ʽ
            String str_threshold = Long.toString(cmd_threshold);

            //ִ�м����������
            if (tmp_monitor.get_cmd_type() == 1) {
                //��½ ssh2
                RmtShellExecutor exe = new RmtShellExecutor(tmp_monitor.get_ip(),
                        tmp_monitor.get_user_name(), tmp_monitor.get_passwd());

                //ƴ��ִ������
                String cmd_all = tmp_monitor.get_cmd();

                if (tmp_monitor.get_cmd_param1() != null) {
                    cmd_all += (" " + tmp_monitor.get_cmd_param1());
                }

                if (tmp_monitor.get_cmd_param2() != null) {
                    cmd_all += (" " + tmp_monitor.get_cmd_param2());
                }

                if (tmp_monitor.get_cmd_param3() != null) {
                    cmd_all += (" " + tmp_monitor.get_cmd_param3());
                }

                
                String cmd_return="";
                //ִ������
                try{
                System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" ִ�м����� " + cmd_all + "\n");
                cmd_return = exe.exec(cmd_all);
                System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" ִ�з���ֵ:{" + cmd_return + "}\n");
                ///////////////////////////////////////////////////
                  
                        
                 }
              catch (java.lang.Exception e) {e.printStackTrace();
                    } finally {
                    	                //ɸѡ����ֵ
                    cmd_return = this.replaceBlank(cmd_return);
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" �����ֵ�ĳ��ȣ� " + cmd_return.length() + "\n");
                    // //�����ѯ�Ľ��̲������򣬷���ֵΪ0 
                    // if(tmp_monitor.get_cmd().indexOf("ps -ef")!=-1){
                    // 	cmd_return = "0";
                    //}
                     
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" �����ֵ�� " + cmd_return + "\n");
                   
                    if(cmd_return.length() == 0){
                                   cmd_return = "0";
                                   }
                if (cmd_threshold > 0) {
                    long return_value = Long.parseLong(cmd_return);
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "���ֵΪ" + ":" +
                        cmd_return + "\n");
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "����ֵΪ" + ":" +
                        cmd_threshold + "\n");

                    //������ֵ�澯
                    if (return_value >= cmd_threshold) {
                        System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "������ֵ:�澯��" +
                            "\n");

                        String alert_str = tmp_monitor.get_sql();
                        if(alert_str !=null){
                        //ƴ���⵽��ֵ
                        alert_str = alert_str.replace("#", str_threshold);
                        alert_str = alert_str.replace("[]",tmp_monitor.get_name());
                        System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" �澯SQL���£�\n" + alert_str);
                        //ִ��sql�澯����
                        this.con.do_update(alert_str);
                      }
                        //ִ�е�ǰ����
                        curren_action(tmp_monitor, str_threshold);
                        //ִ�й�������
                        Relate_action(0, tmp_monitor, this.config, this.con, str_threshold,get_Lock_talbe());
                    }
                }
                if (cmd_threshold < 0) {
                	  cmd_threshold=cmd_threshold*(-1);
                    long return_value = Long.parseLong(cmd_return);
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "���ֵΪ" + ":" +
                        cmd_return + "\n");
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "����ֵΪ" + ":" +
                        cmd_threshold + "\n");

                    //������ֵ�澯
                    if (return_value < cmd_threshold) {
                        System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "������ֵ:�澯��" +
                            "\n");

                        String alert_str = tmp_monitor.get_sql();
                        if(alert_str !=null){
                        //ƴ���⵽��ֵ
                        alert_str = alert_str.replace("#", str_threshold);
                        alert_str = alert_str.replace("[]", tmp_monitor.get_name());
                        System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" �澯SQL���£�\n" + alert_str);
                        //ִ��sql�澯����
                        this.con.do_update(alert_str);
                      }
                        //ִ�е�ǰ����
                        curren_action(tmp_monitor, str_threshold);
                        //ִ�й�������
                        Relate_action(0, tmp_monitor, this.config, this.con, str_threshold,get_Lock_talbe());
                    }
                }
                    }
            }

            //���db���б�
            if (tmp_monitor.get_cmd_type() == 2) {
            	 System.out.println(getLineInfo()+" " +get_time()+" do_count("+tmp_monitor.get_cmd()+","+tmp_monitor.get_cmd_param1()+")");
                long cmd_return = this.con.do_count(tmp_monitor.get_cmd(),
                        tmp_monitor.get_cmd_param1());

                if (cmd_threshold > 0) {
                    String str_cmd_return = Long.toString(cmd_return);
                    String str_cmd_threshold = Long.toString(cmd_threshold);

                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "���ֵΪ" + ":" +
                        str_cmd_return + "\n");
                    System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "����ֵΪ" + ":" +
                        str_cmd_threshold + "\n");

                    if (cmd_return >= cmd_threshold) {
                        System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "������ֵ:�澯��");

                        String alert_str = tmp_monitor.get_sql();
                        alert_str = alert_str.replace("#", str_cmd_return);
                        alert_str = alert_str.replace("[]", tmp_monitor.get_name());
                        System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name()+" �澯SQL:" + "\n" + alert_str);
                        //�澯sql
                        this.con.do_update(alert_str);
                        //ִ�е�ǰ����
                        curren_action(tmp_monitor, str_threshold);

                        //ִ�й�������
                        Relate_action(0, tmp_monitor, this.config, con, str_threshold,get_Lock_talbe());
                    }
                }
            }
          }catch (java.lang.Exception e){ e.printStackTrace();}
          	finally{
          //������ǰ��¼
          if (true) {
          	     if(map.containsKey(tmp_monitor.get_name()))
                 { 
                 //װ���߳�map 
                 System.out.println("\n"+getLineInfo()+" " +get_time()+
                    tmp_monitor.get_name() + " װ�� HashMap<String,test> \n");  	
                 map.remove(tmp_monitor.get_name());
               }
                 if(map_status.containsKey(tmp_monitor.get_name()))
                 { 
                 	tmp_monitor.set_cmd_type(map_status.get(tmp_monitor.get_name()));
                   map_status.remove(tmp_monitor.get_name());
                   String unlock_str="update "+get_Lock_talbe() +" set cmd_type = "+tmp_monitor.get_cmd_type()+",receiver = null"+" where id = "+tmp_monitor.get_id();
                   System.out.println(getLineInfo()+" " +get_time()+" "+tmp_monitor.get_name() + "����sqlΪ" + ":"+unlock_str);
                //������¼sql
                  this.con.do_update(unlock_str);
                }
               
          }
        }
        }
          
    public static void main(String[] args) throws Exception {
    	  //�������ļ���������ݿ�������Ϣ
    	  InputStream inputStream = test.class.getResourceAsStream("db_config.properties");   
        Properties p = new Properties();   
        try {   
         p.load(inputStream);   
        } catch (IOException e1) {  e1.printStackTrace(); 
         e1.printStackTrace();   
        }   
    	  
    	  String ip=p.getProperty("ip");
    	  String port=p.getProperty("port");
    	  String service_name = p.getProperty("service_name");
    	  String config_table= p.getProperty("config_table");
    	  String during= p.getProperty("during");
    	  long lduring = Long.parseLong(during);
    	  test boss = new test();
    	  //��ʼ�����ݿ�����
        boss.con = new connect();
        
        while(true){
        //�����ݿⲢ���ȫ�����������Ϣ
        boss.config = boss.con.do_work(ip, port,
                service_name, "select * FROM "+config_table+" order by id");
        //����ִ�����м��
        for (int i = 0; i < boss.config.size(); i++) {
        test t = new test();
        t.set_index(i);
        t.set_Lock_talbe(config_table);
        t.set_config(boss.config);
        t.set_con(boss.con);
        t.start();
        //t.run();
    } 
    //˯��
      boss.sleep(lduring);
  }
}
}
