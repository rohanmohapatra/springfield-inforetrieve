package net.springfield.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class Reprocess
 */
public class Reprocess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<String> sFiles = null;
	private static final String UPLOAD_DIRECTORY = "D:/Study/Springfield/upload/extracted/";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reprocess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String x = request.getQueryString();
		String file = x.split("\\+")[0];
		String Rule = x.split("\\+")[1];
		HttpSession session = request.getSession();
    	String username = (String) session.getAttribute("UserName");
        MongoDatabase database = Util.getDb("springfield");
        MongoCollection<Document> collection = database.getCollection(username);
		if(file.split("\\.").length==2)
		{
			String[] command = {"cmd.exe","/c","D:\\Python 35\\python3","D:/Study/Springfield/upload/pywrapper.py","--file",file,"--rules",Rule,"--b","0"};
        	try {
        		BasicDBObject newDocument2 = new BasicDBObject();
              	newDocument2.append("$set", new BasicDBObject().append("Status", "extracting"));
              	BasicDBObject searchQuery2 = new BasicDBObject().append("FileName", file);
              	collection.updateOne(searchQuery2, newDocument2);
                // print a message
                System.out.println("Executing Command");
                String line;
                // create a process and execute notepad.exe
                ProcessBuilder pb = new ProcessBuilder(command);
                Process process = pb.start();
                int errCode = process.waitFor();

                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                // print another message
                System.out.println("Command Ends");
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                  }
                  input.close();
                  
                File srcFile = new File("D:/Study/Springfield/upload/extracted/single/"+file.split("\\.")[0]+".txt");
          		String contents = FileUtils.readFileToString(srcFile, "UTF-8");
          		BasicDBObject newDocument = new BasicDBObject();
              	newDocument.append("$set", new BasicDBObject().append("Information",contents));
              	BasicDBObject searchQuery = new BasicDBObject().append("FileName", file);
              	collection.updateOne(searchQuery, newDocument);
             } catch (Exception ex) {
                ex.printStackTrace();
             }
		}
		else
		{
			String[] command = {"cmd.exe","/c","D:\\Python 35\\python3","D:/Study/Springfield/upload/pywrapper.py","--file",file,"--rules",Rule,"--b","1"};
        	try {

                // print a message
        		File folderC = new File(UPLOAD_DIRECTORY+file);
                File[] files = folderC.listFiles();
                for(File f: files) {
                	BasicDBObject newDocument = new BasicDBObject();
                	newDocument.append("$set", new BasicDBObject().append("Status", "extracting"));
                	//newDocument.append("$set", new BasicDBObject());
                	BasicDBObject searchQuery = new BasicDBObject().append("FileName", file+"/"+f.getName());

                	collection.updateOne(searchQuery, newDocument);
                }
                System.out.println("Executing Command");
                for(int i=0;i<command.length;i++)
                {
                	System.out.println(command[i]);
                }
                
                String line;
                // create a process and execute notepad.exe
                ProcessBuilder pb = new ProcessBuilder(command);
                Process process = pb.start();
                int errCode = process.waitFor();

                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                // print another message
                System.out.println("Command Ends");
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                  }
                  input.close();
                  
                  
                  for(File f: files){
                	  File srcFile = new File("D:/Study/Springfield/upload/extracted/"+file+"/"+f.getName().split("\\.")[0]+".txt");
                      String contents = FileUtils.readFileToString(srcFile, "UTF-8");
                  		BasicDBObject newDocument = new BasicDBObject();
                    	newDocument.append("$set", new BasicDBObject().append("Information", contents));
                    	//newDocument.append("$set", new BasicDBObject());
                    	BasicDBObject searchQuery = new BasicDBObject().append("FileName", file+"/"+f.getName());

                    	collection.updateOne(searchQuery, newDocument);
                      //System.out.println(f.getName());
                  }
                  
                  
             } catch (Exception ex) {
                ex.printStackTrace();
             }
		}
		response.sendRedirect("EditRules");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
