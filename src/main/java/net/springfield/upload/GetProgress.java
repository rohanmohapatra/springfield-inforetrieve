package net.springfield.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class GetProgress
 */
public class GetProgress extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String stri = request.getQueryString();
		String file = stri.split("\\+")[0];
		String status = stri.split("\\+")[1];
		request.setAttribute("name", file);
		request.setAttribute("status", status);
		//getServletContext().getRequestDispatcher("/show.jsp").forward(request, response);
		HttpSession session = request.getSession();
    	String username = (String) session.getAttribute("UserName");
        MongoDatabase database = Util.getDb("springfield");
        MongoCollection<Document> collection = database.getCollection(username);
        System.out.println(username);
        BasicDBObject newDocument = new BasicDBObject();
      	newDocument.append("$set", new BasicDBObject().append("Status",status));
      	System.out.println(file);		
      	BasicDBObject searchQuery = new BasicDBObject().append("FileName", file);
      	collection.updateOne(searchQuery, newDocument);

	}

}
