package net.springfield.upload;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
/**
 * Servlet implementation class DisplayResults
 */
public class DisplayResults extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "D:/Study/Springfield/upload/extracted/";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayResults() {
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
        		
        request.setAttribute("required","<script>setTimeout(function(){\r\n" + 
             		"   window.location.reload(1);\r\n" + 
         	"}, 3000);</script>");
		 HttpSession session = request.getSession();
	    String username = (String) session.getAttribute("UserName");
	    MongoDatabase database = Util.getDb("springfield");
	    MongoCollection<Document> collection = database.getCollection(username);
	    FindIterable<Document> documents = collection.find();
	    String fContent = "<table style=\"width:100%\">";
	    for(Document doc : documents) {
	    	
	    	fContent = fContent + "<tr>";
			fContent = fContent + "<td>"+ doc.getString("FileName") +"</td>";
			fContent = fContent + "<td>"+ doc.getString("Status") +"</td>";
			String[] checker = doc.getString("FileName").split("\\/");
			if(checker.length == 1) {
				fContent = fContent + "<td>"+ "<a href=\""+"http://127.0.0.1:8887/extracted/"+"single/"+doc.getString("FileName").split("\\.")[0]+".csv"+"\" download>CSV</a>" +"</td>";
				fContent = fContent + "<td>"+ "<a href=\""+"http://127.0.0.1:8887/extracted/"+"single/"+doc.getString("FileName").split("\\.")[0]+".txt"+"\" download>Text File</a>" +"</td>";
			}
			else
				if(checker.length == 2)
			{
				fContent = fContent + "<td>"+ "<a href=\""+"http://127.0.0.1:8887/extracted/"+doc.getString("FileName").split("\\.")[0]+".csv"+"\" download>CSV</a>" +"</td>";
				fContent = fContent + "<td>"+ "<a href=\""+"http://127.0.0.1:8887/extracted/"+doc.getString("FileName").split("\\.")[0]+".txt"+"\" download>Text File</a>" +"</td>";
			}
			fContent = fContent + "</tr>";
            //x.append(doc.getString("FileName")).append(doc.getString("Status")).append("\n\n\n");
            
        }
	    fContent = fContent + "</table>";
		System.out.println(fContent);
		request.setAttribute("text", fContent);
		getServletContext().getRequestDispatcher("/show.jsp").forward(request, response);
	}

}
