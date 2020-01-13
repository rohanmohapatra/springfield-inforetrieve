package net.springfield.upload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class EditRules
 */
public class EditRules extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditRules() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter x = response.getWriter();
		//x.append("<script>setTimeout(function(){\r\n" + 
        //		"   window.location.reload(1);\r\n" + 
        //		"}, 3000);</script>");
		 HttpSession session = request.getSession();
	    String username = (String) session.getAttribute("UserName");
	    MongoDatabase database = Util.getDb("springfield");
	    MongoCollection<Document> collection = database.getCollection(username);
	    FindIterable<Document> documents = collection.find();
	    String fContent = "<table style=\"width:100%\">";
	    Integer present=0;

	    String filePath = System.getProperty("user.dir") + "/upload/rules/";


	    for(Document doc : documents) {
	    	
	    	fContent = fContent + "<tr>";
			
			String[] checker = doc.getString("FileName").split("\\/");
			if(checker.length==2)
			{
			present = present+1;
			if(present ==1)
			{
				File srcFile = new File(filePath+checker[0].split("\\.")[0]+"Rules.txt");
          		String contents = FileUtils.readFileToString(srcFile, "UTF-8");
				fContent = fContent + "<td>"+ checker[0].split("\\.")[0] +"</td>";
				fContent = fContent + "<td>"+ "Edit "+ checker[0].split("\\.")[0]+"Rules.txt" +"</td>";
				fContent = fContent + "<td>"+ "<form action=\"ChangeRules\" id=\"usrform\" method=\"post\">" + "<textarea rows=\"7\" cols=\"50\" name=\"comment\" form=\"usrform\">\r\n" + 
						contents+"</textarea>" + " <input type=\"hidden\" name=\"file\" value=\""+checker[0].split("\\.")[0]+"Rules.txt\""+">" +"<input type=\"submit\">\r\n" + 
								"</form>";
				System.out.println(checker[0].split("\\.")[0]+"Rules.txt");
				//fContent = fContent + "<td>"+ "<a href=\"Reprocess\">Reprocess</a>" +"</td>";
				fContent = fContent + "<td>"+ "<a href=\"Reprocess?"+checker[0].split("\\.")[0]+"+"+checker[0].split("\\.")[0]+"Rules.txt"+"\">Reprocess</a>" +"</td>";
				fContent = fContent + "</tr>";
			}
			
			}
			else
				if(checker.length==1)
				{
					File srcFile = new File(filePath+checker[0].split("\\.")[0]+"Rules.txt");
	          		String contents = FileUtils.readFileToString(srcFile, "UTF-8");
				fContent = fContent + "<td>"+ doc.getString("FileName") +"</td>";
				fContent = fContent + "<td>"+ "Edit "+ checker[0].split("\\.")[0]+"Rules.txt" +"</td>";
				fContent = fContent + "<td>"+ "<form action=\"ChangeRules\" id=\"usrform\" method = \"post\">" + "<textarea rows=\"7\" cols=\"50\" name=\"comment\" form=\"usrform\">\r\n" + 
						contents+"</textarea>" + " <input type=\"submit\">\r\n" + 
								"</form>";
				fContent = fContent + "<td>"+ "<a href=\"Reprocess?"+doc.getString("FileName")+"+"+checker[0].split("\\.")[0]+"Rules.txt"+"\">Reprocess</a>" +"</td>";
				fContent = fContent + "</tr>";
				}
			
            //x.append(doc.getString("FileName")).append(doc.getString("Status")).append("\n\n\n");
            
        }
	    fContent = fContent + "</table>";
		System.out.println(fContent);
		request.setAttribute("text", fContent);
		getServletContext().getRequestDispatcher("/show.jsp").forward(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
