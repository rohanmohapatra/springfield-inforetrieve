package net.springfield.upload;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase; 

import org.bson.Document; 
import com.mongodb.MongoClient; 
import static com.mongodb.client.model.Filters.eq;
/**
 * Servlet implementation class UploadSingle
 */
public class UploadSingle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "D:/Study/Springfield/upload/pdf";
	private static final String UPLOAD_DIRECTORY2 = "D:/Study/Springfield/upload/rules";
    private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    private static Integer batchn = 0;
    
    public class PythonCall implements Runnable {

        private String pythonWrapperFile = System.getProperty("user.dir") + "/scripts/pywrapper1.py";
        private String extractedFile = System.getProperty("user.dir")+"/upload/extracted";
    	private String fileName;
    	private String ruleName;
    	private Integer isBatch;
    	private MongoCollection<Document> collection;
    	public PythonCall(String file,String rule,Integer batch,MongoCollection<Document> c)
    	{
    		fileName = file;
    		ruleName = rule;
    		isBatch = batch;
    		collection = c;
    	}
    	public void run() {
    		String[] command = {"python3",pythonWrapperFile,"--file",fileName,"--rules",ruleName,"--b",isBatch.toString()};
    		System.out.println(isBatch.toString());
        	try {

        	    System.out.println("Working Directory : "+
                        System.getProperty("user.dir"));

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
                if(isBatch == 0) {  
                File srcFile = new File(extractedFile+"/single/"+fileName.split("\\.")[0]+".txt");
          		String contents = FileUtils.readFileToString(srcFile, "UTF-8");
          		BasicDBObject newDocument = new BasicDBObject();
              	newDocument.append("$set", new BasicDBObject().append("Information",contents));
              	BasicDBObject searchQuery = new BasicDBObject().append("FileName", fileName);
              	collection.updateOne(searchQuery, newDocument);
                }
                /*
                else
                	if(isBatch == 2)
                	{
                		File srcFile = new File("D:/Study/Springfield/upload/ocr-text/single/"+fileName.split("\\.")[0]+".txt");
                  		String contents = FileUtils.readFileToString(srcFile, "UTF-8");
                  		BasicDBObject newDocument = new BasicDBObject();
                      	newDocument.append("$set", new BasicDBObject().append("Information",contents));
                      	BasicDBObject searchQuery = new BasicDBObject().append("FileName", fileName);
                      	collection.updateOne(searchQuery, newDocument);
                	}*/
             } catch (Exception ex) {
                ex.printStackTrace();
             }
    	}
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter writer = response.getWriter();
            writer.println("Request does not contain upload data");
            writer.flush();
            return;
        }
         
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
         
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
         
        // constructs the directory path to store upload file
        //String uploadPath =UPLOAD_DIRECTORY;

        String uploadPath =  System.getProperty("user.dir") + "/upload/pdf";

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        HttpSession session = request.getSession();
    	String username = (String) session.getAttribute("UserName");
        MongoDatabase database = Util.getDb("springfield");
        MongoCollection<Document> collection = database.getCollection(username);
        Document document = new Document();
        try {
            // parses the request's content to extract file data
        	
        	
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();
            String backer="";
            boolean isImage = false;
            boolean useDB = false;
            // iterates over form's fields
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    String fileName = new File(item.getName()).getName();
                    String[] ext2 = fileName.split("\\.");
                    String fileExt =ext2[ext2.length-1];
                    document.append("Status", "extracting");
                    System.out.println("Working Directory : "+
                            System.getProperty("user.dir"));
                    if(fileExt.equals("pdf"))
                    {
                    	if(isImage)
                    	{
                    		String filePath = uploadPath + File.separator + "single" + File.separator + "I_"+fileName;
                            File storeFile = new File(filePath);
                            backer = "I_"+ext2[0]; 
                            // saves the file on disk
                            item.write(storeFile);
                            document.append("FileName", "I_"+fileName);
                            batchn = 2;
                    	}
                    	else
                    	{
                    		String filePath = uploadPath + File.separator + "single" + File.separator + fileName;
                            File storeFile = new File(filePath);
                            backer = ext2[0]; 
                            // saves the file on disk
                            item.write(storeFile);
                            document.append("FileName", fileName);
                    	}
                    	
                    }
                    else
                    	if(fileExt.equals("txt"))
                    {
                        //String filePath = UPLOAD_DIRECTORY2 + File.separator + backer+"Rules.txt";
                        String filePath = System.getProperty("user.dir")+"/upload/rules" + File.separator + backer+"Rules.txt";

                        File storeFile = new File(filePath);
                        // saves the file on disk
                        item.write(storeFile);
                        document.append("Rules", item.getString());
                        
                    }
                }
                if(item.isFormField())
                {
                	 String fieldName = item.getFieldName();
                     String desc = item.getString();
                     System.out.println(desc);
                     if(desc.equals("Image"))
                     {
                    	 isImage = true;
                     }
                     if(desc.equals("Rule"))
                     {
                    	 useDB = true;
                     }
                }
            }
            collection.insertOne(document);
            if(useDB==true)
            {
            	ArrayList<String> ar = Util.getQueries("springfield", username, "Rules",backer+".pdf");
            	System.out.println(ar);
            }
            Runnable r = new PythonCall(backer+".pdf",backer+"Rules.txt",batchn,collection);
            Thread t1 = new Thread(r);
            t1.start();
            
            
            request.setAttribute("message", "Upload has been done Successfully!");
        } catch (Exception ex) {
            request.setAttribute("message", ex.getMessage());
        }
        getServletContext().getRequestDispatcher("/upload.jsp").forward(request, response);
    }
	}


