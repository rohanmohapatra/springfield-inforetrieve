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
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.springfield.upload.UploadSingle.PythonCall;

/**
 * Servlet implementation class UploadMultiple
 */
public class UploadMultiple extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir")+"/upload/pdf";
    private static final String UPLOAD_DIRECTORY2 = System.getProperty("user.dir")+"/upload/rules";
    private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    private static Integer batchn = 1;
    
    
    public class PythonCall implements Runnable {
        private String pythonWrapperFile = System.getProperty("user.dir") + "/scripts/pywrapper1.py";
        private String extractedFile = System.getProperty("user.dir")+"/upload/extracted/";
    	private String fileName;
    	private String ruleName;
    	private Integer isBatch;
    	private MongoCollection<Document> collection;
    	private List<String> sFiles;
    	public PythonCall(String file,String rule,Integer batch,MongoCollection<Document> c,List<String> f)
    	{
    		fileName = file;
    		ruleName = rule;
    		isBatch = batch;
    		collection = c;
    		sFiles = f;
    	}
    	public void run() {
    		String[] command = {"python3",pythonWrapperFile,"--file",fileName,"--rules",ruleName,"--b",isBatch.toString()};
        	try {

                // print a message
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
                  if(isBatch == 1) {
                	  for(int i=0;i<sFiles.size();i++)
                      {
                    	 File srcFile = new File(extractedFile+sFiles.get(i).split("\\.")[0]+".txt");
                        String contents = FileUtils.readFileToString(srcFile, "UTF-8");
                    	BasicDBObject newDocument = new BasicDBObject();
                      	newDocument.append("$set", new BasicDBObject().append("Information", contents));
                      	//newDocument.append("$set", new BasicDBObject());
                      	BasicDBObject searchQuery = new BasicDBObject().append("FileName", sFiles.get(i));

                      	collection.updateOne(searchQuery, newDocument);
                      }
                  }
                  
                  
             } catch (Exception ex) {
                ex.printStackTrace();
             }
    	}
    }
    /**
     * handles file upload via HTTP POST method
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // checks if the request actually contains upload file
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
        String uploadPath = UPLOAD_DIRECTORY;
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        //String folderText = request.getParameter("folderText");
        //System.out.println(folderText);
        
        HttpSession session = request.getSession();
    	String username = (String) session.getAttribute("UserName");
        MongoDatabase database = Util.getDb("springfield");
        MongoCollection<Document> collection = database.getCollection(username);
        
        
        List<String> Files = new ArrayList<String>();
        
        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();
            String backer="";
            boolean isImage = false;
            // iterates over form's fields
            while (iter.hasNext()) {
            	Object x = iter.next();
                FileItem item = (FileItem) x;
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                	String folderText = item.getName().split("\\/")[0];
                	File uploadDir2 = new File(uploadPath+File.separator+folderText);
                    if (!uploadDir2.exists()) {
                        uploadDir2.mkdir();
                    }
                	System.out.println(folderText);
                    String fileName = new File(item.getName()).getName();
                    String[] ext2 = fileName.split("\\.");
                    String fileExt =ext2[ext2.length-1];
                    if(fileExt.equals("pdf"))
                    {
                    	if(isImage)
                    	{
                    		String filePath = uploadPath + File.separator + folderText + File.separator + fileName;
                            File storeFile = new File(filePath);
                            backer = folderText; 
                            // saves the file on disk
                            item.write(storeFile);
                            Document document = new Document();
                            document.append("Status", "extracting");
                            document.append("FileName", folderText + "/I_" + fileName);
                        	Files.add(folderText + "/I_" + fileName);
                        	collection.insertOne(document);
                        	batchn = 3;
                    	}
                    	else {
                    		String filePath = uploadPath + File.separator + folderText + File.separator + fileName;
                            File storeFile = new File(filePath);
                            backer = folderText; 
                            // saves the file on disk
                            item.write(storeFile);
                            Document document = new Document();
                            document.append("Status", "extracting");
                            document.append("FileName", folderText + "/" + fileName);
                        	Files.add(folderText + "/" + fileName);
                        	collection.insertOne(document);
                    	}
                    	
                    }
                    else
                    	if(fileExt.equals("txt"))
                    {
                    	
                    	String filePath = UPLOAD_DIRECTORY2 + File.separator + backer + "Rules"+".txt";
                        File storeFile = new File(filePath);
                        // saves the file on disk
                        item.write(storeFile);
                        for(int i=0;i<Files.size();i++)
                        {
                        	BasicDBObject newDocument = new BasicDBObject();
                        	newDocument.append("$set", new BasicDBObject().append("Rules", item.getString()));
                        			
                        	BasicDBObject searchQuery = new BasicDBObject().append("FileName", Files.get(i));

                        	collection.updateOne(searchQuery, newDocument);
                        }
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
                }
            }
            Runnable r = new PythonCall(backer,backer+"Rules.txt",batchn,collection,Files);
            Thread t1 = new Thread(r);
            t1.start();
            request.setAttribute("message", "Upload has been done successfully!");
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
        getServletContext().getRequestDispatcher("/upload.jsp").forward(request, response);
    }
}
